`generateBalancedData` <-
function (design = c("NESTED", "FACTORIAL", "DIALLEL2", "DIALLEL3", 
    "DIALLEL4"), data, respvar, male, female, rep) 
UseMethod("generateBalancedData")
