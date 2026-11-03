`LTIndex_2` <-
function (file.dat = NULL, weights = NULL, selval = 5, design = "lattice", 
    corr = FALSE, rawdata = TRUE, markers, qtls) 
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
    scores <- cov.scores$Scores[, 1:ncol(allmean)]
    BLT1 <<- as.vector(solve(MMVP) %*% MMVG %*% theta)
    BLT2 <<- as.vector(sqrt(t(BLT1) %*% BLT1))
    BLT <<- BLT1/BLT2
    YLT <<- cbind(scale(allmean), scale(scores)) %*% BLT
    rnames <- rownames(YLT)
    thetaLT <- solve(MMVG) %*% (MMVP) %*% BLT
    thetaNLT <- thetaLT %*% (1/sqrt(t(thetaLT) %*% thetaLT))
    vGb <- t(thetaNLT) %*% MMVG %*% BLT
    VIDS <- t(BLT) %*% MMVP %*% BLT
    VDCG <- t(thetaNLT) %*% MMVG %*% thetaNLT
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrLT <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YLT >= quantile(YLT, 1 - selval/100)
    selYLT <- YLT[selected]
    ord <- order(selYLT, decreasing = TRUE)
    selYLT <- selYLT[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MLTSI <- apply(selentry, 2, mean)
    MLTall <- apply(allmean, 2, mean)
    GAINbyMEANS = MLTSI - MLTall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    LTGain <- as.vector(ks * (MMVG %*% BLT)/as.numeric(sqrt(t(BLT) %*% 
        MMVP %*% BLT)))
    selentry2 <- rbind(selentry, MLTSI, MLTall, GAINbyMEANS, 
        LTGain)
    selLT <- data.frame(selentry2, c(selYLT, rep(NA, 4)))
    colnames(selLT)[ncol(selLT)] <- "LT index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selLT)[(length(selYLT) + 1):(length(selYLT) + 4)] <- rnames
    allLT <- data.frame(allmean, YLT)
    colnames(allLT)[ncol(allLT)] <- "LT index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), MVMole = round(MVM, 2), CovIndBV = vGb, VarInd = VIDS, 
        VarBV = VDCG, CorrIndBV = corrLT, SelGen = round(selLT, 
            2), AllGen = round(allLT, 2)))
}
