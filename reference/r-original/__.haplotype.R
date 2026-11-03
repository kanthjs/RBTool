`==.haplotype` <-
function (x, y) 
{
    if (!is.genotype(y)) 
        y <- as.haplotype(y)
    x.a1 <- allele(x, 1)
    x.a2 <- allele(x, 2)
    y.a1 <- allele(y, 1)
    y.a2 <- allele(y, 2)
    return(x.a1 == y.a1 & x.a2 == y.a2)
}
