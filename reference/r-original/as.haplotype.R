`as.haplotype` <-
function (x, ...) 
{
    retval <- as.genotype(x, ..., reorder = "no")
    class(retval) <- c("haplotype", "genotype", "factor")
    retval
}
