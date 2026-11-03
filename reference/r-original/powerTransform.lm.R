`powerTransform.lm` <-
function (object, ...) 
{
    mf <- if (is.null(object$model)) 
        update(object, model = TRUE, method = "model.frame")$model
    else object$model
    mt <- attr(mf, "terms")
    y <- model.response(mf, "numeric")
    w <- as.vector(model.weights(mf))
    if (is.null(w)) 
        w <- rep(1, dim(mf)[1])
    if (is.empty.model(mt)) {
        x <- matrix(rep(1, dim(mf)[1]), ncol = 1)
    }
    else {
        x <- model.matrix(mt, mf, contrasts)
    }
    estimateTransform(x, y, w, ...)
}
