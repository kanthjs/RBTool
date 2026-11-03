`as.genotype.table` <-
function (x, alleles, ...) 
{
    tmp <- outer(rownames(x), colnames(x), paste, sep = "/")
    retval <- genotype(rep(tmp, x), alleles = alleles)
    retval
}
