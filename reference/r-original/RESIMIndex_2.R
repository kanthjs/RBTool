`RESIMIndex_2` <-
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
    C <- t(MVG) %*% W
    Port <- IcovP %*% C %*% solve(t(C) %*% IcovP %*% C) %*% t(C)
    NR <- nrow(Port)
    QI <- diag(NR)
    A2 <- QI - Port
    A <- A2 %*% IcovP %*% MVG
    BRESIM <- abs(svd(A)$u[, 1]) * theta
    thetaRESIM <- solve(MVG) %*% (MVP) %*% BRESIM
    thetanRESIM <- thetaRESIM/sqrt(sum(thetaRESIM^2))
    YRESIM <<- scale(allmean) %*% BRESIM
    rnames <- rownames(YRESIM)
    vGb <- t(thetanRESIM) %*% MVG %*% BRESIM
    VIDS <- t(BRESIM) %*% MVP %*% BRESIM
    VDCG <- t(thetanRESIM) %*% MVG %*% thetanRESIM
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrRESIM <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YRESIM >= quantile(YRESIM, 1 - selval/100)
    selYRESIM <- YRESIM[selected]
    ord <- order(selYRESIM, decreasing = TRUE)
    selYRESIM <- selYRESIM[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MRESIMSI <- apply(selentry, 2, mean)
    MRESIMall <- apply(allmean, 2, mean)
    GAINbyMEANS = MRESIMSI - MRESIMall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    RESIMGain <- as.vector(ks * (covG %*% BRESIM)/as.numeric(sqrt(t(BRESIM) %*% 
        covP %*% BRESIM)))
    selentry2 <- rbind(selentry, MRESIMSI, MRESIMall, GAINbyMEANS, 
        RESIMGain)
    selRESIM <- data.frame(selentry2, c(selYRESIM, rep(NA, 4)))
    colnames(selRESIM)[ncol(selRESIM)] <- "RESIM index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selRESIM)[(length(selYRESIM) + 1):(length(selYRESIM) + 
        4)] <- rnames
    allRESIM <- data.frame(allmean, YRESIM)
    colnames(allRESIM)[ncol(allRESIM)] <- "RESIM index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), CovIndBV = vGb, VarInd = VIDS, VarBV = VDCG, CorrIndBV = corrRESIM, 
        SelGen = round(selRESIM, 2), AllGen = round(allRESIM, 
            2)))
}
