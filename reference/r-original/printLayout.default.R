`printLayout.default` <-
function (trmt, plotNum, RowLabel = NULL, ColLabel = NULL, title = NULL) 
{
    if (is.null(RowLabel)) {
        RWidth <- 2
    }
    else {
        RWidth <- max(nchar(RowLabel)) + 2
    }
    if (!is.null(title)) {
        cat("\n", title, "\n\n", sep = "")
    }
    if (!is.null(ColLabel)) {
        cellWidth <- max(max(nchar(trmt)), max(nchar(plotNum)), 
            max(nchar(ColLabel))) + 2
        cat(formatC("", width = RWidth, format = "s"), " ", sep = "")
        for (j in (1:ncol(trmt))) cat(formatC(colnames(trmt)[j], 
            width = cellWidth + 1, format = "s", flag = "-"), 
            sep = "")
        cat("\n")
    }
    else {
        cellWidth <- max(max(nchar(trmt)), max(nchar(plotNum))) + 
            2
    }
    for (i in (1:nrow(trmt))) {
        cat(formatC("", width = RWidth, format = "s"), "+", sep = "")
        for (j in (1:ncol(trmt))) cat(formatC(paste(rep("-", 
            cellWidth), collapse = ""), width = cellWidth, format = "s"), 
            "+", sep = "")
        cat("\n")
        if (is.null(RowLabel)) {
            cat(formatC("", width = RWidth, format = "s", flag = "-"), 
                "|", sep = "")
        }
        else {
            cat(formatC(RowLabel[i], width = RWidth, format = "s", 
                flag = "-"), "|", sep = "")
        }
        for (j in (1:ncol(trmt))) cat(formatC(plotNum[i, j], 
            width = cellWidth, format = "d", flag = "#"), "|", 
            sep = "")
        cat("\n")
        cat(formatC("", width = RWidth, format = "s"), "|", sep = "")
        for (j in (1:ncol(trmt))) cat(formatC(trmt[i, j], width = cellWidth, 
            format = "s", flag = "-"), "|", sep = "")
        cat("\n")
        if (i == nrow(trmt)) {
            cat(formatC("", width = RWidth, format = "s"), "+", 
                sep = "")
            for (j in (1:ncol(trmt))) cat(formatC(paste(rep("-", 
                cellWidth), collapse = ""), width = cellWidth, 
                format = "s"), "+", sep = "")
            cat("\n")
        }
    }
}
