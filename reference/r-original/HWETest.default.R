`HWETest.default` <-
function (genindObject, display = c("full", "matrix")) 
{
    library(adegenet)
    if (display == "matrix") {
        result1 <- HWE.test.genind(genindObject, res.type = "matrix")
        summaryTable <- format(round(as.data.frame(result1), 
            4), digits = 4, nsmall = 4, scientific = FALSE)
    }
    if (display == "full") {
        result2 <- HWE.test.genind(genindObject, res.type = "full")
        summaryTable <- NULL
        for (i in 1:length(result2)) {
            locusList <- eval(parse(text = paste("result2$", 
                names(result2)[i], sep = "")))
            for (j in 1:length(locusList)) {
                popnList <- eval(parse(text = paste("locusList$", 
                  names(locusList)[j], sep = "")))
                newRow <- data.frame(Locus = names(result2)[i], 
                  Population = names(locusList)[j], Chi_value = popnList$statistic, 
                  df = popnList$parameter, p_value = popnList$p.value)
                summaryTable <- rbind(summaryTable, newRow)
            }
        }
        rownames(summaryTable) <- NULL
        colnames(summaryTable) <- c("Locus", "Population", "Chisq Value", 
            "df", "Pr(>Chisq)")
        summaryTable[, "Chisq Value"] <- formatC(as.numeric(format(summaryTable[, 
            "Chisq Value"], scientific = FALSE)), format = "f")
        summaryTable[, "Pr(>Chisq)"] <- formatC(as.numeric(format(summaryTable[, 
            "Pr(>Chisq)"], scientific = FALSE)), format = "f")
    }
    return(summaryTable)
}
