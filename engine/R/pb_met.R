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
  res$descriptive <- pb_descriptive(data, traits)
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
              models = if (!random) stats::setNames(list(m), mean), formulas = gsub("`", "", f1), random = random, method = "two-stage",
              descriptive = pb_descriptive(stage1, mean))
  res
}

# Combined analysis of variance across environments (fixed-effect linear model, as in Gomez & Gomez 1984), RCBD in
# every environment: y = Env + Rep(Env) + Genotype + Genotype x Env + error, fitted with stats::lm (sequential sums of
# squares, stats::anova). env_random = TRUE (the usual choice: sites are a sample of the target region): Env is tested
# against Rep(Env) and Genotype against Genotype x Env; FALSE: every term against the pooled error. Homogeneity of the
# error variances of the sites: Bartlett's test (stats::bartlett.test on the per-site RCBD models) and the ratio of the
# largest to the smallest error mean square. Genotype means, SE and the Tukey / Dunnett models use the same error term
# as the genotype F test: with env_random, a model of the genotype x environment cell means (y = Env + Genotype), whose
# residual is the interaction.
pb_combined_anova <- function(data, traits, geno, env, block, env_random = TRUE) {
  if (pb_empty(env)) stop("Choose the environment column.", call. = FALSE)
  pb_check_cols(data, traits, geno, block, env)
  data <- as.data.frame(data)
  q <- function(x) sprintf("`%s`", x)
  out <- list(info = list(), anova = list(), errors = list(), geno = list(), env = list(), genoenv = list(), diag = list(), models = list(), formulas = list())
  bart <- list()
  for (tr in traits) {
    d <- data; d[[tr]] <- pb_num(d[[tr]]); n_read <- nrow(d)
    d <- d[!is.na(d[[tr]]), , drop = FALSE]
    for (v in c(geno, env, block)) d[[v]] <- pb_factor(d[[v]])
    if (nlevels(d[[env]]) < 2) stop("The combined analysis needs at least two environments.", call. = FALSE)
    f1 <- sprintf("%s ~ %s + %s:%s + %s + %s:%s", q(tr), q(env), q(env), q(block), q(geno), q(geno), q(env))
    m <- stats::lm(stats::as.formula(f1), data = d)
    a <- as.data.frame(stats::anova(m))
    lab <- c(attr(stats::terms(m), "term.labels"), "Residuals")
    is_t <- function(x) which(gsub("`", "", lab) %in% gsub("`", "", x))
    iE <- is_t(env); iR <- is_t(c(paste0(env, ":", block), paste0(block, ":", env))); iG <- is_t(geno)
    iGE <- is_t(c(paste0(geno, ":", env), paste0(env, ":", geno))); iErr <- length(lab)
    src <- c("Environment", "Replicate within environment", "Genotype", "Genotype x Environment", "Pooled error")
    rows <- c(iE, iR, iG, iGE, iErr)
    den <- if (env_random) c(iR, iErr, iGE, iErr, NA) else c(iErr, iErr, iErr, iErr, NA)
    Fv <- a$`Mean Sq`[rows] / a$`Mean Sq`[den]
    pv <- stats::pf(Fv, a$Df[rows], a$Df[den], lower.tail = FALSE)
    an <- data.frame(Trait = tr, Source = src, Df = a$Df[rows], `Sum Sq` = a$`Sum Sq`[rows], `Mean Sq` = a$`Mean Sq`[rows],
                     `Tested against` = c(src[match(den[-5], rows)], ""), `F value` = Fv, `Pr(>F)` = pv, check.names = FALSE)
    an <- rbind(an, data.frame(Trait = tr, Source = "Total", Df = sum(a$Df), `Sum Sq` = sum(a$`Sum Sq`), `Mean Sq` = NA,
                               `Tested against` = "", `F value` = NA, `Pr(>F)` = NA, check.names = FALSE))
    # per-site error mean squares and Bartlett's test
    site <- lapply(levels(d[[env]]), function(e) stats::lm(stats::as.formula(sprintf("%s ~ %s + %s", q(tr), q(block), q(geno))), data = d[d[[env]] == e, , drop = FALSE]))
    er <- data.frame(Trait = tr, Environment = levels(d[[env]]), Df = vapply(site, stats::df.residual, 0),
                     `Error mean square` = vapply(site, function(s) sum(stats::resid(s)^2) / stats::df.residual(s), 0), check.names = FALSE)
    bt <- stats::bartlett.test(site)
    bart[[tr]] <- data.frame(Trait = tr, `Bartlett K-squared` = unname(bt$statistic), Df = unname(bt$parameter), `P` = bt$p.value,
                             `Largest / smallest error MS` = max(er$`Error mean square`) / min(er$`Error mean square`), check.names = FALSE)
    # model for the genotype means and comparisons, with the error term of the genotype F test
    if (env_random) {
      cm <- stats::aggregate(d[[tr]], list(d[[geno]], d[[env]]), mean); names(cm) <- c(geno, env, tr)
      mc <- stats::lm(stats::as.formula(sprintf("%s ~ %s + %s", q(tr), q(env), q(geno))), data = cm)
    } else mc <- m
    emm <- suppressMessages(emmeans::emmeans(mc, geno))
    gm <- as.data.frame(emm); gm <- data.frame(Genotype = as.character(gm[[geno]]), Mean = gm$emmean, SE = gm$SE)
    V <- stats::vcov(emm); vv <- diag(V); sed <- sqrt(outer(vv, vv, "+") - 2 * V)[upper.tri(V)]
    mse <- a$`Mean Sq`[iErr]; gmean <- mean(d[[tr]])
    em <- stats::aggregate(d[[tr]], list(Environment = d[[env]]), function(x) c(N = length(x), Mean = mean(x), SD = stats::sd(x), Min = min(x), Max = max(x)))
    em <- data.frame(Environment = em$Environment, em$x)
    ge <- stats::aggregate(d[[tr]], list(d[[geno]], d[[env]]), mean)
    out$info[[tr]] <- data.frame(Trait = tr, `N read` = n_read, `N used` = nrow(d), Environments = nlevels(d[[env]]), Genotypes = nlevels(d[[geno]]),
      `Reps per genotype` = pb_harmonic(table(d[[geno]])), `Grand mean` = gmean, `CV (%)` = 100 * sqrt(mse) / gmean,
      `Average SED` = mean(sed), `LSD (5%)` = stats::qt(0.975, stats::df.residual(mc)) * mean(sed), check.names = FALSE)
    out$anova[[tr]] <- an
    out$errors[[tr]] <- er
    out$geno[[tr]] <- cbind(Trait = tr, gm)
    out$env[[tr]] <- cbind(Trait = tr, em)
    out$genoenv[[tr]] <- data.frame(Trait = tr, Genotype = as.character(ge[[1]]), Environment = as.character(ge[[2]]), Mean = ge$x)
    out$diag[[tr]] <- pb_diag_frame(m, tr)
    out$models[[tr]] <- mc
    out$formulas[[tr]] <- gsub("`", "", f1)
  }
  res <- lapply(out[c("info", "anova", "errors", "geno", "env", "genoenv", "diag")], pb_bind)
  res$bartlett <- pb_bind(bart)
  res$models <- out$models; res$formulas <- unlist(out$formulas); res$random <- FALSE; res$env_random <- env_random
  res$method <- "combined ANOVA"; res$descriptive <- pb_descriptive(data, traits)
  res
}
