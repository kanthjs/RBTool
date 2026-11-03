`MergeData` <-
function (MasterData, TransactionData, byMaster, byTransact, 
    MasterVarInclude = names(MasterData), TransactVarInclude = names(TransactionData), 
    allMaster = FALSE, allTransact = FALSE) 
UseMethod("MergeData")
