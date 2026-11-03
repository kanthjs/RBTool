`ttcTestME` <-
function (design = c("CRD", "RCB", "Alpha", "RowColumn"), data, 
    respvar, tester, f2lines, rep = NULL, block = NULL, row = NULL, 
    column = NULL, individual = NULL, environment, codeP1, codeP2, 
    codeF1, alpha = 0.05) 
UseMethod("ttcTestME")
