# GxE and stability screen (server). Sourced inside server().

gxe_dat <- data_server(input, "gxe")
# the pickers change only between one trait and several (MTSI); a reactiveVal does not invalidate when set to the same
# value, so moving between the other methods keeps the variables already chosen
gxe_multi <- reactiveVal(FALSE)
observe(gxe_multi(identical(input$gxe_kind, "mtsi")))
output$gxe_vars <- renderUI({
  dat <- gxe_dat(); d <- dat$d; cl <- names(d); e <- dat$ex
  mt <- gxe_multi(); num <- numeric_like(d)
  tagList(if (mt) role_select("gxe_trait", "Traits (two or more)", num, e$trait, "yield|^y$", multiple = TRUE) else role_select("gxe_trait", "Trait", num, e$trait[1], "yield|^y$"),
          if (mt) selectizeInput("gxe_lower", "Traits where LOWER is better (e.g. days to flowering, plant height, disease score)", num, e$lower, multiple = TRUE),
          role_select("gxe_geno", "Genotype", cl, e$geno, "gen|entry|line|variet"),
          role_select("gxe_env", "Environment", cl, e$env, "^env|site|loc|trial|season|year"),
          role_select("gxe_rep", "Replicate (block)", cl, e$rep %||% e$block, "^rep|block|blk"))
})
observeEvent(input$go_gxe, {
  if (!length(input$gxe_trait) || !all(nzchar(c(input$gxe_trait, input$gxe_geno %||% "", input$gxe_env %||% "", input$gxe_rep %||% "")))) {
    rv$gxe <- NULL; rv$gxe_err <- "The variables are not chosen yet (step 3). Wait for the lists to appear, then press Run again."; return() }
  d <- gxe_dat()$d; a <- list(d, input$gxe_trait, input$gxe_geno, input$gxe_env, input$gxe_rep)
  r <- run_safe(withProgress(message = "Computing", value = 0.4, switch(input$gxe_kind,
    fw = do.call(pb_fw, a),
    er = do.call(pb_er, a),
    shukla = { x <- do.call(pb_shukla, a); if (!isTRUE(input$shukla_pbtools)) x$pbtools <- NULL; x },
    stats = do.call(pb_stability_stats, a),
    ammi = do.call(pb_ammi, a),
    gge = do.call(pb_gge, c(a, list(scaling = input$gge_scaling, svp = input$gge_svp))),
    waasb = do.call(pb_waasb, c(a, list(higher = !isFALSE(input$gxe_waasb_higher)))),
    mtsi = do.call(pb_multi_trait, c(a, list(lower = input$gxe_lower %||% character(), si = input$gxe_si %||% 15, wy = input$gxe_mt_wy %||% 50,
                                             mgidi_on = input$gxe_mgidi_on %||% "blup"))))))
  if (!is.null(r$res)) r$res$kind <- input$gxe_kind
  rv$gxe <- r$res; rv$gxe_err <- r$err; rv$gxe_set <- settings_of("gxe", stale_extra$gxe); rv$gxe_name <- gxe_dat()$name
})
output$gxe_msg <- msg_ui("gxe", "Run GxE analysis")
track_stale("gxe", "gxe", stale_extra$gxe)

gxe_label <- c(fw = "Finlay-Wilkinson regression", er = "Eberhart-Russell regression", shukla = "Shukla's stability variance", stats = "Stability statistics", ammi = "AMMI", gge = "GGE biplot",
               waasb = "WAASB / WAASBY", mtsi = "Multi-trait selection (MTSI / MGIDI)")
# WAASBY at the live weight (metan's definition on its 0-100 scales), ranks and the four quadrants of the mean vs. WAASB plot
waasby_table <- reactive({
  r <- rv$gxe; req(r, r$kind == "waasb"); t <- r$table; wy <- input$wy_live %||% 50
  t$WAASBY <- (t$`Mean (0-100)` * wy + t$`Stability (0-100)` * (100 - wy)) / 100
  t$`Rank: WAASBY` <- rank(-t$WAASBY, ties.method = "min")
  n_sel <- max(1, round(nrow(t) * (input$si_live %||% 15) / 100))
  t$Selected <- ifelse(t$`Rank: WAASBY` <= n_sel, "yes", "")
  good_mean <- if (isTRUE(r$higher)) t$Mean >= mean(t$Mean) else t$Mean <= mean(t$Mean); stable <- t$WAASB <= mean(t$WAASB)
  t$Quadrant <- ifelse(stable & good_mean, "IV: stable, good mean", ifelse(!stable & good_mean, "II: good mean, unstable", ifelse(stable, "III: stable, poor mean", "I: unstable, poor mean")))
  t[order(t$`Rank: WAASBY`), c("Genotype", "Mean", "WAASB", "WAASBY", "Rank: WAASBY", "Selected", "Quadrant", "Rank: mean", "Rank: WAASB")]
})
gxe_tables <- function() {
  r <- rv$gxe; req(r)
  switch(r$kind,
    fw = list(`Regression on the environment index` = r$table, `Environment index` = r$env_index),
    er = list(`Eberhart-Russell stability parameters` = r$table, `Joint regression ANOVA` = r$anova, `Environment index` = r$env_index),
    shukla = Filter(Negate(is.null), list(`Shukla's stability variance` = r$table, `PBTools model (nlme gls)` = if (is.data.frame(r$pbtools)) r$pbtools)),
    stats = list(`Stability statistics` = r$table, `Ranks` = r$ranks),
    ammi = Filter(Negate(is.null), list(`AMMI analysis of variance` = r$anova, `Scores` = r$scores, `AMMI stability indexes (metan::ammi_indexes)` = r$asv)),
    gge = list(`Variance explained` = r$explained, `Scores` = r$scores),
    waasb = list(`WAASB and WAASBY` = waasby_table(), `Likelihood ratio tests` = r$lrt, `Genetic parameters` = r$estimates, `Environments` = r$env),
    mtsi = Filter(Negate(is.null), list(`MTSI` = r$mtsi_table, `MTSI selection differentials` = r$mtsi_dif, `MGIDI` = r$mgidi_table,
                                        `MGIDI selection differentials` = r$mgidi_dif, `MGIDI factor contributions (strengths and weaknesses)` = r$mgidi_contri)))
}
gxe_thai <- function(r) switch(r$kind,
  waasb = { t <- waasby_table(); e <- r$estimates; h <- e$Values[e$Parameters == "Heritability"]; p_ge <- r$lrt[["Pr(>Chisq)"]][3]
    sprintf("WAASB/WAASBY ของ %s: น้ำหนักค่าเฉลี่ย %d%% และความเสถียร %d%% พันธุ์ที่ WAASBY สูงสุดคือ %s; คัดเลือก %d%% ได้ %s; พันธุ์ที่เสถียรที่สุด (WAASB ต่ำสุด) คือ %s; อัตราพันธุกรรมรวมทุกสถานที่ = %.2f%s",
            r$trait, input$wy_live %||% 50, 100 - (input$wy_live %||% 50), t$Genotype[1], input$si_live %||% 15, paste(t$Genotype[t$Selected == "yes"], collapse = ", "),
            t$Genotype[which.min(t$WAASB)], h, if (isTRUE(p_ge < 0.05)) " และปฏิสัมพันธ์ G×E มีนัยสำคัญ (LRT)" else "") },
  mtsi = sprintf("คัดเลือก %d%% จาก %d ลักษณะ (%s; ค่าต่ำดี: %s): MTSI เลือก %s%s",
            r$si, length(r$traits), paste(r$traits, collapse = ", "), if (length(r$lower)) paste(r$lower, collapse = ", ") else "ไม่มี",
            paste(r$mtsi$sel_gen, collapse = ", "),
            if (!is.null(r$mgidi)) { both <- intersect(r$mtsi$sel_gen, r$mgidi$sel_gen)
              sprintf(" และ MGIDI (%s) เลือก %s%s", if (r$mgidi_on == "blup") "BLUP" else "ค่าเฉลี่ย", paste(r$mgidi$sel_gen, collapse = ", "),
                      if (length(both)) paste0(" พันธุ์ที่ทั้งสองดัชนีเลือกตรงกัน: ", paste(both, collapse = ", ")) else " (สองดัชนีเลือกไม่ตรงกัน)") }
            else " (MGIDI คำนวณไม่ได้ ดูหมายเหตุ)"),
  fw = { t <- r$table; sprintf("Finlay-Wilkinson: slope ของพันธุ์อยู่ระหว่าง %.2f ถึง %.2f พันธุ์ที่ตอบสนองต่อสภาพแวดล้อมดีมากที่สุดคือ %s (slope = %.2f) และพันธุ์ที่ตอบสนองน้อยที่สุดคือ %s (slope = %.2f) พันธุ์ที่ slope ต่างจาก 1 อย่างมีนัยสำคัญมี %d พันธุ์",
         min(t$Slope), max(t$Slope), t$Genotype[which.max(t$Slope)], max(t$Slope), t$Genotype[which.min(t$Slope)], min(t$Slope), sum(t$`p (b1 = 1)` < 0.05)) },
  er = { t <- r$table; st <- t$Genotype[t$`Stable (Eberhart-Russell)` == "yes"]
         sprintf("Eberhart-Russell (alpha = %.2f): พันธุ์ที่เสถียร (slope ไม่ต่างจาก 1 และ s2di ไม่ต่างจาก 0) มี %d พันธุ์%s; ตอบสนองต่อสภาพแวดล้อมดีมากกว่าค่าเฉลี่ย (b1 > 1) %d พันธุ์, น้อยกว่าค่าเฉลี่ย (b1 < 1) %d พันธุ์; ค่า s2di ต่างจาก 0 (ทำนายผลได้ยาก) %d พันธุ์",
                 r$alpha, length(st), if (length(st)) paste0(" ได้แก่ ", paste(st[order(-t$Mean[match(st, t$Genotype)])], collapse = ", ")) else "",
                 sum(t$Response == "above average (b1 > 1)"), sum(t$Response == "below average (b1 < 1)"), sum(t$Predictable == "no (s2di > 0)")) },
  shukla = { t <- r$table; sprintf("พันธุ์ที่เสถียรที่สุดตามค่า Shukla's stability variance (ค่าต่ำสุด) คือ %s และไม่เสถียรที่สุดคือ %s เมื่อพิจารณาทั้งผลผลิตและความเสถียร (ผลรวมลำดับของ Kang) พันธุ์ที่ดีที่สุดคือ %s",
             t$Genotype[which.min(t$`Shukla variance`)], t$Genotype[which.max(t$`Shukla variance`)], t$Genotype[which.min(t$`Sum of ranks (Kang)`)]) },
  stats = { t <- r$table; sprintf("ค่าเฉลี่ยสูงสุด: %s; Wricke ecovalence ต่ำสุด (เสถียร): %s; Annicchiarico สูงสุด: %s", t$Genotype[which.max(t$Mean)], t$Genotype[which.min(t$`Wricke ecovalence`)], t$Genotype[which.max(t$`Annicchiarico Wi (%)`)]) },
  ammi = { a <- r$anova; pc <- a[grepl("^PC", a$Source), ]; sprintf("ปฏิสัมพันธ์ พันธุ์ × สภาพแวดล้อม แยกได้ %d องค์ประกอบ PC1 และ PC2 อธิบายได้ %.1f%% และ %.1f%% ของผลรวมกำลังสองของปฏิสัมพันธ์", nrow(pc), pc$`% of GxE`[1], pc$`% of GxE`[2]) },
  gge = { e <- r$explained; sprintf("GGE biplot: PC1 และ PC2 อธิบายความผันแปรของพันธุ์และปฏิสัมพันธ์ได้ %.1f%% และ %.1f%% (รวม %.1f%%)", e[1, 2], e[2, 2], e[1, 2] + e[2, 2]) })
output$gxe_results <- renderUI({
  r <- rv$gxe; req(r)
  # WAASB: the parts that follow the live sliders (wy_live, si_live) are outputs of their own, so moving a slider
  # redraws them and not every card; isolate() keeps the sliders out of this renderer's dependencies
  live <- function(id, ui) if (r$kind == "waasb") uiOutput(id) else ui
  tb <- isolate(gxe_tables())
  tagList(
    key_stats(stat("Analysis", gxe_label[[r$kind]]), stat(if (r$kind == "mtsi") "Traits" else "Trait", paste(if (r$kind == "mtsi") r$traits else r$trait, collapse = ", "))),
    result_card("สรุปผล (ภาษาไทย)", live("gxe_waasb_th", p(lang = "th", gxe_thai(r))), copy = "cp_gxe_th", copy_label = "คัดลอก"),
    if (r$kind == "mtsi" && length(r$dropped)) note_box(sprintf("Left out of MGIDI because their genotype BLUPs do not vary (no genetic variance across environments): %s. Choose \"Genotype means\" to use them.", paste(r$dropped, collapse = ", "))),
    if (r$kind == "mtsi" && is.null(r$mgidi)) note_box("MGIDI needs at least two traits with genetic variance. Choose \"Genotype means\" in step 4 (Selection), or other traits."),
    if (r$kind %in% c("ammi", "gge", "waasb", "mtsi")) result_card(paste("การแปลผล", c(ammi = "AMMI", gge = "GGE biplot", waasb = "WAASB / WAASBY", mtsi = "MTSI / MGIDI")[[r$kind]]),
      div(lang = "th", live("gxe_waasb_read", reading_ui(gxe_reading(r))), p(class = "text-muted small mb-0", "ดูกราฟแต่ละแบบได้ในแท็บ Plot หมายเลขหัวข้อตรงกับมุมมองของ GGE biplot")),
      copy = "cp_gxe_read", copy_label = "คัดลอก"),
    if (r$kind == "shukla" && is.character(r$pbtools)) note_box("The PBTools model (nlme::gls) could not be fitted to these data: ", r$pbtools),
    if (r$kind == "shukla" && is.data.frame(r$pbtools)) note_box("PBTools estimates Shukla's model differently from metan: one residual standard deviation per genotype, by REML (nlme::gls). Its estimates are standard deviations and rank the genotypes similarly but not identically. See the Methods page."),
    lapply(names(tb), function(n) result_card(n, if (n == "WAASB and WAASBY") live("gxe_waasby", NULL) else html_table(fmt_df(tb[[n]])))))
})
waasb_res <- reactive({ r <- rv$gxe; req(r, r$kind == "waasb"); r })
output$gxe_waasb_th <- renderUI(p(lang = "th", gxe_thai(waasb_res())))
output$gxe_waasb_read <- renderUI(reading_ui(gxe_reading(waasb_res())))
output$gxe_waasby <- renderUI(html_table(fmt_df(waasby_table())))
copy_btn("cp_gxe_th", function() gxe_thai(rv$gxe), "summary")
gxe_reading <- function(r) switch(r$kind, ammi = ammi_reading(r), gge = gge_reading(r), waasb = waasb_reading(r, waasby_table(), input$wy_live %||% 50), mtsi = mtsi_reading(r))
copy_btn("cp_gxe_read", function() reading_text(gxe_reading(rv$gxe)), "interpretation")

# WAASBY ranks over weighting scenarios: computed only when that plot is asked for (about 20 s in the browser)
wsmp_res <- reactive({ r <- rv$gxe; req(r, r$kind == "waasb")
  withProgress(message = "Computing the weighting scenarios", value = 0.5, pb_wsmp(r$model, isTRUE(r$higher))) })
# genotype x environment cell means of the analysed trait, from the data of the screen
gxe_cells <- function(r) {
  d <- gxe_dat()$d; g <- input$gxe_geno; e <- input$gxe_env; y <- r$trait
  validate(need(all(c(g, e, y) %in% names(d)), "Run the analysis again: the variables have changed."))
  a <- stats::aggregate(pb_num(d[[y]]), list(Genotype = as.character(d[[g]]), Environment = as.character(d[[e]])), mean, na.rm = TRUE)
  stats::setNames(a, c("Genotype", "Environment", "Mean"))
}
gxe_plot_obj <- reactive({
  r <- rv$gxe; req(r)
  view <- if (r$kind %in% c("fw", "er")) input$reg_view %||% "quad" else if (r$kind %in% c("shukla", "stats")) input$stab_view %||% "orig" else "orig"
  if (view == "heat") return(ge_heatmap(gxe_cells(r), "Genotype x environment means", r$trait))
  if (view == "quad" && r$kind == "fw") return(mean_slope_plot(r$table, "Finlay-Wilkinson: mean and responsiveness"))
  if (view == "quad" && r$kind == "er") { t <- r$table; t$s2di <- pmax(t$`Deviation variance (s2di)`, 0); t$Stable <- t$`Stable (Eberhart-Russell)`
    return(mean_slope_plot(t, "Eberhart-Russell: mean, slope b and deviation s2di", size_var = "s2di", colour_var = "Stable") +
             ggplot2::labs(size = "s2di", colour = "Stable") + ggplot2::scale_size_continuous(range = c(1.8, 6))) }
  switch(r$kind,
    fw = { cl <- r$cell; t <- r$table
      ggplot2::ggplot(cl, ggplot2::aes(Index, Mean, colour = Genotype)) + ggplot2::geom_point(size = 1.6) +
        ggplot2::geom_abline(data = t, ggplot2::aes(intercept = Mean, slope = Slope, colour = Genotype)) +
        ggplot2::labs(x = "Environment index (site mean - grand mean)", y = r$trait, title = "Finlay-Wilkinson regression") + ggplot2::theme_minimal(base_size = 13) },
    er = { t <- r$table
      ggplot2::ggplot(t, ggplot2::aes(Slope, `Deviation variance (s2di)`, label = Genotype, colour = `Stable (Eberhart-Russell)`)) +
        ggplot2::geom_vline(xintercept = 1, linetype = 2, colour = "grey50") + ggplot2::geom_point(size = 2.5) +
        ggplot2::geom_text(vjust = -0.8, size = 3.3, check_overlap = TRUE, show.legend = FALSE) +
        ggplot2::scale_colour_manual(values = c(yes = brand[["main"]], no = "grey55")) +
        ggplot2::labs(x = "Slope b1", y = "Deviation from regression (s2di)", colour = "Stable", title = "Eberhart-Russell stability parameters") + ggplot2::theme_minimal(base_size = 13) },
    shukla = { t <- r$table
      ggplot2::ggplot(t, ggplot2::aes(Mean, `Shukla variance`, label = Genotype)) + ggplot2::geom_point(colour = brand[["main"]], size = 2.5) +
        ggplot2::geom_text(vjust = -0.8, size = 3.3, check_overlap = TRUE) + ggplot2::geom_vline(xintercept = mean(t$Mean), linetype = 2, colour = "grey50") +
        ggplot2::labs(x = "Genotype mean", y = "Shukla's stability variance", title = "Mean and stability") + ggplot2::theme_minimal(base_size = 13) },
    stats = { t <- r$table
      ggplot2::ggplot(t, ggplot2::aes(Mean, `Wricke ecovalence`, label = Genotype)) + ggplot2::geom_point(colour = brand[["main"]], size = 2.5) +
        ggplot2::geom_text(vjust = -0.8, size = 3.3, check_overlap = TRUE) + ggplot2::labs(x = "Genotype mean", y = "Wricke's ecovalence") + ggplot2::theme_minimal(base_size = 13) },
    ammi = switch(as.character(input$ammi_type %||% 2),
      eigen = ammi_scree_plot(r$anova),
      resid = metan::residual_plots(r$model, plot_theme = theme_rbtool(), col.point = pub_col[["gen"]], col.line = pub_col[["env"]]),
      metan_tidy(metan::plot_scores(r$model, leg.lab = c("Environment", "Genotype"), type = as.integer(input$ammi_type %||% 2), plot_theme = theme_rbtool(), col.gen = pub_col[["gen"]], col.env = pub_col[["env"]],
                         col.segm.env = pub_col[["env"]], col.line = pub_col[["line"]]))),
    waasb = switch(input$waasb_view %||% "rank",
      quad = metan_tidy(metan::plot_scores(r$model, leg.lab = c("Environment", "Genotype"), type = 3, plot_theme = theme_rbtool(), col.gen = pub_col[["gen"]], col.env = pub_col[["env"]], col.line = pub_col[["line"]])),
      pc = metan_tidy(metan::plot_scores(r$model, leg.lab = c("Environment", "Genotype"), type = 2, plot_theme = theme_rbtool(), col.gen = pub_col[["gen"]], col.env = pub_col[["env"]], col.segm.env = pub_col[["env"]], col.line = pub_col[["line"]])),
      scen = plot(wsmp_res(), type = 2) + ggplot2::theme(axis.text = ggplot2::element_text(colour = "black")),
      blup = metan::plot_blup(r$model, plot_theme = theme_rbtool(), col.shape = c(pub_col[["gen"]], pub_col[["env"]])),
      eigen = metan::plot_eigen(r$model, plot_theme = theme_rbtool()),
      resid = { res <- as.data.frame(r$model$Y$residuals)
        record_base_plot(function() pb_diag_plot(data.frame(Fitted = res$.fitted, Residual = res$.resid),
                                                 "Residuals of the mixed model with genotype and genotype x environment random (metan::waasb).")) },
      { t <- waasby_table(); t$Genotype <- factor(t$Genotype, levels = rev(t$Genotype))
        ggplot2::ggplot(t, ggplot2::aes(WAASBY, Genotype, fill = Selected == "yes")) + ggplot2::geom_col(width = .7) +
          ggplot2::scale_fill_manual(values = c(`TRUE` = brand[["main"]], `FALSE` = "grey70"), labels = c(`TRUE` = "selected", `FALSE` = "not selected"), name = NULL) +
          ggplot2::labs(x = sprintf("WAASBY (mean %d%%, stability %d%%)", input$wy_live %||% 50, 100 - (input$wy_live %||% 50)), y = NULL, title = paste("WAASBY:", r$trait)) +
          ggplot2::theme_minimal(base_size = 13) + ggplot2::theme(legend.position = "bottom") }),
    mtsi = switch(input$mt_view %||% "mgidi",
      mtsi = plot(r$mtsi, col.sel = pub_col[["sel"]], col.nonsel = pub_col[["nonsel"]]), mtsi_contri = plot(r$mtsi, type = "contribution"),
      corr = metan::corr_plot(r$gen_means[-1]),
      venn = { validate(need(!is.null(r$mgidi), "MGIDI could not be computed, so there is nothing to compare."))
        metan::venn_plot(list(MTSI = r$mtsi$sel_gen, MGIDI = r$mgidi$sel_gen), show_elements = TRUE) },
      mgidi_contri = { req(r$mgidi); plot(r$mgidi, type = "contribution") },
      { req(r$mgidi); plot(r$mgidi, col.sel = pub_col[["sel"]], col.nonsel = pub_col[["nonsel"]]) }),
    gge = { ty <- as.integer(input$gge_type %||% 1); g <- r$model$Y; ge <- g$labelgen; en <- g$labelenv
      pick <- function(x, set, i = 1) if (!is.null(x) && x %in% set) x else set[i]
      metan_tidy(plot(r$model, type = ty, plot_theme = theme_rbtool(), leg.lab = c("Environment", "Genotype"), col.gen = pub_col[["gen"]], col.env = pub_col[["env"]], col.line = pub_col[["env"]],
           sel_env = pick(input$gge_env, en), sel_gen = pick(input$gge_gen, ge),
           sel_gen1 = pick(input$gge_gen1, ge), sel_gen2 = pick(input$gge_gen2, ge, 2),
           repel = isTRUE(input$gge_repel), size.text.gen = input$gge_text %||% 3.5, size.text.env = input$gge_text %||% 3.5,
           size.text.win = (input$gge_text %||% 3.5) + 1, size.shape = input$gge_point %||% 2.2, size.shape.win = (input$gge_point %||% 2.2) + 1)) })
})
# genotype / environment pickers for the GGE views that examine one environment (5), one genotype (7) or two genotypes (9)
output$gge_pick <- renderUI({
  r <- rv$gxe; req(r, r$kind == "gge"); g <- r$model$Y
  switch(as.character(input$gge_type %||% 1),
    `5` = selectInput("gge_env", "Environment", g$labelenv, isolate(input$gge_env), width = "100%"),
    `7` = selectInput("gge_gen", "Genotype", g$labelgen, isolate(input$gge_gen), width = "100%"),
    `9` = fluidRow(column(6, selectInput("gge_gen1", "Genotype 1", g$labelgen, isolate(input$gge_gen1) %||% g$labelgen[1])),
                   column(6, selectInput("gge_gen2", "Genotype 2", g$labelgen, isolate(input$gge_gen2) %||% g$labelgen[2]))),
    NULL)
})
live_server("gxe", gxe_plot_obj)
output$gxe_data <- renderUI(data_preview_ui(gxe_dat()$d))

output$dl_gxe_xlsx <- downloadHandler("pbtools_gxe.xlsx", function(file) writexl::write_xlsx(gxe_tables(), file))
output$dl_gxe_doc <- downloadHandler("pbtools_gxe_report.doc", function(file) {
  r <- rv$gxe; req(r)
  make_report(file, paste("GxE analysis:", gxe_label[[r$kind]]), rv$gxe_name, rv$gxe_set, method_text(r$kind, r), gxe_tables(),
              plot = tryCatch(gxe_plot_obj(), error = function(e) NULL),
              notes = if (r$kind %in% c("ammi", "gge", "waasb", "mtsi")) unlist(gxe_reading(r), use.names = FALSE), pkgs = c("metan", if (r$kind == "shukla") "nlme"))
})
