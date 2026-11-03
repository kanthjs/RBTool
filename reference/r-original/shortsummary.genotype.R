`shortsummary.genotype` <-
function (object, ..., maxsum) 
{
    tmp <- summary.factor(object, maxsum = maxsum)
    retval <- paste(format(tmp), " (", format(round(prop.table(tmp) * 
        100)), "%)", sep = "")
    names(retval) <- names(tmp)
    retval
}
