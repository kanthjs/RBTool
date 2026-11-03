`modelComparisonTable.default` <-
function (model1, model2) 
{
    # lme4 >= 1.0: summary(model)@AICtab no longer exists; AIC, BIC and logLik are taken from the fit
    fitstat <- function(m) c(AIC(m), BIC(m), as.numeric(logLik(m)))
    models.table <- as.data.frame(rbind(fitstat(model2), fitstat(model1)))
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
