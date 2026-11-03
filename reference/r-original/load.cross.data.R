`load.cross.data` <-
function (P.data, G.data, map.data, cross, heterozygotes = TRUE, 
    genotype, env.label = NULL, env = NULL, ST = NULL) 
{
    library(qtl)
    G.data <- read.table(G.data, header = TRUE, as.is = TRUE, 
        check.names = FALSE)
    map.data <- read.table(map.data, header = FALSE, as.is = TRUE)
    if ((is.null(env.label) == FALSE) & (is.null(env) == FALSE)) {
        P.data <- P.data[which(P.data[match(env.label, names(P.data))] == 
            env), ]
    }
    if (cross != "am") {
        G.data <- G.data[3:nrow(G.data), ]
    }
    P.data <- P.data[match(rownames(G.data), as.character(P.data[[match(genotype, 
        names(P.data))]])), ]
    a <- matrix(rep("", (ncol(P.data) * 2)), 2, ncol(P.data))
    colnames(a) <- colnames(P.data)
    names(a) <- names(P.data)
    b <- rbind(G.data[, 0], t(map.data[, 2:3]))
    names(b) <- as.character(map.data[, 1])
    c <- data.frame(a, b, check.names = FALSE, stringsAsFactors = FALSE)
    rownames(c) <- c(rownames(a), rownames(b))
    if (cross != "am") {
        G.id <- row.names(G.data)
        P.id <- as.character(P.data[[match(genotype, names(P.data))]])
        if (sum(G.id != P.id) > 0) 
            simpleError("IDs don't match")
        geno.data <- G.data
        geno.data[geno.data == "1"] <- "AA"
        geno.data[geno.data == "1/1"] <- "AA"
        geno.data[geno.data == "2"] <- "BB"
        geno.data[geno.data == "2/2"] <- "BB"
        geno.data[geno.data == "1/2"] <- "AB"
        geno.data[geno.data == "1/-"] <- "C"
        geno.data[geno.data == "2/-"] <- "D"
        geno.data[geno.data == "-"] <- NA
        e <- cbind(P.data, geno.data[1:nrow(geno.data), ])
        f <- rbind(c, e)
        names(f)[1] = "id"
        zz <- file("cross.data.file.csv", "w")
        write.csv(f, zz, quote = FALSE, row.names = FALSE)
        close(zz)
    }
    if (cross == "am") {
        G.id <- row.names(G.data)
        P.id <- as.character(P.data[[match(genotype, names(P.data))]])
        if (sum(G.id != P.id) > 0) 
            simpleError("IDs don't match")
        geno.data <- G.data
        geno.data[geno.data == "1"] <- "BB"
        geno.data[geno.data == "0"] <- "AA"
        geno.data[geno.data == "0.5"] <- "AB"
        geno.data[geno.data == "-"] <- NA
        d <- rbind(G.data[0, ], geno.data)
        names(d) <- names(G.data)
        e <- cbind(P.data, d)
        for (i in 2:dim(P.data)[2]) {
            e[, i] <- as.factor(e[, i])
        }
        f <- rbind(c, e)
        names(f)[1] = "id"
        zz <- file("cross.data.file.csv", "w")
        write.csv(f, zz, quote = FALSE, row.names = FALSE)
        close(zz)
    }
    if (cross == "am" & heterozygotes == "FALSE") 
        cross <- "dh"
    if (cross == "am" & heterozygotes == "TRUE") 
        cross <- "f2"
    if (cross == "dh") {
        data.prueba <- read.cross("csv", file = "cross.data.file.csv", 
            genotypes = c("AA", "BB"))
    }
    if (cross == "bc") {
        data.prueba <- read.cross("csv", file = "cross.data.file.csv", 
            genotypes = c("AA", "AB"))
    }
    if (cross == "f2") {
        data.prueba <- read.cross("csv", file = "cross.data.file.csv", 
            genotypes = c("AA", "AB", "BB", "C", "D"))
    }
    if (cross == "riself" | cross == "ri4self" | cross == "ri8self") {
        data.prueba <- read.cross("csv", file = "cross.data.file.csv", 
            genotypes = c("AA", "BB", "C", "D"))
    }
    if (cross == "risib" | cross == "ri4sib" | cross == "ri8sib") {
        data.prueba <- read.cross("csv", file = "cross.data.file.csv", 
            genotypes = c("AA", "BB", "C", "D"))
    }
    class(data.prueba)[1] = paste(cross)
    detach("package:qtl")
    return(data.prueba)
}
