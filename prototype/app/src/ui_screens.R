# The three analysis screens. Each is a sidebar of numbered steps with the Run button at the bottom, and tabs of
# results, as in RDstat.

design_radio <- function(id) radioButtons(id, NULL, c(
  "Randomized complete block (RCB)" = "RCB", "Alpha-lattice" = "Alpha", "Row-column" = "RowCol",
  "Augmented RCB" = "AugRCB", "Augmented latin square" = "AugLS",
  "Latinized alpha-lattice" = "LatinAlpha", "Latinized row-column" = "LatinRowCol"))
design_help <- help_box("Which design?",
  p(strong("RCB:"), " every genotype once in each block (replicate)."),
  p(strong("Alpha-lattice:"), " replicates divided into incomplete blocks; needs the replicate and block columns."),
  p(strong("Row-column:"), " each replicate laid out in rows and columns; needs replicate, row and column."),
  p(strong("Augmented RCB / latin square:"), " checks repeated in every block (or in a latin square of rows and columns), test entries planted once."),
  p(strong("Latinized:"), " alpha-lattice or row-column designs whose blocks (rows) run across replicates; in the latinized row-column design the row numbers continue from one replicate to the next."))
effect_radio <- function(id) radioButtons(id, NULL, c("Fixed: BLUEs (means) and mean comparison" = "fixed", "Random: BLUPs and heritability" = "random"))
effect_help <- help_box("Fixed or random?",
  p(strong("Fixed:"), " the genotypes are the ones you want to compare (released varieties, selected lines). Gives adjusted means (BLUEs) with SE, and Tukey or Dunnett comparisons."),
  p(strong("Random:"), " the genotypes stand for a population (a breeding population, a set of new lines). Gives predicted values (BLUPs), the genetic variance and the heritability."))
downloads <- function(p) conditionalPanel(sprintf("input.go_%s > 0", p),
  downloadButton(paste0("dl_", p, "_xlsx"), "Download Excel (.xlsx)"), downloadButton(paste0("dl_", p, "_doc"), "Download report (Word)"))
pw_controls <- function(p) fluidRow(
  column(4, radioButtons(paste0(p, "_pw_type"), "Comparison", c("Tukey: all pairs" = "Tukey", "Dunnett: against a control" = "Dunnett"))),
  column(3, selectInput(paste0(p, "_pw_alpha"), "Significance level", c("0.05" = 0.05, "0.01" = 0.01, "0.10" = 0.10))),
  column(5, conditionalPanel(sprintf("input.%s_pw_type == 'Dunnett'", p), selectizeInput(paste0(p, "_pw_control"), "Control genotype(s)", NULL, multiple = TRUE))),
  column(12, checkboxInput(paste0(p, "_pw_exact"), "Exact intervals as in PBTools (multcomp single-step; slower, seconds per site)", FALSE)),
  column(12, actionButton(paste0("go_", p, "_pw"), tagList(icon("play"), " Compare means"), class = "btn-primary")))
pw_help <- help_box("About the comparisons",
  p("Simultaneous confidence intervals of the differences (multcomp), on the model with genotype fixed, as PBTools does: a difference is significant when its interval does not include 0."),
  p("By default the intervals come from emmeans (Tukey-Kramer, Dunnett): fast, and the same as the exact ones for balanced data; elsewhere within about 1%. Tick the exact option for the multcomp intervals PBTools uses."),
  p("Tukey also gives letter groups: genotypes sharing a letter do not differ significantly. PBTools lists only the significant pairs; tick the box to see every pair."))

sea_ui <- sidebarLayout(
  sidebarPanel(
    side_step(1, "Data", radioButtons("sea_src", NULL, c("Built-in example" = "ex", "Upload file" = "up"), inline = TRUE),
      conditionalPanel("input.sea_src == 'ex'", selectInput("sea_example", NULL, examples_for("sea"))),
      conditionalPanel("input.sea_src == 'up'", fileInput("sea_file", "Data file (.csv, .txt, .xlsx)", accept = c(".csv", ".txt", ".tsv", ".xlsx", ".xls"))),
      help_box("Design and file layout", p("Randomized complete block design (RCBD): every genotype once in each block (replicate)."),
        p("One row per plot, the first row holds the column names: genotype, block, the environment if the file has several sites, and one column per trait."))),
    side_step(2, "Variables", uiOutput("sea_vars")),
    side_step(3, "Genotype effect", effect_radio("sea_random"), effect_help),
    run_bar("go_sea", "Run single-environment analysis")),
  mainPanel(
    uiOutput("sea_msg"),
    div(id = "sea_out", tabsetPanel(id = "sea_tabs",
      tabPanel("Results", br(), stale_dim(uiOutput("sea_results")), br(), stale_dim(downloads("sea"))),
      tabPanel("Mean comparison", br(), pw_help, pw_controls("sea"), checkboxInput("sea_pw_all", "Show every pair, not only the significant ones"), stale_dim(uiOutput("sea_pw_out"))),
      tabPanel("Plot", br(), stale_dim(uiOutput("sea_plot_pick")), stale_dim(plotOutput("sea_plot", height = "460px"))),
      tabPanel("Assumptions", br(), help_box("How to read these plots",
          p("Residuals against fitted values: the points should scatter evenly around 0, without a funnel or a curve."),
          p("Normal Q-Q plot: the points should lie close to the line; the histogram should look roughly bell-shaped. With many observations the Shapiro-Wilk test flags even small departures, so read it together with the plots.")),
        stale_dim(uiOutput("sea_diag_pick")), stale_dim(plotOutput("sea_diag", height = "560px"))),
      tabPanel("Data", br(), uiOutput("sea_data"))))))

met_ui <- sidebarLayout(
  sidebarPanel(
    side_step(1, "Method",
      radioButtons("met_method", NULL, c("One-stage: all plots in one model" = "one", "Two-stage: stage-1 means of each site" = "two")),
      conditionalPanel("input.met_method == 'two'",
        radioButtons("met_s1", "Stage-1 means", c("Computed here from the plot data" = "calc", "From a file of means (Env, Genotype, Mean, SE, error variance, reps)" = "file")),
        radioButtons("met_weight", "Weights", c("None" = "none", "1 / SE² of the stage-1 means" = "stderr"), inline = TRUE)),
      help_box("One or two stages?",
        p(strong("One-stage"), " fits all plots of all sites together, with the blocks nested in sites; it uses all the information."),
        p(strong("Two-stage"), " first analyses each site (stage 1, genotype fixed) and then combines the site means (stage 2). It is lighter and works when only the means of each site are available."))),
    conditionalPanel("input.met_method == 'one' || input.met_s1 == 'calc'", side_step(2, "Experimental design", design_radio("met_design"))),
    data_step("met", "met", 3),
    side_step(4, "Variables", uiOutput("met_vars")),
    side_step(5, "Genotype effect", effect_radio("met_random"), effect_help),
    run_bar("go_met", "Run multi-environment analysis")),
  mainPanel(
    uiOutput("met_msg"),
    div(id = "met_out", tabsetPanel(id = "met_tabs",
      tabPanel("Results", br(), stale_dim(uiOutput("met_results")), br(), stale_dim(downloads("met"))),
      tabPanel("Mean comparison", br(), pw_help, pw_controls("met"), checkboxInput("met_pw_all", "Show every pair, not only the significant ones"), stale_dim(uiOutput("met_pw_out"))),
      tabPanel("Plot", br(), radioButtons("met_plot_type", NULL, c("Genotype means" = "geno", "Genotype x environment (interaction plot)" = "ge"), inline = TRUE),
        stale_dim(plotOutput("met_plot", height = "480px"))),
      tabPanel("Assumptions", br(), stale_dim(uiOutput("met_diag_pick")), stale_dim(plotOutput("met_diag", height = "560px"))),
      tabPanel("Data", br(), uiOutput("met_data"))))))

gxe_ui <- sidebarLayout(
  sidebarPanel(
    side_step(1, "Analysis",
      radioButtons("gxe_kind", NULL, c("Finlay-Wilkinson regression" = "fw", "Eberhart-Russell regression" = "er", "Shukla's stability variance" = "shukla",
                                       "Stability statistics (several indices)" = "stats", "AMMI" = "ammi", "GGE biplot" = "gge")),
      help_box("Which analysis?",
        p(strong("Finlay-Wilkinson:"), " regression of each genotype on the environment index (site mean minus grand mean). A slope near 1 means average response; above 1, the genotype gains more in good sites."),
        p(strong("Eberhart-Russell:"), " the same slope plus the deviation from regression (s2di) and the joint-regression ANOVA. A genotype is stable when its slope does not differ from 1 and s2di does not differ from 0 (alpha = 0.05)."),
        p(strong("Shukla:"), " the share of the GxE interaction variance due to each genotype; small is stable."),
        p(strong("Stability statistics:"), " Wricke's ecovalence, Shukla, Eberhart-Russell, Annicchiarico, Lin and Binns superiority, ASV and WAASB in one table."),
        p(strong("AMMI and GGE:"), " split the interaction (GGE: genotype plus interaction) into principal components and show genotypes and sites on a biplot."))),
    data_step("gxe", "gxe", 2),
    side_step(3, "Variables", uiOutput("gxe_vars")),
    advanced_box(
      conditionalPanel("input.gxe_kind == 'gge'",
        selectInput("gge_type", "GGE biplot", c("Basic biplot" = 1, "Mean vs. stability" = 2, "Which-won-where" = 3,
                                                "Discriminativeness vs. representativeness" = 4, "Ranking environments" = 6, "Ranking genotypes" = 8)),
        selectInput("gge_scaling", "Scaling", c("None" = "none", "By standard deviation" = "sd")),
        selectInput("gge_svp", "Singular value partitioning", c("Environment-focused" = "environment", "Genotype-focused" = "genotype", "Symmetrical" = "symmetrical"))),
      conditionalPanel("input.gxe_kind == 'ammi'",
        selectInput("ammi_type", "AMMI biplot", c("AMMI1: mean vs. PC1" = 1, "AMMI2: PC1 vs. PC2" = 2))),
      conditionalPanel("input.gxe_kind == 'shukla'", checkboxInput("shukla_pbtools", "Also fit the PBTools model (nlme::gls, one variance per genotype)", TRUE)),
      helpText("All analyses need complete data: every genotype in every environment, with replicates.")),
    run_bar("go_gxe", "Run GxE analysis")),
  mainPanel(
    uiOutput("gxe_msg"),
    div(id = "gxe_out", tabsetPanel(id = "gxe_tabs",
      tabPanel("Results", br(), stale_dim(uiOutput("gxe_results")), br(), stale_dim(downloads("gxe"))),
      tabPanel("Plot", br(), stale_dim(plotOutput("gxe_plot", height = "540px"))),
      tabPanel("Data", br(), uiOutput("gxe_data"))))))
