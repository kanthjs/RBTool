# Genotype x environment and stability (PBTools: Analysis > GxE: stability models and multiplicative models).
# Computed with the metan package (Olivoto & Lucio 2020) on plot data with replicates; the PBTools form of Shukla's
# model is fitted with nlme for comparison.

pb_gxe_data <- function(data, trait, geno, env, rep) {
  pb_check_cols(data, trait, geno, env, rep)
  d <- data.frame(ENV = pb_factor(data[[env]]), GEN = pb_factor(data[[geno]]), REP = pb_factor(data[[rep]]), Y = pb_num(data[[trait]]))
  d <- d[!is.na(d$Y), ]
  if (nlevels(droplevels(d$ENV)) < 3) stop("GxE and stability analyses need at least three environments.", call. = FALSE)
  if (nlevels(droplevels(d$GEN)) < 3) stop("GxE and stability analyses need at least three genotypes.", call. = FALSE)
  if (any(table(d$GEN, d$ENV) == 0)) stop("Every genotype must be tested in every environment (the data are not complete).", call. = FALSE)
  droplevels(d)
}
pb_metan <- function(expr) suppressMessages(suppressWarnings(expr))

# Finlay-Wilkinson / Eberhart-Russell regression on the environmental index (metan::ge_reg). b1 is the PBTools slope.
pb_fw <- function(data, trait, geno, env, rep) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  r <- pb_metan(metan::ge_reg(d, ENV, GEN, REP, Y, verbose = FALSE))$Y
  g <- as.data.frame(r$regression)
  tab <- data.frame(Genotype = g$GEN, Mean = g$b0, Slope = g$b1, `t (b1 = 1)` = as.numeric(g[["t(b1=1)"]]), `p (b1 = 1)` = as.numeric(g$pval_t),
                    `Deviation variance (s2di)` = g$s2di, `F (s2di = 0)` = g[["F(s2di=0)"]], `p (s2di = 0)` = g$pval_f, R2 = g$R2, RMSE = g$RMSE, check.names = FALSE)
  cell <- stats::aggregate(Y ~ GEN + ENV, d, mean)
  idx <- stats::aggregate(Y ~ ENV, d, mean); idx$Index <- idx$Y - mean(d$Y)
  cell$Index <- idx$Index[match(cell$ENV, idx$ENV)]
  list(table = tab, anova = as.data.frame(r$anova), cell = setNames(cell, c("Genotype", "Environment", "Mean", "Index")),
       env_index = data.frame(Environment = idx$ENV, Mean = idx$Y, Index = idx$Index), trait = trait)
}

# Shukla's stability variance (Shukla 1972) with metan; and the model PBTools uses: residual variance per genotype of the
# additive model fitted to the genotype x environment means (nlme::gls with varIdent), with its confidence interval.
pb_shukla <- function(data, trait, geno, env, rep) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  s <- as.data.frame(pb_metan(metan::Shukla(d, ENV, GEN, REP, Y, verbose = FALSE))$Y)
  tab <- data.frame(Genotype = as.character(s$GEN), Mean = s$Y, `Shukla variance` = s$ShuklaVar, `Rank of mean` = s$rMean,
                    `Rank of Shukla variance` = s$rShukaVar, `Sum of ranks (Kang)` = s$ssiShukaVar, check.names = FALSE)
  cell <- stats::aggregate(Y ~ GEN + ENV, d, mean)
  pb <- tryCatch(pb_shukla_gls(cell), error = function(e) conditionMessage(e))
  list(table = tab, pbtools = pb, trait = trait)
}
pb_shukla_gls <- function(cell) {
  m <- nlme::gls(Y ~ GEN + ENV, data = cell, weights = nlme::varIdent(form = ~ 1 | GEN))
  iv <- nlme::intervals(m)
  ratio <- rbind(c(1, 1, 1), as.matrix(iv$varStruct)); sig <- iv$sigma
  lv <- levels(cell$GEN); first <- setdiff(lv, sub("^.*\\.", "", rownames(iv$varStruct)))
  rownames(ratio) <- c(first, sub("^.*\\.", "", rownames(iv$varStruct)))
  out <- data.frame(Genotype = rownames(ratio), Lower = ratio[, 1] * sig[["lower"]], Estimate = ratio[, 2] * sig[["est."]], Upper = ratio[, 3] * sig[["upper"]])
  out <- out[match(lv, out$Genotype), ]; rownames(out) <- NULL
  out
}

# Several stability statistics in one table (metan::ge_stats), with ranks
pb_stability_stats <- function(data, trait, geno, env, rep) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  s <- as.data.frame(pb_metan(metan::ge_stats(d, ENV, GEN, REP, Y, verbose = FALSE))$Y)
  keep <- c(Genotype = "GEN", Mean = "Y", `CV (%)` = "CV", `Wricke ecovalence` = "Ecoval", `Shukla variance` = "Shukla",
            `Slope b1` = "bij", `Deviation s2di` = "Sij", `R2 of regression` = "R2", `Annicchiarico Wi (%)` = "Wi_g",
            `Lin & Binns superiority Pi` = "Pi_a", `AMMI stability value (ASV)` = "ASV", WAASB = "WAASB")
  keep <- keep[keep %in% names(s)]
  tab <- stats::setNames(s[, keep], names(keep)); tab$Genotype <- as.character(tab$Genotype)
  ranks <- c(`Rank: mean` = "Y_R", `Rank: ecovalence` = "Ecoval_R", `Rank: Shukla` = "Shukla_R", `Rank: Annicchiarico` = "Wi_g_R", `Rank: superiority` = "Pi_a_R", `Rank: ASV` = "ASV_R")
  ranks <- ranks[ranks %in% names(s)]
  list(table = tab, ranks = cbind(Genotype = tab$Genotype, stats::setNames(s[, ranks], names(ranks))), trait = trait)
}

# AMMI (metan::performs_ammi): ANOVA with the PC terms, genotype and environment scores
pb_ammi <- function(data, trait, geno, env, rep) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  a <- pb_metan(metan::performs_ammi(d, ENV, GEN, REP, Y, verbose = FALSE))
  r <- a$Y
  an <- as.data.frame(r$ANOVA); names(an)[names(an) == "Proportion"] <- "% of GxE"; names(an)[names(an) == "Accumulated"] <- "Cumulative %"
  sc <- as.data.frame(r$model); names(sc)[names(sc) == "Code"] <- "Name"; names(sc)[names(sc) == "Y"] <- "Mean"
  list(anova = an, scores = sc, model = a, trait = trait)
}

# GGE biplot (metan::gge). centering "environment" = GGE; scaling "none" or "sd"; svp: singular value partitioning.
pb_gge <- function(data, trait, geno, env, rep, scaling = "none", svp = "environment") {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  g <- pb_metan(metan::gge(d, ENV, GEN, Y, centering = "environment", scaling = scaling, svp = svp))
  r <- g$Y
  sc <- rbind(data.frame(type = "GEN", Name = r$labelgen, PC1 = r$coordgen[, 1], PC2 = r$coordgen[, 2]),
              data.frame(type = "ENV", Name = r$labelenv, PC1 = r$coordenv[, 1], PC2 = r$coordenv[, 2]))
  list(explained = data.frame(PC = paste0("PC", seq_along(r$varexpl)), `% explained` = r$varexpl, check.names = FALSE),
       scores = sc, model = g, trait = trait)
}
