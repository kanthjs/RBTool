# UI building blocks and formatting, following RDstat (github.com/kanthjs/RDstat) so both apps look and work alike.

`%||%` <- function(a, b) if (is.null(a)) b else a
help_box <- function(title, ...) tags$details(class = "help-box", tags$summary(title), div(class = "help-body", ...))

# version of this build (written by scripts/build_app.R); shown in the About tab and written to every result file
app_version <- local({
  v <- if (file.exists("VERSION")) readLines("VERSION") else character()
  get <- function(k) sub(paste0("^", k, "="), "", grep(paste0("^", k, "="), v, value = TRUE)[1])
  c(version = get("version"), commit = get("commit"), built = get("built"))
})
version_label <- function() { v <- app_version; if (anyNA(v)) "development build" else sprintf("v%s (%s, built %s)", v[["version"]], v[["commit"]], v[["built"]]) }

fmt_p <- function(p) ifelse(is.na(p), "", ifelse(p < 0.0001, "<0.0001", formatC(p, format = "f", digits = 4)))
fmt_n <- function(x, digits = 2) ifelse(is.na(x), "", formatC(x, format = "f", digits = digits, big.mark = ","))
sig_mark <- function(p) ifelse(is.na(p), "", ifelse(p < 0.01, "**", ifelse(p < 0.05, "*", "ns")))
# numbers for display: 4 significant digits for small values, 2 decimals otherwise; p-values by name
fmt_df <- function(d, p_cols = c("P", "P (genotype)", "p (b1 = 1)", "p (s2di = 0)", "Pr(>F)", "LRT p-value")) {
  if (is.null(d)) return(NULL)
  d <- as.data.frame(d, check.names = FALSE)
  for (j in names(d)) {
    x <- d[[j]]
    if (is.factor(x)) d[[j]] <- as.character(x)
    else if (is.logical(x)) d[[j]] <- ifelse(is.na(x), "", ifelse(x, "yes", "no"))
    else if (is.numeric(x)) d[[j]] <- if (j %in% p_cols) fmt_p(x) else fmt_num(x)
  }
  d
}
# whole numbers with thousands separators, |x| >= 100 with 2 decimals, the rest with 4 significant digits; NA as ""
# (vectorised: one formatC call per kind instead of one per cell)
fmt_num <- function(x) {
  out <- character(length(x)); ok <- !is.na(x)
  int <- ok & x == round(x) & abs(x) < 1e9; big <- ok & !int & abs(x) >= 100; rest <- ok & !int & !big
  out[int] <- formatC(x[int], format = "d", big.mark = ",")
  out[big] <- formatC(x[big], format = "f", digits = 2, big.mark = ",")
  out[rest] <- formatC(x[rest], format = "fg", digits = 4)
  out
}
tsv <- function(df) paste(c(paste(names(df), collapse = "\t"), apply(df, 1, paste, collapse = "\t")), collapse = "\n")

# numbers are right-aligned, text left-aligned
html_table <- function(df, max_rows = 400) {
  if (is.null(df) || !nrow(df)) return(p(class = "text-muted", "(no rows)"))
  more <- nrow(df) > max_rows; df <- utils::head(df, max_rows)
  cols <- lapply(df, as.character)
  num <- vapply(cols, function(v) { v <- v[nzchar(v)]
    length(v) > 0 && all(grepl("^[-<]?[0-9][0-9,.]*[*]*$|^[-]?[0-9.]+e[-+][0-9]+$", v)) }, NA, USE.NAMES = FALSE)
  al <- ifelse(num, "text-align:right;", "text-align:left;")
  # the cells of each row as one HTML string, the markup tags$td() writes, built column by column: building one tag
  # object per cell with df[i, j] took about 10 times as long
  td <- lapply(seq_along(cols), function(j)
    paste0("<td style=\"", al[j], "\"", if (num[j]) " class=\"num\"", ">", htmltools::htmlEscape(cols[[j]]), "</td>"))
  rows <- do.call(paste, c(td, sep = "\n"))
  div(class = "table-responsive",
    tags$table(class = "table table-condensed table-striped", style = "width:auto;",
      tags$thead(tags$tr(lapply(seq_along(df), function(j) tags$th(names(df)[j], style = al[j])))),
      tags$tbody(lapply(rows, function(r) tags$tr(HTML(r))))),
    if (more) p(class = "text-muted small", sprintf("Showing the first %d rows; the Excel file has all of them.", max_rows)))
}
# every row of the data, so the user can check nothing is missing; scrolls inside a box of fixed height
data_preview_ui <- function(d) {
  shown <- d
  shown[] <- lapply(shown, function(v) ifelse(is.na(v), "", as.character(v)))
  n_na <- sum(is.na(d))
  tagList(p(strong(formatC(nrow(d), format = "d", big.mark = ",")), "rows x", strong(ncol(d)), "columns.",
            if (n_na) sprintf("%s empty cells.", formatC(n_na, format = "d", big.mark = ","))),
          div(class = "data-scroll", html_table(shown, max_rows = Inf)))
}

run_hint <- function(button) div(class = "empty-state", icon("chart-simple"),
  h5("ยังไม่มีผล"), p(sprintf("เลือกข้อมูลและตัวแปร แล้วกด %s", button)),
  p(class = "small", "ทุกหน้าเริ่มด้วยข้อมูลตัวอย่าง กดปุ่มได้ทันทีเพื่อดูว่าจะได้ผลอะไร"))
side_step <- function(n, title, ...) div(class = "side-step", div(class = "side-step-title", span(class = "num", n), title), ...)
advanced_box <- function(..., title = " Advanced options") tags$details(class = "advanced", tags$summary(icon("sliders"), title), div(class = "advanced-body", ...))
run_bar <- function(id, label) div(class = "run-bar", actionButton(id, tagList(icon("play"), " ", label), class = "btn-primary w-100"))
result_card <- function(title, ..., copy = NULL, copy_label = "Copy") bslib::card(class = "result-card",
  bslib::card_header(class = "d-flex justify-content-between align-items-center", span(title),
                     if (!is.null(copy)) actionButton(copy, tagList(icon("copy"), " ", copy_label), class = "btn-sm")),
  bslib::card_body(...))
stale_dim <- function(tag) tagAppendAttributes(tag, class = "stale-dim")
stale_js <- "
Shiny.addCustomMessageHandler('stale', function(msg) {
  var el = document.getElementById(msg.id); if (!el) return;
  el.classList.toggle('is-stale', msg.on);
  el.querySelectorAll('.stale-dim').forEach(function(x) { if (msg.on) x.setAttribute('inert', ''); else x.removeAttribute('inert'); });
});"
copy_js <- "
Shiny.addCustomMessageHandler('copy_text', function(msg) {
  var done = function(ok) { Shiny.setInputValue('copy_status', {ok: ok, what: msg.what, t: Date.now()}); };
  var fallback = function() {
    var ta = document.createElement('textarea'); ta.value = msg.text;
    ta.style.position = 'fixed'; ta.style.opacity = '0'; document.body.appendChild(ta); ta.select();
    var ok = false; try { ok = document.execCommand('copy'); } catch (e) {}
    document.body.removeChild(ta); done(ok);
  };
  if (navigator.clipboard && window.isSecureContext) navigator.clipboard.writeText(msg.text).then(function() { done(true); }, fallback);
  else fallback();
});"
stale_note <- function(button) div(class = "alert alert-warning d-flex justify-content-between align-items-center gap-2",
  span(icon("triangle-exclamation"), " ", strong("การตั้งค่าเปลี่ยนไปหลังการวิเคราะห์ครั้งล่าสุด"), " ผลด้านล่างเป็นของการตั้งค่าเดิม จึงแสดงเป็นสีจาง"),
  tags$button(type = "button", class = "btn btn-warning btn-sm flex-none", onclick = sprintf("document.getElementById('%s').click()", button), icon("rotate-right"), " วิเคราะห์ใหม่"))
error_box <- function(msg) div(class = "alert alert-danger", icon("triangle-exclamation", class = "me-2"), strong("พบข้อผิดพลาด: "), msg)
note_box <- function(...) div(class = "alert alert-info small", icon("circle-info"), " ", ...)
home_card <- function(id, icon_name, title, text, examples) bslib::card(class = "home-card",
  bslib::card_body(div(class = "icon", icon(icon_name)), h4(title), p(text), p(class = "text-muted small mb-0", examples)),
  bslib::card_footer(class = "bg-white border-0", actionButton(id, tagList("เริ่มวิเคราะห์ ", icon("arrow-right")), class = "btn-primary w-100")))
home_step <- function(n, title, text) div(class = "step", div(class = "num", n), div(strong(title), br(), span(class = "text-muted small", text)))

# ---- Live View plots (as RDstat): the plot is shown at the proportions of the file that will be downloaded ----
export_sizes <- list(std = c(20, 14), sq = c(16, 16), j1 = c(8.5, 6.5), j2 = c(17.5, 12), slide = c(25, 14))
plot_live_ui <- function(p) div(class = "plot-live-wrapper",
  div(class = "plot-live-card",
    div(class = "plot-live-header",
      span(class = "plot-live-title", icon("eye", class = "me-1"), " Live View ", span(class = "text-muted small ms-1", "(สัดส่วนตามขนาดดาวน์โหลดจริง)")),
      uiOutput(paste0(p, "_live_badge"), inline = TRUE)),
    div(class = "plot-live-viewport", stale_dim(imageOutput(paste0(p, "_plot"), height = "auto")))))
plot_export_ui <- function(p) tagList(
  fluidRow(
    column(4, selectInput(paste0(p, "_plot_theme"), "Theme", c("As designed" = "asis", "Classic (axes, no grid)" = "classic", "Framed with light grid" = "bw", "Minimal (grid, no axes)" = "minimal"))),
    column(3, selectInput(paste0(p, "_plot_fsize"), "Font size (pt)", c("As designed" = 0, "9" = 9, "10" = 10, "11" = 11, "12" = 12, "14" = 14, "16" = 16, "18" = 18))),
    column(5, br(), checkboxInput(paste0(p, "_plot_bw"), "Black and white (greyscale, for print)", FALSE))),
  fluidRow(
    column(4, selectInput(paste0(p, "_dim"), "Size", c("Standard (20 x 14 cm)" = "std", "Square (16 x 16 cm)" = "sq", "Journal, 1 column (8.5 x 6.5 cm)" = "j1",
                                                      "Journal, 2 columns (17.5 x 12 cm)" = "j2", "Slide 16:9 (25 x 14 cm)" = "slide", "Custom" = "custom"))),
    column(2, selectInput(paste0(p, "_fmt"), "File type", c("PNG" = "png", "SVG" = "svg", "PDF" = "pdf"))),
    column(3, conditionalPanel(sprintf("input.%s_fmt == 'png'", p),
      selectInput(paste0(p, "_dpi"), "Resolution", c("Screen (120 dpi)" = 120, "Print (300 dpi)" = 300, "High (600 dpi)" = 600), selected = 300))),
    column(3, br(), stale_dim(downloadButton(paste0("dl_", p, "_plot"), "Download plot")))),
  conditionalPanel(sprintf("input.%s_dim == 'custom'", p), fluidRow(
    column(3, numericInput(paste0(p, "_w"), "Width (cm)", 20, min = 3, max = 60, step = 0.5)),
    column(3, numericInput(paste0(p, "_h"), "Height (cm)", 14, min = 3, max = 60, step = 0.5)),
    column(6, sliderInput(paste0(p, "_scale"), "Text and point size", min = 0.6, max = 1.6, value = 1, step = 0.1)))),
  p(class = "text-muted small mt-1 mb-0", "ขนาดและสัดส่วนที่เลือกแสดงใน Live View แบบเรียลไทม์ และเป็นขนาดของไฟล์ที่ดาวน์โหลด"))

# Radio buttons with a heading above each group of choices (one input). groups: list(heading = c(label = value, ...))
grouped_radio <- function(id, groups) {
  rb <- radioButtons(id, NULL, unlist(unname(groups)))
  g <- rb$children[[2]]; opts <- g$children[[1]]; out <- list(); i <- 0
  for (h in names(groups)) {
    n <- length(groups[[h]])
    out <- c(out, list(div(class = "radio-group-title", h)), opts[i + seq_len(n)]); i <- i + n
  }
  g$children[[1]] <- out; rb$children[[2]] <- g
  rb
}

# ---- publication-style plots ----
# Genotype means as points with error bars (SE or 95% CI), sorted, with the letter groups of the last mean comparison
# (Tukey, LSD or DMRT) beside them. m: Genotype, Mean, SE; letters: Genotype, Group; mult: CI multiplier (t or z).
means_dot_plot <- function(m, letters = NULL, xlab = "Mean", title = NULL, err = "se", mult = 1.96, method = NULL) {
  m <- as.data.frame(m); has_se <- !is.null(m$SE) && !all(is.na(m$SE))
  k <- if (identical(err, "ci")) mult else 1
  m$lo <- if (has_se) m$Mean - k * m$SE else m$Mean; m$hi <- if (has_se) m$Mean + k * m$SE else m$Mean
  m$Genotype <- factor(m$Genotype, levels = m$Genotype[order(m$Mean)])
  m$Group <- if (!is.null(letters)) letters$Group[match(as.character(m$Genotype), letters$Genotype)] else NA
  p <- ggplot2::ggplot(m, ggplot2::aes(Mean, Genotype))
  if (has_se) p <- p + ggplot2::geom_errorbar(ggplot2::aes(xmin = lo, xmax = hi), width = 0.3, orientation = "y", colour = "grey35")
  p <- p + ggplot2::geom_point(size = 2.4, colour = brand[["main"]])
  if (any(!is.na(m$Group))) p <- p + ggplot2::geom_text(ggplot2::aes(x = hi, label = Group), hjust = -0.35, size = 3.4, na.rm = TRUE)
  cap <- c(if (has_se) if (identical(err, "ci")) "Bars: 95% confidence interval." else "Bars: mean ± SE.",
           if (any(!is.na(m$Group))) sprintf("Genotypes sharing a letter do not differ significantly (%s).", method %||% "mean comparison")
           else "Run the Mean comparison tab to add letter groups.")
  p + ggplot2::scale_x_continuous(expand = ggplot2::expansion(mult = c(0.04, 0.10))) +
    ggplot2::labs(x = xlab, y = NULL, title = title, caption = paste(cap, collapse = " ")) +
    ggplot2::theme_classic(base_size = 12) + ggplot2::theme(plot.caption = ggplot2::element_text(hjust = 0, colour = "grey30"))
}
# Genotype x environment heatmap: genotypes (rows) and environments (columns) ordered by their means.
ge_heatmap <- function(ge, title = NULL, fill_lab = "Mean", values = NULL) {
  ge <- as.data.frame(ge); ge$Mean <- as.numeric(ge$Mean)
  gm <- tapply(ge$Mean, ge$Genotype, mean); em <- tapply(ge$Mean, ge$Environment, mean)
  ge$Genotype <- factor(ge$Genotype, levels = names(sort(gm))); ge$Environment <- factor(ge$Environment, levels = names(sort(em)))
  values <- values %||% (nrow(ge) <= 300)
  p <- ggplot2::ggplot(ge, ggplot2::aes(Environment, Genotype, fill = Mean)) + ggplot2::geom_tile(colour = "white", linewidth = 0.4) +
    ggplot2::scale_fill_viridis_c(name = fill_lab, option = "D")
  if (values) p <- p + ggplot2::geom_text(ggplot2::aes(label = formatC(Mean, format = "fg", digits = 3), colour = Mean > stats::median(Mean)), size = 2.6, show.legend = FALSE) +
    ggplot2::scale_colour_manual(values = c(`TRUE` = "black", `FALSE` = "white"))
  p + ggplot2::labs(x = "Environment (ordered by its mean)", y = "Genotype (ordered by its mean)", title = title) +
    ggplot2::theme_minimal(base_size = 12) + ggplot2::theme(panel.grid = ggplot2::element_blank(), axis.text.x = ggplot2::element_text(angle = 45, hjust = 1))
}
# Mean vs. slope b (Finlay-Wilkinson / Eberhart-Russell): four parts split by the grand mean and b = 1.
mean_slope_plot <- function(t, title = NULL, size_var = NULL, colour_var = NULL, ylab = "Regression coefficient (slope b)") {
  t <- as.data.frame(t); gm <- mean(t$Mean)
  p <- ggplot2::ggplot(t, ggplot2::aes(Mean, Slope)) +
    ggplot2::geom_hline(yintercept = 1, linetype = 2, colour = "grey45") + ggplot2::geom_vline(xintercept = gm, linetype = 2, colour = "grey45")
  p <- if (!is.null(colour_var) || !is.null(size_var)) {
    a <- ggplot2::aes(); if (!is.null(size_var)) a$size <- rlang::sym(size_var); if (!is.null(colour_var)) a$colour <- rlang::sym(colour_var)
    p + ggplot2::geom_point(a, alpha = 0.9)
  } else p + ggplot2::geom_point(size = 2.6, colour = brand[["main"]])
  if (!is.null(colour_var)) p <- p + ggplot2::scale_colour_manual(values = c(yes = brand[["main"]], no = "grey55"))
  q <- function(x, y, h, v, lab) ggplot2::annotate("text", x = x, y = y, hjust = h, vjust = v, label = lab, size = 3, colour = "grey30", fontface = "italic")
  p + ggrepel::geom_text_repel(ggplot2::aes(label = Genotype), size = 3.2, max.overlaps = 30, seed = 1) +
    q(Inf, Inf, 1.05, 1.5, "High mean, b > 1: favourable sites") + q(Inf, -Inf, 1.05, -0.8, "High mean, b < 1: wide / poorer sites") +
    q(-Inf, Inf, -0.05, 1.5, "Low mean, b > 1") + q(-Inf, -Inf, -0.05, -0.8, "Low mean, b < 1") +
    ggplot2::scale_y_continuous(expand = ggplot2::expansion(mult = 0.12)) +
    ggplot2::labs(x = sprintf("Genotype mean (grand mean %s)", formatC(gm, format = "fg", digits = 4)), y = ylab, title = title) +
    ggplot2::theme_classic(base_size = 12)
}

# multiplier of the 95% CI: t on the error df for the ANOVA (lm) models, z for the mixed models (asymptotic SE)
ci_mult <- function(mod) if (!is.null(mod) && inherits(mod, "lm")) stats::qt(0.975, stats::df.residual(mod)) else stats::qnorm(0.975)

# Variance components as shares of the total (stacked bars), one bar per trait (and environment).
varcomp_plot <- function(vc, title = "Variance components") {
  vc <- as.data.frame(vc); vc$Variance <- pmax(as.numeric(vc$Variance), 0)
  vc$Key <- if (!is.null(vc$Environment) && !all(is.na(vc$Environment))) paste(vc$Trait, "|", vc$Environment) else vc$Trait
  vc$Component <- gsub(":", " × ", vc$Group, fixed = TRUE)
  tot <- tapply(vc$Variance, vc$Key, sum); vc$Share <- vc$Variance / tot[vc$Key]
  ggplot2::ggplot(vc, ggplot2::aes(Share, Key, fill = Component)) + ggplot2::geom_col(width = 0.7, colour = "white", linewidth = 0.3) +
    ggplot2::scale_x_continuous(labels = function(x) paste0(round(100 * x), "%"), expand = c(0, 0)) +
    ggplot2::scale_fill_viridis_d(option = "D", end = 0.95) +
    ggplot2::labs(x = "Share of the total variance", y = NULL, fill = NULL, title = title) +
    ggplot2::theme_classic(base_size = 12) + ggplot2::theme(legend.position = "bottom")
}
# The style chosen under a plot (theme, font size, black and white), applied to any ggplot, ours or metan's.
apply_plot_style <- function(g, theme = "asis", size = 0, bw = FALSE) {
  # a patchwork takes a theme for all its panels with &; a GGally matrix takes + theme(); a recorded base plot nothing
  if (inherits(g, "patchwork") || inherits(g, "ggmatrix")) {
    add <- function(g, x) if (inherits(g, "patchwork")) g & x else g + x
    if (!identical(theme, "asis")) g <- add(g, switch(theme, classic = ggplot2::theme(panel.grid = ggplot2::element_blank()),
      bw = ggplot2::theme(panel.border = ggplot2::element_rect(fill = NA, colour = "black")), minimal = ggplot2::theme(panel.border = ggplot2::element_blank())))
    if (isTRUE(as.numeric(size) > 0)) g <- add(g, ggplot2::theme(text = ggplot2::element_text(size = as.numeric(size))))
    if (isTRUE(bw) && inherits(g, "patchwork")) g <- suppressMessages(g & ggplot2::scale_colour_grey(start = 0.1, end = 0.6) & ggplot2::scale_fill_grey(start = 0.3, end = 0.85))
    return(g)
  }
  if (!inherits(g, "ggplot")) return(g)
  if (!identical(theme, "asis")) g <- g + switch(theme,
    classic = ggplot2::theme(panel.background = ggplot2::element_rect(fill = "white", colour = NA), panel.grid = ggplot2::element_blank(),
                             panel.border = ggplot2::element_blank(), axis.line = ggplot2::element_line(colour = "black", linewidth = 0.4)),
    bw = ggplot2::theme(panel.background = ggplot2::element_rect(fill = "white", colour = NA), panel.border = ggplot2::element_rect(fill = NA, colour = "black", linewidth = 0.5),
                        panel.grid.major = ggplot2::element_line(colour = "grey90", linewidth = 0.3), panel.grid.minor = ggplot2::element_blank(), axis.line = ggplot2::element_blank()),
    minimal = ggplot2::theme(panel.background = ggplot2::element_blank(), panel.border = ggplot2::element_blank(), axis.line = ggplot2::element_blank(),
                             panel.grid.major = ggplot2::element_line(colour = "grey88", linewidth = 0.3), panel.grid.minor = ggplot2::element_blank()))
  if (isTRUE(as.numeric(size) > 0)) g <- g + ggplot2::theme(text = ggplot2::element_text(size = as.numeric(size)))
  if (isTRUE(bw)) {
    grey_of <- function(x) if (is.character(x) && length(x) == 1 && !x %in% c("black", "white", NA) && !grepl("^gr[ae]y", x)) "grey15" else x
    for (i in seq_along(g$layers)) { pr <- g$layers[[i]]$aes_params
      if (!is.null(pr$colour)) g$layers[[i]]$aes_params$colour <- grey_of(pr$colour)
      if (!is.null(pr$fill)) g$layers[[i]]$aes_params$fill <- if (identical(grey_of(pr$fill), "grey15")) "grey55" else pr$fill }
    sc <- function(a) { s <- g$scales$get_scales(a); if (is.null(s)) NA else if (inherits(s, "ScaleContinuous")) "c" else "d" }
    g <- suppressMessages(g +
      switch(sc("fill"), c = ggplot2::scale_fill_gradient(low = "grey95", high = "grey10"), d = ggplot2::scale_fill_grey(start = 0.25, end = 0.85), NULL) +
      switch(sc("colour"), c = ggplot2::scale_colour_gradient(low = "grey70", high = "black"), d = ggplot2::scale_colour_grey(start = 0.1, end = 0.65), NULL)) +
      ggplot2::theme(axis.text = ggplot2::element_text(colour = "black"))
  }
  g
}

# a base-graphics plot recorded so that the Live View and the downloads can draw it like a ggplot
record_base_plot <- function(draw) {
  grDevices::pdf(NULL, width = 8, height = 6); grDevices::dev.control("enable")
  on.exit(grDevices::dev.off())
  draw(); grDevices::recordPlot()
}
# scree plot of the AMMI terms from metan's AMMI table: share of the GxE sum of squares and cumulative share
ammi_scree_plot <- function(anova) {
  pc <- anova[grepl("^PC", anova$Source), ]; pc$Term <- factor(pc$Source, levels = pc$Source)
  ggplot2::ggplot(pc, ggplot2::aes(Term)) + ggplot2::geom_col(ggplot2::aes(y = `% of GxE`), fill = brand[["main"]], width = 0.65) +
    ggplot2::geom_line(ggplot2::aes(y = `Cumulative %`, group = 1), colour = "grey25") + ggplot2::geom_point(ggplot2::aes(y = `Cumulative %`), colour = "grey25") +
    ggplot2::geom_text(ggplot2::aes(y = `% of GxE`, label = sprintf("%.1f", `% of GxE`)), vjust = -0.5, size = 3.2) +
    ggplot2::labs(x = "Multiplicative term", y = "% of the GxE sum of squares (bars) and cumulative % (line)", title = "AMMI: share of the interaction by term") +
    ggplot2::theme_classic(base_size = 12)
}

# ---- one look for the metan plots ----
# Okabe-Ito colours (colour-blind safe, distinct in greyscale): genotypes blue, environments vermillion
pub_col <- c(gen = "#0072B2", env = "#D55E00", sel = "#D55E00", nonsel = "grey70", line = "grey35")
# theme handed to metan (its plot_theme argument): white panel, light grid, black axis text, legend at the bottom
theme_rbtool <- function(base_size = 12) ggplot2::theme_bw(base_size = base_size) + ggplot2::theme(
  panel.grid.minor = ggplot2::element_blank(), panel.grid.major = ggplot2::element_line(colour = "grey92", linewidth = 0.3),
  panel.border = ggplot2::element_rect(colour = "grey30", fill = NA, linewidth = 0.5), axis.text = ggplot2::element_text(colour = "black"),
  axis.ticks = ggplot2::element_line(colour = "grey30"), plot.title = ggplot2::element_text(face = "bold", size = ggplot2::rel(1.05)),
  legend.position = "bottom", legend.key = ggplot2::element_blank(), strip.background = ggplot2::element_rect(fill = "grey95", colour = "grey30"))
# metan biplots: readable legend (Environment / Genotype, no "type" title)
metan_tidy <- function(g) if (inherits(g, "ggplot") && !inherits(g, "patchwork")) g + ggplot2::labs(colour = NULL, fill = NULL, shape = NULL) else g
