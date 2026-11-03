# Single-environment cases for the designs not covered by ssa_cases.R (augmented, row-column, latinized) plus
# ssa.pairwise and ssa.resid. Data are built without random numbers so both R versions see the same values.
# Usage: Rscript designs_cases.R <repo-root> <outdir> [port|original]
args <- commandArgs(TRUE)
root <- args[1]; out <- args[2]; mode <- if (length(args) > 2) args[3] else "original"
if (mode == "port") suppressMessages(library(PBToolsRD)) else suppressMessages(library(PBTools))
dir.create(out, showWarnings = FALSE, recursive = TRUE)
options(digits = 7, width = 100)

# ---- data ----
# augmented RCB: 3 blocks, 3 checks in every block, 12 unreplicated test entries (4 per block)
aug <- rbind(
  data.frame(Block = rep(1:3, each = 3), Entry = rep(c("CK1", "CK2", "CK3"), 3)),
  data.frame(Block = rep(1:3, each = 4), Entry = sprintf("T%02d", 1:12)))
aug$Block <- factor(aug$Block); aug$Entry <- factor(aug$Entry)
aug$Y <- round(40 + 2 * sin(as.numeric(aug$Entry) * 1.3) + 1.5 * cos(as.numeric(aug$Block) * 2.1) + 0.5 * sin(as.numeric(aug$Entry) * as.numeric(aug$Block)), 3)
aug$Z <- round(10 + cos(as.numeric(aug$Entry) * 0.7) + 0.4 * as.numeric(aug$Block) + 0.7 * sin(as.numeric(aug$Entry) * as.numeric(aug$Block) * 2.9), 3)
aug$Y[5] <- NA
# augmented latin square: 5 x 5 square of 5 checks plus 15 test entries on existing rows and columns
ck <- expand.grid(Col = 1:5, Row = 1:5); ck$Entry <- paste0("CK", (ck$Row + ck$Col) %% 5 + 1)
tt <- data.frame(i = 1:15); tt$Row <- (tt$i * 2) %% 5 + 1; tt$Col <- (tt$i * 3 + tt$i %/% 5) %% 5 + 1; tt$Entry <- sprintf("T%02d", tt$i)
augls <- rbind(ck[, c("Row", "Col", "Entry")], tt[, c("Row", "Col", "Entry")])
augls$Row <- factor(augls$Row); augls$Col <- factor(augls$Col); augls$Entry <- factor(augls$Entry)
augls$Y <- round(30 + 2 * sin(as.numeric(augls$Entry) * 1.1) + cos(as.numeric(augls$Row) * 1.7) + 0.8 * sin(as.numeric(augls$Col) * 2.3) + 0.9 * sin(as.numeric(augls$Row) * as.numeric(augls$Col) * 1.3 + as.numeric(augls$Entry)), 3)
# row-column: 2 reps of a 4 x 4 layout with 16 entries, plus a second environment
rc1 <- expand.grid(Col = 1:4, Row = 1:4, Rep = 1:2)
rc1$g <- ((rc1$Row - 1) * 4 + (rc1$Col - 1) + (rc1$Rep - 1) * (rc1$Row + 2 * rc1$Col)) %% 16 + 1
rc1$Geno <- sprintf("G%02d", rc1$g); rc1$Rep <- factor(rc1$Rep); rc1$Row <- factor(rc1$Row); rc1$Col <- factor(rc1$Col)
rc1$Y <- round(60 + 4 * sin(rc1$g * 1.9) + 2 * cos(as.numeric(rc1$Rep) * 2.2 + as.numeric(rc1$Row)) + 1.2 * sin(as.numeric(rc1$Col) * 1.4 + rc1$g), 3)
rc2 <- rc1; rc2$Env <- "E2"; rc2$Y <- round(rc2$Y * 1.1 + 3 * cos(rc2$g), 3); rc1$Env <- "E1"
rc <- rbind(rc1, rc2)
# latinized row-column needs row levels that continue across replicates (rows 1-8 over 2 reps)
rcl <- rc1; rcl$Row <- factor(as.numeric(as.character(rc1$Row)) + 4 * (as.numeric(as.character(rc1$Rep)) - 1))
# alpha-type layout for the latinized alpha-lattice: 3 reps x 5 blocks x 3 plots, 15 entries
al <- expand.grid(plot = 1:3, Block = 1:5, Rep = 1:3)
al$g <- ((al$Block - 1) * 3 + al$plot - 1 + (al$Rep - 1) * al$Block) %% 15 + 1
al$Geno <- sprintf("G%02d", al$g); al$Row <- factor(al$Block); al$Rep <- factor(al$Rep)
al$Y <- round(50 + 3 * sin(al$g * 1.7) + 2 * cos(as.numeric(al$Rep) * 2.3 + al$Block) + 0.8 * sin(al$g * al$Block + al$plot), 3)

# ---- dump ----
walk <- function(x, name = "") {
  if (is.null(x)) { cat("##", name, "NULL\n"); return(invisible()) }
  if (is.list(x) && !is.data.frame(x)) {
    for (n in seq_along(x)) walk(x[[n]], paste0(name, "/", if (is.null(names(x)) || !nzchar(names(x)[n])) n else names(x)[n]))
  } else if (isS4(x) || inherits(x, c("merMod", "mer", "lm", "glht"))) { cat("##", name, "(model skipped)\n")
  } else if (grepl("/data$", name)) { cat("##", name, "(data skipped)\n")
  } else if (is.numeric(x) && is.null(dim(x))) { cat("##", name, "\n"); cat(signif(x, 6), fill = 100)
  } else { cat("##", name, "\n"); print(x) }
}
run <- function(name, expr) {
  r <- try(expr, silent = TRUE)
  sink(file.path(out, paste0(name, ".txt")))
  if (inherits(r, "try-error")) cat("ERROR\n", as.character(r), "\n") else walk(r, "result")
  sink()
  invisible(r)
}
run("augrcb_fixed",        ssa.test("AugRCB", aug, "Y", "Entry", "Block", is.random = FALSE))
# fixed genotype with excludeCheck = TRUE fails in the original too ("object newExcludeCheck not found"); not covered
run("augrcb_random_2traits", ssa.test("AugRCB", aug, c("Y", "Z"), "Entry", "Block", is.random = TRUE, excludeCheck = TRUE))
run("augrcb_random",       ssa.test("AugRCB", aug, "Y", "Entry", "Block", is.random = TRUE))
run("augrcb_random_excl",  ssa.test("AugRCB", aug, "Y", "Entry", "Block", is.random = TRUE, excludeCheck = TRUE))
run("augrcb_checklist",    ssa.test("AugRCB", aug, "Y", "Entry", "Block", is.random = TRUE, excludeCheck = TRUE, checkList = c("CK1", "CK2", "CK3")))
run("augls_fixed",         ssa.test("AugLS", augls, "Y", "Entry", "Row", column = "Col", is.random = FALSE))
run("augls_random_excl",   ssa.test("AugLS", augls, "Y", "Entry", "Row", column = "Col", is.random = TRUE, excludeCheck = TRUE))
run("rowcol_fixed",        ssa.test("RowCol", rc1, "Y", "Geno", "Row", column = "Col", rep = "Rep", is.random = FALSE))
run("rowcol_random_multi", ssa.test("RowCol", rc, "Y", "Geno", "Row", column = "Col", rep = "Rep", env = "Env", is.random = TRUE))
run("latinalpha_fixed",    ssa.test("LatinAlpha", al, "Y", "Geno", "Row", rep = "Rep", is.random = FALSE))
run("latinalpha_random",   ssa.test("LatinAlpha", al, "Y", "Geno", "Row", rep = "Rep", is.random = TRUE))
run("latinrowcol_fixed",   ssa.test("LatinRowCol", rcl, "Y", "Geno", "Row", column = "Col", rep = "Rep", is.random = FALSE))
run("latinrowcol_random",  ssa.test("LatinRowCol", rcl, "Y", "Geno", "Row", column = "Col", rep = "Rep", is.random = TRUE))

# pairwise comparisons and residuals on a fixed-genotype fit
fit <- ssa.test("RowCol", rc1, "Y", "Geno", "Row", column = "Col", rep = "Rep", is.random = FALSE)
m <- fit$output[[1]]$site[[1]]$model
grDevices::pdf(NULL)
run("pairwise_tukey",  ssa.pairwise(m, type = "Tukey", alpha = 0.05))
run("pairwise_dunnett", ssa.pairwise(m, type = "Dunnett", alpha = 0.05, control.level = "G01"))
grDevices::dev.off()
run("resid", ssa.resid(rc1, fit, "Y", NULL, is.genoRandom = FALSE))
