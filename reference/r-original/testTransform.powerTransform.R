`testTransform.powerTransform` <-
function (object, lambda = rep(1, dim(object$y)[2])) 
{
    fam <- match.fun(object$family)
    Y <- cbind(object$y)
    nc <- dim(Y)[2]
    nr <- nrow(Y)
    lam <- if (length(lambda) == 1) 
        rep(lambda, nc)
    else lambda
    xqr <- object$xqr
    w <- if (is.null(object$weights)) 
        1
    else sqrt(object$weights)
    llik <- function(lambda) {
        (nr/2) * log(((nr - 1)/nr) * det(var(qr.resid(xqr, w * 
            fam(Y, lam, j = TRUE)))))
    }
    LR <- 2 * (llik(lambda) - object$value)
    df <- length(object$lambda)
    pval <- 1 - pchisq(LR, df)
    out <- data.frame(LRT = LR, df = df, pval = pval)
    rownames(out) <- c(paste("LR test, lambda = (", paste(round(lam, 
        2), collapse = " "), ")", sep = ""))
    out
}
