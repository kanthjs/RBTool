# Compare established packages with the PBTools port for multi-environment, stability and AMMI/GGE (RCB_ME data)
suppressMessages({ library(PBToolsRD); library(lme4); library(emmeans); library(metan); library(agricolae); library(statgenSTA); library(statgenGxE) })
options(width = 140)
rcb <- read.csv("reference/data/RCB_ME.csv"); rcb$Block <- factor(rcb$Block); rcb$Env <- factor(rcb$Env); rcb$Genotype <- factor(rcb$Genotype)
s1 <- pb_stage1(rcb, "Yield", "Genotype", "RCB", "Block", env = "Env")
mx <- function(a, b) signif(max(abs(a - b)), 3)

# ---- one-stage: lme4 + emmeans vs PBTools port ----
o <- pb_one_stage(rcb, "Yield", "Genotype", "RCB", "Block", env = "Env", random = FALSE)
m <- lme4::lmer(Yield ~ Genotype + (1 | Env) + (1 | Env:Block) + (1 | Genotype:Env), data = rcb)
em <- as.data.frame(emmeans(m, "Genotype"))
x <- merge(o$geno, em, by = "Genotype")
cat("\n== one-stage LSMeans: PBTools vs emmeans  max|diff| mean", mx(x$LSMean, x$emmean), " SE", mx(x$StdErrMean, x$SE), "\n")
print(head(x[, c("Genotype", "LSMean", "emmean", "StdErrMean", "SE")], 3))

# ---- two-stage: lme4 on stage-1 means vs PBTools port; statgenGxE::gxeVarComp ----
t2 <- pb_two_stage(s1, "Yield_Mean", NULL, "Yield_sigma2", "Yield_No.rep", random = TRUE)
m2 <- lme4::lmer(Yield_Mean ~ (1 | Genotype) + (1 | Env), data = s1)
cat("\n== two-stage variance components: PBTools vs lme4 ==\n"); print(t2$varcomp); print(as.data.frame(lme4::VarCorr(m2))[, c("grp", "vcov")])
TD2 <- createTD(s1, genotype = "Genotype", trial = "Env")
vcm <- gxeVarComp(TD2, trait = "Yield_Mean", engine = "lme4")
cat("statgenGxE::gxeVarComp:\n"); print(vcm$fullRandVC)

# ---- Finlay-Wilkinson: PBTools vs metan::ge_reg (Eberhart & Russell) vs statgenGxE::gxeFw ----
fw <- pb_stability(s1, "Yield_Mean", method = "regression")$table
er <- ge_reg(rcb, Env, Genotype, Block, Yield, verbose = FALSE)$Yield$regression
x <- merge(fw[, c("Genotype", "Slope", "MSDev")], data.frame(Genotype = er$GEN, b1 = er$b1, s2di = er$s2di))
cat("\n== Finlay-Wilkinson slope: PBTools vs metan::ge_reg  max|diff|", mx(x$Slope, x$b1), "\n"); print(head(x, 4))
TDr <- createTD(rcb, genotype = "Genotype", trial = "Env", repId = "Block")
gfw <- gxeFw(TD2, trait = "Yield_Mean")
y <- merge(fw[, c("Genotype", "Slope")], data.frame(Genotype = gfw$estimates$Genotype, sens = gfw$estimates$Sens))
cat("statgenGxE::gxeFw (sensitivity, joint fit of env effects) max|diff|", mx(y$Slope, y$sens), "\n"); print(head(y, 3))

# ---- Shukla: PBTools (gls, varIdent) vs metan::Shukla vs agricolae::stability.par ----
ts <- read.csv("reference/pbtools-port/inst/extdata/Two_stage.csv")
sh <- pb_stability(ts, "Yield_Mean", method = "shukla")$table
tsr <- ts; names(tsr)[1:3] <- c("ENV", "GEN", "Y")
ms <- tapply(ts$Yield_Mean, list(ts$Genotype, ts$Env), mean)
sp <- stability.par(ms, rep = 3, MSerror = mean(ts$Yield_sigma2), console = FALSE)
cat("\n== Shukla: PBTools est. vs agricolae::stability.par sigma2 ==\n")
z <- merge(sh[, c("Genotype", "est.")], data.frame(Genotype = rownames(ms), sigma2 = sp$statistics[["Sigma-square"]]))
print(head(z, 5)); cat("Spearman (PBTools est.^2, agricolae sigma2):", round(cor(z$est.^2, z$sigma2, method = "spearman"), 3), "\n")
ms2 <- metan::Shukla(rcb, Env, Genotype, Block, Yield, verbose = FALSE)$Yield
z2 <- merge(z, data.frame(Genotype = ms2$GEN, metan = ms2$ShuklaVar))
cat("metan::Shukla vs agricolae (raw RCB_ME data, not the same data as the PBTools est. above):\n"); print(head(z2, 3))

# ---- AMMI: PBTools ammi.analysis2 vs agricolae::AMMI (raw) vs metan::performs_ammi ----
a <- pb_multiplicative(s1, "Yield_Mean", "Yield_sigma2", "Yield_No.rep", model = "AMMI")
ag <- with(rcb, AMMI(Env, Genotype, Block, Yield, console = FALSE))
cat("\n== AMMI % of GxE SS by PC: PBTools vs agricolae::AMMI vs metan ==\n")
pm <- performs_ammi(rcb, Env, Genotype, Block, Yield, verbose = FALSE)$Yield$ANOVA
print(head(as.data.frame(pm), 8))
print(data.frame(PC = a$analysis$PC[1:5], PBTools = a$analysis$percent[1:5], agricolae = ag$analysis$percent[1:5]))
cat("F of PC1-3, PBTools:", head(a$analysis$F.value, 3), " agricolae:", head(ag$analysis$F.value, 3), "\n")
cat("genotype scores PC1 max|diff| (abs, sign is arbitrary):", mx(abs(a$biplot$PC1[a$biplot$type == "GEN"]), abs(ag$biplot$PC1[ag$biplot$type == "GEN"])), "\n")

# ---- GGE: PBTools gge.analysis2 vs metan::gge ----
g <- pb_multiplicative(s1, "Yield_Mean", "Yield_sigma2", "Yield_No.rep", model = "GGE")
mg <- gge(s1, Env, Genotype, Yield_Mean, scaling = "none", centering = "environment", svp = "symmetrical")
cat("\n== GGE % explained: PBTools", head(g$analysis$percent, 3), " metan::gge", head(round(mg$Yield_Mean$varexpl, 1), 3), "\n")
sg <- statgenGxE::gxeGGE(TD2, trait = "Yield_Mean")
cat("statgenGxE::gxeGGE % explained:", head(round(sg$importance[2, ] * 100, 1), 3), "\n")
