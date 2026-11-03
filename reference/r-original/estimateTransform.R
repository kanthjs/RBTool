`estimateTransform` <-
function (X, Y, weights = NULL, family = "bcPower", start = NULL, 
    method = "L-BFGS-B", ...) 
{
    fam <- match.fun(family)
    Y <- as.matrix(Y)
    X <- as.matrix(X)
    w <- if (is.null(weights)) 
        1
    else sqrt(weights)
    nc <- dim(Y)[2]
    nr <- nrow(Y)
    xqr <- qr(w * X)
    llik <- function(lambda) {
        (nr/2) * log(((nr - 1)/nr) * det(var(qr.resid(xqr, w * 
            fam(Y, lambda, j = TRUE)))))
    }
    llik1d <- function(lambda, Y) {
        (nr/2) * log(((nr - 1)/nr) * var(qr.resid(xqr, w * fam(Y, 
            lambda, j = TRUE))))
    }
    if (is.null(start)) {
        start <- rep(1, nc)
        for (j in 1:nc) {
            res <- suppressWarnings(optimize(f = function(lambda) llik1d(lambda, 
                Y[, j, drop = FALSE]), lower = -3, upper = +3))
            start[j] <- res$minimum
        }
    }
    res <- optim(start, llik, hessian = TRUE, method = method, 
        ...)
    if (res$convergence != 0) 
        warning(paste("Convergence failure: return code =", res$convergence))
    res$start <- start
    res$lambda <- res$par
    names(res$lambda) <- if (is.null(colnames(Y))) 
        paste("Y", 1:dim(Y)[2], sep = "")
    else colnames(Y)
    roundlam <- res$lambda
    stderr <- sqrt(diag(solve(res$hessian)))
    lamL <- roundlam - 1.96 * stderr
    lamU <- roundlam + 1.96 * stderr
    for (val in rev(c(1, 0, -1, 0.5, 0.33, -0.5, -0.33, 2, -2))) {
        sel <- lamL <= val & val <= lamU
        roundlam[sel] <- val
    }
    res$roundlam <- roundlam
    res$par <- NULL
    res$family <- family
    res$xqr <- xqr
    res$y <- Y
    res$x <- X
    res$weights <- weights
    class(res) <- "powerTransform"
    res
}
