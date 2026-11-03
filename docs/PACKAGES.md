# Established R packages for the PBTools analyses (survey, 2026-11-03)

Goal: compute with published, maintained CRAN packages instead of the recovered PBTools functions, and keep the PBTools port
(`engine/`, checked against the original on R 2.15.1) as the yardstick. All packages below are also in the webR repository
for R 4.5 (`repo.r-wasm.org`, checked 2026-11-03), so the app can run in the browser with Shinylive like RDstat.

Scripts: `scripts/pkg_compare_sea.R`, `scripts/pkg_compare_gxe.R` (data: RCB_ME, 15 genotypes x 11 sites, RCB, 3 reps).

| Analysis (PBTools menu) | Package / function | Result against the PBTools port |
|---|---|---|
| Single env: BLUEs and SE | lme4 + emmeans (same model formulas as PBTools, all 7 designs) | identical (the port is lme4) |
| Single env: BLUEs | statgenSTA `fitTD(design = "rcbd")` | BLUEs identical; SE differ (statgenSTA fits replicate as **fixed**, PBTools as random) |
| Single env: heritability | statgenSTA `extractSTA(what = "heritability")` | identical in 10 of 11 sites; differs where the block variance is 0 (0.18 vs 0.16) |
| Single env: designs | statgenSTA: rcbd, ibd, res.ibd (alpha), rowcol, res.rowcol | no augmented or latinized designs |
| One-stage MET: LSMeans and SE | lme4 + emmeans | identical (max diff 6e-10) |
| Two-stage MET: variance components | lme4, statgenGxE `gxeVarComp` | identical |
| Finlay-Wilkinson | metan `ge_reg` (Eberhart and Russell) | slopes identical (2e-15); PBTools MSDev = metan s2di + pooled error / reps |
| Finlay-Wilkinson | statgenGxE `gxeFw` | slopes differ by up to 0.16: environment effects are estimated jointly, not taken as site means |
| Shukla | metan `Shukla`, agricolae `stability.par` | **different definition**: PBTools fits `nlme::gls` with one residual variance per genotype (REML); the packages use Shukla's (1972) moment formula. Rank correlation 0.77 on the Two_stage data |
| AMMI | agricolae `AMMI`, metan `performs_ammi` | % of GxE SS per PC and genotype scores identical; F of PCs differs slightly (PBTools uses the stage-1 pooled error, the packages the raw-data residual) |
| GGE biplot | metan `gge` | % explained identical (40.1, 21.7, 17.4) |
| Extra stability indices (not in PBTools) | metan: Wricke ecovalence, Annicchiarico, superiority, WAASB, ...; statgenGxE `gxeStability` | - |
| Pairwise comparisons | multcomp / emmeans | PBTools uses multcomp `glht` (already the case in the port) |
