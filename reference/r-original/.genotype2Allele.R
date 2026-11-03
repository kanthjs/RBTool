`.genotype2Allele` <-
function (x) 
{
    parts <- strsplit(x = as.character(x), split = "/")
    parts <- t(sapply(parts, c))
    parts
}
