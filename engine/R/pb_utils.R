# Shared helpers for the analyses. The statistics themselves come from published packages (lme4, emmeans,
# multcomp, nlme, metan); these helpers only prepare data, build the PBTools model formulas and tidy results.

`%||%` <- function(a, b) if (is.null(a)) b else a

pb_designs <- c(RCB = "Randomized complete block (RCB)", Alpha = "Alpha-lattice", RowCol = "Row-column",
                AugRCB = "Augmented RCB", AugLS = "Augmented latin square",
                LatinAlpha = "Latinized alpha-lattice", LatinRowCol = "Latinized row-column")
# which layout columns each design needs besides genotype and block/row
pb_needs_column <- function(design) design %in% c("AugLS", "RowCol", "LatinRowCol")
pb_needs_rep <- function(design) design %in% c("Alpha", "RowCol", "LatinAlpha", "LatinRowCol")

pb_empty <- function(x) is.null(x) || !length(x) || is.na(x[1]) || !nzchar(x[1])
pb_null_if_empty <- function(x) if (pb_empty(x)) NULL else x
pb_num <- function(x) suppressWarnings(as.numeric(as.character(x)))
pb_harmonic <- function(n) { n <- n[n > 0]; 1 / mean(1 / n) }

pb_bind <- function(l) {
  l <- l[!vapply(l, is.null, NA)]
  if (!length(l)) return(NULL)
  out <- do.call(rbind, unname(lapply(l, function(x) { x <- as.data.frame(x); rownames(x) <- NULL; x })))
  rownames(out) <- NULL
  out
}

pb_read_table <- function(path, name = path) {
  ext <- tolower(tools::file_ext(name))
  d <- if (ext %in% c("xlsx", "xls")) as.data.frame(readxl::read_excel(path))
       else if (ext %in% c("txt", "tsv")) utils::read.delim(path, check.names = FALSE, na.strings = c("NA", "", "."))
       else utils::read.csv(path, check.names = FALSE, na.strings = c("NA", "", "."))
  names(d) <- trimws(names(d))
  d
}

# Descriptive statistics of a trait, as PBTools DescriptiveStatistics() with the statistics its analysis dialogs ask
# for (number of non-missing observations, mean, standard deviation). By environment when `env` is given (PBTools:
# single environment); over all rows otherwise (PBTools: one-stage on the plot data, two-stage on the stage-1 means).
pb_descriptive <- function(data, traits, env = NULL) {
  env <- pb_null_if_empty(env)
  pb_check_cols(data, traits, env)
  data <- as.data.frame(data)
  grp <- if (is.null(env)) factor(rep("all", nrow(data))) else pb_factor(data[[env]])
  out <- lapply(traits, function(tr) {
    s <- lapply(split(pb_num(data[[tr]]), grp), function(y) {
      y <- y[!is.na(y)]
      c(length(y), if (length(y)) mean(y) else NA_real_, if (length(y) > 1) stats::sd(y) else NA_real_)
    })
    s <- do.call(rbind, s)
    data.frame(Trait = tr, Environment = levels(grp), `N non-missing` = as.integer(s[, 1]), Mean = s[, 2], SD = s[, 3],
               row.names = NULL, check.names = FALSE)
  })
  out <- pb_bind(out)
  if (is.null(env)) out$Environment <- NULL
  out
}

pb_check_cols <- function(data, ...) {
  cols <- unlist(list(...)); cols <- cols[!is.na(cols) & nzchar(cols)]
  miss <- setdiff(cols, names(data))
  if (length(miss)) stop("Column not found in the data: ", paste(miss, collapse = ", "), call. = FALSE)
  if (anyDuplicated(cols)) stop("The same column was chosen for two different roles.", call. = FALSE)
  invisible(TRUE)
}

# factors with trimmed labels, as PBTools does (trimStrings + factor)
pb_factor <- function(x) factor(trimws(as.character(x)))

# Random design terms of a single-environment model, exactly as in PBTools ssa.test (design structure only;
# the genotype term is added by the caller). `longer_row` is used by the latinized row-column design.
pb_design_terms <- function(design, block, column = NULL, rep = NULL, longer_row = FALSE) {
  q <- function(x) paste0("`", x, "`")
  b <- q(block); k <- if (!is.null(column)) q(column); r <- if (!is.null(rep)) q(rep)
  switch(design,
    RCB = , AugRCB = c(sprintf("(1|%s)", b)),
    AugLS = c(sprintf("(1|%s)", b), sprintf("(1|%s)", k)),
    Alpha = c(sprintf("(1|%s/%s)", r, b)),
    RowCol = c(sprintf("(1|%s)", r), sprintf("(1|%s:%s)", r, b), sprintf("(1|%s:%s)", r, k)),
    LatinAlpha = c(sprintf("(1|%s)", r), sprintf("(1|%s)", b), sprintf("(1|%s:%s)", r, b)),
    LatinRowCol = if (longer_row) c(sprintf("(1|%s)", r), sprintf("(1|%s)", k), sprintf("(1|%s:%s)", r, k), sprintf("(1|%s)", b))
                  else c(sprintf("(1|%s)", r), sprintf("(1|%s)", b), sprintf("(1|%s:%s)", r, b), sprintf("(1|%s)", k)),
    stop("Unknown design: ", design, call. = FALSE))
}

# Variance components of an lme4 fit as a data frame (Group, Variance, SD)
pb_varcomp <- function(m) {
  v <- as.data.frame(lme4::VarCorr(m))
  data.frame(Group = gsub("`", "", v$grp), Variance = v$vcov, SD = v$sdcor, stringsAsFactors = FALSE)
}

# Likelihood ratio test of two nested lme4 fits. refit = FALSE compares the REML fits, as PBTools does for a random
# term; refit = TRUE refits with ML (fixed genotype term).
pb_lrt <- function(m_without, m_with, refit = FALSE, label = NULL) {
  a <- suppressMessages(stats::anova(m_without, m_with, refit = refit))
  data.frame(Test = label %||% NA_character_, Model = c("without", "with"), AIC = a$AIC, BIC = a$BIC, logLik = a$logLik,
             Chisq = c(NA, a$Chisq[2]), Df = c(NA, a$Df[2]), P = c(NA, a$`Pr(>Chisq)`[2]), stringsAsFactors = FALSE)
}

pb_fit <- function(formula, data, REML = TRUE, weights = NULL) {
  f <- stats::as.formula(formula)
  ctl <- lme4::lmerControl(check.conv.singular = "ignore")
  if (is.null(weights)) suppressMessages(lme4::lmer(f, data = data, REML = REML, control = ctl))
  else { data$.w <- weights; suppressMessages(lme4::lmer(f, data = data, REML = REML, weights = .w, control = ctl)) }
}

# BLUEs of a fixed factor with their SE (emmeans), and BLUPs (intercept + random effect, as PBTools reports them)
pb_blue <- function(m, term) {
  e <- as.data.frame(emmeans::emmeans(m, term, lmer.df = "asymptotic"))
  data.frame(Genotype = as.character(e[[term]]), Mean = e$emmean, SE = e$SE, stringsAsFactors = FALSE)
}
pb_blup <- function(m, term) {
  co <- stats::coef(m)[[term]]
  data.frame(Genotype = rownames(co), Mean = co[["(Intercept)"]], stringsAsFactors = FALSE)
}

pb_diag_frame <- function(m, trait, env = NA) {
  data.frame(Trait = trait, Environment = env, Fitted = as.numeric(stats::fitted(m)), Residual = as.numeric(stats::resid(m)), stringsAsFactors = FALSE)
}

# p-value of a 1-df chi-square, formatted
pb_fmt_p <- function(p) ifelse(is.na(p), "", ifelse(p < 0.0001, "<0.0001", formatC(p, format = "f", digits = 4)))
