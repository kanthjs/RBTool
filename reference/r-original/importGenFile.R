`importGenFile` <-
function (filename, extension = c("csv", "gtx", "str", "stru", 
    "dat", "gen"), population = NULL, individual = NULL, ploidyDegree = 2, 
    sep = NULL) 
UseMethod("importGenFile")
