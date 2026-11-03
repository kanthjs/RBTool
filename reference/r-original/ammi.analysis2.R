`ammi.analysis2` <-
function (ENV, GEN, REP, Y, SIGMA2, number = TRUE, biplotPC12 = FALSE, 
    biplotPC13 = FALSE, biplotPC23 = FALSE, ammi1 = FALSE, adaptMap = FALSE, 
    respVar) 
UseMethod("ammi.analysis2")
