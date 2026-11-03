`estimateNA` <-
function (design = c("Alpha", "RowColumn"), fullData, respvar, 
    parent1, parent2, rep, block = NULL, row = NULL, column = NULL) 
UseMethod("estimateNA")
