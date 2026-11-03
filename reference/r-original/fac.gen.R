`fac.gen` <-
function (generate, each = 1, times = 1, order = "standard") 
{
    if (!is.list(generate)) 
        stop("generate must be a list")
    facs.reps <- factor.list(generate, order)
    fnames <- facs.reps$factors
    freps <- facs.reps$reps
    nfac <- length(fnames)
    levels <- rep(1, times = nfac)
    for (i in 1:nfac) {
        if (is.numeric(fnames[[i]]) | is.character(fnames[[i]])) {
            if (length(fnames[[i]]) == 1) 
                levels[i] <- fnames[[i]]
            else levels[i] <- length(fnames[[i]])
        }
        else {
            stop("Levels of factors must be specified using either numeric or character vectors")
        }
    }
    n <- prod(levels) * prod(freps) * each * times
    which.ord <- pmatch(casefold(order), c("standard", "yates"), 
        nomatch = "")
    if (which.ord == "") 
        stop("order must be either standard or yates")
    if (which.ord == "1") 
        counter <- nfac:1
    else counter <- 1:nfac
    genlist <- vector("list", nfac)
    keach <- each
    for (i in counter) {
        lev <- 1:levels[i]
        keach <- keach * freps[i]
        ktimes <- n/(levels[i] * keach)
        {
            if (is.numeric(fnames[[i]])) {
                if (length(fnames[[i]]) != 1) 
                  lev <- fnames[[i]]
                genlist[[i]] <- factor(rep(lev, times = ktimes, 
                  each = keach))
            }
            else {
                genlist[[i]] <- factor(rep(lev, times = ktimes, 
                  each = keach), labels = fnames[[i]])
            }
            keach <- keach * levels[i]
        }
    }
    genframe <- data.frame(genlist)
    names(genframe) <- names(fnames)
    genframe
}
