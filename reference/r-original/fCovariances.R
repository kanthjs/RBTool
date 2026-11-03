`fCovariances` <-
function (data) 
{
    Cov <- cov(data)
    nom <- colnames(data)
    dimnames(Cov) <- list(nom, nom)
    return(Cov)
}
