`adjustAnovaDf.default` <-
function (anova.table, newDfColumn) 
{
    if (attr(anova.table, "class")[[1]] == "summary.aov") {
        df <- as.data.frame(anova.table[[1]])
    }
    if (attr(anova.table, "class")[[1]] == "anova") {
        df <- as.data.frame(anova.table)
    }
    if (attr(anova.table, "class")[[1]] == "data.frame") {
        df <- anova.table
    }
    colnames(df) <- c("Df", "Sum Sq", "Mean Sq", "F value", "Pr(>F)")
    rownames(df) <- trimStrings(rownames(df))
    df["Residuals", "Df"] <- newDfColumn[length(newDfColumn)]
    df[, "Mean Sq"] <- df[, "Sum Sq"]/df[, "Df"]
    df[, "F value"] <- df[, "Mean Sq"]/df["Residuals", "Mean Sq"]
    df[, "Pr(>F)"] <- pf(df[, "F value"], df[, "Df"], df["Residuals", 
        "Df"], lower.tail = F)
    df["Residuals", "F value"] <- NA
    df["Residuals", "Pr(>F)"] <- NA
    return(df)
}
