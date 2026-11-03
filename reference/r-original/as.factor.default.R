`as.factor.default` <-
function (x, ...) 
if (is.factor(x)) x else factor(x)
