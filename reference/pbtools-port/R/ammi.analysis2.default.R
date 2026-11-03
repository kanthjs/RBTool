`ammi.analysis2.default` <-
function (ENV, GEN, REP, Y, SIGMA2, number = TRUE, biplotPC12 = FALSE, 
    biplotPC13 = FALSE, biplotPC23 = FALSE, ammi1 = FALSE, adaptMap = FALSE, 
    respVar) 
{
    numrepEnv <- tapply(REP, ENV, mean)
    sigma2Env <- tapply(SIGMA2, ENV, mean)
    MSE <- sum(numrepEnv * sigma2Env)/sum(numrepEnv)
    NREP <- 1/mean(1/numrepEnv)
    ammi12Out <- ammi.analysis(ENV, GEN, NREP, Y, MSE, number, 
        biplotPC12 = biplotPC12, biplotPC13 = biplotPC13, biplotPC23 = biplotPC23, 
        ammi1 = ammi1, adaptMap = adaptMap, yVar = respVar)
}
