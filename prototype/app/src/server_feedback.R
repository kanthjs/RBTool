# Feedback form and diagnostic report (as RDstat). Sourced inside server() (local = TRUE).
# The report holds the app version, the settings of the active screen and the last error; never the data values.

diagnostic_info <- reactive({
  nav <- input$nav %||% "Home"
  p <- if (nav %in% c("sea", "met", "gxe")) nav else NULL
  screen <- if (is.null(p)) paste0("- Active tab: ", nav) else {
    err <- rv[[paste0(p, "_err")]]
    st <- settings_of(p, stale_extra[[p]] %||% character())
    st <- vapply(names(st), function(k) {
      v <- st[[k]]
      paste0("- ", k, ": ", if (length(v)) paste(utils::head(as.character(v), 12), collapse = ", ") else "-")
    }, character(1))
    paste(c(paste0("- Screen: ", c(sea = "Single environment", met = "Multi-environment", gxe = "GxE & stability")[[p]]),
            st,
            paste0("- Analysis run: ", if (is.null(rv[[p]])) "no" else "yes"),
            paste0("- Error message: ", if (is.null(err)) "None" else err)), collapse = "\n")
  }
  paste(
    sprintf("=== %s Diagnostic Report ===", app_name),
    paste0("App Version: ", version_label()),
    paste0("R Version: ", R.version$major, ".", R.version$minor),
    paste0("System: ", paste(Sys.info()[c("sysname", "release")], collapse = " ")),
    paste0("Timestamp: ", format(Sys.time(), "%Y-%m-%d %H:%M:%S")),
    "--- Parameters & Status ---",
    screen,
    "================================",
    sep = "\n"
  )
})

show_feedback_modal <- function() {
  showModal(modalDialog(
    title = tagList(icon("comment-dots", class = "me-2"), sprintf("ส่งข้อเสนอแนะและแจ้งปัญหา %s", app_name)),
    size = "l", easyClose = TRUE,
    footer = tagList(
      actionButton("cp_diag_modal", tagList(icon("copy"), " คัดลอกข้อมูลวินิจฉัย"), class = "btn btn-outline-secondary"),
      modalButton("ปิดหน้าต่าง")
    ),
    div(
      p(sprintf("ยินดีรับฟังความคิดเห็น ปัญหาการใช้งาน หรือข้อเสนอแนะฟังก์ชันเพิ่มเติม เพื่อพัฒนา %s ให้ตอบโจทย์นักปรับปรุงพันธุ์", app_name)),
      div(class = "d-grid gap-2 mb-4",
        tags$a(href = feedback_form_url, target = "_blank", rel = "noopener noreferrer",
               class = "btn btn-primary btn-lg fw-bold shadow-sm d-flex align-items-center justify-content-center gap-2",
               icon("arrow-up-right-from-square"), " เปิดแบบฟอร์มเพื่อส่งข้อเสนอแนะ")),
      tags$hr(),
      tags$h6(class = "fw-bold text-secondary mb-2", icon("stethoscope", class = "me-1"), " ข้อมูลวินิจฉัยระบบ (System Diagnostics)"),
      p(class = "small text-muted mb-2", "หากพบปัญหา กดปุ่ม ", strong("คัดลอกข้อมูลวินิจฉัย"),
        " แล้ววางในแบบฟอร์ม เพื่อให้ตรวจสอบสาเหตุได้เร็วขึ้น:"),
      tags$textarea(id = "diag_text_box", class = "form-control font-monospace small bg-light mb-2", rows = 9, readonly = "readonly",
                    style = "font-size: 12px; resize: vertical;", diagnostic_info()),
      p(class = "small text-muted", "หมายเหตุ: ข้อมูลนี้มีเพียงชื่อคอลัมน์ ค่าตั้งต้นของการวิเคราะห์ และข้อความแจ้งข้อผิดพลาด ",
        strong("ไม่มีการส่งค่าข้อมูลดิบของคุณออกไปภายนอก"))
    )
  ))
}

observeEvent(input$open_feedback, show_feedback_modal())
observeEvent(input$btn_about_feedback, show_feedback_modal())
observeEvent(input$cp_diag_modal, session$sendCustomMessage("copy_text", list(what = "ข้อมูลวินิจฉัย", text = diagnostic_info())))
observeEvent(input$cp_diagnostic, session$sendCustomMessage("copy_text", list(what = "ข้อมูลวินิจฉัย", text = diagnostic_info())))
