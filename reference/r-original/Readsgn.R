`Readsgn` <-
function () 
{
    n <- ntraits
    nom <- wgts[as.logical(wgts[, 2]), 1]
    sgn <- wgts[1:n, 4]
    names(sgn) <- nom
    return(sgn)
}
