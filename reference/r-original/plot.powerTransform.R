`plot.powerTransform` <-
function (x, z = NULL, round = TRUE, plot = pairs, ...) 
{
    y <- match.fun(x$family)(x$y, coef(x, round = round))
    if (is.null(z)) 
        plot(y, ...)
    else if (is.matrix(z) | is.data.frame(z)) 
        plot(cbind(y, z), ...)
    else {
        y <- cbind(y, z)
        colnames(y)[dim(y)[2]] <- deparse(substitute(z))
        plot(y, ...)
    }
}
