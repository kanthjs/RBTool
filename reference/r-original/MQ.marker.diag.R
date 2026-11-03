`MQ.marker.diag` <-
function (crossobj, QTL.path, estmarker = FALSE, thresholdMQ = 0.1, 
    quantile = FALSE, p.val = 0.01, na.cutoff = 0.1) 
{
    library(qtl)
    MQ.cf.file <- paste(QTL.path, "MQ_cfplot", sep = "")
    MQ.markermap.file <- paste(QTL.path, "MQ_markermapplot", 
        sep = "")
    MQ.estmarkermap.file <- paste(QTL.path, "MQ_estmarkermapplot", 
        sep = "")
    MQ.genotype.file <- paste(QTL.path, "MQ_genotypeplot", sep = "")
    MQ.missinggenotype.file <- paste(QTL.path, "MQ_missingegnotypeplot", 
        sep = "")
    MQ.comparegenotypes.file <- paste(QTL.path, "MQ_comparegenotypesplot", 
        sep = "")
    MQ.identical.genotypes <- paste(QTL.path, "MQ_identical.genotypes.plot", 
        sep = "")
    MQ.genotypic.distortion <- paste(QTL.path, "MQ_genotypic.distortion.plot", 
        sep = "")
    MQ.summary.markers.file <- paste(QTL.path, "MQ_summary.markers.report", 
        sep = "")
    MQ.problems.markers.file <- paste(QTL.path, "MQ_problems.markers.report", 
        sep = "")
    MQ.markermap.plot <- function(crossobj, out = MQ.markermap.file) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        plot.map(crossobj)
        dev.off()
    }
    MQ.estmarkermap.plot <- function(crossobj, out = MQ.estmarkermap.file) {
        newmap <- est.map(crossobj)
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        plot.map(newmap, crossobj)
        dev.off()
    }
    MQ.genotype.plot <- function(crossobj, out = MQ.genotype.file) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        geno.image(crossobj)
        dev.off()
    }
    MQ.missinggenotype.plot <- function(crossobj, out = MQ.missinggenotype.file) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        plot.missing(crossobj)
        dev.off()
    }
    MQ.comparegenotypes.plot <- function(crossobj, out = MQ.comparegenotypes.file) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        output <- comparegeno(crossobj)
        n.ind <- nind(crossobj)
        image(1:n.ind, 1:n.ind, output, col = gray((0:99)/99), 
            breaks = seq(0, 1, len = 101))
        dev.off()
    }
    MQ.cf.plot <- function(crossobj, out = MQ.cf.file) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        plot.rf(crossobj, alternate.chrid = FALSE)
        dev.off()
    }
    MQ.summary.markers <- function(crossobj, p.val = 0.01, na.cutoff = 0.1, 
        out = MQ.genotypic.distortion) {
        names.lg <- names(pull.map(crossobj))
        num.lg <- length(names.lg)
        a <- t(t(unlist(pull.map(crossobj))))
        library(stringr)
        row.names(a) <- str_sub(row.names(a), 3)
        ch <- NULL
        for (i in 1:num.lg) {
            ch <- rbind(ch, t(t(rep(i, summary.map(crossobj)[i, 
                1]))))
        }
        b <- cbind(ch, a)
        n.missing <- nmissing(crossobj, what = "mar")[(nmissing(crossobj, 
            what = "mar"))/sum(summary(crossobj)$n.ind) > na.cutoff]
        p.missing <- ((nmissing(crossobj, what = "mar"))/sum(summary(crossobj)$n.ind))[(nmissing(crossobj, 
            what = "mar"))/sum(summary(crossobj)$n.ind) > na.cutoff]
        if (length(n.missing) == 0) {
            n.missing <- NA
            names(n.missing) <- "NA"
        }
        if (length(p.missing) == 0) {
            p.missing <- NA
            names(p.missing) <- "NA"
        }
        Chr <- NA
        Pos <- NA
        for (i in 1:dim(b)[1]) {
            for (j in 1:length(n.missing)) {
                if (row.names(b)[i] == names(n.missing)[j]) {
                  c <- b[i, 1]
                  p <- b[i, 2]
                }
                else {
                  c <- NULL
                  p <- NULL
                }
                Chr <- rbind(Chr, c)
                Pos <- rbind(Pos, p)
            }
        }
        missing.markers <- data.frame(Chr, Pos, n.missing, p.missing, 
            check.names = FALSE, row.names = names(n.missing))
        names(missing.markers) <- c("Chr.", "Pos.", "Num. missing", 
            "Frec. missing")
        n.missing.i <- nmissing(crossobj, what = "ind")[(nmissing(crossobj, 
            what = "ind"))/sum(summary(crossobj)$n.mar) > na.cutoff]
        p.missing.i <- ((nmissing(crossobj, what = "ind"))/sum(summary(crossobj)$n.mar))[(nmissing(crossobj, 
            what = "ind"))/sum(summary(crossobj)$n.mar) > na.cutoff]
        if (length(n.missing.i) == 0) {
            n.missing.i <- NA
            names(n.missing.i) <- "NA"
        }
        if (length(p.missing.i) == 0) {
            p.missing.i <- NA
            names(p.missing.i) <- "NA"
        }
        missing.individuals <- data.frame(n.missing.i, p.missing.i)
        names(missing.individuals) <- c("Num. missing", "Frec. missing")
        gt <- geno.table(crossobj)
        segregation.distortion <- gt[gt$P.value < p.val, ]
        gt2 <- data.frame(gt, ch, a, -log10(gt$P.value))
        png(file = paste(out, ".png", sep = ""))
        if (nchr(crossobj) < 4) 
            par(mfrow = c(1, nchr(crossobj)))
        if (nchr(crossobj) > 4 & nchr(crossobj) < 8) 
            par(mfrow = c(2, ((round(nchr(crossobj))/2) + 1)))
        if (nchr(crossobj) > 8) 
            par(mfrow = c(3, ((round(nchr(crossobj))/3) + 1)))
        for (i in 1:nchr(crossobj)) {
            c <- gt2[, 7][gt2[, 1] == i]
            d <- gt2[, 8][gt2[, 1] == i]
            e <- cbind(c, d)
            plot(e[, 1], e[, 2], type = "h", xlab = "position", 
                ylab = "-logP", main = paste("Chr", i), ylim = c(0, 
                  max(gt2[, 8])))
        }
        dev.off()
        out <- NULL
        out$missing.markers <- missing.markers
        out$missing.individuals <- missing.individuals
        out$segregation.distortion <- segregation.distortion
        filename <- MQ.summary.markers.file
        write("", file = filename)
        write("-----------------------------------------------", 
            append = TRUE, file = filename)
        sink(file = filename, append = TRUE)
        print(out)
        sink()
    }
    MQ.list.problems <- function(crossobj, thresholdMQ = FALSE, 
        quant = FALSE, out = MQ.identical.genotypes) {
        png(file = paste(out, ".png", sep = ""))
        par(mfrow = c(1, 1))
        cg <- comparegeno(crossobj)
        hist(cg, breaks = 200, xlab = "Proportion of shared alleles", 
            col = "red", main = "Identical genotypes")
        rug(cg, col = "blue")
        dev.off()
        if (quant != "FALSE") {
            a <- which(cg > quantile(cg, (1 - quant), na.rm = TRUE), 
                arr.ind = TRUE)
            c <- NULL
            if (length(a) > 2) {
                for (i in 1:dim(a)[1]) {
                  b <- cg[a[i, 1], a[i, 2]]
                  c <- rbind(c, b)
                }
                identical.genotypes <- cbind(a, c)
            }
            if (length(a) == 0) {
                identical.genotypes <- "No unusually identical genotypes"
            }
            a <- which(cg < quantile(cg, (quant), na.rm = TRUE), 
                arr.ind = TRUE)
            c <- NULL
            if (length(a) > 2) {
                for (i in 1:dim(a)[1]) {
                  b <- cg[a[i, 1], a[i, 2]]
                  c <- rbind(c, b)
                }
                different.genotypes <- cbind(a, c)
            }
            if (length(a) == 0) {
                different.genotypes <- "No unusually different genotypes"
            }
        }
        if (thresholdMQ != "FALSE") {
            a <- which(cg > (1 - thresholdMQ), arr.ind = TRUE)
            c <- NULL
            if (length(a) > 2) {
                for (i in 1:dim(a)[1]) {
                  b <- cg[a[i, 1], a[i, 2]]
                  c <- rbind(c, b)
                }
                identical.genotypes <- cbind(a, c)
            }
            if (length(a) == 0) {
                identical.genotypes <- "No unusually identical genotypes"
            }
            a <- which(cg < thresholdMQ, arr.ind = TRUE)
            c <- NULL
            if (length(a) > 2) {
                for (i in 1:dim(a)[1]) {
                  b <- cg[a[i, 1], a[i, 2]]
                  c <- rbind(c, b)
                }
                different.genotypes <- cbind(a, c)
            }
            if (length(a) == 0) {
                different.genotypes <- "No unusually different genotypes"
            }
        }
        crossobj <- est.rf(crossobj)
        marker.order <- checkAlleles(crossobj)
        out <- NULL
        out$identical.genotypes <- identical.genotypes
        out$different.genotypes <- different.genotypes
        out$marker.order <- marker.order
        filename <- MQ.problems.markers.file
        write("", file = filename)
        write("-----------------------------------------------", 
            append = TRUE, file = filename)
        sink(file = filename, append = TRUE)
        print(out)
        sink()
    }
    MQ.markermap.plot(cross.data)
    if (estmarker == TRUE) {
        MQ.estmarkermap.plot(cross.data)
    }
    MQ.genotype.plot(cross.data)
    MQ.missinggenotype.plot(cross.data)
    MQ.comparegenotypes.plot(cross.data)
    suppressWarnings(MQ.cf.plot(cross.data))
    MQ.list.problems(cross.data, thresholdMQ = thresholdMQ)
    MQ.summary.markers(cross.data, p.val = p.val, na.cutoff = na.cutoff)
    detach("package:qtl")
    detach("package:stringr")
}
