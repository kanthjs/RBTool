`modelComparisonTable.default` <-
function (model1, model2) 
{
    models.table = NULL
    models.table <- rbind(summary(model2)@AICtab[, 1:3], summary(model1)@AICtab[1:3])
    rownames(models.table) <- c("Model2", "Model1")
    Chisq <- c("", round(2 * abs(models.table[1, 3] - models.table[2, 
        3]), digits = 4))
    Df <- c("", 1)
    probValue <- pchisq(2 * abs(models.table[1, 3] - models.table[2, 
        3]), 1, lower.tail = FALSE)
    pvalue <- c("", formatC(as.numeric(format(probValue, scientific = FALSE)), 
        format = "f"))
    models.table <- cbind(models.table, Chisq, Df, pvalue)
    colnames(models.table) <- c("AIC", "BIC", "logLik", "Chisq", 
        "Df", "Pr(>Chisq)")
    return(models.table)
}
