`graph.mea2s.diagplots.default` <-
function (data, respvar, is.random = FALSE, result) 
{
    for (i in (1:length(respvar))) {
        xlabel = respvar[i]
        cc = rgb(0.3, 0.3, 0.7, 0.2)
        if (is.random) {
            filename1 = paste(getwd(), "/diagPlotsMea2s_", respvar[i], 
                "_random.png", sep = "")
        }
        else filename1 = paste(getwd(), "/diagPlotsMea2s_", respvar[i], 
            "_fixed.png", sep = "")
        residValues <- result[[i]]$residuals
        fittedValues <- result[[i]]$fitted.values
        if (!is.null(residValues) & (!is.null(fittedValues))) {
            png(filename = filename1)
            par(mfrow = c(2, 2))
            plot(result[[i]]$residuals ~ result[[i]]$fitted.values, 
                xlab = "Predicted Values", pch = 15, cex = 0.7, 
                col = cc, ylab = "Residuals", main = "Scatterplot of Residuals \nagainst Fitted Values")
            qqnorm(result[[i]]$residuals)
            qqline(result[[i]]$residuals, col = 2, main = title, 
                sub = xlabel)
            hist(result[[i]]$residuals, main = "Histogram of Residuals", 
                col = cc, xlab = "Residual", ylab = "Frequency")
            plot(seq(1:10) ~ seq(1:10), type = "n", boxed = FALSE, 
                axes = FALSE, xlab = "", ylab = "")
            if (is.random) {
                noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is random and response variable = ", 
                  respvar[i], ".", sep = "")
            }
            else {
                noteString <- paste("NOTE: Residuals plotted are taken from the model where genotype is fixed and response variable = ", 
                  respvar[i], ".", sep = "")
            }
            text(5, 7, paste(strwrap(noteString, width = 30), 
                sep = "", collapse = "\n"))
            dev.off()
        }
    }
}
