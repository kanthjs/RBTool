`[.haplotype` <-
function (x, i, drop = FALSE) 
{
    retval <- NextMethod("[")
    class(retval) <- c("haplotype", "genotype", "factor")
    retval
}
