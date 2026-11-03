# Compare the package-based engine (engine/R) with the PBTools port (reference/pbtools-port, itself checked against
# the original PBTools on R 2.15.1). Run from the repo root: Rscript scripts/compare_engine.R
# Needs the port installed: install.packages("reference/pbtools-port", repos = NULL, type = "source")
suppressMessages({ library(PBToolsRD); library(lme4) })
for (f in list.files("engine/R", full.names = TRUE)) source(f)
src <- readLines("scripts/designs_cases.R")
eval(parse(text = src[grep("^aug <- rbind", src):(grep("^# ---- dump ----", src) - 1)]))   # synthetic design data
rcb <- read.csv("reference/data/RCB_ME.csv")
two <- read.csv("reference/pbtools-port/inst/extdata/Two_stage.csv")

res <- data.frame(Case = character(), Quantity = character(), MaxDiff = numeric(), Tol = numeric(), stringsAsFactors = FALSE)
cmp <- function(case, what, a, b, tol = 1e-4) {
  d <- if (length(a) != length(b) || !length(a)) Inf else max(abs(as.numeric(a) - as.numeric(b)) / pmax(1, abs(as.numeric(b))))
  res[nrow(res) + 1, ] <<- list(case, what, d, tol)
}
by_key <- function(x, cols) do.call(paste, c(x[cols], sep = "|"))
match_on <- function(a, b, keys) { ka <- by_key(a, keys); kb <- by_key(b, keys); list(a = a[order(ka), ], b = b[match(sort(ka), kb), ]) }

# ---- single environment ----
sea_case <- function(case, data, trait, geno, design, block, column = NULL, rep = NULL, env = NULL, random, excl = FALSE, tol_h2 = 1e-4) {
  e <- pb_sea(data, trait, geno, design, block, column, rep, env, random = random, exclude_check = excl)
  p <- suppressWarnings(PBToolsRD::pb_ssa(data, trait, geno, design, block, column, rep, env, random = random, exclude_check = excl))
  pm <- p$means
  if (!random) { ms <- p$raw$meansse; names(ms) <- c("Genotype", "PBMean", "PBSE", "Environment"); ms$Environment <- as.character(ms$Environment); pm <- merge(pm, ms, all.x = TRUE) }
  if (is.null(env)) { pm$Environment <- NULL; p$summary$Environment <- NULL }
  keys <- intersect(c("Trait", "Environment", "Genotype"), names(e$means))
  m <- match_on(e$means, pm, keys)
  cmp(case, if (random) "BLUP" else "BLUE", m$a$Mean, m$b$Mean)
  if (!random) cmp(case, "SE of BLUE", m$a$SE, m$b$PBSE)
  cmp(case, "error variance", e$summary$`Error variance`, p$summary$`Error variance`)
  if (random) cmp(case, "heritability (PBTools rounds to 2 decimals)", round(e$summary$Heritability, 2), p$summary$Heritability, 0.011)
  if (random) cmp(case, "LRT chi-square", e$lrt$Chisq[!is.na(e$lrt$Chisq)], as.numeric(p$lrt$Chisq[p$lrt$Model == "Model1"]), 1e-3)
}
rcb$Block <- factor(rcb$Block)
sea_case("RCB 11 sites, random", rcb, "Yield", "Genotype", "RCB", "Block", env = "Env", random = TRUE)
sea_case("RCB 11 sites, fixed", rcb, "Yield", "Genotype", "RCB", "Block", env = "Env", random = FALSE)
sea_case("Alpha-lattice, random", al, "Y", "Geno", "Alpha", "Row", rep = "Rep", random = TRUE)
sea_case("Alpha-lattice, fixed", al, "Y", "Geno", "Alpha", "Row", rep = "Rep", random = FALSE)
sea_case("Row-column 2 envs, random", rc, "Y", "Geno", "RowCol", "Row", "Col", "Rep", env = "Env", random = TRUE)
sea_case("Row-column, fixed", rc1, "Y", "Geno", "RowCol", "Row", "Col", "Rep", random = FALSE)
sea_case("Augmented RCB, fixed", aug, "Y", "Entry", "AugRCB", "Block", random = FALSE)
sea_case("Augmented RCB, random, checks excluded", aug, "Y", "Entry", "AugRCB", "Block", random = TRUE, excl = TRUE)
sea_case("Augmented latin square, fixed", augls, "Y", "Entry", "AugLS", "Row", "Col", random = FALSE)
sea_case("Augmented latin square, random, checks excluded", augls, "Y", "Entry", "AugLS", "Row", "Col", random = TRUE, excl = TRUE)
sea_case("Latinized alpha, random", al, "Y", "Geno", "LatinAlpha", "Row", rep = "Rep", random = TRUE)
sea_case("Latinized row-column, fixed", rcl, "Y", "Geno", "LatinRowCol", "Row", "Col", "Rep", random = FALSE)

# ---- pairwise comparisons (multcomp) ----
e <- pb_sea(rc1, "Y", "Geno", "RowCol", "Row", "Col", "Rep", random = FALSE)
p <- suppressWarnings(PBToolsRD::pb_ssa(rc1, "Y", "Geno", "RowCol", "Row", "Col", "Rep", random = FALSE))
for (ty in c("Tukey", "Dunnett")) {
  # multcomp computes the critical value of the intervals numerically (randomised): same seed for both
  set.seed(1); a <- pb_pairwise(e$models, "Geno", ty, 0.05, control = "G01")$table
  set.seed(1); b <- suppressWarnings(PBToolsRD::pb_pairwise(p$models, ty, 0.05, control = "G01"))$table
  k <- function(x) paste(x$`Genotype i`, x$`Genotype j`)
  cmp(paste("Pairwise", ty, "(multcomp)"), "same significant pairs", as.numeric(setequal(k(a), k(b))), 1, 0)
  m <- match(k(b), k(a))
  cmp(paste("Pairwise", ty, "(multcomp)"), "difference and interval limits", c(a$Difference[m], a$Lower[m], a$Upper[m]), c(b$Difference, b$Lower, b$Upper), 1e-3)
}

if (file.exists("engine/R/pb_met.R")) source("scripts/compare_engine_met.R")

res$OK <- res$MaxDiff <= res$Tol
res$MaxDiff <- signif(res$MaxDiff, 3)
print(res, right = FALSE)
cat(sprintf("\n%d of %d comparisons within tolerance\n", sum(res$OK), nrow(res)))
if (!all(res$OK)) quit(status = 1)
