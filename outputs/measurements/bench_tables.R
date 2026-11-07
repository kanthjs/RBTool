# Benchmark html_table / fmt_df: current vs vectorised versions; checks the HTML is identical
suppressMessages({ library(shiny); library(htmltools) })
setwd("D:/Github/RBTool/prototype/app")
src <- readLines("src/helpers.R")
eval(parse(text = src[grep("^fmt_p <-|^fmt_n <-", src)]))
eval(parse(text = src[18:31]))           # fmt_df (old)
eval(parse(text = src[35:49]))           # html_table (old)
fmt_df_old <- fmt_df; html_table_old <- html_table

fmt_num <- function(x) {
  out <- character(length(x)); ok <- !is.na(x)
  int <- ok & x == round(x) & abs(x) < 1e9; big <- ok & !int & abs(x) >= 100; rest <- ok & !int & !big
  out[int] <- formatC(x[int], format = "d", big.mark = ",")
  out[big] <- formatC(x[big], format = "f", digits = 2, big.mark = ",")
  out[rest] <- formatC(x[rest], format = "fg", digits = 4)
  out
}
fmt_df <- function(d, p_cols = c("P", "P (genotype)", "p (b1 = 1)", "p (s2di = 0)", "Pr(>F)", "LRT p-value")) {
  if (is.null(d)) return(NULL)
  d <- as.data.frame(d, check.names = FALSE)
  for (j in names(d)) {
    x <- d[[j]]
    if (is.factor(x)) d[[j]] <- as.character(x)
    else if (is.logical(x)) d[[j]] <- ifelse(is.na(x), "", ifelse(x, "yes", "no"))
    else if (is.numeric(x)) d[[j]] <- if (j %in% p_cols) fmt_p(x) else fmt_num(x)
  }
  d
}
html_table <- function(df, max_rows = 400) {
  if (is.null(df) || !nrow(df)) return(p(class = "text-muted", "(no rows)"))
  more <- nrow(df) > max_rows; df <- utils::head(df, max_rows)
  cols <- lapply(df, as.character)
  num <- vapply(cols, function(v) { v <- v[nzchar(v)]
    length(v) > 0 && all(grepl("^[-<]?[0-9][0-9,.]*[*]*$|^[-]?[0-9.]+e[-+][0-9]+$", v)) }, NA, USE.NAMES = FALSE)
  al <- ifelse(num, "text-align:right;", "text-align:left;")
  td <- lapply(seq_along(cols), function(j) paste0("<td style=\"", al[j], "\"", if (num[j]) " class=\"num\"", ">", htmlEscape(cols[[j]]), "</td>"))
  rows <- do.call(paste, c(td, sep = "\n"))
  div(class = "table-responsive",
    tags$table(class = "table table-condensed table-striped", style = "width:auto;",
      tags$thead(tags$tr(lapply(seq_along(df), function(j) tags$th(names(df)[j], style = al[j])))),
      tags$tbody(lapply(rows, function(r) tags$tr(HTML(r))))),
    if (more) p(class = "text-muted small", sprintf("Showing the first %d rows; the Excel file has all of them.", max_rows)))
}

set.seed(1); n <- 400
d <- data.frame(Genotype = factor(paste0("G", 1:n)), Env = sample(c("E1", "E<2>&"), n, TRUE), Mean = rnorm(n, 3000, 500), SE = runif(n, 0, 2),
                Reps = sample(1:4, n, TRUE), P = runif(n), Ok = sample(c(TRUE, FALSE, NA), n, TRUE), Small = rnorm(n), check.names = FALSE)
d$Mean[3] <- NA; d$SE[5] <- Inf; d$Small[7] <- 1e12; d$Small[8] <- -0.000123456
same_df <- identical(fmt_df_old(d), fmt_df(d))
h_old <- as.character(html_table_old(fmt_df_old(d))); h_new <- as.character(html_table(fmt_df(d)))
norm <- function(h) gsub("[[:space:]]+", "", h)
cat("fmt_df identical:", same_df, "\n")
cat("html_table identical apart from whitespace between tags:", identical(norm(h_old), norm(h_new)), "\n")
tm <- function(f, k) { invisible(f()); median(vapply(1:k, function(i) system.time(f())[["elapsed"]], 0)) * 1000 }
d15 <- d[1:15, ]
res <- data.frame(case = c("400 rows x 8 cols", "15 rows x 8 cols"),
  old_ms = c(tm(function() as.character(html_table_old(fmt_df_old(d))), 5), tm(function() as.character(html_table_old(fmt_df_old(d15))), 50)),
  new_ms = c(tm(function() as.character(html_table(fmt_df(d))), 5), tm(function() as.character(html_table(fmt_df(d15))), 50)))
res$speedup <- round(res$old_ms / res$new_ms, 1)
print(res)
