`sort.genotype` <-
function (x, decreasing = FALSE, na.last = NA, ..., alleleOrder = allele.names(x), 
    genotypeOrder = NULL) 
{
    x[order(x, decreasing = decreasing, na.last = na.last, alleleOrder = alleleOrder, 
        genotypeOrder = genotypeOrder)]
}
