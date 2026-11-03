`GraphLine` <-
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
UseMethod("GraphLine")
