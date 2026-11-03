`ammi.analysis` <-
function (ENV, GEN, REP, Y, MSE = 0, number = TRUE, biplotPC12 = FALSE, 
    biplotPC13 = FALSE, biplotPC23 = FALSE, ammi1 = FALSE, adaptMap = FALSE, 
    yVar = "Y1", ...) 
UseMethod("ammi.analysis")
