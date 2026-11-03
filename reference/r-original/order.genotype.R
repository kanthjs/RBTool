`order.genotype` <-
function (..., na.last = TRUE, decreasing = FALSE, alleleOrder = allele.names(x), 
    genotypeOrder = NULL) 
{
    x <- list(...)[[1]]
    isHap <- is.haplotype(x)
    reorder <- ifelse(isHap, "no", "yes")
    if (is.null(genotypeOrder)) {
        alleleOrder <- unique(alleleOrder)
        if (!identical(alleleOrder, allele.names(x))) 
            x <- genotype(x, alleles = alleleOrder, reorder = reorder)
        tmp <- as.character(x)
        id <- seq(along = alleleOrder)
        id <- formatC(id, width = ceiling(log10(max(id))), flag = "0")
        for (i in seq(along = alleleOrder)) {
            tmp <- sub(pattern = paste(alleleOrder[i], "/", sep = ""), 
                replacement = paste(i, "_", alleleOrder[i], "/", 
                  sep = ""), x = tmp)
            tmp <- sub(pattern = paste("/", alleleOrder[i], sep = ""), 
                replacement = paste("/", i, "_", alleleOrder[i], 
                  sep = ""), x = tmp)
        }
    }
    else {
        genotypeOrder <- unique(genotypeOrder)
        if (!isHap) {
            genotypeOrder <- .genotype2Haplotype(x = genotypeOrder)
        }
        tmp <- match(x, genotypeOrder)
    }
    return(order(tmp, na.last = TRUE, decreasing = FALSE))
}
