`print.summary.powerTransform` <-
function (x, digits = 4, ...) 
{
    cat(x$label)
    print(round(x$result, digits))
    cat("\nLikelihood ratio tests about transformation parameters\n")
    print(x$tests)
}
