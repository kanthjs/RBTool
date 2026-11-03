`GEOneStage.test.default` <-
function (exptl.design = c("RCB", "AugRCB", "AugLS", "Alpha", 
    "RowCol", "LatinAlpha", "LatinRowCol"), data, respvar, geno, 
    row, column = NULL, rep = NULL, env, is.genoRandom = FALSE) 
{
    library(lme4)
    options(show.signif.stars = FALSE)
    prev.opt <- options()$warn
    options(warn = -1)
    if (exptl.design == "RCB" || exptl.design == "AugRCB") {
        if (is.na(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || is.na(match(row, names(data))) || 
            is.na(match(env, names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "AugLS") {
        if (is.na(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || is.na(match(row, names(data))) || 
            is.na(match(column, names(data))) || is.na(match(env, 
            names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "Alpha" || exptl.design == "LatinAlpha") {
        if (is.na(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || is.na(match(row, names(data))) || 
            is.na(match(rep, names(data))) || is.na(match(env, 
            names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    if (exptl.design == "RowCol" || exptl.design == "LatinRowCol") {
        if (is.na(match(respvar, names(data))) || is.na(match(geno, 
            names(data))) || is.na(match(row, names(data))) || 
            is.na(match(column, names(data))) || is.na(match(rep, 
            names(data))) || is.na(match(env, names(data)))) {
            stop("At least one variable name does not match a column in the data frame.")
        }
    }
    data[, match(geno, names(data))] <- factor(data[, match(geno, 
        names(data))])
    data[, match(row, names(data))] <- factor(data[, match(row, 
        names(data))])
    data[, match(env, names(data))] <- factor(data[, match(env, 
        names(data))])
    if (!is.null(rep)) 
        data[, match(rep, names(data))] <- factor(data[, match(rep, 
            names(data))])
    if (!is.null(column)) 
        data[, match(column, names(data))] <- factor(data[, match(column, 
            names(data))])
    result <- list()
    for (i in (1:length(respvar))) {
        result[[i]] <- list()
        result[[i]]$respvar <- respvar[i]
        temp.data <- subset(data, subset = (is.na(data[, match(respvar[i], 
            names(data))]) == F))
        temp.data[, match(geno, names(temp.data))] <- factor(trimStrings(temp.data[, 
            match(geno, names(temp.data))]))
        temp.data[, match(env, names(temp.data))] <- factor(trimStrings(temp.data[, 
            match(env, names(temp.data))]))
        levelsGeno <- levels(temp.data[, match(geno, names(temp.data))])
        levelsEnv <- levels(temp.data[, match(env, names(temp.data))])
        result[[i]]$nlevelsGeno <- length(levelsGeno)
        result[[i]]$nlevelsEnv <- length(levelsEnv)
        if (max(nchar(levelsGeno)) > 4 || max(nchar(levelsEnv)) > 
            4) {
            newCodingGeno <- data.frame(Genotype = levelsGeno, 
                Code = paste("G", seq(1:length(levelsGeno)), 
                  sep = ""))
            newCodingEnv <- data.frame(Environment = levelsEnv, 
                Code = paste("E", seq(1:length(levelsEnv)), sep = ""))
            result[[i]]$newCodingGeno <- newCodingGeno
            result[[i]]$newCodingEnv <- newCodingEnv
            recodedLevels <- TRUE
            result[[i]]$recodedLevels <- recodedLevels
            temp.data$CodedGeno <- newCodingGeno$Code[match(temp.data[, 
                geno], newCodingGeno$Genotype)]
            temp.data$CodedEnv <- newCodingEnv$Code[match(temp.data[, 
                env], newCodingEnv$Environment)]
        }
        else {
            temp.data$CodedGeno <- temp.data[, match(geno, names(temp.data))]
            temp.data$CodedEnv <- temp.data[, match(env, names(temp.data))]
            recodedLevels <- FALSE
            result[[i]]$recodedLevels <- recodedLevels
        }
        if (exptl.design == "LatinRowCol") {
            lengthPerCross <- tapply(temp.data[, respvar[i]], 
                temp.data[, c(row, column)], length)
            if (all(lengthPerCross <= nlevels(temp.data[, env]), 
                na.rm = TRUE)) {
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
        obsread <- nrow(data)
        obsused <- nrow(temp.data)
        result[[i]]$obsread <- obsread
        result[[i]]$obsused <- obsused
        responseRate <- (obsused/obsread)
        result[[i]]$responseRate <- responseRate
        if (responseRate < 0.8) {
            result[[i]]$manyNAWarning <- "Too many missing observations. Cannot proceed with the analysis."
            next
        }
        else {
            sumStat.Env <- DescriptiveStatistics(temp.data, respvar[i], 
                env, c("min", "mean", "max", "var", "sd"))
            sumStat.Env <- sumStat.Env[, c(2:ncol(sumStat.Env))]
            if (is.genoRandom) 
                trt.stmt <- paste("(1|", geno, ")", sep = "")
            else trt.stmt <- paste(geno, sep = "")
            if (exptl.design == "RCB" || exptl.design == "AugRCB") 
                myformula1 <- paste(respvar[i], " ~ 1 + ", trt.stmt, 
                  " + (1|", env, ") + (1|", row, ":", env, ") + (1|", 
                  geno, ":", env, ")", sep = "")
            if (exptl.design == "AugLS") 
                myformula1 <- paste(respvar[i], " ~ 1 + ", trt.stmt, 
                  " + (1|", env, ") + (1|", row, ":", env, ") + (1|", 
                  column, ":", env, ") + (1|", geno, ":", env, 
                  ")", sep = "")
            if (exptl.design == "Alpha") 
                myformula1 <- paste(respvar[i], " ~ 1 + ", trt.stmt, 
                  " + (1|", env, ") + (1|", rep, ":", env, ") + (1|", 
                  rep, ":", row, ":", env, ") + (1|", geno, ":", 
                  env, ")", sep = "")
            if (exptl.design == "RowCol") 
                myformula1 <- paste(respvar[i], " ~ 1 + ", trt.stmt, 
                  " + (1|", env, ") + (1|", rep, ":", env, ") + (1|", 
                  rep, ":", row, ":", env, ") + (1|", rep, ":", 
                  column, ":", env, ") + (1|", geno, ":", env, 
                  ")", sep = "")
            if (exptl.design == "LatinAlpha") 
                myformula1 <- paste(respvar[i], " ~ 1 + ", trt.stmt, 
                  " + (1|", env, ") + (1|", rep, ":", env, ") + (1|", 
                  row, ":", env, ") + (1|", rep, ":", row, ":", 
                  env, ") + (1|", geno, ":", env, ")", sep = "")
            if (exptl.design == "LatinRowCol") {
                if (longerRow) {
                  myformula1 <- paste(respvar[i], " ~ 1 + ", 
                    trt.stmt, " + (1|", env, ") + (1|", rep, 
                    ":", env, ") + (1|", column, ":", env, ") + (1|", 
                    rep, ":", column, ":", env, ") + (1|", row, 
                    ":", env, ") + (1|", geno, ":", env, ")", 
                    sep = "")
                }
                else {
                  myformula1 <- paste(respvar[i], " ~ 1 + ", 
                    trt.stmt, " + (1|", env, ") + (1|", rep, 
                    ":", env, ") + (1|", row, ":", env, ") + (1|", 
                    rep, ":", row, ":", env, ") + (1|", column, 
                    ":", env, ") + (1|", geno, ":", env, ")", 
                    sep = "")
                }
            }
            model <- lmer(formula(myformula1), data = temp.data)
            result[[i]]$formula1 <- myformula1
            result[[i]]$model <- model
            varcomp <- NULL
            for (j in (1:length(VarCorr(model)))) {
                varcomp <- rbind(varcomp, data.frame(Groups = names(VarCorr(model))[j], 
                  Variance = VarCorr(model)[[j]][1], Std.Dev. = attr(VarCorr(model)[[j]], 
                    "stddev")[[1]]))
            }
            varcomp <- rbind(varcomp, data.frame(Groups = "Residual", 
                Variance = attr(VarCorr(model), "sc")^2, Std.Dev. = attr(VarCorr(model), 
                  "sc")))
            result[[i]]$varcomp.table <- varcomp
            myformula2 <- gsub(paste(" + ", trt.stmt, sep = ""), 
                "", myformula1, fixed = TRUE)
            model1 <- lmer(formula(myformula1), data = temp.data, 
                REML = T)
            model2 <- lmer(formula(myformula2), data = temp.data, 
                REML = T)
            result[[i]]$formula2 <- myformula2
            if (!is.genoRandom) {
                anova.table1 <- anova(model2, model1)
                rownames(anova.table1) <- c("Model2", "Model1")
                attr(anova.table1, "heading")[1] <- paste("TESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING MAXIMUM LIKELIHOOD RATIO TEST:\n", 
                  sep = "")
                attr(anova.table1, "heading")[2] <- paste("Formula for Model1: ", 
                  myformula1, sep = "")
                attr(anova.table1, "heading")[3] <- paste("Formula for Model2: ", 
                  myformula2, sep = "")
                attr(anova.table1, "heading")[4] <- paste("", 
                  sep = "")
                result[[i]]$testsig.Geno <- anova.table1
            }
            else {
                models.table <- modelComparisonTable(model1, 
                  model2)
                result[[i]]$testsig.Geno <- models.table
            }
            myformula3 <- gsub(paste(" + (1|", env, ")", sep = ""), 
                "", myformula1, fixed = TRUE)
            model3 <- lmer(formula(myformula3), data = temp.data, 
                REML = T)
            models.table2 <- modelComparisonTable(model1, model3)
            result[[i]]$formula3 <- myformula3
            result[[i]]$testsig.Env <- models.table2
            myformula4 <- gsub(paste(" + (1|", geno, ":", env, 
                ")", sep = ""), "", myformula1, fixed = TRUE)
            model4 <- lmer(formula(myformula4), data = temp.data, 
                REML = T)
            models.table3 <- modelComparisonTable(model1, model4)
            result[[i]]$formula4 <- myformula4
            result[[i]]$testsig.GenoEnv <- models.table3
            if (is.genoRandom) {
                no.reps <- data.frame(n = tapply(eval(parse(text = paste("temp.data$", 
                  respvar[i], sep = ""))), eval(parse(text = paste("temp.data$", 
                  geno, sep = ""))), FUN = length))
                no.reps <- as.numeric(1/mean(1/no.reps))
                genetic.var <- varcomp[varcomp[, 1] == geno, 
                  "Variance"]
                ge.var <- varcomp[varcomp[, 1] == paste(geno, 
                  ":", env, sep = ""), "Variance"]
                resid.var <- varcomp[varcomp[, 1] == "Residual", 
                  "Variance"]
                heritability <- genetic.var/(genetic.var + (ge.var/nlevels(temp.data[, 
                  env])) + (resid.var/(no.reps * nlevels(temp.data[, 
                  env]))))
                heritability <- as.matrix(round(heritability, 
                  digits = 2))
                rownames(heritability) <- ""
                result[[i]]$heritability <- heritability[1, 1]
                sumStat.Geno <- eval(parse(text = paste("coef(model)$", 
                  geno, sep = "")))
                sumStat.Geno <- cbind(rownames(sumStat.Geno), 
                  sumStat.Geno)
                colnames(sumStat.Geno) <- c(geno, "Mean")
            }
            else {
                myformula5 <- gsub("~ 1", "~ 0", myformula1, 
                  fixed = TRUE)
                model.noint <- lmer(formula(myformula5), data = temp.data)
                sumStat.Geno <- data.frame(summary(model.noint)@coefs)[, 
                  1:2]
                rownames(sumStat.Geno) <- gsub(geno, "", rownames(sumStat.Geno))
                sumStat.Geno <- cbind(rownames(sumStat.Geno), 
                  sumStat.Geno)
                colnames(sumStat.Geno) <- c(geno, "LSMean", "StdErrMean")
                noEntries <- nlevels(temp.data[, match(geno, 
                  names(temp.data))])
                covs <- as.matrix(vcov(model.noint)[1:noEntries, 
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
                rowNames <- rbind("Minimum  ", "Average  ", "Maximum  ")
                sedTable <- as.table(cbind(rowNames, sedCol))
                rownames(sedTable) <- c("", "", "")
                colnames(sedTable) <- c("", "Estimate")
                result[[i]]$sedTable <- sedTable
            }
            rownames(sumStat.Geno) <- NULL
            intercept <- fixef(model)[[1]]
            if (is.genoRandom) {
                geno.effect <- eval(parse(text = paste("ranef(model)$", 
                  geno, sep = "")))
                geno.effect <- data.frame(rownames(geno.effect), 
                  geno.effect)
            }
            else {
                geno.effect <- as.data.frame(fixef(model)[-1])
                geno.effect <- data.frame(gsub(geno, "", rownames(geno.effect)), 
                  geno.effect)
            }
            colnames(geno.effect) <- c(geno, "geno_effect")
            rownames(geno.effect) <- NULL
            env.effect <- eval(parse(text = paste("ranef(model)$", 
                env, sep = "")))
            env.effect <- data.frame(gsub(env, "", rownames(env.effect)), 
                env.effect)
            colnames(env.effect) <- c(env, "env_effect")
            rownames(env.effect) <- NULL
            GXE.effect <- as.data.frame(eval(parse(text = paste("ranef(model)$'", 
                geno, ":", env, "'", sep = ""))))
            names <- t(as.data.frame(strsplit(rownames(GXE.effect), 
                ":")))
            GXE.effect <- data.frame(names[, 1], names[, 2], 
                GXE.effect + intercept)
            colnames(GXE.effect) <- c(geno, env, "ge_effect")
            rownames(GXE.effect) <- NULL
            sumStat.GenoEnv <- merge(GXE.effect, env.effect, 
                by = env, all = TRUE)
            sumStat.GenoEnv <- merge(sumStat.GenoEnv, geno.effect, 
                by = geno, all = TRUE)
            sumStat.GenoEnv <- data.frame(sumStat.GenoEnv[, match(geno, 
                names(sumStat.GenoEnv))], sumStat.GenoEnv[, match(env, 
                names(sumStat.GenoEnv))], rowSums(subset(sumStat.GenoEnv, 
                select = c(ge_effect, env_effect, geno_effect)), 
                na.rm = TRUE))
            colnames(sumStat.GenoEnv) <- c(geno, env, paste(respvar[i], 
                "means", sep = "_"))
            sumStat.GenoEnvCode <- sumStat.GenoEnv
            if (recodedLevels) {
                sumStat.GenoEnvCode$CodedGeno <- newCodingGeno$Code[match(sumStat.GenoEnv[, 
                  geno], newCodingGeno$Genotype)]
                sumStat.GenoEnvCode$CodedEnv <- newCodingEnv$Code[match(sumStat.GenoEnv[, 
                  env], newCodingEnv$Environment)]
            }
            else {
                sumStat.GenoEnvCode$CodedGeno <- sumStat.GenoEnvCode[, 
                  match(geno, names(sumStat.GenoEnvCode))]
                sumStat.GenoEnvCode$CodedEnv <- sumStat.GenoEnvCode[, 
                  match(env, names(sumStat.GenoEnvCode))]
            }
            wide.GenoEnv <- reshape(sumStat.GenoEnv, v.names = paste(respvar[i], 
                "means", sep = "_"), timevar = env, idv = geno, 
                direction = "wide")
            colnames(wide.GenoEnv) <- gsub(paste(respvar[i], 
                "means.", sep = "_"), "", colnames(wide.GenoEnv))
            rownames(wide.GenoEnv) <- 1:nrow(wide.GenoEnv)
            result[[i]]$means.Geno <- sumStat.Geno
            result[[i]]$means.Env <- sumStat.Env
            result[[i]]$means.GenoEnv <- sumStat.GenoEnv
            result[[i]]$wide.GenoEnv <- wide.GenoEnv
            result[[i]]$means.GenoEnvCode <- sumStat.GenoEnvCode
            result[[i]]$residuals <- resid(model1)
            result[[i]]$fitted.values <- fitted(model1)
            result[[i]]$data <- temp.data
            if (!is.genoRandom) {
                envgenorep <- as.data.frame.table(tapply(temp.data[, 
                  respvar[i]], temp.data[, c(env, geno)], length))
                envgenorep <- envgenorep[(is.na(envgenorep[, 
                  "Freq"]) == FALSE), ]
                envgenorep$reciprocal <- 1/envgenorep$Freq
                envgenorep2 <- as.data.frame.table(tapply(envgenorep[, 
                  "reciprocal"], envgenorep[, env], mean))
                envgenorep2$reciprocal2 <- 1/envgenorep2$Freq
                envgenorep3 <- merge(envgenorep, envgenorep2, 
                  by.x = env, by.y = "Var1")
                numrepEnv <- tapply(envgenorep3[, "reciprocal2"], 
                  envgenorep3[, env], mean)
                no.reps <- 1/mean(1/numrepEnv)
                result[[i]]$harmonicMean <- no.reps
                result[[i]]$MSE <- varcomp[varcomp[, 1] == "Residual", 
                  "Variance"]
            }
        }
    }
    for (m in 1:length(respvar)) {
        if (m == 1) {
            means.GenoEnv.all <- as.data.frame(result[[m]]$means.GenoEnv)
        }
        else {
            newGenoEnv <- as.data.frame(result[[m]]$means.GenoEnv)
            if (nrow(newGenoEnv) > 0) {
                if (nrow(means.GenoEnv.all) == 0) {
                  means.GenoEnv.all <- newGenoEnv
                }
                else {
                  means.GenoEnv.all <- merge(means.GenoEnv.all, 
                    newGenoEnv, by = c(geno, env), all = TRUE)
                }
            }
        }
        if (is.genoRandom) {
            if (m == 1) {
                newGeno <- as.data.frame(result[[m]]$means.Geno)
                if (nrow(newGeno) > 0) {
                  colnames(newGeno) <- c(geno, paste(respvar[m], 
                    "_Mean", sep = ""))
                }
                means.Geno.all <- newGeno
            }
            else {
                newGeno <- as.data.frame(result[[m]]$means.Geno)
                if (nrow(newGeno) > 0) {
                  colnames(newGeno) <- c(geno, paste(respvar[m], 
                    "_Mean", sep = ""))
                  if (nrow(means.Geno.all) == 0) {
                    means.Geno.all <- newGeno
                  }
                  else {
                    means.Geno.all <- merge(means.Geno.all, newGeno, 
                      by = c(geno), all = TRUE)
                  }
                }
            }
        }
        else {
            if (m == 1) {
                newGeno <- as.data.frame(result[[m]]$means.Geno)
                if (nrow(newGeno) > 0) {
                  colnames(newGeno) <- c(geno, paste(respvar[m], 
                    "_LSMean", sep = ""), paste(respvar[m], "_StdErrMean", 
                    sep = ""))
                }
                means.Geno.all <- newGeno
            }
            else {
                newGeno <- as.data.frame(result[[m]]$means.Geno)
                if (nrow(newGeno) > 0) {
                  colnames(newGeno) <- c(geno, paste(respvar[m], 
                    "_LSMean", sep = ""), paste(respvar[m], "_StdErrMean", 
                    sep = ""))
                  if (nrow(means.Geno.all) == 0) {
                    means.Geno.all <- newGeno
                  }
                  else {
                    means.Geno.all <- merge(means.Geno.all, newGeno, 
                      by = c(geno), all = TRUE)
                  }
                }
            }
        }
    }
    if (nrow(means.GenoEnv.all) == 0) {
        meansGenoEnvWarning <- "empty"
    }
    else {
        meansGenoEnvWarning <- "not empty"
    }
    if (nrow(means.Geno.all) == 0) {
        meansGenoWarning <- "empty"
    }
    else {
        meansGenoWarning <- "not empty"
    }
    options(warn = prev.opt)
    detach("package:lme4")
    return(list(output = result, means.GenoEnv.all = means.GenoEnv.all, 
        meansGenoEnvWarning = meansGenoEnvWarning, means.Geno.all = means.Geno.all, 
        meansGenoWarning = meansGenoWarning))
}
