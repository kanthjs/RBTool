# Example data, the data step of each screen and the variable pickers.

# `pages`: screens the example is offered on; roles preset the variable pickers
examples <- list(
  rcb11 = list(label = "Rice yield: 15 genotypes x 11 sites, RCB (PBTools)", file = "rcb_11sites.csv", pages = c("sea", "met", "gxe"),
               design = "RCB", trait = "Yield", geno = "Genotype", env = "Env", block = "Block", rep = "Block"),
  rcb2 = list(label = "8 genotypes x 2 sites, RCB, two traits (PBTools)", file = "rcb_2sites.csv", pages = c("sea", "met"),
              design = "RCB", trait = c("Y1", "Y2"), geno = "Gen", env = "Site", block = "Blk"),
  rowcol = list(label = "Row-column: 25 genotypes, 2 reps, 2 sites (simulated)", file = "rowcol_2env.csv", pages = character(0), # not offered: RCBD only for now
                design = "RowCol", trait = "Y", geno = "Geno", env = "Env", block = "Row", column = "Col", rep = "Rep"),
  maize = list(label = "Maize: 13 hybrids x 4 environments, 3 reps, 15 traits (metan data_ge2)", file = "maize_ge2.csv", pages = "gxe",
               trait = c("KW", "ED", "CD", "NKR", "PH"), geno = "GEN", env = "ENV", rep = "REP", lower = "PH"),
  stage1 = list(label = "Stage-1 means: 15 genotypes x 10 sites (PBTools)", file = "stage1_means.csv", pages = "met",
                trait = "Yield_Mean", geno = "Genotype", env = "Env", se = "Yield_StdErrMean", error_var = "Yield_sigma2", reps = "Yield_No.rep"))
examples_for <- function(page) { ex <- Filter(function(e) page %in% e$pages, examples); stats::setNames(names(ex), vapply(ex, `[[`, "", "label")) }

file_input <- function(p) fileInput(paste0(p, "_file"), "Data file (.csv, .txt, .xlsx, .xls)", accept = c(".csv", ".txt", ".tsv", ".xlsx", ".xls"))
data_step <- function(p, page, n, selected = NULL) side_step(n, "Data",
  radioButtons(paste0(p, "_src"), NULL, c("Built-in example" = "ex", "Upload file" = "up"), inline = TRUE),
  conditionalPanel(sprintf("input.%s_src == 'ex'", p), selectInput(paste0(p, "_example"), NULL, examples_for(page), selected = selected)),
  conditionalPanel(sprintf("input.%s_src == 'up'", p), file_input(p), layout_help(page)))

# ---- the layout of the file to upload, shown next to the upload box ----
mini_table <- function(head, rows) tags$table(class = "layout-table",
  tags$thead(tags$tr(lapply(head, tags$th))), tags$tbody(lapply(rows, function(r) tags$tr(lapply(r, tags$td)))))
plot_layout <- mini_table(c("Env", "Rep", "Genotype", "Yield", "Height"), list(
  c("E1", "1", "G1", "3285.7", "95"), c("E1", "1", "G2", "3012.4", "101"), c("...", "...", "...", "...", "..."),
  c("E1", "3", "G15", "2870.0", "98"), c("E2", "1", "G1", "4120.5", "."), c("...", "...", "...", "...", "...")))
means_layout <- mini_table(c("Env", "Genotype", "Yield_Mean", "Yield_SE", "Yield_sigma2", "Yield_No.rep"), list(
  c("E1", "G1", "2999.97", "361.86", "392838.8", "3"), c("E1", "G2", "2190.43", "361.86", "392838.8", "3"), c("...", "...", "...", "...", "...", "...")))
layout_rules <- function(...) tags$ul(class = "layout-rules",
  tags$li("แถวแรกเป็นชื่อคอลัมน์ ชื่อใดก็ได้ (เลือกคอลัมน์ให้ตรงบทบาทในขั้น Variables)"),
  ..., tags$li("ค่าที่หายไป: เว้นว่าง หรือใส่ NA หรือ ."),
  tags$li(".csv (คั่นด้วยจุลภาค), .txt (คั่นด้วย tab), .xlsx หรือ .xls: อ่านชีตแรกของไฟล์ Excel"))
long_rule <- tags$li(strong("หนึ่งแถว = หนึ่งแปลง"), " (รูปแบบยาว): ซ้ำ/บล็อกเป็นแถว ไม่แยกเป็นคอลัมน์ Rep1 | Rep2 | Rep3")
layout_help <- function(page) {
  plots <- tagList(p(class = "mb-1", "ตัวอย่างโครงสร้างข้อมูล (ข้อมูลรายแปลง):"), plot_layout,
    layout_rules(long_rule,
      switch(page,
        sea = tags$li("ต้องมี: พันธุ์, บล็อก (ซ้ำ), ลักษณะที่วัดอย่างน้อย 1 คอลัมน์; คอลัมน์สถานที่ (Env) ใส่เมื่อมีหลายสถานที่ จะวิเคราะห์แยกทีละสถานที่"),
        met = tags$li("ต้องมี: สถานที่ (Env), บล็อก (ซ้ำ) ภายในสถานที่, พันธุ์ และลักษณะที่วัด; แต่ละสถานที่เป็น RCBD"),
        gxe = tagList(tags$li("ต้องมี: สถานที่ (Env), ซ้ำ (Rep), พันธุ์ และลักษณะที่วัด 1 คอลัมน์"),
                      tags$li("MTSI / MGIDI: ลักษณะตั้งแต่ 2 ลักษณะ คอลัมน์ละลักษณะ"),
                      tags$li(strong("ข้อมูลต้องครบ:"), " ทุกพันธุ์ปลูกในทุกสถานที่ และมีซ้ำ")))))
  body <- if (page == "met") tagList(
    conditionalPanel("!(input.met_method == 'two' && input.met_s1 == 'file')", plots),
    conditionalPanel("input.met_method == 'two' && input.met_s1 == 'file'",
      p(class = "mb-1", "ตัวอย่างโครงสร้างข้อมูล (ค่าเฉลี่ยของแต่ละสถานที่ จากการวิเคราะห์ขั้นที่ 1):"), means_layout,
      layout_rules(tags$li("หนึ่งแถว = หนึ่งพันธุ์ในหนึ่งสถานที่: ค่าเฉลี่ย และ SE ของค่าเฉลี่ย"),
                   tags$li("error variance (sigma2) และจำนวนซ้ำ ใช้เมื่อเลือกถ่วงน้ำหนัก; ไม่มีก็ได้")))) else plots
  tags$details(class = "help-box layout-help", open = NA, tags$summary(icon("table"), " โครงสร้างไฟล์ข้อมูล"),
    div(class = "help-body", body, downloadLink(paste0("tpl_", page), tagList(icon("download"), " ดาวน์โหลดไฟล์ตัวอย่าง (.csv) ไปใช้เป็นแม่แบบ"))))
}
# the file a template link downloads
template_file <- c(sea = "rcb_2sites.csv", met = "rcb_11sites.csv", gxe = "rcb_11sites.csv")

# reactive data of one screen: list(d = data frame, name, ex = example roles or NULL)
data_server <- function(input, p) reactive({
  if (identical(input[[paste0(p, "_src")]], "up")) {
    f <- input[[paste0(p, "_file")]]; req(f)
    d <- tryCatch(pb_read_table(f$datapath, f$name), error = function(e) NULL)
    validate(need(!is.null(d) && ncol(d) >= 2, "The file could not be read. Save it as .csv, .xlsx or .xls with the column names in the first row."))
    list(d = d, name = f$name, ex = NULL)
  } else {
    k <- input[[paste0(p, "_example")]] %||% names(examples)[1]; e <- examples[[k]]
    list(d = utils::read.csv(file.path("examples", e$file), check.names = FALSE), name = e$label, ex = e)
  }
})

guess_col <- function(cols, rx, avoid = character(), default = "") {
  m <- setdiff(cols[grepl(rx, cols, ignore.case = TRUE)], avoid)
  if (length(m)) m[1] else default
}
numeric_like <- function(d) names(d)[vapply(d, function(v) { x <- suppressWarnings(as.numeric(as.character(v))); mean(!is.na(x)) > 0.8 }, NA)]

# a select input for one role, preset from the example or guessed from the column names
role_select <- function(id, label, cols, preset, rx, avoid = character(), optional = FALSE, multiple = FALSE) {
  sel <- if (!is.null(preset)) preset else guess_col(cols, rx, avoid, if (optional) "" else cols[1])
  ch <- if (optional) c("(none)" = "", cols) else cols
  selectInput(id, label, ch, selected = sel, multiple = multiple)
}
