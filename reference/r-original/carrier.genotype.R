`carrier.genotype` <-
function (x, allele.name = allele.names(x), any = !missing(allele.name), 
    na.rm = FALSE, ...) 
{
    retval <- allele.count(x, allele.name = allele.name, any = any, 
        na.rm = na.rm) > 0
    attr(retval, "allele") <- retval["allele"]
    attr(retval, "locus") <- attr(x, "locus")
    return(retval)
}
