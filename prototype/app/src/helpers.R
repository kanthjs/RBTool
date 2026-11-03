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
fmt_df <- function(d, p_cols = c("P", "p (b1 = 1)", "p (s2di = 0)", "Pr(>F)", "LRT p-value")) {
  if (is.null(d)) return(NULL)
  d <- as.data.frame(d, check.names = FALSE)
  for (j in names(d)) {
    x <- d[[j]]
    if (is.factor(x)) d[[j]] <- as.character(x)
    else if (is.logical(x)) d[[j]] <- ifelse(is.na(x), "", ifelse(x, "yes", "no"))
    else if (is.numeric(x)) d[[j]] <- if (j %in% p_cols) fmt_p(x) else vapply(x, function(v) {
      if (is.na(v)) "" else if (v == round(v) && abs(v) < 1e9) formatC(v, format = "d", big.mark = ",")
      else if (abs(v) >= 100) formatC(v, format = "f", digits = 2, big.mark = ",")
      else formatC(v, format = "fg", digits = 4) }, "")
  }
  d
}
tsv <- function(df) paste(c(paste(names(df), collapse = "\t"), apply(df, 1, paste, collapse = "\t")), collapse = "\n")

# numbers are right-aligned, text left-aligned
html_table <- function(df, max_rows = 400) {
  if (is.null(df) || !nrow(df)) return(p(class = "text-muted", "(no rows)"))
  more <- nrow(df) > max_rows; df <- utils::head(df, max_rows)
  num <- vapply(seq_along(df), function(j) {
    v <- as.character(df[[j]]); v <- v[nzchar(v)]
    length(v) > 0 && all(grepl("^[-<]?[0-9][0-9,.]*[*]*$|^[-]?[0-9.]+e[-+][0-9]+$", v))
  }, NA)
  al <- ifelse(num, "text-align:right;", "text-align:left;")
  div(class = "table-responsive",
    tags$table(class = "table table-condensed table-striped", style = "width:auto;",
      tags$thead(tags$tr(lapply(seq_along(df), function(j) tags$th(names(df)[j], style = al[j])))),
      tags$tbody(lapply(seq_len(nrow(df)), function(i)
        tags$tr(lapply(seq_along(df), function(j) tags$td(as.character(df[i, j]), style = al[j], class = if (num[j]) "num")))))),
    if (more) p(class = "text-muted small", sprintf("Showing the first %d rows; the Excel file has all of them.", max_rows)))
}
data_preview_ui <- function(d, n = 10) {
  shown <- utils::head(d, n)
  shown[] <- lapply(shown, function(v) ifelse(is.na(v), "", as.character(v)))
  tagList(p(strong(formatC(nrow(d), format = "d", big.mark = ",")), "rows x", strong(ncol(d)), "columns.",
            if (nrow(d) > n) sprintf("Showing the first %d rows.", n)),
          html_table(shown))
}

run_hint <- function(button) div(class = "empty-state", icon("chart-simple"),
  h5("ยังไม่มีผล"), p(sprintf("เลือกข้อมูลและตัวแปร แล้วกด %s", button)),
  p(class = "small", "ทุกหน้าเริ่มด้วยข้อมูลตัวอย่าง กดปุ่มได้ทันทีเพื่อดูว่าจะได้ผลอะไร"))
side_step <- function(n, title, ...) div(class = "side-step", div(class = "side-step-title", span(class = "num", n), title), ...)
advanced_box <- function(..., title = " Advanced options") tags$details(class = "advanced", tags$summary(icon("sliders"), title), div(class = "advanced-body", ...))
run_bar <- function(id, label) div(class = "run-bar", actionButton(id, tagList(icon("play"), " ", label), class = "btn-primary w-100"))
key_stats <- function(...) div(class = "key-stats", lapply(Filter(Negate(is.null), list(...)), function(s)
  div(class = "key-stat", div(class = "label", s$label), div(class = "value", s$value), if (!is.null(s$note)) div(class = "note", s$note))))
stat <- function(label, value, note = NULL) list(label = label, value = value, note = note)
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
