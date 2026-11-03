# Multi-environment cases (one-stage GEOneStage.test, two-stage GETwoStage.test).
# Usage: Rscript ms_cases.R <repo-root> <outdir> [port|original]
args <- commandArgs(TRUE)
root <- args[1]; out <- args[2]; mode <- if (length(args) > 2) args[3] else "original"
if (mode == "port") suppressMessages(library(PBToolsRD)) else suppressMessages(library(PBTools))
dir.create(out, showWarnings = FALSE, recursive = TRUE)
options(digits = 7, width = 100)
ext <- file.path(root, "reference/pbtools-port/inst/extdata")
mm <- read.csv(file.path(ext, "RCB_Multi.csv")); mm$Blk <- factor(mm$Blk); mm$Gen <- factor(mm$Gen)
ts <- read.csv(file.path(ext, "Two_stage.csv"))

# print every element recursively, skipping fitted model objects and the echoed data
walk <- function(x, name = "", keep = NULL, skip = NULL) {
  if (!is.null(skip) && grepl(skip, name)) return(invisible())
  if (!is.null(keep) && (!is.list(x) || is.data.frame(x)) && !grepl(keep, name)) return(invisible())
  if (!is.null(keep) && grepl("means[.]Geno$", name)) x <- x[, 1:2]
  if (is.null(x)) { cat("##", name, "NULL\n"); return(invisible()) }
  if (is.list(x) && !is.data.frame(x)) {
    for (n in seq_along(x)) walk(x[[n]], paste0(name, "/", if (is.null(names(x)) || !nzchar(names(x)[n])) n else names(x)[n]), keep, skip)
  } else if (isS4(x) || inherits(x, c("merMod", "mer", "lm"))) {
    cat("##", name, "(model skipped)\n")
  } else if (grepl("/data$", name)) {
    cat("##", name, "(data skipped)\n")
  } else if (is.numeric(x) && is.null(dim(x))) {
    cat("##", name, "\n"); print(round(as.numeric(x), 4))
  } else { cat("##", name, "\n"); print(x) }
}
run <- function(name, expr, keep = NULL, skip = NULL) {
  r <- try(expr, silent = TRUE)
  sink(file.path(out, paste0(name, ".txt")))
  if (inherits(r, "try-error")) cat("ERROR\n", as.character(r), "\n") else walk(r, "result", keep, skip)
  sink()
}
KEEP_STDERR <- "/(respvar|obsread|obsused|formula1|formula2|means[.]Env|fitted[.]values|means[.]Geno)$"
run("one_fixed",  GEOneStage.test("RCB", mm, c("Y1", "Y2"), "Gen", "Blk", env = "Site", is.genoRandom = FALSE), skip = "testsig|output/2/varcomp[.]table")
run("one_random", GEOneStage.test("RCB", mm, c("Y1", "Y2"), "Gen", "Blk", env = "Site", is.genoRandom = TRUE))
for (w in c("none", "stderr")) for (g in c(FALSE, TRUE)) {
  se <- if (w == "none") NULL else "Yield_StdErrMean"
  run(sprintf("two_%s_%s", w, if (g) "random" else "fixed"),
      GETwoStage.test(ts, "Yield_Mean", se, "Yield_sigma2", "Yield_No.rep", "Genotype", "Env", weight = w, is.genoRandom = g), keep = if (w == "stderr") KEEP_STDERR)
}
