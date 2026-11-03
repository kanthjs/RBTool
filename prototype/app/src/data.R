# Example data, the data step of each screen and the variable pickers.

# `pages`: screens the example is offered on; roles preset the variable pickers
examples <- list(
  rcb11 = list(label = "Rice yield: 15 genotypes x 11 sites, RCB (PBTools)", file = "rcb_11sites.csv", pages = c("sea", "met", "gxe"),
               design = "RCB", trait = "Yield", geno = "Genotype", env = "Env", block = "Block", rep = "Block"),
  rcb2 = list(label = "8 genotypes x 2 sites, RCB, two traits (PBTools)", file = "rcb_2sites.csv", pages = c("sea", "met"),
              design = "RCB", trait = c("Y1", "Y2"), geno = "Gen", env = "Site", block = "Blk"),
  rowcol = list(label = "Row-column: 25 genotypes, 2 reps, 2 sites (simulated)", file = "rowcol_2env.csv", pages = "met",
                design = "RowCol", trait = "Y", geno = "Geno", env = "Env", block = "Row", column = "Col", rep = "Rep"),
  stage1 = list(label = "Stage-1 means: 15 genotypes x 10 sites (PBTools)", file = "stage1_means.csv", pages = "met",
                trait = "Yield_Mean", geno = "Genotype", env = "Env", se = "Yield_StdErrMean", error_var = "Yield_sigma2", reps = "Yield_No.rep"))
examples_for <- function(page) { ex <- Filter(function(e) page %in% e$pages, examples); stats::setNames(names(ex), vapply(ex, `[[`, "", "label")) }

data_step <- function(p, page, n) side_step(n, "Data",
  radioButtons(paste0(p, "_src"), NULL, c("Built-in example" = "ex", "Upload file" = "up"), inline = TRUE),
  conditionalPanel(sprintf("input.%s_src == 'ex'", p), selectInput(paste0(p, "_example"), NULL, examples_for(page))),
  conditionalPanel(sprintf("input.%s_src == 'up'", p),
    fileInput(paste0(p, "_file"), "Data file (.csv, .txt, .xlsx)", accept = c(".csv", ".txt", ".tsv", ".xlsx", ".xls")),
    help_box("How should the file be laid out?",
      p("One row per plot, the first row holds the column names: a column for the genotype, the environment (site, season), the block or replicate (and row, column for row-column designs) and one column per measured trait."),
      p("Long layout: replicates are rows, not columns (not Rep 1 | Rep 2 | Rep 3)."))))

# reactive data of one screen: list(d = data frame, name, ex = example roles or NULL)
data_server <- function(input, p) reactive({
  if (identical(input[[paste0(p, "_src")]], "up")) {
    f <- input[[paste0(p, "_file")]]; req(f)
    d <- tryCatch(pb_read_table(f$datapath, f$name), error = function(e) NULL)
    validate(need(!is.null(d) && ncol(d) >= 2, "The file could not be read. Save it as .csv or .xlsx with the column names in the first row."))
    list(d = d, name = f$name, ex = NULL)
  } else {
    k <- input[[paste0(p, "_example")]] %||% names(examples)[1]; e <- examples[[k]]
    list(d = utils::read.csv(file.path("examples", e$file), check.names = FALSE), name = e$label, ex = e)
  }
})

guess_col <- function(cols, rx, avoid = character(), default = "") {
  m <- setdiff(cols[grepl(rx, cols, ignore.case = TRUE)], avoid)
  if (length(m)) m[1] else default
}
numeric_like <- function(d) names(d)[vapply(d, function(v) { x <- suppressWarnings(as.numeric(as.character(v))); mean(!is.na(x)) > 0.8 }, NA)]

# a select input for one role, preset from the example or guessed from the column names
role_select <- function(id, label, cols, preset, rx, avoid = character(), optional = FALSE, multiple = FALSE) {
  sel <- if (!is.null(preset)) preset else guess_col(cols, rx, avoid, if (optional) "" else cols[1])
  ch <- if (optional) c("(none)" = "", cols) else cols
  selectInput(id, label, ch, selected = sel, multiple = multiple)
}
