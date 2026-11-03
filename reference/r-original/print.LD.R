`print.LD` <-
function (x, digits = getOption("digits"), ...) 
{
    saveopt <- options("digits")
    options(digits = digits)
    cat("\n")
    cat("Pairwise LD\n")
    cat("-----------\n")
    est <- t(as.matrix(c(D = x$D, `D'` = x$"D'", Corr = x$r)))
    rownames(est) <- "Estimates:"
    print(est)
    cat("\n")
    test <- t(as.matrix(c(`X^2` = x$"X^2", `P-value` = x$"P-value", 
        N = x$n)))
    rownames(test) <- "LD Test:"
    print(test)
    cat("\n")
    options(saveopt)
    invisible(x)
}
