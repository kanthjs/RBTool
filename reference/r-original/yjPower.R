`yjPower` <-
function (U, lambda, jacobian.adjusted = FALSE) 
{
    yj1 <- function(U, lambda) {
        nonnegs <- U >= 0
        z <- rep(NA, length(U))
        z[which(nonnegs)] <- bcPower(U[which(nonnegs)] + 1, lambda, 
            jacobian.adjusted = FALSE)
        z[which(!nonnegs)] <- -bcPower(-U[which(!nonnegs)] + 
            1, 2 - lambda, jacobian.adjusted = FALSE)
        if (jacobian.adjusted == TRUE) 
            z * (exp(mean(log((1 + abs(U))^(2 * nonnegs - 1)), 
                na.rm = TRUE)))^(1 - lambda)
        else z
    }
    out <- U
    out <- if (is.matrix(out) | is.data.frame(out)) {
        if (is.null(colnames(out))) 
            colnames(out) <- paste("Z", 1:dim(out)[2], sep = "")
        for (j in 1:ncol(out)) {
            out[, j] <- yj1(out[, j], lambda[j])
        }
        colnames(out) <- paste(colnames(out), round(lambda, 2), 
            sep = "^")
        out
    }
    else yj1(out, lambda)
    out
}
