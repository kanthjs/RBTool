`ssa.test.default` <-
function (exptl.design = c("RCB", "AugRCB", "AugLS", "Alpha", 
    "RowCol", "LatinAlpha", "LatinRowCol"), data, respvar, geno, 
    row, column = NULL, rep = NULL, env = NULL, is.random = FALSE, 
    excludeCheck = FALSE, checkList = NULL) 
{
    options(show.signif.stars = FALSE)
    library(lme4)
    if (exptl.design == "RCB" || exptl.design == "AugRCB") {
        if (anyNA(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || anyNA(match(row, names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "AugLS") {
        if (anyNA(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || anyNA(match(row, names(data))) || 
            anyNA(match(column, names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "Alpha" || exptl.design == "LatinAlpha") {
        if (anyNA(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || anyNA(match(row, names(data))) || 
            anyNA(match(rep, names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "RowCol" || exptl.design == "LatinRowCol") {
        if (anyNA(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || anyNA(match(row, names(data))) || 
            anyNA(match(column, names(data))) || is.na(match(rep, 
            names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (is.null(env)) {
        env = "EnvLevel"
        data <- cbind(data, EnvLevel = 1)
    }
    data[, match(env, names(data))] <- factor(trimStrings(data[, 
        match(env, names(data))]))
    result <- list()
    for (i in (1:length(respvar))) {
        result[[i]] <- list()
        result[[i]]$respvar <- respvar[i]
        for (j in (1:nlevels(data[, match(env, names(data))]))) {
            temp.data <- data
            result[[i]]$site[[j]] <- list()
            result[[i]]$site[[j]]$env <- levels(temp.data[, match(env, 
                names(temp.data))])[j]
            temp.data <- temp.data[temp.data[, match(env, names(temp.data))] == 
                levels(temp.data[, match(env, names(temp.data))])[j], 
                ]
            obsread <- nrow(temp.data)
            result[[i]]$site[[j]]$obsread <- obsread
            temp.data <- subset(temp.data, subset = (is.na(temp.data[, 
                match(respvar[i], names(temp.data))]) == F))
            obsused <- nrow(temp.data)
            result[[i]]$site[[j]]$obsused <- obsused
            temp.data[, match(geno, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(geno, names(temp.data))]))
            temp.data[, match(row, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(row, names(temp.data))]))
            if (exptl.design == "AugLS") {
                temp.data[, match(column, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(column, names(temp.data))]))
            }
            if (exptl.design == "Alpha" || exptl.design == "LatinAlpha") {
                temp.data[, match(rep, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(rep, names(temp.data))]))
            }
            if (exptl.design == "RowCol" || exptl.design == "LatinRowCol") {
                temp.data[, match(column, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(column, names(temp.data))]))
                temp.data[, match(rep, names(temp.data))] <- factor(trimStrings(temp.data[, 
                  match(rep, names(temp.data))]))
            }
            no.reps <- data.frame(n = tapply(eval(parse(text = paste("temp.data$", 
                respvar[i], sep = ""))), eval(parse(text = paste("temp.data$", 
                geno, sep = ""))), FUN = length))
            no.reps <- as.numeric(1/sapply(1/no.reps, mean))
            result[[i]]$site[[j]]$numreps <- no.reps
            if (exptl.design == "LatinRowCol") {
                lengthPerCross <- tapply(temp.data[, respvar[i]], 
                  temp.data[, c(row, column)], length)
                if (all(lengthPerCross <= 1, na.rm = TRUE)) {
                  if (nlevels(temp.data[, row]) > nlevels(temp.data[, 
                    column])) {
                    longerRow <- TRUE
                  }
                  else {
                    longerRow <- FALSE
                  }
                }
                else {
                  stop("The levels of the row/column variable should be continuous across replicates.")
                }
            }
            responseRate <- (obsused/obsread)
            result[[i]]$site[[j]]$responseRate <- responseRate
            if (responseRate < 0.8) {
                result[[i]]$site[[j]]$manyNAWarning <- "Too many missing observations. Cannot proceed with the analysis."
                newExcludeCheck <- excludeCheck
                next
            }
            else {
                checkTestWarning = "NONE"
                if (!is.null(checkList)) {
                  checkList <- trimStrings(checkList)
                  levelsGenoTemp <- levels(temp.data[, match(geno, 
                    names(temp.data))])
                  canProceedToAnalysis <- all(checkList %in% 
                    levelsGenoTemp)
                  if (!canProceedToAnalysis) {
                    isCheckPresent <- checkList %in% levelsGenoTemp
                    deletedCheck <- checkList[!isCheckPresent]
                    if (length(deletedCheck) == length(checkList)) {
                      newCheckList = NULL
                      newExcludeCheck = FALSE
                      checkTestWarning <- "All specified control levels are not present in this trial. Nothing is excluded in the estimation of genotypic variance."
                    }
                    else {
                      newCheckList <- checkList[isCheckPresent]
                      newExcludeCheck <- TRUE
                      deletedList = NULL
                      for (k in 1:length(deletedCheck)) {
                        if (k == 1) {
                          deletedList <- paste(deletedCheck[k])
                        }
                        else {
                          deletedList <- paste(deletedList, ", ", 
                            deletedCheck[k], sep = "")
                        }
                      }
                      checkTestWarning <- paste("The following control level(s) is(are) not present in this trial and deleted from the list of genotype levels to exclude: ", 
                        deletedList, sep = "")
                    }
                  }
                  else {
                    newCheckList <- checkList
                    newExcludeCheck <- TRUE
                  }
                  result[[i]]$site[[j]]$newCheckListLength <- length(newCheckList)
                }
                result[[i]]$site[[j]]$checkTestWarning <- checkTestWarning
                if (!excludeCheck) {
                  newExcludeCheck <- FALSE
                  newCheckList <- NULL
                }
                if (is.random) {
                  if (exptl.design == "AugRCB" | exptl.design == 
                    "AugLS") {
                    if (excludeCheck) {
                      library(doBy)
                      nobs <- summaryBy(formula(paste(respvar[i], 
                        " ~ ", geno, sep = "")), data = temp.data, 
                        FUN = length)
                      newCheckList <- as.vector(nobs[nobs[, match(paste(respvar[i], 
                        ".length", sep = ""), names(nobs))] > 
                        1, geno])
                      result[[i]]$site[[j]]$newCheckListLength <- length(newCheckList)
                      detach("package:doBy")
                    }
                    newExcludeCheck <- excludeCheck
                  }
                  if (!newExcludeCheck) {
                    if (exptl.design == "RCB" | exptl.design == 
                      "AugRCB") {
                      myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                        row, ") + (1|", geno, ")", sep = "")
                    }
                    if (exptl.design == "AugLS") {
                      myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                        row, ") + (1|", column, ") + (1|", geno, 
                        ")", sep = "")
                    }
                    if (exptl.design == "Alpha") {
                      myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                        rep, "/", row, ") + (1|", geno, ")", 
                        sep = "")
                    }
                    if (exptl.design == "RowCol") {
                      myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                        rep, ") + (1|", rep, ":", row, ") + (1|", 
                        rep, ":", column, ") + (1|", geno, ")", 
                        sep = "")
                    }
                    if (exptl.design == "LatinAlpha") {
                      myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                        rep, ") + (1|", row, ") + (1|", rep, 
                        ":", row, ") + (1|", geno, ")", sep = "")
                    }
                    if (exptl.design == "LatinRowCol") {
                      if (longerRow) {
                        myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                          rep, ") + (1|", column, ") + (1|", 
                          rep, ":", column, ") + (1|", row, ") + (1|", 
                          geno, ")", sep = "")
                      }
                      else {
                        myformula1 <- paste(respvar[i], " ~ 1 + (1|", 
                          rep, ") + (1|", row, ") + (1|", rep, 
                          ":", row, ") + (1|", column, ") + (1|", 
                          geno, ")", sep = "")
                      }
                    }
                  }
                  else {
                    for (k in (1:nrow(temp.data))) {
                      if (is.na(match(temp.data[k, match(geno, 
                        names(temp.data))], newCheckList))) {
                        temp.data$Test[k] <- levels(temp.data[, 
                          match(geno, names(temp.data))])[temp.data[k, 
                          match(geno, names(temp.data))]]
                        temp.data$Check[k] <- "0"
                      }
                      else {
                        temp.data$Test[k] <- "0"
                        temp.data$Check[k] <- newCheckList[match(temp.data[k, 
                          match(geno, names(temp.data))], newCheckList)]
                      }
                    }
                    temp.data$Check <- factor(temp.data$Check)
                    temp.data$Test <- factor(temp.data$Test)
                    if (exptl.design == "RCB" | exptl.design == 
                      "AugRCB") {
                      myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                        row, ") + (1|Test:Check)", sep = "")
                    }
                    if (exptl.design == "AugLS") {
                      myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                        row, ") + (1|", column, ") + (1|Test:Check)", 
                        sep = "")
                    }
                    if (exptl.design == "Alpha") {
                      myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                        rep, "/", row, ") + (1|Test:Check)", 
                        sep = "")
                    }
                    if (exptl.design == "RowCol") {
                      myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                        rep, ") + (1|", rep, ":", row, ") + (1|", 
                        rep, ":", column, ") + (1|Test:Check)", 
                        sep = "")
                    }
                    if (exptl.design == "LatinAlpha") {
                      myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                        rep, ") + (1|", row, ") + (1|", rep, 
                        ":", row, ") + (1|Test:Check)", sep = "")
                    }
                    if (exptl.design == "LatinRowCol") {
                      if (longerRow) {
                        myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                          rep, ") + (1|", column, ") + (1|", 
                          rep, ":", column, ") + (1|", row, ") + (1|Test:Check)", 
                          sep = "")
                      }
                      else {
                        myformula1 <- paste(respvar[i], " ~ 1 + Check + (1|", 
                          rep, ") + (1|", row, ") + (1|", rep, 
                          ":", row, ") + (1|", column, ") + (1|Test:Check)", 
                          sep = "")
                      }
                    }
                  }
                }
                if (!is.random) {
                  if (exptl.design == "RCB" | exptl.design == 
                    "AugRCB") {
                    myformula1 <- paste(respvar[i], " ~ 1 + ", 
                      geno, " + (1|", row, ")", sep = "")
                  }
                  if (exptl.design == "AugLS") {
                    myformula1 <- paste(respvar[i], " ~ 1 + ", 
                      geno, " + (1|", row, ") + (1|", column, 
                      ")", sep = "")
                  }
                  if (exptl.design == "Alpha") {
                    myformula1 <- paste(respvar[i], " ~ 1 + ", 
                      geno, " + (1|", rep, "/", row, ")", sep = "")
                  }
                  if (exptl.design == "RowCol") {
                    myformula1 <- paste(respvar[i], " ~ 1 + ", 
                      geno, " + (1|", rep, ") + (1|", rep, ":", 
                      row, ") + (1|", rep, ":", column, ")", 
                      sep = "")
                  }
                  if (exptl.design == "LatinAlpha") {
                    myformula1 <- paste(respvar[i], " ~ 1 + ", 
                      geno, " + (1|", rep, ") + (1|", row, ") + (1|", 
                      rep, ":", row, ")", sep = "")
                  }
                  if (exptl.design == "LatinRowCol") {
                    if (longerRow) {
                      myformula1 <- paste(respvar[i], " ~ 1 + ", 
                        geno, " + (1|", rep, ") + (1|", column, 
                        ") + (1|", rep, ":", column, ") + (1|", 
                        row, ")", sep = "")
                    }
                    else {
                      myformula1 <- paste(respvar[i], " ~ 1 + ", 
                        geno, " + (1|", rep, ") + (1|", row, 
                        ") + (1|", rep, ":", row, ") + (1|", 
                        column, ")", sep = "")
                    }
                  }
                }
                model <- try(lmer(formula(myformula1), data = temp.data), 
                  silent = TRUE)
                if (!is.null(model) && class(model) == "try-error") {
                  msg <- trimStrings(strsplit(model, ":")[[1]])
                  msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                    collapse = " "))
                  msg <- gsub("\"", "", msg)
                  result[[i]]$site[[j]]$lmerRun <- "ERROR"
                  result[[i]]$site[[j]]$lmerError <- msg
                  next
                }
                result[[i]]$site[[j]]$lmerRun <- "NO ERROR"
                result[[i]]$site[[j]]$formula1 <- myformula1
                result[[i]]$site[[j]]$model <- model
                varcomp <- NULL
                for (k in (1:length(VarCorr(model)))) {
                  varcomp <- rbind(varcomp, data.frame(Groups = names(VarCorr(model))[k], 
                    Variance = VarCorr(model)[[k]][1], Std.Dev. = attr(VarCorr(model)[[k]], 
                      "stddev")[[1]]))
                }
                varcomp <- rbind(varcomp, data.frame(Groups = "Residual", 
                  Variance = attr(VarCorr(model), "sc")^2, Std.Dev. = attr(VarCorr(model), 
                    "sc")))
                attr(varcomp, "heading") <- "Variance Components for Random Effects\n"
                result[[i]]$site[[j]]$varcomp.table <- varcomp
                result[[i]]$site[[j]]$varcompnRep <- as.data.frame(attr(VarCorr(model), 
                  "sc")^2)
                result[[i]]$site[[j]]$varcompnRep$numRep <- result[[i]]$site[[j]]$numreps
                result[[i]]$site[[j]]$varcompnRep$env <- result[[i]]$site[[j]]$env[[1]]
                colnames(result[[i]]$site[[j]]$varcompnRep) <- c(paste(respvar[i], 
                  "sigma2", sep = "_"), paste(respvar[i], "No.rep", 
                  sep = "_"), env)
                if (j == 1) {
                  result[[i]]$out.sigma2 <- result[[i]]$site[[j]]$varcompnRep
                }
                else {
                  result[[i]]$out.sigma2 <- rbind(result[[i]]$out.sigma2, 
                    result[[i]]$site[[j]]$varcompnRep)
                }
                if (is.random) {
                  if (!newExcludeCheck) {
                    myformula2 <- gsub(paste(" + (1|", geno, 
                      ")", sep = ""), "", myformula1, fixed = TRUE)
                  }
                  else {
                    myformula2 <- gsub("+ (1|Test:Check)", "", 
                      myformula1, fixed = TRUE)
                  }
                  model1 <- lmer(formula(myformula1), data = temp.data, 
                    REML = T)
                  model2 <- lmer(formula(myformula2), data = temp.data, 
                    REML = T)
                  models.table <- modelComparisonTable(model1, 
                    model2)
                  result[[i]]$site[[j]]$formula2 <- myformula2
                  result[[i]]$site[[j]]$models.table <- models.table
                  result[[i]]$site[[j]]$model1 <- model1
                  if (!newExcludeCheck) {
                    herit <- varcomp[varcomp[, 1] == geno, "Variance"]/(varcomp[varcomp[, 
                      1] == geno, "Variance"] + (varcomp[varcomp[, 
                      1] == "Residual", "Variance"]/no.reps))
                  }
                  else {
                    herit <- varcomp[varcomp[, 1] == "Test:Check", 
                      "Variance"]/(varcomp[varcomp[, 1] == "Test:Check", 
                      "Variance"] + (varcomp[varcomp[, 1] == 
                      "Residual", "Variance"]/no.reps))
                  }
                  herit <- as.matrix(round(herit, digits = 2))
                  rownames(herit) <- ""
                  result[[i]]$site[[j]]$heritability <- herit[1, 
                    1]
                  if (!newExcludeCheck) {
                    sumStat.table <- eval(parse(text = paste("coef(model)$", 
                      geno, sep = "")))
                    sumStat.table <- cbind(rownames(sumStat.table), 
                      sumStat.table)
                  }
                  else {
                    sumStat.table <- coef(model)$"Test:Check"
                    temp.names <- t(as.data.frame(strsplit(rownames(sumStat.table), 
                      ":")))
                    sumStat.table$Test <- temp.names[, 1]
                    sumStat.table$Check <- temp.names[, 2]
                    sumStat.table <- subset(sumStat.table, Test != 
                      "0", select = c("Test", "(Intercept)"))
                  }
                  rownames(sumStat.table) <- NULL
                  colnames(sumStat.table) <- c(geno, "Means")
                  attr(sumStat.table, "heading") <- paste("Predicted Means of ", 
                    geno, "\n", sep = "")
                  result[[i]]$site[[j]]$summary.statistic <- sumStat.table
                  if (newExcludeCheck) {
                    myformula3 <- gsub("~ 1", "~ 0", myformula1)
                    model3 <- lmer(formula(myformula3), data = temp.data)
                    lsmeans.checks <- data.frame(coef(summary(model3)))[-1, 
                      1:2]
                    rownames(lsmeans.checks) <- gsub("Check", 
                      "", rownames(lsmeans.checks))
                    lsmeans.checks <- cbind(rownames(lsmeans.checks), 
                      lsmeans.checks)
                    rownames(lsmeans.checks) <- NULL
                    colnames(lsmeans.checks) <- c(geno, "LSMean", 
                      "StdErrMean")
                    result[[i]]$site[[j]]$lsmeans.checks <- lsmeans.checks
                  }
                  result[[i]]$site[[j]]$sum.out <- sumStat.table
                  result[[i]]$site[[j]]$sum.out$Env <- result[[i]]$site[[j]]$env[[1]]
                  colnames(result[[i]]$site[[j]]$sum.out) <- c(geno, 
                    paste(result[[i]]$respvar, "PredMean", sep = "_"), 
                    env)
                  if (j == 1) {
                    result[[i]]$means.out <- result[[i]]$site[[j]]$sum.out
                  }
                  else {
                    result[[i]]$means.out <- rbind(result[[i]]$means.out, 
                      result[[i]]$site[[j]]$sum.out)
                  }
                }
                if (!is.random) {
                  myformula2 <- gsub(paste(" + ", geno, sep = ""), 
                    "", myformula1, fixed = TRUE)
                  model1 <- lmer(formula(myformula1), data = temp.data, 
                    REML = T)
                  model2 <- lmer(formula(myformula2), data = temp.data, 
                    REML = T)
                  result[[i]]$site[[j]]$formula2 <- myformula2
                  myformula3 <- gsub("~ 1", "~ 0", myformula1)
                  model3 <- lmer(formula(myformula3), data = temp.data)
                  sumStat.table <- data.frame(coef(summary(model3)))[, 
                    1:2]
                  rownames(sumStat.table) <- gsub(geno, "", rownames(sumStat.table))
                  sumStat.table <- cbind(rownames(sumStat.table), 
                    sumStat.table)
                  rownames(sumStat.table) <- NULL
                  colnames(sumStat.table) <- c(geno, "LSMean", 
                    "StdErrMean")
                  result[[i]]$site[[j]]$summary.statistic <- sumStat.table
                  noEntries <- nlevels(temp.data[, match(geno, 
                    names(temp.data))])
                  covs <- as.matrix(vcov(model3)[1:noEntries, 
                    1:noEntries])
                  vars <- diag(covs)
                  vdiff <- outer(vars, vars, "+") - 2 * covs
                  sed <- sqrt(vdiff[upper.tri(vdiff)])
                  minSed <- formatC(as.numeric(format(min(sed), 
                    scientific = FALSE)), format = "f")
                  meanSed <- formatC(as.numeric(format(mean(sed), 
                    scientific = FALSE)), format = "f")
                  maxSed <- formatC(as.numeric(format(max(sed), 
                    scientific = FALSE)), format = "f")
                  sedCol <- rbind(minSed, meanSed, maxSed)
                  rowNames <- rbind("Minimum  ", "Average  ", 
                    "Maximum  ")
                  sedTable <- as.table(cbind(rowNames, sedCol))
                  rownames(sedTable) <- c("", "", "")
                  colnames(sedTable) <- c("", "Estimate")
                  result[[i]]$site[[j]]$sedTable <- sedTable
                  result[[i]]$site[[j]]$sum.out <- sumStat.table
                  result[[i]]$site[[j]]$sum.out$Env <- result[[i]]$site[[j]]$env[[1]]
                  colnames(result[[i]]$site[[j]]$sum.out) <- c(geno, 
                    paste(result[[i]]$respvar, "Mean", sep = "_"), 
                    paste(result[[i]]$respvar, "StdErrMean", 
                      sep = "_"), env)
                  if (j == 1) {
                    result[[i]]$meansse.out <- result[[i]]$site[[j]]$sum.out
                  }
                  else {
                    result[[i]]$meansse.out <- rbind(result[[i]]$meansse.out, 
                      result[[i]]$site[[j]]$sum.out)
                  }
                  if (j == 1) {
                    result[[i]]$means.out <- result[[i]]$site[[j]]$sum.out[-3]
                  }
                  else {
                    result[[i]]$means.out <- rbind(result[[i]]$means.out, 
                      result[[i]]$site[[j]]$sum.out[-3])
                  }
                }
                result[[i]]$site[[j]]$residuals <- resid(model1)
                result[[i]]$site[[j]]$fitted.values <- fitted(model1)
                result[[i]]$site[[j]]$data <- temp.data
            }
            result[[i]]$site[[j]]$newExcludeCheck <- newExcludeCheck
        }
        if (exptl.design == "RCB" | exptl.design == "AugRCB") {
            byVariables <- c(env, geno, row)
        }
        if (exptl.design == "AugLS") {
            byVariables <- c(env, geno, row, column)
        }
        if (exptl.design == "Alpha" || exptl.design == "LatinAlpha") {
            byVariables <- c(env, geno, row, rep)
        }
        if (exptl.design == "RowCol" || exptl.design == "LatinRowCol") {
            byVariables <- c(env, geno, row, column, rep)
        }
        if (i == 1) {
            means.out.all <- result[[i]]$means.out
        }
        else {
            meansOut2 <- result[[i]]$means.out
            if (!is.null(meansOut2)) {
                if (is.null(means.out.all)) {
                  means.out.all <- meansOut2
                }
                else {
                  means.out.all <- merge(means.out.all, meansOut2, 
                    by = c(env, geno), all = TRUE)
                }
            }
        }
        if (!is.random) {
            if (i == 1) {
                meansse.out.all <- result[[i]]$meansse.out
            }
            else {
                meansseOut2 <- result[[i]]$meansse.out
                if (!is.null(meansseOut2)) {
                  if (is.null(meansse.out.all)) {
                    meansse.out.all <- meansseOut2
                  }
                  else {
                    meansse.out.all <- merge(meansse.out.all, 
                      meansseOut2, by = c(env, geno), all = TRUE)
                  }
                }
            }
        }
        else {
            meansse.out.all <- NULL
        }
        if (i == 1) {
            varrep.out.all <- result[[i]]$out.sigma2
        }
        else {
            sigmaOut2 <- result[[i]]$out.sigma2
            if (!is.null(sigmaOut2)) {
                if (is.null(varrep.out.all)) {
                  varrep.out.all <- sigmaOut2
                }
                else {
                  varrep.out.all <- merge(varrep.out.all, sigmaOut2, 
                    by = c(env), all = TRUE)
                }
            }
        }
    }
    if (is.null(means.out.all)) {
        meansWarning <- "empty"
    }
    else {
        meansWarning <- "not empty"
    }
    if (is.null(meansse.out.all)) {
        meansseWarning <- "empty"
    }
    else {
        meansseWarning <- "not empty"
    }
    if (is.null(varrep.out.all)) {
        varrepWarning <- "empty"
    }
    else {
        varrepWarning <- "not empty"
    }
    detach("package:lme4")
    return(list(output = result, means = means.out.all, meansWarning = meansWarning, 
        meansse = meansse.out.all, meansseWarning = meansseWarning, 
        varrep = varrep.out.all, varrepWarning = varrepWarning, 
        byVars = byVariables))
}
