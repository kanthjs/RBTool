`KNIndex_2` <-
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
    thetaKN <- Readwgt()
    nom.traits <- colnames(traits)[-(1:fcol - 1)]
    W <- Readrest(weights)
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
    IcovP <- solve(MVP)
    covGV <- MVG %*% thetaKN
    Bs <- IcovP %*% covGV
    C <- t(MVG) %*% W
    Port <- IcovP %*% C %*% solve(t(C) %*% IcovP %*% C) %*% t(C)
    NR <- nrow(Port)
    A2 <- diag(NR)
    A <- A2 - Port
    BKNnn <- A %*% Bs
    BKN <- BKNnn/sqrt(sum(BKNnn^2))
    YKN <<- scale(allmean) %*% BKN
    rnames <- rownames(YKN)
    vGb <- t(thetaKN) %*% MVG %*% BKN
    VIDS <- t(BKN) %*% MVP %*% BKN
    VDCG <- t(thetaKN) %*% MVG %*% thetaKN
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrKN <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YKN >= quantile(YKN, 1 - selval/100)
    selYKN <- YKN[selected]
    ord <- order(selYKN, decreasing = TRUE)
    selYKN <- selYKN[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MKNSI <- apply(selentry, 2, mean)
    MKNall <- apply(allmean, 2, mean)
    GAINbyMEANS = MKNSI - MKNall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    KNGain <- as.vector(ks * (covG %*% BKN)/as.numeric(sqrt(t(BKN) %*% 
        covP %*% BKN)))
    selentry2 <- rbind(selentry, MKNSI, MKNall, GAINbyMEANS, 
        KNGain)
    selKN <- data.frame(selentry2, c(selYKN, rep(NA, 4)))
    colnames(selKN)[ncol(selKN)] <- "KN index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selKN)[(length(selYKN) + 1):(length(selYKN) + 4)] <- rnames
    allKN <- data.frame(allmean, YKN)
    colnames(allKN)[ncol(allKN)] <- "KN index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), CovIndBV = vGb, VarInd = VIDS, VarBV = VDCG, CorrIndBV = corrKN, 
        SelGen = round(selKN, 2), AllGen = round(allKN, 2)))
}
