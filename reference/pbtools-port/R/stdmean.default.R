`stdmean.default` <-
function (x, na.rm = TRUE) 
{
    if (!is.numeric(x) && !is.complex(x) && !is.logical(x) && 
        !is.vector(x)) 
        stop("The argument should be a numeric vector.")
    if (na.rm) 
        x <- x[!is.na(x)]
    else if (any(is.na(x))) 
        return(x[FALSE][NA])
    stdmean <- sqrt(var(x)/length(x))
    return(stdmean)
}
