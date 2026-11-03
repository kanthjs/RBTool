`GEOneStage_resid.default` <-
function (geOneResult, respvar, is.genoRandom = FALSE) 
{
    for (m in (1:length(respvar))) {
        if (is.genoRandom) {
            names.resid <- paste(respvar[m], "resid_random", 
                sep = "_")
        }
        else names.resid <- paste(respvar[m], "resid_fixed", 
            sep = "_")
        if (m == 1) {
            residuals <- as.data.frame(geOneResult$output[[m]]$residuals)
            if (nrow(residuals) > 0) {
                names(residuals) <- names.resid
                residuals <- cbind(geOneResult$output[[m]]$data, 
                  residuals)
            }
        }
        else {
            resid2 <- as.data.frame(geOneResult$output[[m]]$residuals)
            if (nrow(resid2) > 0) {
                if (nrow(residuals) == 0) {
                  residuals <- resid2
                  names(residuals) <- names.resid
                  residuals <- cbind(geOneResult$output[[m]]$data, 
                    residuals)
                }
                else {
                  names(resid2) <- names.resid
                  resid2 <- cbind(geOneResult$output[[m]]$data, 
                    resid2)
                  residuals <- merge(residuals, resid2, all = TRUE)
                }
            }
        }
    }
    if (nrow(residuals) == 0) {
        ge1residWarning <- "empty"
    }
    else {
        ge1residWarning <- "not empty"
    }
    return(list(residuals = residuals, ge1residWarning = ge1residWarning))
}
