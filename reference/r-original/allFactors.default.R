`allFactors.default` <-
function (x) 
{
    x <- as.integer(x)
    div <- seq_len(abs(x))
    factors <- div[x%%div == 0L]
    return(factors)
}
