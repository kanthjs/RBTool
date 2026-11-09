# Changelog

All notable changes to RBTool. Dates are when the change was made; versions follow the app version written by
`scripts/build_app.R` (`0.9.<CI run number>` on GitHub Pages from 0.9.0; `0.1.<CI run number>` before).

## 0.9.0 - 2026-11-09

First version for general use.

### Added (2026-11-08 to 2026-11-09)
- A "what you get" box under the choices of step 1 on every screen: when to use the analysis, what it gives, what it does not, and the question it answers. Single and Multi-environment: one box each for ANOVA, genotype fixed and genotype random; GxE & stability: one box for each of the eight analyses.
- Feedback: "ส่งข้อเสนอแนะ" in the navbar and buttons on the About page open the Google Form, with a diagnostic report to copy (app version, R, settings of the active screen, last error; no data values). `scripts/create_feedback_form.gs` builds the form.
- Mean comparison shows one trait | environment at a time, with an "All" option, so that letters of different environments are not read together.
- Two-stage with weights 1/SE² and genotype random: a warning that its heritability depends on the units of the data (the GxE variance is fixed at 1, as PBTools); the Thai summary leaves it out.

### Changed (2026-11-08 to 2026-11-09)
- Step 1 is "Analysis" on every screen; on Single and Multi-environment the genotype effect (fixed or random) moved into it, next to the method.
- Multi-environment: the method is Combined ANOVA or Mixed model. One-stage (default) or two-stage, and the weights of the site means (default now 1/SE²), are under "How the sites are combined".
- Data tab lists every row (scroll box, header kept in view) with the number of empty cells, instead of the first 10 rows.
- Methods: descriptive statistics cite R (mean(), sd()); the list of differences from PBTools is removed.

### Removed (2026-11-08 to 2026-11-09)
- Two-stage from a file of site means (and the stage-1 means example and template): two-stage now always computes stage 1 from the plot data.
- The key-number cards above the results.

### Fixed (2026-11-08 to 2026-11-09)
- The app did not start: an invalid escape (`\(`) in a Methods text.
- Two-stage with genotype fixed: the results page failed ("argument is of length zero").

### Added (2026-11-07)
- Loading screen as in RDstat (`prototype/loading_screen.html`, put into the site by the new `scripts/export_site.R`): RBTool name and colours, progress bar estimated from the last load in the browser, seconds elapsed and left, the three loading steps, a note that the data stay in the browser, and a hint when it takes over 3 minutes. CI exports with this script and installs `png`, which the server tests need (deploy run #6 stopped there).
- GGE biplot: all ten metan views (basic, mean vs. stability, which-won-where, discriminativeness vs. representativeness, examine an environment, ranking environments, examine a genotype, ranking genotypes, compare two genotypes, relationship among environments), chosen on the Plot tab with pickers for the environment / genotypes, label and point size, and a help box on reading each view. AMMI: nominal yield vs. environment PC1 added.
- Live View plots (as RDstat) on every Plot tab: the plot is drawn at the proportions of the file that will be downloaded; size presets (standard, square, journal 1 and 2 columns, slide, custom cm with a text-size slider) and PNG / SVG / PDF download.
- Thai reading of the AMMI (ANOVA shares, AMMI1, AMMI2, ASV and ssiASV from metan::ammi_indexes) and GGE results (which-won-where mega-environments, mean vs. stability, ideal genotype and environment, environment correlations); also in the Word report. AMMI stability indexes table.
- More metan plots in GxE: AMMI scree plot (from metan's AMMI table) and residuals (metan::residual_plots); WAASB genotype BLUPs (metan::plot_blup), eigenvalues (metan::plot_eigen) and mixed-model residuals; correlations among the traits (metan::corr_plot) and a Venn of the MTSI and MGIDI selections (metan::venn_plot). The metan plots are drawn with one RBTool theme (theme_rbtool, through metan's plot_theme argument), Okabe-Ito colours (genotypes blue, environments vermillion) and readable legends; the theme, font and black-and-white options also reach patchwork and GGally plots; GGally matrices and recorded base plots download like ggplots.
- Plot options under every plot: theme (as designed, classic, framed with light grid, minimal), font size and black and white (greyscale) for print, applied to the Live View and the downloaded file; plot options and download size no longer mark the results as changed. New plots: WAASBY ranks over weighting scenarios (metan::wsmp) and variance components as shares of the total (mixed models, Single and Multi-environment).
- Publication plots: genotype means as points with error bars (SE or 95% CI; t for ANOVA, z for mixed models) and the letter groups of the last Tukey / LSD / DMRT run (Single and Multi-environment; the bar chart stays as an option); genotype x environment heatmap ordered by genotype and environment means, colour-blind-safe viridis scale (Multi-environment and the stability methods of GxE); mean vs. slope b four-part plot for Finlay-Wilkinson and Eberhart-Russell (size = s2di, colour = stable), labels kept apart with ggrepel.
- Mean comparisons: LSD (Fisher) and DMRT (Duncan) from agricolae (LSD.test, duncan.test) with the Standard ANOVA and Combined ANOVA methods (Combined ANOVA with environments random: GxE mean square as error); agricolae added to the bundle (one extra dependency, AlgDesign).
- GxE: WAASB / WAASBY (metan::waasb) with a live weight slider for mean vs. stability and selection intensity, WAASBY ranking, four-quadrant plot and BLUP biplot; multi-trait selection with MTSI (metan::mtsi) and MGIDI (metan::mgidi, on BLUPs or means) with per-trait direction (lower is better), selection intensity, selection differentials and strengths / weaknesses; Thai readings. Traits without genetic variance are left out of MGIDI on BLUPs with a note. New example: maize, 13 hybrids x 4 environments, 15 traits (metan data_ge2).
- Multi-environment: "Combined ANOVA" method (Gomez & Gomez 1984): Env + Rep(Env) + Genotype + GxE with stats::lm; environments random (Env tested against Rep(Env), genotype against GxE, as aov with Error strata) or fixed (pooled error); CV, LSD, Bartlett's test and the error mean square of each site; Tukey / Dunnett with the error term of the genotype F test.
- Single environment: "Standard ANOVA" method (RCBD, blocks and genotypes fixed, stats::lm / anova) with the ANOVA table, CV and LSD; Tukey / Dunnett comparisons work on it.
- CI (`.github/workflows/pages.yml`) installs agricolae so that Shinylive bundles it. The Pages deploy step waits up to 30 minutes (deploy run #4 timed out at the default 10). Bundle measured: see docs/PACKAGES.md.
- Data layout guide with an example table and a template download next to every upload box; .xls named in the upload labels (it was already read); "." and "NA" cells in Excel files are missing values, as in CSV files.

### Changed (2026-11-07)
- Methods page rewritten in the RDstat style: R package table (version, use, functions) with software references; topics in accordions grouped by analysis, each with its Thai description, software, functions, its own references and a copy button; full list of method references.
- Single environment lists Standard ANOVA first and Multi-environment lists Combined ANOVA first (the defaults).
- GxE & stability: the methods are listed under three headings, "Stability", "Multivariate biplots" and "Modern BLUP & multi-trait selection" (a separate Selection menu was tried and dropped as more confusing).
- Every screen starts with the Method step (Single environment: Method, Experimental design, Data, Variables, Genotype effect, as on Multi-environment); the GxE step "Analysis" is now "Method".
- Multi-environment screen: RCBD only; the row-column example is no longer offered.
- Pressing Run before the variable lists have loaded gives a clear message.

### Fixed (2026-11-07)
- Tukey letters failed when genotype codes are numbers (emmeans writes "Gen2 - Gen1"); the factor name is now taken off.

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
