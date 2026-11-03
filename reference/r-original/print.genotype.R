`print.genotype` <-
function (x, ...) 
{
    if (!is.null(attr(x, "locus"))) 
        print(attr(x, "locus"))
    print(as.character(x))
    cat("Alleles:", allele.names(x), "\n")
    invisible(x)
}
