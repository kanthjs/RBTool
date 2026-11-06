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
| Shukla | metan `Shukla`, agricolae `stability.par` | **different definition**: PBTools fits `nlme::gls` with one residual variance per genotype (REML); the packages use Shukla's (1972) moment formula. Rank correlation 0.77 on the Two_stage data. metan and agricolae agree with each other on the same data (RCB_ME): identical ranks; agricolae = metan x reps (replicate-total scale) |
| AMMI | agricolae `AMMI`, metan `performs_ammi` | % of GxE SS per PC and genotype scores identical; F of PCs differs slightly (PBTools uses the stage-1 pooled error, the packages the raw-data residual) |
| GGE biplot | metan `gge` | % explained identical (40.1, 21.7, 17.4); statgenGxE `gxeGGE` identical too |
| Extra stability indices (not in PBTools) | metan: Wricke ecovalence, Annicchiarico, superiority, WAASB, ...; statgenGxE `gxeStability` | - |
| Pairwise comparisons | multcomp / emmeans | PBTools uses multcomp `glht` (already the case in the port) |

## Second look: metan, statgenSTA / statgenGxE, agricolae as all-in-one toolkits (2026-11-03)

Question: could one of these packages carry the whole workflow (single environment, multi-environment, stability)?
Script: `scripts/pkg_compare_metan_sea.R`.

| Package | Single environment | Multi-environment | Stability, AMMI, GGE | Fit for this app |
|---|---|---|---|---|
| metan | `gamem` (`Y ~ REP + (1 \| GEN)`: replicate **fixed**), `anova_ind` (fixed ANOVA per site); RCBD and alpha-lattice only | `gamem_met` (`Y ~ ENV/REP + (1 \| GEN) + (1 \| GEN:ENV)`: environment and replicate fixed) | all of them, identical to PBTools where PBTools has them; also WAASB, MTSI, MGIDI (multi-trait selection) | already used for GxE and stability; its mixed models agree with PBTools when the block variance is not zero (site E2: identical variances, H2 0.947, BLUPs within 0.0005) but differ when it is zero (site E1: H2 0.161 vs 0.178, because the replicate is fixed); `gamem_met` gives the same genotype and GxE variances as the PBTools one-stage model (2877, 664748), with a different heritability formula (h2mg 0.043 vs 0.045) |
| statgenSTA + statgenGxE | `fitTD`: rcbd, alpha (res.ibd), row-column, spatial (SpATS); replicate fixed; Cullis heritability | `gxeVarComp` on stage-1 means: identical to the PBTools two-stage model | Finlay-Wilkinson (environment effects fitted jointly: slopes differ from PBTools by up to 0.16), AMMI, GGE, superiority, static, Wricke | a clean two-stage pipeline and spatial models; BLUEs identical, SE differ (replicate fixed), no augmented designs; good candidate for a later "spatial analysis" option, not as the PBTools yardstick |
| agricolae | `aov`-based fixed-effect analyses, `PBIB.test` (alpha-lattice), `DAU.test` (augmented), LSD / HSD / Duncan / SNK | none as mixed model | `AMMI` (PBTools' own AMMI code comes from it: identical), `stability.par` (Shukla, Kang) | already used by RDstat; fits the "classic ANOVA" view (fixed blocks, letters by LSD/HSD/Duncan) rather than the PBTools mixed models |

Conclusion: no single package reproduces PBTools across the whole menu. The engine therefore keeps lme4 + emmeans
(+ multcomp / multcompView) for the PBTools mixed models and uses metan for GxE and stability. Worth adding later:
metan's WAASB and MTSI / MGIDI (multi-trait selection indices), statgenSTA's spatial models (SpATS), and agricolae's
LSD / Duncan letters for users who want the classic fixed-block ANOVA view.
