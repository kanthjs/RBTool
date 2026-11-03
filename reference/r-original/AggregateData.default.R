`AggregateData.default` <-
function (data, var, grp, stat = c("mean"), append = FALSE) 
{
    if (is.character(data)) {
        data <- eval(parse(text = data))
    }
    fxnLabel <- c("min", "max", "mean", "median", "sum", "variance", 
        "standard deviation")
    fxnEq <- c("min", "max", "mean", "median", "sum", "var", 
        "sd")
    capture.output(result <- DescriptiveStatistics(data, var, 
        grp, statistics = fxnEq[match(stat, fxnLabel)]))
    aggrData <- reshape(result, idvar = grp, timevar = "Variable", 
        direction = "wide")
    if (append) {
        tempData <- merge(data, aggrData, by = grp)
        tempData <- tempData[, unique(c(names(data), names(aggrData)))]
        remove(list = c("aggrData", "result", "fxnLabel", "fxnEq"))
        return(tempData)
    }
    else {
        attr(aggrData, "reshapeWide") <- NULL
        remove(list = c("result", "fxnLabel", "fxnEq"))
        return(aggrData)
    }
}
