`as.genotype.allele.count` <-
function (x, alleles = c("A", "B"), ...) 
{
    if (!is.matrix(x) & !is.data.frame(x)) {
        x <- cbind(x, 2 - x)
        colnames(x) <- alleles
    }
    if (any(x > 2, na.rm = TRUE) || any(x < 0, na.rm = TRUE)) 
        stop("Allele counts must be in {0,1,2}")
    allele.names <- colnames(x)
    tmp <- apply(x, 1, function(y) rep(colnames(x), ifelse(is.na(y), 
        0, y)))
    if (!is.matrix(tmp)) 
        retval <- genotype(sapply(tmp, paste, collapse = "/"), 
            alleles = alleles, ...)
    else retval <- genotype(a1 = tmp[1, ], a2 = tmp[2, ], ...)
    return(retval)
}
