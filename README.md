# RBTool

Web app for the analysis of plant breeding trials in the spirit of IRRI **PBTools** (Phenotypic Analysis menu): single-environment, multi-environment (one-stage and two-stage) and genotype x environment / stability analyses. It runs R in the browser with Shinylive, like its sister project [RDstat](https://github.com/kanthjs/RDstat).

The statistics are computed with published R packages instead of PBTools' own functions, and every result is checked against PBTools itself:

| Analysis | Package |
|---|---|
| Single environment (RCBD in the app; the engine has all 7 PBTools designs), BLUEs / BLUPs, heritability | lme4, emmeans |
| Mean comparison (Tukey, Dunnett), letter groups | emmeans (default), multcomp (exact, as PBTools), multcompView |
| Multi-environment, one-stage and two-stage | lme4, emmeans |
| Finlay-Wilkinson, Shukla, stability indices, AMMI, GGE | metan (and nlme for the PBTools form of Shukla) |

## Layout

| Path | What |
|---|---|
| `engine/R/` | the analyses (`pb_sea`, `pb_stage1`, `pb_one_stage`, `pb_two_stage`, `pb_fw`, `pb_shukla`, `pb_stability_stats`, `pb_ammi`, `pb_gge`, `pb_pairwise`) |
| `prototype/app/` | the Shiny app; `prototype/app/engine` is copied from `engine/R` by `scripts/build_app.R` |
| `prototype/tests/test_server.R` | headless test of the app |
| `reference/` | the original PBTools: recovered R functions, decompiled Java bridge, outputs of the original on R 2.15.1, and `pbtools-port` (PBTools ported to R 4.5, the yardstick) |
| `scripts/` | `compare_engine.R` (engine vs PBTools), `check_port.sh` (port vs original), case scripts, `build_app.R`, `make_examples.R` |
| `docs/PBTOOLS_GUIDE.md` | start here (Thai): how PBTools works, the scope, the models, the reference material, and how to continue on another machine |
| `docs/PACKAGES.md` | why these packages, and how their results compare with PBTools |
| `PORTING.md` | reverse-engineering notes and every known difference |

## Run and test

```
Rscript -e 'install.packages(c("shiny", "bslib", "lme4", "emmeans", "multcomp", "multcompView", "nlme", "metan", "agricolae", "readxl", "writexl", "ggplot2"))'
Rscript scripts/build_app.R
Rscript prototype/tests/test_server.R
Rscript -e 'shiny::runApp("prototype/app")'
```

Check the engine against PBTools (needs the port installed):

```
Rscript -e 'install.packages("reference/pbtools-port", repos = NULL, type = "source")'
Rscript scripts/compare_engine.R
```

Build the browser version (with the RBTool loading screen): `Rscript scripts/export_site.R _site` and serve `_site` over http.

## License

GPL (>= 2), as PBTools.
