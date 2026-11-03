`factor.list` <-
function (generate, order = "standard") 
{
    n <- length(generate)
    which.ord <- pmatch(casefold(order), c("standard", "yates"), 
        nomatch = "")
    if (which.ord == "") 
        stop("order must be either standard or yates")
    if (which.ord == "1") 
        counter <- 1:n
    else counter <- n:1
    kfac <- 0
    for (i in counter) {
        if (!(names(generate[i]) == "")) {
            kfac <- kfac + 1
            if (kfac == 1) {
                fnames <- list(generate[[i]])
                names(fnames) <- names(generate)[i]
                freps <- 1
            }
            else {
                knames <- list(generate[[i]])
                names(knames) <- names(generate)[i]
                fnames <- c(fnames, knames)
                freps <- c(freps, 1)
            }
        }
        else {
            if (kfac == 0) 
                if (which.ord == "1") 
                  stop("Must start with a factor name - set times argument instead")
                else stop("Must end with a factor name - set each argument instead")
            freps[kfac] <- generate[[i]]
        }
    }
    if (which.ord == "2") {
        fnames <- fnames[kfac:1]
        freps <- freps[kfac:1]
    }
    return(list(factors = fnames, reps = freps))
}
