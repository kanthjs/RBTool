`ReadQTL` <-
function (qtl = NULL) 
{
    qtl0 <- qtl
    qtl0 <- read.csv(file = qtl0, skip = 1)
    qtl <- qtl0[, seq(1, ncol(qtl0) - 1, by = 2)]
    link <- qtl0[, seq(2, ncol(qtl0), by = 2)]
    return(list(QTL = qtl, Link = link))
}
