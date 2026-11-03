`makeHaplotypes` <-
function (data, convert, sep = "/", tol = 0.9, ...) 
{
    makeGenotypes(data = data, convert = convert, sep = sep, 
        tol = tol, method = as.haplotype, ...)
}
