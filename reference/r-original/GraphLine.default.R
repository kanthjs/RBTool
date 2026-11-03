`GraphLine.default` <-
function (data, outputPath, yVars, xVar = NULL, lineVars = NULL, 
    mTitle = NULL, yAxisLab = NULL, xAxisLab = NULL, yMinValue = NULL, 
    yMaxValue = NULL, axisLabelStyle = 1, byVar = NULL, errBars = FALSE, 
    typeErrBar = c(NULL, "stdErr", "confInt"), errMult = 1, confLev = NULL, 
    plotCol = NULL, showLineLabels = FALSE, showLeg = FALSE, 
    legPos = c("bottomright", "bottom", "bottomleft", "left", 
        "topleft", "top", "topright", "right", "center"), legTitle = NULL, 
    legCol = 1, boxed = TRUE, linePtTypes, lineTypes, lineWidths, 
    pointChars, pointCharSizes, multGraphs = FALSE, numRowsGraphs = 1, 
    numColsGraphs = 1, orientGraphs = c("left-right", "top-bottom")) 
{
    if (is.character(data)) {
        data <- eval(parse(text = data))
    }
    if (!any(is.na(suppressWarnings(as.numeric(levels(data[, 
        xVar])))))) {
        xNumeric = TRUE
    }
    else xNumeric = FALSE
    lineVar = NULL
    if (is.null(lineVars)) {
        lineVar <- make.unique(c(names(data), "lineVar"))[length(make.unique(c(names(data), 
            "grp.Var")))]
        data[, lineVar] = rep(1, nrow(data))
    }
    else {
        if (length(lineVars) == 2) {
            lineVar = paste(lineVars[1], lineVars[2], sep = "-")
            data[, lineVar] = paste(data[, lineVars[1]], data[, 
                lineVars[2]], sep = "-")
        }
        else if (length(lineVars) == 3) {
            lineVar = paste(lineVars[1], lineVars[2], lineVars[3], 
                sep = "-")
            data[, lineVar] = paste(data[, lineVars[1]], data[, 
                lineVars[2]], data[, lineVars[3]], sep = "-")
        }
        else {
            lineVar = lineVars[1]
            data[, lineVar] = data[, lineVars[1]]
        }
    }
    data[, xVar] <- factor(data[, xVar])
    data[, lineVar] <- factor(data[, lineVar])
    if (!is.null(byVar)) {
        data[, byVar] <- factor(data[, byVar])
    }
    if (!multGraphs) {
        numRowsGraphs = 1
        numColsGraphs = 1
    }
    numCells = numRowsGraphs * numColsGraphs
    numGroups = 1
    if (!is.null(byVar)) {
        numGroups = nlevels(data[, byVar])
        numGraphs = nlevels(data[, byVar]) * length(yVars)
    }
    else numGraphs = length(yVars)
    graphNum = 1
    k = 1
    for (m in 1:numGroups) {
        if (!is.null(byVar)) {
            tempData1 = data[which(data[, byVar] == levels(data[, 
                byVar])[m]), ]
            subTitle = paste(byVar, " = ", levels(tempData1[, 
                byVar])[m], sep = "")
        }
        else {
            tempData1 = data
            subTitle = NULL
        }
        for (i in 1:length(yVars)) {
            if (!multGraphs) {
                png(filename = paste(outputPath, "/", "ResponsePlot_", 
                  xVar, "_", yVars[i], ".png", sep = ""), width = 900, 
                  height = 500)
                par(mfrow = c(numRowsGraphs, numColsGraphs))
            }
            else {
                if (graphNum == 1 || graphNum%%numCells == 1 || 
                  numCells == 1) {
                  widthAdj = numColsGraphs * 480
                  heightAdj = numRowsGraphs * 480
                  png(filename = paste(outputPath, "linegraph_", 
                    k, ".png", sep = ""), width = widthAdj, height = heightAdj)
                  if (orientGraphs == "top-bottom") {
                    par(mfcol = c(numRowsGraphs, numColsGraphs))
                  }
                  else if (orientGraphs == "left-right") {
                    par(mfrow = c(numRowsGraphs, numColsGraphs))
                  }
                }
            }
            tempData2 = tempData1[, c(yVars[i], xVar, lineVar)]
            namesX = NULL
            if (!all(is.na(tempData2[, yVars[i]])) && !all(is.na(tempData2[, 
                xVar]))) {
                if (xNumeric) {
                  xVals = as.numeric(levels(tempData2[, xVar]))
                }
                else xVals = c(1:nlevels(tempData2[, xVar]))
                allYStats = NULL
                yStatEB = NULL
                yStatEBj = NULL
                yStatHW = NULL
                yStatLL = NULL
                yStatUL = NULL
                yStat = NULL
                lenlineVar = nlevels(tempData2[, lineVar])
                for (j in 1:lenlineVar) {
                  if (!is.null(lineVar)) {
                    tempData = tempData2[which(tempData2[, lineVar] == 
                      levels(tempData2[, lineVar])[j]), ]
                  }
                  else tempData = tempData2
                  yStat = tapply(tempData[, yVars[i]], tempData[, 
                    xVar], mean, na.rm = TRUE)
                  if (errBars) {
                    ySum = tapply((tempData[, yVars[i]]), tempData[, 
                      xVar], sum, na.rm = TRUE)
                    yFreq = ySum/yStat
                    yStdDev = tapply(tempData[, yVars[i]], tempData[, 
                      xVar], sd, na.rm = TRUE)
                    yStdErr = yStdDev/sqrt(yFreq)
                    if (typeErrBar == "stdErr") {
                      halfWidth = errMult * yStdErr
                    }
                    else if (typeErrBar == "confInt") {
                      halfWidth = qnorm((1 + confLev)/2) * yStdErr
                    }
                    lLimit = yStat - halfWidth
                    uLimit = yStat + halfWidth
                    yStatLL = rbind(yStatLL, lLimit)
                    yStatUL = rbind(yStatUL, uLimit)
                    yStatHW = rbind(yStatHW, halfWidth)
                  }
                  allYStats = rbind(allYStats, yStat)
                }
                allYStats = t(allYStats)
                allYStats1 = t(allYStats)
                if (!xNumeric) 
                  namesX = rownames(allYStats)
                yMinLim = if (!is.na(yMinValue[i])) {
                  yMinValue[i]
                }
                else {
                  if (!is.null(yStatLL)) 
                    min(allYStats1, yStatLL, na.rm = TRUE)
                  else min(allYStats1, na.rm = TRUE)
                }
                yMaxLim = if (!is.na(yMaxValue[i])) {
                  yMaxValue
                }
                else {
                  if (!is.null(yStatUL)) 
                    max(allYStats1, yStatUL, na.rm = TRUE)
                  else max(allYStats1, na.rm = TRUE)
                }
                yVarLim = c(yMinLim, yMaxLim)
                legLab = NULL
                q = 1
                if (showLeg || showLineLabels) {
                  for (r in 1:lenlineVar) {
                    if (!all(is.nan(allYStats[, r]))) {
                      legLab[q] = levels(tempData[, lineVar])[r]
                      q = q + 1
                    }
                  }
                }
                if (xNumeric) {
                  xVarLim = c(min(as.numeric(tempData[, xVar])), 
                    max(as.numeric(tempData[, xVar])))
                }
                else xVarLim = NULL
                if (showLineLabels || multGraphs) 
                  par(mar = c(5, 4, 4, 4) + 0.2)
                varToPlot = allYStats[, 1]
                plot(xVals, varToPlot, ylab = yAxisLab[i], xlab = xAxisLab, 
                  main = mTitle, type = "n", axes = FALSE, col = plotCol, 
                  ylim = yVarLim)
                if (xNumeric) {
                  axis(1, at = xVals, las = axisLabelStyle)
                }
                else {
                  axis(1, at = 1:length(namesX), labels = namesX, 
                    las = axisLabelStyle, cex.axis = 0.75)
                }
                axis(2, ylim = yVarLim, las = axisLabelStyle)
                if (!is.null(subTitle)) 
                  mtext(side = 3, text = subTitle, line = 0.25, 
                    cex = 0.9)
                if (showLeg) 
                  legend(legPos, legend = legLab, title = legTitle, 
                    pch = pointChars, lty = lineTypes, col = plotCol, 
                    cex = 0.75, ncol = legCol)
                if (boxed) 
                  box(lty = 1)
                for (l in 1:lenlineVar) {
                  varToPlot = allYStats[, l]
                  lines(xVals, varToPlot, type = linePtTypes[l], 
                    lwd = lineWidths[l], pch = pointChars[l], 
                    cex = pointCharSizes[l], col = plotCol[l], 
                    lty = lineTypes[l])
                  text(xVals, varToPlot, cex = 0.65, labels = legLab[l], 
                    col = plotCol[l])
                }
                if (errBars) {
                  lLimits = t(matrix(yStatLL, c(lenlineVar, length(xVals))))
                  uLimits = t(matrix(yStatUL, c(lenlineVar, length(xVals))))
                  for (h in 1:lenlineVar) {
                    arrows(xVals, lLimits[, h], xVals, uLimits[, 
                      h], angle = 90, code = 3, length = 0.1, 
                      col = plotCol[h])
                  }
                }
            }
            if ((graphNum%%numCells == 0) || graphNum == numGraphs) {
                dev.off()
                k = k + 1
            }
            graphNum = graphNum + 1
        }
    }
}
