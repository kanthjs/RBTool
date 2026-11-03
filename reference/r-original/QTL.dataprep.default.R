`QTL.dataprep.default` <-
function (P_data, Gdatafile, Mdatafile, P_geno) 
{
    G_data <- read.table(Gdatafile, header = FALSE, na.strings = c("NA", 
        "."), blank.lines.skip = TRUE, sep = "\t")
    M_data <- read.table(Mdatafile, header = FALSE, na.strings = c("NA", 
        "."), blank.lines.skip = TRUE, sep = "\t")
    P_data[match(P_geno, names(P_data))] <- trimStrings(as.matrix(P_data[match(P_geno, 
        names(P_data))]))
    G_data[, 1] <- trimStrings(G_data[, 1])
    M_data[, 1] <- trimStrings(M_data[, 1])
    G_dataMat <- as.matrix(G_data)
    P_gid <- unique(P_data[match(P_geno, names(P_data))])
    M_mid <- M_data[1]
    colnames(M_mid) <- c("1")
    G_mid <- G_dataMat[1, ]
    G_midt <- as.data.frame(t(G_dataMat)[, 1])
    rownames(G_midt) <- NULL
    G_gidt <- as.data.frame(t(G_dataMat)[1, ])
    colnames(G_gidt) <- "G.id"
    G_diffGid <- as.character(as.matrix(setdiff(G_data[, 1], 
        as.matrix(P_data[match(P_geno, names(P_data))]))))
    G_diffGidNoSpace <- G_diffGid[which(G_diffGid != "")]
    P_diffGid <- as.character(as.matrix(setdiff(as.matrix(P_data[match(P_geno, 
        names(P_data))]), G_data[, 1])))
    P_diffGidNoSpace <- P_diffGid[which(P_diffGid != "")]
    P_dataRed <- merge(P_data, G_gidt, by.x = P_geno, by.y = "G.id", 
        sort = TRUE)
    G_dataRed <- merge(G_dataMat, P_gid, by.x = "V1", by.y = P_geno, 
        sort = FALSE)
    G_dataRed <- rbind(G_mid, G_dataRed)
    isNewPhenoCreated <- FALSE
    if (length(P_diffGidNoSpace) != 0) {
        write.table(P_dataRed, file = paste(getwd(), "/newPhenoData.csv", 
            sep = ""), quote = FALSE, sep = ",", row.names = FALSE, 
            col.names = TRUE)
        isNewPhenoCreated <- TRUE
    }
    colnames(M_data) <- c("V1", "V2_1", "V3_1")
    G_datat <- as.data.frame(t(G_dataRed))
    rownames(G_datat) <- NULL
    M_diffMid <- as.character(as.matrix(setdiff(M_data[, 1], 
        G_datat[, 1])))
    M_diffMidNoSpace <- M_diffMid[which(M_diffMid != "")]
    M_dataRed <- merge(M_data, G_midt, by.x = "V1", by.y = names(G_midt)[1], 
        sort = FALSE)
    G_diffMid <- as.character(as.matrix(setdiff(G_datat[, 1], 
        M_data[, 1])))
    G_diffMidNoSpace <- G_diffMid[which(G_diffMid != "")]
    G_datatRed <- merge(M_mid, G_datat, by.x = "1", by.y = names(G_datat)[1], 
        sort = FALSE)
    G_datatRed <- rbind(G_datat[1, ], G_datatRed)
    G_dataRed2 <- t(G_datatRed)
    isNewMapCreated <- FALSE
    if (length(M_diffMidNoSpace) != 0) {
        write.table(M_dataRed, file = paste(getwd(), "/newMapData.txt", 
            sep = ""), quote = FALSE, sep = "\t", row.names = FALSE, 
            col.names = FALSE)
        isNewMapCreated <- TRUE
    }
    isNewGenoCreated <- FALSE
    if (length(G_diffMidNoSpace) != 0 || length(G_diffGidNoSpace) != 
        0) {
        write.table(G_dataRed2, file = paste(getwd(), "/newGenoData.txt", 
            sep = ""), quote = FALSE, sep = "\t", row.names = FALSE, 
            col.names = FALSE)
        isNewGenoCreated <- TRUE
    }
    return(list(G_diffGid = G_diffGid, P_diffGid = P_diffGid, 
        M_diffMid = M_diffMid, G_diffMid = G_diffMid, isNewPhenoCreated = isNewPhenoCreated, 
        isNewMapCreated = isNewMapCreated, isNewGenoCreated = isNewGenoCreated))
}
