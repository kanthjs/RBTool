# The Methods and About pages. Sourced by app.R.

method_refs <- c(
  "International Rice Research Institute (IRRI). PBTools: Plant Breeding Tools, version 1.1.",
  "Bates D, Maechler M, Bolker B, Walker S (2015). Fitting linear mixed-effects models using lme4. Journal of Statistical Software 67(1): 1-48.",
  "Lenth RV. emmeans: Estimated marginal means, aka least-squares means. R package.",
  "Hothorn T, Bretz F, Westfall P (2008). Simultaneous inference in general parametric models. Biometrical Journal 50(3): 346-363.",
  "Pinheiro J, Bates D, R Core Team. nlme: Linear and nonlinear mixed effects models. R package.",
  "Olivoto T, Lucio AD (2020). metan: An R package for multi-environment trial analysis. Methods in Ecology and Evolution 11(6): 783-789.",
  "Finlay KW, Wilkinson GN (1963). The analysis of adaptation in a plant-breeding programme. Australian Journal of Agricultural Research 14: 742-754.",
  "Eberhart SA, Russell WA (1966). Stability parameters for comparing varieties. Crop Science 6: 36-40.",
  "Shukla GK (1972). Some statistical aspects of partitioning genotype-environmental components of variability. Heredity 29: 237-245.",
  "Wricke G (1962). Über eine Methode zur Erfassung der ökologischen Streubreite in Feldversuchen. Zeitschrift für Pflanzenzüchtung 47: 92-96.",
  "Gauch HG (1992). Statistical analysis of regional yield trials: AMMI analysis of factorial designs. Elsevier.",
  "Yan W, Kang MS (2003). GGE biplot analysis: a graphical tool for breeders, geneticists, and agronomists. CRC Press.")

methods_ui <- div(class = "container", style = "max-width: 820px;",
  h3("วิธีการและซอฟต์แวร์"),
  p("แอปนี้ทำการวิเคราะห์ตามเมนู Phenotypic Analysis ของ IRRI PBTools 1.1 โดยคำนวณด้วยแพ็กเกจ R ที่ตีพิมพ์และดูแลอยู่ แทนฟังก์ชันที่ PBTools เขียนเอง ผลทุกส่วนตรวจเทียบกับ PBTools (โค้ดต้นฉบับที่กู้คืนและรันบน R 2.15.1) ด้วยชุดทดสอบ 83 รายการ"),
  uiOutput("methods_versions"),
  h4("Single environment"),
  p("แต่ละลักษณะในแต่ละสถานที่: linear mixed model (lme4, REML) ที่มีเทอมของแบบแผนการทดลองแบบเดียวกับ PBTools ทั้ง 7 แบบ พันธุ์ fixed: ค่าเฉลี่ยปรับแล้วและ SE จาก emmeans พันธุ์ random: BLUP, likelihood ratio test ของความแปรปรวนทางพันธุกรรม และ heritability = s2g / (s2g + s2e / r) เปรียบเทียบค่าเฉลี่ยด้วย Tukey หรือ Dunnett (multcomp) เหมือน PBTools และเพิ่มตัวอักษรกลุ่ม"),
  h4("Multi-environment"),
  p("One-stage: ทุกแปลงทุกสถานที่ในโมเดลเดียว สถานที่ บล็อกในสถานที่ และปฏิสัมพันธ์พันธุ์ × สถานที่ เป็น random Two-stage: ค่าเฉลี่ยของพันธุ์ในแต่ละสถานที่ (ขั้นที่ 1) รวมในโมเดลที่สถานที่เป็น random เลือกถ่วงน้ำหนักด้วย 1/SE² ได้"),
  h4("GxE and stability (metan)"),
  p("Finlay-Wilkinson / Eberhart-Russell (metan::ge_reg), Shukla (metan::Shukla), ดัชนีความเสถียรหลายแบบ (metan::ge_stats), AMMI (metan::performs_ammi) และ GGE biplot (metan::gge)"),
  h4("ความต่างจาก PBTools ที่ควรรู้"),
  tags$ul(
    tags$li("Shukla: PBTools ไม่ได้ใช้สูตรของ Shukla (1972) แต่ประมาณส่วนเบี่ยงเบนมาตรฐานของ residual แยกตามพันธุ์ด้วย nlme::gls (REML) แอปแสดงทั้งสองแบบ ลำดับของพันธุ์ใกล้เคียงกันแต่ไม่เท่ากัน"),
    tags$li("Two-stage แบบถ่วงน้ำหนัก 1/SE²: ค่าความแปรปรวนและ SE ใช้ lme4 รุ่นปัจจุบัน ซึ่งต่างจาก PBTools 1.1 (lme4 รุ่นเก่าปรับสเกลน้ำหนักต่างกัน) ค่าเฉลี่ยตรงกัน"),
    tags$li("AMMI: ใช้ข้อมูลรายแปลง ค่า F ของแต่ละ PC จึงใช้ error ของข้อมูลจริง PBTools ใช้ error รวมจากขั้นที่ 1 ค่า F ต่างเล็กน้อย ส่วนร้อยละและคะแนนของ PC ตรงกัน"),
    tags$li("ช่วงความเชื่อมั่นของ Tukey/Dunnett คำนวณจุดวิกฤตเชิงตัวเลข (multcomp) แอปกำหนด seed ให้ผลซ้ำได้")),
  h4("เอกสารอ้างอิง"), tags$ul(lapply(method_refs, tags$li)))

about_ui <- div(class = "container", style = "max-width: 820px;",
  h3("About"),
  p(sprintf("%s ทำการวิเคราะห์ข้อมูลการทดลองปรับปรุงพันธุ์พืชตามแนวทางของโปรแกรม PBTools (Plant Breeding Tools) ของสถาบันวิจัยข้าวนานาชาติ (IRRI) เป็นแอปเว็บที่รัน R ภายในเบราว์เซอร์ ออกแบบให้ใช้งานคล้าย RDstat", app_name)),
  div(class = "alert alert-warning", strong("รุ่นทดลอง (beta) "), "ผลลัพธ์ตรวจเทียบกับ PBTools แล้ว แต่อยู่ระหว่างการพัฒนา ผลที่สำคัญควรตรวจสอบด้วยโปรแกรมอื่นหรือปรึกษานักสถิติ"),
  h4("ข้อมูลของคุณ"), p("ไฟล์ของคุณถูกอ่านและวิเคราะห์ภายในเบราว์เซอร์ ไม่ได้ถูกอัปโหลดไปยังเซิร์ฟเวอร์ใด"),
  h4("รุ่นของแอป"), p(code(version_label())),
  h4("ผู้ดูแล"), p(app_maintainer),
  h4("ซอร์สโค้ด"), p(tags$a(href = "https://github.com/kanthjs/PBTool-Rev", target = "_blank", "github.com/kanthjs/PBTool-Rev")),
  h4("ฟอนต์"), p("ข้อความในแอปใช้ฟอนต์ จุฬาภรณ์ลิขิต (Chulabhorn Likhit) ฟอนต์แห่งชาติจากราชวิทยาลัยจุฬาภรณ์"),
  h4("สัญญาอนุญาต"), p("GNU General Public License รุ่น 2 หรือใหม่กว่า (PBTools เป็น GPL >= 2)"))
