`LD.genotype` <-
function (g1, g2, ...) 
{
    if (is.haplotype(g1) || is.haplotype(g2)) 
        stop("Haplotype options are not yet supported.")
    if (nallele(g1) != 2 || nallele(g2) != 2) 
        stop("This function currently only supports 2-allele genotypes.")
    prop.A <- summary(g1)$allele.freq[, 2]
    prop.B <- summary(g2)$allele.freq[, 2]
    major.A <- names(prop.A)[which.max(prop.A)]
    major.B <- names(prop.B)[which.max(prop.B)]
    pA <- max(prop.A, na.rm = TRUE)
    pB <- max(prop.B, na.rm = TRUE)
    pa <- 1 - pA
    pb <- 1 - pB
    Dmin <- max(-pA * pB, -pa * pb)
    pmin <- pA * pB + Dmin
    Dmax <- min(pA * pb, pB * pa)
    pmax <- pA * pB + Dmax
    counts <- table(allele.count(g1, major.A), allele.count(g2, 
        major.B))
    n3x3 <- matrix(0, nrow = 3, ncol = 3)
    colnames(n3x3) <- rownames(n3x3) <- 0:2
    for (i in rownames(counts)) for (j in colnames(counts)) n3x3[3 - 
        as.numeric(i), 3 - as.numeric(j)] <- counts[i, j]
    loglik <- function(pAB, ...) {
        (2 * n3x3[1, 1] + n3x3[1, 2] + n3x3[2, 1]) * log(pAB) + 
            (2 * n3x3[1, 3] + n3x3[1, 2] + n3x3[2, 3]) * log(pA - 
                pAB) + (2 * n3x3[3, 1] + n3x3[2, 1] + n3x3[3, 
            2]) * log(pB - pAB) + (2 * n3x3[3, 3] + n3x3[3, 2] + 
            n3x3[2, 3]) * log(1 - pA - pB + pAB) + n3x3[2, 2] * 
            log(pAB * (1 - pA - pB + pAB) + (pA - pAB) * (pB - 
                pAB))
    }
    solution <- optimize(loglik, lower = pmin + .Machine$double.eps, 
        upper = pmax - .Machine$double.eps, maximum = TRUE)
    pAB <- solution$maximum
    estD <- pAB - pA * pB
    if (estD > 0) 
        estDp <- estD/Dmax
    else estDp <- estD/Dmin
    n <- sum(n3x3)
    corr <- estD/sqrt(pA * pB * pa * pb)
    dchi <- (2 * n * estD^2)/(pA * pa * pB * pb)
    dpval <- 1 - pchisq(dchi, 1)
    retval <- list(call = match.call(), D = estD, `D'` = estDp, 
        r = corr, `R^2` = corr^2, n = n, `X^2` = dchi, `P-value` = dpval)
    class(retval) <- "LD"
    retval
}
