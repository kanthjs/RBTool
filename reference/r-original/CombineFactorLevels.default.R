`CombineFactorLevels.default` <-
function (data, concatVar, targetName = NULL) 
{
    if (is.character(data)) {
        data <- eval(parse(text = data))
    }
    if (is.null(targetName)) {
        tempName <- make.unique(c(names(data), "NewVar"), sep = "")
    }
    else {
        if (!is.valid.name(targetName)) {
            targetName <- make.names(c(names(data), "NewVar"), 
                unique = TRUE)
        }
        tempName <- make.unique(c(names(data), targetName), sep = "")
    }
    command <- paste("factor(paste(data[,'", paste(concatVar, 
        collapse = "'],'_',data[,'", sep = ""), "'], sep = ''))", 
        sep = "")
    data <- data.frame(data, eval(parse(text = command)))
    colnames(data)[ncol(data)] <- tempName[length(tempName)]
    remove(list = c("command", "tempName", "targetName"))
    return(data)
}
