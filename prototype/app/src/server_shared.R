# Server parts used by every screen. Sourced inside server() (local = TRUE), so input, output and session are in scope.

run_safe <- function(expr) tryCatch(list(res = expr, err = NULL),
  error = function(e) list(res = NULL, err = sub("^Error in [^:]*: ?", "", conditionMessage(e))))

# Name of the data column chosen for a role (geno, env, block) at the last run of screen p, for the Thai summaries;
# falls back to a Thai word when the role was not chosen
col_lab <- function(p, role, default) {
  v <- rv[[paste0(p, "_set")]][[paste0(p, "_", role)]]
  if (length(v) == 1 && nzchar(v)) v else default
}

# Inputs of a screen that change its results; once they differ from the values of the last run, the results are greyed
# out (stale_js) and a note offers to run again
settings_of <- function(p, extra = character()) {
  ids <- c(grep(paste0("^", p, "_"), names(input), value = TRUE), extra)
  ids <- sort(ids[!grepl("_pw|_plot|_diag|_tabs|_file$|_dim$|_fmt$|_dpi$|_w$|_h$|_scale$|_err$", ids)])
  f <- input[[paste0(p, "_file")]]
  c(stats::setNames(lapply(ids, function(i) input[[i]]), ids), list(file = if (!is.null(f)) f$name))
}
track_stale <- function(p, rv_name, extra = character()) {
  observe({
    now <- settings_of(p, extra)
    then <- rv[[paste0(rv_name, "_set")]]
    session$sendCustomMessage("stale", list(id = paste0(p, "_out"), on = !is.null(then) && !identical(now, then)))
  })
}
msg_ui <- function(p, button) renderUI({
  err <- rv[[paste0(p, "_err")]]; res <- rv[[p]]
  if (!is.null(err)) error_box(err)
  else if (is.null(res)) run_hint(sprintf("\"%s\"", button))
  else if (!identical(settings_of(p, stale_extra[[p]] %||% character()), rv[[paste0(p, "_set")]])) stale_note(paste0("go_", p))
})
copy_btn <- function(id, text_fn, what) observeEvent(input[[id]], session$sendCustomMessage("copy_text", list(text = text_fn(), what = what)))
observeEvent(input$copy_status, showNotification(if (isTRUE(input$copy_status$ok)) "Copied: paste into Excel or Word" else "Copy failed", type = if (isTRUE(input$copy_status$ok)) "message" else "error", duration = 3))

# the diagnostic plots of one model (trait and environment chosen in a select input)
diag_server <- function(p, res_fn) {
  keys <- reactive({ d <- res_fn()$diag; req(d); unique(if (!is.null(d$Environment) && !all(is.na(d$Environment))) paste(d$Trait, "|", d$Environment) else d$Trait) })
  output[[paste0(p, "_diag_pick")]] <- renderUI(if (length(keys()) > 1) selectInput(paste0(p, "_diag_key"), "Trait / environment", keys()))
  plot_fn <- function() {
    d <- res_fn()$diag; req(d)
    k <- input[[paste0(p, "_diag_key")]]; if (is.null(k) || !k %in% keys()) k <- keys()[1]
    key <- if (!is.null(d$Environment) && !all(is.na(d$Environment))) paste(d$Trait, "|", d$Environment) else d$Trait
    pb_diag_plot(d[key == k, ], sprintf("Residuals of the model with genotype %s (%s).", if (isTRUE(res_fn()$random)) "random" else "fixed", k))
  }
  output[[paste0(p, "_diag")]] <- renderPlot(plot_fn())
  plot_fn
}

# Tukey / Dunnett comparisons of a screen
pw_server <- function(p, res_fn, geno_fn) {
  pw <- reactiveVal(NULL); pw_err <- reactiveVal(NULL)
  observe({ r <- res_fn(); g <- if (!is.null(r$means)) r$means$Genotype else r$geno$Genotype
    updateSelectizeInput(session, paste0(p, "_pw_control"), choices = sort(unique(g)), selected = sort(unique(g))[1]) })
  observeEvent(res_fn(), { pw(NULL); pw_err(NULL) })
  observeEvent(input[[paste0("go_", p, "_pw")]], {
    ty <- input[[paste0(p, "_pw_type")]]
    r <- run_safe(withProgress(message = "Comparing means", value = 0.5, if (ty %in% c("LSD", "DMRT"))
      pb_classic_compare(res_fn()$models, geno_fn(), ty, as.numeric(input[[paste0(p, "_pw_alpha")]])) else
      pb_pairwise(res_fn()$models, geno_fn(), input[[paste0(p, "_pw_type")]], as.numeric(input[[paste0(p, "_pw_alpha")]]), input[[paste0(p, "_pw_control")]], all = TRUE,
                  method = if (isTRUE(input[[paste0(p, "_pw_exact")]])) "multcomp" else "emmeans")))
    pw(r$res); pw_err(r$err)
  })
  output[[paste0(p, "_pw_out")]] <- renderUI({
    r <- res_fn()
    if (is.null(r)) return(run_hint("the Run button in the sidebar"))
    if (isTRUE(r$random)) return(note_box("The last analysis had genotype random. Mean comparisons need genotype fixed: choose Fixed in step \"Genotype effect\" and run again."))
    if (!is.null(pw_err())) return(error_box(pw_err()))
    x <- pw(); if (is.null(x)) return(p(class = "text-muted", "Choose the comparison and press Compare means."))
    if (isTRUE(x$classic)) return(tagList(
      result_card(sprintf("Letter groups (%s, alpha = %s)", if (x$type == "DMRT") "Duncan's multiple range test" else "Fisher's LSD", x$alpha), html_table(fmt_df(x$letters)),
        p(class = "text-muted small mb-0", "Genotypes sharing a letter do not differ significantly (agricolae::", if (x$type == "DMRT") "duncan.test" else "LSD.test", "), sorted by mean.")),
      result_card("Error term and critical values", html_table(fmt_df(x$stats)))))
    tab <- x$table; if (!isTRUE(input[[paste0(p, "_pw_all")]])) tab <- tab[tab$Significant, ]
    tab$Significant <- ifelse(tab$Significant, "*", "")
    tagList(
      if (length(x$warning)) note_box(paste(x$warning, collapse = "; ")),
      if (!is.null(x$letters)) result_card("Letter groups (Tukey)", html_table(fmt_df(x$letters)),
        p(class = "text-muted small mb-0", "Genotypes sharing a letter do not differ significantly at the chosen level (multcomp::cld). PBTools does not print letters.")),
      result_card(sprintf("%s comparisons, simultaneous %d%% confidence intervals", x$type, round(100 * (1 - x$alpha))),
        if (!nrow(tab)) p("No pair differs significantly.") else html_table(fmt_df(tab))))
  })
  pw
}

# ---- Live View plots (as RDstat) ----
# width and height in inches of the downloaded plot
export_dims <- function(p) {
  d <- input[[paste0(p, "_dim")]] %||% "std"
  cm <- suppressWarnings(as.numeric(if (d == "custom") c(input[[paste0(p, "_w")]], input[[paste0(p, "_h")]]) else export_sizes[[d]]))
  if (length(cm) != 2 || anyNA(cm)) cm <- export_sizes$std
  pmin(pmax(cm, 3), 60) / 2.54
}
# "Text and point size" (custom size only) enlarges everything by drawing on a smaller canvas of the same proportions
plot_scale <- function(p) if (identical(input[[paste0(p, "_dim")]], "custom")) as.numeric(input[[paste0(p, "_scale")]] %||% 1) else 1
save_plot <- function(g, file, wh, dpi = 300, scale = 1) {
  if (!inherits(g, "ggplot")) {
    w <- wh[1] / scale; h <- wh[2] / scale
    switch(tools::file_ext(file), svg = grDevices::svg(file, width = w, height = h), pdf = grDevices::pdf(file, width = w, height = h),
           grDevices::png(file, width = round(w * dpi * scale), height = round(h * dpi * scale), res = dpi * scale, bg = "white"))
    on.exit(grDevices::dev.off()); print(g); return(invisible(file))
  }
  dev <- switch(tools::file_ext(file), svg = function(filename, width, height, ...) grDevices::svg(filename, width = width, height = height), tools::file_ext(file))
  ggplot2::ggsave(file, g, device = dev, width = wh[1] / scale, height = wh[2] / scale, dpi = dpi * scale, units = "in", bg = "white")
}
# Live View (WYSIWYG): laid out at the downloaded size in inches, drawn at the resolution that fits the card on screen,
# so text and points take the same share of the figure as in the file.
# the plot with the theme, font size and black-and-white choice of its screen
styled_plot <- function(p, plot_fn) apply_plot_style(plot_fn(), input[[paste0(p, "_plot_theme")]] %||% "asis",
                                                     input[[paste0(p, "_plot_fsize")]] %||% 0, isTRUE(input[[paste0(p, "_plot_bw")]]))
render_live_plot <- function(p, plot_fn) renderImage({
  wh <- export_dims(p) / plot_scale(p)
  k <- min(680 / wh[1], 620 / wh[2]); px <- round(wh * k)
  pr <- session$clientData$pixelratio %||% 1
  g <- styled_plot(p, plot_fn); f <- tempfile(fileext = ".png")
  shiny::plotPNG(function() print(g), f, width = px[1] * pr, height = px[2] * pr, res = k * pr)
  list(src = f, width = px[1], height = px[2], contentType = "image/png", alt = "Plot")
}, deleteFile = TRUE)
live_server <- function(p, plot_fn) {
  output[[paste0(p, "_plot")]] <- render_live_plot(p, plot_fn)
  output[[paste0(p, "_live_badge")]] <- renderUI({
    wh <- export_dims(p) * 2.54
    span(class = "badge bg-light text-danger border px-2 py-1", title = "ขนาดและสัดส่วนที่จะได้เมื่อบันทึกภาพ",
         icon("ruler-combined", class = "me-1"), sprintf("%.1f × %.1f ซม. (%.2f:1)", wh[1], wh[2], wh[1] / wh[2]))
  })
  output[[paste0("dl_", p, "_plot")]] <- downloadHandler(
    function() paste0("rbtool_", p, "_plot.", input[[paste0(p, "_fmt")]] %||% "png"),
    function(file) save_plot(styled_plot(p, plot_fn), file, export_dims(p), as.numeric(input[[paste0(p, "_dpi")]] %||% 300), plot_scale(p)))
}

# template data files offered next to the upload boxes (the met link gives the file of means when that is chosen)
for (pg in names(template_file)) local({
  pg <- pg
  output[[paste0("tpl_", pg)]] <- downloadHandler(
    function() { f <- template_file[[pg]]; if (pg == "met" && identical(input$met_method, "two") && identical(input$met_s1, "file")) f <- "stage1_means.csv"; paste0("rbtool_template_", f) },
    function(file) { f <- template_file[[pg]]; if (pg == "met" && identical(input$met_method, "two") && identical(input$met_s1, "file")) f <- "stage1_means.csv"
      file.copy(file.path("examples", f), file) })
})
