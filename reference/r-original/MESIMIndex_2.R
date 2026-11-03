`MESIMIndex_2` <-
function (file.dat = NULL, weights = NULL, selval = 5, design = "lattice", 
    corr = FALSE, rawdata = TRUE, markers, qtls) 
{
    if (rawdata == TRUE) {
        traits <<- NULL
        traits2 <<- NULL
        covG <<- NULL
        covP <<- NULL
        allmean <<- NULL
    }
    if (is.null(traits)) {
        traits <<- Read(file.dat, weights)
    }
    theta <- rep(Readsgn(), 2)
    nom.traits <- colnames(traits)[-(1:fcol - 1)]
    if (is.null(covG) & is.null(covP) & is.null(allmean)) {
        covG <<- gCovariances(traits, design = design)
        dimnames(covG) <<- list(nom.traits, nom.traits)
        allmean <<- Means(traits)
        colnames(allmean) <<- nom.traits
        covP <<- fCovariances(allmean)
        dimnames(covP) <<- dimnames(covG)
    }
    codes <- ReadMarkers(markers)
    QTLS <- ReadQTL(qtls)
    cov.scores <- CovMol(codes, QTLS)
    MVMNS <<- as.matrix(cov.scores$S[1:ncol(allmean), 1:ncol(allmean)])
    dimnames(MVMNS) <- dimnames(covG)
    if (corr == TRUE) {
        mat.name <- "CORRELATION"
        MVGS <<- cov2cor(covG)
        MVG <- MVGS
        MVPS <<- cov2cor(covP)
        MVP <- MVPS
        MVMS <<- cov2cor(MVMNS)
        MVM <- MVMS
    }
    else {
        mat.name <- "COVARIANCE"
        MVG <- covG
        MVP <- covP
        MVM <- MVMNS
    }
    MMVP <<- rbind(cbind(MVP, MVM), cbind(MVM, MVM))
    MMVG <<- rbind(cbind(MVG, MVM), cbind(MVM, MVM))
    svdIPG <- svd(solve(MMVP) %*% MMVG)
    EigVecMESIM <- as.matrix(abs(svdIPG$u[, 1])) * theta
    EigVecMESIM <- EigVecMESIM/as.vector(sqrt(t(EigVecMESIM) %*% 
        EigVecMESIM))
    scores <- cov.scores$Scores[, 1:ncol(allmean)]
    YMESIM <<- cbind(scale(allmean), scale(scores)) %*% EigVecMESIM
    rnames <- rownames(YMESIM)
    thetaMESIM <- solve(MMVG) %*% (MMVP) %*% EigVecMESIM
    thetaNMESIM <- thetaMESIM %*% (1/sqrt(t(thetaMESIM) %*% thetaMESIM))
    vGb <- t(thetaNMESIM) %*% MMVG %*% EigVecMESIM
    VIDS <- t(EigVecMESIM) %*% MMVP %*% EigVecMESIM
    VDCG <- t(thetaNMESIM) %*% MMVG %*% thetaNMESIM
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrMESIM <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YMESIM >= quantile(YMESIM, 1 - selval/100)
    selYMESIM <- YMESIM[selected]
    ord <- order(selYMESIM, decreasing = TRUE)
    selYMESIM <- selYMESIM[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MMESIMSI <- apply(selentry, 2, mean)
    MMESIMall <- apply(allmean, 2, mean)
    GAINbyMEANS = MMESIMSI - MMESIMall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    MESIMGain <- as.vector(ks * (MMVG %*% EigVecMESIM)/as.numeric(sqrt(t(EigVecMESIM) %*% 
        MMVP %*% EigVecMESIM)))
    selentry2 <- rbind(selentry, MMESIMSI, MMESIMall, GAINbyMEANS, 
        MESIMGain)
    selMESIM <- data.frame(selentry2, c(selYMESIM, rep(NA, 4)))
    colnames(selMESIM)[ncol(selMESIM)] <- "MESIM index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selMESIM)[(length(selYMESIM) + 1):(length(selYMESIM) + 
        4)] <- rnames
    allMESIM <- data.frame(allmean, YMESIM)
    colnames(allMESIM)[ncol(allMESIM)] <- "MESIM index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), MVMole = round(MVM, 2), CovIndBV = vGb, VarInd = VIDS, 
        VarBV = VDCG, CorrIndBV = corrMESIM, SelGen = round(selMESIM, 
            2), AllGen = round(allMESIM, 2)))
}
