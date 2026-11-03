`.genotype2Haplotype` <-
function (x) 
{
    x <- unique(x)
    N <- length(x)
    parts <- .genotype2Allele(x = x)
    parts <- rbind(parts, parts[, 2:1])
    ind <- rep(1:N, each = 2) + c(0, N)
    parts <- parts[ind, ]
    parts <- unique(paste(parts[, 1], parts[, 2], sep = "/"))
    parts
}
