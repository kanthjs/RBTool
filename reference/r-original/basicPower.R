`basicPower` <-
function (U, lambda) 
{
    bp1 <- function(U, lambda) {
        if (any(U[!is.na(U)] <= 0)) 
            stop("First argument must be strictly positive.")
        if (abs(lambda) <= 1e-06) 
            log(U)
        else (U^lambda)
    }
    out <- U
    out <- if (is.matrix(out) | is.data.frame(out)) {
        if (is.null(colnames(out))) 
            colnames(out) <- paste("Z", 1:dim(out)[2], sep = "")
        for (j in 1:ncol(out)) {
            out[, j] <- bp1(out[, j], lambda[j])
            colnames(out)[j] <- if (abs(lambda[j]) <= 1e-06) 
                paste("log(", colnames(out)[j], ")", sep = "")
            else paste(colnames(out)[j], round(lambda[j], 2), 
                sep = "^")
        }
        out
    }
    else bp1(out, lambda)
    out
}
