# Headless test of the app's server logic (native R). Run from anywhere: Rscript prototype/tests/test_server.R
# (run scripts/build_app.R first so that prototype/app/engine exists)
here <- dirname(normalizePath(sub("--file=", "", grep("--file=", commandArgs(FALSE), value = TRUE))))
setwd(file.path(here, "..", "app"))
suppressMessages(library(shiny))
app <- source("app.R")$value
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
  session$setInputs(sea_src = "ex", sea_example = "rcb11", sea_design = "RCB")
  check(grepl("No results|ยังไม่มีผล", html(output$sea_msg)), "single env: empty state before the first run")
  go("go_sea", sea_trait = "Yield", sea_geno = "Genotype", sea_env = "Env", sea_block = "Block", sea_random = "random")
  r <- rv$sea
  check(is.null(rv$sea_err) && nrow(r$summary) == 11, "single env random: 11 environments")
  check(abs(r$summary$Heritability[r$summary$Environment == "E2"] - 0.95) < 0.01, "single env random: heritability E2 = 0.95 (PBTools)")
  check(grepl("Heritability", html(output$sea_results)) && grepl("result-card", html(output$sea_results)), "single env: key numbers and result cards")
  go("go_sea", sea_random = "fixed")
  check(!is.null(rv$sea$models) && length(rv$sea$models) == 11, "single env fixed: one model per environment for the comparisons")
  session$setInputs(sea_pw_type = "Tukey", sea_pw_alpha = "0.05", sea_pw_all = FALSE, sea_pw_control = character())
  go("go_sea_pw")
  check(grepl("Letter groups", html(output$sea_pw_out)) && grepl("Tukey comparisons", html(output$sea_pw_out)), "Tukey: letters and significant pairs")
  session$setInputs(sea_pw_type = "Dunnett", sea_pw_control = "GEN1"); go("go_sea_pw")
  check(grepl("Dunnett comparisons", html(output$sea_pw_out)), "Dunnett against GEN1")
  check(!is.null(output$sea_plot) && !is.null(output$sea_diag), "single env: plot and residual checks render")
  html_doc <- doc_text(output$dl_sea_doc)
  check(grepl("Single-environment analysis", html_doc) && grepl("lme4", html_doc) && grepl("GEN1", html_doc), "single env: Word report")
  check(file.exists(output$dl_sea_xlsx), "single env: Excel file")
  session$setInputs(sea_random = "random")
  check(grepl("settings have changed|การตั้งค่าเปลี่ยนไป", html(output$sea_msg)), "single env: changed settings are flagged")

  # ---- the other designs ----
  for (k in c("alpha", "rowcol", "augrcb", "augls", "latinrc")) {
    e <- examples[[k]]
    session$setInputs(sea_example = k, sea_design = e$design)
    go("go_sea", sea_trait = e$trait, sea_geno = e$geno, sea_env = e$env %||% "", sea_block = e$block, sea_col = e$column %||% "", sea_rep = e$rep %||% "",
       sea_random = "random", sea_excl = TRUE)
    check(is.null(rv$sea_err), paste("single env:", pb_designs[[e$design]], rv$sea_err))
  }

  # ---- multi-environment ----
  session$setInputs(met_src = "ex", met_example = "rcb11", met_design = "RCB", met_method = "one")
  go("go_met", met_trait = "Yield", met_geno = "Genotype", met_env = "Env", met_block = "Block", met_random = "random")
  check(is.null(rv$met_err) && !is.na(rv$met$info$Heritability), paste("one-stage random: heritability across sites", rv$met_err))
  check(grepl("GxE interaction", html(output$met_results)), "one-stage: GxE test in the key numbers")
  session$setInputs(met_method = "two", met_s1 = "calc", met_weight = "none")
  go("go_met", met_random = "fixed")
  check(is.null(rv$met_err) && nrow(rv$met$stage1) == 165, paste("two-stage from plot data: 165 stage-1 means", rv$met_err))
  session$setInputs(met_pw_type = "Tukey", met_pw_alpha = "0.05", met_pw_all = TRUE); go("go_met_pw")
  check(grepl("Tukey comparisons", html(output$met_pw_out)), "two-stage: Tukey comparison")
  session$setInputs(met_example = "stage1", met_s1 = "file", met_weight = "stderr")
  go("go_met", met_s1geno = "Genotype", met_s1env = "Env", met_s1mean = "Yield_Mean", met_s1se = "Yield_StdErrMean", met_s1err = "Yield_sigma2", met_s1reps = "Yield_No.rep", met_random = "random")
  check(is.null(rv$met_err) && grepl("1/SE", html(output$met_results)), paste("two-stage from a file of means, weighted", rv$met_err))
  check(grepl("Multi-environment analysis", doc_text(output$dl_met_doc)), "multi-environment: Word report")

  # ---- GxE ----
  session$setInputs(gxe_src = "ex", gxe_example = "rcb11", gge_type = "1", gge_scaling = "none", gge_svp = "environment", ammi_type = "2", shukla_pbtools = TRUE)
  for (k in c("fw", "shukla", "stats", "ammi", "gge")) {
    go("go_gxe", gxe_kind = k, gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Block")
    check(is.null(rv$gxe_err) && !is.null(output$gxe_plot), paste("GxE:", k, rv$gxe_err))
  }
  check(grepl("PC1", doc_text(output$dl_gxe_doc)), "GxE: Word report")
  go("go_gxe", gxe_kind = "fw", gxe_trait = "Yield", gxe_geno = "Genotype", gxe_env = "Env", gxe_rep = "Genotype")
  check(!is.null(rv$gxe_err), "GxE: a wrong choice of columns gives a message, not a crash")
})
cat("all server tests passed\n")
