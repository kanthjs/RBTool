`MergeData.default` <-
function (MasterData, TransactionData, byMaster, byTransact, 
    MasterVarInclude = names(MasterData), TransactVarInclude = names(TransactionData), 
    allMaster = FALSE, allTransact = FALSE) 
{
    if (is.character(MasterData)) {
        MasterData <- eval(parse(text = MasterData))
    }
    if (is.character(TransactionData)) {
        TransactionData <- eval(parse(text = TransactionData))
    }
    MasterData <- MasterData[unique(c(byMaster, MasterVarInclude))]
    TransactionData <- TransactionData[unique(c(byTransact, TransactVarInclude))]
    mergeData <- merge(MasterData, TransactionData, by.x = byMaster, 
        by.y = byTransact, all.x = allMaster, all.y = allTransact, 
        suffixes = c(".1", ".2"))
    remove(list = c("MasterData", "TransactionData"))
    return(mergeData)
}
