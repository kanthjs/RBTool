`graph.sea.diagplots.default` <-
function (data, respvar, env, is.random = FALSE, result) 
{
    for (i in (1:length(respvar))) {
        nlevelsEnv <- 0
        if (!is.null(env)) {
            nlevelsEnv <- nlevels(factor(data[, match(env, names(data))]))
        }
        else {
            nlevelsEnv <- 1
        }
        for (j in (1:nlevelsEnv)) {
            siteLabel = result$output[[i]]$site[[j]]$env[[1]]
            xlabel = respvar[i]
            cc = rgb(0.3, 0.3, 0.7, 0.2)
            if (is.random) {
                filename1 = paste(getwd(), "/diagPlotsSEA_", 
                  respvar[i], "_", siteLabel, "_random.png", 
                  sep = "")
            }
            else filename1 = paste(getwd(), "/diagPlotsSEA_", 
                respvar[i], "_", siteLabel, "_fixed.png", sep = "")
            residValues <- result$output[[i]]$site[[j]]$residuals
            fittedValues <- result$output[[i]]$site[[j]]$fitted.values
            if (!is.null(residValues) & (!is.null(fittedValues))) {
                png(filename1)
                par(mfrow = c(2, 2))
                plot(residValues ~ fittedValues, xlab = "Predicted Values", 
                  pch = 15, cex = 0.7, col = cc, ylab = "Residuals", 
                  main = "Scatterplot of Residuals \nagainst Fitted Values")
                qqnorm(residValues)
                qqline(residValues, col = 2, main = title, sub = xlabel)
                hist(residValues, main = "Histogram of Residuals", 
                  col = cc, xlab = "Residual", ylab = "Frequency")
                plot(seq(1:10) ~ seq(1:10), type = "n", boxed = FALSE, 
                  axes = FALSE, xlab = "", ylab = "")
                if (is.random) {
                  if (is.null(env)) {
                    noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is random and response variable = ", 
                      respvar[i], ".", sep = "")
                  }
                  else {
                    noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is random, response variable = ", 
                      respvar[i], " and ", env, " = ", siteLabel, 
                      ".", sep = "")
                  }
                }
                else {
                  if (is.null(env)) {
                    noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is fixed and response variable = ", 
                      respvar[i], ".", sep = "")
                  }
                  else {
                    noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is fixed, response variable = ", 
                      respvar[i], " and ", env, " = ", siteLabel, 
                      ".", sep = "")
                  }
                }
                text(5, 7, paste(strwrap(noteString, width = 30), 
                  sep = "", collapse = "\n"))
                dev.off()
            }
        }
    }
}
