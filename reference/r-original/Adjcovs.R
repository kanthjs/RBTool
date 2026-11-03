`Adjcovs` <-
function (i, data, design = "lattice") 
{
    if (design == "lattice") {
        varcorr <- VarCorr(lmer(data[, i] ~ 1 + (1 | REP:Block) + 
            (1 | ENTRY), data = data, REML = TRUE))
        cov.names <- c("block(rep)", "ENTRY", "Residual")
    }
    else if (design == "rcb") {
        varcorr <- VarCorr(lmer(data[, i] ~ 1 + (1 | REP) + (1 | 
            ENTRY), data = data, REML = TRUE))
        cov.names <- c("rep", "ENTRY", "Residual")
    }
    covs <- c(varcorr$REP, varcorr$ENTRY, attr(varcorr, "sc")^2)
    names(covs) <- cov.names
    return(covs)
}
