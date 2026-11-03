`powerTransform.default` <-
function (object, ...) 
{
    y <- object
    if (!inherits(y, "matrix") & !inherits(y, "data.frame")) {
        y <- matrix(y, ncol = 1)
        colnames(y) <- c(paste(deparse(substitute(object))))
    }
    y <- na.omit(y)
    x <- rep(1, dim(y)[1])
    estimateTransform(x, y, NULL, ...)
}
