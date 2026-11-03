`gge.analysis2.default` <-
function (ENV, GEN, REP, Y, SIGMA2, number = TRUE, graphSym = FALSE, 
    graphEnv = FALSE, graphGeno = FALSE, respVar, f = 0.5) 
{
    numrepEnv <- tapply(REP, ENV, mean)
    sigma2Env <- tapply(SIGMA2, ENV, mean)
    MSE <- sum(numrepEnv * sigma2Env)/sum(numrepEnv)
    NREP <- 1/mean(1/numrepEnv)
    gge2Out <- gge.analysis(ENV, GEN, NREP, Y, MSE, number, graphSym = graphSym, 
        graphEnv = graphEnv, graphGeno = graphGeno, yVar = respVar, 
        f)
}
