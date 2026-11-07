# Single-environment screen (server). Sourced inside server().

sea_dat <- data_server(input, "sea")

row_label <- function(design) if (design %in% c("RowCol", "AugLS", "LatinRowCol")) "Row" else if (design %in% c("RCB", "AugRCB")) "Block (replicate)" else "Block (incomplete block)"
raw_vars_ui <- function(p, dat, design, multi_trait = TRUE, need_env = FALSE) {
  d <- dat$d; cl <- names(d); e <- dat$ex; num <- numeric_like(d)
  tagList(
    role_select(paste0(p, "_trait"), if (multi_trait) "Trait(s) to analyse" else "Trait", num, e$trait, "yield|^y$|weight|height", multiple = multi_trait),
    role_select(paste0(p, "_geno"), "Genotype (entry, variety, line)", cl, e$geno, "gen|entry|line|variet|cultivar|clone"),
    role_select(paste0(p, "_env"), if (need_env) "Environment (site, season, year)" else "Environment (site), if the file has several", cl,
                if (!is.null(e)) e$env %||% "" else NULL, "^env|site|loc|trial|season|year", optional = !need_env),
    role_select(paste0(p, "_block"), row_label(design), cl, e$block, if (design %in% c("RowCol", "AugLS", "LatinRowCol")) "^row" else "block|blk|^rep"),
    if (pb_needs_column(design)) role_select(paste0(p, "_col"), "Column", cl, e$column, "^col"),
    if (pb_needs_rep(design)) role_select(paste0(p, "_rep"), "Replicate", cl, e$rep, "^rep"))
}
output$sea_vars <- renderUI(raw_vars_ui("sea", sea_dat(), "RCB"))

observeEvent(input$go_sea, {
  if (!length(input$sea_trait) || !nzchar(input$sea_geno %||% "") || !nzchar(input$sea_block %||% "")) {
    rv$sea <- NULL; rv$sea_err <- "The variables are not chosen yet (step 4). Wait for the lists to appear, then press Run again."; return() }
  d <- sea_dat()$d
  anova <- identical(input$sea_method, "anova")
  r <- run_safe(withProgress(message = if (anova) "Computing the analysis of variance" else "Fitting the mixed models", value = 0.4,
    if (anova) pb_sea_anova(d, input$sea_trait, input$sea_geno, input$sea_block, env = input$sea_env)
    else pb_sea(d, input$sea_trait, input$sea_geno, "RCB", input$sea_block, env = input$sea_env, random = input$sea_random == "random")))
  rv$sea <- r$res; rv$sea_err <- r$err; rv$sea_set <- settings_of("sea"); rv$sea_name <- sea_dat()$name
})
output$sea_msg <- msg_ui("sea", "Run single-environment analysis")
track_stale("sea", "sea")

sea_thai <- function(r) {
  s <- r$summary; n_env <- if (is.null(s$Environment)) 1 else length(unique(s$Environment))
  if (identical(r$method, "anova")) {
    m <- r$means; best <- m[which.max(m$Mean), ]; sig <- sum(s$`P (genotype)` < 0.05, na.rm = TRUE)
    return(sprintf("วิเคราะห์ %s ด้วยการวิเคราะห์ความแปรปรวนแบบมาตรฐาน (RCBD, บล็อกและพันธุ์เป็น fixed effect) พันธุ์แตกต่างกันอย่างมีนัยสำคัญ (F test, p < 0.05) %d จาก %d %s; ค่าสัมประสิทธิ์ความแปรผัน (CV) อยู่ระหว่าง %.1f%% ถึง %.1f%%%s ค่าเฉลี่ยสูงสุดคือ %s (%s%s) ใช้แท็บ Mean comparison เพื่อเปรียบเทียบพันธุ์",
            paste(unique(s$Trait), collapse = ", "), sig, nrow(s), if (n_env > 1) "สถานที่/ลักษณะ" else "ลักษณะ", min(s$`CV (%)`), max(s$`CV (%)`),
            if (max(s$`CV (%)`) > 20) " (CV เกิน 20% ความแม่นยำของการทดลองค่อนข้างต่ำ)" else "",
            best$Genotype, fmt_n(best$Mean), if (!is.null(best$Environment)) paste0(", ", best$Environment) else ""))
  }
  if (isTRUE(r$random)) {
    sig <- sum(s$`LRT p-value` < 0.05, na.rm = TRUE)
    sprintf("วิเคราะห์ %s ด้วย linear mixed model แบบแผน %s โดยให้พันธุ์เป็น random effect ความแปรปรวนทางพันธุกรรมมีนัยสำคัญ (likelihood ratio test, p < 0.05) %d จาก %d %s ค่าอัตราพันธุกรรม (heritability) อยู่ระหว่าง %.2f ถึง %.2f",
            paste(unique(s$Trait), collapse = ", "), pb_designs[[r$design]], sig, nrow(s), if (n_env > 1) "สถานที่/ลักษณะ" else "ลักษณะ", min(s$Heritability), max(s$Heritability))
  } else {
    m <- r$means; best <- m[which.max(m$Mean), ]
    sprintf("วิเคราะห์ %s ด้วย linear mixed model แบบแผน %s โดยให้พันธุ์เป็น fixed effect ได้ค่าเฉลี่ยปรับแล้ว (BLUE) ของ %d พันธุ์ ค่าเฉลี่ยสูงสุดคือ %s (%s%s) ค่า SED เฉลี่ย %s ใช้แท็บ Mean comparison เพื่อเปรียบเทียบพันธุ์",
            paste(unique(s$Trait), collapse = ", "), pb_designs[[r$design]], length(unique(m$Genotype)), best$Genotype, fmt_n(best$Mean),
            if (!is.null(best$Environment)) paste0(", ", best$Environment) else "", fmt_n(mean(s$`Average SED`)))
  }
}
output$sea_results <- renderUI({
  r <- rv$sea; req(r)
  s <- r$summary
  tagList(
    key_stats(stat("Traits", length(unique(s$Trait))), stat("Environments", if (is.null(s$Environment)) 1 else length(unique(s$Environment))),
              stat("Genotypes", length(unique(r$means$Genotype))),
              if (isTRUE(r$random)) stat("Heritability", sprintf("%.2f", stats::median(s$Heritability)), if (nrow(s) > 1) sprintf("median; range %.2f to %.2f", min(s$Heritability), max(s$Heritability)))
              else if (identical(r$method, "anova")) stat("CV (%)", sprintf("%.1f", stats::median(s$`CV (%)`)), if (nrow(s) > 1) sprintf("median; range %.1f to %.1f", min(s$`CV (%)`), max(s$`CV (%)`)) else "coefficient of variation")
              else stat("Average SED", fmt_n(mean(s$`Average SED`)), "standard error of a difference")),
    result_card("สรุปผล (ภาษาไทย)", p(lang = "th", sea_thai(r)), p(lang = "th", class = "text-muted small mb-0", "สรุปนี้สร้างจากโปรแกรม ควรอ่านร่วมกับบริบทของการทดลอง"), copy = "cp_sea_th", copy_label = "คัดลอก"),
    result_card("Descriptive statistics", html_table(fmt_df(r$descriptive)), copy = "cp_sea_desc",
      p(class = "text-muted small mb-0", "Raw data of each trait, by environment, as PBTools (DescriptiveStatistics).")),
    result_card("Summary by environment", html_table(fmt_df(s)), copy = "cp_sea_sum",
      if (identical(r$method, "anova")) p(class = "text-muted small mb-0", "CV = sqrt(error mean square) / grand mean x 100. LSD (5%) = t(0.975, error df) x average SED; with missing plots the SED differs between pairs.")),
    if (!is.null(r$anova)) result_card("Analysis of variance", html_table(fmt_df(r$anova)), copy = "cp_sea_aov",
      p(class = "text-muted small mb-0", "RCBD, blocks and genotypes fixed (stats::lm, sequential sums of squares: block first).")),
    result_card(if (isTRUE(r$random)) "Predicted genotype values (BLUPs)" else if (identical(r$method, "anova")) "Genotype means and SE" else "Adjusted genotype means (BLUEs) and SE", html_table(fmt_df(r$means)), copy = "cp_sea_means"),
    if (!is.null(r$varcomp)) result_card("Variance components", html_table(fmt_df(r$varcomp)), copy = "cp_sea_vc"),
    if (!is.null(r$lrt)) result_card("Likelihood ratio test of the genotype variance", html_table(fmt_df(r$lrt)),
      p(class = "text-muted small mb-0", "REML fits with and without the genotype term, as PBTools (lme4 anova, refit = FALSE).")),
    result_card("Model", tags$pre(class = "small mb-0", paste(names(r$formulas), r$formulas, sep = ":  ", collapse = "\n"))))
})
copy_btn("cp_sea_th", function() sea_thai(rv$sea), "summary")
copy_btn("cp_sea_desc", function() tsv(fmt_df(rv$sea$descriptive)), "descriptive statistics")
copy_btn("cp_sea_sum", function() tsv(fmt_df(rv$sea$summary)), "summary table")
copy_btn("cp_sea_means", function() tsv(fmt_df(rv$sea$means)), "means")
copy_btn("cp_sea_aov", function() tsv(fmt_df(rv$sea$anova)), "ANOVA table")
copy_btn("cp_sea_vc", function() tsv(fmt_df(rv$sea$varcomp)), "variance components")

sea_pw <- pw_server("sea", reactive(rv$sea), reactive(input$sea_geno))

sea_keys <- reactive({ m <- rv$sea$means; req(m); unique(if (!is.null(m$Environment)) paste(m$Trait, "|", m$Environment) else m$Trait) })
output$sea_plot_pick <- renderUI(if (length(sea_keys()) > 1) selectInput("sea_plot_key", "Trait / environment", sea_keys()))
sea_plot_obj <- reactive({
  r <- rv$sea; m <- r$means; req(m)
  k <- input$sea_plot_key; if (is.null(k) || !k %in% sea_keys()) k <- sea_keys()[1]
  key <- if (!is.null(m$Environment)) paste(m$Trait, "|", m$Environment) else m$Trait
  if (identical(input$sea_plot_style, "vc")) {
    validate(need(!is.null(r$varcomp), "Variance components come from the mixed model: choose Mixed model in step 1 and run again."))
    return(varcomp_plot(r$varcomp, "Variance components"))
  }
  m <- m[key == k, ]
  xlab <- if (isTRUE(r$random)) "Predicted value (BLUP)" else if (identical(r$method, "anova")) "Mean" else "Adjusted mean (BLUE)"
  if (!identical(input$sea_plot_style, "bar")) {
    mk <- c(k, paste(k, "| 1")); x <- sea_pw()
    lt <- if (!is.null(x$letters)) x$letters[x$letters$Model %in% mk, ]
    mod <- r$models[[intersect(mk, names(r$models))[1] %||% ""]]
    return(means_dot_plot(m, lt, xlab, k, input$sea_err %||% "se", ci_mult(mod), method = x$type))
  }
  m$Genotype <- factor(m$Genotype, levels = m$Genotype[order(m$Mean)])
  p <- ggplot2::ggplot(m, ggplot2::aes(Genotype, Mean)) + ggplot2::geom_col(fill = brand[["main"]], alpha = .85, width = .7) + ggplot2::coord_flip() +
    ggplot2::labs(x = NULL, y = if (isTRUE(r$random)) "Predicted value (BLUP)" else if (identical(r$method, "anova")) "Mean" else "Adjusted mean (BLUE)", title = k) + ggplot2::theme_minimal(base_size = 13)
  if (!is.null(m$SE)) p <- p + ggplot2::geom_errorbar(ggplot2::aes(ymin = Mean - SE, ymax = Mean + SE), width = .3, colour = "grey30")
  p
})
live_server("sea", sea_plot_obj)
sea_diag_fn <- diag_server("sea", reactive(rv$sea))
output$sea_data <- renderUI(data_preview_ui(sea_dat()$d))

sea_tables <- function() { r <- rv$sea; x <- sea_pw()
  Filter(Negate(is.null), list(`Descriptive statistics` = r$descriptive, Summary = r$summary, ANOVA = r$anova, Means = r$means, `Variance components` = r$varcomp, LRT = r$lrt, SED = r$sed,
                               `Pairwise comparisons` = x$table, `Letter groups` = x$letters, `Comparison statistics` = x$stats)) }
output$dl_sea_xlsx <- downloadHandler("pbtools_single_environment.xlsx", function(file) writexl::write_xlsx(sea_tables(), file))
output$dl_sea_doc <- downloadHandler("pbtools_single_environment_report.doc", function(file) {
  r <- rv$sea; req(r)
  make_report(file, "Single-environment analysis", rv$sea_name, rv$sea_set, method_text("sea", r), sea_tables(),
              plot = tryCatch(sea_plot_obj(), error = function(e) NULL), diag = sea_diag_fn,
              pkgs = c(if (!identical(r$method, "anova")) "lme4", "emmeans", if (!is.null(sea_pw())) (if (isTRUE(sea_pw()$classic)) "agricolae" else "multcomp")))
})
