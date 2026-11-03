`genotypeOrder<-` <-
function (x, value) 
{
    if (!is.genotype(x)) 
        stop("'x' must be of a genotype class")
    alleles <- allele.names(x)
    goAll <- expectedGenotypes(alleles = alleles, haplotype = TRUE)
    goDef <- unique(sort(as.character(x)))
    if (is.null(value)) {
        attr(x, "genotypeOrder") <- goAll
    }
    else {
        value <- unique(value)
        parts <- strsplit(x = value, split = "/")
        parts <- sapply(parts, c)
        test <- !(parts %in% alleles)
        if (any(test)) 
            stop("adding genotype names with alleles that are not in the data")
        test <- !(goDef %in% value)
        if (any(test)) {
            testDefinAll <- goDef[test] %in% goAll
            testDefinAllnotVa <- !(goDef[testDefinAll] %in% value)
            goPos <- goDef[testDefinAllnotVa]
            value <- .genotype2Haplotype(x = value)
            test <- !(goPos %in% value)
            goPos <- goPos[test]
            if (any(test)) 
                value <- c(value, goPos)
            testGOnotAll <- !(goAll %in% value)
            if (any(testGOnotAll)) 
                value <- c(value, goAll[testGOnotAll])
        }
        else {
            value <- .genotype2Haplotype(x = value)
        }
        attr(x, "genotypeOrder") <- value
    }
    x
}
