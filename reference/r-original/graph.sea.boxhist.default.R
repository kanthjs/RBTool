`graph.sea.boxhist.default` <-
function (data, respvar, env, result, box = FALSE, hist = FALSE) 
{
    if (is.null(env)) {
        env = "EnvLevel"
        data <- cbind(data, EnvLevel = 1)
    }
    if (box == TRUE) {
        for (i in (1:length(respvar))) {
            boxfile = paste(getwd(), "/boxplotSEA_", respvar[i], 
                ".png", sep = "")
            if (!all(is.na(data[, match(respvar[i], names(data))]))) {
                png(boxfile)
                xlabel = respvar[i]
                boxplot(data[, match(respvar[i], names(data))] ~ 
                  data[, match(env, names(data))], data = data, 
                  xlab = xlabel, main = paste("Boxplot of ", 
                    respvar[i], sep = ""))
                dev.off()
            }
        }
    }
    if (hist == TRUE) {
        for (i in (1:length(respvar))) {
            for (j in (1:nlevels(factor(data[, match(env, names(data))])))) {
                if (!is.null(result$output[[i]]$site[[j]]$data)) {
                  siteLabel = result$output[[i]]$site[[j]]$env[[1]]
                  histfile = paste(getwd(), "/histSEA_", respvar[i], 
                    "_", siteLabel, ".png", sep = "")
                  png(histfile)
                  xlabel = respvar[i]
                  hist(result$output[[i]]$site[[j]]$data[, match(respvar[i], 
                    names(result$output[[i]]$site[[j]]$data))], 
                    xlab = xlabel, main = paste("Histogram of ", 
                      respvar[i], " for ", env, " = ", siteLabel, 
                      sep = ""))
                  dev.off()
                }
            }
        }
    }
}
