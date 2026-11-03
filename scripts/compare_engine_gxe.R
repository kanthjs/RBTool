# GxE part of compare_engine.R (sourced from there; uses cmp(), match_on(), rcb, two)
s1p <- suppressWarnings(PBToolsRD::pb_stage1(rcb, "Yield", "Genotype", "RCB", "Block", env = "Env"))

fw <- pb_fw(rcb, "Yield", "Genotype", "Env", "Block")
pfw <- suppressWarnings(PBToolsRD::pb_stability(s1p, "Yield_Mean", method = "regression"))$table
m <- match_on(fw$table, pfw, "Genotype")
cmp("Finlay-Wilkinson (metan::ge_reg)", "slope", m$a$Slope, m$b$Slope)
cmp("Finlay-Wilkinson (metan::ge_reg)", "genotype mean", m$a$Mean, m$b$Mean)

cell <- data.frame(GEN = factor(two$Genotype), ENV = factor(two$Env), Y = two$Yield_Mean)
sg <- pb_shukla_gls(cell)
psh <- suppressWarnings(PBToolsRD::pb_stability(two, "Yield_Mean", method = "shukla"))$table
m <- match_on(sg, psh, "Genotype")
cmp("Shukla, PBTools model (nlme::gls)", "estimate", m$a$Estimate, m$b$est.)

am <- pb_ammi(rcb, "Yield", "Genotype", "Env", "Block")
pam <- suppressWarnings(PBToolsRD::pb_multiplicative(s1p, "Yield_Mean", "Yield_sigma2", "Yield_No.rep", model = "AMMI"))
pcs <- am$anova[grepl("^PC", am$anova$Source), ]
cmp("AMMI (metan::performs_ammi)", "% of GxE per PC", pcs$`% of GxE`[1:5], pam$analysis$percent[1:5], 1e-3)
ga <- am$scores[am$scores$type == "GEN", ]; gp <- pam$biplot[pam$biplot$type == "GEN", ]
m <- match_on(data.frame(Genotype = ga$Name, PC1 = ga$PC1, PC2 = ga$PC2), data.frame(Genotype = gp$Name, PC1 = gp$PC1, PC2 = gp$PC2), "Genotype")
cmp("AMMI (metan::performs_ammi)", "genotype scores PC1, PC2 (sign is arbitrary)", abs(c(m$a$PC1, m$a$PC2)), abs(c(m$b$PC1, m$b$PC2)), 1e-3)

gg <- pb_gge(rcb, "Yield", "Genotype", "Env", "Block")
pgg <- suppressWarnings(PBToolsRD::pb_multiplicative(s1p, "Yield_Mean", "Yield_sigma2", "Yield_No.rep", model = "GGE"))
cmp("GGE (metan::gge)", "% explained by PC1-PC3", round(gg$explained$`% explained`[1:3], 1), pgg$analysis$percent[1:3], 1e-3)
