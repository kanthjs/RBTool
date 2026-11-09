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
    use = "Tukey และ Dunnett แบบ exact เหมือน PBTools",
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
    use = "Shukla แบบ PBTools (ความแปรปรวนแยกตามพันธุ์)",
    fun = c("gls()")
  ),
  list(
    pkg = "metan",
    use = "Finlay-Wilkinson, Eberhart-Russell, Shukla, ดัชนีความเสถียร, AMMI, GGE, WAASB, MTSI, MGIDI",
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
  gxe = "ปฏิสัมพันธ์ G×E และความเสถียร",
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
    software = c("stats::lm()", "stats::anova()", "emmeans::emmeans()"),
    refs = c("Gomez KA", "Fisher RA"),
    p(
      "วิเคราะห์ความแปรปรวนแบบสุ่มในบล็อกสมบูรณ์ (RCBD) ทีละลักษณะทีละสถานที่ ด้วยโมเดล y = block + Vareity + ความคลาดเคลื่อน ให้บล็อกและพันธุ์เป็น fixed effect (",
      code("lm()"),
      " และ ",
      code("anova()"),
      " การทดสอบอิทธิพลของพันธุ์ดำเนินการโดยใช้ F-test โดยคำนวณจากอัตราส่วนระหว่างความแปรปรวนเฉลี่ยของพันธุ์ (MS variety) ต่อความแปรปรวนเฉลี่ยของความคลาดเคลื่อน (MS error) ภายหลังการปรับผลของบล็อกเข้าสู่แบบจำลองก่อนพันธุ์ตามลำดับ (sequential sum of squares) (Gomez & Gomez, 1984)"
    ),
    p(
      "ค่าเฉลี่ยของพันธุ์และ SE จาก ",
      code("emmeans()"),
      " ค่าสัมประสิทธิ์การแปรผัน (CV) = จากรากที่สองของความแปรปรวนเฉลี่ยของความคลาดเคลื่อน (MS error) หารด้วยค่าเฉลี่ยรวมทั้งการทดลอง แล้วคูณด้วย 100 ส่วนค่าความแตกต่างอย่างน้อยที่มีนัยสำคัญ (least significant difference; LSD) ที่ระดับนัยสำคัญ 5 เปอร์เซ็นต์ คำนวณจากค่า t ที่ระดับความน่าจะเป็น 0.975 โดยใช้องศาเสรีของความคลาดเคลื่อน คูณด้วยค่าคลาดมาตรฐานของความแตกต่างเฉลี่ย (standard error of difference; SED)  ",
      "เมื่อข้อมูลครบ (ทุกพันธุ์ในทุกบล็อก) ค่าเฉลี่ยและ F test ของพันธุ์เท่ากับวิธี mixed model เปรียบเทียบค่าเฉลี่ยได้ทั้ง Tukey, Dunnett, LSD และ DMRT"
    )
  ),
  method_topic(
    "sea_mixed",
    "Mixed model: BLUE, BLUP และอัตราพันธุกรรม",
    "sea",
    software = c("lme4::lmer()", "stats::anova()", "emmeans::emmeans()"),
    refs = c("International Rice", "Cullis BR"),
    p(
      "โมเดลผสมเชิงเส้น: บล็อกเป็น random effect ประมาณด้วย REML (",
      code("lmer()"),
      ") ",
      "พันธุ์เป็น fixed: ค่าเฉลี่ยปรับแล้ว (BLUE) และ SE จาก ",
      code("emmeans()"),
      " (SE แบบ asymptotic) ",
      "พันธุ์ถูกกำหนดเป็น random effect ใน mixed model โดยประมาณองค์ประกอบความแปรปรวนด้วย REML และคำนวณค่าทำนายค่าจีโนไทป์ (BLUP) จาก intercept บวกอิทธิพลสุ่มของพันธุ์ ความแปรปรวนทางพันธุกรรมทดสอบด้วย likelihood ratio test เปรียบเทียบแบบจำลองที่มีและไม่มีเทอมพันธุ์ (",
      code("anova(refit = FALSE)"),
      ")"
    ),
    p(
      "อัตราพันธุกรรม (Heritability) H² = σ²g / (σ²g + σ²e / r) โดย r คือค่าเฉลี่ยฮาร์มอนิกของจำนวนซ้ำต่อพันธุ์ ",
      "สมการนี้ถูกต้องเมื่อข้อมูลสมดุลเท่านั้น เท่านั้น แต่ในกรณีที่มีจำนวนแปลงทดลองสูญหายเป็นจำนวนมาก การประมาณค่า \\(H^{2}\\) ด้วยวิธีของ Cullis et al. (2006) ซึ่งคำนวณจากค่าความแปรปรวนของผลต่างระหว่างค่าความสามารถทางพันธุกรรมแบบ BLUP จะมีความเหมาะสมมากกว่า"
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
    "Combined ANOVA (การวิเคราะห์ความแปรปรวนร่วม)",
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
    software = c("lme4::lmer()", "emmeans::emmeans()"),
    refs = c("International Rice", "Smith A"),
    p(
      "ทุกแปลงของทุกสถานที่ในโมเดลผสมเดียว (",
      code("lmer()"),
      ") สถานที่ บล็อกภายในสถานที่ และปฏิสัมพันธ์พันธุ์ × สถานที่ เป็น random เหมือน PBTools ",
      "พันธุ์เป็น fixed (BLUE) หรือ random (BLUP และอัตราพันธุกรรมรวมทุกสถานที่) ทดสอบเทอมสุ่มแต่ละเทอมด้วย likelihood ratio test ",
      "ค่าเฉลี่ยพันธุ์ × สถานที่ คือค่าทำนายจากโมเดลโดยไม่รวมเทอมของบล็อก"
    )
  ),
  method_topic(
    "met_two",
    "Two-stage (mixed model)",
    "met",
    software = c("lme4::lmer()", "emmeans::emmeans()"),
    refs = c("International Rice", "Smith A"),
    p(
      "ขั้นที่ 1 วิเคราะห์ทีละสถานที่ (พันธุ์เป็น fixed) ได้ค่าเฉลี่ยพันธุ์ SE ความแปรปรวนของความคลาดเคลื่อน และจำนวนซ้ำ ขั้นที่ 2 รวมค่าเฉลี่ยของทุกสถานที่ในโมเดลที่สถานที่เป็น random (",
      code("lmer()"),
      ") ",
      "เลือกถ่วงน้ำหนักด้วย 1/SE² ได้ เพื่อให้สถานที่ที่แม่นยำกว่ามีน้ำหนักมากกว่า (Smith et al., 2001)"
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
      "ถดถอยค่าเฉลี่ยของแต่ละพันธุ์กับดัชนีสภาพแวดล้อม (ค่าเฉลี่ยสถานที่ − ค่าเฉลี่ยรวม) ด้วย ",
      code("ge_reg()"),
      " ",
      "ใช้วิธี Finlay-Wilkinson (1963) และ Eberhart-Russell (1966) ผ่านฟังก์ชัน ge_reg() จากแพ็กเกจ metan โดยถดถอยค่าเฉลี่ยของแต่ละพันธุ์กับดัชนีสภาพแวดล้อม (ค่าเฉลี่ยสถานที่ − ค่าเฉลี่ยรวม) วิธี Finlay-Wilkinson ประเมินค่าความชัน (
𝑏
1
b 
1
​
 ) และทดสอบว่า 
𝑏
1
b 
1
​
  แตกต่างจาก 1 หรือไม่ ส่วนวิธี Eberhart-Russell ประเมินค่าความชัน (
𝑏
1
b 
1
​
 ) และส่วนเบี่ยงเบนจากเส้นถดถอย (
𝑠
𝑑
𝑖
2
s 
di
2
​
 ) พร้อมทั้งวิเคราะห์ความแปรปรวนของ joint regression พันธุ์ถือว่ามีความเสถียรเมื่อ 
𝑏
1
b 
1
​
  ไม่แตกต่างจาก 1 และ 
𝑠
𝑑
𝑖
2
s 
di
2
​
  ไม่แตกต่างจาก 0 ที่ระดับนัยสำคัญ 
𝛼
=
0.05
α=0.05"
    )
  ),
  method_topic(
    "shukla",
    "Shukla's stability variance",
    "gxe",
    software = c("metan::Shukla()", "nlme::gls()"),
    refs = c("Shukla GK", "International Rice"),
    p(
      "ใช้ดัชนีความแปรปรวนความเสถียรของ Shukla (Shukla, 1972) ด้วย ",
      code("Shukla()"),
      " ค่าน้อย = เสถียร พร้อมผลรวมลำดับของผลผลิตและความเสถียร (Kang) ",
      "เพื่อประเมินสัดส่วนของความแปรปรวนจาก G × E interaction ที่มาจากแต่ละพันธุ์ โดยค่าน้อยหมายถึงความเสถียรสูง พร้อมทั้งรายงานผลรวมลำดับของผลผลิตและความเสถียรตามแนวคิดของ Kang ผู้ใช้สามารถเลือกแสดงโมเดลแบบ PBTools ได้ โดยรายงานส่วนเบี่ยงเบนมาตรฐานของ residual แยกตามพันธุ์ และประมาณค่าด้วยวิธี REML (",
      code("gls()"),
      ")"
    )
  ),
  method_topic(
    "stab_stats",
    "ดัชนีความเสถียรหลายแบบ",
    "gxe",
    software = c("metan::ge_stats()"),
    refs = c("Wricke G", "Lin CS", "Annicchiarico P", "Purchase JL"),
    p(
      "ใช้ฟังก์ชัน  ",
      code("ge_stats()"),
      " เพื่อคำนวณดัชนีความเสถียรหลายแบบในตารางเดียว ได้แก่ ecovalence ของ Wricke (1962), ความแปรปรวนความเสถียรของ Shukla, วิธี Eberhart-Russell, ดัชนีความเชื่อมั่นของ Annicchiarico (1992), superiority ของ Lin & Binns (1988), ",
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
      "ใช้การวิเคราะห์ AMMI เพื่อแยกองค์ประกอบของ G × E interaction ด้วยวิธี singular value decomposition (SVD) (",
      code("performs_ammi()"),
      "; Gauch, 1992) โดยทดสอบนัยสำคัญของแต่ละองค์ประกอบด้วย F-test ",
      "และแสดงผลผ่านกราฟ AMMI1 (ค่าเฉลี่ยกับ PC1), AMMI2 (PC1 กับ PC2) และ nominal yield นอกจากนี้ คำนวณดัชนีความเสถียร ASV และ ssiASV ",
      code("ammi_indexes()"),
      " ",
      "การแปลผลภาษาไทยอ่านจากตาราง ANOVA, คะแนน PC และดัชนีเหล่านี้"
    )
  ),
  method_topic(
    "gge",
    "GGE biplot",
    "gxe",
    software = c("metan::gge()"),
    refs = c("Yan W"),
    p(
      "การวิเคราะห์ GGE biplot ดำเนินการเพื่อแสดงความสัมพันธ์ระหว่างพันธุ์และสภาพแวดล้อมโดยใช้ข้อมูลที่ปรับด้วยค่าเฉลี่ยของสถานที่ (G + G×E) และแยกองค์ประกอบด้วยวิธี singular value decomposition (SVD)",
      code("gge()"),
      "; Yan & Kang, 2003) โดยผู้ใช้สามารถเลือกวิธีการปรับมาตรฐาน (scaling) และการแบ่งส่วนค่า singular value (singular value partitioning) ได้ นอกจากนี้ ",
      "มีกราฟครบ 10 แบบ: basic, mean vs. stability, which-won-where, discriminativeness vs. representativeness, examine an environment, ranking environments, examine a genotype, ranking genotypes, compare two genotypes และ relationship among environments"
    ),
    p(
      "การแปลผลภาษาไทยอ่านจากพิกัดบน PC1-PC2 ที่ metan คำนวณ:  โดยผลคูณของพิกัดพันธุ์กับสถานที่คือค่าประมาณผลของพันธุ์ในสถานที่นั้น (ไม่ขึ้นกับ SVP) ซึ่งใช้หาพันธุ์ที่ชนะในแต่ละสถานที่ (mega-environment) นอกจากนี้ ยังสามารถอ่านลำดับค่าเฉลี่ยตามแกนสภาพแวดล้อมเฉลี่ย (AEA) และระยะห่างจากพันธุ์และสถานที่อุดมคติได้"
    )
  ),
  # ---- BLUP and multi-trait selection ----
  method_topic(
    "waasb",
    "WAASB และ WAASBY",
    "sel",
    software = c("metan::waasb()", "metan::plot_scores()"),
    refs = c("Olivoto T, Lucio AD, da Silva JAG, Marchioro"),
    p(
      "การประมาณค่า BLUP ด้วย Linear Mixed Model (LMM): วิเคราะห์ผ่านโมเดลผสมโดยกำหนดให้พันธุ์ (Genotype) และปฏิสัมพันธ์ระหว่างพันธุ์กับสภาพแวดล้อม (G×E Interaction) เป็น Random Effects ",
      "(ด้วย ",
      code("lme4"),
      ") เพื่อสกัดเมทริกซ์ Best Linear Unbiased Prediction (BLUP) ของ G×E ออกมา ",
      "จากนั้นนำเมทริกซ์ BLUP ของ G×E มากระจายด้วย Singular Value Decomposition (SVD) ในรูปแบบ UΣVᵀ เพื่อสกัดแกน Interaction Principal Component Axes (IPCA) แต่ละแกน"
    ),
    p(
      "สถิติความเสถียร WAASB (Weighted Average of Absolute Scores): คำนวณจากค่าเฉลี่ยถ่วงน้ำหนักของค่าสัมบูรณ์ของคะแนน IPCA ของแต่ละพันธุ์ ",
      "โดยถ่วงน้ำหนักด้วยสัดส่วนความแปรปรวน (Variance Explained) ที่แต่ละ IPCA อธิบายได้จากความแปรปรวนของ G×E ทั้งหมด: ",
      "WAASB_i = Σ (|IPCA_ik| × EP_k) / Σ EP_k (เมื่อ EP_k คือสัดส่วนความแปรปรวนของแกนที่ k) ",
      "พันธุ์ที่มีค่า WAASB ต่ำ แสดงถึงการตอบสนองต่อ G×E ต่ำ จึงมีความเสถียรสูงข้ามสภาพแวดล้อม (Wide Adaptability)"
    ),
    p(
      "ดัชนีคัดเลือกพร้อมกัน WAASBY (Simultaneous Selection Index): บูรณาการทั้งผลผลิตเฉลี่ย (Y) และความเสถียร (WAASB) ",
      "โดยนำค่าเฉลี่ยและค่า WAASB มาแปลงเป็นสเกลสัมพัทธ์ 0–100 (Rescaling) ซึ่งในส่วนของ WAASB จะกลับทิศทาง (Inverted) เพื่อให้คะแนน 100 หมายถึงเสถียรภาพสูงสุด ",
      "พร้อมทั้งแบ่ง 4 ส่วน (Quadrants I–IV) โดยใช้ค่าเฉลี่ยรวม (Grand Mean) และค่าเฉลี่ย WAASB ตัดแกนเพื่อจำแนกกลุ่มพันธุ์เด่นด้านผลผลิตและเสถียรภาพ"
    )
  ),
  method_topic(
    "mtsi",
    "MTSI และ MGIDI (คัดเลือกหลายลักษณะ)",
    "sel",
    software = c(
      "metan::waasb()",
      "metan::mtsi()",
      "metan::gmd()",
      "metan::mgidi()"
    ),
    refs = c("Olivoto T, Lucio AD, da Silva JAG, Sari", "Olivoto T, Nardino M"),
    p(
      "ข้อมูลนำเข้าและการปรับสเกล (Input & Rescaling): ทั้งสองดัชนีออกแบบมาเพื่อแก้ปัญหา Collinearity ในการคัดเลือกหลายลักษณะ (Multi-trait selection) ",
      "โดย ",
      code("mtsi()"),
      " ใช้ค่าดัชนี WAASBY ของแต่ละลักษณะเป็นตัวแปรนำเข้า (ผนวกทั้งผลผลิตและเสถียรภาพ) ขณะที่ ",
      code("mgidi()"),
      " ใช้ค่า BLUP ของพันธุ์ (สกัดผ่าน ",
      code("gmd()"),
      ") หรือค่าเฉลี่ยทางฟีโนไทป์ ",
      "ข้อมูลทุกทุกลักษณะจะถูก Rescale เป็นสเกลสัมพัทธ์ 0–100 ตามทิศทางการคัดเลือกที่กำหนด (ค่าสูงดี หรือ ค่าต่ำดี) เพื่อให้คะแนน 100 แทนค่าที่พึงประสงค์สูงสุดเสมอ ",
      "(สำหรับ MGIDI หากลักษณะใดไม่มีความแปรปรวนทางพันธุกรรม หรือค่า Genotypic Variance ใกล้ศูนย์ จะถูกคัดออกอัตโนมัติ)"
    ),

    # 2. การวิเคราะห์ปัจจัย (Exploratory Factor Analysis) และการกำหนด Ideotype
    p(
      "การจัดกลุ่มลักษณะและการสร้างพันธุ์อุดมคติ (Factor Analysis & Ideotype): ",
      "ใช้การวิเคราะห์ปัจจัยเชิงสำรวจ (EFA) ผ่านเมทริกซ์สหสัมพันธ์ โดยสกัดปัจจัยตามเกณฑ์ Eigenvalue > 1 และหมุนแกนปัจจัยแบบตั้งฉาก (Varimax Rotation) ",
      "เพื่อลดมิติของลักษณะที่สัมพันธ์กันให้อยู่ในรูปคะแนนปัจจัย (Factor Scores; F_ij) ",
      "จากนั้นสร้างเวกเตอร์พันธุ์อุดมคติ (Ideotype) ขึ้นมา โดยกำหนดให้มีค่า Factor Score สูงสุดในทุกแกนปัจจัย"
    ),

    # 3. การคำนวณระยะห่างยุคลิดและการประเมินประสิทธิภาพการคัดเลือก
    p(
      "การวัดระยะห่างและการจำแนกคุณสมบัติ (Distance & Selection Gain): ",
      "คำนวณระยะห่างแบบยุคลิดหลายมิติ (Multi-trait Distance) ระหว่างแต่ละพันธุ์กับพันธุ์อุดมคติ: ",
      "D_i = √[ Σ (F_ij - F_ideotype,j)² ] โดยพันธุ์ที่มีค่าระยะห่างน้อยที่สุดคือพันธุ์ที่ใกล้เคียงอุดมคติมากที่สุด ",
      "ผลลัพธ์แสดงทั้ง Selection Differential (SD), อัตราความก้าวหน้าในการคัดเลือก (Selection Gain; SG%), ",
      "รวมถึงการแตกสลายระยะห่างออกเป็นสัดส่วนของแต่ละ Factor เพื่อแจกแจง จุดแข็ง (Strengths) และ จุดอ่อน (Weaknesses) ของพันธุ์ที่ได้รับคัดเลือก"
    )
  ),
  # ---- data and plots ----
  method_topic(
    "desc",
    "สถิติเชิงพรรณนา",
    "data",
    software = c("stats::sd()"),
    refs = c("International Rice"),
    p(
      "คำนวณแบบเดียวกับฟังก์ชัน DescriptiveStatistics ของ PBTools: จำนวนข้อมูลที่ไม่สูญหาย ค่าเฉลี่ย และส่วนเบี่ยงเบนมาตรฐาน (ตัวหาร n − 1) ",
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
    "วิธีวิเคราะห์ของโปรแกรม เป็น Phenotypic Analysis เท่านั้น คำนวณด้วย R และ packages ที่เป็นที่ยอมรับบน CRAN แล้ว",
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
  p("หากพบข้อผิดพลาด ผลการวิเคราะห์ดูผิดปกติ หรือมีข้อเสนอแนะฟังก์ชันที่อยากให้เพิ่ม แจ้งได้ผ่านแบบฟอร์มออนไลน์"),
  div(
    class = "d-flex gap-2 flex-wrap mb-2",
    actionButton("btn_about_feedback", tagList(icon("comment-dots"), " แบบฟอร์มส่งข้อเสนอแนะ / แจ้งปัญหา"), class = "btn btn-primary"),
    actionButton("cp_diagnostic", tagList(icon("copy"), " คัดลอกข้อมูลวินิจฉัยระบบ"), class = "btn btn-outline-secondary")
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
  p("GNU General Public License รุ่น 2 หรือใหม่กว่า (PBTools เป็น GPL >= 2)")
)
