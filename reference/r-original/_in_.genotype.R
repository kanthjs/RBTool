`%in%.genotype` <-
function (x, table) 
{
    xA1 <- allele(x, 1)
    xA2 <- allele(x, 2)
    x1 <- paste(xA1, xA2, sep = "/")
    x2 <- paste(xA2, xA1, sep = "/")
    ((x1 %in% table) | (x2 %in% table))
}
