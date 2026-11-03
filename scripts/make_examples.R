# Write the example data files of the app (prototype/app/examples). Run from the repo root.
# scripts/check_examples.R checks that each file is a valid layout of its design.
out <- "prototype/app/examples"; dir.create(out, showWarnings = FALSE, recursive = TRUE)
w <- function(d, f) utils::write.csv(d, file.path(out, f), row.names = FALSE)

# PBTools example data (IRRI): 15 genotypes x 11 sites, RCB with 3 blocks; 8 genotypes x 2 sites, two traits;
# stage-1 means of 15 genotypes x 10 sites
w(read.csv("reference/data/RCB_ME.csv"), "rcb_11sites.csv")
w(read.csv("reference/pbtools-port/inst/extdata/RCB_Multi.csv"), "rcb_2sites.csv")
w(read.csv("reference/pbtools-port/inst/extdata/Two_stage.csv"), "stage1_means.csv")

# Row-column design, simulated: 25 genotypes in a 5 x 5 layout, 2 replicates, 2 sites. Replicate 1 places genotype
# (a, b) at row a, column b; replicate 2 at the cell with r + c = a and r + 2c = b (mod 5), two orthogonal latin
# squares, so every row and every column of replicate 2 meets each row and column of replicate 1 once.
set.seed(20261103)
cells <- expand.grid(Col = 0:4, Row = 0:4)
g_rep1 <- with(cells, 5 * Row + Col + 1)
g_rep2 <- with(cells, 5 * ((Row + Col) %% 5) + (Row + 2 * Col) %% 5 + 1)
gen_eff <- rnorm(25, 0, 4)
rc <- do.call(rbind, lapply(c("E1", "E2"), function(e) do.call(rbind, lapply(1:2, function(r) {
  g <- if (r == 1) g_rep1 else g_rep2
  data.frame(Env = e, Rep = r, Row = cells$Row + 1, Col = cells$Col + 1, Geno = sprintf("G%02d", g),
             Y = round(60 + (e == "E2") * 6 + gen_eff[g] * (1 + 0.3 * (e == "E2")) + rnorm(5, 0, 2)[cells$Row + 1] +
                       rnorm(5, 0, 1.5)[cells$Col + 1] + rnorm(25, 0, 2), 2)) }))))
w(rc[order(rc$Env, rc$Rep, rc$Row, rc$Col), ], "rowcol_2env.csv")
cat("examples written to", out, "\n")
