# Run the single-environment analysis (ssa.test) cases and write a text dump per case.
# Usage: Rscript ssa_cases.R <repo-root> <outdir> [port|original]
args <- commandArgs(TRUE)
root <- args[1]; out <- args[2]; mode <- if (length(args) > 2) args[3] else "original"
if (mode == "port") suppressMessages(library(PBToolsRD)) else suppressMessages(library(PBTools))
dir.create(out, showWarnings = FALSE, recursive = TRUE)
options(digits = 7, width = 100)

rcb <- read.csv(file.path(root, "reference/data/RCB_ME.csv"), header = TRUE)
rcb$Block <- factor(rcb$Block)

# deterministic alpha-lattice (3 reps x 5 blocks x 3 plots, 15 genotypes), no RNG
al <- expand.grid(plot = 1:3, Block = 1:5, Rep = 1:3)
al$g <- ((al$Block - 1) * 3 + al$plot - 1 + (al$Rep - 1) * al$Block) %% 15 + 1
al$Geno <- paste0("G", al$g)
al$Blk <- paste0("R", al$Rep, "B", al$Block)
al$Rep <- factor(al$Rep)
al$Y <- round(50 + 3 * sin(al$g * 1.7) + 2 * cos(as.numeric(al$Rep) * 2.3 + al$Block) + 0.8 * sin(al$g * al$Block + al$plot), 3)
al$Y[c(4, 20)] <- NA

dump <- function(r, file) {
  sink(file)
  for (o in r$output) for (s in o$site) {
    cat("## site", s$env, "| respvar", o$respvar, "\n")
    cat("obsread", s$obsread, "obsused", s$obsused, "numreps", s$numreps, "\n")
    cat("formula1:", s$formula1, "\n")
    cat("lmerRun:", s$lmerRun, "\n")
    print(s$varcomp.table); print(s$varcompnRep)
    print(s$models.table)
    cat("heritability", s$heritability, "\n")
    print(s$summary.statistic)
    cat("residuals\n"); print(round(as.numeric(s$residuals), 4))
  }
  cat("## means\n"); print(r$means)
  cat("## varrep\n"); print(r$varrep)
  sink()
}
run <- function(name, expr) {
  r <- try(expr, silent = TRUE)
  if (inherits(r, "try-error")) writeLines(c("ERROR", as.character(r)), file.path(out, paste0(name, ".txt")))
  else dump(r, file.path(out, paste0(name, ".txt")))
}
run("rcb_random_multi", ssa.test("RCB", rcb, "Yield", "Genotype", "Block", env = "Env", is.random = TRUE))
run("rcb_fixed_multi",  ssa.test("RCB", rcb, "Yield", "Genotype", "Block", env = "Env", is.random = FALSE))
run("rcb_random_single", ssa.test("RCB", subset(rcb, Env == "E1"), "Yield", "Genotype", "Block", is.random = TRUE))
run("alpha_random", ssa.test("Alpha", al, "Y", "Geno", "Blk", rep = "Rep", is.random = TRUE))
run("alpha_fixed",  ssa.test("Alpha", al, "Y", "Geno", "Blk", rep = "Rep", is.random = FALSE))
