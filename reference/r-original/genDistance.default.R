`genDistance.default` <-
function (genindObject, method = 1, displayDiag = FALSE, displayUpper = FALSE) 
{
    options(width = 500)
    library(adegenet)
    pop <- genind2genpop(genindObject, miss = "0")
    dist <- dist.genpop(pop, met = method, diag = displayDiag, 
        upper = displayUpper)
    result <- round(dist, digits = 4)
    return(result)
}
