# The Methods and About pages, in the style of RDstat: what each analysis does, the functions that do it and the
# references, by topic. Software references are built from the installed packages (package_ref). Keep this in step
# with the engine code: engine/R/pb_sea.R, pb_met.R, pb_compare.R, pb_gxe.R, pb_utils.R. Sourced by app.R.

# references of the statistical methods; a topic names them by the start of the text (method_ref)
method_refs <- c(
  "International Rice Research Institute (IRRI). PBTools: Plant Breeding Tools, version 1.1.",
  "Gomez KA, Gomez AA (1984). Statistical procedures for agricultural research, 2nd ed. John Wiley & Sons, New York.",
  "Bartlett MS (1937). Properties of sufficiency and statistical tests. Proceedings of the Royal Society of London A 160(901): 268-282. https://doi.org/10.1098/rspa.1937.0109",
  "Shapiro SS, Wilk MB (1965). An analysis of variance test for normality (complete samples). Biometrika 52(3-4): 591-611. https://doi.org/10.1093/biomet/52.3-4.591",
  "Cullis BR, Smith AB, Coombes NE (2006). On the design of early generation variety trials with correlated data. Journal of Agricultural, Biological, and Environmental Statistics 11(4): 381-393. https://doi.org/10.1198/108571106X154443",
  "Smith A, Cullis B, Gilmour A (2001). The analysis of crop variety evaluation data in Australia. Australian & New Zealand Journal of Statistics 43(2): 129-145. https://doi.org/10.1111/1467-842X.00163",
  "Tukey JW (1949). Comparing individual means in the analysis of variance. Biometrics 5(2): 99-114. https://doi.org/10.2307/3001913",
  "Dunnett CW (1955). A multiple comparison procedure for comparing several treatments with a control. Journal of the American Statistical Association 50(272): 1096-1121. https://doi.org/10.1080/01621459.1955.10501294",
  "Duncan DB (1955). Multiple range and multiple F tests. Biometrics 11(1): 1-42. https://doi.org/10.2307/3001478",
  "Fisher RA (1935). The design of experiments. Oliver and Boyd, Edinburgh.",
  "Finlay KW, Wilkinson GN (1963). The analysis of adaptation in a plant-breeding programme. Australian Journal of Agricultural Research 14(6): 742-754. https://doi.org/10.1071/AR9630742",
  "Eberhart SA, Russell WA (1966). Stability parameters for comparing varieties. Crop Science 6(1): 36-40. https://doi.org/10.2135/cropsci1966.0011183X000600010011x",
  "Shukla GK (1972). Some statistical aspects of partitioning genotype-environmental components of variability. Heredity 29(2): 237-245. https://doi.org/10.1038/hdy.1972.87",
  "Wricke G (1962). Über eine Methode zur Erfassung der ökologischen Streubreite in Feldversuchen. Zeitschrift für Pflanzenzüchtung 47: 92-96.",
  "Lin CS, Binns MR (1988). A superiority measure of cultivar performance for cultivar x location data. Canadian Journal of Plant Science 68(1): 193-198. https://doi.org/10.4141/cjps88-018",
  "Annicchiarico P (1992). Cultivar adaptation and recommendation from alfalfa trials in Northern Italy. Journal of Genetics and Breeding 46: 269-278.",
  "Purchase JL, Hatting H, van Deventer CS (2000). Genotype x environment interaction of winter wheat (Triticum aestivum L.) in South Africa: II. Stability analysis of yield performance. South African Journal of Plant and Soil 17(3): 101-107. https://doi.org/10.1080/02571862.2000.10634878",
  "Gauch HG (1992). Statistical analysis of regional yield trials: AMMI analysis of factorial designs. Elsevier, Amsterdam.",
  "Yan W, Kang MS (2003). GGE biplot analysis: a graphical tool for breeders, geneticists, and agronomists. CRC Press, Boca Raton.",
  "Olivoto T, Lucio AD, da Silva JAG, Marchioro VS, de Souza VQ, Jost E (2019a). Mean performance and stability in multi-environment trials I: Combining features of AMMI and BLUP techniques. Agronomy Journal 111(6): 2949-2960. https://doi.org/10.2134/agronj2019.03.0220",
  "Olivoto T, Lucio AD, da Silva JAG, Sari BG, Diel MI (2019b). Mean performance and stability in multi-environment trials II: Selection based on multiple traits. Agronomy Journal 111(6): 2961-2969. https://doi.org/10.2134/agronj2019.03.0221",
  "Olivoto T, Nardino M (2021). MGIDI: toward an effective multivariate selection in biological experiments. Bioinformatics 37(10): 1383-1389. https://doi.org/10.1093/bioinformatics/btaa981"
)
method_ref <- function(key) {
  hit <- method_refs[startsWith(method_refs, key)]
  if (length(hit) != 1) {
    stop("No single method reference starts with '", key, "'.", call. = FALSE)
  }
  hit
}

# the R packages, what they do here and the functions used (the "R package" table)
software_uses <- list(
  list(
    pkg = "lme4",
    use = "linear mixed model (REML): BLUE, BLUP, องค์ประกอบความแปรปรวน",
    fun = c("lmer()")
  ),
  list(
    pkg = "emmeans",
    use = "ค่าเฉลี่ยปรับแล้วและ SE, Tukey และ Dunnett (ค่าเริ่มต้น)",
    fun = c("emmeans()", "contrast()")
  ),
  list(
    pkg = "multcomp",
    use = "Tukey และ Dunnett แบบ exact",
    fun = c("glht()", "mcp()")
  ),
  list(
    pkg = "multcompView",
    use = "ตัวอักษรจัดกลุ่มค่าเฉลี่ย (Tukey)",
    fun = c("multcompLetters()")
  ),
  list(
    pkg = "agricolae",
    use = "LSD และ DMRT (Duncan)",
    fun = c("LSD.test()", "duncan.test()")
  ),
  list(
    pkg = "nlme",
    use = "Shukla (ความแปรปรวนแยกตามพันธุ์)",
    fun = c("gls()")
  ),
  list(
    pkg = "metan",
    use = "Finlay-Wilkinson, Eberhart-Russell, Shukla, ดัชนีเสถียรภาพ, AMMI, GGE, WAASB, MTSI, MGIDI",
    fun = c(
      "ge_reg()",
      "Shukla()",
      "ge_stats()",
      "performs_ammi()",
      "ammi_indexes()",
      "gge()",
      "waasb()",
      "mtsi()",
      "mgidi()"
    )
  ),
  list(
    pkg = "ggplot2",
    use = "กราฟและการบันทึกไฟล์ภาพ",
    fun = c("ggplot()", "ggsave()")
  ),
  list(
    pkg = "readxl",
    use = "อ่านไฟล์ Excel (.xlsx, .xls)",
    fun = c("read_excel()")
  ),
  list(pkg = "writexl", use = "บันทึกผลเป็นไฟล์ Excel", fun = c("write_xlsx()"))
)
cited_packages <- vapply(software_uses, `[[`, "", "pkg")
package_ref <- function(pkg) {
  ref <- tryCatch(
    paste(format(utils::citation(pkg), style = "text"), collapse = " "),
    error = function(e) {
      sprintf(
        "%s: R package%s.",
        pkg,
        tryCatch(
          paste(" version", utils::packageVersion(pkg)),
          error = function(e) ""
        )
      )
    }
  )
  gsub("[*_]", "", gsub("[[:space:]]+", " ", ref))
}
package_refs <- function() {
  unname(c(package_ref("base"), vapply(cited_packages, package_ref, "")))
}

# the groups of topics, in the order shown
method_groups <- c(
  sea = "วิเคราะห์สถานที่เดียว (Single environment)",
  met = "วิเคราะห์หลายสถานที่ (Multi-environment)",
  cmp = "การเปรียบเทียบค่าเฉลี่ย",
  gxe = "ปฏิสัมพันธ์ G×E และเสถียรภาพ",
  sel = "BLUP และการคัดเลือกหลายลักษณะ",
  data = "ข้อมูลและกราฟ"
)
# one topic: what the app does (Thai text in ...), the functions that do it ("pkg::fun()") and the keys of its references
method_topic <- function(
  id,
  title,
  group,
  ...,
  software = character(),
  refs = character()
) {
  list(
    id = id,
    title = title,
    group = group,
    body = tagList(...),
    software = software,
    refs = refs
  )
}

method_topics <- list(
  # ---- single environment ----
  method_topic(
    "sea_anova",
    "Standard ANOVA (RCBD)",
    "sea",
    software = c(
      "stats::lm()",
      "stats::anova()",
      "emmeans::emmeans()",
      "agricolae::LSD.test()",
      "agricolae::duncan.test()"
    ),
    refs = c("Gomez KA", "Fisher RA"),
    p(
      "การวิเคราะห์ความแปรปรวนแบบดั้งเดิมตามตำรามาตรฐานเกษตร (Gomez & Gomez, 1984) สำหรับแผนการทดลองแบบสุ่มในบล็อกสมบูรณ์ (RCBD) ด้วยโมเดลเชิงเส้น ",
      code("y = Block + Variety + Error"),
      " โดยกำหนดให้ทั้งบล็อกและพันธุ์เป็น Fixed effect ประมวลผลผ่านฟังก์ชัน Engine ",
      code("pb_sea(..., method = 'anova')")
    ),
    p(
      strong("ฟังก์ชันและวิธีการวิเคราะห์ทางสถิติ: "),
      tags$ul(
        tags$li(strong("การฟิตโมเดลและการทดสอบ ANOVA: "), "สร้างโมเดลเชิงเส้นด้วย ", code("stats::lm()"), " และคำนวณตาราง F-test ด้วย ", code("stats::anova()"), " (Sequential Sum of Squares / Type I SS) เพื่อทดสอบว่าพันธุ์มีความแตกต่างกันอย่างมีนัยสำคัญหรือไม่"),
        tags$li(strong("การคำนวณค่าเฉลี่ยและ SE: "), "คำนวณค่าเฉลี่ยของพันธุ์และค่าความคลาดเคลื่อนมาตรฐาน (SE) ด้วย ", code("emmeans::emmeans()")),
        tags$li(strong("การเปรียบเทียบค่าเฉลี่ยรายคู่: "), "คำนวณ Fisher's LSD ด้วย ", code("agricolae::LSD.test()"), " และ Duncan's Multiple Range Test ด้วย ", code("agricolae::duncan.test()"), " พร้อมแสดงตัวอักษรจัดกลุ่ม (Letter groups)")
      )
    ),
    p(
      strong("สิ่งที่ได้และการแปลผลสำหรับผู้ใช้งานทั่วไป: "),
      tags$ul(
        tags$li(strong("F-test (ANOVA Table): "), "หากค่า p-value < 0.05 แสดงว่าพันธุ์ต่างๆ ให้ผลผลิตแตกต่างกันจริง ไม่ใช่เพราะความบังเอิญ"),
        tags$li(strong("CV (%) (สัมประสิทธิ์ความแปรผัน): "), "ชี้วัดความแม่นยำในการจัดการแปลงทดลอง โดยทั่วไปในการทดลองข้าวหรือพืชไร่ ค่า CV ควรต่ำกว่า 10–15% หากค่า CV สูงเกิน 20% แสดงว่าแปลงทดลองมีความแปรปรวนจากสภาพแวดล้อมหรือการจัดการสูง"),
        tags$li(strong("LSD (Least Significant Difference): "), "ค่าความแตกต่างน้อยที่สุดที่มีนัยสำคัญทางสถิติ (5%) ใช้เป็นเกณฑ์ดูว่าผลต่างระหว่างสองพันธุ์ต้องห่างกันเท่าใดจึงจะถือว่าต่างกันจริง")
      )
    ),
    p(
      strong("ข้อควรระวังและข้อจำกัด: "),
      "Standard ANOVA เหมาะกับการทดลองที่มีข้อมูลครบถ้วนสมบูรณ์ (ทุกพันธุ์มีในทุกบล็อก) หากมีแปลงทดลองสูญหายหรือข้อมูลไม่สมดุล (Unbalanced data) ค่าเฉลี่ยดิบอาจเกิดความลำเอียง และวิธีนี้ไม่สามารถแยกองค์ประกอบความแปรปรวนทางพันธุกรรมหรือคำนวณอัตราพันธุกรรม (H²) ได้ (กรณีดังกล่าวแนะนำให้เปลี่ยนไปใช้ Mixed model)"
    )
  ),
  method_topic(
    "sea_mixed",
    "Mixed model: BLUE, BLUP และอัตราพันธุกรรม",
    "sea",
    software = c(
      "lme4::lmer()",
      "emmeans::emmeans()",
      "lme4::ranef()",
      "lme4::VarCorr()",
      "stats::anova()"
    ),
    refs = c("International Rice", "Cullis BR"),
    p(
      "โมเดลผสมเชิงเส้น (Linear Mixed Model): ประมาณค่าองค์ประกอบความแปรปรวนด้วยวิธี Restricted Maximum Likelihood (REML) ผ่าน ",
      code("lme4::lmer()"),
      " โดยกำหนดให้บล็อก (หรือพจน์ตามแผนการทดลอง) เป็น random effect เพื่อควบคุมความแปรปรวนของพื้นที่แปลงทดลอง ประมวลผลผ่านฟังก์ชัน Engine ",
      code("pb_sea(..., method = 'mixed')")
    ),
    p(
      strong("1. ค่าเฉลี่ยปรับแล้ว (BLUE - Best Linear Unbiased Estimator): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันที่ใช้: "), "กำหนดพันธุ์เป็น Fixed effect และคำนวณค่าเฉลี่ยปรับแล้วพร้อมค่า SE ด้วย ", code("emmeans::emmeans(m, geno, lmer.df = 'asymptotic')"), " และสกัดค่า SED จาก Covariance Matrix (", code("stats::vcov()"), ")"),
        tags$li(strong("ขจัดอิทธิพลของบล็อก/สภาพแวดล้อม: "), "ในการทดลองจริง แต่ละบล็อกมีความอุดมสมบูรณ์ไม่เท่ากัน ค่าเฉลี่ยทั่วไปอาจทำให้พันธุ์ที่บังเอิญอยู่ในบล็อกที่ดีมีผลผลิตสูงเกินจริง แต่ BLUE จะหักลบอิทธิพลของบล็อกออก ทำให้สะท้อนศักยภาพทางพันธุกรรมที่แท้จริงของพันธุ์"),
        tags$li(strong("รองรับข้อมูลไม่สมดุล (Unbalanced Data / Missing Plots): "), "เมื่อมีแปลงทดลองสูญหายหรือจำนวนซ้ำไม่เท่ากัน ค่าเฉลี่ยทั่วไปจะเกิดความลำเอียง (bias) แต่ BLUE เป็นตัวประมาณการเชิงเส้นที่ดีที่สุดและไม่อคติ จึงเปรียบเทียบระหว่างพันธุ์ได้อย่างเที่ยงตรงและเป็นธรรม"),
        tags$li(strong("รายงานค่าความคลาดเคลื่อนของผลต่าง (SED): "), "รายงานค่าความคลาดเคลื่อนมาตรฐานสำหรับเปรียบเทียบผลต่างระหว่างคู่พันธุ์ (Standard Error of Difference) ช่วยให้การทดสอบความแตกต่าง (Tukey, Dunnett) มีความแม่นยำและถูกต้องตามทฤษฎีสถิติ")
      )
    ),
    p(
      strong("2. ค่าทำนายทางพันธุกรรม (BLUP - Best Linear Unbiased Prediction): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันที่ใช้: "), "กำหนดพันธุ์เป็น Random effect ผ่าน ", code("lmer(y ~ (1|Block) + (1|Genotype))"), " สกัดค่า BLUP จาก intercept (", code("lme4::fixef()"), ") บวกอิทธิพลสุ่มของแต่ละพันธุ์ (", code("lme4::ranef()"), ")"),
        tags$li(strong("การทดสอบนัยสำคัญทางพันธุกรรม: "), "ทดสอบนัยสำคัญของความแปรปรวนระหว่างพันธุ์ (σ²g) ด้วย Likelihood Ratio Test (LRT) เปรียบเทียบแบบจำลองที่มีและไม่มีเทอมพันธุ์ ด้วย ", code("stats::anova(m0, m, refit = FALSE)")),
        tags$li(strong("การหดค่าเข้าหาค่าเฉลี่ย (Shrinkage Effect): "), "พันธุ์ที่มีข้อมูลน้อยหรือมีความผันแปรสูง ค่าจะถูกดึงเข้าหาค่าเฉลี่ยรวมส่วนกลาง ในขณะที่พันธุ์ที่มีข้อมูลซ้ำมากและแน่นอน ค่าจะใกล้เคียงค่าเฉลี่ยจริง ช่วยลดความเสี่ยงในการหลงคัดเลือกพันธุ์ที่ได้คะแนนสูงเพราะความบังเอิญ"),
        tags$li(strong("เหมาะอย่างยิ่งสำหรับการจัดอันดับเพื่อคัดเลือก (Selection): "), "BLUP สะท้อนถึงมูลค่าการผสมพันธุ์ (Breeding Value) หรือศักยภาพที่ถ่ายทอดได้ จึงเป็นค่ามาตรฐานที่ใช้จัดอันดับพันธุ์ในงานปรับปรุงพันธุ์พืช")
      )
    ),
    p(
      strong("3. อัตราพันธุกรรม (Broad-Sense Heritability, H²): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันที่ใช้: "), "สกัดค่าความแปรปรวนทางพันธุกรรม (σ²g) และความคลาดเคลื่อน (σ²e) ด้วย ", code("lme4::VarCorr()")),
        tags$li(strong("สูตรมาตรฐานฮาร์มอนิก: "), "คำนวณจาก H² = σ²g / (σ²g + σ²e / r) โดย r คือค่าเฉลี่ยฮาร์มอนิกของจำนวนซ้ำต่อพันธุ์ คำนวณผ่านฟังก์ชัน Engine ", code("pb_harmonic()")),
        tags$li(strong("เกณฑ์การแปลผล: "), "H² สูง (> 0.60) พันธุกรรมมีอิทธิพลสูง คัดเลือกได้ผลดี; H² ปานกลาง (0.30–0.60) ควรใช้ข้อมูลหลายสถานที่ช่วยคัดเลือก; H² ต่ำ (< 0.30) สิ่งแวดล้อมมีผลมาก ควรเพิ่มจำนวนซ้ำ"),
        tags$li(strong("ข้อมูลไม่สมดุล (Cullis Heritability): "), "ในกรณีที่มีแปลงทดลองสูญหายเป็นจำนวนมาก การประมาณค่าด้วยวิธีของ Cullis et al. (2006) จากความแปรปรวนของผลต่างระหว่าง BLUP จะมีความเหมาะสมและสะท้อนความแม่นยำในการคัดเลือกได้ดีกว่า")
      )
    )
  ),
  method_topic(
    "diag",
    "การตรวจสอบข้อตกลงเบื้องต้น (residuals)",
    "sea",
    software = c("stats::shapiro.test()"),
    refs = c("Shapiro SS"),
    p(
      "แท็บ Assumptions แสดงกราฟของ residual: residual กับค่าที่ทำนาย, normal Q-Q plot และ histogram ",
      "เพิ่มการทดสอบการแจกแจงปกติด้วย Shapiro-Wilk (",
      code("shapiro.test()"),
      "; Shapiro & Wilk, 1965) เมื่อข้อมูลมีจำนวนมาก การทดสอบนี้จะพบความเบี่ยงเบนเล็กน้อยได้ง่าย จึงควรดูร่วมกับกราฟ"
    )
  ),
  # ---- multi-environment ----
  method_topic(
    "met_anova",
    "Combined ANOVA (การวิเคราะห์ความแปรปรวนรวมหลายสถานที่)",
    "met",
    software = c(
      "stats::lm()",
      "stats::anova()",
      "stats::bartlett.test()",
      "emmeans::emmeans()"
    ),
    refs = c("Gomez KA", "Bartlett MS"),
    p(
      "รวมทุกสถานที่ในโมเดล y = สถานที่ + ซ้ำภายในสถานที่ + พันธุ์ + พันธุ์ × สถานที่ + ความคลาดเคลื่อน (",
      code("lm()"),
      ", ",
      code("anova()"),
      ") ตาม Gomez & Gomez (1984) ",
      "เมื่อสถานที่เป็น random (ค่าเริ่มต้น สถานที่เป็นตัวแทนของพื้นที่เป้าหมาย) ทดสอบสถานที่ด้วย MS ของซ้ำภายในสถานที่ และทดสอบพันธุ์ด้วย MS ของ G×E ",
      "เมื่อสถานที่เป็น fixed ทดสอบทุกเทอมด้วย pooled error ค่าเฉลี่ยพันธุ์ SE, LSD และการเปรียบเทียบค่าเฉลี่ยใช้ error term เดียวกับ F test ของพันธุ์"
    ),
    p(
      "ก่อนรวมข้อมูล ตรวจความเท่ากันของความแปรปรวนของความคลาดเคลื่อนของแต่ละสถานที่ด้วย Bartlett's test (",
      code("bartlett.test()"),
      "; Bartlett, 1937) และอัตราส่วน MS ใหญ่สุด/เล็กสุด ",
      "ถ้าไม่เท่ากัน (Bartlett มีนัยสำคัญ หรืออัตราส่วนเกินประมาณ 3) การรวมด้วย ANOVA อาจไม่เหมาะสม ควรใช้ one-stage หรือ two-weighted analysis แทน"
    )
  ),
  method_topic(
    "met_one",
    "One-stage (mixed model)",
    "met",
    software = c(
      "lme4::lmer()",
      "emmeans::emmeans()",
      "stats::anova()",
      "stats::predict()"
    ),
    refs = c("International Rice", "Smith A"),
    p(
      "การวิเคราะห์ข้อมูลหลายสถานที่แบบขั้นตอนเดียว (One-stage analysis) โดยรวมข้อมูลดิบทุกแปลงของทุกสถานที่เข้าสู่โมเดลผสมเดียว ผ่านฟังก์ชัน Engine ",
      code("pb_one_stage(..., method = 'mixed')")
    ),
    p(
      strong("ฟังก์ชันและวิธีการวิเคราะห์: "),
      tags$ul(
        tags$li(strong("ฟังก์ชันฟิตโมเดล: "), "ใช้ ", code("lme4::lmer()"), " กำหนดให้ สถานที่ (Env), บล็อกภายในสถานที่ (Rep%in%Env หรือ Block%in%Env), และปฏิสัมพันธ์พันธุ์ × สถานที่ (Geno:Env) เป็น random effects"),
        tags$li(strong("ประมาณค่าเฉลี่ยพันธุ์ (BLUE / BLUP): "), "เมื่อพันธุ์เป็น Fixed ได้ค่าเฉลี่ยรวมทุกสถานที่ (BLUE) และ SE ด้วย ", code("emmeans::emmeans()"), "; เมื่อพันธุ์เป็น Random ได้ค่า BLUP และอัตราพันธุกรรมรวมทุกสถานที่"),
        tags$li(strong("การทดสอบ G×E Interaction: "), "ทดสอบนัยสำคัญของปฏิสัมพันธ์พันธุ์ × สถานที่ ด้วย Likelihood Ratio Test (LRT) ผ่าน ", code("stats::anova(m0, m, refit = FALSE)")),
        tags$li(strong("ค่าเฉลี่ยพันธุ์ในแต่ละสถานที่ (G×E Means): "), "ทำนายค่าเฉลี่ยรายสถานที่โดยไม่รวมอิทธิพลของบล็อกด้วย ", code("stats::predict(m, re.form = ~ (1|Env) + (1|Geno:Env))"))
      )
    )
  ),
  method_topic(
    "met_two",
    "Two-stage (mixed model)",
    "met",
    software = c("lme4::lmer()", "emmeans::emmeans()"),
    refs = c("International Rice", "Smith A"),
    p(
      "การวิเคราะห์ข้อมูลหลายสถานที่แบบสองขั้นตอน (Two-stage analysis) เหมาะสำหรับกรณีที่มีสถานที่ทดลองจำนวนมาก หรือความแม่นยำของแต่ละสถานที่ต่างกันมาก ผ่านฟังก์ชัน Engine ",
      code("pb_two_stage()")
    ),
    p(
      strong("ฟังก์ชันและขั้นตอนการวิเคราะห์: "),
      tags$ul(
        tags$li(strong("ขั้นที่ 1 (Single-site BLUEs): "), "วิเคราะห์ข้อมูลทีละสถานที่ด้วย ", code("lme4::lmer()"), " โดยกำหนดพันธุ์เป็น Fixed effect เพื่อสกัดค่าเฉลี่ยปรับแล้ว (BLUE) และ Standard Error (SE) ผ่านฟังก์ชัน Engine ", code("pb_stage1()")),
        tags$li(strong("ขั้นที่ 2 (Combined Stage-2 Model): "), "นำค่าเฉลี่ยจากขั้นที่ 1 มารวมกันในโมเดลผสมด้วย ", code("lme4::lmer(Mean ~ Geno + (1|Env), weights = 1/SE^2)"), " เพื่อให้สถานที่ที่มีความแม่นยำสูง (SE ต่ำ) มีน้ำหนักในการประเมินมากกว่า (Smith et al., 2001)")
      )
    )
  ),
  # ---- mean comparisons ----
  method_topic(
    "tukey",
    "Tukey และ Dunnett",
    "cmp",
    software = c(
      "emmeans::emmeans()",
      "emmeans::contrast()",
      "multcomp::glht()",
      "multcompView::multcompLetters()"
    ),
    refs = c("Tukey JW", "Dunnett CW"),
    p(
      "simultaneous confidence intervals ของผลต่างระหว่างพันธุ์ โดยวิธี Tukey ใช้สำหรับการเปรียบเทียบค่าเฉลี่ยระหว่างพันธุ์ทุกคู่ (Tukey, 1949) ส่วนวิธี Dunnett ใช้สำหรับการเปรียบเทียบค่าเฉลี่ยของพันธุ์ทดสอบกับพันธุ์ควบคุมหรือพันธุ์ตรวจสอบ (Dunnett, 1955) โดยผลต่างระหว่างค่าเฉลี่ยถือว่ามีนัยสำคัญทางสถิติเมื่อช่วงความเชื่อมั่นพร้อมกันของผลต่างไม่ครอบคลุมค่าศูนย์ ",
      "ค่าเริ่มต้น ใช้ ",
      code("emmeans()"),
      " (Tukey-Kramer, Dunnett แบบประมาณ) ซึ่งเร็ว และเท่ากับวิธี exact เมื่อข้อมูลสมดุล (",
      code("glht()"),
      ") ",
      "ตัวอักษรจัดกลุ่มจาก ",
      code("multcompLetters()"),
      " พันธุ์ที่มีตัวอักษรร่วมกันไม่แตกต่างกัน"
    )
  ),
  method_topic(
    "lsd_dmrt",
    "LSD และ DMRT (Duncan)",
    "cmp",
    software = c("agricolae::LSD.test()", "agricolae::duncan.test()"),
    refs = c("Fisher RA", "Duncan DB"),
    p(
      "วิธีการเปรียบเทียบแบบดั้งเดิมของ Standard ANOVA และ Combined ANOVA ได้แก่ Fisher’s LSD (",
      code("LSD.test()"),
      " จาก agricolae paclage ซึ่งไม่ปรับค่า p สำหรับการเปรียบเทียบหลายคู่) ส่วนวิธี Duncan's multiple range test (",
      code("duncan.test()"),
      "; Duncan, 1955) ให้ตัวอักษรจัดกลุ่มเรียงตามค่าเฉลี่ย ใช้ MS และ df ของ error term จากโมเดล ANOVA ",
      " ข้อเสนอแนะ: ทั้งสองวิธีพบความแตกต่างได้มากกว่า Tukey เพราะไม่ควบคุมความผิดพลาดของการเปรียบเทียบทั้งชุด วารสารหลายแห่งจึงแนะนำ Tukey หรือ Dunnett"
    )
  ),
  # ---- GxE and stability ----
  method_topic(
    "fw_er",
    "Finlay-Wilkinson และ Eberhart-Russell",
    "gxe",
    software = c("metan::ge_reg()"),
    refs = c("Finlay KW", "Eberhart SA"),
    p(
      "วิเคราะห์การตอบสนองและเสถียรภาพของพันธุ์ด้วยโมเดลการถดถอยเชิงเส้น (Joint Regression Analysis) ผ่านฟังก์ชัน ",
      code("metan::ge_reg()"),
      " และฟังก์ชัน Engine ",
      code("pb_fw()"),
      " และ ",
      code("pb_er()"),
      " โดยถดถอยค่าเฉลี่ยของแต่ละพันธุ์กับดัชนีสภาพแวดล้อม (Environmental Index = ค่าเฉลี่ยสถานที่ − ค่าเฉลี่ยรวม): "
    ),
    p(
      strong("วิธี Finlay-Wilkinson (1963): "),
      "ประเมินค่าความชัน (Slope; b₁) เพื่อจำแนกรูปแบบการตอบสนองต่อสภาพแวดล้อม: ",
      tags$ul(
        tags$li(strong("b₁ ≈ 1.0 (เสถียรภาพระดับเฉลี่ย): "), "พันธุ์มีการตอบสนองต่อสภาพแวดล้อมตามมาตรฐานทั่วไป ให้ผลผลิตเพิ่มขึ้นตามศักยภาพของพื้นที่อย่างสม่ำเสมอ"),
        tags$li(strong("b₁ > 1.0 (ตอบสนองดีในพื้นที่อุดมสมบูรณ์): "), "พันธุ์ที่ให้ผลผลิตเพิ่มขึ้นอย่างมากเมื่อปลูกในสภาพแวดล้อมที่ดี มีการให้น้ำและปุ๋ยเต็มที่ (High responsiveness / เหมาะกับพื้นที่เกษตรปราณีต)"),
        tags$li(strong("b₁ < 1.0 (ทนทานในสภาพแวดล้อมจำกัด): "), "พันธุ์ที่ให้ผลผลิตคงที่แม้ปลูกในสภาพแวดล้อมแย่หรือแห้งแล้ง (Stress tolerance / เหมาะกับพื้นที่เสี่ยง)")
      )
    ),
    p(
      strong("วิธี Eberhart-Russell (1966): "),
      "ประเมินทั้งค่าความชัน (b₁) และค่าความแปรปรวนที่เบี่ยงเบนจากเส้นถดถอย (Deviation from regression; s²di) พร้อมทั้งวิเคราะห์ ANOVA ของ joint regression: ",
      tags$ul(
        tags$li(strong("พันธุ์ที่มีเสถียรภาพสูง (Stable): "), "ต้องมีค่าความชัน b₁ ไม่แตกต่างจาก 1.0 และมีค่า s²di ไม่แตกต่างจาก 0 (p > 0.05) แสดงว่าผลผลิตเพิ่มตามศักยภาพพื้นที่และคาดการณ์ผลผลิตได้อย่างแม่นยำ"),
        tags$li(strong("ค่า s²di > 0 (ทำนายผลได้ยาก): "), "พันธุ์มีความผันผวนสูง ผลผลิตไม่เป็นไปตามเส้นถดถอย ตอบสนองต่อสภาพแวดล้อมไม่แน่นอน")
      )
    )
  ),
  method_topic(
    "shukla",
    "Shukla's stability variance",
    "gxe",
    software = c("metan::Shukla()", "nlme::gls()"),
    refs = c("Shukla GK", "International Rice"),
    p(
      "ประเมินเสถียรภาพของแต่ละพันธุ์จากสัดส่วนความแปรปรวนของปฏิสัมพันธ์ G×E ที่มาจากพันธุ์นั้นๆ โดยตรง ผ่านฟังก์ชัน Engine ",
      code("pb_shukla()"),
      " ซึ่งรองรับ 2 วิธีการคำนวณ: ",
      code("metan::Shukla()"),
      " (สูตรดั้งเดิมของ Shukla, 1972) และ ",
      code("nlme::gls(..., weights = varIdent(form = ~ 1 | Genotype))"),
      " (สูตร REML แยกความแปรปรวนตามพันธุ์ตามแนวทางของ PBTools): ",
      tags$ul(
        tags$li(strong("ค่าความแปรปรวนเสถียรภาพ (σ²i): "), "ประเมินสัดส่วนของความแปรปรวนจาก G×E interaction ที่มาจากแต่ละพันธุ์ โดยค่าน้อยหมายถึงมีเสถียรภาพสูง พร้อมทั้งมี F-test ทดสอบนัยสำคัญ"),
        tags$li(strong("ผลรวมลำดับของ Kang (Kang's sum of ranks): "), "บูรณาการอันดับของผลผลิตเฉลี่ยและอันดับของเสถียรภาพเข้าด้วยกัน ช่วยคัดเลือกพันธุ์ที่ทั้งให้ผลผลิตดีและมีเสถียรภาพสูงพร้อมกัน")
      )
    )
  ),
  method_topic(
    "stab_stats",
    "ดัชนีเสถียรภาพหลายแบบ",
    "gxe",
    software = c("metan::ge_stats()"),
    refs = c("Wricke G", "Lin CS", "Annicchiarico P", "Purchase JL"),
    p(
      "ใช้ฟังก์ชัน  ",
      code("ge_stats()"),
      " เพื่อคำนวณดัชนีเสถียรภาพหลายแบบในตารางเดียว ได้แก่ ecovalence ของ Wricke (1962), ความแปรปรวนเสถียรภาพของ Shukla, วิธี Eberhart-Russell, ดัชนีความเชื่อมั่นของ Annicchiarico (1992), superiority ของ Lin & Binns (1988), ",
      "AMMI stability value (ASV; Purchase et al., 2000) และ WAASB พร้อมทั้งลำดับของแต่ละดัชนีเพื่อช่วยในการจัดอันดับพันธุ์"
    )
  ),
  method_topic(
    "ammi",
    "AMMI",
    "gxe",
    software = c(
      "metan::performs_ammi()",
      "metan::ammi_indexes()",
      "metan::plot_scores()"
    ),
    refs = c("Gauch HG", "Purchase JL"),
    p(
      "วิเคราะห์ Additive Main Effects and Multiplicative Interaction (AMMI) เพื่อแยกองค์ประกอบของปฏิสัมพันธ์ G×E ด้วยวิธี Singular Value Decomposition (SVD) ผ่านฟังก์ชัน Engine ",
      code("pb_ammi()"),
      " โดยใช้ ",
      code("metan::performs_ammi()"),
      " (Gauch, 1992) ทดสอบนัยสำคัญของแต่ละ Principal Component (PC) ด้วย F-test และแสดงผลผ่าน Biplot: "
    ),
    tags$ul(
      tags$li(strong("AMMI1 (ค่าเฉลี่ย vs. PC1): "), "แกนนอนคือผลผลิตเฉลี่ย แกนตั้งคือคะแนน PC1 จุดที่อยู่ใกล้เส้นศูนย์ (PC1 ≈ 0) แสดงถึงพันธุ์ที่มีเสถียรภาพสูง (ปฏิสัมพันธ์ต่ำ) ส่วนจุดที่อยู่ห่างจากศูนย์มากจะตอบสนองต่อสภาพแวดล้อมเฉพาะ"),
      tags$li(strong("AMMI2 (PC1 vs. PC2): "), "พล็อตด้วย ", code("metan::plot_scores()"), " พันธุ์ที่อยู่ใกล้จุดกำเนิด (0,0) มีเสถียรภาพสูงในทุกมิติ ส่วนพันธุ์และสถานที่ที่ชี้ไปในทิศทางเดียวกัน (มุมแคบ) แสดงถึงความจำเพาะและการปรับตัวเข้ากันได้ดี"),
      tags$li(strong("ดัชนีเสถียรภาพ ASV และ ssiASV: "), "คำนวณ AMMI Stability Value (ASV) และดัชนีผลผลิต-เสถียรภาพ (ssiASV) เพื่อจัดอันดับพันธุ์ที่โดดเด่นทั้งผลผลิตและเสถียรภาพ ผ่าน ", code("metan::ammi_indexes()"))
    )
  ),
  method_topic(
    "gge",
    "GGE biplot",
    "gxe",
    software = c("metan::gge()", "metan::plot.gge()"),
    refs = c("Yan W"),
    p(
      "วิเคราะห์ Genotype + Genotype × Environment (GGE) biplot เพื่อประเมินผลของพันธุ์และปฏิสัมพันธ์ไปพร้อมกัน ผ่านฟังก์ชัน Engine ",
      code("pb_gge()"),
      " โดยใช้ ",
      code("metan::gge()"),
      " (Yan & Kang, 2003) ซึ่งปรับข้อมูลด้วยค่าเฉลี่ยของสถานที่และกระจายด้วยวิธี SVD พร้อมแสดงกราฟวิเคราะห์ครบ 10 รูปแบบผ่าน ",
      code("metan::plot.gge()"),
      " โดยมีฟังก์ชันเด่นคือ:"
    ),
    tags$ul(
      tags$li(strong("Which-Won-Where (ใครชนะที่ไหน): "), "วาดรูปหลายเหลี่ยมล้อมรอบกลุ่มพันธุ์เพื่อจำแนกกลุ่มสภาพแวดล้อม (Mega-environments) และระบุว่าพันธุ์ใดให้ผลผลิตสูงสุดในแต่ละกลุ่มสภาพแวดล้อม"),
      tags$li(strong("Mean vs. Stability (ค่าเฉลี่ยและเสถียรภาพ): "), "ประเมินผลผลิตตามแกนสภาพแวดล้อมเฉลี่ย (Average-Environment Axis; AEA) และประเมินเสถียรภาพจากระยะห่างตั้งฉากกับแกน AEA (ยิ่งใกล้แกน = เสถียรภาพสูง)"),
      tags$li(strong("Ideal Genotype & Environment: "), "แสดงวงกลมศูนย์กลางเปรียบเทียบระยะห่างของแต่ละพันธุ์กับ 'พันธุ์ในอุดมคติ' (Ideal Genotype ที่มีผลผลิตสูงและเสถียรภาพสูงสุด)")
    )
  ),
  # ---- BLUP and multi-trait selection ----
  method_topic(
    "waasb",
    "WAASB และ WAASBY (โมเดลเสถียรภาพยุคใหม่)",
    "sel",
    software = c("metan::waasb()", "metan::plot_scores()"),
    refs = c("Olivoto T, Lucio AD, da Silva JAG, Marchioro"),
    p(
      "โมเดลเสถียรภาพยุคใหม่ที่ผสานจุดเด่นของ Linear Mixed Model (BLUP) เข้ากับ Multivariate Analysis (AMMI) เพื่อแก้ข้อจำกัดของ AMMI ดั้งเดิมที่มักพิจารณาเฉพาะแกน PC1 และ PC2 เท่านั้น คำนวณผ่านฟังก์ชัน ",
      code("metan::waasb()"),
      " (Olivoto et al., 2019): "
    ),
    p(
      strong("1. สถิติเสถียรภาพ WAASB (Weighted Average of Absolute Scores): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันและหลักการ: "), "วิเคราะห์โมเดลผสมด้วย ", code("lme4"), " สกัดเมทริกซ์ BLUP ของ G×E ออกมากระจายด้วย SVD และคำนวณค่าเฉลี่ยถ่วงน้ำหนักของคะแนน Interaction Principal Component (IPCA) ทุกแกน โดยถ่วงน้ำหนักตามสัดส่วนความแปรปรวนที่แต่ละแกนอธิบายได้จริงผ่าน ", code("metan::waasb()")),
        tags$li(strong("การแปลผล: "), "พันธุ์ที่มีค่า WAASB ต่ำ แสดงถึงปฏิสัมพันธ์กับสภาพแวดล้อมต่ำ จึงมีเสถียรภาพสูงและปรับตัวได้กว้างขวาง (Wide adaptability ปลูกที่ไหนก็ให้ผลสม่ำเสมอ)")
      )
    ),
    p(
      strong("2. ดัชนีคัดเลือกพร้อมกัน WAASBY (Simultaneous Selection Index): "),
      tags$ul(
        tags$li(strong("บูรณาการผลผลิตและเสถียรภาพ: "), "นำค่าเฉลี่ยผลผลิต (Y) และคะแนนเสถียรภาพ (WAASB) มาแปลงเป็นสเกล 0–100 แล้วรวมกันเป็นดัชนีเดียว ช่วยตอบคำถามว่าพันธุ์ใดดีที่สุดเมื่อคิดทั้งผลผลิตและความสม่ำเสมอ"),
        tags$li(strong("ปรับน้ำหนักได้ตามเป้าหมาย (Weight of Mean %): "), "ผู้ใช้สามารถเลื่อนแถบน้ำหนักในขั้นตอน Selection เพื่อปรับเกณฑ์ตามพื้นที่เป้าหมาย: ",
          tags$ul(
            tags$li("เน้นผลผลิตสูง (เช่น น้ำหนักผลผลิต 65–80% : เสถียรภาพ 20–35%): เหมาะกับพื้นที่ชลประทานหรือเขตเกษตรปราณีตที่มีการดูแลจัดการดี"),
            tags$li("เน้นเสถียรภาพสูง (เช่น น้ำหนักผลผลิต 50% : เสถียรภาพ 50%): เหมาะกับพื้นที่เกษตรน้ำฝน พื้นที่แห้งแล้ง หรือเขตที่มีความเสี่ยงต่อการเปลี่ยนแปลงสภาพภูมิอากาศ")
          )
        ),
        tags$li(strong("แผนภาพ 4 จตุภาค (Four Quadrants): "), "พล็อตด้วย ", code("metan::plot_scores()"), " เพื่อจำแนกกลุ่มพันธุ์: ",
          tags$ul(
            tags$li("จตุภาค IV (กลุ่มเด่น): ผลผลิตสูงกว่าค่าเฉลี่ย และมีเสถียรภาพสูง (WAASB ต่ำ) เหมาะแนะนำให้ปลูกอย่างกว้างขวาง"),
            tags$li("จตุภาค II: ผลผลิตสูง แต่เสถียรภาพต่ำ แนะนำเฉพาะพื้นที่เฉพาะที่ตอบสนองดี"),
            tags$li("จตุภาค III: เสถียรภาพสูง แต่ผลผลิตต่ำ"),
            tags$li("จตุภาค I: ผลผลิตต่ำและไม่เสถียร ควรคัดทิ้ง")
          )
        )
      )
    )
  ),
  method_topic(
    "mtsi",
    "MTSI และ MGIDI (การคัดเลือกหลายลักษณะพร้อมกัน)",
    "sel",
    software = c(
      "metan::waasb()",
      "metan::mtsi()",
      "metan::gmd()",
      "metan::mgidi()"
    ),
    refs = c("Olivoto T, Lucio AD, da Silva JAG, Sari", "Olivoto T, Nardino M"),
    p(
      "ในงานปรับปรุงพันธุ์พืชจริง นักวิจัยมักต้องการคัดเลือกสายพันธุ์ที่มีลักษณะดีเด่นหลายด้านพร้อมกัน (เช่น ผลผลิตสูง ต้นเตี้ย วันออกดอกพอเหมาะ ต้านทานโรค) แต่ลักษณะเหล่านี้มักมีความสัมพันธ์เชื่อมโยงหรือขัดแย้งกัน การคัดเลือกทีละลักษณะมักไม่ได้ผลดี จึงต้องใช้ดัชนีคัดเลือกหลายลักษณะ (Multi-trait selection index) ที่แก้ปัญหาความสัมพันธ์ซ้ำซ้อน (Multicollinearity):"
    ),
    p(
      strong("1. ดัชนี MTSI (Multi-Trait Stability Index): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันที่ใช้: "), "คำนวณผ่าน ", code("metan::mtsi()"), " โดยใช้ค่าดัชนี WAASBY ของแต่ละลักษณะ (ที่คำนวณจาก ", code("metan::waasb()"), ") เป็นตัวแปรนำเข้า จึงเป็นการคัดเลือกที่บูรณาการทั้ง 'ผลผลิตและเสถียรภาพ' ของทุกลักษณะไปพร้อมกัน เหมาะกับการทดลองหลายสถานที่ (Multi-environment trials)"),
        tags$li(strong("การแปลผล: "), "จัดกลุ่มลักษณะด้วย Factor Analysis แล้วสร้าง 'พันธุ์ในอุดมคติ (Ideotype)' ขึ้นมาเปรียบเทียบ พันธุ์ที่มีค่า MTSI ต่ำที่สุดคือพันธุ์ที่อยู่ใกล้เคียงพันธุ์อุดมคติมากที่สุด")
      )
    ),
    p(
      strong("2. ดัชนี MGIDI (Multi-Trait Genotype-Ideotype Distance Index): "),
      tags$ul(
        tags$li(strong("ฟังก์ชันที่ใช้: "), "คำนวณผ่าน ", code("metan::mgidi()"), " โดยใช้ค่าศักยภาพทางพันธุกรรม (BLUP) สกัดผ่าน ", code("metan::gmd()"), " หรือใช้ค่าเฉลี่ยของพันธุ์โดยตรง เหมาะสำหรับการคัดเลือกหลายลักษณะในการทดลองสถานที่เดียว หรือในขั้นตอนการคัดเลือกรุ่นแรกๆ ที่ยังไม่ได้เน้นเสถียรภาพข้ามสถานที่"),
        tags$li(strong("การแปลผล: "), "พันธุ์ที่มีค่า MGIDI ต่ำที่สุดคือพันธุ์ที่เด่นครบทุกมิติตามเป้าหมายการปรับปรุงพันธุ์")
      )
    ),
    p(
      strong("3. จุดแข็งและจุดอ่อนของพันธุ์ที่ได้รับคัดเลือก (Strengths & Weaknesses): "),
      "ระบบจะแสดงแผนภูมิแจกแจงสัดส่วนการมีส่วนร่วมของแต่ละปัจจัย ทำให้ทราบทันทีว่าแต่ละพันธุ์ที่ได้รับคัดเลือก ",
      strong("เด่นในปัจจัยใด (จุดแข็ง)"), " และ ", strong("ยังมีข้อด้อยในปัจจัยใด (จุดอ่อน)"),
      " เพื่อนำไปใช้วางแผนการจัดการแปลงหรือกำหนดคู่ผสมในรอบถัดไปได้อย่างแม่นยำ พร้อมทั้งรายงานค่าผลต่างการคัดเลือก (Selection Differential) ของทุกลักษณะ"
    )
  ),
  # ---- data and plots ----
  method_topic(
    "desc",
    "สถิติเชิงพรรณนา",
    "data",
    software = c("base::mean()", "stats::sd()"),
    p(
      "จำนวนข้อมูลที่ไม่สูญหาย ค่าเฉลี่ย (",
      code("mean()"),
      ") และส่วนเบี่ยงเบนมาตรฐาน (",
      code("sd()"),
      ", ตัวหาร n − 1) ด้วย R ",
      "Single environment แยกตามสถานที่ One-stage และ Combined ANOVA รวมทุกแปลงของทุกสถานที่ Two-stage ใช้ค่าเฉลี่ยขั้นที่ 1 ของทุกสถานที่"
    )
  ),
  method_topic(
    "files",
    "การอ่านไฟล์ กราฟ และการบันทึกผล",
    "data",
    software = c(
      "readxl::read_excel()",
      "utils::read.csv()",
      "ggplot2::ggplot()",
      "ggplot2::ggsave()",
      "writexl::write_xlsx()"
    ),
    p(
      "อ่าน .csv, .txt (คั่นด้วย tab), .xlsx และ .xls (ชีตแรก, ",
      code("read_excel()"),
      ") ช่องว่าง NA และ . นับเป็นค่าสูญหาย ",
      "กราฟแสดงแบบ Live View ตามสัดส่วนของไฟล์ที่จะดาวน์โหลด บันทึกเป็น PNG, SVG หรือ PDF (",
      code("ggsave()"),
      ") ตารางบันทึกเป็น Excel (",
      code("write_xlsx()"),
      ") ",
      "และรายงาน Word ที่มีการตั้งค่า วิธีการ ตาราง กราฟ และเอกสารอ้างอิง"
    )
  )
)

# the R packages a topic uses besides base R, from its "pkg::fun()" entries
topic_pkgs <- function(t) {
  setdiff(
    unique(sub("::.*", "", grep("^[A-Za-z0-9.]+::", t$software, value = TRUE))),
    c("base", "stats", "utils")
  )
}
# R and each package with the version installed now
topic_software <- function(t) {
  if (length(t$software)) {
    c(
      paste("R", paste(R.version$major, R.version$minor, sep = ".")),
      vapply(topic_pkgs(t), function(p) trimws(paste(p, pkg_ver(p))), "")
    )
  }
}
# the references of a topic: R and its packages (from citation()) and the methods
topic_refs <- function(t) {
  unname(sort(unique(c(
    if (length(t$software)) package_ref("base"),
    vapply(topic_pkgs(t), package_ref, ""),
    vapply(t$refs, method_ref, "")
  ))))
}
topic_copy_text <- function(t) {
  paste(
    c(
      t$title,
      if (length(t$software)) {
        c(
          paste("Software:", paste(topic_software(t), collapse = ", ")),
          paste("Functions:", paste(t$software, collapse = "; "))
        )
      },
      "",
      "References",
      topic_refs(t)
    ),
    collapse = "\n"
  )
}
topic_ui <- function(t) {
  refs <- topic_refs(t)
  bslib::accordion_panel(
    t$title,
    value = t$id,
    t$body,
    if (length(t$software)) {
      div(
        class = "small mb-2",
        p(
          class = "mb-1",
          strong("ซอฟต์แวร์ที่ใช้: "),
          paste(topic_software(t), collapse = ", ")
        ),
        p(
          class = "mb-1",
          strong("ฟังก์ชัน: "),
          lapply(seq_along(t$software), function(i) {
            tagList(if (i > 1) "; ", code(t$software[i]))
          })
        )
      )
    },
    if (length(refs)) {
      tagList(
        h6("เอกสารอ้างอิงของหัวข้อนี้"),
        tags$ul(class = "small", lapply(refs, tags$li)),
        actionButton(
          paste0("cp_topic_", t$id),
          tagList(icon("copy"), " คัดลอกซอฟต์แวร์และเอกสารอ้างอิงของหัวข้อนี้"),
          class = "btn-sm"
        )
      )
    }
  )
}
# the topics in their groups, each group an accordion that starts closed
methods_topics_ui <- function() {
  tagList(lapply(names(method_groups), function(g) {
    ts <- Filter(function(t) t$group == g, method_topics)
    tagList(
      h4(class = "mt-4", method_groups[[g]]),
      do.call(
        bslib::accordion,
        c(
          lapply(ts, topic_ui),
          list(id = paste0("methods_", g), open = FALSE, multiple = TRUE)
        )
      )
    )
  }))
}
# the "R package" table: package, version, what it does here, functions
methods_packages_ui <- function() {
  tagList(
    div(
      class = "table-responsive",
      tags$table(
        class = "table table-sm table-striped small",
        tags$thead(tags$tr(
          tags$th("Package"),
          tags$th("รุ่น"),
          tags$th("ใช้ทำอะไร"),
          tags$th("ฟังก์ชัน")
        )),
        tags$tbody(lapply(software_uses, function(s) {
          tags$tr(
            tags$td(strong(s$pkg)),
            tags$td(pkg_ver(s$pkg)),
            tags$td(s$use),
            tags$td(lapply(seq_along(s$fun), function(i) {
              tagList(if (i > 1) ", ", code(s$fun[i]))
            }))
          )
        }))
      )
    ),
    h4("เอกสารอ้างอิงของซอฟต์แวร์"),
    tags$ul(lapply(package_refs(), tags$li)),
    actionButton("cp_refs", "คัดลอกเอกสารอ้างอิง"),
    p(
      class = "text-muted",
      sprintf(
        "รุ่นที่ใช้ในการใช้งานครั้งนี้: %s %s, %s",
        app_name,
        version_label(),
        R.version.string
      )
    )
  )
}

methods_ui <- div(
  class = "container",
  style = "max-width: 820px;",
  h3("Methods"),
  p(
    "โปรแกรมวิเคราะห์ Phenotypic Analysis เท่านั้น คำนวณด้วย R และ packages ที่เป็นที่ยอมรับบน CRAN แล้ว",
    "โดยแสดงชื่อ package รุ่น และฟังก์ชันที่ใช้ เพื่อให้อ้างอิงได้เมื่อตีพิมพ์"
  ),
  h3("R package"),
  uiOutput("methods_text"),
  h3("วิธีการวิเคราะห์ของโปรแกรม"),
  p(
    "เลือกหัวข้อที่ตรงกับการวิเคราะห์ที่ใช้ แต่ละหัวข้ออธิบายว่าการวิเคราะห์และฟังก์ชันที่ใช้ และเอกสารอ้างอิงเอกสาร"
  ),
  uiOutput("methods_topics"),
  h3("เอกสารอ้างอิงของวิธีการทางสถิติ"),
  tags$ul(lapply(method_refs, tags$li)),
  actionButton("cp_mrefs", "คัดลอกเอกสารอ้างอิงของวิธีการ")
)

cite_app <- sprintf(
  "Jaisong K (%s). %s: analysis of plant breeding trials in the browser, in the spirit of IRRI PBTools. Version %s. https://kanthjs.github.io/RBTool/",
  if (anyNA(app_version)) {
    format(Sys.Date(), "%Y")
  } else {
    substr(app_version[["built"]], 1, 4)
  },
  app_name,
  if (anyNA(app_version)) "development build" else app_version[["version"]]
)
cite_packages <- c(
  "ทุกการวิเคราะห์: R Core Team. R: A language and environment for statistical computing. R Foundation for Statistical Computing, Vienna, Austria. https://www.R-project.org/",
  "Single environment, multi-environment (BLUEs/BLUPs, variance components): Bates D, Maechler M, Bolker B, Walker S (2015). Fitting linear mixed-effects models using lme4. Journal of Statistical Software 67(1): 1-48. doi:10.18637/jss.v067.i01",
  "ค่าเฉลี่ยและการเปรียบเทียบค่าเฉลี่ย (ค่าเริ่มต้น): Lenth RV, Piaskowski J. emmeans: Estimated marginal means, aka least-squares means. R package. doi:10.32614/CRAN.package.emmeans",
  "การเปรียบเทียบค่าเฉลี่ยแบบ exact (เหมือน PBTools): Hothorn T, Bretz F, Westfall P (2008). Simultaneous inference in general parametric models. Biometrical Journal 50(3): 346-363. doi:10.1002/bimj.200810425",
  "ตัวอักษรจัดกลุ่มค่าเฉลี่ย: Graves S, Piepho HP, Selzer L. multcompView: Visualizations of paired comparisons. R package. doi:10.32614/CRAN.package.multcompView",
  "Shukla แบบ PBTools: Pinheiro J, Bates D, R Core Team. nlme: Linear and nonlinear mixed effects models. R package. doi:10.32614/CRAN.package.nlme",
  "Finlay-Wilkinson, Eberhart-Russell, Shukla, ดัชนีเสถียรภาพ, AMMI, GGE: Olivoto T, Lucio AD (2020). metan: An R package for multi-environment trial analysis. Methods in Ecology and Evolution 11(6): 783-789. doi:10.1111/2041-210X.13384"
)

about_ui <- div(
  class = "container",
  style = "max-width: 820px;",
  h3("About"),
  p(sprintf(
    "%s (Rice Breeding Tool) เป็น interactive web application (apps) ที่สร้างโดย R จาก Shiny package เพื่อวิเคราะห์ข้อมูลการทดลองปรับปรุงพันธุ์พืช โดยมีการนำแนวทางการวิเคราะห์จากโปรแกรม PBTools (Plant Breeding Tools) ของสถาบันวิจัยข้าวนานาชาติ (IRRI) เป็นแอปเว็บที่รัน R ภายในเบราว์เซอร์ได้ทันที",
    app_name
  )),
  div(
    class = "alert alert-warning",
    strong("รุ่นทดลอง (beta) "),
    "ผลลัพธ์ตรวจเทียบกับ PBTools แล้ว แต่อยู่ระหว่างการพัฒนา ผลที่สำคัญควรตรวจสอบด้วยโปรแกรมอื่นหรือปรึกษานักสถิติ"
  ),
  h4("ข้อมูลของคุณ"),
  p("ไฟล์ของคุณถูกอ่านและวิเคราะห์ภายในเบราว์เซอร์ ไม่ได้ถูกอัปโหลดไปยังเซิร์ฟเวอร์ใด"),
  h4("รุ่นของแอป"),
  p(code(version_label())),
  h4("ส่งข้อเสนอแนะและแจ้งปัญหา"),
  p(
    "หากพบข้อผิดพลาด ผลการวิเคราะห์ดูผิดปกติ หรือมีข้อเสนอแนะฟังก์ชันที่อยากให้เพิ่ม แจ้งได้ผ่านแบบฟอร์มออนไลน์"
  ),
  div(
    class = "d-flex gap-2 flex-wrap mb-2",
    actionButton(
      "btn_about_feedback",
      tagList(icon("comment-dots"), " แบบฟอร์มส่งข้อเสนอแนะ / แจ้งปัญหา"),
      class = "btn btn-primary"
    ),
    actionButton(
      "cp_diagnostic",
      tagList(icon("copy"), " คัดลอกข้อมูลวินิจฉัยระบบ"),
      class = "btn btn-outline-secondary"
    )
  ),
  h4("ผู้ดูแล"),
  p(app_maintainer),
  h4("ซอร์สโค้ด"),
  p(tags$a(
    href = "https://github.com/kanthjs/RBTool",
    target = "_blank",
    "github.com/kanthjs/RBTool"
  )),
  h4("วิธีอ้างอิง"),
  p(
    "ถ้านำผลจากแอปนี้ไปตีพิมพ์ โปรดอ้างอิงแพ็กเกจ R ที่คำนวณการวิเคราะห์ที่คุณรายงาน (รุ่นของแพ็กเกจดูได้ที่หน้า Methods)"
  ),
  h4("ฟอนต์"),
  p(
    "ข้อความในแอปใช้ฟอนต์ จุฬาภรณ์ลิขิต (Chulabhorn Likhit) ฟอนต์แห่งชาติจากราชวิทยาลัยจุฬาภรณ์"
  ),
  h4("สัญญาอนุญาต"),
  p("GNU General Public License รุ่น 2 หรือใหม่กว่า")
)
