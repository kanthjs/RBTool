`graph.mea1s.predint.default` <-
function (data, respVar, geno, result) 
{
    for (i in (1:length(respVar))) {
        filename = paste(getwd(), "/predIntMea1S_", respVar[i], 
            ".png", sep = "")
        png(filename = filename)
        if (length(levels(result$output[[i]]$data[, match(geno, 
            names(result$output[[i]]$data))])) <= 50) {
            print(dotplot(ranef(result$output[[i]]$model, postVar = TRUE))[[1]])
        }
        else {
            (qqmath(ranef(result$output[[i]]$model, postVar = TRUE))[1])
        }
        dev.off()
    }
}
