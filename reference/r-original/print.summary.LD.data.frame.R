`print.summary.LD.data.frame` <-
function (x, digits = getOption("digits"), ...) 
{
    cat("\n")
    cat("Pairwise LD\n")
    cat("-----------\n")
    print(as.matrix(unclass(x)), digits = digits, quote = FALSE, 
        na.print = "    ", right = TRUE)
    cat("\n")
    invisible(x)
}
