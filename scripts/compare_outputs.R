# Compare two dumps token by token: numbers within rel 1e-3 or abs 5e-4 of the largest number in the file, other tokens equal.
compare_dump <- function(a, b, rel = 3e-3, abs_scale = 5e-4) {
  ta <- unlist(strsplit(readLines(a), "[[:space:]]+")); tb <- unlist(strsplit(readLines(b), "[[:space:]]+"))
  ta <- ta[nzchar(ta)]; tb <- tb[nzchar(tb)]
  if (length(ta) != length(tb)) return(sprintf("token count %d vs %d", length(ta), length(tb)))
  nums <- suppressWarnings(as.numeric(ta)); abs <- abs_scale * max(abs(nums), na.rm = TRUE)
  bad <- character()
  for (i in seq_along(ta)) {
    x <- suppressWarnings(as.numeric(ta[i])); y <- suppressWarnings(as.numeric(tb[i]))
    ok <- if (!is.na(x) && !is.na(y)) abs(x - y) <= max(abs, rel * max(abs(x), abs(y))) else identical(ta[i], tb[i])
    if (!ok) bad <- c(bad, sprintf("#%d %s vs %s", i, ta[i], tb[i]))
  }
  if (length(bad)) bad else character()
}
args <- commandArgs(TRUE)
if (length(args) == 2) for (f in list.files(args[1], "[.]txt$")) {
  r <- compare_dump(file.path(args[1], f), file.path(args[2], f))
  cat(sprintf("%-28s %s\n", f, if (length(r)) paste(c("DIFF", head(r, 6)), collapse = " | ") else "OK"))
}
