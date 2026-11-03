`stdskewness.default` <-
function (x, na.rm = TRUE) 
{
    if (!is.numeric(x) && !is.complex(x) && !is.logical(x) && 
        !is.vector(x)) 
        stop("The argument should be a numeric vector.")
    if (na.rm) 
        x <- x[!is.na(x)]
    else if (any(is.na(x))) 
        return(x[FALSE][NA])
    stdskewness <- skew(x)/sqrt(6/length(x))
    return(stdskewness)
}
