`Readrest` <-
function (file.wgt = NULL) 
{
    n <- ntraits
    nom <- wgts[as.logical(wgts[, 2]), 1]
    indrest <- wgts[1:n, 5]
    W <- diag(n)[, which(indrest == 1)]
    return(W)
}
