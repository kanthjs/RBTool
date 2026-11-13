# RBTool web app: single-environment, multi-environment and GxE / stability analyses of plant breeding trials, in
# the spirit of IRRI PBTools, computed with published R packages (lme4, emmeans, multcomp, nlme, metan). The layout and
# look follow RDstat (github.com/kanthjs/RDstat). The engine sources in ./engine are copied from engine/R by
# scripts/build_app.R.
library(shiny)

# Packages used by the engine and the app; this block (never run) lets Shinylive see that they must be bundled
if (FALSE) {
  lme4::lmer
  emmeans::emmeans
  multcomp::glht
  multcompView::multcompLetters
  nlme::gls
  metan::ge_reg
  agricolae::duncan.test
  readxl::read_excel
  writexl::write_xlsx
  ggplot2::ggplot
  svglite::svglite
  ragg::agg_png
  jsonlite::base64_enc
  htmltools::htmlEscape
}
engine_dir <- if (dir.exists("engine")) {
  "engine"
} else {
  file.path("..", "..", "engine", "R")
}
for (f in sort(list.files(engine_dir, pattern = "[.]R$", full.names = TRUE))) {
  source(f, local = FALSE)
}

app_name <- "RBTool"
app_maintainer <- "Kanthanawit Jaisong"
# Feedback form; set RBTOOL_FEEDBACK_URL to use another form
feedback_form_url <- Sys.getenv(
  "RBTOOL_FEEDBACK_URL",
  "https://docs.google.com/forms/d/e/1FAIpQLSdDmLjXssZG49TLmOreuoG4x0kcZkHJnH5XVUq-RvMTfkz8IA/viewform"
)
source("src/helpers.R", local = TRUE)
source("src/style.R", local = TRUE) # brand colours and CSS (from RDstat)
source("src/data.R", local = TRUE) # examples, data step, variable pickers
source("src/ui_screens.R", local = TRUE) # the three analysis screens
source("src/gxe_interpret.R", local = TRUE) # Thai reading of the AMMI and GGE biplots
source("report.R", local = TRUE) # Word report, method paragraphs

home_ui <- div(
  class = "container",
  style = "max-width: 1040px;",
  div(
    class = "hero",
    h2(icon("seedling"), " ", app_name),
    p(
      "Rice Breeding Tool คือ Interactive Web application เพื่อวิเคราะห์ข้อมูลการทดลองปรับปรุงพันธุ์พืช โดยมีโปรแกรม PBTools ของ IRRI เป็นแนวทาง อาศัย R package ต่าง ๆ ที่ได้รับการยอมรับ ใช้งานผ่านเบราว์เซอร์ได้ทันที"
    )
  ),
  h4("ต้องการวิเคราะห์อะไร", class = "mt-0"),
  bslib::layout_column_wrap(
    width = "300px",
    fill = FALSE,
    heights_equal = "row",
    home_card(
      "home_sea",
      "location-dot",
      "Single environment Analysis",
      "วิเคราะห์ความแปรปรวน (ANOVA) ค่าเฉลี่ยพันธุ์ปรับแล้ว (BLUE) หรือค่าทำนาย (BLUP) องค์ประกอบความแปรปรวน และอัตราพันธุกรรม แยกแต่ละสถานที่",
      "RCBD · Tukey, Dunnett, Duncan ตัวอักษรกลุ่ม"
    ),
    home_card(
      "home_met",
      "map-location-dot",
      "Multi-environment Analysis",
      "รวมหลายสถานที่ในโมเดลเดียว (one-stage) หรือรวมค่าเฉลี่ยของแต่ละสถานที่ (two-stage) ทดสอบปฏิสัมพันธ์พันธุ์ × สถานที่",
      "BLUE / BLUP รวมทุกสถานที่ · heritability · กราฟปฏิสัมพันธ์"
    ),
    home_card(
      "home_gxe",
      "chart-line",
      "G×E and Stability",
      "พันธุ์ใดให้ผลสูงและเสถียร: Finlay-Wilkinson, Eberhart-Russell, Shukla, AMMI, GGE biplot และคัดเลือกพันธุ์ด้วย WAASB / WAASBY และหลายลักษณะ (MTSI / MGIDI)",
      "ต้องมีข้อมูลทุกพันธุ์ในทุกสถานที่ พร้อมซ้ำ"
    )
  ),
  h4("ใช้งานอย่างไร"),
  div(
    class = "steps",
    home_step(1, "เลือกการวิเคราะห์", "จากการ์ดด้านบน หรือเมนูด้านบนของหน้า"),
    home_step(
      2,
      "เลือกข้อมูล",
      "เริ่มด้วยข้อมูลตัวอย่าง หรืออัปโหลด .csv / .xlsx / .xls หนึ่งแถวต่อหนึ่งแปลง มีคอลัมน์พันธุ์ สถานที่ บล็อก/ซ้ำ และลักษณะที่วัด"
    ),
    home_step(
      3,
      "กด Run แล้วดูผล",
      "ตาราง กราฟ ตรวจสมมติฐาน และดาวน์โหลด Excel หรือรายงาน Word"
    )
  ),
  div(
    class = "alert alert-light border d-flex gap-2 align-items-start",
    icon("lock"),
    span("ข้อมูลของคุณถูกวิเคราะห์ในเบราว์เซอร์เท่านั้น ไม่บันทึกหรือไม่ถูกส่งไปยังเซิร์ฟเวอร์ใด")
  )
)

source("methods.R", local = TRUE) # methods_ui, about_ui

ui <- bslib::page_navbar(
  title = tags$span(
    icon("seedling"),
    app_name,
    tags$span(class = "badge bg-light text-danger", "beta")
  ),
  window_title = app_name,
  id = "nav",
  theme = bslib::bs_theme(version = 5),
  fillable = FALSE,
  lang = "th",
  navbar_options = bslib::navbar_options(bg = brand[["main"]], theme = "dark"),
  header = tags$head(
    google_sans_link,
    tags$script(HTML(copy_js)),
    tags$script(HTML(stale_js)),
    tags$script(HTML(download_js)),
    tags$style(HTML(app_css)),
    useBusyIndicators()
  ),
  tabPanel("Home", home_ui),
  tabPanel("Single environment", sea_ui, value = "sea"),
  tabPanel("Multi-environment", met_ui, value = "met"),
  tabPanel("GxE & stability", gxe_ui, value = "gxe"),
  bslib::nav_spacer(),
  bslib::nav_item(actionLink(
    "open_feedback",
    tagList(icon("comment-dots"), " ส่งข้อเสนอแนะ"),
    class = "nav-link text-white fw-semibold"
  )),
  bslib::nav_menu(
    "Info",
    align = "right",
    tabPanel("Methods", methods_ui),
    tabPanel("About", about_ui)
  )
)

server <- function(input, output, session) {
  rv <- reactiveValues()
  stale_extra <- list(gxe = c("gge_scaling", "gge_svp", "shukla_pbtools"))
  observeEvent(input$home_sea, bslib::nav_select("nav", "sea"))
  observeEvent(input$home_met, bslib::nav_select("nav", "met"))
  observeEvent(input$home_gxe, bslib::nav_select("nav", "gxe"))
  source("src/server_shared.R", local = TRUE)
  source("src/server_sea.R", local = TRUE)
  source("src/server_met.R", local = TRUE)
  source("src/server_gxe.R", local = TRUE)
  source("src/server_feedback.R", local = TRUE) # feedback form and diagnostics
  output$methods_text <- renderUI(methods_packages_ui())
  output$methods_topics <- renderUI(methods_topics_ui())
  for (t in method_topics) {
    local({
      tp <- t
      observeEvent(
        input[[paste0("cp_topic_", tp$id)]],
        session$sendCustomMessage(
          "copy_text",
          list(what = tp$title, text = topic_copy_text(tp))
        )
      )
    })
  }
  observeEvent(
    input$cp_refs,
    session$sendCustomMessage(
      "copy_text",
      list(
        what = "Software references",
        text = paste(
          package_refs(),
          collapse = "
"
        )
      )
    )
  )
  observeEvent(
    input$cp_mrefs,
    session$sendCustomMessage(
      "copy_text",
      list(
        what = "Method references",
        text = paste(
          method_refs,
          collapse = "
"
        )
      )
    )
  )
}

shinyApp(ui, server)
