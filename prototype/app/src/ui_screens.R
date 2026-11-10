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

# "What you get" box of each choice in step 1 of the single-environment screen: when to use it, what it gives,
# what it does not give, and the question it answers
get_box <- function(use, gets, not = NULL, answers) {
  div(
    class = "get-box",
    lang = "th",
    p(strong("ใช้เมื่อ: "), use),
    p(strong("จะได้:")),
    tags$ul(lapply(gets, tags$li)),
    if (!is.null(not)) p(strong("ไม่ได้: "), not),
    p(class = "get-q", icon("circle-question"), " ", answers)
  )
}
sea_get_anova <- get_box(
  "การทดลอง RCBD ที่ข้อมูลครบหรือเกือบครบ และต้องการตาราง ANOVA แบบดั้งเดิม (ตามตำรา Gomez & Gomez)",
  list(
    "ตาราง ANOVA และ F test ว่าพันธุ์ต่างกันไหม",
    "ค่าเฉลี่ยพันธุ์ ± SE, CV และ LSD",
    "แท็บ Mean comparison: Tukey, Dunnett (เทียบพันธุ์มาตรฐาน), LSD, DMRT"
  ),
  "ความแปรปรวนทางพันธุกรรมและอัตราพันธุกรรม (เลือก Mixed model แบบ Random)",
  "พันธุ์ไหนให้ผลดีกว่ากันในการทดลองนี้"
)
sea_get_fixed <- get_box(
  "พันธุ์ในการทดลองคือพันธุ์ที่คุณต้องการเปรียบเทียบเอง (พันธุ์รับรอง สายพันธุ์ที่คัดแล้ว) หรือมีแปลงหาย",
  list(
    "ค่าเฉลี่ยปรับแล้ว (BLUE) ± SE และ SED: ขจัดอิทธิพลของบล็อก/สภาพแวดล้อมออก เปรียบเทียบศักยภาพพันธุ์ได้อย่างเป็นธรรมแม้ข้อมูลไม่สมดุล (unbalanced data)",
    "แท็บ Mean comparison: Tukey (ทุกคู่) หรือ Dunnett (เทียบกับพันธุ์มาตรฐาน)"
  ),
  "ความแปรปรวนทางพันธุกรรมและอัตราพันธุกรรม (เลือก Random)",
  "พันธุ์ไหนดีที่สุด และพันธุ์ใหม่ดีกว่าพันธุ์มาตรฐานไหม"
)
sea_get_random <- get_box(
  "พันธุ์เป็นตัวแทนของประชากรที่กำลังคัดเลือก (ลูกผสม สายพันธุ์รุ่นแรก ๆ)",
  list(
    "ความแปรปรวนทางพันธุกรรม (σ²g) และความคลาดเคลื่อน (σ²e) พร้อม likelihood ratio test ว่า σ²g มีนัยสำคัญไหม",
    "อัตราพันธุกรรม (H²): สูง (> 0.6) คัดเลือกจากค่าที่วัดได้มีแนวโน้มได้ผล; ต่ำ (< 0.3) ควรเพิ่มซ้ำหรือสถานที่ก่อนคัด",
    "ค่าทำนาย (BLUP) ใช้จัดอันดับเพื่อคัดเลือก: ค่าถูกหดเข้าหาค่าเฉลี่ย ลดโอกาสคัดพันธุ์ที่ดูดีเพราะบังเอิญ"
  ),
  "การเปรียบเทียบค่าเฉลี่ย (Tukey, Dunnett) ค่า H² จากสถานที่เดียวรวมปฏิสัมพันธ์พันธุ์ × สถานที่ไว้ จึงมักสูงกว่าค่าที่ใช้ได้ทั่วไป",
  "ลักษณะนี้ปรับปรุงด้วยการคัดเลือกได้ไหม และควรคัดพันธุ์ไหน"
)

sea_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Analysis",
      radioButtons(
        "sea_method",
        "Method",
        c(
          "Standard ANOVA (fixed blocks, F test, LSD, CV)" = "anova",
          "Mixed model (lme4, as PBTools)" = "mixed"
        )
      ),
      conditionalPanel(
        "input.sea_method == 'mixed'",
        radioButtons(
          "sea_random",
          "Genotype effect",
          c(
            "Fixed: compare the genotypes (BLUEs)" = "fixed",
            "Random: genetic variance and heritability (BLUPs)" = "random"
          )
        )
      ),
      conditionalPanel("input.sea_method == 'anova'", sea_get_anova),
      conditionalPanel("input.sea_method == 'mixed' && input.sea_random == 'fixed'", sea_get_fixed),
      conditionalPanel("input.sea_method == 'mixed' && input.sea_random == 'random'", sea_get_random)
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

met_get_anova <- get_box(
  "ข้อมูลครบ (ทุกพันธุ์ในทุกบล็อกของทุกสถานที่) และต้องการตาราง ANOVA รวมแบบดั้งเดิม (Gomez & Gomez)",
  list(
    "ตาราง ANOVA รวม: สถานที่, ซ้ำภายในสถานที่, พันธุ์ และพันธุ์ × สถานที่ (G×E) พร้อม F test, CV และ LSD",
    "Bartlett's test ว่าความคลาดเคลื่อนของแต่ละสถานที่เท่ากันไหม (ถ้าไม่เท่า การรวมวิเคราะห์ต้องระวัง)",
    "ค่าเฉลี่ยพันธุ์รวมทุกสถานที่ และแท็บ Mean comparison: Tukey, Dunnett, LSD, DMRT",
    "สถานที่เป็น Random: ทดสอบพันธุ์กับ G×E (ผลใช้กับพื้นที่เป้าหมายได้); Fixed: ทดสอบกับความคลาดเคลื่อนรวม (ผลใช้กับสถานที่เหล่านี้เท่านั้น)"
  ),
  "ความแปรปรวนทางพันธุกรรมและอัตราพันธุกรรม (เลือก Mixed model แบบ Random)",
  "พันธุ์ไหนดีเมื่อรวมทุกสถานที่ และพันธุ์ตอบสนองต่างกันตามสถานที่ไหม"
)
met_get_fixed <- get_box(
  "พันธุ์ในการทดลองคือพันธุ์ที่คุณต้องการเปรียบเทียบเอง หรือข้อมูลไม่ครบ (แปลงหาย บางพันธุ์ไม่ได้ปลูกบางที่)",
  list(
    "ค่าเฉลี่ยปรับแล้วรวมทุกสถานที่ (BLUE) ± SE",
    "แท็บ Mean comparison: Tukey (ทุกคู่) หรือ Dunnett (เทียบกับพันธุ์มาตรฐาน)",
    "One-stage: ทดสอบ G×E (likelihood ratio test) และความแปรปรวนของ G×E; Two-stage ไม่มีการทดสอบ G×E"
  ),
  "อัตราพันธุกรรม (เลือก Random)",
  "พันธุ์ไหนดีที่สุดโดยเฉลี่ยทุกสถานที่"
)
met_get_random <- get_box(
  "พันธุ์เป็นตัวแทนของประชากรที่กำลังคัดเลือก และสถานที่เป็นตัวแทนของพื้นที่เป้าหมาย",
  list(
    "ความแปรปรวนทางพันธุกรรม (σ²g), ของ G×E (σ²ge, One-stage) และความคลาดเคลื่อน (σ²e)",
    "อัตราพันธุกรรมข้ามสถานที่ H² = σ²g / (σ²g + σ²ge/e + σ²e/re): แยก G×E ออกแล้ว จึงใช้ได้ทั่วไปกว่า H² ของสถานที่เดียว",
    "ค่าทำนาย (BLUP) รวมทุกสถานที่ ใช้จัดอันดับเพื่อคัดเลือก",
    "ถ้า σ²ge ใหญ่เทียบกับ σ²g: พันธุ์ตอบสนองต่างกันมากตามสถานที่ ดูต่อในหน้า GxE & stability"
  ),
  "การเปรียบเทียบค่าเฉลี่ย (Tukey, Dunnett)",
  "ลักษณะนี้คัดเลือกได้ผลในพื้นที่เป้าหมายไหม และควรคัดพันธุ์ไหน"
)

met_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Analysis",
      radioButtons(
        "met_method",
        "Method",
        c(
          "Combined ANOVA (classic analysis of variance across sites)" = "anova",
          "Mixed model (lme4, as PBTools)" = "mixed"
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
        "input.met_method == 'mixed'",
        radioButtons(
          "met_random",
          "Genotype effect",
          c(
            "Fixed: compare the genotypes across sites (BLUEs)" = "fixed",
            "Random: genetic variance and heritability across sites (BLUPs)" = "random"
          )
        )
      ),
      conditionalPanel("input.met_method == 'anova'", met_get_anova),
      conditionalPanel("input.met_method == 'mixed' && input.met_random == 'fixed'", met_get_fixed),
      conditionalPanel("input.met_method == 'mixed' && input.met_random == 'random'", met_get_random),
      conditionalPanel(
        "input.met_method == 'mixed'",
        advanced_box(
          title = " How the sites are combined",
          radioButtons(
            "met_stage",
            "Model",
            c(
              "One model of all plots (one-stage)" = "one",
              "Site means first, then combined (two-stage)" = "two"
            )
          ),
          conditionalPanel(
            "input.met_stage == 'two'",
            radioButtons(
              "met_weight",
              "Weights of the site means",
              c("1 / SE² (precise sites count more)" = "stderr", "None" = "none")
            )
          ),
          div(
            class = "small text-muted",
            lang = "th",
            p(
              strong("One-stage"),
              " (ค่าเริ่มต้น): ใส่ข้อมูลทุกแปลงจากทุกสถานที่ใน model เดียว ใช้ข้อมูลครบที่สุด และทดสอบ G×E ได้"
            ),
            p(
              class = "mb-0",
              strong("Two-stage"),
              ": วิเคราะห์ทีละสถานที่ก่อน (BLUE และ SE) แล้วรวมค่าเฉลี่ย เร็วกว่า ใช้เมื่อมีสถานที่จำนวนมาก หรือความแม่นของแต่ละสถานที่ต่างกันมาก ทดสอบ G×E ไม่ได้ ถ่วงน้ำหนักด้วย 1/SE² ให้ผลใกล้ one-stage กว่า"
            )
          )
        )
      )
    ),
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
    ),
    data_step("met", "met", 3),
    side_step(4, "Variables", uiOutput("met_vars")),
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

# "What you get" box of each analysis of the GxE & stability screen
gxe_get <- list(
  fw = get_box(
    "อยากรู้ว่าพันธุ์ตอบสนองต่อสภาพแวดล้อมที่ดีขึ้นหรือแย่ลงอย่างไร (ควรมีอย่างน้อย 4–5 สถานที่)",
    list(
      "slope b ของแต่ละพันธุ์บนดัชนีสภาพแวดล้อม: b ≈ 1 ปรับตัวได้ทั่วไป, b > 1 ให้ผลดีในที่สภาพดี, b < 1 ทนสภาพแย่",
      "กราฟเส้นถดถอยของแต่ละพันธุ์ และกราฟค่าเฉลี่ย vs. slope"
    ),
    "ความแม่นของการทำนาย (ใช้ Eberhart-Russell) และพันธุ์ไหนเหมาะกับที่ไหน (ใช้ GGE)",
    "พันธุ์ไหนตอบสนองต่อสภาพแวดล้อมดี และพันธุ์ไหนทนสภาพแย่"
  ),
  er = get_box(
    "อยากรู้ว่าพันธุ์ให้ผลสูงและคงที่ทุกสภาพแวดล้อมไหม (ควรมีอย่างน้อย 4–5 สถานที่)",
    list(
      "slope b: b ≈ 1 ปรับตัวทั่วไป, b > 1 เหมาะกับที่ดี, b < 1 ทนที่แย่",
      "s²di: ทำนายผลของพันธุ์ได้แม่นแค่ไหน (ค่าใกล้ 0 ทำนายได้ดี)",
      "ANOVA ของ joint regression และกราฟค่าเฉลี่ย vs. b แบ่งสี่ส่วน"
    ),
    "บอกว่าพันธุ์ไหนเหมาะกับที่ไหน (ใช้ GGE)",
    "พันธุ์ไหนให้ผลสูงและคงที่ทุกสภาพแวดล้อม"
  ),
  shukla = get_box(
    "อยากจัดอันดับเสถียรภาพของพันธุ์ด้วยค่าเดียว",
    list(
      "ความแปรปรวนเสถียรภาพ (σ²i) ของแต่ละพันธุ์: ส่วนของ G×E ที่มาจากพันธุ์นั้น ค่าน้อยคือมีเสถียรภาพ พร้อม F test",
      "ทั้งสูตรของ Shukla (1972) และแบบที่ PBTools ใช้ (nlme::gls)"
    ),
    "บอกทิศทางการตอบสนอง (ใช้ Finlay-Wilkinson หรือ Eberhart-Russell)",
    "พันธุ์ไหนเสถียรที่สุด"
  ),
  stats = get_box(
    "อยากเทียบหลายดัชนีเสถียรภาพพร้อมกัน หรือต้องรายงานดัชนีที่วารสารขอ",
    list(
      "ตารางเดียว: Wricke's ecovalence, Shukla, Eberhart-Russell, Annicchiarico, Lin & Binns superiority, ASV และ WAASB",
      "อันดับของแต่ละพันธุ์ในแต่ละดัชนี"
    ),
    "คำตอบเดียวว่าพันธุ์ไหนดีที่สุด แต่ละดัชนีวัดเสถียรภาพต่างแบบกัน อันดับจึงไม่ตรงกันเสมอ",
    "พันธุ์ไหนเสถียรเมื่อดูจากหลายดัชนี"
  ),
  ammi = get_box(
    "อยากแยกและดูรูปแบบของ G×E (ควรมีอย่างน้อย 4–5 สถานที่)",
    list(
      "ANOVA ของ AMMI: แบ่ง G×E เป็น principal components (PC) พร้อม F test ว่า PC ไหนมีนัยสำคัญ",
      "AMMI1 (ค่าเฉลี่ย vs. PC1) และ AMMI2 (PC1 vs. PC2) biplot: พันธุ์ใกล้จุดกำเนิดคือเสถียร พันธุ์กับสถานที่ที่อยู่ทางเดียวกันเข้ากันดี",
      "ดัชนีเสถียรภาพจาก AMMI (เช่น ASV)"
    ),
    "แบ่งพื้นที่ (mega-environment) ได้ชัดเท่า GGE",
    "G×E มีรูปแบบอย่างไร และพันธุ์ไหนเสถียร"
  ),
  gge = get_box(
    "อยากแบ่งพื้นที่และเลือกพันธุ์เฉพาะพื้นที่ (ควรมีอย่างน้อย 4–5 สถานที่)",
    list(
      "Which-won-where: พันธุ์ที่ชนะในแต่ละกลุ่มสถานที่ (mega-environment)",
      "Mean vs. stability และพันธุ์ในอุดมคติ: พันธุ์ที่ให้ผลสูงและเสถียร",
      "Discriminativeness vs. representativeness: สถานที่ทดสอบไหนแยกพันธุ์ได้ดีและเป็นตัวแทนพื้นที่"
    ),
    "การทดสอบทางสถิติของ G×E (ใช้ AMMI หรือ Multi-environment)",
    "พันธุ์ไหนชนะที่ไหน และควรแนะนำพันธุ์แยกตามพื้นที่ไหม"
  ),
  waasb = get_box(
    "อยากจัดอันดับพันธุ์ด้วยผลผลิตและเสถียรภาพพร้อมกันในค่าเดียว",
    list(
      "WAASB: เสถียรภาพจาก BLUP ของ G×E ใช้ทุก PC ไม่ใช่แค่ PC1–PC2 (ค่าน้อยคือมีเสถียรภาพ)",
      "WAASBY: คะแนนรวมของผลผลิตและเสถียรภาพ ปรับน้ำหนักได้ด้วยแถบเลื่อน (step 4) ผลเปลี่ยนทันที",
      "ความแปรปรวนทางพันธุกรรมและอัตราพันธุกรรมจาก mixed model"
    ),
    "บอกว่าพันธุ์ไหนเหมาะกับที่ไหน (ใช้ GGE)",
    "พันธุ์ไหนดีที่สุดเมื่อคิดทั้งผลผลิตและเสถียรภาพ"
  ),
  mtsi = get_box(
    "ต้องคัดพันธุ์จากหลายลักษณะพร้อมกัน (อย่างน้อย 2 ลักษณะ)",
    list(
      "พันธุ์ที่ถูกเลือกตามสัดส่วนที่กำหนด (selection intensity)",
      "MTSI: ใช้ WAASBY ของแต่ละลักษณะ (ผลผลิตและเสถียรภาพ); MGIDI: ใช้ BLUP หรือค่าเฉลี่ยของพันธุ์",
      "จุดแข็งและจุดอ่อนของแต่ละพันธุ์ และ selection differential ของแต่ละลักษณะ"
    ),
    "ผลของลักษณะเดียว (ใช้วิธีอื่นในหน้านี้)",
    "ควรคัดพันธุ์ไหนเมื่อต้องการหลายลักษณะดีพร้อมกัน"
  )
)

gxe_ui <- sidebarLayout(
  sidebarPanel(
    side_step(
      1,
      "Analysis",
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
      lapply(names(gxe_get), function(k) {
        conditionalPanel(sprintf("input.gxe_kind == '%s'", k), gxe_get[[k]])
      }),
      help_box(
        "How each analysis works",
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
