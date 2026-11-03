`gge.analysis` <-
function (ENV, GEN, REP, Y, MSE = 0, number = TRUE, yVar = "Y1", 
    f = 0.5, graphSym = FALSE, graphEnv = FALSE, graphGeno = FALSE, 
    ...) 
UseMethod("gge.analysis")
