`GETwoStage.dataRecode.default` <-
function (data, respvar, geno, env) 
{
    respvar <- trimStrings(respvar)
    geno <- trimStrings(geno)
    env <- trimStrings(env)
    if (is.na(match(respvar, names(data))) || is.na(match(geno, 
        names(data))) || is.na(match(env, names(data)))) {
        stop("At least one variable name does not match a column in the data frame.")
    }
    result <- list()
    for (i in (1:length(respvar))) {
        temp.data <- subset(data, subset = (is.na(data[, match(respvar[i], 
            names(data))]) == F))
        result[[i]] <- list()
        result[[i]]$respvar <- respvar[i]
        obsread <- nrow(data)
        obsused <- nrow(temp.data)
        responseRate <- obsused/obsread
        result[[i]]$obsread <- obsread
        result[[i]]$obsused <- obsused
        result[[i]]$responseRate <- responseRate
        if (responseRate < 0.8) {
            result[[i]]$manyNAWarning <- "Too many missing observations. Cannot proceed with the analysis."
            next
        }
        else {
            temp.data[, match(geno, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(geno, names(temp.data))]))
            temp.data[, match(env, names(temp.data))] <- factor(trimStrings(temp.data[, 
                match(env, names(temp.data))]))
            levelsGeno <- levels(temp.data[, match(geno, names(temp.data))])
            levelsEnv <- levels(temp.data[, match(env, names(temp.data))])
            result[[i]]$nlevelsGeno <- length(levelsGeno)
            result[[i]]$nlevelsEnv <- length(levelsEnv)
            if (max(nchar(levelsGeno)) > 4 || max(nchar(levelsEnv)) > 
                4) {
                newCodingGeno <- data.frame(Genotype = levelsGeno, 
                  Code = paste("G", seq(1:length(levelsGeno)), 
                    sep = ""))
                newCodingEnv <- data.frame(Environment = levelsEnv, 
                  Code = paste("E", seq(1:length(levelsEnv)), 
                    sep = ""))
                result[[i]]$newCodingGeno <- newCodingGeno
                result[[i]]$newCodingEnv <- newCodingEnv
                recodedLevels <- TRUE
                result[[i]]$recodedLevels <- recodedLevels
                temp.data$CodedGeno <- newCodingGeno$Code[match(temp.data[, 
                  geno], newCodingGeno$Genotype)]
                temp.data$CodedEnv <- newCodingEnv$Code[match(temp.data[, 
                  env], newCodingEnv$Environment)]
            }
            else {
                temp.data$CodedGeno <- temp.data[, match(geno, 
                  names(temp.data))]
                temp.data$CodedEnv <- temp.data[, match(env, 
                  names(temp.data))]
                recodedLevels <- FALSE
                result[[i]]$recodedLevels <- recodedLevels
            }
            result[[i]]$data <- temp.data
        }
    }
    return(result)
}
