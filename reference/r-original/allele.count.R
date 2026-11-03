`allele.count` <-
function (x, allele.name = allele.names(x), any = !missing(allele.name), 
    na.rm = FALSE) 
{
    if (!missing(allele.name) && length(allele.name) == 1) {
        a.1 <- allele(x, 1)
        a.2 <- allele(x, 2)
        retval <- ifelse(is.na(a.1) | is.na(a.2), ifelse(na.rm, 
            0, NA), (a.1 == allele.name) + (a.2 == allele.name))
        attr(retval, "allele") <- allele.name
        attr(retval, "locus") <- attr(x, "locus")
        return(retval)
    }
    else {
        retval <- sapply(allele.name, function(y) allele.count(x, 
            y))
        if (any == TRUE && is.matrix(retval)) 
            retval <- apply(retval, 1, sum, na.rm = na.rm)
        if (na.rm) 
            retval[is.na(retval)] <- 0
        attr(retval, "locus") <- attr(x, "locus")
        return(retval)
    }
}
