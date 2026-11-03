# Write reference/pbtools-port/NAMESPACE: export every function, register S3 methods of generics (bodies calling UseMethod)
e <- new.env(); for (f in list.files("reference/pbtools-port/R", full.names = TRUE)) sys.source(f, e)
nm <- sort(ls(e, all.names = TRUE)); fn <- nm[vapply(nm, function(n) is.function(get(n, e)), logical(1))]
generics <- fn[vapply(fn, function(n) any(grepl("UseMethod", deparse(get(n, e)))), logical(1))]
s3 <- character()
for (g in generics) for (cl in c("default", "data.frame", "formula")) if (paste0(g, ".", cl) %in% fn) s3 <- c(s3, sprintf("S3method(%s, %s)", g, cl))
writeLines(c(paste0("export(", paste0("`", fn, "`", collapse = ", "), ")"), s3,
  "import(stats, utils, graphics, grDevices)", "importFrom(lme4, lmer, VarCorr)"), "reference/pbtools-port/NAMESPACE")
cat(length(fn), "functions;", length(s3), "S3 methods\n")
