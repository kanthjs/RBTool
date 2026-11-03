`designLatinizedRowCol.default` <-
function (generate, r = 2, trial = 1, rowPerRep, numFieldRow, 
    serpentine = FALSE, file = NULL) 
{
    if (r < 2) {
        stop("The number of replicates should be greater than or equal to 2.")
    }
    if (length(generate[[1]]) == 1) {
        tempComb <- FactorList(generate)
    }
    else {
        tempComb <- generate
    }
    if ((r * length(tempComb[[1]])) > 1500) {
        stop("The maximum number of experimental units that can be generated is 1500.")
    }
    colPerRep <- length(tempComb[[1]])/rowPerRep
    if (numFieldRow == rowPerRep) {
        case <- 2
    }
    else {
        if (numFieldRow%%rowPerRep == 0) {
            case <- 1
            colPerRep <- length(tempComb[[1]])/rowPerRep
        }
        else {
            stop("ERROR:")
        }
    }
    randomize <- NULL
    plan <- list()
    for (i in 1:trial) {
        if (case == 1) {
            result <- try(temp <- DiGGer(NumberOfTreatments = length(tempComb[[1]]), 
                RowsInDesign = rowPerRep * r, ColumnsInDesign = colPerRep, 
                RowsInReplicate = rowPerRep, ColumnsInReplicate = colPerRep, 
                BlockIn2D = c(rowPerRep * r, 1), TreatmentName = tempComb[[1]]), 
                silent = TRUE)
        }
        else {
            result <- try(temp <- DiGGer(NumberOfTreatments = length(tempComb[[1]]), 
                RowsInDesign = rowPerRep, ColumnsInDesign = colPerRep * 
                  r, RowsInReplicate = rowPerRep, ColumnsInReplicate = colPerRep, 
                BlockIn2D = c(1, colPerRep * r), TreatmentName = tempComb[[1]]), 
                silent = TRUE)
        }
        if (all(class(result) == "try-error")) {
            msg <- trimStrings(strsplit(result, ":")[[1]])
            msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                collapse = " "))
            stop(paste("Error in DiGGer:", msg, sep = ""))
        }
        capture.output(run(temp))
        if (all(class(result) == "try-error")) {
            msg <- trimStrings(strsplit(result, ":")[[1]])
            msg <- trimStrings(paste(strsplit(msg, "\n")[[length(msg)]], 
                collapse = " "))
            stop(paste("Error in DiGGer:", msg, sep = ""))
        }
        result_mat <- getDesign(temp)
        fbook <- print(temp, option = "list")
        plan[[i]] <- matrix(fbook$ID, nrow(result_mat), ncol(result_mat))
        if (i == 1) {
            repNum <- matrix(as.numeric(paste(fbook$REP, paste(c(rep(0, 
                max(nchar(1:length(tempComb[[1]]))))), collapse = ""), 
                sep = "")), nrow(plan[[i]]), ncol(plan[[i]]))
            plotNumTemp <- matrix(1:length(tempComb[[1]]), rowPerRep, 
                colPerRep, byrow = TRUE)
            plotNum <- NULL
            if (case == 1) {
                if (serpentine) {
                  for (k in seq(2, rowPerRep, by = 2)) {
                    plotNumTemp[k, ] <- rev(plotNumTemp[k, ])
                  }
                }
                for (j in 1:r) {
                  plotNum <- rbind(plotNum, plotNumTemp)
                }
            }
            else {
                if (serpentine) {
                  for (k in seq(2, colPerRep, by = 2)) {
                    plotNumTemp[k, ] <- rev(plotNumTemp[k, ])
                  }
                }
                for (j in 1:r) {
                  plotNum <- cbind(plotNum, plotNumTemp)
                }
            }
            plotNum <- repNum + plotNum
        }
        tempFieldOrder <- as.data.frame.table(plotNum)
        tempFieldOrder[, "Var1"] <- as.numeric(tempFieldOrder[, 
            "Var1"])
        tempFieldOrder[, "Var2"] <- as.numeric(tempFieldOrder[, 
            "Var2"])
        colnames(tempFieldOrder)[3] <- "PlotNum"
        randomize <- rbind(randomize, cbind(Trial = 1, merge(print(temp, 
            option = "list"), tempFieldOrder, by.x = c("ROW", 
            "RANGE"), by.y = c("Var1", "Var2"))))
        dimnames(plan[[i]]) <- list(paste("FieldRow", 1:nrow(plan[[i]]), 
            sep = ""), paste("FieldCol", 1:ncol(plan[[i]]), sep = ""))
    }
    dimnames(plotNum) <- dimnames(plan[[1]])
    names(plan) <- paste("Trial", 1:trial, sep = "")
    randomize <- randomize[, c("Trial", "REP", "ID", "PlotNum", 
        "ROW", "RANGE")]
    names(randomize) <- c("Trial", "Rep", names(tempComb)[1], 
        "PlotNum", "FieldRow", "FieldCol")
    randomize <- randomize[order(randomize$Trial, randomize$PlotNum), 
        ]
    randomize[, "Trial"] <- factor(randomize[, "Trial"])
    randomize[, "Rep"] <- factor(randomize[, "Rep"])
    randomize[, "FieldRow"] <- as.numeric(randomize[, "FieldRow"])
    randomize[, "FieldCol"] <- as.numeric(randomize[, "FieldCol"])
    rownames(randomize) <- 1:nrow(randomize)
    cat(toupper("Design Properties:"), "\n", sep = "")
    cat("\t", "Incomplete Block Design", "\n", sep = "")
    cat("\t", "Latinized Row-Column Design", "\n\n", sep = "")
    cat(toupper("Design Parameters:"), "\n", sep = "")
    cat("\t", "Number of Trials = ", trial, "\n", sep = "")
    cat("\t", "Number of Treatments = ", length(tempComb[[1]]), 
        "\n", sep = "")
    cat("\t", "Number of Replicates = ", r, "\n", sep = "")
    cat("\t", "Number of Rows per Replicate = ", rowPerRep, "\n", 
        sep = "")
    cat("\t", "Number of Field Rows = ", numFieldRow, "\n\n", 
        sep = "")
    if (!is.null(file)) {
        tempFile <- strsplit(file, split = "\\.")[[1]]
        tempExt <- tolower(tempFile[length(tempFile)])
        if (tempExt != "csv") {
            if (tempExt != "rda") 
                tempExt <- "csv"
        }
        newFile <- paste(tempFile[1:length(tempFile) - 1], collapse = ".", 
            sep = "")
        newFile <- paste(newFile, tempExt, sep = ".")
        if (tempExt == "csv") {
            write.csv(randomize, file = newFile, row.names = FALSE)
        }
        else {
            save(randomize, file = newFile)
        }
    }
    else {
        cat("Results of Randomization:\n")
        printDataFrame(randomize)
    }
    return(invisible(list(fieldbook = randomize, layout = plan, 
        plotNum = plotNum)))
}
