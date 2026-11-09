# Headless test of the app's server logic (native R). Run from anywhere: Rscript prototype/tests/test_server.R
# (run scripts/build_app.R first so that prototype/app/engine exists)
here <- dirname(normalizePath(sub("--file=", "", grep("--file=", commandArgs(FALSE), value = TRUE))))
setwd(file.path(here, "..", "app"))
suppressMessages(library(shiny))
app <- source("app.R")$value
# height and width in pixels of a PNG file, from its IHDR header (no png package needed)
png_dim <- function(path) { b <- readBin(path, "raw", 24); be <- function(x) sum(as.integer(x) * 256^(3:0)); c(be(b[21:24]), be(b[17:20])) }
check <- function(cond, msg) if (!isTRUE(cond)) stop("FAILED: ", msg, call. = FALSE) else cat("ok  ", msg, "\n")
html <- function(x) paste(as.character(x$html), collapse = "")
doc_text <- function(path) {
  x <- readLines(path, warn = FALSE); i <- grep("^Content-Location: file:///C:/pbtools/report.htm", x)
  body <- x[(i + 2):length(x)]; body <- body[seq_len(which(body == "")[1] - 1)]
  rawToChar(jsonlite::base64_dec(paste(body, collapse = "")))
}

testServer(app, {
  n <- 0; go <- function(id, ...) { n <<- n + 1; args <- list(...); args[[id]] <- n; do.call(session$setInputs, args) }

  # ---- single environment: RCB, 11 sites ----
  session$setInputs(sea_src = "ex", sea_example = "rcb11", sea_method = "mixed")
  check(grepl("No results|ยังไม่มีผล", html(output$sea_msg)), "single env: empty state before the first run")
  go("go_sea", sea_trait = "Yield", sea_geno = "Genotype", sea_env = "Env", sea_block = "Block", sea_random = "random")
  r <- rv$sea
  check(is.null(rv$sea_err) && nrow(r$summary) == 11, "single env random: 11 environments")
  check(abs(r$summary$Heritability[r$summary$Environment == "E2"] - 0.95) < 0.01, "single env random: heritability E2 = 0.95 (PBTools)")
  check(grepl("Heritability", html(output$sea_results)) && grepl("result-card", html(output$sea_results)), "single env: heritability and result cards")
  check(grepl("<strong>495</strong>\\s*rows", html(output$sea_data)) && lengths(gregexpr("<tr", html(output$sea_data))) == 496, "single env: Data tab lists every row")
  ds <- r$descriptive[r$descriptive$Environment == "E1", ]
  check(nrow(r$descriptive) == 11 && ds$`N non-missing` == 45 && abs(ds$Mean - 2403.838) < 0.001 && grepl("Descriptive statistics", html(output$sea_results)),
        "single env: descriptive statistics by environment (PBTools: E1 n = 45)")
  go("go_sea", sea_random = "fixed")
  check(!is.null(rv$sea$models) && length(rv$sea$models) == 11, "single env fixed: one model per environment for the comparisons")
  session$setInputs(sea_pw_type = "Tukey", sea_pw_alpha = "0.05", sea_pw_all = FALSE, sea_pw_control = character())
  go("go_sea_pw")
  check(grepl("Letter groups", html(output$sea_pw_out)) && grepl("Tukey comparisons", html(output$sea_pw_out)), "Tukey: letters and significant pairs")
  check(grepl("sea_pw_key", html(output$sea_pw_pick)) && !grepl("Yield | E2", html(output$sea_pw_out), fixed = TRUE), "Tukey: one environment shown at a time")
  session$setInputs(sea_pw_key = "__all__")
  check(grepl("Yield | E2", html(output$sea_pw_out), fixed = TRUE), "Tukey: all environments on request")
  session$setInputs(sea_pw_key = "Yield | E2")
  check(grepl("GEN", html(output$sea_pw_out)) && !grepl("Yield | E1<", html(output$sea_pw_out), fixed = TRUE), "Tukey: environment E2 chosen")
  session$setInputs(sea_pw_type = "Dunnett", sea_pw_control = "GEN1"); go("go_sea_pw")
  check(grepl("Dunnett comparisons", html(output$sea_pw_out)), "Dunnett against GEN1")
  check(!is.null(output$sea_plot) && !is.null(output$sea_diag), "single env: plot and residual checks render")
  html_doc <- doc_text(output$dl_sea_doc)
  check(grepl("Single-environment analysis", html_doc) && grepl("lme4", html_doc) && grepl("GEN1", html_doc), "single env: Word report")
  check(file.exists(output$dl_sea_xlsx), "single env: Excel file")
  session$setInputs(sea_random = "random")
  check(grepl("settings have changed|การตั้งค่าเปลี่ยนไป", html(output$sea_msg)), "single env: changed settings are flagged")

  # ---- exact (multcomp) comparisons and a single site ----
  session$setInputs(sea_example = "rcb2")
  go("go_sea", sea_trait = c("Y1", "Y2"), sea_geno = "Gen", sea_env = "", sea_block = "Blk", sea_random = "fixed")
  check(is.null(rv$sea_err) && is.null(rv$sea$summary$Environment) && nrow(rv$sea$summary) == 2, "single env: one site (no environment column), two traits")
  check(grepl("ให้ Gen เป็น fixed", sea_thai(rv$sea)) && !grepl("พันธุ์", sea_thai(rv$sea)), "Thai summary names the genotype column of the data")
  session$setInputs(sea_pw_type = "Tukey", sea_pw_exact = TRUE, sea_pw_all = TRUE); go("go_sea_pw")
  check(grepl("Tukey comparisons", html(output$sea_pw_out)), "Tukey with the exact multcomp intervals")
  t0 <- Sys.time(); session$setInputs(sea_example = "rcb11"); go("go_sea", sea_trait = "Yield", sea_geno = "Genotype", sea_env = "Env", sea_block = "Block", sea_random = "fixed")
  session$setInputs(sea_pw_exact = FALSE); go("go_sea_pw")
  check(as.numeric(Sys.time() - t0, units = "secs") < 30, "single env 11 sites + Tukey (emmeans) in under 30 s")

  # ---- multi-environment ----
  session$setInputs(met_src = "ex", met_example = "rcb11", met_design = "RCB", met_method = "mixed", met_stage = "one")
  go("go_met", met_trait = "Yield", met_geno = "Genotype", met_env = "Env", met_block = "Block", met_random = "random")
  check(is.null(rv$met_err) && !is.na(rv$met$info$Heritability), paste("one-stage random: heritability across sites", rv$met_err))
  check(grepl("Genotype x Environment", html(output$met_results)) && !grepl("key-stat", html(output$met_results)), "one-stage: GxE test in the results, no key-number cards")
  check(nrow(rv$met$descriptive) == 1 && rv$met$descriptive$`N non-missing` == 495 && grepl("Descriptive statistics", html(output$met_results)),
        "one-stage: descriptive statistics of all plots (n = 495)")
  session$setInputs(met_method = "mixed", met_stage = "two", met_weight = "none")
  go("go_met", met_random = "fixed")
  check(is.null(rv$met_err) && nrow(rv$met$stage1) == 165, paste("two-stage from plot data: 165 stage-1 means", rv$met_err))
  check(grepl("สองขั้นตอน", html(output$met_results)), "two-stage fixed: results and Thai summary render (no GxE test)")
  session$setInputs(met_pw_type = "Tukey", met_pw_alpha = "0.05", met_pw_all = TRUE); go("go_met_pw")
  check(grepl("Tukey comparisons", html(output$met_pw_out)), "two-stage: Tukey comparison")
  session$setInputs(met_weight = "stderr")
  go("go_met", met_random = "random")
  check(is.null(rv$met_err) && grepl("1/SE", html(output$met_results)), paste("two-stage random, weighted 1/SE^2", rv$met_err))
  check(grepl("ไม่ควรใช้ค่าอัตราพันธุกรรม", html(output$met_results)) && grepl("ไม่รายงานอัตราพันธุกรรม", html(output$met_results)), "two-stage weighted random: warning about H2")
  check(grepl("Multi-environment analysis", doc_text(output$dl_met_doc)), "multi-environment: Word report")


  go("go_sea_pw", sea_pw_type = "DMRT"); check(grepl("Standard ANOVA", html(output$sea_pw_out)), "DMRT with a mixed model: clear message")
  session$setInputs(sea_pw_type = "Tukey")

  # ---- single environment: standard ANOVA ----
  session$setInputs(sea_src = "ex", sea_example = "rcb2")
  go("go_sea", sea_method = "anova", sea_trait = c("Y1", "Y2"), sea_geno = "Gen", sea_env = "Site", sea_block = "Blk")
  a <- rv$sea$anova; ref <- anova(lm(Y1 ~ factor(Blk) + factor(Gen), data = subset(read.csv("examples/rcb_2sites.csv"), Site == "Env1")))
  check(is.null(rv$sea_err) && isTRUE(all.equal(a$`F value`[2], ref$`F value`[2])), paste("standard ANOVA: genotype F test equals aov", rv$sea_err))
  check(grepl("Analysis of variance", html(output$sea_results)) && grepl("CV", html(output$sea_results)), "standard ANOVA: table and CV shown")
  go("go_sea_pw", sea_pw_type = "Tukey", sea_pw_alpha = "0.05", sea_pw_exact = FALSE)
  check(!is.null(sea_pw()$letters), "standard ANOVA: Tukey letters (numeric genotype codes)")
  go("go_sea_pw", sea_pw_type = "DMRT"); check(grepl("Duncan", html(output$sea_pw_out)) && nrow(sea_pw()$letters) == 32, "standard ANOVA: DMRT letters")
  go("go_sea_pw", sea_pw_type = "LSD"); check(grepl("Fisher", html(output$sea_pw_out)), "standard ANOVA: LSD letters")
  check(grepl("agricolae", doc_text(output$dl_sea_doc)), "DMRT / LSD: agricolae cited in the report")
  session$setInputs(sea_pw_type = "Tukey")
  check(grepl("standard analysis of variance", doc_text(output$dl_sea_doc)), "standard ANOVA: Word report")
  session$setInputs(sea_method = "mixed")

  # ---- multi-environment: combined ANOVA ----
  session$setInputs(met_src = "ex", met_example = "rcb11", met_method = "anova", met_env_random = "random")
  go("go_met", met_trait = "Yield", met_geno = "Genotype", met_env = "Env", met_block = "Block")
  a <- rv$met$anova
  check(is.null(rv$met_err) && isTRUE(all.equal(a$`F value`[a$Source == "Genotype"], 2183921.2 / 2088951.79, tolerance = 1e-6)),
        paste("combined ANOVA: genotype tested against GxE (as aov with Error strata)", rv$met_err))
  check(grepl("Bartlett", html(output$met_results)) && grepl("Combined analysis of variance", html(output$met_results)), "combined ANOVA: tables shown")
  go("go_met_pw", met_pw_type = "Tukey", met_pw_alpha = "0.05", met_pw_exact = FALSE)
  check(!is.null(met_pw()$letters), "combined ANOVA: Tukey letters")
  go("go_met_pw", met_pw_type = "DMRT"); check(abs(sea <- met_pw()$stats$`Error mean square` - 2088951.79 / 3) < 1, "combined ANOVA: DMRT with the GxE mean square")
  session$setInputs(met_pw_type = "Tukey")
  check(grepl("Gomez", doc_text(output$dl_met_doc)), "combined ANOVA: Word report")
  session$setInputs(met_env_random = "fixed"); go("go_met")
  check(rv$met$anova$`Tested against`[rv$met$anova$Source == "Genotype"] == "Pooled error", "combined ANOVA: environments fixed")

  # ---- GxE ----
  ui_html <- as.character(gxe_ui)
  check(all(vapply(c("Stability", "Multivariate biplots", "Modern BLUP &amp; multi-trait selection"), function(h) grepl(sprintf("radio-group-title\">%s<", h), ui_html), NA)), "GxE: methods grouped under three headings")
  session$setInputs(gxe_src = "ex", gxe_example = "rcb11", gge_type = "1", gge_scaling = "none", gge_svp = "environment", ammi_type = "2", shukla_pbtools = TRUE)
  for (k in c("fw", "er", "shukla", "stats", "ammi", "gge")) {
    go("go_gxe", gxe_kind = k, gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
    check(is.null(rv$gxe_err) && !is.null(output$gxe_plot), paste("GxE:", k, rv$gxe_err))
    if (k %in% c("ammi", "gge")) check(grepl(if (k == "ammi") "ssiASV" else "mega-environment", html(output$gxe_results)), paste("GxE:", k, "interpretation"))
    if (k == "er") {
      t <- rv$gxe$table
      check(all(t$`Stable (Eberhart-Russell)` == ifelse(t$`p (b1 = 1)` >= 0.05 & t$`p (s2di = 0)` >= 0.05, "yes", "no")) &&
            "Joint regression ANOVA" %in% names(gxe_tables()), "GxE: Eberhart-Russell stable class and joint-regression ANOVA")
      check(grepl("Eberhart and Russell", doc_text(output$dl_gxe_doc)), "GxE: Eberhart-Russell Word report")
    }
  }
  check(grepl("PC1", doc_text(output$dl_gxe_doc)), "GxE: Word report")
  check(grepl("Genotype", readLines(output$tpl_gxe, n = 1)), "template file download")
  f <- readxl::readxl_example("datasets.xls"); session$setInputs(gxe_src = "up", gxe_file = data.frame(name = "x.xls", datapath = f))
  check(nrow(gxe_dat()$d) == 32, "upload: .xls file is read"); session$setInputs(gxe_src = "ex")
  for (ty in 1:10) {
    session$setInputs(gge_type = as.character(ty), gge_env = "E3", gge_gen = "GEN5", gge_gen1 = "GEN1", gge_gen2 = "GEN9")
    check(!is.null(output$gxe_plot$src), paste("GGE biplot view", ty))
  }
  session$setInputs(gge_type = "9"); check(grepl("Genotype 2", html(output$gge_pick)), "GGE: genotype pickers for 'compare two genotypes'")
  session$setInputs(gxe_dim = "j1", gxe_fmt = "png", gxe_dpi = "300")
  dims <- png_dim(output$dl_gxe_plot)
  check(all(abs(dims[2:1] - round(c(8.5, 6.5) / 2.54 * 300)) <= 1), "plot download: journal 1 column at 300 dpi")
  session$setInputs(gxe_fmt = "svg"); check(file.size(output$dl_gxe_plot) > 1000, "plot download: SVG")
  session$setInputs(gxe_dim = "custom", gxe_w = 12, gxe_h = 12, gxe_scale = 1.3, gxe_fmt = "pdf"); check(file.size(output$dl_gxe_plot) > 1000, "plot download: custom size PDF")
  check(grepl("12.0 × 12.0", html(output$gxe_live_badge)), "Live View badge shows the custom size")
  # WAASB / WAASBY and multi-trait selection on the maize example (metan data_ge2)
  session$setInputs(gxe_src = "ex", gxe_example = "maize", wy_live = 50, si_live = 15, waasb_view = "rank", gxe_waasb_higher = TRUE)
  go("go_gxe", gxe_kind = "waasb", gxe_trait = "KW", gxe_geno = "GEN", gxe_env = "ENV", gxe_rep = "REP")
  check(is.null(rv$gxe_err) && grepl("WAASBY", html(output$gxe_results)), paste("WAASB:", rv$gxe_err))
  rd <- reading_text(gxe_reading(rv$gxe)); th <- gxe_thai(rv$gxe)
  check(grepl("GEN", rd) && grepl("ENV", rd) && !grepl("พันธุ์|สถานที่", paste(rd, th)), "GxE Thai text names the genotype and environment columns")
  w50 <- html(output$gxe_waasby); th50 <- html(output$gxe_waasb_th)
  check(grepl("gxe_waasby", html(output$gxe_results)) && grepl("WAASBY", w50), "WAASB: the live table is an output of its own")
  t1 <- waasby_table()$Genotype[1]; session$setInputs(wy_live = 100)
  check(!identical(w50, html(output$gxe_waasby)) && !identical(th50, html(output$gxe_waasb_th)), "WAASB: the live table and summary follow the slider")
  check(waasby_table()$Genotype[1] == rv$gxe$table$Genotype[which.max(rv$gxe$table$Mean)], "WAASBY at 100% mean weight ranks by the mean (live slider)")
  for (v in c("rank", "quad", "pc")) { session$setInputs(waasb_view = v); check(!is.null(output$gxe_plot$src), paste("WAASB plot", v)) }
  session$setInputs(gxe_si = 15, gxe_mt_wy = 50, gxe_mgidi_on = "blup", mt_view = "mgidi")
  go("go_gxe", gxe_kind = "mtsi", gxe_trait = c("KW", "ED", "CD", "NKR", "PH"), gxe_lower = "PH", gxe_geno = "GEN", gxe_env = "ENV", gxe_rep = "REP")
  check(is.null(rv$gxe_err) && length(rv$gxe$mtsi$sel_gen) >= 1 && !is.null(rv$gxe$mgidi), paste("MTSI and MGIDI:", rv$gxe_err))
  for (v in c("mgidi", "mgidi_contri", "mtsi", "mtsi_contri")) { session$setInputs(mt_view = v); check(!is.null(output$gxe_plot$src), paste("multi-trait plot", v)) }
  check(grepl("MGIDI", doc_text(output$dl_gxe_doc)), "multi-trait: Word report")
  go("go_gxe", gxe_kind = "mtsi", gxe_trait = c("KW", "NKE", "TKW", "PH"), gxe_lower = "PH")
  check(grepl("Left out of MGIDI", html(output$gxe_results)), "MGIDI: traits without genetic variance are left out with a note")
  session$setInputs(gxe_example = "rcb11")
  go("go_gxe", gxe_kind = "fw", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Genotype")
  check(!is.null(rv$gxe_err), "GxE: a wrong choice of columns gives a message, not a crash")
  # ---- Methods page (RDstat style) ----
  check(all(vapply(method_topics, function(t) length(topic_refs(t)) > 0, NA)) && grepl("accordion", as.character(methods_topics_ui())), "Methods: every topic has its references; topics in accordions")
  check(grepl("agricolae", as.character(methods_packages_ui())), "Methods: R package table lists agricolae")
  # ---- publication plots ----
  session$setInputs(sea_src = "ex", sea_example = "rcb11", sea_method = "anova", sea_plot_style = "dot", sea_err = "ci")
  go("go_sea", sea_trait = "Yield", sea_geno = "Genotype", sea_env = "Env", sea_block = "Block")
  go("go_sea_pw", sea_pw_type = "DMRT", sea_pw_alpha = "0.05")
  g <- sea_plot_obj(); check(inherits(g, "ggplot") && any(!is.na(g$data$Group)) && grepl("DMRT", g$labels$caption), "dot plot of means with DMRT letters")
  session$setInputs(met_src = "ex", met_example = "rcb11", met_method = "anova", met_plot_type = "heat")
  go("go_met", met_trait = "Yield", met_geno = "Genotype", met_env = "Env", met_block = "Block")
  check(inherits(met_plot_obj(), "ggplot") && !is.null(output$met_plot$src), "G x E heatmap (multi-environment)")
  session$setInputs(met_plot_type = "geno", met_plot_style = "dot", met_err = "se"); check(!is.null(output$met_plot$src), "multi-environment dot plot")
  session$setInputs(gxe_src = "ex", gxe_example = "rcb11", reg_view = "quad")
  for (k in c("fw", "er")) { go("go_gxe", gxe_kind = k, gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
    check(!is.null(output$gxe_plot$src), paste("mean vs. slope four-part plot:", k)) }
  session$setInputs(reg_view = "heat"); check(!is.null(output$gxe_plot$src), "G x E heatmap (GxE screen)")
  # ---- plot style, WAASBY scenarios, variance components ----
  session$setInputs(gxe_plot_theme = "bw", gxe_plot_fsize = "10", gxe_plot_bw = TRUE)
  g <- styled_plot("gxe", gxe_plot_obj); check(inherits(g, "ggplot") && !is.null(output$gxe_plot$src), "plot style: theme, font size, black and white")
  check(!identical(rv$gxe_set, NULL) && identical(settings_of("gxe", stale_extra$gxe), rv$gxe_set), "plot style does not mark the results as changed")
  session$setInputs(gxe_plot_theme = "asis", gxe_plot_fsize = "0", gxe_plot_bw = FALSE)
  session$setInputs(gxe_example = "rcb11", waasb_view = "scen", wy_live = 50, si_live = 15)
  go("go_gxe", gxe_kind = "waasb", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
  check(!is.null(output$gxe_plot$src), "WAASBY weighting scenarios (metan::wsmp)")
  session$setInputs(sea_src = "ex", sea_example = "rcb11", sea_method = "mixed", sea_random = "random", sea_plot_style = "vc")
  go("go_sea", sea_trait = "Yield", sea_geno = "Genotype", sea_env = "Env", sea_block = "Block")
  check(!is.null(output$sea_plot$src), "variance components (single environment)")
  session$setInputs(met_src = "ex", met_example = "rcb11", met_method = "mixed", met_stage = "one", met_random = "random", met_plot_type = "vc")
  go("go_met", met_trait = "Yield", met_geno = "Genotype", met_env = "Env", met_block = "Block")
  check(!is.null(output$met_plot$src), "variance components (one-stage)")
  # ---- more metan plots ----
  session$setInputs(gxe_src = "ex", gxe_example = "rcb11", ammi_type = "eigen", gxe_dim = "std", gxe_fmt = "png")
  go("go_gxe", gxe_kind = "ammi", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
  check(!is.null(output$gxe_plot$src), "AMMI scree plot"); session$setInputs(ammi_type = "resid"); check(!is.null(output$gxe_plot$src), "AMMI residuals (metan::residual_plots)")
  check(file.size(output$dl_gxe_plot) > 1000, "download of a patchwork plot")
  session$setInputs(waasb_view = "blup")
  go("go_gxe", gxe_kind = "waasb", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
  for (v in c("blup", "eigen", "resid")) { session$setInputs(waasb_view = v); check(!is.null(output$gxe_plot$src), paste("WAASB plot", v)) }
  session$setInputs(gxe_fmt = "pdf"); check(file.size(output$dl_gxe_plot) > 1000, "download of a recorded base plot (PDF)"); session$setInputs(gxe_fmt = "png")
  session$setInputs(gxe_example = "maize", gxe_si = 15, gxe_mt_wy = 50, gxe_mgidi_on = "blup", mt_view = "corr")
  go("go_gxe", gxe_kind = "mtsi", gxe_trait = c("KW", "ED", "CD", "NKR", "PH"), gxe_lower = "PH", gxe_geno = "GEN", gxe_env = "ENV", gxe_rep = "REP")
  check(!is.null(output$gxe_plot$src), "correlations among traits (metan::corr_plot)")
  check(file.size(output$dl_gxe_plot) > 1000, "download of a GGally matrix")
  session$setInputs(mt_view = "venn"); check(!is.null(output$gxe_plot$src), "Venn of MTSI and MGIDI selections")
  # ---- metan plots in the RBTool look, style options on patchwork and GGally plots ----
  session$setInputs(gxe_plot_theme = "classic", gxe_plot_fsize = "11", gxe_plot_bw = TRUE)
  check(!is.null(output$gxe_plot$src), "style options on a GGally matrix (corr_plot)")
  session$setInputs(gxe_example = "rcb11", ammi_type = "resid")
  go("go_gxe", gxe_kind = "ammi", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
  check(inherits(styled_plot("gxe", gxe_plot_obj), "patchwork") && !is.null(output$gxe_plot$src), "style options on a patchwork (AMMI residuals)")
  session$setInputs(ammi_type = "2", gxe_plot_theme = "asis", gxe_plot_fsize = "0", gxe_plot_bw = FALSE)
  g <- gxe_plot_obj(); check(isTRUE(g$theme$legend.position == "bottom"), "metan biplot drawn with theme_rbtool")
})
cat("all server tests passed\n")
