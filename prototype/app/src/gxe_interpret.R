# Thai reading of the AMMI and GGE biplots. Only reads numbers metan has computed: the AMMI ANOVA, scores and
# stability indexes (metan::ammi_indexes), and the GGE biplot coordinates (metan::gge). For GGE the product of the
# genotype and environment coordinates on PC1-PC2 is the biplot's approximation of each genotype's performance in each
# environment; it does not depend on the singular value partitioning.

lst <- function(x, n = 5) { x <- as.character(x); if (length(x) > n) paste0(paste(x[seq_len(n)], collapse = ", "), " และอีก ", length(x) - n, " รายการ") else paste(x, collapse = ", ") }
top <- function(x, n = 3) paste(as.character(x)[seq_len(min(n, length(x)))], collapse = ", ")
pct <- function(x) sprintf("%.1f%%", x)

# list(heading = c(bullet, ...), ...)
ammi_reading <- function(r) {
  a <- r$anova; sc <- r$scores; g <- sc[sc$type == "GEN", ]; e <- sc[sc$type == "ENV", ]
  ss <- setNames(a$`Sum Sq`, a$Source); main <- ss[c("ENV", "GEN", "GEN:ENV")]; sh <- 100 * main / sum(main)
  p_ge <- a$`Pr(>F)`[a$Source == "GEN:ENV"]; pcs <- a[grepl("^PC", a$Source), ]
  n_sig <- sum(pcs$`Pr(>F)` < 0.05, na.rm = TRUE)
  gm <- mean(g$Mean); low_pc1 <- abs(g$PC1) <= stats::median(abs(g$PC1))
  good <- g$Name[g$Mean > gm & low_pc1]; good <- good[order(-g$Mean[match(good, g$Name)])]
  dist2 <- sqrt(g$PC1^2 + g$PC2^2); edist <- sqrt(e$PC1^2 + e$PC2^2)
  out <- list(
    `ภาพรวม (ANOVA ของ AMMI)` = c(
      sprintf("จากผลรวมกำลังสองของ สภาพแวดล้อม พันธุ์ และปฏิสัมพันธ์ สภาพแวดล้อมอธิบายได้ %s พันธุ์ %s และปฏิสัมพันธ์ G×E %s", pct(sh[1]), pct(sh[2]), pct(sh[3])),
      sprintf("ปฏิสัมพันธ์ G×E %s (p %s)", if (isTRUE(p_ge < 0.05)) "มีนัยสำคัญ: พันธุ์ตอบสนองต่อสถานที่ต่างกัน จึงควรพิจารณาความเสถียรร่วมกับผลผลิต" else "ไม่มีนัยสำคัญ: เลือกพันธุ์จากค่าเฉลี่ยรวมได้",
              if (isTRUE(p_ge < 0.0001)) "< 0.0001" else sprintf("= %.4f", p_ge)),
      sprintf("แยกปฏิสัมพันธ์ได้ %d องค์ประกอบ มีนัยสำคัญ %d องค์ประกอบ; PC1 และ PC2 อธิบายได้ %s และ %s ของ G×E (รวม %s)%s", nrow(pcs), n_sig,
              pct(pcs$`% of GxE`[1]), pct(pcs$`% of GxE`[2]), pct(pcs$`Cumulative %`[2]),
              if (pcs$`Cumulative %`[2] < 60) " ซึ่งค่อนข้างต่ำ ภาพ AMMI2 แสดงปฏิสัมพันธ์ได้ไม่ครบ ควรอ่านร่วมกับตาราง" else "")),
    `AMMI1 (ค่าเฉลี่ย กับ PC1)` = c(
      sprintf("แกนนอนคือค่าเฉลี่ย (ค่าเฉลี่ยรวม %.2f) แกนตั้งคือ PC1: จุดที่อยู่ใกล้ PC1 = 0 มีปฏิสัมพันธ์น้อย (เสถียร) จุดที่ห่างจาก 0 มากตอบสนองต่อสถานที่เฉพาะ", gm),
      sprintf("พันธุ์ผลผลิตสูงสุด: %s (%.2f)", g$Name[which.max(g$Mean)], max(g$Mean)),
      if (length(good)) sprintf("พันธุ์ที่ผลผลิตสูงกว่าค่าเฉลี่ยและ PC1 ใกล้ 0 (ผลผลิตสูงและเสถียร): %s", lst(good)) else "ไม่มีพันธุ์ที่ทั้งผลผลิตสูงกว่าค่าเฉลี่ยและ PC1 ใกล้ 0",
      sprintf("พันธุ์ที่ PC1 ห่างจาก 0 มากที่สุด (ปฏิสัมพันธ์สูง): %s", top(g$Name[order(-abs(g$PC1))], 3)),
      sprintf("สถานที่ให้ผลผลิตสูงสุด %s และต่ำสุด %s; สถานที่ที่ PC1 ห่างจาก 0 มากที่สุด (แยกความแตกต่างของพันธุ์ได้มาก/มีปฏิสัมพันธ์สูง): %s; ใกล้ 0 ที่สุด: %s",
              e$Name[which.max(e$Mean)], e$Name[which.min(e$Mean)], top(e$Name[order(-abs(e$PC1))], 2), e$Name[which.min(abs(e$PC1))]),
      "พันธุ์และสถานที่ที่ PC1 มีเครื่องหมายเดียวกันมีปฏิสัมพันธ์ทางบวก (พันธุ์นั้นทำได้ดีกว่าที่คาดในสถานที่นั้น) เครื่องหมายตรงข้ามมีปฏิสัมพันธ์ทางลบ"),
    `AMMI2 (PC1 กับ PC2)` = c(
      sprintf("พันธุ์ที่อยู่ใกล้จุดกำเนิด (0, 0) เสถียรที่สุดในทั้งสองแกน: %s; อยู่ห่างที่สุด (ปฏิสัมพันธ์สูงสุด): %s", top(g$Name[order(dist2)], 3), top(g$Name[order(-dist2)], 3)),
      sprintf("สถานที่ที่อยู่ห่างจุดกำเนิดมากที่สุด (ทำให้เกิดปฏิสัมพันธ์มากที่สุด): %s", top(e$Name[order(-edist)], 3)),
      "พันธุ์ที่อยู่ในทิศทางเดียวกับสถานที่ใด (มุมแคบ) ปรับตัวเฉพาะกับสถานที่นั้น"))
  if (!is.null(r$asv)) { s <- r$asv
    out$`ดัชนีความเสถียร (ASV, metan::ammi_indexes)` = c(
      sprintf("AMMI stability value (ASV) ต่ำ = เสถียร: เสถียรที่สุด %s; เสถียรน้อยที่สุด %s", top(s$Genotype[order(s$ASV)], 3), top(s$Genotype[order(-s$ASV)], 2)),
      sprintf("ดัชนีผลผลิต-ความเสถียร ssiASV (ผลรวมลำดับของผลผลิตและ ASV ค่าต่ำดี): พันธุ์ที่ดีที่สุด %s", top(s$Genotype[order(s$`Yield stability index (ssiASV)`, -s$Mean)], 3)))
  }
  out
}

gge_reading <- function(r) {
  m <- r$model$Y; G <- m$coordgen[, 1:2, drop = FALSE]; E <- m$coordenv[, 1:2, drop = FALSE]
  gn <- as.character(m$labelgen); en <- as.character(m$labelenv); ex <- r$explained[[2]]
  pred <- G %*% t(E)                                   # biplot approximation, genotype x environment
  win <- gn[apply(pred, 2, which.max)]; mega <- split(en, factor(win, unique(win)))
  a <- colMeans(E); la <- sqrt(sum(a^2)); u <- a / la    # average-environment axis (AEA)
  proj <- drop(G %*% u); perp <- abs(G[, 1] * u[2] - G[, 2] * u[1])
  ig <- u * max(proj); dg <- sqrt((G[, 1] - ig[1])^2 + (G[, 2] - ig[2])^2)
  L <- sqrt(rowSums(E^2)); cosA <- drop(E %*% u) / L; ang <- acos(pmin(1, pmax(-1, cosA))) * 180 / pi
  ie <- u * max(L); de <- sqrt((E[, 1] - ie[1])^2 + (E[, 2] - ie[2])^2)
  ce <- (E %*% t(E)) / outer(L, L); neg <- which(upper.tri(ce) & ce < 0, arr.ind = TRUE)
  above <- proj > 0; stab_above <- gn[above][order(perp[above])]
  list(
    `ภาพรวม` = c(
      sprintf("PC1 และ PC2 อธิบายความผันแปรของ G + G×E ได้ %s และ %s รวม %s%s", pct(ex[1]), pct(ex[2]), pct(ex[1] + ex[2]),
              if (ex[1] + ex[2] < 60) ": ต่ำกว่า 60% ภาพ biplot แสดงข้อมูลได้ไม่ครบ ควรแปลผลด้วยความระมัดระวัง" else ": ภาพ biplot แทนข้อมูลได้ดี"),
      sprintf("SVP = %s, scaling = %s; ข้อสรุปด้านล่างที่อิงผลคูณของพิกัด (ผู้ชนะ, ลำดับค่าเฉลี่ย) ไม่ขึ้นกับ SVP แต่ระยะห่างจากแกน (ความเสถียร) ควรอ่านกับ SVP = genotype และการประเมินสถานที่ควรอ่านกับ SVP = environment ตาม Yan & Kang (2003)", m$svp, m$scaling)),
    `3. Which-won-where (ใครชนะที่ไหน)` = c(
      sprintf("แบ่งสถานที่ได้ %d กลุ่ม (mega-environment) ตามพันธุ์ที่ชนะ:", length(mega)),
      vapply(names(mega), function(w) sprintf("%s ชนะใน %s", w, paste(mega[[w]], collapse = ", ")), ""),
      if (length(mega) > 1) "มีพันธุ์ชนะต่างกันในแต่ละกลุ่มสถานที่ แสดงว่ามีปฏิสัมพันธ์แบบสลับลำดับ (crossover) อาจแนะนำพันธุ์แยกตามกลุ่มสถานที่"
      else "พันธุ์เดียวชนะในทุกสถานที่ ไม่พบปฏิสัมพันธ์แบบสลับลำดับในระนาบ PC1-PC2"),
    `2. Mean vs. stability และ 8. Ranking genotypes` = c(
      sprintf("ลำดับค่าเฉลี่ยตามแกนสภาพแวดล้อมเฉลี่ย (AEA, หัวลูกศร): สูงสุด %s; ต่ำสุด %s", top(gn[order(-proj)], 3), top(gn[order(proj)], 2)),
      if (length(stab_above)) sprintf("ในพันธุ์ที่สูงกว่าค่าเฉลี่ย พันธุ์ที่ใกล้แกน AEA (เสถียร) คือ %s; ห่างแกนมากที่สุด (ไม่เสถียร) คือ %s", top(stab_above, 3), stab_above[length(stab_above)]),
      sprintf("พันธุ์ที่ใกล้พันธุ์อุดมคติ (ผลผลิตสูงและเสถียร, จุดศูนย์กลางของวงกลม) ที่สุด: %s", top(gn[order(dg)], 3))),
    `4. Discriminativeness vs. representativeness และ 6. Ranking environments` = c(
      sprintf("เวกเตอร์ยาว = แยกความแตกต่างของพันธุ์ได้ดี: ยาวที่สุด %s; สั้นที่สุด (ให้ข้อมูลน้อย) %s", top(en[order(-L)], 3), top(en[order(L)], 2)),
      sprintf("มุมแคบกับแกน AEA = เป็นตัวแทนของทุกสถานที่: ดีที่สุด %s (มุม %.0f°)", en[which.min(ang)], min(ang)),
      if (any(ang > 90)) sprintf("สถานที่ที่ทำมุมป้านกับ AEA (ไม่เป็นตัวแทน ลำดับพันธุ์ต่างจากภาพรวม): %s", lst(en[ang > 90])) else "ทุกสถานที่ทำมุมแหลมกับ AEA",
      sprintf("สถานที่ใกล้สถานที่ทดสอบอุดมคติ (ยาวและเป็นตัวแทน) ที่สุด: %s", top(en[order(de)], 3))),
    `10. Relationship among environments` = c(
      "มุมระหว่างเวกเตอร์สถานที่ประมาณสหสัมพันธ์: มุมแหลม = สัมพันธ์ทางบวก, มุมฉาก = ไม่สัมพันธ์, มุมป้าน = สัมพันธ์ทางลบ",
      if (nrow(neg)) sprintf("คู่สถานที่ที่สัมพันธ์ทางลบ (มุมเกิน 90°) มี %d คู่ เช่น %s", nrow(neg), lst(sprintf("%s-%s", en[neg[, 1]], en[neg[, 2]]), 4))
      else "ไม่มีคู่สถานที่ที่สัมพันธ์ทางลบ ทุกสถานที่จัดลำดับพันธุ์ไปในทิศทางเดียวกัน",
      sprintf("คู่ที่คล้ายกันที่สุด: %s", { k <- which(upper.tri(ce), arr.ind = TRUE); k <- k[which.max(ce[k]), ]; sprintf("%s-%s (cos = %.2f)", en[k[1]], en[k[2]], ce[k[1], k[2]]) })))
}

reading_ui <- function(x) tagList(lapply(names(x), function(h) tagList(h6(class = "mt-2 mb-1 fw-bold", h), tags$ul(class = "mb-2", lapply(x[[h]], tags$li)))))
reading_text <- function(x) paste(unlist(lapply(names(x), function(h) c(h, paste("-", x[[h]]), ""))), collapse = "\n")

waasb_reading <- function(r, t, wy) {
  q <- table(factor(substr(t$Quadrant, 1, regexpr(":", t$Quadrant) - 1), c("I", "II", "III", "IV")))
  iv <- t$Genotype[startsWith(t$Quadrant, "IV")]; ii <- t$Genotype[startsWith(t$Quadrant, "II:")]
  e <- r$estimates; get <- function(k) e$Values[e$Parameters == k]
  list(
    `ความแปรปรวนและพารามิเตอร์ทางพันธุกรรม` = c(
      sprintf("อัตราพันธุกรรมรวมทุกสถานที่ (h2) = %.2f, อัตราพันธุกรรมของค่าเฉลี่ยพันธุ์ (h2mg) = %.2f, ความแม่นยำการคัดเลือก (Accuracy) = %.2f", get("Heritability"), get("h2mg"), get("Accuracy")),
      sprintf("สัดส่วนความแปรปรวนของ G×E ต่อความแปรปรวนทั้งหมด (GEIr2) = %.2f; สหสัมพันธ์ของพันธุ์ข้ามสถานที่ (rge) = %.2f", get("GEIr2"), get("rge")),
      if (get("h2mg") < 0.5) "h2mg ต่ำ: ความแตกต่างของค่าเฉลี่ยพันธุ์ส่วนใหญ่เป็นผลของ G×E และความคลาดเคลื่อน การคัดเลือกจากค่าเฉลี่ยรวมจะได้ผลจำกัด" else "h2mg สูงพอ: ค่าเฉลี่ยพันธุ์รวมทุกสถานที่เชื่อถือได้"),
    `สี่จตุภาค (ค่าเฉลี่ย กับ WAASB)` = c(
      sprintf("จตุภาค IV (เสถียรและค่าเฉลี่ยดี, เหมาะแนะนำกว้าง) %d พันธุ์: %s", q[["IV"]], if (length(iv)) paste(iv, collapse = ", ") else "ไม่มี"),
      sprintf("จตุภาค II (ค่าเฉลี่ยดีแต่ไม่เสถียร, เหมาะกับบางสถานที่) %d พันธุ์: %s", q[["II"]], if (length(ii)) paste(ii, collapse = ", ") else "ไม่มี"),
      sprintf("จตุภาค III (เสถียรแต่ค่าเฉลี่ยต่ำ) %d พันธุ์ และจตุภาค I (ไม่เสถียรและค่าเฉลี่ยต่ำ) %d พันธุ์", q[["III"]], q[["I"]])),
    `WAASBY` = c(
      sprintf("ที่น้ำหนักค่าเฉลี่ย %d%% : ความเสถียร %d%% พันธุ์อันดับหนึ่งคือ %s และที่คัดเลือกได้คือ %s", wy, 100 - wy, t$Genotype[1], paste(t$Genotype[t$Selected == "yes"], collapse = ", ")),
      "เลื่อนน้ำหนักในขั้น Selection เพื่อดูว่าลำดับพันธุ์เปลี่ยนอย่างไร: น้ำหนักค่าเฉลี่ยสูง (เช่น 65-80%) สำหรับพื้นที่ดี น้ำหนักความเสถียรสูง (เช่น 50%) สำหรับพื้นที่เสี่ยง"))
}

mtsi_reading <- function(r) {
  strengths <- function(cf, sel) { cf <- as.data.frame(cf); cf <- cf[cf[[1]] %in% sel, , drop = FALSE]; fa <- names(cf)[-1]
    vapply(seq_len(nrow(cf)), function(i) { v <- unlist(cf[i, -1]); sprintf("%s: จุดแข็งคือ %s (สัดส่วนน้อย = ใกล้อุดมคติ), จุดอ่อนคือ %s", cf[[1]][i], fa[which.min(v)], fa[which.max(v)]) }, "") }
  fa_traits <- function(dif) { dif <- as.data.frame(dif); vapply(split(dif$VAR, dif$Factor), paste, "", collapse = ", ") }
  out <- list(
    `MTSI (ความเสถียรหลายลักษณะ จาก WAASBY)` = c(
      sprintf("วิเคราะห์ปัจจัย (factor analysis) จัดลักษณะเป็นกลุ่ม: %s", paste(sprintf("%s = %s", names(fa_traits(r$mtsi_dif)), fa_traits(r$mtsi_dif)), collapse = "; ")),
      sprintf("พันธุ์ที่ถูกคัดเลือก (%d%%): %s ค่า MTSI ต่ำ = ใกล้พันธุ์อุดมคติทั้งค่าเฉลี่ยและความเสถียร", r$si, paste(r$mtsi$sel_gen, collapse = ", ")),
      { d <- as.data.frame(r$mtsi_dif); sprintf("ผลต่างการคัดเลือก (selection differential) ของค่าเฉลี่ย: %s", paste(sprintf("%s %+.1f%%", d$VAR, d$SDperc), collapse = ", ")) }))
  if (!is.null(r$mgidi)) out$`MGIDI (ระยะห่างจากพันธุ์อุดมคติ)` = c(
      sprintf("คำนวณจาก%s พันธุ์ที่ถูกคัดเลือก: %s", if (r$mgidi_on == "blup") " BLUP ของพันธุ์" else "ค่าเฉลี่ยพันธุ์", paste(r$mgidi$sel_gen, collapse = ", ")),
      { d <- as.data.frame(r$mgidi_dif); sprintf("ผลต่างการคัดเลือก: %s (ตรวจว่าทิศทางตรงกับเป้าหมาย: ลักษณะที่ต้องการต่ำควรติดลบ)", paste(sprintf("%s %+.1f%%", d$VAR, d$SDperc), collapse = ", ")) },
      strengths(r$mgidi$contri_fac, r$mgidi$sel_gen))
  out
}
