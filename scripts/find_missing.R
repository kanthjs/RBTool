# List functions referenced by reference/pbtools-port/R that exist in reference/r-original but are not yet there
eng <- sub("[.]R$", "", list.files("reference/pbtools-port/R")); orig <- sub("[.]R$", "", list.files("reference/r-original", "[.]R$"))
orig <- orig[!startsWith(orig, "_")]
e <- new.env(); for (f in list.files("reference/pbtools-port/R", full.names = TRUE)) sys.source(f, e)
used <- unique(unlist(lapply(ls(e), function(n) codetools::findGlobals(get(n, e), merge = TRUE))))
base <- c(ls(baseenv()), ls("package:stats")); miss <- setdiff(setdiff(intersect(used, orig), eng), base); cat(miss, sep = "\n")
