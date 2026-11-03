# metan's mixed models (gamem, gamem_met) compared with the engine (RCB_ME data). Run from the repo root.
suppressMessages({ library(metan); library(lme4) })
for (f in list.files("engine/R", full.names = TRUE)) source(f)
rcb <- read.csv("prototype/app/examples/rcb_11sites.csv"); rcb$Block <- factor(rcb$Block)
est <- function(x, p) x$ESTIMATES$Values[x$ESTIMATES$Parameters == p]

for (site in c("E1", "E2")) {
  d <- rcb[rcb$Env == site, ]
  ours <- pb_sea(d, "Yield", "Genotype", "RCB", "Block", random = TRUE)
  g <- suppressWarnings(gamem(d, Genotype, Block, Yield, verbose = FALSE))$Yield
  cat(sprintf("\n== site %s, genotype random: engine (block random, PBTools) vs metan::gamem (%s) ==\n", site, deparse(g$formula)))
  print(data.frame(engine = c(ours$summary$`Genotype variance`, ours$summary$`Error variance`, ours$summary$Heritability),
                   metan = c(est(g, "Gen_var"), est(g, "Res_var"), est(g, "h2mg")),
                   row.names = c("genotype variance", "residual variance", "heritability (entry-mean basis)")))
  m <- merge(ours$means, data.frame(Genotype = g$BLUPgen$GEN, metan = g$BLUPgen$Predicted), by = "Genotype")
  cat("BLUP max |diff|:", signif(max(abs(m$Mean - m$metan)), 3), "\n")
}

o <- pb_one_stage(rcb, "Yield", "Genotype", "Env", "RCB", "Block", random = TRUE)
w <- suppressWarnings(gamem_met(rcb, Env, Genotype, Block, Yield, verbose = FALSE))$Yield
cat("\n== one-stage, genotype random ==\nengine (PBTools model):\n"); print(o$varcomp[, c("Group", "Variance")])
cat("heritability (PBTools formula):", round(o$info$Heritability, 3), "\n")
cat(sprintf("metan::gamem_met (%s):\n", deparse(w$formula))); print(as.data.frame(w$random))
cat("h2mg:", round(est(w, "h2mg"), 3), "\n")
