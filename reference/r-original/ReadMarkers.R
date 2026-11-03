`ReadMarkers` <-
function (markers = NULL) 
{
    markers <- read.csv(file = markers, na.strings = ".")
    nmark <- ncol(markers)
    colnames(markers) <- paste("M", 1:nmark, sep = "")
    recode <- function(i) {
        cod <- ifelse(markers[, i] == 0, -1, ifelse(markers[, 
            i] == 1, 0, ifelse(markers[, i] == 2, 1, NA)))
        cod[which(is.na(cod))] <- 0
        return(cod)
    }
    if (any(markers == 2)) 
        codes <- sapply(1:nmark, recode)
    else codes <- markers
    return(as.matrix(codes))
}
