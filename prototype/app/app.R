# PBTool-Rev web app: single-environment, multi-environment and GxE / stability analyses of plant breeding trials, in
# the spirit of IRRI PBTools, computed with published R packages (lme4, emmeans, multcomp, nlme, metan). The layout and
# look follow RDstat (github.com/kanthjs/RDstat). The engine sources in ./engine are copied from engine/R by
# scripts/build_app.R.
library(shiny)

# Packages used by the engine and the app; this block (never run) lets Shinylive see that they must be bundled
if (FALSE) {
  lme4::lmer; emmeans::emmeans; multcomp::glht; multcompView::multcompLetters; nlme::gls; metan::ge_reg
  readxl::read_excel; writexl::write_xlsx; ggplot2::ggplot; jsonlite::base64_enc; htmltools::htmlEscape
}
engine_dir <- if (dir.exists("engine")) "engine" else file.path("..", "..", "engine", "R")
for (f in sort(list.files(engine_dir, pattern = "[.]R$", full.names = TRUE))) source(f, local = FALSE)

app_name <- "PBTool-Rev"
app_maintainer <- "Kanthanawit Jaisong"
source("src/helpers.R", local = TRUE)
source("src/style.R", local = TRUE)       # brand colours and CSS (from RDstat)
source("src/data.R", local = TRUE)        # examples, data step, variable pickers
source("src/ui_screens.R", local = TRUE)  # the three analysis screens
source("report.R", local = TRUE)          # Word report, method paragraphs

home_ui <- div(class = "container", style = "max-width: 1040px;",
  div(class = "hero", h2(icon("seedling"), " ", app_name),
      p("วิเคราะห์ข้อมูลการทดลองปรับปรุงพันธุ์พืช ตามแนวทางของโปรแกรม PBTools ของ IRRI: วิเคราะห์สถานที่เดียว หลายสถานที่ และปฏิสัมพันธ์พันธุ์กับสภาพแวดล้อม คำนวณด้วยแพ็กเกจ R ที่ได้รับการยอมรับ ใช้งานผ่านเบราว์เซอร์ได้ทันที")),
  h4("ต้องการวิเคราะห์อะไร", class = "mt-0"),
  bslib::layout_column_wrap(width = "300px", fill = FALSE, heights_equal = "row",
    home_card("home_sea", "location-dot", "วิเคราะห์สถานที่เดียว",
              "ค่าเฉลี่ยพันธุ์ปรับแล้ว (BLUE) หรือค่าทำนาย (BLUP) องค์ประกอบความแปรปรวน และอัตราพันธุกรรม แยกแต่ละสถานที่",
              "RCBD · Tukey, Dunnett, ตัวอักษรกลุ่ม"),
    home_card("home_met", "map-location-dot", "วิเคราะห์หลายสถานที่",
              "รวมหลายสถานที่ในโมเดลเดียว (one-stage) หรือรวมค่าเฉลี่ยของแต่ละสถานที่ (two-stage) ทดสอบปฏิสัมพันธ์พันธุ์ × สถานที่",
              "BLUE / BLUP รวมทุกสถานที่ · heritability · กราฟปฏิสัมพันธ์"),
    home_card("home_gxe", "chart-line", "G×E และความเสถียร",
              "พันธุ์ใดให้ผลสูงและเสถียร Finlay-Wilkinson, Shukla, ดัชนีความเสถียรหลายแบบ, AMMI และ GGE biplot",
              "ต้องมีข้อมูลทุกพันธุ์ในทุกสถานที่ พร้อมซ้ำ")),
  h4("ใช้งานอย่างไร"),
  div(class = "steps",
    home_step(1, "เลือกการวิเคราะห์", "จากการ์ดด้านบน หรือเมนูด้านบนของหน้า"),
    home_step(2, "เลือกข้อมูล", "เริ่มด้วยข้อมูลตัวอย่าง หรืออัปโหลด .csv / .xlsx หนึ่งแถวต่อหนึ่งแปลง มีคอลัมน์พันธุ์ สถานที่ บล็อก/ซ้ำ และลักษณะที่วัด"),
    home_step(3, "กด Run แล้วดูผล", "ตาราง กราฟ ตรวจสมมติฐาน และดาวน์โหลด Excel หรือรายงาน Word")),
  div(class = "alert alert-light border d-flex gap-2 align-items-start", icon("lock"),
      span("ข้อมูลของคุณถูกวิเคราะห์ในเบราว์เซอร์ของคุณเท่านั้น ไม่ได้ถูกส่งไปยังเซิร์ฟเวอร์ใด")))

source("methods.R", local = TRUE)         # methods_ui, about_ui

ui <- bslib::page_navbar(
  title = tags$span(icon("seedling"), app_name, tags$span(class = "badge bg-light text-danger", "beta")), window_title = app_name, id = "nav",
  theme = bslib::bs_theme(version = 5), fillable = FALSE, lang = "th",
  navbar_options = bslib::navbar_options(bg = brand[["main"]], theme = "dark"),
  header = tags$head(tags$script(HTML(copy_js)), tags$script(HTML(stale_js)), tags$style(HTML(app_css)), useBusyIndicators()),
  tabPanel("Home", home_ui),
  tabPanel("Single environment", sea_ui, value = "sea"),
  tabPanel("Multi-environment", met_ui, value = "met"),
  tabPanel("GxE & stability", gxe_ui, value = "gxe"),
  bslib::nav_spacer(),
  bslib::nav_menu("Info", align = "right", tabPanel("Methods", methods_ui), tabPanel("About", about_ui)))

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
  output$methods_versions <- renderUI(p(sprintf("%s %s, R %s.%s; lme4 %s, emmeans %s, multcomp %s, nlme %s, metan %s.", app_name, version_label(),
    R.version$major, R.version$minor, pkg_ver("lme4"), pkg_ver("emmeans"), pkg_ver("multcomp"), pkg_ver("nlme"), pkg_ver("metan"))))
}

shinyApp(ui, server)
