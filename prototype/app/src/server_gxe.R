# GxE and stability screen (server). Sourced inside server().

gxe_dat <- data_server(input, "gxe")
output$gxe_vars <- renderUI({
  dat <- gxe_dat(); d <- dat$d; cl <- names(d); e <- dat$ex
  tagList(role_select("gxe_trait", "Trait", numeric_like(d), e$trait[1], "yield|^y$"),
          role_select("gxe_geno", "Genotype", cl, e$geno, "gen|entry|line|variet"),
          role_select("gxe_env", "Environment", cl, e$env, "^env|site|loc|trial|season|year"),
          role_select("gxe_rep", "Replicate (block)", cl, e$rep %||% e$block, "^rep|block|blk"))
})
observeEvent(input$go_gxe, {
  d <- gxe_dat()$d; a <- list(d, input$gxe_trait, input$gxe_geno, input$gxe_env, input$gxe_rep)
  r <- run_safe(withProgress(message = "Computing", value = 0.4, switch(input$gxe_kind,
    fw = do.call(pb_fw, a),
    shukla = { x <- do.call(pb_shukla, a); if (!isTRUE(input$shukla_pbtools)) x$pbtools <- NULL; x },
    stats = do.call(pb_stability_stats, a),
    ammi = do.call(pb_ammi, a),
    gge = do.call(pb_gge, c(a, list(scaling = input$gge_scaling, svp = input$gge_svp))))))
  if (!is.null(r$res)) r$res$kind <- input$gxe_kind
  rv$gxe <- r$res; rv$gxe_err <- r$err; rv$gxe_set <- settings_of("gxe", stale_extra$gxe); rv$gxe_name <- gxe_dat()$name
})
output$gxe_msg <- msg_ui("gxe", "Run GxE analysis")
track_stale("gxe", "gxe", stale_extra$gxe)

gxe_label <- c(fw = "Finlay-Wilkinson regression", shukla = "Shukla's stability variance", stats = "Stability statistics", ammi = "AMMI", gge = "GGE biplot")
gxe_tables <- function() {
  r <- rv$gxe; req(r)
  switch(r$kind,
    fw = list(`Regression on the environment index` = r$table, `Environment index` = r$env_index, `ANOVA` = r$anova),
    shukla = Filter(Negate(is.null), list(`Shukla's stability variance` = r$table, `PBTools model (nlme gls)` = if (is.data.frame(r$pbtools)) r$pbtools)),
    stats = list(`Stability statistics` = r$table, `Ranks` = r$ranks),
    ammi = list(`AMMI analysis of variance` = r$anova, `Scores` = r$scores),
    gge = list(`Variance explained` = r$explained, `Scores` = r$scores))
}
gxe_thai <- function(r) switch(r$kind,
  fw = { t <- r$table; sprintf("Finlay-Wilkinson: slope ของพันธุ์อยู่ระหว่าง %.2f ถึง %.2f พันธุ์ที่ตอบสนองต่อสภาพแวดล้อมดีมากที่สุดคือ %s (slope = %.2f) และพันธุ์ที่ตอบสนองน้อยที่สุดคือ %s (slope = %.2f) พันธุ์ที่ slope ต่างจาก 1 อย่างมีนัยสำคัญมี %d พันธุ์",
         min(t$Slope), max(t$Slope), t$Genotype[which.max(t$Slope)], max(t$Slope), t$Genotype[which.min(t$Slope)], min(t$Slope), sum(t$`p (b1 = 1)` < 0.05)) },
  shukla = { t <- r$table; sprintf("พันธุ์ที่เสถียรที่สุดตามค่า Shukla's stability variance (ค่าต่ำสุด) คือ %s และไม่เสถียรที่สุดคือ %s เมื่อพิจารณาทั้งผลผลิตและความเสถียร (ผลรวมลำดับของ Kang) พันธุ์ที่ดีที่สุดคือ %s",
             t$Genotype[which.min(t$`Shukla variance`)], t$Genotype[which.max(t$`Shukla variance`)], t$Genotype[which.min(t$`Sum of ranks (Kang)`)]) },
  stats = { t <- r$table; sprintf("ค่าเฉลี่ยสูงสุด: %s; Wricke ecovalence ต่ำสุด (เสถียร): %s; Annicchiarico สูงสุด: %s", t$Genotype[which.max(t$Mean)], t$Genotype[which.min(t$`Wricke ecovalence`)], t$Genotype[which.max(t$`Annicchiarico Wi (%)`)]) },
  ammi = { a <- r$anova; pc <- a[grepl("^PC", a$Source), ]; sprintf("ปฏิสัมพันธ์ พันธุ์ × สภาพแวดล้อม แยกได้ %d องค์ประกอบ PC1 และ PC2 อธิบายได้ %.1f%% และ %.1f%% ของผลรวมกำลังสองของปฏิสัมพันธ์", nrow(pc), pc$`% of GxE`[1], pc$`% of GxE`[2]) },
  gge = { e <- r$explained; sprintf("GGE biplot: PC1 และ PC2 อธิบายความผันแปรของพันธุ์และปฏิสัมพันธ์ได้ %.1f%% และ %.1f%% (รวม %.1f%%)", e[1, 2], e[2, 2], e[1, 2] + e[2, 2]) })
output$gxe_results <- renderUI({
  r <- rv$gxe; req(r)
  tb <- gxe_tables()
  tagList(
    key_stats(stat("Analysis", gxe_label[[r$kind]]), stat("Trait", r$trait)),
    result_card("สรุปผล (ภาษาไทย)", p(lang = "th", gxe_thai(r)), copy = "cp_gxe_th", copy_label = "คัดลอก"),
    if (r$kind == "shukla" && is.character(r$pbtools)) note_box("The PBTools model (nlme::gls) could not be fitted to these data: ", r$pbtools),
    if (r$kind == "shukla" && is.data.frame(r$pbtools)) note_box("PBTools estimates Shukla's model differently from metan: one residual standard deviation per genotype, by REML (nlme::gls). Its estimates are standard deviations and rank the genotypes similarly but not identically. See the Methods page."),
    lapply(names(tb), function(n) result_card(n, html_table(fmt_df(tb[[n]])))))
})
copy_btn("cp_gxe_th", function() gxe_thai(rv$gxe), "summary")

gxe_plot_obj <- reactive({
  r <- rv$gxe; req(r)
  switch(r$kind,
    fw = { cl <- r$cell; t <- r$table
      ggplot2::ggplot(cl, ggplot2::aes(Index, Mean, colour = Genotype)) + ggplot2::geom_point(size = 1.6) +
        ggplot2::geom_abline(data = t, ggplot2::aes(intercept = Mean, slope = Slope, colour = Genotype)) +
        ggplot2::labs(x = "Environment index (site mean - grand mean)", y = r$trait, title = "Finlay-Wilkinson regression") + ggplot2::theme_minimal(base_size = 13) },
    shukla = { t <- r$table
      ggplot2::ggplot(t, ggplot2::aes(Mean, `Shukla variance`, label = Genotype)) + ggplot2::geom_point(colour = brand[["main"]], size = 2.5) +
        ggplot2::geom_text(vjust = -0.8, size = 3.3, check_overlap = TRUE) + ggplot2::geom_vline(xintercept = mean(t$Mean), linetype = 2, colour = "grey50") +
        ggplot2::labs(x = "Genotype mean", y = "Shukla's stability variance", title = "Mean and stability") + ggplot2::theme_minimal(base_size = 13) },
    stats = { t <- r$table
      ggplot2::ggplot(t, ggplot2::aes(Mean, `Wricke ecovalence`, label = Genotype)) + ggplot2::geom_point(colour = brand[["main"]], size = 2.5) +
        ggplot2::geom_text(vjust = -0.8, size = 3.3, check_overlap = TRUE) + ggplot2::labs(x = "Genotype mean", y = "Wricke's ecovalence") + ggplot2::theme_minimal(base_size = 13) },
    ammi = metan::plot_scores(r$model, type = as.integer(input$ammi_type)),
    gge = plot(r$model, type = as.integer(input$gge_type)))
})
output$gxe_plot <- renderPlot(gxe_plot_obj())
output$gxe_data <- renderUI(data_preview_ui(gxe_dat()$d))

output$dl_gxe_xlsx <- downloadHandler("pbtools_gxe.xlsx", function(file) writexl::write_xlsx(gxe_tables(), file))
output$dl_gxe_doc <- downloadHandler("pbtools_gxe_report.doc", function(file) {
  r <- rv$gxe; req(r)
  make_report(file, paste("GxE analysis:", gxe_label[[r$kind]]), rv$gxe_name, rv$gxe_set, method_text(r$kind, r), gxe_tables(),
              plot = tryCatch(gxe_plot_obj(), error = function(e) NULL), pkgs = c("metan", if (r$kind == "shukla") "nlme"))
})
