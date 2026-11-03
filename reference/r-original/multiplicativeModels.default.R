`multiplicativeModels.default` <-
function (data, outFileName, respvar, environment, genotype, 
    numberOfReps, residualVariances, responsePlot = FALSE, doAMMI = TRUE, 
    biplotPC12 = FALSE, biplotPC13 = FALSE, biplotPC23 = FALSE, 
    ammi1 = FALSE, adaptMap = FALSE, doGGE = FALSE, graphSym = FALSE, 
    graphEnv = FALSE, graphGeno = FALSE) 
{
    outFileName <- trimStrings(outFileName)
    respvar <- trimStrings(respvar)
    environment <- trimStrings(environment)
    genotype <- trimStrings(genotype)
    if (!is.numeric(numberOfReps)) {
        numberOfReps <- trimStrings(numberOfReps)
    }
    if (!is.numeric(residualVariances)) {
        residualVariances <- trimStrings(residualVariances)
    }
    capture.output(cat("\nMULTIPLICATIVE MODELS\n\n\n"), file = outFileName, 
        append = FALSE)
    createdResponsePlot1 = FALSE
    createdResponsePlot2 = FALSE
    createdBiplotPC12 = FALSE
    createdBiplotPC13 = FALSE
    createdBiplotPC23 = FALSE
    createdAmmi1 = FALSE
    createdAdaptMap = FALSE
    createdGraphSym = FALSE
    createdGraphEnv = FALSE
    createdGraphGeno = FALSE
    result <- list()
    data[, environment] <- factor(data[, environment])
    data[, genotype] <- factor(data[, genotype])
    levelsGeno <- levels(data[, genotype])
    levelsEnv <- levels(data[, environment])
    commonLevels <- intersect(levelsGeno, levelsEnv)
    if (length(commonLevels) > 0) {
        withCommonLevels <- TRUE
    }
    else {
        withCommonLevels <- FALSE
    }
    if (max(nchar(levelsGeno)) > 4 || max(nchar(levelsEnv)) > 
        4 || withCommonLevels) {
        newCodingGeno <- data.frame(Genotype = levelsGeno, Code = paste("G", 
            seq(1:length(levelsGeno)), sep = ""))
        newCodingEnv <- data.frame(Environment = levelsEnv, Code = paste("E", 
            seq(1:length(levelsEnv)), sep = ""))
        suppressWarnings(result$newCodingGeno <- newCodingGeno)
        suppressWarnings(result$newCodingEnv <- newCodingEnv)
        recodedLevels <- TRUE
        temp.data <- data
        temp.data$CodedGeno <- newCodingGeno$Code[match(temp.data[, 
            genotype], newCodingGeno$Genotype)]
        temp.data$CodedEnv <- newCodingEnv$Code[match(temp.data[, 
            environment], newCodingEnv$Environment)]
    }
    else {
        temp.data <- data
        temp.data$CodedGeno <- temp.data[, match(genotype, names(temp.data))]
        temp.data$CodedEnv <- temp.data[, match(environment, 
            names(temp.data))]
        recodedLevels <- FALSE
    }
    temp.data$CodedGeno <- factor(temp.data$CodedGeno)
    temp.data$CodedEnv <- factor(temp.data$CodedEnv)
    temp.dataAll <- temp.data
    for (i in 1:length(respvar)) {
        result$respvar[[i]] <- list()
        capture.output(cat("------------------------------\n"), 
            file = outFileName, append = TRUE)
        capture.output(cat("RESPONSE VARIABLE: ", respvar[i], 
            "\n"), file = outFileName, append = TRUE)
        capture.output(cat("------------------------------\n\n\n"), 
            file = outFileName, append = TRUE)
        result$respvar[[i]] <- respvar[i]
        temp.data <- temp.dataAll
        if (nlevels(temp.data[, "CodedEnv"]) > 2) {
            rep <- tapply(temp.data[, respvar[i]], temp.data[, 
                c("CodedEnv", "CodedGeno")], function(x) length(which(!is.na(x))))
            if (is.numeric(numberOfReps) && any(rep > 1, na.rm = TRUE)) {
                meansWide <- as.data.frame(tapply(temp.data[, 
                  respvar[i]], temp.data[, c("CodedEnv", "CodedGeno")], 
                  function(x) mean(x, na.rm = TRUE)))
                meansWide <- cbind(envTemp = rownames(meansWide), 
                  meansWide)
                colnames(meansWide)[1] <- "CodedEnv"
                temp.data <- data.frame(reshape(meansWide, direction = "long", 
                  varying = colnames(meansWide)[2:ncol(meansWide)], 
                  v.names = respvar[i], timevar = "CodedGeno", 
                  idvar = "CodedEnv", times = colnames(meansWide)[2:ncol(meansWide)]), 
                  row.names = NULL)
                rep <- tapply(temp.data[, respvar[i]], temp.data[, 
                  c("CodedEnv", "CodedGeno")], function(x) length(which(!is.na(x))))
                temp.data$CodedGeno <- factor(temp.data$CodedGeno)
                temp.data$CodedEnv <- factor(temp.data$CodedEnv)
            }
            responseRate <- 1 - ((sum(rep == 0, na.rm = TRUE) + 
                sum(is.na(rep)))/(nlevels(temp.data[, "CodedEnv"]) * 
                nlevels(temp.data[, "CodedGeno"])))
            if (responseRate < 0.8) {
                capture.output(cat("*** \nERROR: Too many missing observations. Cannot proceed with the analysis.\n***\n\n\n"), 
                  file = outFileName, append = TRUE)
                suppressWarnings(result$respvar[[i]]$Error <- "ERROR: Too many missing observations. Cannot proceed with the analysis.")
            }
            else {
                if (doAMMI) {
                  capture.output(cat("AMMI ANALYSIS:\n\n"), file = outFileName, 
                    append = TRUE)
                  if (!is.numeric(numberOfReps)) {
                    result.temp <- try(ammi.analysis2(temp.data[, 
                      "CodedEnv"], temp.data[, "CodedGeno"], 
                      temp.data[, numberOfReps[i]], temp.data[, 
                        respvar[i]], temp.data[, residualVariances[i]], 
                      number = FALSE, biplotPC12 = biplotPC12, 
                      biplotPC13 = biplotPC13, biplotPC23 = biplotPC23, 
                      ammi1 = ammi1, adaptMap = adaptMap, respVar = respvar[i]), 
                      silent = TRUE)
                  }
                  else {
                    result.temp <- try(ammi.analysis(temp.data[, 
                      "CodedEnv"], temp.data[, "CodedGeno"], 
                      numberOfReps[i], temp.data[, respvar[i]], 
                      residualVariances[i], number = FALSE, biplotPC12 = biplotPC12, 
                      biplotPC13 = biplotPC13, biplotPC23 = biplotPC23, 
                      ammi1 = ammi1, adaptMap = adaptMap, yVar = respvar[i]), 
                      silent = TRUE)
                  }
                  if (!is.null(class(result.temp)) & class(result.temp) == 
                    "try-error") {
                    msg <- trimStrings(strsplit(result.temp, 
                      ":")[[1]])
                    msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                      collapse = " "))
                    msg <- gsub("\"", "", msg)
                    capture.output(cat("*** \nERROR in ammi.analysis function:\n  ", 
                      msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$Error <- paste("ERROR: ", 
                      msg, sep = ""))
                  }
                  else {
                    capture.output(result.temp$analysis, file = outFileName, 
                      append = TRUE)
                    capture.output(cat("\n\n"), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$ammi <- result.temp$analysis)
                    createdBiplotPC12 = biplotPC12
                    createdBiplotPC13 = biplotPC13
                    createdBiplotPC23 = biplotPC23
                    createdAmmi1 = ammi1
                    createdAdaptMap = adaptMap
                  }
                }
                if (doGGE) {
                  capture.output(cat("GGE ANALYSIS:\n\n"), file = outFileName, 
                    append = TRUE)
                  if (!is.numeric(numberOfReps)) {
                    result.temp2 <- try(gge.analysis2(temp.data[, 
                      "CodedEnv"], temp.data[, "CodedGeno"], 
                      temp.data[, numberOfReps[i]], temp.data[, 
                        respvar[i]], temp.data[, residualVariances[i]], 
                      number = FALSE, graphSym = FALSE, graphEnv = FALSE, 
                      graphGeno = FALSE, respVar = respvar[i], 
                      f = 0.5), silent = TRUE)
                  }
                  else {
                    result.temp2 <- try(gge.analysis(temp.data[, 
                      "CodedEnv"], temp.data[, "CodedGeno"], 
                      numberOfReps[i], temp.data[, respvar[i]], 
                      residualVariances[i], number = FALSE, graphSym = FALSE, 
                      graphEnv = FALSE, graphGeno = FALSE, yVar = respvar[i], 
                      f = 0.5), silent = TRUE)
                  }
                  if (!is.null(class(result.temp2)) & class(result.temp2) == 
                    "try-error") {
                    msg <- trimStrings(strsplit(result.temp2, 
                      ":")[[1]])
                    msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                      collapse = " "))
                    msg <- gsub("\"", "", msg)
                    capture.output(cat("*** \nERROR in gge.analysis function:\n  ", 
                      msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$Error <- paste("ERROR: ", 
                      msg, sep = ""))
                  }
                  else {
                    capture.output(result.temp2$analysis, file = outFileName, 
                      append = TRUE)
                    capture.output(cat("\n\n"), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$gge <- result.temp2$analysis)
                  }
                  if (graphSym) {
                    if (!is.numeric(numberOfReps)) {
                      result.temp3 <- try(gge.analysis2(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        temp.data[, numberOfReps[i]], temp.data[, 
                          respvar[i]], temp.data[, residualVariances[i]], 
                        number = FALSE, graphSym = TRUE, graphEnv = FALSE, 
                        graphGeno = FALSE, respVar = respvar[i], 
                        f = 0.5), silent = TRUE)
                    }
                    else {
                      result.temp3 <- try(gge.analysis(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        numberOfReps[i], temp.data[, respvar[i]], 
                        residualVariances[i], number = FALSE, 
                        graphSym = TRUE, graphEnv = FALSE, graphGeno = FALSE, 
                        yVar = respvar[i], f = 0.5), silent = TRUE)
                    }
                    if (!is.null(class(result.temp3)) & class(result.temp3) == 
                      "try-error") {
                      msg <- trimStrings(strsplit(result.temp3, 
                        ":")[[1]])
                      msg <- trimStrings(paste(strsplit(msg, 
                        "\n")[[length(msg)]], collapse = " "))
                      msg <- gsub("\"", "", msg)
                      capture.output(cat("*** \nERROR in gge graph (symmetric view):\n  ", 
                        msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                        append = TRUE)
                      suppressWarnings(result$respvar[[i]]$Error <- paste("ERROR: ", 
                        msg, sep = ""))
                    }
                    else {
                      createdGraphSym = TRUE
                    }
                  }
                  if (graphEnv) {
                    if (!is.numeric(numberOfReps)) {
                      result.temp3 <- try(gge.analysis2(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        temp.data[, numberOfReps[i]], temp.data[, 
                          respvar[i]], temp.data[, residualVariances[i]], 
                        number = FALSE, graphSym = FALSE, graphEnv = TRUE, 
                        graphGeno = FALSE, respVar = respvar[i], 
                        f = 0), silent = TRUE)
                    }
                    else {
                      result.temp3 <- try(gge.analysis(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        numberOfReps[i], temp.data[, respvar[i]], 
                        residualVariances[i], number = FALSE, 
                        graphSym = FALSE, graphEnv = TRUE, graphGeno = FALSE, 
                        yVar = respvar[i], f = 0), silent = TRUE)
                    }
                    if (!is.null(class(result.temp3)) & class(result.temp3) == 
                      "try-error") {
                      msg <- trimStrings(strsplit(result.temp3, 
                        ":")[[1]])
                      msg <- trimStrings(paste(strsplit(msg, 
                        "\n")[[length(msg)]], collapse = " "))
                      msg <- gsub("\"", "", msg)
                      capture.output(cat("*** \nERROR in gge graph (environment view):\n  ", 
                        msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                        append = TRUE)
                      suppressWarnings(result$respvar[[i]]$Error <- paste("ERROR: ", 
                        msg, sep = ""))
                    }
                    else {
                      createdGraphEnv = TRUE
                    }
                  }
                  if (graphGeno) {
                    if (!is.numeric(numberOfReps)) {
                      result.temp3 <- try(gge.analysis2(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        temp.data[, numberOfReps[i]], temp.data[, 
                          respvar[i]], temp.data[, residualVariances[i]], 
                        number = FALSE, graphSym = FALSE, graphEnv = FALSE, 
                        graphGeno = TRUE, respVar = respvar[i], 
                        f = 1), silent = TRUE)
                    }
                    else {
                      result.temp3 <- try(gge.analysis(temp.data[, 
                        "CodedEnv"], temp.data[, "CodedGeno"], 
                        numberOfReps[i], temp.data[, respvar[i]], 
                        residualVariances[i], number = FALSE, 
                        graphSym = FALSE, graphEnv = FALSE, graphGeno = TRUE, 
                        yVar = respvar[i], f = 1), silent = TRUE)
                    }
                    if (!is.null(class(result.temp3)) & class(result.temp3) == 
                      "try-error") {
                      msg <- trimStrings(strsplit(result.temp3, 
                        ":")[[1]])
                      msg <- trimStrings(paste(strsplit(msg, 
                        "\n")[[length(msg)]], collapse = " "))
                      msg <- gsub("\"", "", msg)
                      capture.output(cat("*** \nERROR in gge graph (genotypic view):\n  ", 
                        msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                        append = TRUE)
                      suppressWarnings(result$respvar[[i]]$Error <- paste("ERROR: ", 
                        msg, sep = ""))
                    }
                    else {
                      createdGraphGeno = TRUE
                    }
                  }
                }
                if (responsePlot) {
                  nlevelsEnv <- length(levelsEnv)
                  nlevelsGeno <- length(levelsGeno)
                  resPlot1 <- try(GraphLine(data = temp.data, 
                    outputPath = getwd(), yVars = c(respvar[i]), 
                    xVar = c("CodedGeno"), lineVars = c("CodedEnv"), 
                    mTitle = paste("Response Plot of ", respvar[i], 
                      sep = ""), yAxisLab = c(respvar[i]), xAxisLab = genotype, 
                    yMinValue = c(NA), yMaxValue = c(NA), axisLabelStyle = 2, 
                    byVar = NULL, plotCol = c(1:nlevelsEnv), 
                    showLineLabels = TRUE, showLeg = FALSE, boxed = TRUE, 
                    linePtTypes = rep("b", nlevelsEnv), lineTypes = rep(1, 
                      nlevelsEnv), lineWidths = rep(1, nlevelsEnv), 
                    pointChars = rep(" ", nlevelsEnv), pointCharSizes = rep(1, 
                      nlevelsEnv), multGraphs = FALSE), silent = TRUE)
                  resPlot2 <- try(GraphLine(data = temp.data, 
                    outputPath = getwd(), yVars = c(respvar[i]), 
                    xVar = c("CodedEnv"), lineVars = c("CodedGeno"), 
                    mTitle = paste("Response Plot of ", respvar[i], 
                      sep = ""), yAxisLab = c(respvar[i]), xAxisLab = environment, 
                    yMinValue = c(NA), yMaxValue = c(NA), axisLabelStyle = 2, 
                    byVar = NULL, plotCol = c(1:nlevelsGeno), 
                    showLineLabels = TRUE, showLeg = FALSE, boxed = TRUE, 
                    linePtTypes = rep("b", nlevelsGeno), lineTypes = rep(1, 
                      nlevelsGeno), lineWidths = rep(1, nlevelsGeno), 
                    pointChars = rep(" ", nlevelsGeno), pointCharSizes = rep(1, 
                      nlevelsGeno), multGraphs = FALSE), silent = TRUE)
                  if (!is.null(class(resPlot1)) & class(resPlot1) == 
                    "try-error") {
                    msg <- trimStrings(strsplit(resPlot1, ":")[[1]])
                    msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                      collapse = " "))
                    msg <- gsub("\"", "", msg)
                    capture.output(cat("*** \nERROR in GraphLine function:\n  ", 
                      msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$responsePlot1Error <- paste("ERROR: ", 
                      msg, sep = ""))
                  }
                  else {
                    createdResponsePlot1 = TRUE
                  }
                  if (!is.null(class(resPlot2)) & class(resPlot2) == 
                    "try-error") {
                    msg <- trimStrings(strsplit(resPlot2, ":")[[1]])
                    msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                      collapse = " "))
                    msg <- gsub("\"", "", msg)
                    capture.output(cat("*** \nERROR in GraphLine function:\n  ", 
                      msg, "\n***\n\n\n", sep = ""), file = outFileName, 
                      append = TRUE)
                    suppressWarnings(result$respvar[[i]]$responsePlot2Error <- paste("ERROR: ", 
                      msg, sep = ""))
                  }
                  else {
                    createdResponsePlot2 = TRUE
                  }
                }
            }
        }
        else {
            capture.output(cat("*** \nERROR: The environment factor should have at least three levels.\n***\n\n\n"), 
                file = outFileName, append = TRUE)
            suppressWarnings(result$respvar[[i]]$Error <- "ERROR: The environment factor should have at least three levels.")
        }
        capture.output(cat("\n"), file = outFileName, append = TRUE)
    }
    if (recodedLevels && (createdResponsePlot1 || createdResponsePlot2 || 
        createdBiplotPC12 || createdBiplotPC13 || createdBiplotPC23 || 
        createdAmmi1 || createdAdaptMap || createdGraphSym || 
        createdGraphEnv || createdGraphGeno)) {
        capture.output(cat("------------------------------\n"), 
            file = outFileName, append = TRUE)
        capture.output(cat("\nCODES USED IN GRAPHS:\n\n"), file = outFileName, 
            append = TRUE)
        capture.output(newCodingGeno, file = outFileName, append = TRUE)
        capture.output(cat("\n"), file = outFileName, append = TRUE)
        capture.output(newCodingEnv, file = outFileName, append = TRUE)
    }
    capture.output(cat("\n==============================\n"), 
        file = outFileName, append = TRUE)
    return(result)
}
