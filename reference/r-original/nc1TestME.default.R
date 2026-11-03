`nc1TestME.default` <-
function (design = c("CRD", "RCB", "Alpha", "RowColumn"), data, 
    respvar, female, male, rep = NULL, block = NULL, row = NULL, 
    column = NULL, inbred = TRUE, individual = NULL, environment) 
{
    options(show.signif.stars = FALSE)
    data <- eval(parse(text = data))
    library(lme4)
    respvar <- trimStrings(respvar)
    female <- trimStrings(female)
    male <- trimStrings(male)
    if (!is.null(block)) {
        block <- trimStrings(block)
    }
    if (!is.null(rep)) {
        rep <- trimStrings(rep)
    }
    if (!is.null(row)) {
        row <- trimStrings(row)
    }
    if (!is.null(column)) {
        column <- trimStrings(column)
    }
    if (!is.null(individual)) {
        individual <- trimStrings(individual)
    }
    environment <- trimStrings(environment)
    if (design == "CRD") {
        designName <- "CRD"
    }
    if (design == "RCB") {
        designName <- "RCB"
    }
    if (design == "Alpha") {
        designName <- "ALPHA-LATTICE"
    }
    if (design == "RowColumn") {
        designName <- "ROW-COLUMN"
    }
    if (inbred) {
        parentsType <- "INBRED"
    }
    else {
        parentsType <- "CROSS"
    }
    cat("\nMULTIPLE ENVIRONMENT ANALYSIS\n")
    cat("\nDESIGN: NORTH CAROLINA EXPERIMENT I IN ", designName, 
        " (", parentsType, ")\n", sep = "")
    result <- list()
    for (i in (1:length(respvar))) {
        result[[i]] <- list()
        temp.data <- data[(is.na(data[, match(respvar[i], names(data))]) == 
            FALSE), ]
        cat("\n-----------------------------")
        cat("\nRESPONSE VARIABLE: ", respvar[i], "\n", sep = "")
        cat("-----------------------------\n")
        responseRate <- (nrow(temp.data)/nrow(data))
        if (responseRate < 0.8) {
            cat("\nToo many missing observations. Cannot proceed with the analysis.\n\n")
            next
        }
        else {
            temp.data[, match(female, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(female, names(temp.data))]))
            temp.data[, match(male, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(male, names(temp.data))]))
            temp.data[, match(environment, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(environment, names(temp.data))]))
            f <- nlevels(temp.data[, match(female, names(temp.data))])
            m <- nlevels(temp.data[, match(male, names(temp.data))])
            e <- nlevels(temp.data[, match(environment, names(temp.data))])
            temp.data$cross <- factor(paste(temp.data[, male], 
                ":", temp.data[, female], sep = ""))
            levelsMaleFemale <- levels(temp.data$cross)
            t <- nlevels(temp.data$cross)
            lengthPerCross <- tapply(temp.data[, respvar[i]], 
                temp.data[, c("cross", environment)], length)
            repHarmonicMean <- 1/mean(1/lengthPerCross, na.rm = TRUE)
            if (design == "CRD") {
                nlevelsRep <- max(lengthPerCross, na.rm = TRUE)
                nBalance <- t * e * nlevelsRep
            }
            if (design == "RCB") {
                temp.data[, match(block, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(block, names(temp.data))]))
                levelsRep <- levels(temp.data[, match(block, 
                  names(temp.data))])
                levelsEnv <- levels(temp.data[, match(environment, 
                  names(temp.data))])
                nlevelsRep <- nlevels(temp.data[, match(block, 
                  names(temp.data))])
                nBalance <- t * e * nlevelsRep
            }
            if (design == "Alpha") {
                temp.data[, match(rep, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(rep, names(temp.data))]))
                temp.data[, match(block, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(block, names(temp.data))]))
                nlevelsRep <- nlevels(temp.data[, match(rep, 
                  names(temp.data))])
                nBalance <- t * e * nlevelsRep
            }
            if (design == "RowColumn") {
                temp.data[, match(rep, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(rep, names(temp.data))]))
                temp.data[, match(row, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(row, names(temp.data))]))
                temp.data[, match(column, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(column, names(temp.data))]))
                nlevelsRep <- nlevels(temp.data[, match(rep, 
                  names(temp.data))])
                nBalance <- t * e * nlevelsRep
            }
            temp.data <- temp.data[-c(match("cross", names(temp.data)))]
            funcTrialSum <- class.information2(names(temp.data), 
                temp.data)
            cat("\nDATA SUMMARY: ", "\n\n", sep = "")
            print(funcTrialSum)
            cat("\n Number of observations read: ", nrow(data), 
                sep = "")
            cat("\n Number of observations used: ", nrow(temp.data), 
                sep = "")
            missingObs <- nBalance - nrow(temp.data)
            if (design == "CRD" || design == "RCB") {
                cat("\n\n\nANOVA TABLE:\n\n")
                if ((nrow(temp.data)/nBalance) >= 0.9) {
                  if (nrow(temp.data) == nBalance) {
                    anovaRemark <- "REMARK: Raw dataset is balanced."
                    dataForAnova <- temp.data
                  }
                  else {
                    if (design == "CRD") {
                      dataForAnova <- temp.data
                      anovaRemark <- "REMARK: Raw dataset is unbalanced."
                    }
                    if (design == "RCB") {
                      fullTrtCombi <- NULL
                      for (z in 1:length(levelsEnv)) {
                        for (x in 1:length(levelsMaleFemale)) {
                          for (y in 1:length(levelsRep)) {
                            newRow <- c(levelsEnv[z], strsplit(levelsMaleFemale[x], 
                              ":", fixed = TRUE)[[1]][1], strsplit(levelsMaleFemale[x], 
                              ":", fixed = TRUE)[[1]][2], levelsRep[y])
                            fullTrtCombi <- rbind(fullTrtCombi, 
                              newRow)
                          }
                        }
                      }
                      colnames(fullTrtCombi) <- c(environment, 
                        male, female, block)
                      rownames(fullTrtCombi) <- c(1:nrow(fullTrtCombi))
                      fullTrtCombi <- data.frame(fullTrtCombi)
                      if (nrow(temp.data) < nrow(fullTrtCombi)) {
                        balancedData <- merge(temp.data, fullTrtCombi, 
                          by = c(environment, male, female, block), 
                          all.x = TRUE, all.y = TRUE)
                      }
                      balancedData$Cross <- eval(parse(text = paste("balancedData[,'", 
                        male, "']:balancedData[,'", female, "']", 
                        sep = "")))
                      dataForAnova <- NULL
                      for (v in (1:e)) {
                        balanceSplit <- balancedData[balancedData[, 
                          environment] == levels(temp.data[, 
                          match(environment, names(temp.data))])[v], 
                          ]
                        balanceSplit <- balanceSplit[, c(male, 
                          female, block, respvar[i], "Cross")]
                        tempDataForAnova <- estimateMissingData(design = "RCB", 
                          data = balanceSplit, respvar[i], male, 
                          female, block)
                        tempDataForAnova <- tempDataForAnova[, 
                          c(male, female, block, respvar[i])]
                        tempDataForAnova$EnvIndex <- levels(temp.data[, 
                          match(environment, names(temp.data))])[v]
                        dataForAnova <- rbind(dataForAnova, tempDataForAnova)
                      }
                      colnames(dataForAnova)[match("EnvIndex", 
                        names(dataForAnova))] <- environment
                      dataForAnova[, match(environment, names(dataForAnova))] <- factor(dataForAnova[, 
                        match(environment, names(dataForAnova))])
                      anovaRemark <- "REMARK: Raw data and estimates of the missing values are used."
                    }
                  }
                  if (design == "CRD") {
                    myformula <- paste(respvar[i], " ~ ", environment, 
                      " + ", male, "/", female, " + ", environment, 
                      ":", male, " + ", environment, ":", female, 
                      ":", male, sep = "")
                    anovaNested <- summary(aov(formula(myformula), 
                      data = dataForAnova))
                    anovaNested.temp <- summary(aov(formula(myformula), 
                      data = temp.data))
                    anovaNested <- adjustAnovaDf(anovaNested, 
                      anovaNested.temp[[1]]$Df)
                  }
                  if (design == "RCB") {
                    myformula <- paste(respvar[i], " ~ ", environment, 
                      " + ", environment, ":", block, " + ", 
                      male, "/", female, " + ", environment, 
                      ":", male, " + ", environment, ":", male, 
                      ":", female, sep = "")
                    anovaNested <- summary(aov(formula(myformula), 
                      data = dataForAnova))
                    anovaNested.temp <- summary(aov(formula(myformula), 
                      data = temp.data))
                    anovaNested.temp[[1]]$Df[length(anovaNested.temp[[1]]$Df)] <- anovaNested[[1]]$Df[length(anovaNested[[1]]$Df)] - 
                      missingObs
                    anovaNested <- adjustAnovaDf(anovaNested, 
                      anovaNested.temp[[1]]$Df)
                    index <- match(paste(environment, ":", block, 
                      sep = ""), trimStrings(rownames(anovaNested)))
                    indexEnv <- match(paste(environment), trimStrings(rownames(anovaNested)))
                    anovaNested <- rbind(anovaNested[c(indexEnv, 
                      index), ], anovaNested[-I(match(c(indexEnv, 
                      index), 1:nrow(anovaNested))), ])
                    anovaNested[1, "F value"] <- anovaNested[1, 
                      "Mean Sq"]/anovaNested[2, "Mean Sq"]
                    anovaNested[1, "Pr(>F)"] <- pf(anovaNested[1, 
                      "F value"], anovaNested[1, "Df"], anovaNested[2, 
                      "Df"], lower.tail = FALSE)
                  }
                  anovaFormat <- formatAnovaTable(anovaNested)
                  print(anovaFormat)
                  cat("-------\n")
                  cat(anovaRemark)
                  result[[i]]$nc1.anova <- anovaFormat
                }
                else {
                  anovaRemark <- "ERROR: Too many missing values. Cannot perform ANOVA."
                  cat(anovaRemark)
                }
            }
            if (design == "CRD") {
                myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                  environment, ") + (1|", male, "/", female, 
                  ") + (1|", environment, ":", male, ") + (1|", 
                  environment, ":", female, ":", male, ")", sep = "")
            }
            if (design == "RCB") {
                myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                  environment, ") + (1|", block, ":", environment, 
                  ") + (1|", male, "/", female, ") + (1|", environment, 
                  ":", male, ") + (1|", environment, ":", female, 
                  ":", male, ")", sep = "")
            }
            if (design == "Alpha") {
                myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                  environment, ") + (1|", environment, ":", rep, 
                  ") + (1|", environment, ":", rep, ":", block, 
                  ") + (1|", male, "/", female, ") + (1|", environment, 
                  ":", male, ") + (1|", environment, ":", female, 
                  ":", male, ")", sep = "")
            }
            if (design == "RowColumn") {
                myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                  environment, ") + (1|", environment, ":", rep, 
                  ") + (1|", environment, ":", rep, ":", row, 
                  ") + (1|", environment, ":", rep, ":", column, 
                  ") + (1|", male, "/", female, ") + (1|", environment, 
                  ":", male, ") + (1|", environment, ":", female, 
                  ":", male, ")", sep = "")
            }
            model <- lmer(formula(myformula1), data = temp.data)
            result[[i]]$lmer.result <- summary(model)
            remat <- summary(model)@REmat
            Groups <- remat[, 1]
            Variance <- formatC(as.numeric(remat[, 3]), format = "f")
            Std.Deviation <- formatC(as.numeric(remat[, 4]), 
                format = "f")
            Variance2 <- format(rbind("Variance", cbind(Variance)), 
                justify = "right")
            Std.Deviation2 <- format(rbind("Std. Deviation", 
                cbind(Std.Deviation)), justify = "right")
            Groups2 <- format(rbind("Groups", cbind(Groups)), 
                justify = "left")
            rematNew <- noquote(cbind(Groups2, Variance2, Std.Deviation2))
            colnames(rematNew) <- c("", "", "")
            rownames(rematNew) <- rep("", nrow(rematNew))
            cat("\n\n\nLINEAR MIXED MODEL FIT BY RESTRICTED MAXIMUM LIKELIHOOD:\n\n")
            cat(" Formula: ", myformula1, "\n\n")
            print(summary(model)@AICtab)
            cat("\n Random Effects:")
            print(rematNew)
            varcomp <- summary(model)@REmat
            Ve <- as.numeric(varcomp[varcomp[, 1] == "Residual", 
                "Variance"])
            Vef_m <- as.numeric(varcomp[varcomp[, 1] == paste(environment, 
                ":", female, ":", male, sep = ""), "Variance"])
            Vem <- as.numeric(varcomp[varcomp[, 1] == paste(environment, 
                ":", male, sep = ""), "Variance"])
            Vf_m <- as.numeric(varcomp[varcomp[, 1] == paste(female, 
                ":", male, sep = ""), "Variance"])
            Vm <- as.numeric(varcomp[varcomp[, 1] == male, "Variance"])
            if (inbred) {
                F <- 1
            }
            else {
                F <- 0
            }
            VA <- (4/(1 + F)) * Vm
            VD <- (4/(1 + F)^2) * (Vf_m - Vm)
            VAE <- (4/(1 + F)) * Vem
            VDE <- (4/(1 + F)^2) * (Vef_m - Vem)
            if (VD < 0 || VD < 1e-10) 
                VD <- 0
            if (VA < 0 || VA < 1e-10) 
                VA <- 0
            if (VAE < 0 || VAE < 1e-10) 
                VAE <- 0
            if (VDE < 0 || VDE < 1e-10) 
                VDE <- 0
            VE <- Ve
            VP <- VA + VD + VAE + VDE + VE
            h2B <- (VA + VD)/VP
            h2N <- VA/VP
            Dominance.ratio <- sqrt(2 * VD/VA)
            VA_p <- formatNumericValue(VA)
            VAE_p <- formatNumericValue(VAE)
            VD_p <- formatNumericValue(VD)
            VDE_p <- formatNumericValue(VDE)
            h2N_p <- formatNumericValue(h2N)
            h2B_p <- formatNumericValue(h2B)
            Dominance.ratio_p <- formatNumericValue(Dominance.ratio)
            Estimate <- rbind(VA_p, VAE_p, VD_p, VDE_p, h2N_p, 
                h2B_p, Dominance.ratio_p)
            with.colheader <- format(rbind("Estimate", Estimate), 
                justify = "right")
            colnames(with.colheader) <- c("")
            rownames(with.colheader) <- c("", " VA", " VAxE", 
                " VD", " VDxE", " h2-narrow sense", " H2-broad sense", 
                " Dominance Ratio")
            TABLE <- as.table(with.colheader)
            cat("\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n")
            print(TABLE)
            result[[i]]$genvar.components <- TABLE
            r <- repHarmonicMean
            H2fm <- Vm/(Vm + Vf_m/f + Vem/e + Vef_m/(f * e) + 
                Ve/(e * r * f))
            H2ff <- Vf_m/(Vf_m + Vef_m/e + Ve/(e * r))
            H2ffs <- (Vm + Vf_m)/(Vm + Vf_m + Vem/e + Vef_m/e + 
                Ve/(e * r))
            h2m <- (2/(1 + F)) * Vm/(Vm + Vf_m + Vem + Vef_m + 
                Ve)
            H2m <- ((4/(1 + F)) * Vm + (4/(1 + F)^2) * (Vf_m - 
                Vm))/(Vm + Vf_m + Vem + Vef_m + Ve)
            h2f <- (4/(1 + F)) * Vf_m/(Vm + Vf_m + Vem + Vef_m + 
                Ve)
            H2f <- ((4/(1 + F)) * Vf_m + (4/(1 + F)^2) * (Vf_m - 
                Vm))/(Vm + Vf_m + Vem + Vef_m + Ve)
            h2fs <- (2/(1 + F)) * (Vm + Vf_m)/(Vm + Vf_m + Vem + 
                Vef_m + Ve)
            H2fs <- ((2/(1 + F)) * (Vm + Vf_m) + (4/(1 + F)^2) * 
                (Vf_m - Vm))/(Vm + Vf_m + Vem + Vef_m + Ve)
            rowMale2 <- paste("", male)
            rowFemale2 <- paste("", female)
            family <- round(rbind(H2fm, H2ff, H2ffs), digits = 2)
            narrowsense <- round(rbind(h2m, h2f, h2fs), digits = 2)
            broadsense <- round(rbind(H2m, H2f, H2fs), digits = 2)
            TABLE2 <- as.table(cbind(family, narrowsense, broadsense))
            colnames(TABLE2) <- c("Family Selection", "Narrow Sense", 
                "Broad sense")
            rownames(TABLE2) <- c(rowMale2, rowFemale2, " Full-sib")
            TABLE2_final <- as.table(TABLE2)
            result[[i]]$heritability <- TABLE2_final
            cat("\n")
        }
    }
    cat("\n==============================================================\n")
    detach("package:lme4")
    return(list(output = result))
}
