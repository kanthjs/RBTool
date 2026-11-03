# GxE cases: stability.analysis (regression, shukla), ammi.analysis(2), gge.analysis(2).
# Usage: Rscript gxe_cases.R <repo-root> <outdir> [port|original]
args <- commandArgs(TRUE)
root <- args[1]; out <- args[2]; mode <- if (length(args) > 2) args[3] else "original"
if (mode == "port") suppressMessages(library(PBToolsRD)) else suppressMessages(library(PBTools))
dir.create(out, showWarnings = FALSE, recursive = TRUE)
options(digits = 7, width = 100)
ext <- file.path(root, "reference/pbtools-port/inst/extdata")
ts <- read.csv(file.path(ext, "Two_stage.csv")); mm <- read.csv(file.path(ext, "RCB_Multi.csv"))
walk <- function(x, name = "") {
  if (is.null(x)) { cat("##", name, "NULL\n"); return(invisible()) }
  if (is.list(x) && !is.data.frame(x)) {
    for (n in seq_along(x)) walk(x[[n]], paste0(name, "/", if (is.null(names(x)) || !nzchar(names(x)[n])) n else names(x)[n]))
  } else if (is.data.frame(x) || is.matrix(x) || inherits(x, "by")) { cat("##", name, "\n"); print(as.data.frame(unclass(x)), digits = 5)
  } else if (is.numeric(x)) { cat("##", name, "\n"); cat(signif(as.numeric(x), 6), fill = 100)
  } else { cat("##", name, "\n"); print(x) }
}
run <- function(name, expr) {
  grDevices::pdf(NULL); r <- try(expr, silent = TRUE); grDevices::dev.off()
  sink(file.path(out, paste0(name, ".txt")))
  if (inherits(r, "try-error")) cat("ERROR\n", as.character(r), "\n") else walk(r, "result")
  sink()
}
run("stab_regression", stability.analysis(ts, "Yield_Mean", "Genotype", "Env", method = "regression"))
# interval bounds (lower, upper) depend on the nlme version (3.1-104 vs 3.1-168); only the estimates are compared
shukla <- function() { r <- stability.analysis(ts, "Yield_Mean", "Genotype", "Env", method = "shukla"); r[[1]][[1]]$stability <- r[[1]][[1]]$stability["est."]; r }
run("stab_shukla", shukla())
run("ammi2_twostage",  ammi.analysis2(ts$Env, ts$Genotype, ts$Yield_No.rep, ts$Yield_Mean, ts$Yield_sigma2, number = FALSE, respVar = "Yield"))
run("gge2_twostage",   gge.analysis2(ts$Env, ts$Genotype, ts$Yield_No.rep, ts$Yield_Mean, ts$Yield_sigma2, number = FALSE, respVar = "Yield"))
run("ammi_raw", ammi.analysis(mm$Site, mm$Gen, 4, mm$Y1, MSE = 13.6, number = FALSE, yVar = "Y1"))
run("gge_raw",  gge.analysis(mm$Site, mm$Gen, 4, mm$Y1, MSE = 13.6, number = FALSE, yVar = "Y1"))
