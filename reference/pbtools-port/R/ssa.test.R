`ssa.test` <-
function (exptl.design = c("RCB", "AugRCB", "AugLS", "Alpha", 
    "RowCol", "LatinAlpha", "LatinRowCol"), data, respvar, geno, 
    row, column = NULL, rep = NULL, env = NULL, is.random = FALSE, 
    excludeCheck = FALSE, checkList = NULL) 
UseMethod("ssa.test")
