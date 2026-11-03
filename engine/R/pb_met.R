# Multi-environment analysis (PBTools: Analysis > Multi-environment), fitted with lme4 and emmeans.
# One-stage: all plots in one model with the PBTools design terms nested in environment, environment and
# genotype x environment random. Two-stage: the stage-1 genotype means of each environment (pb_stage1) in a second
# model with environment random, optionally weighted by 1 / SE^2.

# Random design terms nested in environment, exactly as in PBTools GEOneStage.test
pb_met_terms <- function(design, block, column = NULL, rep = NULL, env, longer_row = FALSE) {
  q <- function(x) paste0("`", x, "`")
  E <- q(env); b <- q(block); k <- if (!is.null(column)) q(column); r <- if (!is.null(rep)) q(rep)
  t <- function(...) sprintf("(1|%s)", paste(c(...), collapse = ":"))
  switch(design,
    RCB = , AugRCB = c(t(E), t(b, E)),
    AugLS = c(t(E), t(b, E), t(k, E)),
    Alpha = c(t(E), t(r, E), t(r, b, E)),
    RowCol = c(t(E), t(r, E), t(r, b, E), t(r, k, E)),
    LatinAlpha = c(t(E), t(r, E), t(b, E), t(r, b, E)),
    LatinRowCol = if (longer_row) c(t(E), t(r, E), t(k, E), t(r, k, E), t(b, E)) else c(t(E), t(r, E), t(b, E), t(r, b, E), t(k, E)),
    stop("Unknown design: ", design, call. = FALSE))
}

pb_one_stage <- function(data, traits, geno, env, design = "RCB", block, column = NULL, rep = NULL, random = FALSE) {
  column <- if (pb_needs_column(design)) pb_null_if_empty(column)
  rep <- if (pb_needs_rep(design)) pb_null_if_empty(rep)
  if (pb_empty(env)) stop("Choose the environment column.", call. = FALSE)
  pb_check_cols(data, traits, geno, block, column, rep, env)
  data <- as.data.frame(data)
  out <- list(info = list(), varcomp = list(), lrt = list(), geno = list(), env = list(), genoenv = list(), diag = list(), models = list(), formulas = list())
  for (tr in traits) {
    d <- data; d[[tr]] <- pb_num(d[[tr]]); n_read <- nrow(d)
    d <- d[!is.na(d[[tr]]), , drop = FALSE]
    for (v in c(geno, env, block, column, rep)) d[[v]] <- pb_factor(d[[v]])
    if (nlevels(d[[env]]) < 2) stop("The one-stage analysis needs at least two environments.", call. = FALSE)
    longer_row <- design == "LatinRowCol" && nlevels(d[[block]]) > nlevels(d[[column]])
    g <- sprintf("`%s`", geno); y <- sprintf("`%s`", tr); ge <- sprintf("(1|%s:%s)", g, sprintf("`%s`", env)); E <- sprintf("(1|`%s`)", env)
    terms <- pb_met_terms(design, block, column, rep, env, longer_row)
    gt <- if (random) sprintf("(1|%s)", g) else g
    f1 <- paste(y, "~ 1 +", gt, "+", paste(c(terms, ge), collapse = " + "))
    m <- pb_fit(f1, d)
    drop_term <- function(f, term) gsub(paste0(" + ", term), "", f, fixed = TRUE)
    m_g <- pb_fit(drop_term(f1, gt), d)
    lr <- rbind(pb_lrt(m_g, m, refit = !random, label = "Genotype"),
                pb_lrt(pb_fit(drop_term(f1, E), d), m, label = "Environment"),
                pb_lrt(pb_fit(drop_term(f1, ge), d), m, label = "Genotype x Environment"))
    vc <- pb_varcomp(m)
    n_env <- nlevels(d[[env]])
    reps <- pb_harmonic(table(d[[geno]]))           # PBTools: replicates per genotype over all environments
    h2 <- NA_real_
    if (random) {
      s2g <- vc$Variance[vc$Group == geno]; s2ge <- vc$Variance[vc$Group == paste0(geno, ":", env)]; s2e <- vc$Variance[vc$Group == "Residual"]
      h2 <- s2g / (s2g + s2ge / n_env + s2e / (reps * n_env))
      gm <- pb_blup(m, geno)
    } else {
      gm <- pb_blue(m, geno); out$models[[tr]] <- m
    }
    # genotype x environment means: model prediction without the design terms (as PBTools: intercept + genotype +
    # environment + genotype x environment effects)
    nd <- expand.grid(levels(d[[geno]]), levels(d[[env]]), stringsAsFactors = FALSE); names(nd) <- c(geno, env)
    re <- paste("~", E, "+", ge, if (random) paste("+", gt))
    nd$Mean <- stats::predict(m, newdata = nd, re.form = stats::as.formula(re), allow.new.levels = TRUE)
    em <- stats::aggregate(d[[tr]], list(Environment = d[[env]]), function(x) c(N = length(x), Mean = mean(x), SD = stats::sd(x), Min = min(x), Max = max(x)))
    em <- data.frame(Environment = em$Environment, em$x)
    out$info[[tr]] <- data.frame(Trait = tr, `N read` = n_read, `N used` = nrow(d), Environments = n_env, Genotypes = nlevels(d[[geno]]),
                                 `Reps per genotype` = reps, Heritability = h2, check.names = FALSE)
    out$varcomp[[tr]] <- cbind(Trait = tr, vc)
    out$lrt[[tr]] <- cbind(Trait = tr, lr)
    out$geno[[tr]] <- cbind(Trait = tr, gm)
    out$env[[tr]] <- cbind(Trait = tr, em)
    out$genoenv[[tr]] <- data.frame(Trait = tr, Genotype = nd[[geno]], Environment = nd[[env]], Mean = nd$Mean)
    out$diag[[tr]] <- pb_diag_frame(m, tr)
    out$formulas[[tr]] <- gsub("`", "", f1)
  }
  res <- lapply(out[c("info", "varcomp", "lrt", "geno", "env", "genoenv", "diag")], pb_bind)
  res$models <- out$models; res$formulas <- unlist(out$formulas); res$random <- random; res$method <- "one-stage"
  res
}

# Two-stage: `stage1` has one row per genotype and environment (e.g. from pb_stage1)
pb_two_stage <- function(stage1, mean = "Mean", se = "SE", error_var = "ErrorVariance", reps = "Reps", geno = "Genotype", env = "Env",
                         weight = c("none", "stderr"), random = FALSE) {
  weight <- match.arg(weight)
  if (weight == "stderr" && pb_empty(se)) stop("Weighting by 1/SE^2 needs the SE column.", call. = FALSE)
  pb_check_cols(stage1, mean, if (weight == "stderr") se, error_var, reps, geno, env)
  d <- as.data.frame(stage1)
  for (v in c(mean, error_var, reps, if (weight == "stderr") se)) d[[v]] <- pb_num(d[[v]])
  d <- d[!is.na(d[[mean]]), , drop = FALSE]
  d[[geno]] <- pb_factor(d[[geno]]); d[[env]] <- pb_factor(d[[env]])
  w <- if (weight == "stderr") 1 / d[[se]]^2 else NULL
  g <- sprintf("`%s`", geno); E <- sprintf("(1|`%s`)", env); y <- sprintf("`%s`", mean)
  gt <- if (random) sprintf("(1|%s)", g) else g
  f1 <- paste(y, "~ 1 +", gt, "+", E)
  m <- pb_fit(f1, d, weights = w)
  vc <- pb_varcomp(m)
  n_env <- nlevels(d[[env]])
  rep_env <- tapply(d[[reps]], d[[env]], base::mean); err_env <- tapply(d[[error_var]], d[[env]], base::mean)
  num_rep <- pb_harmonic(rep_env)
  pooled_err <- sum(err_env * rep_env) / sum(rep_env)
  lr <- NULL; h2 <- NA_real_
  if (random) {
    lr <- rbind(pb_lrt(pb_fit(paste(y, "~ 1 +", E), d, weights = w), m, label = "Genotype"),
                pb_lrt(pb_fit(paste(y, "~ 1 +", gt), d, weights = w), m, label = "Environment"))
    s2g <- vc$Variance[vc$Group == geno]
    s2ge <- if (weight == "none") vc$Variance[vc$Group == "Residual"] - pooled_err else 1   # as PBTools
    h2 <- s2g / (s2g + s2ge / n_env + pooled_err / (num_rep * n_env))
    gm <- pb_blup(m, geno)
  } else gm <- pb_blue(m, geno)
  em <- stats::aggregate(d[[mean]], list(Environment = d[[env]]), base::mean); names(em)[2] <- "Mean"
  res <- list(info = data.frame(Trait = mean, `N used` = nrow(d), Environments = n_env, Genotypes = nlevels(d[[geno]]),
                                `Reps (harmonic mean over environments)` = num_rep, `Pooled error variance` = pooled_err,
                                Weight = if (weight == "stderr") "1/SE^2" else "none", Heritability = h2, check.names = FALSE),
              varcomp = cbind(Trait = mean, vc), lrt = if (!is.null(lr)) cbind(Trait = mean, lr), geno = cbind(Trait = mean, gm),
              env = cbind(Trait = mean, em), genoenv = NULL, diag = pb_diag_frame(m, mean),
              models = if (!random) stats::setNames(list(m), mean), formulas = gsub("`", "", f1), random = random, method = "two-stage")
  res
}
