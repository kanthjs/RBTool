`[.genotype` <-
function (x, i, drop = FALSE) 
{
    allelesOld <- attr(x, "allele.names")
    retval <- NextMethod("[")
    ll <- levels(retval) <- na.omit(levels(retval))
    class(retval) <- c("genotype", "factor")
    if (drop) {
        alleles <- unique(unlist(strsplit(ll, "/")))
    }
    else {
        alleles <- attr(x, "allele.names")
    }
    attr(retval, "allele.names") <- alleles
    attr(retval, "allele.map") <- do.call("rbind", strsplit(ll, 
        "/"))
    attr(retval, "locus") <- attr(x, "locus")
    attr(retval, "label") <- attr(x, "label")
    goCur <- attr(x, "genotypeOrder")
    if (drop) {
        allelesOld <- allelesOld[!(allelesOld %in% alleles)]
        tmp <- allele(as.haplotype(goCur))
        test <- tmp %in% allelesOld
        test <- rowSums(matrix(test, ncol = 2)) > 0
        attr(retval, "genotypeOrder") <- goCur[!test]
    }
    else {
        attr(retval, "genotypeOrder") <- goCur
    }
    return(retval)
}
