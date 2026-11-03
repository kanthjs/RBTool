`adjmeans` <-
function (i, data, design = "lattice") 
{
    mu <- switch(design, lattice = lmer(data[, i] ~ ENTRY + (1 | 
        REP/Block) - 1, data = data, REML = TRUE)@mu, rcb = lmer(data[, 
        i] ~ ENTRY + (1 | REP) - 1, data = data, REML = TRUE)@mu)
    matmu <- matrix(mu, ncol = length(unique(data$REP)))
    means <- apply(matmu, 1, mean)
    names(means) <- paste("Entry ", unique(data$ENTRY), sep = "")
    return(means)
}
