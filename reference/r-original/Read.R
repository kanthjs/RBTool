`Read` <-
function (file.dat = NULL, file.wgt = NULL) 
{
    fcol <<- 4
    fname <- file.dat
    traits1 <- read.csv(file = fname, header = T)
    traits1[, 1] <- as.factor(traits1[, 1])
    traits1[, 2] <- as.factor(traits1[, 2])
    traits1[, 3] <- as.factor(traits1[, 3])
    nom <- colnames(traits1[, -(1:(fcol - 1))])
    n <- length(nom)
    fname <- file.wgt
    wgts <<- read.csv(file = fname, header = T)
    traits <- TraitsSelect(traits1, wgts[, 2])
    ntraits <<- ncol(traits) - (fcol - 1)
    return(traits)
}
