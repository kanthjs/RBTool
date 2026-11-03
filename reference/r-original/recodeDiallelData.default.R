`recodeDiallelData.default` <-
function (design = c("diallel1", "diallel2", "diallel3", "diallel4"), 
    data, p1, p2, rep, block = NULL, row = NULL, column = NULL, 
    respvar) 
{
    data <- data[, match(c(p1, p2, rep, block, row, column, respvar), 
        names(data))]
    data[, p1] <- factor(data[, p1])
    data[, p2] <- factor(data[, p2])
    data[, rep] <- factor(data[, rep])
    p1Levels <- levels(data[, p1])
    p2Levels <- levels(data[, p2])
    levelsRep <- levels(data[, rep])
    levelsParents <- unique(c(p1Levels, p2Levels))
    if (!any(is.na(as.numeric(levelsParents)))) {
        levelsParents <- levels(factor(as.numeric(levelsParents)))
    }
    newCoding <- data.frame(levelsParents = levelsParents, newCodeParents = seq(1:length(levelsParents)))
    data2 <- data.frame(data[, p2], data[, p1], data[, rep], 
        data[, block], data[, row], data[, column], data[, respvar])
    names(data2) <- c(p1, p2, rep, block, row, column, respvar)
    if (design == "diallel1" | design == "diallel3") {
        doubleData <- data
    }
    else {
        doubleData <- unique(rbind(data, data2))
    }
    doubleData$newCodeP1 <- newCoding$newCodeParents[match(doubleData[, 
        p1], newCoding$levelsParents)]
    doubleData$newCodeP2 <- newCoding$newCodeParents[match(doubleData[, 
        p2], newCoding$levelsParents)]
    fullTrtCombi = NULL
    for (i in 1:length(levelsParents)) {
        for (j in 1:length(levelsParents)) {
            for (k in 1:length(levelsRep)) {
                if (design == "diallel1") {
                  newRow <- c(i, j, levelsRep[k])
                  fullTrtCombi <- rbind(fullTrtCombi, newRow)
                }
                if (design == "diallel2") {
                  if (i <= j) {
                    newRow <- c(i, j, levelsRep[k])
                    fullTrtCombi <- rbind(fullTrtCombi, newRow)
                  }
                }
                if (design == "diallel3") {
                  if (i != j) {
                    newRow <- c(i, j, levelsRep[k])
                    fullTrtCombi <- rbind(fullTrtCombi, newRow)
                  }
                }
                if (design == "diallel4") {
                  if (i < j) {
                    newRow <- c(i, j, levelsRep[k])
                    fullTrtCombi <- rbind(fullTrtCombi, newRow)
                  }
                }
            }
        }
    }
    colnames(fullTrtCombi) <- c("newCodeP1", "newCodeP2", rep)
    rownames(fullTrtCombi) <- c(1:nrow(fullTrtCombi))
    fullTrtCombi <- data.frame(fullTrtCombi)
    tempData <- merge(fullTrtCombi, doubleData, by = colnames(fullTrtCombi), 
        all.x = TRUE, all.y = FALSE)
    tempData <- tempData[-c(match(c(p1, p2), names(tempData)))]
    tempData$Cross <- tempData[, "newCodeP1"]:tempData[, "newCodeP2"]
    return(list(tempData = tempData, newCoding = newCoding))
}
