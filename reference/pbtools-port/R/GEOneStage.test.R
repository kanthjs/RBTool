`GEOneStage.test` <-
function (exptl.design = c("RCB", "AugRCB", "AugLS", "Alpha", 
    "RowCol", "LatinAlpha", "LatinRowCol"), data, respvar, geno, 
    row, column = NULL, rep = NULL, env, is.genoRandom = FALSE) 
UseMethod("GEOneStage.test")
