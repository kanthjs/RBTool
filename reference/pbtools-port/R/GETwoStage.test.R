`GETwoStage.test` <-
function (data, respvar, stderr = NULL, sigma2, numrep, geno, 
    env, weight = c("none", "stderr", "stdmean"), is.genoRandom = FALSE) 
UseMethod("GETwoStage.test")
