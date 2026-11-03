# Structured front end for the ported PBTools analyses (used by the Shiny app).
# Each function runs the original routine and returns plain data frames instead of printed text.

`%||%` <- function(a, b) if (is.null(a)) b else a

pb_quiet <- function(expr) {
  # the original routines print descriptive statistics and lme4 messages; hide them in the app
  utils::capture.output(res <- suppressMessages(suppressWarnings(expr)))
  res
}

pb_read_table <- function(path, name = path) {
  ext <- tolower(tools::file_ext(name))
  if (ext %in% c("xlsx", "xls")) as.data.frame(readxl::read_excel(path))
  else if (ext == "txt") utils::read.delim(path, check.names = FALSE, na.strings = c("NA", "", "."))
  else utils::read.csv(path, check.names = FALSE, na.strings = c("NA", "", "."))
}

pb_design_labels <- c(RCB = "RCB (randomized complete block)", Alpha = "Alpha-lattice", RowCol = "Row-column",
                      AugRCB = "Augmented RCB", AugLS = "Augmented latin square",
                      LatinAlpha = "Latinized alpha-lattice", LatinRowCol = "Latinized row-column")

pb_check_cols <- function(data, ...) {
  cols <- unlist(list(...)); cols <- cols[!is.null(cols) & nzchar(cols)]
  miss <- setdiff(cols, names(data))
  if (length(miss)) stop("Column not found in the data: ", paste(miss, collapse = ", "), call. = FALSE)
  if (anyDuplicated(cols)) stop("The same column was chosen for two different roles.", call. = FALSE)
  invisible(TRUE)
}

pb_num <- function(x) suppressWarnings(as.numeric(as.character(x)))
pb_null_if_empty <- function(x) if (is.null(x) || !length(x) || !nzchar(x[1])) NULL else x

pb_bind <- function(l) {
  l <- l[!vapply(l, is.null, NA)]
  if (!length(l)) return(NULL)
  out <- do.call(rbind, unname(lapply(l, function(x) { rownames(x) <- NULL; x })))
  rownames(out) <- NULL
  out
}

# ---- single environment ---------------------------------------------------------------------------------------
pb_ssa <- function(data, resp, geno, design = "RCB", block, column = NULL, rep = NULL, env = NULL, random = FALSE, exclude_check = FALSE) {
  column <- pb_null_if_empty(column); rep <- pb_null_if_empty(rep); env <- pb_null_if_empty(env)
  pb_check_cols(data, resp, geno, block, column, rep, env)
  data <- as.data.frame(data)
  for (v in resp) data[[v]] <- pb_num(data[[v]])
  r <- pb_quiet(ssa.test(design, data, resp, geno, block, column = column, rep = rep, env = env, is.random = random, excludeCheck = isTRUE(exclude_check) && random && design %in% c("AugRCB", "AugLS")))
  rows <- list(); vc <- list(); lrt <- list(); mn <- list(); dg <- list()
  for (o in r$output) for (s in o$site) {
    e <- s$env
    rows[[length(rows) + 1]] <- data.frame(Trait = o$respvar, Environment = e, `Obs. read` = s$obsread, `Obs. used` = s$obsused,
      Reps = round(as.numeric(s$numreps), 2), `Error variance` = if (!is.null(s$varcompnRep)) s$varcompnRep[[1]] else NA_real_,
      Heritability = if (random && !is.null(s$heritability)) as.numeric(s$heritability) else NA_real_, check.names = FALSE)
    if (!is.null(s$varcomp.table)) vc[[length(vc) + 1]] <- cbind(Trait = o$respvar, Environment = e, as.data.frame(s$varcomp.table))
    if (!is.null(s$models.table)) lrt[[length(lrt) + 1]] <- cbind(Trait = o$respvar, Environment = e, Model = rownames(s$models.table), as.data.frame(s$models.table))
    ms <- s$summary.statistic
    if (!is.null(ms)) { ms <- as.data.frame(ms); names(ms)[1:2] <- c("Genotype", "Mean"); mn[[length(mn) + 1]] <- cbind(Trait = o$respvar, Environment = e, ms) }
    dg[[length(dg) + 1]] <- pb_diag_frame(o$respvar, e, s$fitted.values, s$residuals)
  }
  # fitted models (genotype fixed only) keyed "trait | environment", for the pairwise comparisons
  models <- list()
  if (!random) for (o in r$output) for (s in o$site) if (!is.null(s$model)) models[[paste(o$respvar, "|", s$env)]] <- s$model
  list(summary = pb_bind(rows), varcomp = pb_bind(vc), lrt = pb_bind(lrt), means = pb_bind(mn), random = random, models = models, diag = pb_bind(dg), raw = r)
}

# stage-1 table for the two-stage analysis: BLUEs, their SE, error variance and number of reps per environment
pb_stage1 <- function(data, resp, geno, design = "RCB", block, column = NULL, rep = NULL, env) {
  r <- pb_ssa(data, resp, geno, design, block, column, rep, env, random = FALSE)$raw
  if (is.null(r$meansse) || is.null(r$varrep)) stop("The single-environment analysis returned no means.", call. = FALSE)
  m <- r$meansse; names(m)[names(m) == env] <- "Env"
  v <- r$varrep; names(v)[names(v) == env] <- "Env"
  out <- merge(m, v, by = "Env")
  names(out)[names(out) == geno] <- "Genotype"
  keep <- c("Env", "Genotype", grep("_Mean$|_PredMean$|StdErrMean$|sigma2$|No[.]rep$", names(out), value = TRUE))
  out <- out[, unique(keep)]
  out[order(out$Env, out$Genotype), ]
}

# ---- multi environment -----------------------------------------------------------------------------------------
pb_collect_multi <- function(outputs, random) {
  one <- function(o) {
    tab <- function(x) if (!is.null(x)) cbind(Trait = o$respvar, as.data.frame(x)) else NULL
    lrt <- NULL
    for (nm in c("testsig.Geno", "testsig.Env", "testsig.GenoEnv")) if (!is.null(o[[nm]])) {
      x <- as.data.frame(o[[nm]])
      if ("Chi Df" %in% names(x)) { x <- x[, setdiff(names(x), "Df")]; names(x)[names(x) == "Chi Df"] <- "Df" }
      lrt <- rbind(lrt, cbind(Test = sub("testsig[.]", "", nm), Model = rownames(x), x))
    }
    if (!is.null(lrt)) lrt <- cbind(Trait = o$respvar, lrt)
    list(varcomp = tab(o$varcomp.table), lrt = lrt, geno = tab(o$means.Geno), env = tab(o$means.Env), genoenv = tab(o$means.GenoEnv),
         info = data.frame(Trait = o$respvar, `Obs. used` = o$obsused,
                           `Harmonic mean of reps` = if (!is.null(o$harmonicMean)) o$harmonicMean else NA_real_,
                           Heritability = if (random && !is.null(o$heritability)) as.numeric(o$heritability) else NA_real_, check.names = FALSE))
  }
  parts <- lapply(outputs, one)
  g <- function(k) pb_bind(lapply(parts, `[[`, k))
  models <- stats::setNames(lapply(outputs, function(o) o$model), vapply(outputs, function(o) o$respvar, ""))
  list(info = g("info"), varcomp = g("varcomp"), lrt = g("lrt"), geno = g("geno"), env = g("env"), genoenv = g("genoenv"), random = random,
       models = if (random) NULL else Filter(Negate(is.null), models), diag = pb_bind(lapply(outputs, function(o) pb_diag_frame(o$respvar, NA, o$fitted.values, o$residuals))))
}

pb_one_stage <- function(data, resp, geno, design = "RCB", block, column = NULL, rep = NULL, env, random = FALSE) {
  column <- pb_null_if_empty(column); rep <- pb_null_if_empty(rep)
  pb_check_cols(data, resp, geno, block, column, rep, env)
  data <- as.data.frame(data); for (v in resp) data[[v]] <- pb_num(data[[v]])
  r <- pb_quiet(GEOneStage.test(design, data, resp, geno, block, column = column, rep = rep, env = env, is.genoRandom = random))
  pb_collect_multi(r$output, random)
}

pb_two_stage <- function(stage1, resp, stderr = NULL, sigma2, numrep, geno = "Genotype", env = "Env",
                         weight = c("none", "stderr"), random = FALSE) {
  weight <- match.arg(weight)
  stderr <- pb_null_if_empty(stderr)
  pb_check_cols(stage1, resp, stderr, sigma2, numrep, geno, env)
  stage1 <- as.data.frame(stage1); for (v in c(resp, stderr, sigma2, numrep)) stage1[[v]] <- pb_num(stage1[[v]])
  if (weight == "stderr" && is.null(stderr)) stop("Weighting by standard error needs the standard-error column.", call. = FALSE)
  r <- pb_quiet(GETwoStage.test(stage1, resp, if (weight == "none") NULL else stderr, sigma2, numrep, geno, env, weight = weight, is.genoRandom = random))
  pb_collect_multi(r, random)
}

# ---- GxE --------------------------------------------------------------------------------------------------------
pb_stability <- function(stage1, resp, geno = "Genotype", env = "Env", method = c("regression", "shukla")) {
  method <- match.arg(method); pb_check_cols(stage1, resp, geno, env)
  stage1 <- as.data.frame(stage1); stage1[[resp]] <- pb_num(stage1[[resp]])
  r <- pb_quiet(stability.analysis(stage1, resp, geno, env, method = method))
  s <- r[[1]][[1]]
  if (!is.null(s$error)) stop(s$error, call. = FALSE)
  st <- as.data.frame(s$stability); st <- cbind(Genotype = rownames(st), st); rownames(st) <- NULL
  gm <- stats::aggregate(stage1[[resp]], list(Genotype = as.character(stage1[[geno]])), mean, na.rm = TRUE); names(gm)[2] <- "Mean"
  list(table = merge(gm, st, by = "Genotype"), method = r$method, data = stage1, resp = resp, geno = geno, env = env)
}

# multiplicative models on stage-1 means (AMMI and GGE); sigma2 and reps per row as in the two-stage analysis
pb_multiplicative <- function(stage1, resp, sigma2, numrep, geno = "Genotype", env = "Env", model = c("AMMI", "GGE")) {
  model <- match.arg(model); pb_check_cols(stage1, resp, sigma2, numrep, geno, env)
  d <- as.data.frame(stage1)
  d <- d[stats::complete.cases(d[, c(resp, sigma2, numrep, geno, env)]), ]
  f <- if (model == "AMMI") ammi.analysis2 else gge.analysis2
  grDevices::pdf(NULL); on.exit(grDevices::dev.off())
  r <- pb_quiet(f(d[[env]], d[[geno]], pb_num(d[[numrep]]), pb_num(d[[resp]]), pb_num(d[[sigma2]]), number = FALSE, respVar = resp))
  bp <- r$biplot; bp <- cbind(Name = rownames(bp), bp); rownames(bp) <- NULL
  list(model = model, analysis = cbind(PC = rownames(r$analysis), r$analysis), biplot = bp, means = r$means, resp = resp)
}

# ---- mean comparisons ---------------------------------------------------------------------------------------------
# Pairwise comparisons of genotype means as PBTools does them: Tukey (all pairs) or Dunnett (against one or more
# control genotypes), on a model fitted with genotype fixed. Like PBTools, only the pairs whose confidence
# interval excludes 0 are listed.
pb_pairwise <- function(models, type = c("Tukey", "Dunnett"), alpha = 0.05, control = NULL) {
  type <- match.arg(type)
  if (!length(models)) stop("There is no fitted model with genotype as a fixed effect. Run the analysis with genotype = Fixed first.", call. = FALSE)
  control <- control[nzchar(control)]
  if (type == "Dunnett" && !length(control)) stop("Choose at least one control genotype for Dunnett's test.", call. = FALSE)
  rows <- list(); none <- character(); warn <- character()
  for (nm in names(models)) {
    r <- pb_quiet(ssa.pairwise(models[[nm]], type = type, alpha = alpha, control.level = if (type == "Dunnett") control else NULL))
    if (!identical(r$controlTestWarning, "NONE") && !is.null(r$controlTestWarning)) warn <- c(warn, r$controlTestWarning)
    if (is.data.frame(r$result)) {
      d <- r$result; names(d) <- c("Genotype i", "Genotype j", "Difference", "Lower", "Upper")
      d[["Genotype i"]] <- as.character(d[["Genotype i"]]); d[["Genotype j"]] <- as.character(d[["Genotype j"]])
      rows[[nm]] <- cbind(Model = nm, d)
    } else none <- c(none, nm)
  }
  list(table = pb_bind(rows), none = none, warning = unique(warn), type = type, alpha = alpha)
}

# ---- diagnostics ---------------------------------------------------------------------------------------------------
# residuals and fitted values of the fitted model, one row per observation (Environment is NA when not applicable)
pb_diag_frame <- function(trait, env, fitted, resid) {
  if (is.null(fitted) || is.null(resid) || !length(resid)) return(NULL)
  data.frame(Trait = trait, Environment = if (is.null(env)) NA_character_ else as.character(env), Fitted = as.numeric(fitted), Residual = as.numeric(resid), stringsAsFactors = FALSE)
}

# The three plots of PBTools (graph.sea.diagplots): residuals against fitted values, normal Q-Q plot of the residuals,
# histogram of the residuals, with a note in the fourth panel. Drawn with base graphics, as PBTools does.
pb_diag_plot <- function(diag, note = NULL) {
  op <- graphics::par(mfrow = c(2, 2), mar = c(4.2, 4.2, 3.2, 1)); on.exit(graphics::par(op))
  cc <- grDevices::rgb(0.3, 0.3, 0.7, 0.45)
  graphics::plot(diag$Fitted, diag$Residual, pch = 15, cex = 0.8, col = cc, xlab = "Predicted values", ylab = "Residuals", main = "Residuals against fitted values")
  graphics::abline(h = 0, lty = 2, col = "grey50")
  stats::qqnorm(diag$Residual, pch = 15, cex = 0.8, col = cc, main = "Normal Q-Q plot of residuals"); stats::qqline(diag$Residual, col = 2)
  graphics::hist(diag$Residual, main = "Histogram of residuals", col = cc, xlab = "Residual", ylab = "Frequency")
  graphics::plot(0:1, 0:1, type = "n", axes = FALSE, xlab = "", ylab = "")
  sw <- if (length(diag$Residual) >= 3 && length(diag$Residual) <= 5000) tryCatch(stats::shapiro.test(diag$Residual), error = function(e) NULL) else NULL
  txt <- c(if (!is.null(note)) paste(strwrap(note, 34), collapse = "\n"),
           if (!is.null(sw)) sprintf("Shapiro-Wilk test of the residuals: W = %.3f, p = %s", sw$statistic, format.pval(sw$p.value, digits = 3)))
  graphics::text(0.5, 0.5, paste(txt, collapse = "\n\n"), cex = 0.95)
  invisible(NULL)
}
