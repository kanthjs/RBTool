`[<-.haplotype` <-
function (x, i, value) 
{
    if (!is.haplotype(value)) 
        stop("Assigned value must be of class haplotype.")
    NextMethod("[<-")
}
