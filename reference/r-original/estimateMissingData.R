`estimateMissingData` <-
function (design = c("CRD", "RCB"), data = NULL, respvar, male, 
    female, rep) 
UseMethod("estimateMissingData")
