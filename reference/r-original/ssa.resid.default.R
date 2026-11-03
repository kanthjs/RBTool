`ssa.resid.default` <-
function (data, ssaResult, respvar, env, is.genoRandom = FALSE) 
{
    result <- list()
    for (i in (1:length(respvar))) {
        result[[i]] <- list()
        nlevelsEnv <- 0
        if (!is.null(env)) {
            nlevelsEnv <- nlevels(factor(data[, match(env, names(data))]))
        }
        else {
            nlevelsEnv <- 1
        }
        if (is.genoRandom) {
            names.resid <- paste(respvar[i], "resid_random", 
                sep = "_")
        }
        else {
            names.resid <- paste(respvar[i], "resid_fixed", sep = "_")
        }
        for (j in (1:nlevelsEnv)) {
            if (j == 1) {
                result[[i]]$residuals <- as.data.frame(ssaResult$output[[i]]$site[[j]]$residuals)
                if (nrow(result[[i]]$residuals) > 0) {
                  names(result[[i]]$residuals) <- names.resid
                  dataForResid <- ssaResult$output[[i]]$site[[j]]$data
                  areColumnsPresent <- match(c("Test", "Check"), 
                    names(dataForResid))
                  if (all(is.na(areColumnsPresent))) {
                  }
                  else {
                    dataForResid <- dataForResid[-c(areColumnsPresent)]
                  }
                  result[[i]]$residuals <- cbind(dataForResid, 
                    result[[i]]$residuals)
                }
            }
            else {
                resid2 <- as.data.frame(ssaResult$output[[i]]$site[[j]]$residuals)
                if (nrow(resid2) > 0) {
                  names(resid2) <- names.resid
                  dataForResid2 <- ssaResult$output[[i]]$site[[j]]$data
                  areColumnsPresent <- match(c("Test", "Check"), 
                    names(dataForResid2))
                  if (all(is.na(areColumnsPresent))) {
                  }
                  else {
                    dataForResid2 <- dataForResid2[-c(areColumnsPresent)]
                  }
                  resid2 <- cbind(dataForResid2, resid2)
                  if (nrow(result[[i]]$residuals) == 0) {
                    result[[i]]$residuals <- resid2
                  }
                  else {
                    result[[i]]$residuals <- rbind(result[[i]]$residuals, 
                      resid2)
                  }
                }
            }
        }
        if (i == 1) {
            resid.out.all <- result[[i]]$residuals
        }
        else {
            residOut2 <- result[[i]]$residuals
            if (nrow(residOut2) > 0) {
                if (nrow(resid.out.all) == 0) {
                  resid.out.all <- residOut2
                }
                else {
                  resid.out.all <- merge(resid.out.all, residOut2, 
                    all = TRUE, sort = TRUE)
                }
            }
        }
    }
    if (nrow(resid.out.all) == 0) {
        residWarning <- "empty"
    }
    else {
        if (!is.null(env)) {
            resid.out.all <- SortCases(resid.out.all, env)
        }
        residWarning <- "not empty"
    }
    return(list(residuals = resid.out.all, residWarning = residWarning))
}
