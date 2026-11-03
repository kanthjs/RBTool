`bcPower` <-
function (U, lambda, jacobian.adjusted = FALSE) 
{
    bc1 <- function(U, lambda) {
        if (any(U[!is.na(U)] <= 0)) 
            stop("First argument must be strictly positive.")
        z <- if (abs(lambda) <= 1e-06) 
            log(U)
        else ((U^lambda) - 1)/lambda
        if (jacobian.adjusted == TRUE) {
            z * (exp(mean(log(U), na.rm = TRUE)))^(1 - lambda)
        }
        else z
    }
    out <- U
    out <- if (is.matrix(out) | is.data.frame(out)) {
        if (is.null(colnames(out))) 
            colnames(out) <- paste("Z", 1:dim(out)[2], sep = "")
        for (j in 1:ncol(out)) {
            out[, j] <- bc1(out[, j], lambda[j])
        }
        colnames(out) <- paste(colnames(out), round(lambda, 2), 
            sep = "^")
        out
    }
    else bc1(out, lambda)
    out
}
