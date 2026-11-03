`powerTransform.formula` <-
function (object, data, subset, weights, na.action, ...) 
{
    mf <- match.call(expand.dots = FALSE)
    m <- match(c("object", "data", "subset", "weights", "na.action"), 
        names(mf), 0L)
    mf <- mf[c(1L, m)]
    mf$drop.unused.levels <- TRUE
    mf[[1L]] <- as.name("model.frame")
    names(mf)[which(names(mf) == "object")] <- "formula"
    mf <- eval(mf, parent.frame())
    mt <- attr(mf, "terms")
    y <- model.response(mf, "numeric")
    w <- as.vector(model.weights(mf))
    if (is.null(w)) 
        w <- rep(1, dim(mf)[1])
    if (is.empty.model(mt)) {
        x <- matrix(rep(1, dim(mf)[1]), ncol = 1)
    }
    else {
        x <- model.matrix(mt, mf)
    }
    estimateTransform(x, y, w, ...)
}
