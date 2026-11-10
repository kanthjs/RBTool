# คู่มือทำงานต่อ: PBTools และ RBTool

เอกสารนี้สรุปความเข้าใจโปรแกรม IRRI PBTools 1.1 และสถานะของ RBTool ณ วันที่ 4 พฤศจิกายน 2569 (commit `f567f62`) เขียนไว้สำหรับทำงานต่อบนเครื่องอื่นที่ไม่มี PBTools ติดตั้งอยู่ รายละเอียดเชิงเทคนิคทั้งหมดอยู่ใน `PORTING.md` ส่วนเหตุผลที่เลือกแพ็กเกจ R อยู่ใน `docs/PACKAGES.md` เอกสารนี้เป็นแผนที่สำหรับเริ่มอ่าน

## 1. เป้าหมายและขอบเขต

RBTool ทำงานแบบเดียวกับเมนู **Phenotypic Analysis** ของ PBTools โดยคำนวณด้วยแพ็กเกจ R ที่เผยแพร่และดูแลอยู่ (lme4, emmeans, multcomp, multcompView, nlme, metan) ไม่ได้ใช้ฟังก์ชันของ PBTools เอง และตัวเลขทุกค่าต้องตรวจเทียบกับ PBTools แล้วได้ผลเหมือนกัน แนวทางนี้เดียวกับโปรเจกต์ STAR → RDstat

การตัดสินใจของผู้พัฒนา (กันต์ธณวิชญ์) ที่ใช้อยู่

- 3 พ.ย. 2569: ขอบเขตคือ single environment, multi-environment (one-stage, two-stage) และ GxE (stability models, multiplicative models) ส่วน QTL, mating designs (NC I–III, diallel, triple test cross), generation mean, selection index, randomization และ design อยู่นอกขอบเขต
- 3 พ.ย. 2569: two-stage แบบถ่วงน้ำหนัก 1/SE² ใช้ผลตาม lme4 1.x ไม่เลียนสเกลของ lme4 รุ่นเก่า
- 3 พ.ย. 2569: หน้า Single environment ในแอปมีแค่ RCBD ส่วน engine ยังรองรับครบ 7 designs
- 4 พ.ย. 2569: ให้ RBTool ล้อกับฟังก์ชันที่ PBTools มีและตรวจความเหมือน ยังไม่เพิ่มฟีเจอร์ใหม่ เพิ่ม Descriptive statistics แล้ว ตัวเลือกอื่นที่ยังขาด (ดูหัวข้อ 6) ยอมรับให้ละไว้

## 2. PBTools ทำงานอย่างไร

PBTools 1.1 เป็นโปรแกรม Java แบบ Eclipse RCP (e4) ที่ใช้โค้ดชุดเดียวกับ STAR (ชื่อ product คือ `Star.product`) แต่ละโปรแกรมเลือกแสดงเมนูต่างกัน ตัวโปรแกรมไม่ได้คำนวณสถิติเอง ลำดับการทำงานเป็นดังนี้

1. ผู้ใช้เลือกเมนูและกรอก dialog
2. คลาส `PBToolAnalysisManager` ใน `RJavaManager1.0.jar` ประกอบคำสั่ง R เป็นข้อความ
3. ส่งคำสั่งไปรันใน R 2.15.1 ผ่าน JRI (เริ่มด้วย `library(PBTools)`)
4. ผลลัพธ์ถูกเขียนเป็นไฟล์ข้อความด้วย `sink()` / `capture.output()`

สถิติทั้งหมดจึงอยู่ใน R package `PBTools` ฝั่ง Java ทำหน้าที่แค่ประกอบคำสั่งและจัดรูปผลลัพธ์

ตำแหน่งไฟล์บนเครื่องเดิม (Windows)

| สิ่งของ | ที่อยู่ |
|---|---|
| โปรแกรม | `C:\PBTools` (`pbtools.exe`, plugin `plugins\Star_1.0.0.201406021442.jar`) |
| Java bridge | `lib/RJavaManager1.0.jar` ภายใน plugin (สำเนาเดียวกันอยู่ที่ `configuration\org.eclipse.osgi\bundles\1\1\.cp\lib\`), md5 `1846cb4e04396665b011b0feebf43e24` |
| เมนู | `Application.e4xmi` ภายใน plugin jar |
| R ที่ PBTools ใช้ | `C:\RPackages\R-2.15.1` (lme4 0.999999-0, nlme 3.1-104), แพ็กเกจที่ `library\PBTools` |
| R ปัจจุบัน | `C:\Program Files\R\R-4.5.2` (lme4 2.0, nlme 3.1-168, metan 1.19) |

ใน jar ไม่มี path ของ R ฝังอยู่ ค่านี้น่าจะมาจาก launcher (install4j) หรือ PATH/registry ของ Windows ยังไม่ได้ยืนยัน

## 3. เมนูในขอบเขตและสิ่งที่ PBTools เรียกใช้

| เมนู PBTools | ฟังก์ชัน R ของ PBTools | ตัวเลือกใน dialog |
|---|---|---|
| Single-environment Analysis | `ssa.test`, `ssa.pairwise`, `ssa.resid`, `DescriptiveStatistics`, `genoNpheno.corr` | 7 designs; genotype fixed/random; exclude checks; descriptive statistics; variance components; boxplot/histogram ข้อมูลดิบ; heatmap residual ตามแถว-คอลัมน์; diagnostic plots; pairwise (ทุกคู่ / เทียบ control, alpha); geno-pheno correlation |
| Multi-environment > One-Stage | `GEOneStage.test`, `GEOneStage_resid`, `ssa.pairwise` | เหมือน SEA แต่ไม่มี heatmap, geno-pheno correlation และ exclude checks |
| Multi-environment > Two-Stage | `GETwoStage.test`, `GETwoStage_resid` | ไฟล์ค่าเฉลี่ยพร้อม SE, error variance และ reps; weight `none` หรือ `stderr` (ใน signature มี `stdmean` ด้วย แต่ในตัวฟังก์ชันไม่มีโค้ดรองรับ); fixed/random; pairwise |
| GxE > Stability Models | `stability.analysis` | Finlay-Wilkinson regression, Shukla |
| GxE > Multiplicative Models | `multiplicativeModels` → `ammi.analysis(2)`, `gge.analysis(2)`, `AMMI.contour` | AMMI (response plot, biplot PC1–2/1–3/2–3, AMMI1, adaptation map), GGE (symmetric/environment/genotype view); รับข้อมูลดิบหรือค่าเฉลี่ยพร้อม reps และ residual variance |

Descriptive statistics ใน dialog ของ PBTools เรียก `DescriptiveStatistics()` ด้วยค่า default เสมอ คือ `nnmiss`, `mean` และ `sd` SEA แยกตามสถานที่ ส่วน one-stage และ two-stage ใช้ `grp = NULL`

## 4. โมเดลที่ PBTools ใช้ (RBTool ใช้แบบเดียวกัน)

Single environment ฟิตแยกแต่ละลักษณะในแต่ละสถานที่ด้วย lmer (REML) โดยมีพจน์ของแผนการทดลองดังนี้

| Design | พจน์สุ่มของแผน |
|---|---|
| RCB, AugRCB | `(1|Block)` |
| AugLS | `(1|Row) + (1|Col)` |
| Alpha | `(1|Rep/Block)` |
| RowCol | `(1|Rep) + (1|Rep:Row) + (1|Rep:Col)` |
| LatinAlpha | `(1|Rep) + (1|Block) + (1|Rep:Block)` |
| LatinRowCol | `(1|Rep) + (1|Row) + (1|Rep:Row) + (1|Col)` (สลับ row กับ col เมื่อจำนวน row มากกว่า col) |

- **Genotype fixed:** ใช้ BLUE และ SE จาก covariance ของโมเดล (emmeans ต้องตั้ง `lmer.df = "asymptotic"` มิฉะนั้นจะได้ Kenward-Roger)
- **Genotype random:** BLUP คือ intercept บวก random effect มี LRT ของ genotype variance (REML, `refit = FALSE`) และ H² = σ²g / (σ²g + σ²e / r) เมื่อ r คือ harmonic mean ของจำนวนซ้ำต่อพันธุ์
- **Exclude checks:** ใช้ได้เฉพาะ augmented designs ร่วมกับ genotype random

One-stage ใช้พจน์ของแผนซ้อนในสถานที่ บวก `(1|Env)` และ `(1|Geno:Env)` H² = σ²g / (σ²g + σ²ge/e + σ²e/(r·e)) ค่าเฉลี่ย GxE คือค่าทำนายที่ไม่รวมพจน์ของ block

Two-stage ขั้นที่ 1 คือ BLUE ของพันธุ์ในแต่ละสถานที่ ขั้นที่ 2 คือ `Mean ~ Geno + (1|Env)` จะถ่วงน้ำหนัก 1/SE² หรือไม่ก็ได้

Pairwise ของ PBTools ใช้ `multcomp::glht` แบบ single-step สำหรับ Tukey และ Dunnett และแสดงเฉพาะคู่ที่มีนัยสำคัญ ค่าวิกฤตหาเชิงตัวเลขแบบสุ่ม จึงต้องตั้ง seed

Stability: Finlay-Wilkinson ใช้ slope บนดัชนีสถานที่ (ค่าเฉลี่ยสถานที่ลบค่าเฉลี่ยรวม) ส่วน Shukla ของ PBTools ไม่ใช่สูตร Shukla (1972) แต่ใช้ `nlme::gls` ที่ให้ residual variance หนึ่งค่าต่อพันธุ์ (varIdent)

AMMI ของ PBTools มาจากโค้ดของ agricolae ใช้ pooled error ของขั้นที่ 1 สำหรับค่า F ของ PC

## 5. วัสดุอ้างอิงใน `reference/` และความน่าเชื่อถือ

ตรวจเมื่อ 4 พ.ย. 2569 (รายละเอียดอยู่ในหัวข้อ "Provenance check" ของ `PORTING.md`)

- `r-original/`: ฟังก์ชัน R 321 ตัวที่ deparse จาก PBTools ที่ติดตั้งอยู่ คอมเมนต์หายไปหมด แหล่งนี้คือ **ความจริงหลัก**
- `pbtools-r-fork/`: ซอร์สจาก shingocat ในส่วนที่อยู่ในขอบเขตตรงกับตัวติดตั้ง ต่างกันแค่การปรับให้เข้ากับ lme4 รุ่นใหม่ ส่วนที่มีเพิ่มอีก 99 ฟังก์ชัน (`doSEA`, `doMEA`, contrast, QTL) เป็นรุ่นพัฒนาต่อที่ไม่ได้มากับ PBTools 1.1
- `pbtools-port/` (แพ็กเกจ `PBToolsRD`): PBTools ที่ port มารันบน R 4.5 สร้างจาก `r-original` ใช้เป็น **ไม้บรรทัด** ต่างจากของเดิมเฉพาะ 5 ฟังก์ชันที่จงใจแก้ (ดู `PORTING.md`)
- `original-outputs/`: ผลที่รันจาก PBTools บน R 2.15.1 จริง ใช้เทียบกับ port
- `java-rbridge/` (เก็บในเครื่องเท่านั้น ไม่ได้อยู่บน GitHub): decompile มาจาก `RJavaManager1.0.jar` ของ **STAR** ซึ่งเป็นรุ่นที่ใหม่กว่าตัวใน PBTools ทุกอย่างที่อยู่ใน jar ของ PBTools มีอยู่ในชุดนี้ครบ แต่มีของเกินมา ได้แก่ p-rep (`ssaTestPrep`), outlier detection, regression, augmented alpha/row-column, ตาราง F แบบ lmerTest และ contrast ห้ามถือว่าของที่เกินมาเป็นฟีเจอร์ของ PBTools 1.1

## 6. RBTool: โครงสร้าง ความครอบคลุม และการตรวจสอบ

โครงสร้างของ repo: `engine/R/` เก็บการวิเคราะห์ (`pb_sea`, `pb_stage1`, `pb_one_stage`, `pb_two_stage`, `pb_descriptive`, `pb_pairwise`, `pb_fw`, `pb_shukla`, `pb_stability_stats`, `pb_ammi`, `pb_gge`, `pb_diag_plot`) ส่วน `prototype/app/` เป็นแอป Shiny ที่ export เป็น Shinylive (ธีมสีแดง #A61C1C) โดย `scripts/build_app.R` คัดลอก engine เข้าแอป

| PBTools | RBTool | ผล |
|---|---|---|
| SEA: 7 designs, fixed/random, exclude checks, หลายลักษณะ, variance components | `pb_sea` (แอปแสดงแค่ RCB) | เหมือน |
| Descriptive statistics | `pb_descriptive` | เหมือน (ผลต่าง 0) |
| Pairwise Tukey/Dunnett | `pb_pairwise` (ค่า default คือ emmeans ซึ่งเร็ว ตัวเลือก exact คือ multcomp) | exact เหมือน, default ต่างกันไม่เกิน 1% |
| One-stage, two-stage | `pb_one_stage`, `pb_two_stage` | เหมือน |
| Finlay-Wilkinson, Eberhart-Russell, Shukla | `pb_fw`, `pb_er`, `pb_shukla` (รูปแบบ PBTools ใช้ nlme) | เหมือน |
| AMMI, GGE | `pb_ammi`, `pb_gge` (metan) | % และ score เหมือน, ค่า F ของ PC ต่างเล็กน้อย |
| ยังไม่มี (ยอมรับให้ละไว้) | boxplot/histogram ข้อมูลดิบ, heatmap residual, geno-pheno correlation, response plot, biplot PC1–3/2–3, adaptation map, multiplicative ที่รับค่าเฉลี่ยเป็น input | – |

RBTool มีบางอย่างที่ PBTools ไม่มี ได้แก่ letter groups, ตารางดัชนีเสถียรภาพจาก metan, สรุปผลภาษาไทย และไฟล์ Excel/Word ให้ดาวน์โหลด

ห่วงโซ่การตรวจสอบมีสามขั้น

1. port เทียบกับตัวจริง: `sh scripts/check_port.sh` 33 กรณี ถ้าจะสร้างผลอ้างอิงใหม่ด้วย `REGEN=1` ต้องมี R 2.15.1 ซึ่งมีเฉพาะเครื่องเดิม
2. engine เทียบกับ port: `Rscript scripts/compare_engine.R` 99 จุด
3. แอป: `Rscript prototype/tests/test_server.R` 30 ข้อ

ความต่างที่รู้แล้วและบันทึกไว้ในหน้า Methods ของแอป

- two-stage แบบถ่วงน้ำหนัก: lme4 รุ่นเก่าคูณสเกล variance และ SE ด้วย 1/mean(w) ค่าเฉลี่ยยังตรงกัน
- LRT ของ genotype fixed ใน one-stage: lme4 รุ่นเก่าหยุดที่จุด optimum ที่แย่กว่า
- singular fit: lme4 รุ่นเก่าให้ 0 พอดี รุ่นใหม่ให้ค่า 1e-3 ถึง 1e-8
- ขอบเขตช่วงของ Shukla ต่างกันตามรุ่นของ nlme แต่ค่าประมาณตรงกัน
- บั๊กเดิมของ PBTools: genotype fixed ร่วมกับ excludeCheck จะ error ว่า `newExcludeCheck` not found และ latinized row-column ต้องใช้เลขแถวที่ต่อเนื่องข้ามซ้ำ

## 7. เริ่มทำงานบนเครื่องใหม่

1. `git clone https://github.com/kanthjs/RBTool.git`
2. ติดตั้ง R 4.5 แล้วติดตั้งแพ็กเกจ: `install.packages(c("shiny", "bslib", "lme4", "emmeans", "multcomp", "multcompView", "nlme", "metan", "readxl", "writexl", "ggplot2", "doBy", "shinylive"))` bslib ต้องเป็นรุ่นที่มี `navbar_options`
3. ติดตั้งไม้บรรทัด: `install.packages("reference/pbtools-port", repos = NULL, type = "source")`
4. ตรวจ: `Rscript scripts/compare_engine.R`, `Rscript scripts/build_app.R`, `Rscript prototype/tests/test_server.R`
5. รันแอป: `Rscript -e 'shiny::runApp("prototype/app")'`

สิ่งที่ทำได้เฉพาะบนเครื่องเดิม (ที่มี `C:\PBTools` และ `C:\RPackages\R-2.15.1`) คือ เปิด PBTools เพื่อดูผลหรือ dialog จริง, สร้าง `original-outputs` ใหม่ (`REGEN=1`) และแตก jar หรือเมนูจากตัวติดตั้ง ส่วน `check_port.sh` เขียน path ของ Rscript แบบ Windows ไว้ตายตัว ถ้าใช้บนเครื่องอื่นต้องแก้ตัวแปร `R45`

ข้อควรระวัง: แอปตั้ง version จากเลข CI run และ workflow `pages.yml` จะ deploy อัตโนมัติเมื่อ push เข้า `main` ถ้ามีการเปลี่ยน `prototype/**` หรือ `engine/**`

## 8. งานที่ยังค้าง

- ~~รัน `test_server.R` บน Windows หลังเพิ่ม Descriptive statistics~~ ทำแล้ว 4 พ.ย. 2569 บนเครื่อง Windows ที่มี R 2.15.1 (ไม่มี `C:\PBTools`): `compare_engine.R` 97/97, `test_server.R` 30/30
- (ไม่เร่ง ทำได้เฉพาะเครื่องที่ติดตั้ง PBTools ไว้ที่ `C:\PBTools`) decompile jar ของ PBTools จริงมาแทนชุดของ STAR ใน `reference/java-rbridge`
- (ไม่เร่ง ทำได้เฉพาะเครื่องที่ติดตั้ง PBTools) ยืนยันว่า GUI เรียกใช้ R ที่ `C:\RPackages\R-2.15.1`
- ~~ตรวจว่า deploy ขึ้น GitHub Pages ได้สำเร็จ~~ สำเร็จแล้ว 4 พ.ย. 2569: https://kanthjs.github.io/RBTool/ (ยังไม่ได้ลองเปิดใช้งานจริงในเบราว์เซอร์)
- ตรวจข้อความภาษาไทย และจับเวลาการทำงานในเบราว์เซอร์ (metan ดึงแพ็กเกจตามมาจำนวนมาก)
