`summary.LD.data.frame` <-
function (object, digits = getOption("digits"), which = c("D", 
    "D'", "r", "X^2", "P-value", "n", " "), rowsep, show.all = FALSE, 
    ...) 
{
    if (missing(rowsep)) 
        if (length(which) == 1) 
            rowsep <- NULL
        else rowsep <- " "
    if (is.null(rowsep)) 
        blank <- NULL
    else blank <- matrix(rowsep, ncol = ncol(object$D), nrow = nrow(object$D))
    saveopt <- options("digits")
    options(digits = digits)
    pdat <- list()
    for (name in which) pdat[[name]] <- object[[name]]
    library(gdata)
    tab <- interleave(D = if ("D" %in% names(pdat)) 
        pdat$D
    else NULL, `D'` = pdat$"D'", Corr. = pdat$r, `X^2` = pdat$"X^2", 
        `P-value` = pdat$"P-value", n = pdat$n, ` ` = blank, 
        sep = " ")
    statlist <- which[!(which %in% c("P-value", "n", " "))]
    statlist[statlist == "X^2"] <- "X\\^2"
    formatlist <- sapply(statlist, function(object) grep(object, 
        rownames(tab)))
    formatlist <- unique(sort(unlist(formatlist)))
    pvallist <- grep("P-value", rownames(tab))
    tab[formatlist, ] <- formatC(as.numeric(tab[formatlist, ]), 
        digits = digits, format = "f")
    tab[pvallist, ] <- apply(object$"P-value", c(1, 2), function(object) trim(format.pval(object, 
        digits = digits)))
    tab[trim(tab) == "NA"] <- NA
    if (!show.all) {
        entrylen <- nrow(tab)/nrow(object$n)
        tab <- tab[1:(nrow(tab) - entrylen), -1]
    }
    options(saveopt)
    class(tab) <- "summary.LD.data.frame"
    tab
}
