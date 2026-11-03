`LD.data.frame` <-
function (g1, ...) 
{
    gvars <- sapply(g1, function(x) (is.genotype(x) && nallele(x) == 
        2))
    if (any(gvars == FALSE)) {
        warning("Non-genotype variables or genotype variables ", 
            "with more or less than two alleles detected. ", 
            "These variables will be omitted: ", paste(colnames(g1)[!gvars], 
                collapse = ", "))
        g1 <- g1[, gvars]
    }
    P <- matrix(nrow = ncol(g1), ncol = ncol(g1))
    rownames(P) <- colnames(g1)
    colnames(P) <- colnames(g1)
    P <- D <- Dprime <- nobs <- chisq <- p.value <- corr <- R.2 <- P
    for (i in 1:(ncol(g1) - 1)) for (j in (i + 1):ncol(g1)) {
        ld <- LD(g1[, i], g1[, j])
        D[i, j] <- ld$D
        Dprime[i, j] <- ld$"D'"
        corr[i, j] <- ld$r
        R.2[i, j] <- ld$"R^2"
        nobs[i, j] <- ld$n
        chisq[i, j] <- ld$"X^2"
        p.value[i, j] <- ld$"P-value"
    }
    retval <- list(call = match.call(), D = D, `D'` = Dprime, 
        r = corr, `R^2` = R.2, n = nobs, `X^2` = chisq, `P-value` = p.value)
    class(retval) <- "LD.data.frame"
    retval
}
