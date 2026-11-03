`[<-.genotype` <-
function (x, i, value) 
{
    if (all(is.na(value) | as.character(value) <= "")) {
        x.class <- class(x)
        x <- unclass(x)
        x[i] <- NA
        class(x) <- x.class
        return(x)
    }
    if (!is.genotype(value)) {
        value <- genotype(value)
    }
    lx <- levels(x)
    lv <- levels(value)
    ax <- allele.names(x)
    av <- allele.names(value)
    m <- is.na(match(av, ax))
    if (any(m)) 
        warning(paste("Adding new allele name:", av[m], "\n"))
    la <- unique(c(lx, lv))
    aa <- unique(c(ax, av))
    cx <- class(x)
    nas <- is.na(x)
    data <- match(levels(value)[value], la)
    class(x) <- NULL
    x[i] <- data
    attr(x, "levels") <- la
    map <- attr(x, "allele.map") <- do.call("rbind", strsplit(la, 
        "/"))
    attr(x, "allele.names") <- aa
    goCur <- attr(x, "genotypeOrder")
    goAll <- expectedGenotypes(alleles = aa, haplotype = TRUE)
    attr(x, "genotypeOrder") <- c(goCur, goAll[!(goAll %in% goCur)])
    class(x) <- cx
    x
}
