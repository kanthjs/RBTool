`multiplicativeModels` <-
function (data, outFileName, respvar, environment, genotype, 
    numberOfReps, residualVariances, responsePlot = FALSE, doAMMI = TRUE, 
    biplotPC12 = FALSE, biplotPC13 = FALSE, biplotPC23 = FALSE, 
    ammi1 = FALSE, adaptMap = FALSE, doGGE = FALSE, graphSym = FALSE, 
    graphEnv = FALSE, graphGeno = FALSE) 
UseMethod("multiplicativeModels")
