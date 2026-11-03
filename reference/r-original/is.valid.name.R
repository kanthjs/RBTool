`is.valid.name` <-
function (x) 
{
    length(x) == 1 && is.character(x) && x == make.names(x)
}
