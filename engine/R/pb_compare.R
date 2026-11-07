# Pairwise comparison of genotype means (PBTools: ssa.pairwise): Tukey (all pairs) or Dunnett (against control
# genotypes), simultaneous confidence intervals at level 1 - alpha on a model with genotype fixed. A difference is
# significant when its interval excludes 0; PBTools lists only those pairs (`all` keeps every pair).
#   method "emmeans" (default, fast): emmeans contrasts, Tukey-Kramer intervals (studentized range) and Dunnett's
#     intervals ("dunnettx" approximation); identical to the exact intervals for balanced data.
#   method "multcomp" (as PBTools, slow): multcomp::glht single-step intervals; the critical value comes from the
#     multivariate t distribution, computed numerically (seconds per environment with 15 genotypes).
# Tukey also gives letter groups (multcompView::multcompLetters on the significant pairs), which PBTools does not.

pb_pairwise <- function(models, geno, type = c("Tukey", "Dunnett"), alpha = 0.05, control = NULL, all = FALSE,
                        method = c("emmeans", "multcomp"), seed = 1) {
  type <- match.arg(type); method <- match.arg(method)
  if (!is.null(seed)) set.seed(seed)   # the multcomp critical value is randomised; a fixed seed makes it reproducible
  if (!length(models)) stop("There is no model with genotype as a fixed effect. Run the analysis with genotype = Fixed first.", call. = FALSE)
  control <- control[!is.na(control) & nzchar(control)]
  if (type == "Dunnett" && !length(control)) stop("Choose at least one control genotype for Dunnett's test.", call. = FALSE)
  rows <- list(); letters <- list(); warn <- character()
  for (nm in names(models)) {
    m <- models[[nm]]
    lv <- levels(stats::model.frame(m)[[geno]])
    ctl <- if (type == "Dunnett") {
      ok <- control %in% lv
      if (!all(ok)) warn <- c(warn, sprintf("%s: control(s) not in the data: %s", nm, paste(control[!ok], collapse = ", ")))
      control[ok]
    } else NA
    for (cn in ctl) {
      d <- if (method == "emmeans") pb_pairs_emmeans(m, geno, type, alpha, cn, lv) else pb_pairs_multcomp(m, geno, type, alpha, cn, lv)
      d <- cbind(Model = nm, d)
      d$Significant <- d$Lower > 0 | d$Upper < 0
      rows[[length(rows) + 1]] <- d
      if (type == "Tukey") {
        sig <- stats::setNames(d$Significant, paste(match(d$`Genotype i`, lv), match(d$`Genotype j`, lv), sep = "-"))
        lt <- multcompView::multcompLetters(sig)$Letters
        letters[[nm]] <- data.frame(Model = nm, Genotype = lv[as.integer(names(lt))], Group = unname(lt))
      }
    }
  }
  tab <- pb_bind(rows)
  if (!all && !is.null(tab)) tab <- tab[tab$Significant, setdiff(names(tab), "Significant"), drop = FALSE]
  list(table = tab, letters = pb_bind(letters), warning = warn, type = type, alpha = alpha, method = method,
       none = setdiff(names(models), unique(tab$Model)))
}

# both helpers return Genotype i, Genotype j, Difference (i - j), Lower, Upper; Dunnett: j is the control
pb_pairs_emmeans <- function(m, geno, type, alpha, control, lv) {
  emm <- suppressMessages(emmeans::emmeans(m, geno, lmer.df = "asymptotic"))
  ct <- if (type == "Tukey") emmeans::contrast(emm, "revpairwise", adjust = "tukey")
        else emmeans::contrast(emm, "trt.vs.ctrl", ref = match(control, lv), adjust = "dunnettx")
  ci <- as.data.frame(stats::confint(ct, level = 1 - alpha, adjust = if (type == "Tukey") "tukey" else "dunnettx"))
  pr <- strsplit(as.character(ci$contrast), " - ", fixed = TRUE)
  # emmeans writes numeric-looking levels with the factor name in front ("Gen2 - Gen1"); take it off
  pr <- lapply(pr, function(x) ifelse(x %in% lv, x, substring(x, nchar(geno) + 1)))
  lo <- grep("LCL$|lower.CL$", names(ci), value = TRUE); up <- grep("UCL$|upper.CL$", names(ci), value = TRUE)
  data.frame(`Genotype i` = vapply(pr, `[`, "", 1), `Genotype j` = vapply(pr, `[`, "", 2), Difference = ci$estimate,
             Lower = ci[[lo]], Upper = ci[[up]], check.names = FALSE)
}
pb_pairs_multcomp <- function(m, geno, type, alpha, control, lv) {
  K <- if (type == "Tukey") "Tukey" else multcomp::contrMat(stats::setNames(rep(1, length(lv)), lv), type = "Dunnett", base = match(control, lv))
  lf <- list(K); names(lf) <- geno
  gl <- suppressWarnings(multcomp::glht(m, linfct = do.call(multcomp::mcp, lf)))
  ci <- stats::confint(gl, level = 1 - alpha)$confint
  pr <- strsplit(rownames(ci), " - ", fixed = TRUE)
  data.frame(`Genotype i` = vapply(pr, `[`, "", 1), `Genotype j` = vapply(pr, `[`, "", 2), Difference = ci[, "Estimate"],
             Lower = ci[, "lwr"], Upper = ci[, "upr"], check.names = FALSE)
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

# Classic multiple comparisons on the fixed-effect ANOVA models (lm): Fisher's LSD and Duncan's multiple range test
# (DMRT), with agricolae::LSD.test and agricolae::duncan.test. The error mean square and df are those of the model, so
# for the combined ANOVA with environments random (model of the GxE cell means) they are the GxE mean square / r.
pb_classic_compare <- function(models, geno, type = c("LSD", "DMRT"), alpha = 0.05) {
  type <- match.arg(type)
  if (!length(models)) stop("There is no ANOVA model to compare.", call. = FALSE)
  if (!all(vapply(models, inherits, NA, "lm") & !vapply(models, inherits, NA, "merMod")))
    stop("LSD and DMRT are given with the Standard ANOVA (single environment) or the Combined ANOVA (multi-environment). Choose that method and run again, or use Tukey / Dunnett.", call. = FALSE)
  letters <- list(); stats <- list()
  for (nm in names(models)) {
    m <- models[[nm]]
    x <- if (type == "DMRT") agricolae::duncan.test(m, geno, alpha = alpha, group = TRUE)
         else agricolae::LSD.test(m, geno, alpha = alpha, p.adj = "none", group = TRUE)
    g <- x$groups
    letters[[nm]] <- data.frame(Model = nm, Genotype = rownames(g), Mean = g[[1]], Group = trimws(as.character(g$groups)))
    st <- x$statistics
    stats[[nm]] <- data.frame(Model = nm, `Error mean square` = st$MSerror, `Error df` = st$Df,
                              Critical = if (type == "DMRT") sprintf("%.4g to %.4g", min(x$duncan$CriticalRange), max(x$duncan$CriticalRange)) else if (is.null(st$LSD)) "differs between pairs (unequal replication)" else sprintf("%.4g", st$LSD),
                              check.names = FALSE)
    names(stats[[nm]])[4] <- if (type == "DMRT") "Critical range (2 to p means)" else "LSD"
  }
  list(table = NULL, letters = pb_bind(letters), stats = pb_bind(stats), type = type, alpha = alpha, classic = TRUE, warning = character())
}
