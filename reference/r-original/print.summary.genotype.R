`print.summary.genotype` <-
function (x, ..., round = 2) 
{
    if (!is.null(x$locus)) {
        cat("\n")
        print(x$locus)
    }
    cat("\n")
    cat("Number of samples typed: ", x$n.typed, " (", round(100 * 
        x$n.typed/x$n.total, 1), "%)\n", sep = "")
    cat("\n")
    cat("Allele Frequency: (", x$nallele, " alleles)\n", sep = "")
    print(round(x$allele.freq, digits = round), ...)
    cat("\n")
    cat("\n")
    cat("Genotype Frequency:\n")
    print(round(x$genotype.freq, digits = round), ...)
    cat("\n")
    cat("Heterozygosity (Hu)  = ", x$Hu, "\n", sep = "")
    cat("Poly. Inf. Content   = ", x$pic, "\n", sep = "")
    cat("\n")
    invisible(x)
}
