`recodeDiallelData` <-
function (design = c("diallel1", "diallel2", "diallel3", "diallel4"), 
    data, p1, p2, rep, block = NULL, row = NULL, column = NULL, 
    respvar) 
UseMethod("recodeDiallelData")
