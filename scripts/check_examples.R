# Check that each example data set of the app is a valid layout of the design it is offered for.
# Run from the repo root: Rscript scripts/check_examples.R
ex <- "prototype/app/examples"
rd <- function(f) read.csv(file.path(ex, f))
bad <- 0
ok <- function(cond, msg) { cat(if (isTRUE(cond)) "ok   " else "FAIL ", msg, "\n"); if (!isTRUE(cond)) bad <<- bad + 1 }
once <- function(d, g, by) all(table(d[[g]], interaction(d[by], drop = TRUE)) == 1)

d <- rd("rcb_11sites.csv"); ok(once(d, "Genotype", c("Env", "Block")), "RCB 11 sites: every genotype once in every block of every site")
d <- rd("rcb_2sites.csv"); ok(once(d, "Gen", c("Site", "Blk")), "RCB 2 sites: every genotype once in every block")
d <- rd("rowcol_2env.csv")
ok(once(d, "Geno", c("Env", "Rep")), "Row-column: every genotype once per replicate in each site")
ok(all(table(d$Env, d$Rep, d$Row, d$Col) == 1), "Row-column: one plot per row x column cell")
e1 <- d[d$Env == "E1", ]; r1 <- e1[e1$Rep == 1, ]; r2 <- e1[e1$Rep == 2, ]
meet <- function(by1, by2) all(table(r1[[by1]][match(r2$Geno, r1$Geno)], r2[[by2]]) == 1)
ok(meet("Row", "Row") && meet("Col", "Row") && meet("Row", "Col") && meet("Col", "Col"), "Row-column: rows and columns of replicate 2 cross those of replicate 1 once")
if (bad) quit(status = 1)
