`genoNpheno.corr` <-
function (exptl.design = c("RCB", "AugRCB", "AugLS", "Alpha", 
    "RowCol", "LatinAlpha", "LatinRowCol"), data, respvar, geno, 
    row, column = NULL, rep = NULL, env, excludeLevels = FALSE, 
    excludeList = NULL) 
UseMethod("genoNpheno.corr")
