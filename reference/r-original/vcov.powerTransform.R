`vcov.powerTransform` <-
function (object, ...) 
{
    ans <- solve(object$hessian)
    rownames(ans) <- names(coef(object))
    colnames(ans) <- names(coef(object))
    ans
}
