`popStructure.default` <-
function (genindObject, pairwiseFst = FALSE) 
{
    options(width = 500)
    library(adegenet)
    fValues <- fstat(genindObject)
    result <- list()
    fst <- fValues["Total", "pop"]
    fit <- fValues["Total", "Ind"]
    fis <- fValues["pop", "Ind"]
    result$fst <- fst
    result$fit <- fit
    result$fis <- fis
    if (pairwiseFst) {
        fstMatrix <- pairwise.fst(genindObject, res.type = "matrix")
        result$fstMatrix <- round(fstMatrix, digits = 4)
    }
    return(result)
}
