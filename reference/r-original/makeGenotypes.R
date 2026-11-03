`makeGenotypes` <-
function (data, convert, sep = "/", tol = 0.5, ..., method = as.genotype) 
{
    data <- as.data.frame(data)
    if (missing(convert)) {
        fun <- function(x) length(unlist(grep(sep, as.character(x))))
        convert <- sapply(data, fun)/nrow(data) > tol
    }
    if (is.list(convert)) {
        if (!all(sapply(convert, length) == 2)) 
            stop("When convert is a list, each element must be a 2-vector.")
        namelist <- names(data)
        for (pair in convert) {
            if (!is.character(pair)) 
                pair <- namelist[pair]
            index <- which(colnames(data) == pair[1])
            data[[index]] <- method(data[[pair[1]]], data[[pair[2]]], 
                sep = sep, ...)
            colnames(data)[index] <- paste(pair, collapse = sep)
            data[[pair[1]]] <- data[[pair[2]]] <- NULL
        }
    }
    else {
        if (is.character(convert)) 
            namelist <- convert
        else namelist <- colnames(data)[convert]
        for (col in namelist) data[[col]] <- method(data[[col]], 
            sep = sep, ...)
    }
    data
}
