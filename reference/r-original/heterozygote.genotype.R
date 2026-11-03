`heterozygote.genotype` <-
function (x, allele.name, ...) 
{
    {
        a1 <- allele(x, 1)
        a2 <- allele(x, 2)
        if (missing(allele.name)) 
            retval <- ifelse(is.na(a1) | is.na(a2), NA, !a1 == 
                a2)
        else retval <- ((a1 %in% allele.name) | (a2 %in% allele.name)) & 
            (a1 != a2)
        attr(retval, "locus") <- attr(x, "locus")
        return(retval)
    }
}
