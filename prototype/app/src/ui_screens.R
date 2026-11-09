# The three analysis screens. Each is a sidebar of numbered steps with the Run button at the bottom, and tabs of
# results, as in RDstat.

design_radio <- function(id) {
  radioButtons(
    id,
    NULL,
    c(
      "Randomized complete block (RCB)" = "RCB",
      "Alpha-lattice" = "Alpha",
      "Row-column" = "RowCol",
      "Augmented RCB" = "AugRCB",
      "Augmented latin square" = "AugLS",
      "Latinized alpha-lattice" = "LatinAlpha",
      "Latinized row-column" = "LatinRowCol"
    )
  )
}
design_help <- help_box(
  "Which design?",
  p(strong("RCB:"), " every genotype once in each block (replicate)."),
  p(
    strong("Alpha-lattice:"),
    " replicates divided into incomplete blocks; needs the replicate and block columns."
  ),
  p(
    strong("Row-column:"),
    " each replicate laid out in rows and columns; needs replicate, row and column."
  ),
  p(
    strong("Augmented RCB / latin square:"),
    " checks repeated in every block (or in a latin square of rows and columns), test entries planted once."
  ),
  p(
    strong("Latinized:"),
    " alpha-lattice or row-column designs whose blocks (rows) run across replicates; in the latinized row-column design the row numbers continue from one replicate to the next."
  )
)
effect_radio <- function(id) {
  radioButtons(
    id,
    NULL,
    c(
      "Fixed: BLUEs (means) and mean comparison" = "fixed",
      "Random: BLUPs and heritability" = "random"
    )
  )
}
effect_help <- help_box(
  "Fixed or random?",
  p(
    strong("Fixed:"),
    " the genotypes are the ones you want to compare (released varieties, selected lines). Gives adjusted means (BLUEs) with SE, and Tukey or Dunnett comparisons."
  ),
  p(
    strong("Random:"),
    " the genotypes stand for a population (a breeding population, a set of new lines). Gives predicted values (BLUPs), the genetic variance and the heritability."
  )
)
downloads <- function(p) {
  conditionalPanel(
    sprintf("input.go_%s > 0", p),
    downloadButton(paste0("dl_", p, "_xlsx"), "Download Excel (.xlsx)"),
    downloadButton(paste0("dl_", p, "_doc"), "Download report (Word)")
  )
}
pw_controls <- function(p) {
  fluidRow(
    column(
      4,
      radioButtons(
        paste0(p, "_pw_type"),
        "Comparison",
        c(
          "Tukey: all pairs" = "Tukey",
          "Dunnett: against a control" = "Dunnett",
          "LSD (Fisher), with the ANOVA methods" = "LSD",
          "DMRT (Duncan), with the ANOVA methods" = "DMRT"
        )
      )
    ),
    column(
      3,
      selectInput(
        paste0(p, "_pw_alpha"),
        "Significance level",
        c("0.05" = 0.05, "0.01" = 0.01, "0.10" = 0.10)
      )
    ),
    column(
      5,
      conditionalPanel(
        sprintf("input.%s_pw_type == 'Dunnett'", p),
        selectizeInput(
          paste0(p, "_pw_control"),
          "Control genotype(s)",
          NULL,
          multiple = TRUE
        )
      )
    ),
    column(
      12,
      conditionalPanel(
        sprintf(
          "input.%s_pw_type == 'Tukey' || input.%s_pw_type == 'Dunnett'",
          p,
          p
        ),
        checkboxInput(
          paste0(p, "_pw_exact"),
          "Exact intervals as in PBTools (multcomp single-step; slower, seconds per site)",
          FALSE
        )
      )
    ),
    column(
      12,
      actionButton(
        paste0("go_", p, "_pw"),
        tagList(icon("play"), " Compare means"),
        class = "btn-primary"
      )
    )
  )
}
pw_help <- help_box(
  "About the comparisons",
  p(
    strong("LSD and DMRT"),
    " (agricolae) are the classic comparisons of the Standard ANOVA and Combined ANOVA methods: letters from Fisher's least significant difference (no adjustment for many comparisons, so more pairs come out different) or Duncan's multiple range test. They are not given with the mixed models; Tukey (or Dunnett against a check) keeps the error rate of all comparisons at the chosen level and is preferred by many journals."
  ),
  p(
    "Simultaneous confidence intervals of the differences (multcomp), on the model with genotype fixed, as PBTools does: a difference is significant when its interval does not include 0."
  ),
  p(
    "By default the intervals come from emmeans (Tukey-Kramer, Dunnett): fast, and the same as the exact ones for balanced data; elsewhere within about 1%. Tick the exact option for the multcomp intervals PBTools uses."
  ),
  p(
    "Tukey also gives letter groups: genotypes sharing a letter do not differ significantly. PBTools lists only the significant pairs; tick the box to see every pair."
  )
)

sea_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Method",
      radioButtons(
        "sea_method",
        NULL,
        c(
          "Standard ANOVA (fixed blocks, F test, LSD, CV)" = "anova",
          "Mixed model (lme4)" = "mixed"
        )
      ),
      help_box(
        "Which method?",
        p(
          strong("Standard ANOVA:"),
          " the classic analysis of variance of an RCBD, y = block + genotype + error, with blocks and genotypes fixed (stats::lm and anova). Gives the ANOVA table with the F test of genotypes, the coefficient of variation (CV) and the LSD; means can be compared with Tukey, Dunnett, LSD or DMRT."
        ),
        p(
          strong("Mixed model:"),
          " blocks random, fitted by REML as PBTools does. Genotype can be fixed (BLUEs) or random (BLUPs and heritability)."
        ),
        p(
          "With complete data (every genotype in every block) both methods give the same genotype means and the same F test of genotypes; they differ when plots are missing."
        )
      )
    ),
    side_step(
      2,
      "Experimental design",
      radioButtons(
        "sea_design",
        NULL,
        c("Randomized complete block (RCBD)" = "RCB")
      ),
      help_box(
        "Design",
        p(
          "Randomized complete block design (RCBD): every genotype once in each block (replicate)."
        )
      )
    ),
    side_step(
      3,
      "Data",
      radioButtons(
        "sea_src",
        NULL,
        c("Built-in example" = "ex", "Upload file" = "up"),
        inline = TRUE
      ),
      conditionalPanel(
        "input.sea_src == 'ex'",
        selectInput("sea_example", NULL, examples_for("sea"))
      ),
      conditionalPanel(
        "input.sea_src == 'up'",
        file_input("sea"),
        layout_help("sea")
      )
    ),
    side_step(4, "Variables", uiOutput("sea_vars")),
    conditionalPanel(
      "input.sea_method != 'anova'",
      side_step(5, "Genotype effect", effect_radio("sea_random"), effect_help)
    ),
    run_bar("go_sea", "Run single-environment analysis")
  ),
  mainPanel(
    uiOutput("sea_msg"),
    div(
      id = "sea_out",
      tabsetPanel(
        id = "sea_tabs",
        tabPanel(
          "Results",
          br(),
          stale_dim(uiOutput("sea_results")),
          br(),
          stale_dim(downloads("sea"))
        ),
        tabPanel(
          "Mean comparison",
          br(),
          pw_help,
          pw_controls("sea"),
          checkboxInput(
            "sea_pw_all",
            "Show every pair, not only the significant ones"
          ),
          stale_dim(uiOutput("sea_pw_out"))
        ),
        tabPanel(
          "Plot",
          br(),
          stale_dim(uiOutput("sea_plot_pick")),
          fluidRow(
            column(
              6,
              radioButtons(
                "sea_plot_style",
                "Plot",
                c(
                  "Means: points with error bars and letters (publication)" = "dot",
                  "Means: bar chart" = "bar",
                  "Variance components (mixed model)" = "vc"
                )
              )
            ),
            column(
              6,
              radioButtons(
                "sea_err",
                "Error bars",
                c("± SE" = "se", "95% confidence interval" = "ci"),
                inline = TRUE
              ),
              helpText(
                "Letter groups come from the last run of the Mean comparison tab (Tukey, LSD or DMRT)."
              )
            )
          ),
          plot_live_ui("sea"),
          plot_export_ui("sea")
        ),
        tabPanel(
          "Assumptions",
          br(),
          help_box(
            "How to read these plots",
            p(
              "Residuals against fitted values: the points should scatter evenly around 0, without a funnel or a curve."
            ),
            p(
              "Normal Q-Q plot: the points should lie close to the line; the histogram should look roughly bell-shaped. With many observations the Shapiro-Wilk test flags even small departures, so read it together with the plots."
            )
          ),
          stale_dim(uiOutput("sea_diag_pick")),
          stale_dim(plotOutput("sea_diag", height = "560px"))
        ),
        tabPanel("Data", br(), uiOutput("sea_data"))
      )
    )
  )
)

met_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Method",
      radioButtons(
        "met_method",
        NULL,
        c(
          "Combined ANOVA (classic analysis of variance across sites)" = "anova",
          "One-stage: all plots in one model" = "one",
          "Two-stage: stage-1 means of each site" = "two"
        )
      ),
      conditionalPanel(
        "input.met_method == 'anova'",
        radioButtons(
          "met_env_random",
          "Environments are",
          c(
            "Random: a sample of the target region (test genotypes against GxE)" = "random",
            "Fixed: only these sites matter (test against the pooled error)" = "fixed"
          )
        )
      ),
      conditionalPanel(
        "input.met_method == 'two'",
        radioButtons(
          "met_s1",
          "Stage-1 means",
          c(
            "Computed here from the plot data" = "calc",
            "From a file of means (Env, Genotype, Mean, SE, error variance, reps)" = "file"
          )
        ),
        radioButtons(
          "met_weight",
          "Weights",
          c("None" = "none", "1 / SE² of the stage-1 means" = "stderr"),
          inline = TRUE
        )
      ),
      help_box(
        "One or two stages?",
        p(
          strong("One-stage"),
          " fits all plots of all sites together, with the blocks nested in sites; it uses all the information."
        ),
        p(
          strong("Two-stage"),
          " first analyses each site (stage 1, genotype fixed) and then combines the site means (stage 2). It is lighter and works when only the means of each site are available."
        ),
        p(
          strong("Combined ANOVA"),
          " is the classic analysis of variance of all sites together (Gomez & Gomez 1984): environment, replicates within environment, genotype and genotype x environment, with F tests, CV and LSD, and Bartlett's test of the homogeneity of the error variances of the sites. Best with complete data."
        )
      )
    ),
    conditionalPanel(
      "input.met_method != 'two' || input.met_s1 == 'calc'",
      side_step(
        2,
        "Experimental design",
        radioButtons(
          "met_design",
          NULL,
          c("Randomized complete block (RCBD)" = "RCB")
        ),
        help_box(
          "Design",
          p(
            "Randomized complete block design (RCBD) at every site: every genotype once in each block (replicate)."
          )
        )
      )
    ),
    data_step("met", "met", 3),
    side_step(4, "Variables", uiOutput("met_vars")),
    conditionalPanel(
      "input.met_method != 'anova'",
      side_step(5, "Genotype effect", effect_radio("met_random"), effect_help)
    ),
    run_bar("go_met", "Run multi-environment analysis")
  ),
  mainPanel(
    uiOutput("met_msg"),
    div(
      id = "met_out",
      tabsetPanel(
        id = "met_tabs",
        tabPanel(
          "Results",
          br(),
          stale_dim(uiOutput("met_results")),
          br(),
          stale_dim(downloads("met"))
        ),
        tabPanel(
          "Mean comparison",
          br(),
          pw_help,
          pw_controls("met"),
          checkboxInput(
            "met_pw_all",
            "Show every pair, not only the significant ones"
          ),
          stale_dim(uiOutput("met_pw_out"))
        ),
        tabPanel(
          "Plot",
          br(),
          radioButtons(
            "met_plot_type",
            NULL,
            c(
              "Genotype means" = "geno",
              "Genotype x environment heatmap" = "heat",
              "Genotype x environment (interaction plot)" = "ge",
              "Variance components" = "vc"
            ),
            inline = TRUE
          ),
          conditionalPanel(
            "input.met_plot_type == 'geno'",
            fluidRow(
              column(
                6,
                radioButtons(
                  "met_plot_style",
                  "Style",
                  c(
                    "Points with error bars and letters (publication)" = "dot",
                    "Bar chart" = "bar"
                  )
                )
              ),
              column(
                6,
                radioButtons(
                  "met_err",
                  "Error bars",
                  c("± SE" = "se", "95% confidence interval" = "ci"),
                  inline = TRUE
                ),
                helpText(
                  "Letter groups come from the last run of the Mean comparison tab (Tukey, LSD or DMRT)."
                )
              )
            )
          ),
          plot_live_ui("met"),
          plot_export_ui("met")
        ),
        tabPanel(
          "Assumptions",
          br(),
          stale_dim(uiOutput("met_diag_pick")),
          stale_dim(plotOutput("met_diag", height = "560px"))
        ),
        tabPanel("Data", br(), uiOutput("met_data"))
      )
    )
  )
)

gge_types <- c(
  "1. Basic biplot" = 1,
  "2. Mean vs. stability" = 2,
  "3. Which-won-where" = 3,
  "4. Discriminativeness vs. representativeness" = 4,
  "5. Examine an environment" = 5,
  "6. Ranking environments" = 6,
  "7. Examine a genotype" = 7,
  "8. Ranking genotypes" = 8,
  "9. Compare two genotypes" = 9,
  "10. Relationship among environments" = 10
)
gge_help <- help_box(
  "Which GGE biplot? (Yan & Kang 2003)",
  p(strong("1. Basic:"), " genotypes and environments on PC1 and PC2."),
  p(
    strong("2. Mean vs. stability:"),
    " the arrow on the average-environment axis points to higher mean yield; a genotype far from the axis is unstable. Best read with genotype-focused SVP."
  ),
  p(
    strong("3. Which-won-where:"),
    " the polygon joins the genotypes farthest from the origin; lines from the origin cut the plot into sectors (mega-environments), and the vertex genotype of a sector wins in the environments inside it."
  ),
  p(
    strong("4. Discriminativeness vs. representativeness:"),
    " a long environment vector discriminates well; a small angle with the average-environment axis means representative. Best read with environment-focused SVP."
  ),
  p(
    strong("5. Examine an environment:"),
    " ranks the genotypes in the environment you choose."
  ),
  p(
    strong("6. Ranking environments:"),
    " environments against the ideal environment (centre of the circles)."
  ),
  p(
    strong("7. Examine a genotype:"),
    " the environments where the genotype you choose does best."
  ),
  p(
    strong("8. Ranking genotypes:"),
    " genotypes against the ideal genotype (high mean, stable)."
  ),
  p(
    strong("9. Compare two genotypes:"),
    " the line joining them and its perpendicular; environments on the side of a genotype favour it."
  ),
  p(
    strong("10. Relationship among environments:"),
    " the cosine of the angle between environment vectors approximates their correlation: acute = positive, right angle = none, obtuse = negative."
  )
)
gxe_plot_controls <- tagList(
  conditionalPanel(
    "input.gxe_kind == 'fw' || input.gxe_kind == 'er'",
    selectInput(
      "reg_view",
      "Plot",
      c(
        "Mean vs. slope b (four parts, publication)" = "quad",
        "Regression lines / slope vs. s2di" = "orig",
        "Genotype x environment heatmap" = "heat"
      ),
      width = "380px"
    )
  ),
  conditionalPanel(
    "input.gxe_kind == 'shukla' || input.gxe_kind == 'stats'",
    selectInput(
      "stab_view",
      "Plot",
      c(
        "Mean vs. stability" = "orig",
        "Genotype x environment heatmap" = "heat"
      ),
      width = "380px"
    )
  ),
  conditionalPanel(
    "input.gxe_kind == 'gge'",
    fluidRow(
      column(
        6,
        selectInput("gge_type", "GGE biplot view", gge_types, width = "100%")
      ),
      column(6, uiOutput("gge_pick"))
    ),
    fluidRow(
      column(
        4,
        sliderInput(
          "gge_text",
          "Label size",
          min = 2,
          max = 7,
          value = 3.5,
          step = 0.5
        )
      ),
      column(
        4,
        sliderInput(
          "gge_point",
          "Point size",
          min = 1,
          max = 5,
          value = 2.2,
          step = 0.2
        )
      ),
      column(
        4,
        br(),
        checkboxInput("gge_repel", "Keep labels apart (repel)", TRUE)
      )
    ),
    gge_help
  ),
  conditionalPanel(
    "input.gxe_kind == 'waasb'",
    selectInput(
      "waasb_view",
      "Plot",
      c(
        "WAASBY ranking (live weight)" = "rank",
        "Mean vs. WAASB (four quadrants)" = "quad",
        "Biplot of the GxE BLUPs: PC1 vs. PC2" = "pc",
        "Ranks over weighting scenarios (mean 0-100%)" = "scen",
        "Genotype BLUPs with confidence intervals" = "blup",
        "Eigenvalues of the GxE BLUP matrix (scree plot)" = "eigen",
        "Residuals of the mixed model" = "resid"
      ),
      width = "380px"
    )
  ),
  conditionalPanel(
    "input.gxe_kind == 'mtsi'",
    selectInput(
      "mt_view",
      "Plot",
      c(
        "MGIDI: selected genotypes" = "mgidi",
        "MGIDI: strengths and weaknesses" = "mgidi_contri",
        "MTSI: selected genotypes" = "mtsi",
        "MTSI: strengths and weaknesses" = "mtsi_contri",
        "Correlations among the traits (genotype means)" = "corr",
        "Genotypes selected by MTSI and MGIDI (Venn)" = "venn"
      ),
      width = "380px"
    )
  ),
  conditionalPanel(
    "input.gxe_kind == 'ammi'",
    selectInput(
      "ammi_type",
      "AMMI biplot",
      c(
        "AMMI1: mean vs. PC1" = 1,
        "AMMI2: PC1 vs. PC2" = 2,
        "Nominal yield vs. environment PC1" = 4,
        "Share of the interaction by term (scree plot)" = "eigen",
        "Residuals of the AMMI model" = "resid"
      ),
      width = "380px"
    )
  )
)

gxe_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Method",
      grouped_radio(
        "gxe_kind",
        list(
          `Stability` = c(
            "Finlay-Wilkinson regression" = "fw",
            "Eberhart-Russell regression" = "er",
            "Shukla's stability variance" = "shukla",
            "Stability statistics (several indices)" = "stats"
          ),
          `Multivariate biplots` = c("AMMI" = "ammi", "GGE biplot" = "gge"),
          `Modern BLUP & multi-trait selection` = c(
            "WAASB / WAASBY (BLUP-based stability)" = "waasb",
            "Multi-trait selection (MTSI / MGIDI)" = "mtsi"
          )
        )
      ),
      help_box(
        "Which analysis?",
        h6(class = "fw-bold mt-1", "Stability"),
        p(
          strong("Finlay-Wilkinson:"),
          " regression of each genotype on the environment index (site mean minus grand mean). A slope near 1 means average response; above 1, the genotype gains more in good sites."
        ),
        p(
          strong("Eberhart-Russell:"),
          " the same slope plus the deviation from regression (s2di) and the joint-regression ANOVA. A genotype is stable when its slope does not differ from 1 and s2di does not differ from 0 (alpha = 0.05)."
        ),
        p(
          strong("Shukla:"),
          " the share of the GxE interaction variance due to each genotype; small is stable."
        ),
        p(
          strong("Stability statistics:"),
          " Wricke's ecovalence, Shukla, Eberhart-Russell, Annicchiarico, Lin and Binns superiority, ASV and WAASB in one table."
        ),
        h6(class = "fw-bold mt-2", "Multivariate biplots"),
        p(
          strong("AMMI and GGE:"),
          " split the interaction (GGE: genotype plus interaction) into principal components and show genotypes and sites on a biplot."
        ),
        h6(class = "fw-bold mt-2", "Modern BLUP & multi-trait selection"),
        p(
          strong("WAASB / WAASBY:"),
          " stability from a linear mixed model (genotype and GxE random): the weighted average of the absolute scores of all principal components of the GxE BLUPs, not only PC1 and PC2. WAASBY joins the mean and the stability with weights you choose (Olivoto et al. 2019)."
        ),
        p(
          strong("MTSI / MGIDI:"),
          " select genotypes on several traits at once: factor analysis groups correlated traits, then genotypes are ranked by their distance to the ideotype (the best value of every trait). MTSI uses the WAASBY of each trait (mean and stability), MGIDI the genotype BLUPs or means (Olivoto et al. 2019; Olivoto & Nardino 2021)."
        )
      )
    ),
    data_step("gxe", "gxe", 2),
    side_step(3, "Variables", uiOutput("gxe_vars")),
    conditionalPanel(
      "input.gxe_kind == 'waasb'",
      side_step(
        4,
        "Selection (updates live)",
        sliderInput(
          "wy_live",
          "Weight of the mean in WAASBY (%); the rest goes to stability",
          min = 0,
          max = 100,
          value = 50,
          step = 5
        ),
        sliderInput(
          "si_live",
          "Genotypes to select (%)",
          min = 5,
          max = 50,
          value = 15,
          step = 5
        )
      )
    ),
    conditionalPanel(
      "input.gxe_kind == 'mtsi'",
      side_step(
        4,
        "Selection",
        sliderInput(
          "gxe_si",
          "Genotypes to select (selection intensity, %)",
          min = 5,
          max = 50,
          value = 15,
          step = 5
        ),
        sliderInput(
          "gxe_mt_wy",
          "MTSI: weight of the mean in WAASBY (%)",
          min = 0,
          max = 100,
          value = 50,
          step = 5
        ),
        radioButtons(
          "gxe_mgidi_on",
          "MGIDI computed on",
          c(
            "Genotype BLUPs (genotype random)" = "blup",
            "Genotype means" = "means"
          ),
          inline = TRUE
        )
      )
    ),
    advanced_box(
      conditionalPanel(
        "input.gxe_kind == 'waasb'",
        checkboxInput(
          "gxe_waasb_higher",
          "Higher values of the trait are better",
          TRUE
        )
      ),
      conditionalPanel(
        "input.gxe_kind == 'gge'",
        selectInput(
          "gge_scaling",
          "Scaling",
          c("None" = "none", "By standard deviation" = "sd")
        ),
        selectInput(
          "gge_svp",
          "Singular value partitioning",
          c(
            "Environment-focused" = "environment",
            "Genotype-focused" = "genotype",
            "Symmetrical" = "symmetrical"
          )
        )
      ),
      conditionalPanel(
        "input.gxe_kind == 'shukla'",
        checkboxInput(
          "shukla_pbtools",
          "Also fit the PBTools model (nlme::gls, one variance per genotype)",
          TRUE
        )
      ),
      helpText(
        "All analyses need complete data: every genotype in every environment, with replicates."
      )
    ),
    run_bar("go_gxe", "Run GxE analysis")
  ),
  mainPanel(
    uiOutput("gxe_msg"),
    div(
      id = "gxe_out",
      tabsetPanel(
        id = "gxe_tabs",
        tabPanel(
          "Results",
          br(),
          stale_dim(uiOutput("gxe_results")),
          br(),
          stale_dim(downloads("gxe"))
        ),
        tabPanel(
          "Plot",
          br(),
          gxe_plot_controls,
          plot_live_ui("gxe"),
          plot_export_ui("gxe")
        ),
        tabPanel("Data", br(), uiOutput("gxe_data"))
      )
    )
  )
)
