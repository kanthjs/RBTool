`expectedHaplotypes` <-
function (x, alleles = allele.names(x), ploidy = 2, sort = TRUE, 
    haplotype = TRUE) 
{
    expectedGenotypes(x = x, alleles = alleles, ploidy = ploidy, 
        sort = sort, haplotype = haplotype)
}
