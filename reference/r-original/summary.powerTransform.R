`summary.powerTransform` <-
function (object, ...) 
{
    one <- 1 == length(object$lambda)
    label <- paste(object$family, (if (one) 
        "Transformation to Normality"
    else "Transformations to Multinormality"), "\n\n")
    lambda <- object$lambda
    stderr <- sqrt(diag(solve(object$hessian)))
    df <- length(lambda)
    result <- cbind(lambda, stderr, lambda - 1.96 * stderr, lambda + 
        1.96 * stderr)
    rownames(result) <- names(object$lambda)
    colnames(result) <- c("Est.Power", "Std.Err.", "Wald Lower Bound", 
        "Wald Upper Bound")
    tests <- testTransform(object, 0)
    tests <- rbind(tests, testTransform(object, 1))
    if (!(all(object$roundlam == 0) | all(object$roundlam == 
        1) | length(object$roundlam) == 1)) 
        tests <- rbind(tests, testTransform(object, object$roundlam))
    out <- list(label = label, result = result, tests = tests)
    class(out) <- "summary.powerTransform"
    out
}
