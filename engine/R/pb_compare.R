# Pairwise comparison of genotype means (PBTools: ssa.pairwise), with multcomp: Tukey (all pairs) or Dunnett (against
# control genotypes), simultaneous confidence intervals at level 1 - alpha on a model with genotype fixed.
# PBTools lists only the pairs whose interval excludes 0; `all` keeps every pair. Tukey also gives letter groups
# (multcomp::cld), which PBTools does not.

pb_pairwise <- function(models, geno, type = c("Tukey", "Dunnett"), alpha = 0.05, control = NULL, all = FALSE, seed = 1) {
  # multcomp finds the critical value of the simultaneous intervals numerically (randomised); a fixed seed makes the
  # limits reproducible
  if (!is.null(seed)) set.seed(seed)
  type <- match.arg(type)
  if (!length(models)) stop("There is no model with genotype as a fixed effect. Run the analysis with genotype = Fixed first.", call. = FALSE)
  control <- control[!is.na(control) & nzchar(control)]
  if (type == "Dunnett" && !length(control)) stop("Choose at least one control genotype for Dunnett's test.", call. = FALSE)
  rows <- list(); letters <- list(); warn <- character()
  for (nm in names(models)) {
    m <- models[[nm]]
    lv <- levels(stats::model.frame(m)[[geno]])
    if (type == "Tukey") {
      K <- "Tukey"   # character, so that multcomp::cld can give letter groups
      ctr <- list(K)
    } else {
      ok <- control %in% lv
      if (!all(ok)) warn <- c(warn, sprintf("%s: control(s) not in the data: %s", nm, paste(control[!ok], collapse = ", ")))
      ctr <- lapply(control[ok], function(cn) multcomp::contrMat(stats::setNames(rep(1, length(lv)), lv), type = "Dunnett", base = match(cn, lv)))
    }
    for (K in ctr) {
      lf <- list(K); names(lf) <- geno
      gl <- suppressWarnings(multcomp::glht(m, linfct = do.call(multcomp::mcp, lf)))
      ci <- stats::confint(gl, level = 1 - alpha)$confint
      pr <- strsplit(rownames(ci), " - ", fixed = TRUE)
      d <- data.frame(Model = nm, `Genotype i` = vapply(pr, `[`, "", 1), `Genotype j` = vapply(pr, `[`, "", 2),
                      Difference = ci[, "Estimate"], Lower = ci[, "lwr"], Upper = ci[, "upr"], check.names = FALSE)
      d$Significant <- d$Lower > 0 | d$Upper < 0
      rows[[length(rows) + 1]] <- d
      if (type == "Tukey") {
        cl <- multcomp::cld(gl, level = 1 - alpha)
        letters[[nm]] <- data.frame(Model = nm, Genotype = names(cl$mcletters$Letters), Group = unname(cl$mcletters$Letters))
      }
    }
  }
  tab <- pb_bind(rows)
  if (!all && !is.null(tab)) tab <- tab[tab$Significant, setdiff(names(tab), "Significant"), drop = FALSE]
  list(table = tab, letters = pb_bind(letters), warning = warn, type = type, alpha = alpha,
       none = setdiff(names(models), unique(tab$Model)))
}

# The diagnostic plots PBTools draws (graph.sea.diagplots): residuals against fitted values, normal Q-Q plot and
# histogram of the residuals, plus a note; the Shapiro-Wilk test of the residuals is added (stats::shapiro.test).
pb_diag_plot <- function(diag, note = NULL) {
  op <- graphics::par(mfrow = c(2, 2), mar = c(4.2, 4.2, 3.2, 1)); on.exit(graphics::par(op))
  cc <- grDevices::rgb(0.65, 0.11, 0.11, 0.5)
  graphics::plot(diag$Fitted, diag$Residual, pch = 16, cex = 0.8, col = cc, xlab = "Fitted values", ylab = "Residuals", main = "Residuals against fitted values")
  graphics::abline(h = 0, lty = 2, col = "grey50")
  stats::qqnorm(diag$Residual, pch = 16, cex = 0.8, col = cc, main = "Normal Q-Q plot of residuals"); stats::qqline(diag$Residual, col = "grey25")
  graphics::hist(diag$Residual, main = "Histogram of residuals", col = cc, border = "white", xlab = "Residual", ylab = "Frequency")
  graphics::plot(0:1, 0:1, type = "n", axes = FALSE, xlab = "", ylab = "")
  sw <- if (length(diag$Residual) >= 3 && length(diag$Residual) <= 5000) tryCatch(stats::shapiro.test(diag$Residual), error = function(e) NULL)
  txt <- c(if (!is.null(note)) paste(strwrap(note, 36), collapse = "\n"),
           if (!is.null(sw)) sprintf("Shapiro-Wilk test of the residuals:\nW = %.3f, p = %s", sw$statistic, format.pval(sw$p.value, digits = 3)))
  graphics::text(0.5, 0.5, paste(txt, collapse = "\n\n"), cex = 0.95)
  invisible(NULL)
}
