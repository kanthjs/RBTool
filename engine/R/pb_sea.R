# Single-environment analysis (PBTools: Analysis > Single environment).
# Model per trait and environment, fitted with lme4 (REML), with the design terms PBTools uses for each of its seven
# designs. Genotype fixed: BLUEs and their SE from emmeans. Genotype random: BLUPs (intercept + random effect, as
# PBTools reports them), a likelihood ratio test of the genotype variance (REML fits, lme4 anova) and the PBTools
# heritability H2 = s2g / (s2g + s2e / r), r = harmonic mean of the number of replicates per genotype.

pb_sea <- function(data, traits, geno, design = "RCB", block, column = NULL, rep = NULL, env = NULL,
                   random = FALSE, exclude_check = FALSE, check_list = NULL) {
  column <- if (pb_needs_column(design)) pb_null_if_empty(column)
  rep <- if (pb_needs_rep(design)) pb_null_if_empty(rep)
  env <- pb_null_if_empty(env)
  if (pb_needs_column(design) && is.null(column)) stop("This design needs a column variable.", call. = FALSE)
  if (pb_needs_rep(design) && is.null(rep)) stop("This design needs a replicate variable.", call. = FALSE)
  pb_check_cols(data, traits, geno, block, column, rep, env)
  data <- as.data.frame(data)
  desc <- pb_descriptive(data, traits, env)
  if (is.null(env)) { env <- ".env"; data[[env]] <- "1" }
  data[[env]] <- pb_factor(data[[env]])
  exclude_check <- isTRUE(exclude_check) && random && design %in% c("AugRCB", "AugLS")

  out <- list(summary = list(), varcomp = list(), lrt = list(), means = list(), diag = list(), formulas = list(), models = list(), sed = list())
  for (tr in traits) for (e in levels(data[[env]])) {
    d <- data[data[[env]] == e, , drop = FALSE]
    n_read <- nrow(d)
    d[[tr]] <- pb_num(d[[tr]])
    d <- d[!is.na(d[[tr]]), , drop = FALSE]
    for (v in c(geno, block, column, rep)) d[[v]] <- pb_factor(d[[v]])
    if (nrow(d) < 3 || nlevels(d[[geno]]) < 2) stop(sprintf("Too few observations of %s in environment %s.", tr, e), call. = FALSE)
    reps <- pb_harmonic(table(d[[geno]]))
    longer_row <- FALSE
    if (design == "LatinRowCol") {
      if (any(table(d[[block]], d[[column]]) > 1)) stop("The levels of the row/column variable should be continuous across replicates.", call. = FALSE)
      longer_row <- nlevels(d[[block]]) > nlevels(d[[column]])
    }
    terms <- pb_design_terms(design, block, column, rep, longer_row)
    y <- sprintf("`%s`", tr); g <- sprintf("`%s`", geno)
    key <- paste(tr, "|", e)

    if (random) {
      if (exclude_check) {
        checks <- if (length(check_list)) intersect(trimws(check_list), levels(d[[geno]])) else names(which(table(d[[geno]]) > 1))
        if (!length(checks)) stop("No check genotype found (a check must appear more than once).", call. = FALSE)
        isck <- as.character(d[[geno]]) %in% checks
        d$Test <- factor(ifelse(isck, "0", as.character(d[[geno]]))); d$Check <- factor(ifelse(isck, as.character(d[[geno]]), "0"))
        f1 <- paste(y, "~ 1 + Check +", paste(terms, collapse = " + "), "+ (1|Test:Check)")
        f0 <- paste(y, "~ 1 + Check +", paste(terms, collapse = " + "))
        gterm <- "Test:Check"
      } else {
        f1 <- paste(y, "~ 1 +", paste(terms, collapse = " + "), "+", sprintf("(1|%s)", g))
        f0 <- paste(y, "~ 1 +", paste(terms, collapse = " + "))
        gterm <- geno
      }
      m <- pb_fit(f1, d); m0 <- pb_fit(f0, d)
      vc <- pb_varcomp(m)
      s2g <- vc$Variance[vc$Group == gterm]; s2e <- vc$Variance[vc$Group == "Residual"]
      h2 <- s2g / (s2g + s2e / reps)
      lr <- pb_lrt(m0, m, refit = FALSE, label = "Genotype")
      bl <- pb_blup(m, gterm)
      if (exclude_check) { bl <- bl[!startsWith(bl$Genotype, "0:"), ]; bl$Genotype <- sub(":0$", "", bl$Genotype) }
      out$lrt[[key]] <- cbind(Trait = tr, Environment = e, lr)
      out$means[[key]] <- cbind(Trait = tr, Environment = e, bl)
      out$summary[[key]] <- data.frame(Trait = tr, Environment = e, `N read` = n_read, `N used` = nrow(d), Reps = reps,
        `Genotype variance` = s2g, `Error variance` = s2e, Heritability = h2, `LRT p-value` = lr$P[2], check.names = FALSE)
    } else {
      f1 <- paste(y, "~ 1 +", g, "+", paste(terms, collapse = " + "))
      m <- pb_fit(f1, d)
      vc <- pb_varcomp(m)
      # SE from the model covariance (asymptotic), as PBTools; emmeans would otherwise apply the Kenward-Roger adjustment
      emm <- emmeans::emmeans(m, geno, lmer.df = "asymptotic")
      bl <- as.data.frame(emm); bl <- data.frame(Genotype = as.character(bl[[geno]]), Mean = bl$emmean, SE = bl$SE)
      V <- stats::vcov(emm); vv <- diag(V); sed <- sqrt(outer(vv, vv, "+") - 2 * V)[upper.tri(V)]
      out$means[[key]] <- cbind(Trait = tr, Environment = e, bl)
      out$models[[key]] <- m
      out$sed[[key]] <- data.frame(Trait = tr, Environment = e, Minimum = min(sed), Average = mean(sed), Maximum = max(sed))
      out$summary[[key]] <- data.frame(Trait = tr, Environment = e, `N read` = n_read, `N used` = nrow(d), Reps = reps,
        `Error variance` = vc$Variance[vc$Group == "Residual"], `Average SED` = mean(sed), check.names = FALSE)
    }
    out$varcomp[[key]] <- cbind(Trait = tr, Environment = e, vc)
    out$diag[[key]] <- pb_diag_frame(m, tr, e)
    out$formulas[[key]] <- gsub("`", "", f1)
  }
  res <- lapply(out[c("summary", "varcomp", "lrt", "means", "diag", "sed")], pb_bind)
  res$models <- out$models; res$formulas <- unlist(out$formulas); res$descriptive <- desc
  res$random <- random; res$design <- design; res$single_env <- identical(env, ".env")
  if (res$single_env) for (k in c("summary", "varcomp", "lrt", "means", "diag", "sed")) if (!is.null(res[[k]])) res[[k]]$Environment <- NULL
  res
}

# Standard (fixed-effect) analysis of variance of an RCBD, per trait and environment: y = block + genotype + error,
# fitted with stats::lm (blocks fixed), sequential sums of squares (stats::anova; block first, so genotype is adjusted
# for blocks when plots are missing). Means and SE from emmeans; LSD = t(0.975, error df) x average SED; CV = sqrt(MSE) / mean.
# The lm models feed the same Tukey / Dunnett comparisons (emmeans or multcomp) as the mixed-model analysis.
pb_sea_anova <- function(data, traits, geno, block, env = NULL) {
  env <- pb_null_if_empty(env)
  pb_check_cols(data, traits, geno, block, env)
  data <- as.data.frame(data)
  desc <- pb_descriptive(data, traits, env)
  if (is.null(env)) { env <- ".env"; data[[env]] <- "1" }
  data[[env]] <- pb_factor(data[[env]])
  out <- list(summary = list(), anova = list(), means = list(), diag = list(), formulas = list(), models = list(), sed = list())
  for (tr in traits) for (e in levels(data[[env]])) {
    d <- data[data[[env]] == e, , drop = FALSE]; n_read <- nrow(d)
    d[[tr]] <- pb_num(d[[tr]]); d <- d[!is.na(d[[tr]]), , drop = FALSE]
    for (v in c(geno, block)) d[[v]] <- pb_factor(d[[v]])
    if (nrow(d) < 3 || nlevels(d[[geno]]) < 2) stop(sprintf("Too few observations of %s in environment %s.", tr, e), call. = FALSE)
    f1 <- sprintf("`%s` ~ `%s` + `%s`", tr, block, geno)
    m <- stats::lm(stats::as.formula(f1), data = d)
    a <- as.data.frame(stats::anova(m))
    an <- data.frame(Trait = tr, Environment = e, Source = c("Block", "Genotype", "Error"), Df = a$Df, `Sum Sq` = a$`Sum Sq`,
                     `Mean Sq` = a$`Mean Sq`, `F value` = a$`F value`, `Pr(>F)` = a$`Pr(>F)`, check.names = FALSE)
    an <- rbind(an, data.frame(Trait = tr, Environment = e, Source = "Total", Df = sum(a$Df), `Sum Sq` = sum(a$`Sum Sq`),
                               `Mean Sq` = NA, `F value` = NA, `Pr(>F)` = NA, check.names = FALSE))
    emm <- emmeans::emmeans(m, geno)
    bl <- as.data.frame(emm); bl <- data.frame(Genotype = as.character(bl[[geno]]), Mean = bl$emmean, SE = bl$SE)
    V <- stats::vcov(emm); vv <- diag(V); sed <- sqrt(outer(vv, vv, "+") - 2 * V)[upper.tri(V)]
    mse <- a$`Mean Sq`[3]; dfe <- a$Df[3]; gm <- mean(d[[tr]])
    key <- paste(tr, "|", e)
    out$anova[[key]] <- an
    out$means[[key]] <- cbind(Trait = tr, Environment = e, bl)
    out$models[[key]] <- m
    out$sed[[key]] <- data.frame(Trait = tr, Environment = e, Minimum = min(sed), Average = mean(sed), Maximum = max(sed))
    out$summary[[key]] <- data.frame(Trait = tr, Environment = e, `N read` = n_read, `N used` = nrow(d), Reps = pb_harmonic(table(d[[geno]])),
      `Grand mean` = gm, `Error mean square` = mse, `CV (%)` = 100 * sqrt(mse) / gm, `F (genotype)` = a$`F value`[2], `P (genotype)` = a$`Pr(>F)`[2],
      `Average SED` = mean(sed), `LSD (5%)` = stats::qt(0.975, dfe) * mean(sed), check.names = FALSE)
    out$diag[[key]] <- pb_diag_frame(m, tr, e)
    out$formulas[[key]] <- gsub("`", "", f1)
  }
  res <- lapply(out[c("summary", "anova", "means", "diag", "sed")], pb_bind)
  res$models <- out$models; res$formulas <- unlist(out$formulas); res$descriptive <- desc
  res$random <- FALSE; res$design <- "RCB"; res$method <- "anova"; res$single_env <- identical(env, ".env")
  if (res$single_env) for (k in c("summary", "anova", "means", "diag", "sed")) if (!is.null(res[[k]])) res[[k]]$Environment <- NULL
  res
}

# Stage-1 table for the two-stage and stability analyses: per environment the BLUEs of the genotypes (genotype fixed),
# their SE, the error variance and the harmonic mean of the number of replicates, as PBTools builds it.
pb_stage1 <- function(data, trait, geno, design = "RCB", block, column = NULL, rep = NULL, env) {
  if (pb_empty(env)) stop("The stage-1 table needs an environment column.", call. = FALSE)
  r <- pb_sea(data, trait, geno, design, block, column, rep, env, random = FALSE)
  s <- r$summary[, c("Environment", "Error variance", "Reps")]
  out <- merge(r$means[, c("Environment", "Genotype", "Mean", "SE")], s, by = "Environment")
  names(out) <- c("Env", "Genotype", "Mean", "SE", "ErrorVariance", "Reps")
  out[order(out$Env, out$Genotype), ]
}
