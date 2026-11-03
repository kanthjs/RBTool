`haplotype` <-
function (a1, a2 = NULL, alleles = NULL, sep = "/", remove.spaces = TRUE, 
    reorder = "no", allow.partial.missing = FALSE, locus = NULL, 
    genotypeOrder = NULL) 
{
    retval <- genotype(a1 = a1, a2 = a2, alleles = alleles, sep = sep, 
        remove.spaces = remove.spaces, reorder = reorder, allow.partial.missing = allow.partial.missing, 
        locus = locus, genotypeOrder = genotypeOrder)
    class(retval) <- c("haplotype", "genotype", "factor")
    retval
}
