`SortCases.default` <-
function (data, var, sortBy = "ascending") 
{
    if (is.character(data)) {
        data <- eval(parse(text = data))
    }
    if (length(sortBy) != length(var)) {
        if (length(sortBy) > length(var)) {
            sortBy <- sortBy[1:length(var)]
        }
        else {
            if (length(sortBy) > 1) {
                sortBy <- c(sortBy, rep("ascending", length(var) - 
                  length(sortBy)))
            }
            else {
                sortBy <- rep(sortBy, length(var))
            }
        }
    }
    for (i in (1:length(var))) {
        if (rev(sortBy)[i] == "ascending") {
            data <- data[order(data[, rev(var)[i]], decreasing = FALSE), 
                ]
        }
        else {
            data <- data[order(data[, rev(var)[i]], decreasing = TRUE), 
                ]
        }
    }
    rownames(data) <- 1:nrow(data)
    return(data)
}
