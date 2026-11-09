# Multi-environment screen (server). Sourced inside server().

met_dat <- data_server(input, "met")
observeEvent(input$met_example, {
  e <- examples[[input$met_example]]
  if (!is.null(e$design)) updateRadioButtons(session, "met_design", selected = e$design)
})
# anova, one (one-stage) or two (two-stage, the stage-1 means computed here from the plot data)
met_mode <- reactive(if (!identical(input$met_method, "mixed")) "anova" else if (identical(input$met_stage, "two")) "two" else "one")

# what the pickers depend on; a reactiveVal does not invalidate when set to the same value, so switching between
# combined ANOVA and one-stage keeps the variables already chosen
met_vars_mode <- reactiveVal(NULL)
observe({ req(input$met_method); met_vars_mode(list(design = input$met_design, multi = met_mode() != "two")) })
output$met_vars <- renderUI({
  dat <- met_dat(); mode <- met_vars_mode(); req(mode)
  raw_vars_ui("met", dat, mode$design, multi_trait = mode$multi, need_env = TRUE)
})

observeEvent(input$go_met, {
  d <- met_dat()$d; random <- input$met_random == "random"; s1 <- NULL
  r <- run_safe(withProgress(message = if (met_mode() == "anova") "Computing the combined analysis of variance" else "Fitting the mixed models", value = 0.4, {
    if (met_mode() == "anova") {
      pb_combined_anova(d, input$met_trait, input$met_geno, input$met_env, input$met_block, env_random = !identical(input$met_env_random, "fixed"))
    } else if (met_mode() == "one") {
      pb_one_stage(d, input$met_trait, input$met_geno, input$met_env, input$met_design, input$met_block, input$met_col, input$met_rep, random)
    } else {
      s1 <- pb_stage1(d, input$met_trait[1], input$met_geno, input$met_design, input$met_block, input$met_col, input$met_rep, input$met_env)
      x <- pb_two_stage(s1, weight = input$met_weight, random = random)
      x$stage1 <- s1; x$geno_col <- "Genotype"; x
    }
  }))
  rv$met <- r$res; rv$met_err <- r$err; rv$met_set <- settings_of("met"); rv$met_name <- met_dat()$name
})
output$met_msg <- msg_ui("met", "Run multi-environment analysis")
track_stale("met", "met")
met_geno_col <- reactive(if (rv$met$method %in% c("one-stage", "combined ANOVA")) input$met_geno else rv$met$geno_col %||% "Genotype")

met_thai <- function(r) {
  if (identical(r$method, "combined ANOVA")) return(met_anova_thai(r))
  i <- r$info; ge <- r$lrt[!is.na(r$lrt$Chisq) & r$lrt$Test == "Genotype x Environment", ]
  g <- col_lab("met", "geno", "พันธุ์"); e <- col_lab("met", "env", "สถานที่")
  paste0(sprintf("วิเคราะห์ข้อมูลหลายสถานที่แบบ%s (%d %s, %d %s) โดยให้ %s เป็น %s",
                 if (r$method == "one-stage") "ขั้นตอนเดียว (one-stage)" else "สองขั้นตอน (two-stage)", i$Environments[1], e, i$Genotypes[1], g, g,
                 if (isTRUE(r$random)) "random effect" else "fixed effect"),
         if (NROW(ge)) sprintf(" ปฏิสัมพันธ์ระหว่าง %s กับ %s %s (likelihood ratio test, p %s)", g, e, if (ge$P[1] < 0.05) "มีนัยสำคัญทางสถิติ" else "ไม่มีนัยสำคัญทางสถิติ",
                               if (ge$P[1] < 0.0001) "< 0.0001" else paste("=", formatC(ge$P[1], format = "f", digits = 4))) else "",
         if (isTRUE(r$random) && identical(i$Weight[1], "1/SE^2")) " ไม่รายงานอัตราพันธุกรรม เพราะการถ่วงน้ำหนัก 1/SE² ทำให้ค่านี้ขึ้นกับหน่วยของข้อมูล" else
         if (isTRUE(r$random)) sprintf(" ค่าอัตราพันธุกรรมรวมทุกสถานที่ = %s", paste(sprintf("%.2f", i$Heritability), collapse = ", ")) else
           { best <- r$geno[which.max(r$geno$Mean), ]; sprintf(" %s ที่มีค่าเฉลี่ยรวมสูงสุดคือ %s (%s)", g, best$Genotype, fmt_n(best$Mean)) })
}
output$met_results <- renderUI({
  r <- rv$met; req(r)
  gewide <- if (!is.null(r$genoenv)) { w <- stats::reshape(r$genoenv[r$genoenv$Trait == r$genoenv$Trait[1], c("Genotype", "Environment", "Mean")], idvar = "Genotype", timevar = "Environment", direction = "wide"); names(w) <- sub("^Mean[.]", "", names(w)); w }
  tagList(
    if (identical(r$info$Weight[1], "1/SE^2")) note_box("With weights 1/SE², the variance components and SE follow lme4 1.x; PBTools 1.1 (old lme4) printed them on another scale. The means agree. See the Methods page."),
    if (identical(r$info$Weight[1], "1/SE^2") && isTRUE(r$random)) div(class = "alert alert-warning small", lang = "th", icon("triangle-exclamation"),
      strong(" ไม่ควรใช้ค่าอัตราพันธุกรรม (H²) ของการวิเคราะห์นี้: "),
      "two-stage แบบถ่วงน้ำหนัก 1/SE² กำหนดความแปรปรวนของ G×E เป็น 1 ตามที่ PBTools ทำ ค่า H² จึงขึ้นกับหน่วยของข้อมูล (เช่น kg/ha ให้ H² เกือบ 1) ",
      "ค่า BLUP และการจัดอันดับพันธุ์ใช้ได้ตามปกติ ถ้าต้องการ H² ใช้ one-stage หรือ two-stage แบบไม่ถ่วงน้ำหนัก"),
    result_card("สรุปผล (ภาษาไทย)", p(lang = "th", met_thai(r)), copy = "cp_met_th", copy_label = "คัดลอก"),
    result_card("Descriptive statistics", html_table(fmt_df(r$descriptive)), copy = "cp_met_desc",
      p(class = "text-muted small mb-0", if (r$method %in% c("one-stage", "combined ANOVA")) "Plot data of all environments together, as PBTools (DescriptiveStatistics)."
        else "Stage-1 means of all environments together, as PBTools (DescriptiveStatistics).")),
    result_card("Summary", html_table(fmt_df(r$info)),
      if (!is.null(r$anova)) p(class = "text-muted small mb-0", sprintf("CV = sqrt(pooled error MS) / grand mean x 100. LSD (5%%) = t x average SED of the genotype means, with the error term of the genotype F test (%s).",
                                                                       if (isTRUE(r$env_random)) "genotype x environment" else "pooled error"))),
    if (!is.null(r$anova)) result_card("Combined analysis of variance", html_table(fmt_df(r$anova)), copy = "cp_met_aov",
      p(class = "text-muted small mb-0", if (isTRUE(r$env_random)) "Environments random: Environment is tested against Replicate within environment, Genotype against Genotype x Environment (expected mean squares of the mixed model; Gomez & Gomez 1984). stats::lm, sequential sums of squares."
        else "Environments fixed: every term is tested against the pooled error. stats::lm, sequential sums of squares.")),
    if (!is.null(r$bartlett)) result_card("Homogeneity of the error variances of the sites", html_table(fmt_df(r$bartlett)), html_table(fmt_df(r$errors)),
      p(class = "text-muted small mb-0", "Bartlett's test (stats::bartlett.test) on the RCBD analysis of each site. A significant test, or a largest / smallest ratio above about 3, means the sites are not equally precise and pooling their errors is questionable.")),
    result_card(if (isTRUE(r$random)) "Genotype BLUPs across environments" else if (!is.null(r$anova)) "Genotype means across environments and SE" else "Genotype means across environments (BLUEs) and SE", html_table(fmt_df(r$geno)), copy = "cp_met_geno"),
    result_card("Environment means", html_table(fmt_df(r$env))),
    if (!is.null(r$varcomp)) result_card("Variance components", html_table(fmt_df(r$varcomp))),
    if (!is.null(r$lrt)) result_card("Likelihood ratio tests", html_table(fmt_df(r$lrt)),
      p(class = "text-muted small mb-0", "Each term is tested by dropping it from the model: REML fits for random terms; ML refit for the fixed genotype term.")),
    if (!is.null(gewide)) result_card(sprintf("Genotype x environment means (%s)", r$genoenv$Trait[1]), html_table(fmt_df(gewide)), copy = "cp_met_ge",
      p(class = "text-muted small mb-0", if (!is.null(r$anova)) "Arithmetic means of each genotype in each environment." else "Model predictions without the block terms, as PBTools.")),
    if (!is.null(r$stage1)) result_card("Stage-1 table (input of stage 2)", html_table(fmt_df(r$stage1))),
    result_card("Model", tags$pre(class = "small mb-0", paste(r$formulas, collapse = "\n"))))
})
copy_btn("cp_met_th", function() met_thai(rv$met), "summary")
copy_btn("cp_met_desc", function() tsv(fmt_df(rv$met$descriptive)), "descriptive statistics")
copy_btn("cp_met_geno", function() tsv(fmt_df(rv$met$geno)), "genotype means")
copy_btn("cp_met_aov", function() tsv(fmt_df(rv$met$anova)), "ANOVA table")
copy_btn("cp_met_ge", function() tsv(fmt_df(rv$met$genoenv)), "GxE means")

met_pw <- pw_server("met", reactive(rv$met), met_geno_col)

met_plot_obj <- reactive({
  r <- rv$met; req(r)
  if (identical(input$met_plot_type, "vc")) {
    validate(need(!is.null(r$varcomp), "Variance components come from the one-stage and two-stage mixed models: choose one of them in step 1 and run again."))
    return(varcomp_plot(r$varcomp, "Variance components"))
  }
  if (input$met_plot_type %in% c("ge", "heat")) {
    ge <- if (!is.null(r$genoenv)) r$genoenv[r$genoenv$Trait == r$genoenv$Trait[1], ] else {
      s <- r$stage1; g <- met_geno_col(); en <- "Env"; mn <- "Mean"
      data.frame(Genotype = s[[g]], Environment = s[[en]], Mean = pb_num(s[[mn]])) }
    if (identical(input$met_plot_type, "heat")) return(ge_heatmap(ge, "Genotype x environment means", if (!is.null(r$geno$Trait)) r$geno$Trait[1] else "Mean"))
    em <- tapply(ge$Mean, ge$Environment, mean); ge$Environment <- factor(ge$Environment, levels = names(sort(em)))
    ggplot2::ggplot(ge, ggplot2::aes(Environment, Mean, group = Genotype, colour = Genotype)) + ggplot2::geom_line() + ggplot2::geom_point(size = 1.6) +
      ggplot2::labs(x = "Environment (ordered by its mean)", y = "Mean", title = "Genotype x environment interaction") + ggplot2::theme_minimal(base_size = 13) +
      ggplot2::theme(axis.text.x = ggplot2::element_text(angle = 45, hjust = 1))
  } else {
    g <- r$geno[r$geno$Trait == r$geno$Trait[1], ]
    if (!identical(input$met_plot_style, "bar")) {
      x <- met_pw(); tr <- g$Trait[1]
      lt <- if (!is.null(x$letters)) x$letters[x$letters$Model == tr, ]
      return(means_dot_plot(g, lt, if (isTRUE(r$random)) "BLUP across environments" else if (identical(r$method, "combined ANOVA")) "Mean across environments" else "Mean across environments (BLUE)",
                            tr, input$met_err %||% "se", ci_mult(r$models[[tr]]), method = x$type))
    }
    g$Genotype <- factor(g$Genotype, levels = g$Genotype[order(g$Mean)])
    p <- ggplot2::ggplot(g, ggplot2::aes(Genotype, Mean)) + ggplot2::geom_col(fill = brand[["main"]], alpha = .85, width = .7) + ggplot2::coord_flip() +
      ggplot2::labs(x = NULL, y = if (isTRUE(r$random)) "BLUP across environments" else "Mean across environments (BLUE)", title = g$Trait[1]) + ggplot2::theme_minimal(base_size = 13)
    if (!is.null(g$SE)) p <- p + ggplot2::geom_errorbar(ggplot2::aes(ymin = Mean - SE, ymax = Mean + SE), width = .3, colour = "grey30")
    p
  }
})
live_server("met", met_plot_obj)
met_diag_fn <- diag_server("met", reactive(rv$met))
output$met_data <- renderUI(data_preview_ui(met_dat()$d))

met_tables <- function() { r <- rv$met; x <- met_pw()
  Filter(Negate(is.null), list(`Descriptive statistics` = r$descriptive, Summary = r$info, ANOVA = r$anova, `Bartlett test` = r$bartlett, `Error MS by site` = r$errors, `Genotype means` = r$geno, `Environment means` = r$env, `Variance components` = r$varcomp,
                               LRT = r$lrt, `GxE means` = r$genoenv, `Stage-1 table` = r$stage1, `Pairwise comparisons` = x$table, `Letter groups` = x$letters, `Comparison statistics` = x$stats)) }
output$dl_met_xlsx <- downloadHandler("pbtools_multi_environment.xlsx", function(file) writexl::write_xlsx(met_tables(), file))
output$dl_met_doc <- downloadHandler("pbtools_multi_environment_report.doc", function(file) {
  r <- rv$met; req(r)
  make_report(file, paste("Multi-environment analysis,", r$method), rv$met_name, rv$met_set, method_text("met", r), met_tables(),
              plot = tryCatch(met_plot_obj(), error = function(e) NULL), diag = met_diag_fn, pkgs = c(if (!identical(r$method, "combined ANOVA")) "lme4", "emmeans", if (!is.null(met_pw())) (if (isTRUE(met_pw()$classic)) "agricolae" else "multcomp")))
})

# Thai summary of the combined ANOVA, one paragraph per trait
met_anova_thai <- function(r) {
  gl <- col_lab("met", "geno", "พันธุ์"); el <- col_lab("met", "env", "สถานที่")
  sig <- function(p) if (p < 0.01) "มีนัยสำคัญยิ่ง (p < 0.01)" else if (p < 0.05) "มีนัยสำคัญ (p < 0.05)" else sprintf("ไม่มีนัยสำคัญ (p = %.3f)", p)
  paste(vapply(unique(r$anova$Trait), function(t) {
    x <- r$anova[r$anova$Trait == t, ]; ii <- r$info[r$info$Trait == t, ]; bb <- r$bartlett[r$bartlett$Trait == t, ]; g <- r$geno[r$geno$Trait == t, ]
    pg <- x$`Pr(>F)`[x$Source == "Genotype"]; pge <- x$`Pr(>F)`[x$Source == "Genotype x Environment"]
    paste0(sprintf("%s: วิเคราะห์ความแปรปรวนร่วม %d %s, %d %s (%s เป็น %s) ความแตกต่างระหว่าง %s %s ปฏิสัมพันธ์ %s × %s %s",
                   t, ii$Environments, el, ii$Genotypes, gl, el, if (isTRUE(r$env_random)) paste("random ทดสอบ", gl, "ด้วย MS ของ G×E") else "fixed ทดสอบด้วย pooled error",
                   gl, sig(pg), gl, el, sig(pge)),
           if (pge < 0.05) " จึงควรดูผลรายสถานที่ หรือวิเคราะห์ความเสถียร (เมนู GxE & stability)" else "",
           sprintf(" CV = %.1f%% LSD (5%%) ของค่าเฉลี่ย %s = %s %s ที่มีค่าเฉลี่ยรวมสูงสุดคือ %s (%s)", ii$`CV (%)`, gl, fmt_n(ii$`LSD (5%)`), gl, g$Genotype[which.max(g$Mean)], fmt_n(max(g$Mean))),
           if (bb$P < 0.05) sprintf(" ข้อควรระวัง: ความแปรปรวนของความคลาดเคลื่อนแต่ละ %s ไม่เท่ากัน (Bartlett p %s, MS ใหญ่สุด/เล็กสุด = %.1f) การรวมข้อมูลด้วย ANOVA จึงอาจไม่เหมาะสม ควรพิจารณาวิธี one-stage หรือ two-stage แบบถ่วงน้ำหนัก",
                                    el, if (bb$P < 0.0001) "< 0.0001" else sprintf("= %.4f", bb$P), bb$`Largest / smallest error MS`)
           else sprintf(" ความแปรปรวนของความคลาดเคลื่อนแต่ละ %s ไม่แตกต่างกัน (Bartlett test) จึงรวมข้อมูลวิเคราะห์ได้", el))
  }, ""), collapse = " ")
}
