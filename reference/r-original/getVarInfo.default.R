`getVarInfo.default` <-
function (fileName, fileFormat, separator = NULL) 
{
    if (fileFormat == 1) {
        data <- read.csv(fileName, header = TRUE, na.strings = c("NA", 
            "."), blank.lines.skip = TRUE, sep = ",")
    }
    else if (fileFormat == 2) {
        if (is.na(separator)) 
            separator = "\t"
        data <- read.table(fileName, header = TRUE, na.strings = c("NA", 
            "."), blank.lines.skip = TRUE, sep = separator)
    }
    else {
        data <- eval(parse(text = load(fileName)))
    }
    varInfo = as.data.frame(names(data))
    for (i in 1:ncol(data)) {
        if (is.numeric(data[, i])) 
            varInfo[i, 2] <- "Numeric"
        else varInfo[i, 2] <- "Factor"
    }
    names(varInfo) = c("Variable", "Type")
    return(varInfo)
}
