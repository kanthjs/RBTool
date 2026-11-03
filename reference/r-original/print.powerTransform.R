`print.powerTransform` <-
function (x, ...) 
{
    cat("Estimated transformation parameters \n")
    print(x$lambda)
    invisible(x)
}
