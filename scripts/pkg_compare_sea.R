# Compare established packages with the PBTools port for single-environment analysis (RCB_ME data, 11 sites)
suppressMessages({ library(PBToolsRD); library(statgenSTA); library(lme4); library(emmeans) })
options(width = 140)
rcb <- read.csv("reference/data/RCB_ME.csv"); rcb$Block <- factor(rcb$Block)

# PBTools port (checked against the original)
pbR <- pb_ssa(rcb, "Yield", "Genotype", "RCB", "Block", env = "Env", random = TRUE)
pbF <- pb_ssa(rcb, "Yield", "Genotype", "RCB", "Block", env = "Env", random = FALSE)

# statgenSTA
TD <- createTD(rcb, genotype = "Genotype", trial = "Env", repId = "Block")
fit <- fitTD(TD, traits = "Yield", design = "rcbd", engine = "lme4", what = c("fixed", "random"))
blue <- extractSTA(fit, what = "BLUEs"); blup <- extractSTA(fit, what = "BLUPs"); h2 <- extractSTA(fit, what = "heritability")
sev <- extractSTA(fit, what = "seBLUEs")

# heritability
h <- merge(pbR$summary[, c("Environment", "Heritability")], data.frame(Environment = h2$trial, statgen = round(h2$Yield, 2)))
cat("\n== heritability: PBTools vs statgenSTA ==\n"); print(h)

# BLUEs and their SE
pm <- pbF$raw$meansse; names(pm) <- c("genotype", "PB_BLUE", "PB_SE", "trial")
m <- merge(pm, data.frame(genotype = blue$genotype, trial = blue$trial, ST_BLUE = blue$Yield))
m <- merge(m, data.frame(genotype = sev$genotype, trial = sev$trial, ST_SE = sev$Yield))
cat("\n== BLUE max |diff| ==", max(abs(m$PB_BLUE - m$ST_BLUE)), " SE max |diff| ==", max(abs(m$PB_SE - m$ST_SE)), "\n")

# BLUPs (PBTools: coef(model)$Genotype = intercept + BLUP)
pu <- pbR$means; names(pu) <- c("Trait", "trial", "genotype", "PB_BLUP")
u <- merge(pu, data.frame(genotype = blup$genotype, trial = blup$trial, ST_BLUP = blup$Yield))
cat("== BLUP max |diff| ==", max(abs(u$PB_BLUP - u$ST_BLUP)), "\n"); print(head(u, 4))

# variance components
vc <- extractSTA(fit, what = "varGen"); ve <- extractSTA(fit, what = "varErr")
v <- merge(pbR$varcomp[pbR$varcomp$Groups == "Genotype", c("Environment", "Variance")], data.frame(Environment = vc$trial, statgen = vc$Yield))
v <- merge(v, merge(pbR$varcomp[pbR$varcomp$Groups == "Residual", c("Environment", "Variance")], data.frame(Environment = ve$trial, statgenErr = ve$Yield), by = "Environment"), by = "Environment")
cat("\n== variance components (genotype, residual) ==\n"); print(v)
