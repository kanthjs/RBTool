`graph.sea.predint.default` <-
function (data, respvar, env, geno, result) 
{
    dir.create("plots")
    for (i in (1:length(respvar))) {
        for (j in (1:nlevels(data[, match(env, names(data))]))) {
            filename = paste(getwd(), "/plots/predIntSea_", respvar[i], 
                "_", result$output[[i]]$site[[j]]$env, ".png", 
                sep = "")
            png(filename)
            if (length(levels(result$output[[i]]$site[[j]]$data[, 
                match(geno, names(result$output[[i]]$site[[j]]$data))])) <= 
                50) {
                print(dotplot(ranef(result$output[[i]]$site[[j]]$model1, 
                  postVar = TRUE))[[1]])
            }
            else {
                (qqmath(ranef(result$output[[i]]$site[[j]]$model1, 
                  postVar = TRUE))[1])
            }
            dev.off()
        }
    }
}
