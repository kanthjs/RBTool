`nc3TestME` <-
function (design = c("CRD", "RCB", "Alpha", "RowColumn"), data, 
    respvar, tester, f2lines, rep = NULL, block = NULL, row = NULL, 
    column = NULL, individual = NULL, environment = NULL) 
UseMethod("nc3TestME")
