# Report for Microsoft Word. Word opens a single-file web page (MHTML) saved as .doc, with the tables as Word tables
# and the plot embedded; this needs no extra R package besides htmltools and jsonlite (both installed with shiny).
# Sourced by app.R (needs fmt_df, app_name, version_label). The word_doc() layout is the one RDstat uses.

b64_lines <- function(raw) {
  s <- gsub("\\s", "", jsonlite::base64_enc(raw))
  substring(s, seq(1, nchar(s), 76), pmin(nchar(s), seq(76, nchar(s) + 75, 76)))
}
esc <- function(x) htmltools::htmlEscape(as.character(x))
w_h2 <- function(x) sprintf("<h2>%s</h2>", esc(x))
w_p <- function(x, cls = NULL) sprintf("<p%s>%s</p>", if (is.null(cls)) "" else sprintf(" class=%s", cls), esc(x))
w_ul <- function(x) paste0("<ul>", paste0("<li>", esc(x), "</li>", collapse = ""), "</ul>")

word_table <- function(df) {
  if (is.null(df) || !nrow(df)) return("")
  df <- fmt_df(df)
  num <- vapply(df, function(v) { v <- as.character(v); v <- v[nzchar(v)]; length(v) > 0 && all(grepl("^[-<]?[0-9][0-9,.eE+-]*$", v)) }, NA)
  th <- paste0("<th", ifelse(num, " class=n", ""), ">", esc(names(df)), "</th>", collapse = "")
  rows <- vapply(seq_len(nrow(df)), function(i)
    paste0("<tr>", paste0("<td", ifelse(num, " class=n", ""), ">", esc(unlist(df[i, ], use.names = FALSE)), "</td>", collapse = ""), "</tr>"), "")
  paste0("<table><tr>", th, "</tr>", paste(rows, collapse = ""), "</table>")
}

word_doc <- function(file, title, blocks, pngs = list(), tail = NULL) {
  css <- paste("body { font-family: 'TH Sarabun New', 'TH SarabunPSK', 'Cordia New', sans-serif; font-size: 16pt; }",
               "h1 { font-size: 22pt; } h2 { font-size: 18pt; margin-top: 14pt; } p.small { font-size: 13pt; color: #555; }",
               "table { border-collapse: collapse; margin: 4pt 0 8pt; } th, td { border-top: 1px solid #999; border-bottom: 1px solid #999; padding: 1pt 6pt; font-size: 14pt; }",
               "th { text-align: left; } .n { text-align: right; }")
  imgs <- unlist(lapply(seq_along(pngs), function(i) c(w_h2(names(pngs)[i]), sprintf("<p><img width=600 src=\"file:///C:/pbtools/plot%d.png\"></p>", i))))
  body <- paste(c(sprintf("<h1>%s</h1>", esc(title)), unlist(blocks), imgs, unlist(tail)), collapse = "\n")
  html <- sprintf("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\"><head><meta charset=\"utf-8\"><style>%s</style></head><body lang=th>%s</body></html>", css, body)
  b <- "----=_NextPart_PBToolsRD"
  parts <- c("MIME-Version: 1.0", sprintf("Content-Type: multipart/related; boundary=\"%s\"", b), "",
             paste0("--", b), "Content-Type: text/html; charset=\"utf-8\"", "Content-Transfer-Encoding: base64",
             "Content-Location: file:///C:/pbtools/report.htm", "", b64_lines(charToRaw(enc2utf8(html))))
  for (i in seq_along(pngs)) parts <- c(parts, "", paste0("--", b), "Content-Type: image/png", "Content-Transfer-Encoding: base64",
                                        sprintf("Content-Location: file:///C:/pbtools/plot%d.png", i), "", b64_lines(readBin(pngs[[i]], "raw", file.size(pngs[[i]]))))
  writeLines(c(parts, "", paste0("--", b, "--")), file, useBytes = TRUE)
}


pkg_ver <- function(p) tryCatch(as.character(utils::packageVersion(p)), error = function(e) "?")
pkg_ref <- function(p) tryCatch(gsub("[*_]", "", gsub("[[:space:]]+", " ", paste(format(utils::citation(p), style = "text"), collapse = " "))),
                                error = function(e) sprintf("%s: R package version %s.", p, pkg_ver(p)))
pbtools_ref <- "International Rice Research Institute (IRRI). PBTools: Plant Breeding Tools, version 1.1."

# the settings of a run as a two-column table: input ids without the screen prefix, examples and empty values dropped
settings_table <- function(set, data_name) {
  set <- set[!vapply(set, function(v) is.null(v) || !length(v) || identical(v, "") || identical(v, FALSE), NA)]
  set <- set[!grepl("_src$|_example$|^file$", names(set))]
  v <- vapply(set, function(x) paste(as.character(x), collapse = ", "), "")
  k <- sub("^[a-z]+_", "", names(set))
  data.frame(Setting = c("Data", k), Value = c(data_name, unname(v)), check.names = FALSE)
}

make_report <- function(file, title, data_name, settings, method, tables, plot = NULL, notes = NULL, pkgs = "lme4", diag = NULL) {
  pngs <- list()
  if (!is.null(plot)) { f <- tempfile(fileext = ".png"); ggplot2::ggsave(f, plot, width = 7, height = 5, dpi = 150); pngs[["Plot"]] <- f }
  if (is.function(diag)) { f <- tempfile(fileext = ".png"); grDevices::png(f, width = 1000, height = 800, res = 130); try(diag(), silent = TRUE); grDevices::dev.off(); pngs[["Residual checks"]] <- f }
  blocks <- list(
    w_p(sprintf("%s %s, %s. Data: %s", app_name, version_label(), format(Sys.time(), "%Y-%m-%d %H:%M"), data_name), "small"),
    w_h2("Settings"), word_table(settings_table(settings, data_name)),
    w_h2("Method"), w_p(method),
    if (length(notes)) w_ul(notes),
    unlist(lapply(names(tables), function(n) if (!is.null(tables[[n]]) && nrow(tables[[n]])) c(w_h2(n), word_table(tables[[n]])))))
  word_doc(file, title, blocks, pngs, tail = list(w_h2("References"), w_ul(c(pbtools_ref, vapply(unique(pkgs), pkg_ref, "")))))
}

# one paragraph on how the analysis was done, for the report and the Methods page
method_text <- function(kind, r = NULL) {
  rv <- sprintf("R %s.%s", R.version$major, R.version$minor)
  g <- if (isTRUE(r$random)) "genotype as a random effect (BLUPs; heritability H2 = s2g / (s2g + s2e / r) as in PBTools, r the harmonic mean of the replicates per genotype)" else
    "genotype as a fixed effect (adjusted means, BLUEs, with SE from emmeans)"
  switch(kind,
    sea = if (identical(r$method, "anova")) sprintf("Each trait in each environment was analysed with the standard analysis of variance of a randomized complete block design, y = block + genotype + error with blocks and genotypes fixed (stats::lm and stats::anova, %s, sequential sums of squares with blocks first). Genotype means and SE from emmeans %s; CV = sqrt(error mean square) / grand mean x 100; LSD (5%%) = t(0.975, error df) x average SED. Means were compared with simultaneous confidence intervals (Tukey or Dunnett).", rv, pkg_ver("emmeans"))
      else sprintf("Each trait in each environment was analysed with a linear mixed model fitted by REML with lme4 %s (%s), with the design terms of IRRI PBTools 1.1 for the %s design and %s. The genotype variance was tested with a likelihood ratio test of the REML fits with and without the genotype term. Means were compared with simultaneous confidence intervals (Tukey or Dunnett) from multcomp %s.",
                  pkg_ver("lme4"), rv, pb_designs[[r$design %||% "RCB"]], g, pkg_ver("multcomp")),
    met = if (identical(r$method, "combined ANOVA")) sprintf("Combined analysis of variance across environments (Gomez & Gomez 1984), RCBD in each environment: y = environment + replicate within environment + genotype + genotype x environment + error, fitted with stats::lm (%s, sequential sums of squares). %s Homogeneity of the error variances of the environments was checked with Bartlett's test (stats::bartlett.test). Genotype means and SE from emmeans %s with the error term of the genotype F test; CV = sqrt(pooled error MS) / grand mean x 100; LSD (5%%) = t x average SED.",
            rv, if (isTRUE(r$env_random)) "Environments were taken as random: environment was tested against replicates within environment and genotype against the genotype x environment interaction." else "Environments were taken as fixed: every term was tested against the pooled error.", pkg_ver("emmeans"))
      else if (identical(r$method, "one-stage")) sprintf("All plots of all environments were analysed in one linear mixed model (one-stage) with lme4 %s (%s): environment, blocks within environment and genotype x environment random, as in IRRI PBTools 1.1, and %s. Each random term was tested with a likelihood ratio test.", pkg_ver("lme4"), rv, g)
          else sprintf("Two-stage analysis: the genotype means of each environment (stage 1, genotype fixed) were combined in a linear mixed model with environment random (stage 2), %s, with lme4 %s (%s), as in IRRI PBTools 1.1, and %s.",
                       if (identical(r$info$Weight[1], "1/SE^2")) "weighted by the inverse of the squared SE of the stage-1 means" else "without weights", pkg_ver("lme4"), rv, g),
    fw = sprintf("Finlay and Wilkinson (1963) regression of the genotype means on the environment index (site mean minus grand mean), with metan::ge_reg, metan %s (Olivoto & Lucio 2020), %s; the slope was tested against 1 with a t test.", pkg_ver("metan"), rv),
    er = sprintf("Eberhart and Russell (1966) stability parameters (regression slope b1 on the environment index and deviation from regression s2di) with the joint-regression analysis of variance, with metan::ge_reg, metan %s (Olivoto & Lucio 2020), %s. b1 was tested against 1 with a t test and s2di against 0 with an F test on the pooled error; genotypes with b1 = 1 and s2di = 0 (alpha = %.2f) were classed as stable.", pkg_ver("metan"), rv, r$alpha %||% 0.05),
    shukla = sprintf("Shukla's (1972) stability variance with metan %s, %s; for comparison, the PBTools form of the model (one residual variance per genotype in the additive model of the genotype x environment means, nlme::gls with varIdent, REML) with nlme %s.", pkg_ver("metan"), rv, pkg_ver("nlme")),
    stats = sprintf("Stability statistics (Wricke's ecovalence, Shukla, Eberhart-Russell, Annicchiarico, Lin and Binns superiority, ASV, WAASB) with metan::ge_stats, metan %s, %s.", pkg_ver("metan"), rv),
    ammi = sprintf("Additive main effects and multiplicative interaction (AMMI) model with metan::performs_ammi, metan %s, %s; the interaction was split by singular value decomposition and each PC tested against the pooled error.", pkg_ver("metan"), rv),
    waasb = sprintf("WAASB and WAASBY (Olivoto et al. 2019) with metan::waasb, metan %s, %s: linear mixed model with genotype and genotype x environment random (lme4), singular value decomposition of the matrix of genotype x environment BLUPs; WAASB is the average of the absolute scores of all principal components weighted by the variance they explain; WAASBY = (mean on a 0-100 scale x wY + WAASB on a 0-100 scale x (100 - wY)) / 100.", pkg_ver("metan"), rv),
    mtsi = sprintf("Multi-trait selection with metan %s, %s: MTSI (multi-trait stability index; Olivoto et al. 2019) from the WAASBY of each trait (metan::waasb, metan::mtsi), and MGIDI (multi-trait genotype-ideotype distance index; Olivoto & Nardino 2021) from the genotype %s (metan::mgidi). Both use factor analysis of the traits and select the genotypes nearest the ideotype at a selection intensity of %s%%.", pkg_ver("metan"), rv, if (identical(r$mgidi_on, "means")) "means" else "BLUPs", r$si),
    gge = sprintf("GGE biplot (genotype plus genotype x environment) with metan::gge, metan %s, %s: environment-centred data, singular value decomposition.", pkg_ver("metan"), rv))
}
