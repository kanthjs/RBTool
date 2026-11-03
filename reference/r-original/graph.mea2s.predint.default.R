`graph.mea2s.predint.default` <-
function (data, respvar, geno, result) 
{
    for (i in (1:length(respvar))) {
        filename = paste(getwd(), "/predIntMea2S_", respvar[i], 
            ".png", sep = "")
        png(filename = filename)
        if (length(levels(result[[i]]$data[, match(geno, names(result[[i]]$data))])) <= 
            50) {
            print(dotplot(ranef(result[[i]]$model, postVar = TRUE))[[1]])
        }
        else {
            (qqmath(ranef(result[[i]]$model, postVar = TRUE))[1])
        }
        dev.off()
    }
}
