`homozygote.genotype` <-
function (x, allele.name, ...) 
{
    a1 <- allele(x, 1)
    a2 <- allele(x, 2)
    if (missing(allele.name)) 
        retval <- ifelse(is.na(a1) | is.na(a2), NA, a1 == a2)
    else retval <- ifelse(is.na(a1) | is.na(a2), NA, a1 == allele.name & 
        a2 == allele.name)
    attr(retval, "locus") <- attr(x, "locus")
    return(retval)
}
