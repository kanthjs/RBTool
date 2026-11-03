`gge.analysis.default` <-
function (ENV, GEN, REP, Y, MSE = 0, number = TRUE, yVar = "Y1", 
    f = 0.5, graphSym = FALSE, graphEnv = FALSE, graphGeno = FALSE, 
    ...) 
{
    ENV <- as.factor(ENV)
    GEN <- as.factor(GEN)
    nenv <- length(unique(ENV))
    ngen <- length(unique(GEN))
    minimo <- min(ngen, nenv)
    if (length(REP) > 1) {
        REP <- as.factor(REP)
        nrep <- length(unique(REP))
        modelo <- aov(Y ~ ENV + REP %in% ENV + GEN + ENV:GEN)
        mm <- anova(modelo)
        nn <- mm[2, ]
        mm[2, ] <- mm[3, ]
        mm[3, ] <- nn
        row.names(mm)[2] <- "REP(ENV)"
        row.names(mm)[3] <- "GEN     "
        mm[1, 4] <- mm[1, 3]/mm[2, 3]
        mm[1, 5] <- 1 - pf(mm[1, 4], mm[1, 1], mm[2, 1])
        DFE <- df.residual(modelo)
        MSE <- deviance(modelo)/DFE
        medy <- mean(Y, na.rm = TRUE)
    }
    else {
        DFE <- nenv * (ngen - 1) * (REP - 1)
        DFEa <- nenv * (REP - 1)
        nrep <- REP
        modelo <- aov(Y ~ ENV + GEN + ENV:GEN)
        xx <- as.matrix(anova(modelo))
        xx <- rbind(xx[1, ], xx[1, ], xx[2:4, ])
        xx[2, 1] <- DFEa
        xx[2, 2:5] <- NA
        xx[, 2] <- xx[, 2] * nrep
        xx[, 3] <- xx[, 3] * nrep
        xx[5, 1] <- DFE
        xx[5, 3] <- MSE
        xx[5, 2] <- MSE * DFE
        xx[1, 4] <- NA
        xx[3, 4] <- xx[3, 3]/MSE
        xx[4, 4] <- xx[4, 3]/MSE
        xx[1, 5] <- NA
        xx[3, 5] <- 1 - pf(xx[3, 4], xx[3, 1], DFE)
        xx[4, 5] <- 1 - pf(xx[4, 4], xx[4, 1], DFE)
        row.names(xx)[1] <- "ENV     "
        row.names(xx)[2] <- "REP(ENV)"
        medy <- mean(Y, na.rm = TRUE)
    }
    raw <- data.frame(ENV, GEN, Y)
    MEDIAS <- tapply(raw[, 3], raw[, c(1, 2)], mean)
    xx <- rownames(MEDIAS)
    yy <- colnames(MEDIAS)
    fila <- length(xx)
    col <- length(yy)
    total <- fila * col
    x <- character(length = total)
    y <- character(length = total)
    z <- numeric(length = total)
    k <- 0
    for (i in 1:fila) {
        for (j in 1:col) {
            k <- k + 1
            x[k] <- xx[i]
            y[k] <- yy[j]
            z[k] <- MEDIAS[i, j]
        }
    }
    MEDIAS <- data.frame(ENV = x, GEN = y, Y = z)
    x <- MEDIAS[, 1]
    y <- MEDIAS[, 2]
    z <- MEDIAS[, 3]
    modelo2 <- lm(z ~ x + y)
    for (i in 1:length(z)) {
        if (is.na(z[i])) 
            z[i] <- predict(modelo2, data.frame(x = MEDIAS[i, 
                1], y = MEDIAS[i, 2]))
    }
    MEDIAS <- data.frame(ENV = x, GEN = y, Y = z)
    modelo1 <- lm(Y ~ ENV, data = MEDIAS)
    residual <- modelo1$residuals
    MEDIAS <- data.frame(MEDIAS, RESIDUAL = residual)
    mlabel <- names(MEDIAS)
    names(MEDIAS) <- c(mlabel[1:2], yVar, mlabel[4])
    OUTRES <- MEDIAS[order(MEDIAS[, 1], MEDIAS[, 2]), ]
    OUTRES2 <- by(OUTRES[, 4], OUTRES[, c(2, 1)], function(x) sum(x))
    OUTMED <- by(OUTRES[, 3], OUTRES[, c(2, 1)], function(x) sum(x))
    s <- svd(OUTRES2)
    U <- s$u
    L <- s$d
    V <- s$v
    L <- L[1:minimo]
    SS <- (L^2) * nrep
    SUMA <- sum(SS)
    percent <- round(((1/SUMA) * SS) * 100, 1)
    minimo <- min(ngen, nenv)
    DFAMMI <- rep(0, minimo)
    acum <- DFAMMI
    MSAMMI <- DFAMMI
    F.AMMI <- DFAMMI
    PROBF <- DFAMMI
    acumula <- 0
    for (i in 1:minimo) {
        DF <- (ngen - 1) + (nenv - 1) - (2 * i - 1)
        if (DF <= 0) 
            break
        DFAMMI[i] <- DF
        acumula <- acumula + percent[i]
        acum[i] <- acum[i] + acumula
        MSAMMI[i] <- SS[i]/DFAMMI[i]
        F.AMMI[i] <- round(MSAMMI[i]/MSE, 2)
        PROBF[i] <- round(1 - pf(F.AMMI[i], DFAMMI[i], DFE), 
            4)
    }
    SS <- round(SS, 6)
    MSAMMI <- round(MSAMMI, 6)
    SSAMMI <- data.frame(percent, acum, Df = DFAMMI, `Sum Sq` = SS, 
        `Mean Sq` = MSAMMI, `F value` = F.AMMI, Pr.F = PROBF)
    nssammi <- nrow(SSAMMI)
    SSAMMI <- SSAMMI[SSAMMI$Df > 0, ]
    nss <- nrow(SSAMMI)
    row.names(SSAMMI) <- paste("PC", 1:nss, sep = "")
    if (f == 0.5) {
        LL <- sqrt(diag(L))
        SCOREG <- U %*% LL
        SCOREE <- V %*% LL
    }
    if (f == 0) {
        SCOREG <- U
        SCOREE <- V %*% diag(L)
    }
    if (f == 1) {
        SCOREG <- U %*% diag(L)
        SCOREE <- V
    }
    SCORES <- rbind(SCOREG, SCOREE)
    colnames(SCORES) <- paste("PC", 1:nssammi, sep = "")
    MSCORES <- SCORES[1:ngen, ]
    NSCORES <- SCORES[(ngen + 1):(ngen + nenv), ]
    MGEN <- data.frame(type = "GEN", Y = apply(OUTMED, 1, mean), 
        MSCORES)
    MENV <- data.frame(type = "ENV", Y = apply(OUTMED, 2, mean), 
        NSCORES)
    commonRowNames <- intersect(row.names(MGEN), row.names(MENV))
    if (length(commonRowNames) > 0) {
        row.names(MGEN) <- paste("G", row.names(MGEN), sep = "")
        row.names(MENV) <- paste("E", row.names(MENV), sep = "")
    }
    bplot <- rbind(MGEN, MENV)
    bplot <- bplot[, 1:(nss + 2)]
    mlabel <- names(bplot)
    names(bplot) <- c(mlabel[1], yVar, mlabel[c(-1, -2)])
    maxy <- max(bplot[, 4])
    miny <- min(bplot[, 4])
    maxx <- max(bplot[, 3])
    minx <- min(bplot[, 3])
    row.names(bplot) <- c(row.names(MGEN), row.names(MENV))
    cp.name <- rownames(SSAMMI)[1:3]
    cp.per <- SSAMMI[1:3, 1]
    forFilename <- "GGE"
    pcScoreFilename <- paste(getwd(), "/", forFilename, "_PC_scores_", 
        yVar, ".csv", sep = "")
    bplot_print <- data.frame(Levels = rownames(bplot), bplot[, 
        1:4])
    write.csv(bplot_print, file = pcScoreFilename, row.names = FALSE)
    Type.code <- as.numeric(bplot$type)
    if (f == 0.5 && graphSym) {
        if (mean(MENV$PC1) < 0) {
            MENV$PC1 <- -MENV$PC1
            MENV$PC2 <- -MENV$PC2
            MGEN$PC1 <- -MGEN$PC1
            MGEN$PC2 <- -MGEN$PC2
            bplot_new <- rbind(MGEN, MENV)
            minx_new <- min(bplot_new[, 3])
            maxx_new <- max(bplot_new[, 3])
        }
        else {
            bplot_new <- bplot
            minx_new <- min(bplot[, 3])
            maxx_new <- max(bplot[, 3])
        }
        if (ncol(bplot_new) > 3) {
            png(filename = paste(getwd(), "/", forFilename, "_what-won-where_biplot_", 
                yVar, ".png", sep = ""))
            par(cex = 0.8)
            plot(bplot_new[, 3], bplot_new[, 4], cex = 0.8, xlab = "PC 1", 
                ylab = "PC 2", frame = TRUE, pch = Type.code, 
                col = "white", main = paste("What-won-where Biplot for ", 
                  gsub("_means", "", yVar), sep = ""))
            if (number == TRUE) {
                text(MGEN[, 3], MGEN[, 4], cex = 0, text(MGEN[, 
                  3], MGEN[, 4], labels = as.character(1:nrow(MGEN)), 
                  col = "blue"))
            }
            if (number == FALSE) {
                text(MGEN[, 3], MGEN[, 4], cex = 0, text(MGEN[, 
                  3], MGEN[, 4], labels = row.names(MGEN), col = "blue"))
            }
            points(MENV[, 3], MENV[, 4], cex = 0, text(MENV[, 
                3], MENV[, 4], labels = row.names(MENV), col = "red"))
            s <- seq(length(MENV[, 3]))
            pc_percent <- paste("PC1=", cp.per[1], "%; PC2=", 
                cp.per[2], "%", sep = "")
            mtext(text = pc_percent, side = 3, cex = 0.8, adj = 0)
            bplotGen <- bplot_new[1:ngen, 3:4]
            CH <- chull(bplotGen$PC1, bplotGen$PC2)
            hull <- c(CH, CH[1])
            lines(bplot_new[hull, 3:4], col = "green")
            polygonPoints <- bplot_new[hull, 3:4]
            for (l in 1:(nrow(polygonPoints) - 1)) {
                slope <- (polygonPoints[l, 2] - polygonPoints[l + 
                  1, 2])/(polygonPoints[l, 1] - polygonPoints[l + 
                  1, 1])
                perp.slope = -1/slope
                yhat <- perp.slope * max(polygonPoints[l, 1], 
                  polygonPoints[l + 1, 1])
                yhat2 <- perp.slope * min(polygonPoints[l, 1], 
                  polygonPoints[l + 1, 1])
                if (polygonPoints[l, 1] < 0 && polygonPoints[l + 
                  1, 1] < 0) {
                  if (slope > 0) {
                    if (yhat < max(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 > min(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                  else {
                    if (yhat > min(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 < max(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                }
                else if (polygonPoints[l, 1] > 0 && polygonPoints[l + 
                  1, 1] > 0) {
                  if (slope > 0) {
                    if (yhat < max(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 > min(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                  else {
                    if (yhat > min(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 < max(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                }
                else if (polygonPoints[l, 2] > 0 && polygonPoints[l + 
                  1, 2] > 0) {
                  if (slope > 0) {
                    if (yhat < max(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 > min(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                  else {
                    if (yhat > min(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 < max(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                }
                else if (polygonPoints[l, 2] < 0 && polygonPoints[l + 
                  1, 2] < 0) {
                  if (slope > 0) {
                    if (yhat < max(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 > min(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                  else {
                    if (yhat > min(polygonPoints[l, 2], polygonPoints[l + 
                      1, 2]) && yhat2 < max(polygonPoints[l, 
                      2], polygonPoints[l + 1, 2])) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                }
                else {
                  yIntercept <- (-1 * polygonPoints[l, 2]) - 
                    (slope * polygonPoints[l, 1])
                  xIntercept <- ((-1 * polygonPoints[l, 2]) + 
                    (slope * polygonPoints[l, 1]))/slope
                  if (slope > 0) {
                    if (yIntercept > 0 && xIntercept < 0) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                    else {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                  else {
                    if (yIntercept < 0 && xIntercept < 0) {
                      xPoints <- seq(minx_new * 1.7, 0, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                    else {
                      xPoints <- seq(0, maxx_new * 1.7, length = 20)
                      yPoints <- perp.slope * xPoints
                      lines(xPoints, yPoints, lty = 1, col = 1)
                    }
                  }
                }
            }
            dev.off()
        }
    }
    if (f == 0 && graphEnv) {
        scores <- bplot[c(1:5)]
        scores$Name <- rownames(scores)
        rownames(scores) <- NULL
        geno <- scores[scores$type == "GEN", c(2:6)]
        env <- scores[scores$type == "ENV", c(2:6)]
        g <- length(levels(factor(geno$Name)))
        e <- length(levels(factor(env$Name)))
        if (mean(env$PC1) < 0) {
            env$PC1 <- -env$PC1
            env$PC2 <- -env$PC2
            geno$PC1 <- -geno$PC1
            geno$PC2 <- -geno$PC2
        }
        max_env_PC1 <- max(abs(env$PC1))
        max_geno_PC1 <- max(abs(geno$PC1))
        arb_mult <- max_env_PC1/max_geno_PC1
        geno$PC1 <- geno$PC1 * arb_mult
        geno$PC2 <- geno$PC2 * arb_mult
        png(filename = paste(getwd(), "/", forFilename, "_biplot_EnvtView1_", 
            yVar, ".png", sep = ""))
        par(cex = 0.8)
        plot(env$PC1, env$PC2, cex = 0.8, ylim = c(min(geno$PC2, 
            env$PC2), max(geno$PC2, env$PC2)), xlim = c(min(geno$PC1, 
            env$PC1), max(geno$PC1, env$PC1)), pch = " ", main = paste(forFilename, 
            " Biplot-Environment View for ", gsub("_means", "", 
                yVar), sep = ""), frame = TRUE, xlab = "PC1", 
            ylab = "PC2")
        points(geno$PC1, geno$PC2, cex = 0.8, pch = " ")
        text(env$PC1, env$PC2, labels = env$Name, col = "red", 
            cex = 0.8)
        text(geno$PC1, geno$PC2, labels = geno$Name, col = "blue", 
            cex = 0.8)
        abline(h = 0, v = 0, lty = 1, col = "green")
        s <- seq(e)
        arrows(0, 0, 0.99 * (env$PC1[s]), 0.99 * env$PC2[s], 
            col = "brown", lwd = 1, length = 0.05)
        spokes <- sqrt(env$PC1^2 + env$PC2^2)
        spokes.df <- cbind(env, spokes)
        max_length <- max(spokes)
        env_maxSpoke <- spokes.df[spokes.df[, "spokes"] == max_length, 
            "Name"]
        library(plotrix)
        draw.circle(0, 0, 0.15 * max_length, lty = 2)
        draw.circle(0, 0, 0.3 * max_length, lty = 2)
        draw.circle(0, 0, 0.45 * max_length, lty = 2)
        draw.circle(0, 0, 0.6 * max_length, lty = 2)
        draw.circle(0, 0, 0.75 * max_length, lty = 2)
        pc_percent <- paste("PC1=", cp.per[1], "%; PC2=", cp.per[2], 
            "%", sep = "")
        mtext(text = pc_percent, side = 3, cex = 0.8, adj = 0)
        dev.off()
        AE.PC1 <- mean(env$PC1)
        AE.PC2 <- mean(env$PC2)
        slope.AEA <- AE.PC2/AE.PC1
        max.x <- max(geno$PC1, env$PC1) + abs(0.07 * (max(geno$PC1, 
            env$PC1) - min(geno$PC1, env$PC1)))
        min.x <- min(geno$PC1, env$PC1) - abs(0.07 * (max(geno$PC1, 
            env$PC1) - min(geno$PC1, env$PC1)))
        spokes <- sqrt(env$PC1^2 + env$PC2^2)
        spokes.df <- cbind(env, spokes)
        max_length <- max(spokes)
        env_maxSpoke <- spokes.df[spokes.df[, "spokes"] == max_length, 
            "Name"]
        hyp.AE <- sqrt(AE.PC1^2 + AE.PC2^2)
        hyp.BE <- sqrt(env$PC1[env$Name == env_maxSpoke]^2 + 
            env$PC2[env$Name == env_maxSpoke]^2)
        angle <- asin(AE.PC1/hyp.AE)
        L.ideal <- hyp.BE * cos(angle)
        H.ideal <- L.ideal * tan(angle)
        if (slope.AEA < 0) {
            L.ideal_new <- (L.ideal * -1)
        }
        else {
            L.ideal_new <- L.ideal
        }
        minY.graph <- min(env$PC2, geno$PC2, L.ideal_new)
        maxY.graph <- max(env$PC2, geno$PC2, L.ideal_new)
        minX.graph <- min(env$PC1, geno$PC1, min.x, H.ideal)
        maxX.graph <- max(env$PC1, geno$PC1, max.x, H.ideal)
        png(filename = paste(getwd(), "/", forFilename, "_biplot_EnvtView2_", 
            yVar, ".png", sep = ""))
        par(mar = c(5, 4, 4, 7) + 0.1, cex = 0.8)
        plot(env$PC1, env$PC2, cex = 0.8, ylim = c(minY.graph, 
            maxY.graph), xlim = c(minX.graph, maxX.graph), pch = " ", 
            main = paste(forFilename, " Biplot-Environment View for ", 
                gsub("_means", "", yVar), sep = ""), frame = TRUE, 
            xlab = "PC1", ylab = "PC2")
        points(geno$PC1, geno$PC2, cex = 0.8, pch = " ")
        text(env$PC1, env$PC2, labels = env$Name, col = "red", 
            cex = 0.8)
        text(geno$PC1, geno$PC2, labels = geno$Name, col = "blue", 
            cex = 0.8)
        abline(h = 0, v = 0, lty = 1, col = "green")
        s <- seq(e)
        arrows(0, 0, 0.99 * (env$PC1[s]), 0.99 * env$PC2[s], 
            col = "brown", lwd = 1, length = 0.05)
        points(AE.PC1, AE.PC2, cex = 2, pch = 1, col = "red")
        arrows(0, 0, 0.99 * (AE.PC1), 0.99 * AE.PC2, col = "red", 
            lwd = 2, length = 0.1)
        if (slope.AEA < 0) {
            if (slope.AEA * minX.graph < maxY.graph) {
                arrows(0, 0, minX.graph, slope.AEA * minX.graph, 
                  col = "red", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "red", lwd = 1, lty = 2, 
                  length = 0)
                text(minX.graph, slope.AEA * minX.graph, labels = "AEA", 
                  col = "red", cex = 0.9)
            }
            else {
                arrows(0, 0, solve(slope.AEA, maxY.graph), maxY.graph, 
                  col = "red", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "red", lwd = 1, lty = 2, 
                  length = 0)
                text(solve(slope.AEA, maxY.graph), maxY.graph, 
                  labels = "AEA", col = "red", cex = 0.9)
            }
        }
        else {
            if (slope.AEA * minX.graph > minY.graph) {
                arrows(0, 0, minX.graph, slope.AEA * minX.graph, 
                  col = "red", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "red", lwd = 1, lty = 2, 
                  length = 0)
                text(minX.graph, slope.AEA * minX.graph, labels = "AEA", 
                  col = "red", cex = 0.9)
            }
            else {
                arrows(0, 0, solve(slope.AEA, minY.graph), minY.graph, 
                  col = "red", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "red", lwd = 1, lty = 2, 
                  length = 0)
                text(solve(slope.AEA, minY.graph), minY.graph, 
                  labels = "AEA", col = "red", cex = 0.9)
            }
        }
        points(H.ideal, L.ideal_new, cex = 1.3, pch = 16, col = "blue")
        draw.circle(H.ideal, L.ideal_new, 0.15 * max_length, 
            lty = 2)
        draw.circle(H.ideal, L.ideal_new, 0.3 * max_length, lty = 2)
        draw.circle(H.ideal, L.ideal_new, 0.45 * max_length, 
            lty = 2)
        pc_percent <- paste("PC1=", cp.per[1], "%; PC2=", cp.per[2], 
            "%", sep = "")
        mtext(text = pc_percent, side = 3, cex = 0.8, adj = 0)
        par(xpd = TRUE)
        legend(maxX.graph + abs(0.07 * (maxX.graph - minX.graph)), 
            minY.graph + abs(0.1 * (maxY.graph - minY.graph)), 
            legend = c("Average Env", "Ideal Env"), title = "Legend:", 
            pch = c(1, 16), pt.cex = 1.5, col = c("red", "blue"), 
            cex = 0.8)
        par(xpd = FALSE)
        dev.off()
    }
    if (f == 1 && graphGeno) {
        library(plotrix)
        scores <- bplot[c(1:5)]
        scores$Name <- rownames(scores)
        rownames(scores) <- NULL
        geno <- scores[scores$type == "GEN", c(2:6)]
        env <- scores[scores$type == "ENV", c(2:6)]
        g <- length(levels(factor(geno$Name)))
        e <- length(levels(factor(env$Name)))
        if (mean(env$PC1) < 0) {
            env$PC1 <- -env$PC1
            env$PC2 <- -env$PC2
            geno$PC1 <- -geno$PC1
            geno$PC2 <- -geno$PC2
        }
        max_env_PC1 <- max(abs(env$PC1))
        max_geno_PC1 <- max(abs(geno$PC1))
        arb_mult <- max_geno_PC1/max_env_PC1
        env$PC1 <- env$PC1 * arb_mult
        env$PC2 <- env$PC2 * arb_mult
        png(filename = paste(getwd(), "/", forFilename, "_biplot_GenoView1_", 
            yVar, ".png", sep = ""))
        par(cex = 0.8)
        plot(env$PC1, env$PC2, ylim = c(min(geno$PC2, env$PC2), 
            max(geno$PC2, env$PC2)), xlim = c(min(geno$PC1, env$PC1), 
            max(geno$PC1, env$PC1)), cex = 0.8, pch = " ", main = paste(forFilename, 
            " Biplot-Genotype View for ", gsub("_means", "", 
                yVar), sep = ""), frame = TRUE, xlab = "PC1", 
            ylab = "PC2")
        points(geno$PC1, geno$PC2, cex = 0.8, pch = " ")
        text(env$PC1, env$PC2, labels = env$Name, col = "red", 
            cex = 0.8)
        text(geno$PC1, geno$PC2, labels = geno$Name, col = "blue", 
            cex = 0.8)
        abline(h = 0, v = 0, lty = 1, col = "green")
        s <- seq(g)
        arrows(0, 0, 0.99 * (geno$PC1[s]), 0.99 * geno$PC2[s], 
            col = "brown", lwd = 1, length = 0.05)
        spokes <- sqrt(geno$PC1^2 + geno$PC2^2)
        max_length <- max(spokes)
        draw.circle(0, 0, 0.15 * max_length, lty = 2)
        draw.circle(0, 0, 0.3 * max_length, lty = 2)
        draw.circle(0, 0, 0.45 * max_length, lty = 2)
        draw.circle(0, 0, 0.6 * max_length, lty = 2)
        draw.circle(0, 0, 0.75 * max_length, lty = 2)
        pc_percent <- paste("PC1=", cp.per[1], "%; PC2=", cp.per[2], 
            "%", sep = "")
        mtext(text = pc_percent, side = 3, cex = 0.8, adj = 0)
        dev.off()
        scores <- bplot[c(1:5)]
        scores$Name <- rownames(scores)
        rownames(scores) <- NULL
        geno <- scores[scores$type == "GEN", c(2:6)]
        env <- scores[scores$type == "ENV", c(2:6)]
        g <- length(levels(factor(geno$Name)))
        e <- length(levels(factor(env$Name)))
        if (mean(env$PC1) < 0) {
            env$PC1 <- -env$PC1
            env$PC2 <- -env$PC2
            geno$PC1 <- -geno$PC1
            geno$PC2 <- -geno$PC2
        }
        max_env_PC1 <- max(abs(env$PC1))
        max_geno_PC1 <- max(abs(geno$PC1))
        arb_mult <- max_geno_PC1/max_env_PC1
        env$PC1 <- env$PC1 * arb_mult
        env$PC2 <- env$PC2 * arb_mult
        AE.PC1 <- mean(env$PC1)
        AE.PC2 <- mean(env$PC2)
        slope.AEA <- AE.PC2/AE.PC1
        max.x <- max(geno$PC1, env$PC1) + abs(0.07 * (max(geno$PC1, 
            env$PC1) - min(geno$PC1, env$PC1)))
        min.x <- min(geno$PC1, env$PC1) - abs(0.07 * (max(geno$PC1, 
            env$PC1) - min(geno$PC1, env$PC1)))
        max.y <- max(geno$PC2, env$PC2) + abs(0.07 * (max(geno$PC2, 
            env$PC2) - min(geno$PC2, env$PC2)))
        min.y <- min(geno$PC2, env$PC2) - abs(0.07 * (max(geno$PC2, 
            env$PC2) - min(geno$PC2, env$PC2)))
        geno$Name <- factor(geno$Name)
        perp.slope = -1/slope.AEA
        m1 <- c(0, slope.AEA)
        spokes <- rep(0, g)
        geno.solu = NULL
        for (i in 1:g) {
            m2 <- c(geno$PC2[geno$Name == levels(geno$Name)[i]] - 
                geno$PC1[geno$Name == levels(geno$Name)[i]] * 
                  perp.slope, perp.slope)
            a <- m1 - m2
            cm <- rbind(m1, m2)
            solu <- c(-solve(cbind(cm[, 2], -1)) %*% cm[, 1])
            spokes[i] <- sqrt(solu[1]^2 + solu[2]^2)
            geno.solu_new <- data.frame(Name = levels(geno$Name)[i], 
                PC1 = geno$PC1[geno$Name == levels(geno$Name)[i]], 
                PC2 = geno$PC2[geno$Name == levels(geno$Name)[i]], 
                solu1 = solu[1], solu2 = solu[2], spokes = spokes[i])
            geno.solu <- rbind(geno.solu, geno.solu_new)
        }
        geno.solu.positive <- geno.solu[geno.solu[, "PC1"] > 
            0, ]
        max.spoke <- max(geno.solu.positive$spokes)
        spokes <- as.data.frame(cbind(1:g, spokes))
        max.spoke2 <- spokes[spokes$spokes == max.spoke, ]
        m2 <- c(geno$PC2[max.spoke2$V1] - geno$PC1[max.spoke2$V1] * 
            perp.slope, perp.slope)
        a <- m1 - m2
        cm <- rbind(m1, m2)
        solu <- c(-solve(cbind(cm[, 2], -1)) %*% cm[, 1])
        minY.graph <- min(geno$PC2, env$PC2, geno.solu$solu2, 
            solu[2], min.y)
        maxY.graph <- max(geno$PC2, env$PC2, geno.solu$solu2, 
            solu[2], max.y)
        minX.graph <- min(geno$PC1, env$PC1, geno.solu$solu1, 
            solu[1], min.x)
        maxX.graph <- max(geno$PC1, env$PC1, geno.solu$solu1, 
            solu[1], max.x)
        png(filename = paste(getwd(), "/", forFilename, "_biplot_GenoView2_", 
            yVar, ".png", sep = ""))
        par(mar = c(5, 4, 4, 7) + 0.1, cex = 0.8)
        plot(env$PC1, env$PC2, ylim = c(minY.graph, maxY.graph), 
            xlim = c(minX.graph, maxX.graph), cex = 0.8, pch = 1, 
            col = "red", main = paste(forFilename, " Biplot-Genotype View for ", 
                gsub("_means", "", yVar), sep = ""), frame = TRUE, 
            xlab = "PC1", ylab = "PC2")
        points(geno$PC1, geno$PC2, cex = 0.8, pch = " ", col = 4)
        text(geno$PC1, geno$PC2, labels = geno$Name, col = 4, 
            cex = 0.8)
        points(AE.PC1, AE.PC2, cex = 1.8, pch = 1, col = "blue")
        arrows(0, 0, 0.95 * (AE.PC1), 0.95 * AE.PC2, col = "blue", 
            lwd = 2, length = 0.05)
        if (slope.AEA < 0) {
            if (slope.AEA * minX.graph < maxY.graph) {
                arrows(0, 0, minX.graph, slope.AEA * minX.graph, 
                  col = "blue", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "blue", lwd = 1, 
                  lty = 2, length = 0)
                text(minX.graph, slope.AEA * minX.graph, labels = "AEA", 
                  col = "blue", cex = 0.9)
            }
            else {
                arrows(0, 0, solve(slope.AEA, maxY.graph), maxY.graph, 
                  col = "blue", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "blue", lwd = 1, 
                  lty = 2, length = 0)
                text(solve(slope.AEA, maxY.graph), maxY.graph, 
                  labels = "AEA", col = "blue", cex = 0.9)
            }
        }
        else {
            if (slope.AEA * minX.graph > minY.graph) {
                arrows(0, 0, minX.graph, slope.AEA * minX.graph, 
                  col = "blue", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "blue", lwd = 1, 
                  lty = 2, length = 0)
                text(minX.graph, slope.AEA * minX.graph, labels = "AEA", 
                  col = "blue", cex = 0.9)
            }
            else {
                arrows(0, 0, solve(slope.AEA, minY.graph), minY.graph, 
                  col = "blue", lwd = 1, lty = 2, length = 0)
                arrows(0, 0, (maxX.graph * 1.1), slope.AEA * 
                  (maxX.graph * 1.1), col = "blue", lwd = 1, 
                  lty = 2, length = 0)
                text(solve(slope.AEA, minY.graph), minY.graph, 
                  labels = "AEA", col = "blue", cex = 0.9)
            }
        }
        if ((-1/slope.AEA) > 0) {
            if ((-1/slope.AEA) * minX.graph > minY.graph) {
                arrows(0, 0, minX.graph, (-1/slope.AEA) * minX.graph, 
                  col = "blue", lwd = 2, length = 0.15)
            }
            else {
                arrows(0, 0, solve((-1/slope.AEA), minY.graph), 
                  minY.graph, col = "blue", lwd = 2, length = 0.15)
            }
            if ((-1/slope.AEA) * maxX.graph < maxY.graph) {
                arrows(0, 0, maxX.graph, (-1/slope.AEA) * maxX.graph, 
                  col = "blue", lwd = 2, length = 0.15)
            }
            else {
                arrows(0, 0, solve((-1/slope.AEA), maxY.graph), 
                  maxY.graph, col = "blue", lwd = 2, length = 0.15)
            }
        }
        else {
            if ((-1/slope.AEA) * minX.graph < maxY.graph) {
                arrows(0, 0, minX.graph, (-1/slope.AEA) * minX.graph, 
                  col = "blue", lwd = 2, length = 0.15)
            }
            else {
                arrows(0, 0, solve((-1/slope.AEA), maxY.graph), 
                  maxY.graph, col = "blue", lwd = 2, length = 0.15)
            }
            if ((-1/slope.AEA) * maxX.graph > minY.graph) {
                arrows(0, 0, maxX.graph, (-1/slope.AEA) * maxX.graph, 
                  col = "blue", lwd = 2, length = 0.15)
            }
            else {
                arrows(0, 0, solve((-1/slope.AEA), minY.graph), 
                  minY.graph, col = "blue", lwd = 2, length = 0.15)
            }
        }
        for (i in 1:g) {
            arrows(geno.solu$PC1[i], geno.solu$PC2[i], geno.solu$solu1[i], 
                geno.solu$solu2[i], col = "black", lwd = 1, lty = 1, 
                length = 0)
        }
        points(solu[1], solu[2], cex = 1.8, pch = 16, col = "blue")
        draw.circle(solu[1], solu[2], 0.15 * max.spoke2$spokes, 
            lty = 2)
        draw.circle(solu[1], solu[2], 0.3 * max.spoke2$spokes, 
            lty = 2)
        draw.circle(solu[1], solu[2], 0.45 * max.spoke2$spokes, 
            lty = 2)
        draw.circle(solu[1], solu[2], 0.6 * max.spoke2$spokes, 
            lty = 2)
        pc_percent <- paste("PC1=", cp.per[1], "%; PC2=", cp.per[2], 
            "%", sep = "")
        mtext(text = pc_percent, side = 3, cex = 0.8, adj = 0)
        par(xpd = TRUE)
        legend(maxX.graph + abs(0.07 * (maxX.graph - minX.graph)), 
            minY.graph + abs(0.1 * (maxY.graph - minY.graph)), 
            legend = c("Average Env", "Ideal Geno"), title = "Legend:", 
            pch = c(1, 16), pt.cex = 1.5, col = c("blue", "blue"), 
            cex = 0.8)
        par(xpd = FALSE)
        dev.off()
    }
    return(list(genXenv = OUTRES2, analysis = SSAMMI, means = MEDIAS, 
        biplot = bplot))
}
