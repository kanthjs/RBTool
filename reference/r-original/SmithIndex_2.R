`SmithIndex_2` <-
function (file.dat = NULL, weights = NULL, selval = 5, design = "lattice", 
    corr = FALSE, rawdata = TRUE) 
{
    library(lme4)
    library(Hmisc)
    if (rawdata == TRUE) {
        traits <<- NULL
        covG <<- NULL
        covP <<- NULL
        allmean <<- NULL
    }
    if (is.null(traits)) {
        traits <<- Read(file.dat, weights)
    }
    theta <- Readwgt()
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
    BSmith1 <- as.vector(solve(MVP) %*% MVG %*% theta)
    BSmith2 <- as.vector(sqrt(t(BSmith1) %*% BSmith1))
    BSmith <- BSmith1/BSmith2
    YSmith <<- scale(allmean) %*% BSmith
    rnames <- rownames(YSmith)
    vGb <- t(theta) %*% MVG %*% BSmith
    VIDS <- t(BSmith) %*% MVP %*% BSmith
    VDCG <- t(theta) %*% MVG %*% theta
    vGv <- sqrt(abs(VDCG))
    bPb <- sqrt(abs(VIDS))
    corrSMITH <- min(0.9999, vGb/(vGv %*% bPb))
    selected <- YSmith >= quantile(YSmith, 1 - selval/100)
    selYSmith <- YSmith[selected]
    ord <- order(selYSmith, decreasing = TRUE)
    selYSmith <- selYSmith[ord]
    selentry <- allmean[selected, ]
    nom.selentry <- rownames(allmean)[selected]
    selentry <- data.frame(as.matrix(selentry)[ord, ])
    dimnames(selentry) <- list(nom.selentry[ord], nom.traits)
    MsmithSI <- apply(selentry, 2, mean)
    Msmithall <- apply(allmean, 2, mean)
    GAINbyMEANS = MsmithSI - Msmithall
    ks <- if (selval == 5) 
        2.063
    else if (selval == 10) 
        1.755
    else 1.4
    SMITHGain = as.vector(ks * (covG %*% BSmith)/as.numeric(sqrt(t(BSmith) %*% 
        covP %*% BSmith)))
    selentry2 <- rbind(selentry, MsmithSI, Msmithall, GAINbyMEANS, 
        SMITHGain)
    selSMITH <- data.frame(selentry2, c(selYSmith, rep(NA, 4)))
    colnames(selSMITH)[ncol(selSMITH)] <- "Smith index"
    rnames <- c("Mean of Selected Individuals", "Mean of all Individuals", 
        "Selection Differential", paste("Expected Genetic Gain for", 
            paste(selval, "%", sep = ""), sep = " "))
    rownames(selSMITH)[(length(selYSmith) + 1):(length(selYSmith) + 
        4)] <- rnames
    allSMITH <- data.frame(allmean, YSmith)
    colnames(allSMITH)[ncol(selSMITH)] <- "Smith index"
    rm(fcol, ntraits, mat, pos = ".GlobalEnv")
    detach("package:lme4")
    detach("package:Hmisc")
    return(list(MVGeno = round(MVG, 2), MVPheno = round(MVP, 
        2), CovIndBV = vGb, VarInd = VIDS, VarBV = VDCG, CorrIndBV = corrSMITH, 
        SelGen = round(selSMITH, 2), AllGen = round(allSMITH, 
            2)))
}
