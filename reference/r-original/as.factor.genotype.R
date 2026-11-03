`as.factor.genotype` <-
function (x, ...) 
{
    attr(x, "class") <- "factor"
    attr(x, "allele.names") <- NULL
    attr(x, "allele.map") <- NULL
    attr(x, "locus") <- NULL
    attr(x, "genotypeOrder") <- NULL
    x
}
