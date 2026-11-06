# Changelog

All notable changes to RBTool. Dates are when the change was made; versions follow the app version written by
`scripts/build_app.R` (`0.1.<CI run number>` on GitHub Pages).

## Unreleased

### Changed
- Finlay-Wilkinson now shows the slope, its t test and R2 only; the deviation from regression and the joint-regression ANOVA moved to the new Eberhart-Russell choice.
- `scripts/pkg_compare_gxe.R`: GGE line works with statgenGxE 1.0.11; new check of metan::Shukla against agricolae::stability.par on the same data (identical ranks, agricolae = metan x reps). Rerun 2026-11-06 with lme4 2.0.6: `compare_engine.R` 97/97.
- The project is named RBTool everywhere (app title, README, guide); the source link on the Info page pointed to github.com/kanthjs/PBTool-Rev and now points to github.com/kanthjs/RBTool.
- Single-environment screen: RCBD only (the engine keeps all seven PBTools designs, and they are still checked against PBTools).
- Mean comparisons are about 100 times faster: Tukey and Dunnett intervals now come from emmeans (Tukey-Kramer, Dunnett) and the letter groups from multcompView. Tukey for 11 sites took 5 minutes, now under 2 seconds. The exact multcomp intervals that PBTools uses are still available with the option "Exact intervals as in PBTools"; the default intervals agree with them within about 1% (identical for balanced data).
- Row-column example (multi-environment) replaced by a valid layout: 25 genotypes, 5 x 5, two replicates built from two orthogonal latin squares, two sites.

### Removed
- Alpha-lattice, augmented RCB, augmented latin square and latinized row-column examples: the simulated layouts were not valid designs (some genotypes appeared twice in a replicate). `scripts/check_examples.R` now checks every example layout.

### Added
- Eberhart-Russell regression (`pb_er`, metan::ge_reg) as its own choice on the GxE screen: slope b1 with its t test, deviation from regression s2di with its F test, R2, the joint-regression ANOVA, and a class for each genotype (response to environments, predictable, stable when b1 = 1 and s2di = 0 at alpha 0.05); plot of b1 against s2di; Thai summary, Excel and Word. Checked against PBTools: same slope, and s2di + pooled error / reps equals the PBTools MSDev (`compare_engine.R`, now 99 points).
- `CITATION.cff` (RBTool and the R packages it uses) and a "วิธีอ้างอิง" (How to cite) section on the Info page: the citation of RBTool with its version, and the package to cite for each analysis, with DOIs.
- Descriptive statistics (`pb_descriptive`), as PBTools `DescriptiveStatistics` with the statistics its dialogs use (number of non-missing observations, mean, SD): by environment on the single-environment screen, over all plots for one-stage, over the stage-1 means for two-stage. Shown as a result card and in the Excel and Word downloads; identical to PBTools (`scripts/compare_engine.R`).
- `scripts/check_examples.R` (example layouts), `scripts/timing.R` (time of each analysis).
- multcompView declared for Shinylive and the deploy workflow.

## 0.1.0 - 2026-11-03

### Added
- Engine on published packages (`engine/R`): single environment (`pb_sea`, lme4 + emmeans, seven PBTools designs), stage-1 table (`pb_stage1`), one-stage and two-stage multi-environment analyses (`pb_one_stage`, `pb_two_stage`), Tukey and Dunnett comparisons (`pb_pairwise`, multcomp), Finlay-Wilkinson (`pb_fw`, metan), Shukla (`pb_shukla`, metan; PBTools form with nlme), stability statistics (`pb_stability_stats`, metan), AMMI (`pb_ammi`, metan), GGE biplot (`pb_gge`, metan), PBTools residual plots (`pb_diag_plot`).
- Shiny app (`prototype/app`) with the RDstat layout: Home, Single environment, Multi-environment, GxE & stability, Methods, About; Thai summaries, Excel and Word downloads, residual checks, stale-result marking; headless tests (`prototype/tests/test_server.R`).
- Reference material: recovered PBTools 1.1 R functions, decompiled Java bridge, outputs of the original on R 2.15.1, and the port to R 4.5 (`reference/pbtools-port`) used as the yardstick; `scripts/check_port.sh` (port vs original, 33 cases) and `scripts/compare_engine.R` (engine vs port).
- GitHub Pages workflow for the Shinylive export.
- Red theme (#A61C1C), so the app is not mistaken for RDstat.
