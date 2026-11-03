`graph.mea2s.boxhist.default` <-
function (data, respvar, result, box = FALSE, hist = FALSE) 
{
    if (box == TRUE) {
        for (i in (1:length(respvar))) {
            boxfile = paste(getwd(), "/boxplotMea2S_", respvar[i], 
                ".png", sep = "")
            if (!all(is.na(data[, match(respvar[i], names(data))]))) {
                png(filename = boxfile)
                xlabel = respvar[i]
                boxplot((data[, match(respvar[i], names(data))]), 
                  data = data, xlab = xlabel, main = paste("Boxplot of ", 
                    respvar[i], sep = ""))
                dev.off()
            }
        }
    }
    if (hist == TRUE) {
        for (i in (1:length(respvar))) {
            if (!is.null(result[[i]]$data)) {
                histfile = paste(getwd(), "/histMea2S_", respvar[i], 
                  ".png", sep = "")
                png(filename = histfile)
                xlabel = respvar[i]
                hist(result[[i]]$data[, match(respvar[i], names(result[[i]]$data))], 
                  xlab = xlabel, main = paste("Histogram of ", 
                    respvar[i], sep = ""))
                dev.off()
            }
        }
    }
}
