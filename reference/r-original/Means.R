`Means` <-
function (data, design = "lattice") 
{
    am <- sapply(fcol:ncol(data), adjmeans, data, design)
    am <- as.data.frame(am)
    colnames(am) <- colnames(data[, -(1:(fcol - 1))])
    return(am)
}
