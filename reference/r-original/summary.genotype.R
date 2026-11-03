`summary.genotype` <-
function (object, ..., maxsum) 
{
    if (!missing(maxsum)) 
        return(shortsummary.genotype(object, ..., maxsum = maxsum))
    retval <- list()
    retval$allele.names <- allele.names(object)
    retval$locus <- attr(object, "locus")
    class(retval) <- "summary.genotype"
    af <- table(allele(object))
    missed <- !names(af) %in% retval$allele.names
    af.tab <- rep(0, length(retval$allele.names))
    names(af.tab) <- retval$allele.names
    af.tab[names(af)] <- af
    paf <- prop.table(af.tab)
    retval$allele.freq <- cbind(Count = af.tab, Proportion = paf)
    gf <- table(object)
    pgf <- prop.table(gf)
    retval$genotype.freq <- cbind(Count = gf, Proportion = pgf)
    asFun <- as.genotype
    if (is.haplotype(object)) 
        asFun <- as.haplotype
    tmp <- asFun(rownames(retval$genotype.freq), alleles = allele.names(object))
    tmp <- order(tmp, genotypeOrder = genotypeOrder(object))
    retval$genotype.freq <- retval$genotype.freq[tmp, ]
    n.typed <- sum(gf)
    correction <- 2 * n.typed/max(1, 2 * n.typed - 1)
    ehet <- (1 - sum(paf * paf))
    matings <- (paf %*% t(paf))^2
    uninf.mating.freq <- sum(matings) - sum(diag(matings))
    pic <- ehet - uninf.mating.freq
    retval$Hu <- correction * ehet
    retval$pic <- pic
    retval$n.typed <- n.typed
    retval$n.total <- length(object)
    retval$nallele <- nallele(object)
    if (any(is.na(object))) {
        retval$allele.freq <- rbind(retval$allele.freq, `NA` = c(sum(is.na(allele(object))), 
            NA))
        retval$genotype.freq <- rbind(retval$genotype.freq, `NA` = c(sum(is.na(object)), 
            NA))
    }
    return(retval)
}
