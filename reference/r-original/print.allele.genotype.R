`print.allele.genotype` <-
function (x, ...) 
{
    if (!is.null(attr(x, "locus"))) 
        print(attr(x, "locus"))
    cat("Allele(s):", attr(x, "which"), "\n")
    attr(x, "which") <- attr(x, "class") <- attr(x, "locus") <- attr(x, 
        "allele.names") <- NULL
    NextMethod("print", x)
}
