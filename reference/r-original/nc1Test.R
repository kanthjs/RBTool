`nc1Test` <-
function (design = c("CRD", "RCB", "Alpha", "RowColumn"), data, 
    respvar, female, male, rep = NULL, block = NULL, row = NULL, 
    column = NULL, inbred = TRUE, individual = NULL, environment = NULL) 
UseMethod("nc1Test")
