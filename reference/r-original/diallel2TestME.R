`diallel2TestME` <-
function (design = c("CRD", "RCB", "Alpha", "RowColumn"), data, 
    respvar, p1, p2, rep = NULL, block = NULL, row = NULL, column = NULL, 
    cross = TRUE, individual = NULL, environment, alpha = 0.05) 
UseMethod("diallel2TestME")
