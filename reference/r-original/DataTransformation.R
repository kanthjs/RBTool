`DataTransformation` <-
function (data, var, transformation = c("log", "ln", "sqrt", 
    "exp", "power", "standardize"), targetName = "newVar") 
UseMethod("DataTransformation")
