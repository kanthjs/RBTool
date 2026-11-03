# Server parts used by every screen. Sourced inside server() (local = TRUE), so input, output and session are in scope.

run_safe <- function(expr) tryCatch(list(res = expr, err = NULL),
  error = function(e) list(res = NULL, err = sub("^Error in [^:]*: ?", "", conditionMessage(e))))

# Inputs of a screen that change its results; once they differ from the values of the last run, the results are greyed
# out (stale_js) and a note offers to run again
settings_of <- function(p, extra = character()) {
  ids <- c(grep(paste0("^", p, "_"), names(input), value = TRUE), extra)
  ids <- sort(ids[!grepl("_pw|_plot|_diag|_tabs|_file$", ids)])
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
    r <- run_safe(withProgress(message = "Comparing means", value = 0.5,
      pb_pairwise(res_fn()$models, geno_fn(), input[[paste0(p, "_pw_type")]], as.numeric(input[[paste0(p, "_pw_alpha")]]), input[[paste0(p, "_pw_control")]], all = TRUE)))
    pw(r$res); pw_err(r$err)
  })
  output[[paste0(p, "_pw_out")]] <- renderUI({
    r <- res_fn()
    if (is.null(r)) return(run_hint("the Run button in the sidebar"))
    if (isTRUE(r$random)) return(note_box("The last analysis had genotype random. Mean comparisons need genotype fixed: choose Fixed in step \"Genotype effect\" and run again."))
    if (!is.null(pw_err())) return(error_box(pw_err()))
    x <- pw(); if (is.null(x)) return(p(class = "text-muted", "Choose the comparison and press Compare means."))
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
