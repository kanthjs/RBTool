# Copy the engine into the Shiny app so that Shinylive can bundle it, and write the version file.
# Usage (from the repo root): Rscript scripts/build_app.R
dest <- file.path("prototype", "app", "engine")
unlink(dest, recursive = TRUE); dir.create(dest, recursive = TRUE)
src <- list.files(file.path("engine", "R"), pattern = "[.]R$", full.names = TRUE)
stopifnot(length(src) > 0, all(file.copy(src, dest)))
commit <- tryCatch(system2("git", c("rev-parse", "--short", "HEAD"), stdout = TRUE, stderr = FALSE), error = function(e) "unknown")
if (!length(commit)) commit <- "unknown"
writeLines(c(paste0("version=", Sys.getenv("PBTOOL_APP_VERSION", "0.1.0")), paste0("commit=", commit[1]), paste0("built=", format(Sys.Date()))),
           file.path("prototype", "app", "VERSION"))
cat("copied", length(src), "engine files to", dest, "\n")
