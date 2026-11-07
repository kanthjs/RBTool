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

# Regression of each genotype on the environmental index (site mean minus grand mean), both with metan::ge_reg:
# Finlay-Wilkinson (1963): the slope b1 (the PBTools slope); Eberhart-Russell (1966): the slope b1 and the deviation
# from regression s2di, with the joint-regression ANOVA. A genotype is stable in the Eberhart-Russell sense when b1 does
# not differ from 1 and s2di does not differ from 0.
pb_reg_fit <- function(data, trait, geno, env, rep) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  r <- pb_metan(metan::ge_reg(d, ENV, GEN, REP, Y, verbose = FALSE))$Y
  cell <- stats::aggregate(Y ~ GEN + ENV, d, mean)
  idx <- stats::aggregate(Y ~ ENV, d, mean); idx$Index <- idx$Y - mean(d$Y)
  cell$Index <- idx$Index[match(cell$ENV, idx$ENV)]
  list(g = as.data.frame(r$regression), anova = as.data.frame(r$anova), cell = setNames(cell, c("Genotype", "Environment", "Mean", "Index")),
       env_index = data.frame(Environment = idx$ENV, Mean = idx$Y, Index = idx$Index))
}
pb_fw <- function(data, trait, geno, env, rep) {
  f <- pb_reg_fit(data, trait, geno, env, rep); g <- f$g
  tab <- data.frame(Genotype = g$GEN, Mean = g$b0, Slope = g$b1, `t (b1 = 1)` = as.numeric(g[["t(b1=1)"]]), `p (b1 = 1)` = as.numeric(g$pval_t),
                    R2 = g$R2, check.names = FALSE)
  list(table = tab, cell = f$cell, env_index = f$env_index, trait = trait)
}
pb_er <- function(data, trait, geno, env, rep, alpha = 0.05) {
  f <- pb_reg_fit(data, trait, geno, env, rep); g <- f$g
  pt <- as.numeric(g$pval_t); pf <- as.numeric(g$pval_f)
  resp <- ifelse(pt >= alpha, "average (b1 = 1)", ifelse(g$b1 > 1, "above average (b1 > 1)", "below average (b1 < 1)"))
  tab <- data.frame(Genotype = g$GEN, Mean = g$b0, Slope = g$b1, `t (b1 = 1)` = as.numeric(g[["t(b1=1)"]]), `p (b1 = 1)` = pt,
                    `Deviation variance (s2di)` = g$s2di, `F (s2di = 0)` = g[["F(s2di=0)"]], `p (s2di = 0)` = pf, R2 = g$R2, RMSE = g$RMSE,
                    Response = resp, Predictable = ifelse(pf >= alpha, "yes (s2di = 0)", "no (s2di > 0)"),
                    `Stable (Eberhart-Russell)` = ifelse(pt >= alpha & pf >= alpha, "yes", "no"), check.names = FALSE)
  list(table = tab, anova = f$anova, cell = f$cell, env_index = f$env_index, alpha = alpha, trait = trait)
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
  ix <- tryCatch(as.data.frame(pb_metan(metan::ammi_indexes(a))$Y), error = function(e) NULL)
  asv <- if (!is.null(ix)) data.frame(Genotype = as.character(ix$GEN), Mean = ix$Y, `Rank: mean` = ix$Y_R, ASV = ix$ASV, `Rank: ASV` = ix$ASV_R,
                                      `Yield stability index (ssiASV)` = ix$ASV_SSI, check.names = FALSE)
  list(anova = an, scores = sc, asv = asv, model = a, trait = trait)
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

# WAASB (Olivoto et al. 2019, metan::waasb): BLUP-based stability, the weighted average of the absolute scores of all
# the principal components of the BLUP matrix of the GxE effects (genotype and GxE random). WAASBY combines the mean
# and WAASB on 0-100 scales (metan's PctResp and PctWAASB); the app reweights them live with the same definition,
# WAASBY = (PctResp x wY + PctWAASB x (100 - wY)) / 100.
pb_waasb <- function(data, trait, geno, env, rep, higher = TRUE) {
  d <- pb_gxe_data(data, trait, geno, env, rep)
  w <- pb_metan(metan::waasb(d, ENV, GEN, REP, Y, mresp = if (higher) "h" else "l", wresp = 50, verbose = FALSE))
  r <- w$Y; m <- as.data.frame(r$model)
  g <- m[m$type == "GEN", ]; e <- m[m$type == "ENV", ]
  tab <- data.frame(Genotype = as.character(g$Code), Mean = g$Y, WAASB = g$WAASB, `Mean (0-100)` = g$PctResp, `Stability (0-100)` = g$PctWAASB,
                    `Rank: mean` = g$OrResp, `Rank: WAASB` = g$OrWAASB, check.names = FALSE)
  pcs <- grep("^PC[0-9]+$", names(m), value = TRUE)
  list(table = tab, env = data.frame(Environment = as.character(e$Code), Mean = e$Y, WAASB = e$WAASB),
       lrt = as.data.frame(r$LRT), estimates = as.data.frame(r$ESTIMATES), pca = as.data.frame(r$PCA),
       model = w, higher = higher, trait = trait)
}

# Multi-trait selection (Olivoto et al. 2019, 2021; metan): MTSI from the WAASBY of each trait (metan::mtsi), and MGIDI
# from the genotype BLUPs or means (metan::mgidi), both selecting SI percent of the genotypes nearest the ideotype.
# `lower`: traits for which lower values are better. Traits whose genotype BLUPs do not vary (no genetic variance) are
# left out of MGIDI on BLUPs, with a note.
pb_multi_trait <- function(data, traits, geno, env, rep, lower = character(), si = 15, wy = 50, mgidi_on = c("blup", "means")) {
  mgidi_on <- match.arg(mgidi_on)
  if (length(traits) < 2) stop("Choose at least two traits for multi-trait selection.", call. = FALSE)
  safe <- paste0("T", seq_along(traits)); lab <- stats::setNames(traits, safe)
  d0 <- pb_gxe_data(data, traits[1], geno, env, rep)        # checks the design on the first trait
  d <- data.frame(ENV = pb_factor(data[[env]]), GEN = pb_factor(data[[geno]]), REP = pb_factor(data[[rep]]))
  for (i in seq_along(traits)) d[[safe[i]]] <- pb_num(data[[traits[i]]])
  d <- droplevels(d[stats::complete.cases(d), ])
  if (any(table(d$GEN, d$ENV) == 0)) stop("Every genotype must have all the traits in every environment (rows with a missing trait are left out).", call. = FALSE)
  ide <- ifelse(traits %in% lower, "l", "h")
  w <- pb_metan(metan::waasb(d, ENV, GEN, REP, dplyr::all_of(safe), mresp = ide, wresp = rep(wy, length(safe)), verbose = FALSE))
  relabel <- function(x) { x <- as.data.frame(x); for (k in names(x)) if (is.character(x[[k]]) || is.factor(x[[k]])) { v <- as.character(x[[k]]); i <- v %in% safe; v[i] <- lab[v[i]]; x[[k]] <- v }
                           names(x) <- ifelse(names(x) %in% safe, lab[names(x)], names(x)); x }
  mt <- pb_metan(metan::mtsi(w, SI = si, verbose = FALSE))
  gm <- if (mgidi_on == "blup") as.data.frame(pb_metan(metan::gmd(w, "blupg", verbose = FALSE)))
        else { a <- stats::aggregate(d[safe], list(GEN = d$GEN), mean); a$GEN <- as.character(a$GEN); a }
  keep <- safe[vapply(safe, function(s) { x <- gm[[s]]; stats::sd(x) > 1e-8 * max(1, abs(mean(x))) }, NA)]
  dropped <- unname(lab[setdiff(safe, keep)])
  mg <- if (length(keep) >= 2) pb_metan(metan::mgidi(gm[c("GEN", keep)], SI = si, ideotype = ide[match(keep, safe)], verbose = FALSE))
  gmeans <- stats::aggregate(d[safe], list(Genotype = as.character(d$GEN)), mean); names(gmeans)[-1] <- traits
  list(mtsi = mt, mgidi = mg, model = w, traits = traits, gen_means = gmeans, lower = lower, si = si, wy = wy, mgidi_on = mgidi_on, dropped = dropped,
       mtsi_table = data.frame(Genotype = mt$MTSI$Genotype, MTSI = mt$MTSI$MTSI, Selected = ifelse(mt$MTSI$Genotype %in% mt$sel_gen, "yes", "")),
       mtsi_dif = relabel(mt$sel_dif_trait),
       mgidi_table = if (!is.null(mg)) data.frame(Genotype = mg$MGIDI$Genotype, MGIDI = mg$MGIDI$MGIDI, Selected = ifelse(mg$MGIDI$Genotype %in% mg$sel_gen, "yes", "")),
       mgidi_dif = if (!is.null(mg)) relabel(mg$sel_dif),
       mgidi_contri = if (!is.null(mg)) relabel(mg$contri_fac),
       relabel = relabel)
}

# WAASBY over weighting scenarios (metan::wsmp): the ranks of the genotypes when the weight of the mean goes from 0 to
# 100 percent in steps of 5 (Olivoto et al. 2019, Fig. of the WAASY scenarios). higher: larger trait values are better.
pb_wsmp <- function(model, higher = TRUE) pb_metan(metan::wsmp(model, mresp = if (higher) 100 else 0, progbar = FALSE))
