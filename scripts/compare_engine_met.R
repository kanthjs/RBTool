# Multi-environment part of compare_engine.R (sourced from there; uses cmp(), match_on(), rcb, two, rc)
met_case <- function(case, data, traits, geno, env, design, block, column = NULL, rep = NULL, random) {
  e <- pb_one_stage(data, traits, geno, env, design, block, column, rep, random = random)
  # the port's wrapper is run one trait at a time (its LRT tables of different traits cannot be bound together)
  ps <- lapply(traits, function(tr) suppressWarnings(PBToolsRD::pb_one_stage(data, tr, geno, design, block, column, rep, env, random = random)))
  p <- lapply(c(geno = "geno", varcomp = "varcomp", lrt = "lrt", info = "info", genoenv = "genoenv"), function(k) {
    l <- lapply(ps, function(x) { y <- x[[k]]; if (k == "genoenv") names(y) <- c("Trait", "G", "E", "Mean"); y }); do.call(rbind, l) })
  pg <- p$geno; names(pg)[2] <- "Genotype"; names(pg)[names(pg) %in% c("LSMean", "Mean")] <- "Mean"
  m <- match_on(e$geno, pg, c("Trait", "Genotype"))
  cmp(case, if (random) "genotype BLUP" else "genotype LSMean", m$a$Mean, m$b$Mean)
  if (!random) cmp(case, "SE of LSMean", m$a$SE, m$b$StdErrMean)
  ev <- e$varcomp; pv <- p$varcomp; pv$Group <- pv$Groups
  m <- match_on(ev, pv, c("Trait", "Group"))
  cmp(case, "variance components", m$a$Variance, m$b$Variance, 1e-3)
  if (random) cmp(case, "heritability (PBTools rounds to 2 decimals)", round(e$info$Heritability, 2), p$info$Heritability, 0.011)
  pl <- p$lrt[p$lrt$Model == "Model1", ]; el <- e$lrt[!is.na(e$lrt$Chisq), ]
  keep <- if (random) c("Geno", "Env", "GenoEnv") else c("Env", "GenoEnv")   # fixed genotype: see PORTING.md (old lme4 optimum)
  el$Test <- c(Genotype = "Geno", Environment = "Env", `Genotype x Environment` = "GenoEnv")[el$Test]
  m <- match_on(el[el$Test %in% keep, ], pl[pl$Test %in% keep, ], c("Trait", "Test"))
  cmp(case, "LRT chi-square (environment, GxE)", as.numeric(m$a$Chisq), as.numeric(m$b$Chisq), 1e-2)
  pge <- p$genoenv; names(pge) <- c("Trait", "Genotype", "Environment", "Mean")
  pge <- pge[, c("Trait", "Genotype", "Environment", "Mean")]
  m <- match_on(e$genoenv, pge, c("Trait", "Genotype", "Environment"))
  cmp(case, "genotype x environment means", m$a$Mean, m$b$Mean, 1e-3)
}
multi <- read.csv("reference/pbtools-port/inst/extdata/RCB_Multi.csv")
met_case("One-stage RCB 11 sites, fixed", rcb, "Yield", "Genotype", "Env", "RCB", "Block", random = FALSE)
met_case("One-stage RCB 11 sites, random", rcb, "Yield", "Genotype", "Env", "RCB", "Block", random = TRUE)
met_case("One-stage RCB 2 sites 2 traits, random", multi, c("Y1", "Y2"), "Gen", "Site", "RCB", "Blk", random = TRUE)
met_case("One-stage row-column 2 envs, fixed", rc, "Y", "Geno", "Env", "RowCol", "Row", "Col", "Rep", random = FALSE)
rcx <- read.csv("prototype/app/examples/rowcol_2env.csv")
met_case("One-stage row-column (app example), random", rcx, "Y", "Geno", "Env", "RowCol", "Row", "Col", "Rep", random = TRUE)

two_case <- function(case, weight, random) {
  e <- pb_two_stage(two, "Yield_Mean", "Yield_StdErrMean", "Yield_sigma2", "Yield_No.rep", "Genotype", "Env", weight = weight, random = random)
  p <- suppressWarnings(PBToolsRD::pb_two_stage(two, "Yield_Mean", "Yield_StdErrMean", "Yield_sigma2", "Yield_No.rep", "Genotype", "Env", weight = weight, random = random))
  pg <- p$geno; names(pg)[names(pg) %in% c("LSMean", "Mean")] <- "Mean"
  m <- match_on(e$geno, pg, "Genotype")
  cmp(case, if (random) "genotype BLUP" else "genotype mean", m$a$Mean, m$b$Mean)
  if (!random && weight == "none") cmp(case, "SE of mean", m$a$SE, m$b$StdErrMean)
  pv <- p$varcomp; pv$Group <- pv$Groups; m <- match_on(e$varcomp, pv, "Group")
  cmp(case, "variance components", m$a$Variance, m$b$Variance, 1e-3)
  if (random) cmp(case, "heritability (PBTools rounds to 2 decimals)", round(e$info$Heritability, 2), as.numeric(p$info$Heritability), 0.011)
}
two_case("Two-stage, no weight, fixed", "none", FALSE)
two_case("Two-stage, no weight, random", "none", TRUE)
two_case("Two-stage, 1/SE^2, fixed", "stderr", FALSE)
two_case("Two-stage, 1/SE^2, random", "stderr", TRUE)

if (file.exists("engine/R/pb_gxe.R")) source("scripts/compare_engine_gxe.R")
