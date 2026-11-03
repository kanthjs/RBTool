`ESIMIndex_2` <-
function (file.dat = NULL, weights = NULL, selval = 5, design = "lattice", 
    corr = FALSE, rawdata = TRUE) 
{
    if (rawdata == TRUE) {
        traits <<- NULL
        covG <<- NULL
        covP <<- NULL
        allmean <<- NULL
    }
    if (is.null(traits)) {
        traits <<- Read(file.dat, weights)
    }
    theta <- Readsgn()
    nom.traits <- colnames(traits)[-(1:fcol - 1)]
    if (is.null(covG) & is.null(covP) & is.null(allmean)) {
        covG <<- gCovariances(traits, design = design)
        dimnames(covG) <<- list(nom.traits, nom.traits)
        allmean <<- Means(traits)
        colnames(allmean) <<- nom.traits
        covP <<- fCovariances(allmean)
        dimnames(covP) <<- dimnames(covG)
    }
    if (corr == TRUE) {
        mat.name <- "CORRELATION"
        MVGS <<- cov2cor(covG)
        MVG <- MVGS
        MVPS <<- cov2cor(covP)
        MVP <- MVPS
    }
    else {
        mat.name <- "COVARIANCE"
        MVG <- covG
        MVP <- covP
    }
    svdIPG <- svd(solve(MVP) %*% MVG)
    EigVecESIM <- as.matrix(abs(svdIPG$u[, 1])) * theta
    EigVecESIM <- EigVecESIM/as.vector(sqrt(t(EigVecESIM) %*% 
        EigVecESIM))
    YESIM <<- scale(allmean) %*% EigVecESIM
    rnames <- rownames(YESIM)
    thetaESIM <- solve(MVG) %*% (MVP) %*% EigVecESIM
    thetaNESIM <- thetaESIM %*% (1/sqrt(t(thetaESIM) %*% thetaESIM))
    vGb <- t(thetaNESIM) %*% MVG %*% EigVecESIM
    VIDS <- t(EigVecESIM) %*% MVP %*% EigVecESIM
    VDCG <- t(thetaNESIM) %*% MVG %*% thetaNESIM
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrESIM <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YESIM >= quantile(YESIM, 1 - selval/100)
    selYESIM <- YESIM[selected]
    ord <- order(selYESIM, decreasing = TRUE)
    selYESIM <- selYESIM[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MESIMSI <- apply(selentry, 2, mean)
    MESIMall <- apply(allmean, 2, mean)
    GAINbyMEANS = MESIMSI - MESIMall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    ESIMGain <- as.vector(ks * (covG %*% EigVecESIM)/as.numeric(sqrt(t(EigVecESIM) %*% 
        covP %*% EigVecESIM)))
    selentry2 <- rbind(selentry, MESIMSI, MESIMall, GAINbyMEANS, 
        ESIMGain)
    selESIM <- data.frame(selentry2, c(selYESIM, rep(NA, 4)))
    colnames(selESIM)[ncol(selESIM)] <- "ESIM index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selESIM)[(length(selYESIM) + 1):(length(selYESIM) + 
        4)] <- rnames
    allESIM <- data.frame(allmean, YESIM)
    colnames(allESIM)[ncol(allESIM)] <- "ESIM index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), CovIndBV = vGb, VarInd = VIDS, VarBV = VDCG, CorrIndBV = corrESIM, 
        SelGen = round(selESIM, 2), AllGen = round(allESIM, 2)))
}
