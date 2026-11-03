/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import java.util.ArrayList;
import java.util.Arrays;
import org.irri.breedingtool.rjava.manager.api.IRJavaDataManipulationManager;
import org.irri.breedingtool.rjava.utilities.InputTransform;
import org.rosuda.JRI.Rengine;

public class RJavaDataManipulationManager
implements IRJavaDataManipulationManager {
    private Rengine rEngine;
    private InputTransform inputTransform;

    public RJavaDataManipulationManager(Rengine rEngine) {
        this.rEngine = rEngine;
        this.inputTransform = new InputTransform();
    }

    @Override
    public void sortCases(String dataFileName, String newFileName, String[] varToSort, String[] varToSortOrder) {
        String varToSortRObject = this.inputTransform.createRVector(varToSort);
        String varToSortOrderRObject = this.inputTransform.createRVector(varToSortOrder);
        String ls = "ls()";
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\"), blank.lines.skip=TRUE, sep = \",\")";
        String funcSortCases = "sorted <- SortCases(data = dataRead, var = " + varToSortRObject + ", sortBy = " + varToSortOrderRObject + ")";
        String writeData = "write.csv(sorted, quote = FALSE, file = \"" + newFileName + "\", row.names = FALSE)";
        System.out.println(ls);
        System.out.println(readData);
        System.out.println(funcSortCases);
        System.out.println(writeData);
        this.rEngine.eval(ls);
        this.rEngine.eval(readData);
        this.rEngine.eval(funcSortCases);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void subSet(String dataFileName, String newFileName, ArrayList<String> subsetConditions) {
        String inputSubset = this.inputTransform.subSetInputTransform(subsetConditions);
        this.readData(dataFileName);
        String funcSubset = "subsetted <- tryCatch(subset(dataRead, " + inputSubset + "), error=function(err) \"notRun\")";
        String writeData = "subData <- tryCatch(write.table(subsetted,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(funcSubset);
        System.out.println(writeData);
        this.rEngine.eval(funcSubset);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public ArrayList<String> getVariableInfo(String fileName, String tempFileName, int fileFormat, String separator) {
        ArrayList<String> toreturn = new ArrayList<String>();
        String funcGetVarInfo = fileFormat == 2 ? "varsAndTypes <- getVarInfo(fileName = \"" + fileName + "\", fileFormat = 2, separator = \"" + separator + "\")" : "varsAndTypes <- getVarInfo(fileName = \"" + fileName + "\", fileFormat = " + fileFormat + ", separator = NULL)";
        String writeTempData = "tryCatch(write.table(varsAndTypes,file =\"" + tempFileName + "\",sep=\":\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(funcGetVarInfo);
        System.out.println(writeTempData);
        this.rEngine.eval(funcGetVarInfo);
        this.rEngine.eval(writeTempData);
        String[] vars = this.rEngine.eval("as.vector(varsAndTypes$Variable)").asStringArray();
        String[] types = this.rEngine.eval("as.vector(varsAndTypes$Type)").asStringArray();
        int i = 0;
        while (i < vars.length) {
            toreturn.add(String.valueOf(vars[i]) + ":" + types[i]);
            ++i;
        }
        for (String s : toreturn) {
            System.out.println(s);
        }
        this.rEngineEnd();
        return toreturn;
    }

    @Override
    public String[] getDataInPackage(String pkgName) {
        String getData = "result <- data(package = \"" + pkgName + "\")";
        System.out.println(getData);
        this.rEngine.eval(getData);
        Object[] getResult = this.rEngine.eval("result$results[,\"Item\"]").asStringArray();
        System.out.println(Arrays.toString(getResult));
        this.rEngineEnd();
        return getResult;
    }

    @Override
    public void loadDataInPackage(String pkgName, String dataset, String outputPath) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String getData = "data(" + dataset + ",package = \"" + pkgName + "\")";
        String varInfoInitial = "varInfo <- as.data.frame(names(" + dataset + "))";
        String fxnLoop = "for(i in 1:ncol(" + dataset + ")) {\n";
        fxnLoop = String.valueOf(fxnLoop) + "   if (is.numeric(" + dataset + "[,i])) {  varInfo[i,2] <- \"Numeric\"\n";
        fxnLoop = String.valueOf(fxnLoop) + "   } else {  varInfo[i,2] <- \"Factor\" }\n";
        fxnLoop = String.valueOf(fxnLoop) + "}";
        String varInfo = "names(varInfo) <- c(\"Variable\", \"Type\")\n";
        String listFiles1 = "resultFiles <- list.files(\"" + outputPath + "\", pattern = \".csv\")";
        String listFiles2 = "resultFiles <- unlist(strsplit(resultFiles, split = \"\\\\.csv\"))";
        String listFiles3 = "newName <- \"" + dataset + "\" \n";
        String listFiles4 = "if (any(resultFiles == \"" + dataset + "\")) { \n";
        listFiles4 = String.valueOf(listFiles4) + "     result <- grep(\"" + dataset + "\", x = resultFiles, value = TRUE) \n";
        listFiles4 = String.valueOf(listFiles4) + "     result <- result[which(as.numeric(regexpr(\"" + dataset + "\", text = result)) == 1)] \n";
        listFiles4 = String.valueOf(listFiles4) + "     result <- result[which(sapply(strsplit(result, split = \")\"), length) == 1)] \n";
        listFiles4 = String.valueOf(listFiles4) + "     newName <- paste(\"" + dataset + "\",\"(\",length(result), \")\",sep = \"\") \n";
        listFiles4 = String.valueOf(listFiles4) + "} \n";
        String writeVarInfo = "write.table(varInfo, file = paste(\"" + outputPath + "\", newName,\"_varinfo.txt\", sep = \"\"), sep = \":\", row.names = FALSE)";
        String writeData = "write.csv(" + dataset + ", file = paste(\"" + outputPath + "\", newName, \".csv\", sep = \"\"), row.names = FALSE)";
        System.out.println(rmvStmt);
        System.out.println(getData);
        System.out.println(varInfoInitial);
        System.out.println(fxnLoop);
        System.out.println(varInfo);
        System.out.println(listFiles1);
        System.out.println(listFiles2);
        System.out.println(listFiles3);
        System.out.println(listFiles4);
        System.out.println(writeVarInfo);
        System.out.println(writeData);
        this.rEngine.eval(rmvStmt);
        this.rEngine.eval(getData);
        this.rEngine.eval(varInfoInitial);
        this.rEngine.eval(fxnLoop);
        this.rEngine.eval(varInfo);
        this.rEngine.eval(listFiles1);
        this.rEngine.eval(listFiles2);
        this.rEngine.eval(listFiles3);
        this.rEngine.eval(listFiles4);
        this.rEngine.eval(writeVarInfo);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void dataTransformation(String dataFileName, String newFileName, String newVariableName, String inputVariable, String formula) {
        this.readData(dataFileName);
        String transformData = "newData <- DataTransformation(data = \"dataRead\", var = \"" + inputVariable + "\", transformation = \"" + formula + "\", targetName = \"" + newVariableName + "\")";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(transformData);
        System.out.println(writeData);
        this.rEngine.eval(transformData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void combineFactorLevels(String dataFileName, String newFileName, String[] inputVariableInput, String targetName) {
        String inputVariables = this.inputTransform.createRVector(inputVariableInput);
        this.readData(dataFileName);
        String combinedData = "newData <- CombineFactorLevels(data = \"dataRead\", concatVar = " + inputVariables + ", targetName = \"" + targetName + "\")";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(combinedData);
        System.out.println(writeData);
        this.rEngine.eval(combinedData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    private void readData(String dataFileName) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        this.rEngine.end();
    }

    @Override
    public void aggregateData(String dataFileName, String newFileName, String[] numVariablesInput, String[] factorVariablesInput, String[] functionInput, boolean toAppend) {
        String inputNumVariables = this.inputTransform.createRVector(numVariablesInput);
        String inputFactorVariables = this.inputTransform.createRVector(factorVariablesInput);
        String function = this.inputTransform.createRVector(functionInput);
        this.readData(dataFileName);
        String aggregateData = "newData <- AggregateData(data = \"dataRead\", var = " + inputNumVariables + ", grp = " + inputFactorVariables + ", stat = " + function + ", append = " + String.valueOf(toAppend).toUpperCase() + ")";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(aggregateData);
        System.out.println(writeData);
        this.rEngine.eval(aggregateData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void toWide(String dataFileName, String newFileName, String[] reshapeVariablesInput, String[] retainVariablesInput, String[] indexFactorInput) {
        String reshapeVariables = this.inputTransform.createRVector(reshapeVariablesInput);
        String retainVariables = this.inputTransform.createRVector(retainVariablesInput);
        String indexFactor = this.inputTransform.createRVector(indexFactorInput);
        this.readData(dataFileName);
        String wideData = "newData <- ToWide(data = \"dataRead\", vnames = " + reshapeVariables + ", timevar = " + indexFactor + ", idvar = " + retainVariables + ")";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(wideData);
        System.out.println(writeData);
        this.rEngine.eval(wideData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void toLong(String dataFileName, String newFileName, ArrayList<String[]> reshapeVariablesInput, String[] retainVariablesInput, String[] indexFactorInput, String[] targetVariablesInput, String[] newVarCategoryInput) {
        String reshapeVariables = this.inputTransform.createRVector(reshapeVariablesInput);
        String retainVariables = this.inputTransform.createRVector(retainVariablesInput);
        String indexFactor = this.inputTransform.createRVector(indexFactorInput);
        String targetVariables = this.inputTransform.createRVector(targetVariablesInput);
        String newVarCategory = this.inputTransform.createRVector(newVarCategoryInput);
        this.readData(dataFileName);
        String longData = "newData <- ToLong(data = \"dataRead\", varying = list(" + reshapeVariables + "), timevar = " + indexFactor + ", vnames = " + targetVariables + ", idvar = " + retainVariables + ", label = " + newVarCategory + ")";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(longData);
        System.out.println(writeData);
        this.rEngine.eval(longData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void mergeData(String dataFileName, String transactionFile, String newFileName, String[] keyVariablesMasterInput, String[] keyVariablesTransactInput, String[] retainVariablesMasterInput, String[] retainVariablesTransactInput, int includeOption) {
        String keyVariablesMaster = this.inputTransform.createRVector(keyVariablesMasterInput);
        String keyVariablesTransact = this.inputTransform.createRVector(keyVariablesTransactInput);
        String retainVariablesMaster = this.inputTransform.createRVector(retainVariablesMasterInput);
        String retainVariablesTransact = this.inputTransform.createRVector(retainVariablesTransactInput);
        this.readData(dataFileName);
        String readTransactionData = "dataTransaction <- read.csv(\"" + transactionFile + "\", header = TRUE, na.strings = c(\"NA\",\".\"), blank.lines.skip=TRUE, sep = \",\")";
        String mergeData = null;
        switch (includeOption) {
            case 0: {
                mergeData = "newData <- MergeData(MasterData = \"dataRead\", TransactionData= \"dataTransaction\", byMaster= " + keyVariablesMaster + ", byTransact= " + keyVariablesTransact + ", MasterVarInclude =" + retainVariablesMaster + ", TransactVarInclude =" + retainVariablesTransact + ", allMaster=FALSE, allTransact=FALSE)";
                break;
            }
            case 1: {
                mergeData = "newData <- MergeData(MasterData = \"dataRead\", TransactionData= \"dataTransaction\", byMaster= " + keyVariablesMaster + ", byTransact= " + keyVariablesTransact + ", MasterVarInclude =" + retainVariablesMaster + ", TransactVarInclude =" + retainVariablesTransact + ", allMaster=TRUE, allTransact=FALSE)";
                break;
            }
            case 2: {
                mergeData = "newData <- MergeData(MasterData = \"dataRead\", TransactionData= \"dataTransaction\", byMaster= " + keyVariablesMaster + ", byTransact= " + keyVariablesTransact + ", MasterVarInclude =" + retainVariablesMaster + ", TransactVarInclude =" + retainVariablesTransact + ", allMaster=FALSE, allTransact=TRUE)";
                break;
            }
            case 3: {
                mergeData = "newData <- MergeData(MasterData = \"dataRead\", TransactionData= \"dataTransaction\", byMaster= " + keyVariablesMaster + ", byTransact= " + keyVariablesTransact + ", MasterVarInclude =" + retainVariablesMaster + ", TransactVarInclude =" + retainVariablesTransact + ", allMaster=TRUE, allTransact=TRUE)";
            }
        }
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(readTransactionData);
        System.out.println(mergeData);
        System.out.println(writeData);
        this.rEngine.eval(readTransactionData);
        this.rEngine.eval(mergeData);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void appendData(String MName, String TName, String dataFileName, String transactionFile, String newFileName, String[] keyVariablesMasterInput, String[] keyVariablesTransactInput, String[] retainVariablesMasterInput, String[] retainVariablesTransactInput) {
        String keyVariablesMaster = this.inputTransform.createRVector(keyVariablesMasterInput);
        String keyVariablesTransact = this.inputTransform.createRVector(keyVariablesTransactInput);
        String retainVariablesMaster = this.inputTransform.createRVector(retainVariablesMasterInput);
        String retainVariablesTransact = this.inputTransform.createRVector(retainVariablesTransactInput);
        this.readData(dataFileName);
        String readTransactionData = "dataTransaction <- read.csv(\"" + transactionFile + "\", header = TRUE, na.strings = c(\"NA\",\".\"), blank.lines.skip=TRUE, sep = \",\")";
        String appendData = "newData <- AppendData(MasterData = \"dataRead\", TransactionData= \"dataTransaction\", byMaster= " + keyVariablesMaster + ", byTransact= " + keyVariablesTransact + ", MasterVarKeep =" + retainVariablesMaster + ", TransactVarKeep =" + retainVariablesTransact + ")";
        String renameMaster = "newData[newData[,\"Source\"] == 1,\"Source\"] <- \"" + MName + "\"";
        String renameTransact = "newData[newData[,\"Source\"] == 2,\"Source\"] <- \"" + TName + "\"";
        String writeData = "tryCatch(write.table(newData,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(readTransactionData);
        System.out.println(appendData);
        System.out.println(renameMaster);
        System.out.println(renameTransact);
        System.out.println(writeData);
        this.rEngine.eval(readTransactionData);
        this.rEngine.eval(appendData);
        this.rEngine.eval(renameMaster);
        this.rEngine.eval(renameTransact);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public void convertRdaToCsv(String rdaFilename, String newFileName) {
        String loadRda = "dataFrame<-eval(parse(text = load(\"" + rdaFilename + "\")))";
        String writeData = "tryCatch(write.table(dataFrame,file =\"" + newFileName + "\",sep=\",\",row.names=FALSE), error=function(err) \"notRun\")";
        System.out.println(loadRda);
        System.out.println(writeData);
        this.rEngine.eval(loadRda);
        this.rEngine.eval(writeData);
        this.rEngineEnd();
    }

    @Override
    public String isColumnNumeric(String dataFileName, String columnName) {
        this.readData(dataFileName);
        String isNumeric = "is.numeric(dataRead$" + columnName + ")";
        System.out.println(isNumeric);
        String isNumericOut = this.rEngine.eval(isNumeric).asBool().toString();
        return isNumericOut;
    }

    public int[] howManyLevelsHaveOneDatum(String dataFileName, String categoryName, String responseColumnName, String groupingName) {
        this.readData(dataFileName);
        String deleteNA = "dataRead <- dataRead[(is.na(dataRead[,\"" + responseColumnName + "\"]) == FALSE),]";
        this.rEngine.eval(deleteNA);
        System.out.println(deleteNA);
        String getGroupingLevelsCount = null;
        int groupingLevelsCount = 0;
        if (groupingName != null) {
            String setFactorGroupingName = "tempData[, \"" + groupingName + "\"] = factor(tempData[, \"" + groupingName + "\"])";
            this.rEngine.eval(setFactorGroupingName);
            getGroupingLevelsCount = "nlevels(dataRead[, \"" + groupingName + "\"])";
            groupingLevelsCount = this.rEngine.eval(getGroupingLevelsCount).asInt();
            System.out.println(setFactorGroupingName);
            System.out.println(getGroupingLevelsCount);
        } else {
            groupingLevelsCount = 1;
        }
        String tempData = null;
        int[] count = new int[groupingLevelsCount];
        int m = 0;
        while (m < groupingLevelsCount) {
            tempData = groupingName != null ? "tempData = dataRead[which(dataRead[,\"" + groupingName + "\"] == levels(dataRead[,\"" + groupingName + "\"])[" + (m + 1) + "]),]" : "tempData = dataRead";
            String setFactorCategoryName = "tempData[, \"" + categoryName + "\"] = factor(tempData[, \"" + categoryName + "\"])";
            String computeNi = "ni<-tapply(tempData$" + responseColumnName + ", tempData$" + categoryName + ", length)";
            String niEqualToOne = "sum(ni==1)";
            this.rEngine.eval(tempData);
            this.rEngine.eval(setFactorCategoryName);
            this.rEngine.eval(computeNi);
            count[m] = this.rEngine.eval(niEqualToOne).asInt();
            System.out.println(tempData);
            System.out.println(setFactorCategoryName);
            System.out.println(computeNi);
            System.out.println(niEqualToOne);
            ++m;
        }
        return count;
    }

    public String[] checkLevelsWithOneDatum(String dataFileName, String categoryName, String responseColumnName, String groupingName) {
        this.readData(dataFileName);
        String deleteNA = "dataRead <- dataRead[(is.na(dataRead[,\"" + responseColumnName + "\"]) == FALSE),]";
        this.rEngine.eval(deleteNA);
        System.out.println(deleteNA);
        int groupingLevelsCount = 1;
        String[] groupingLevels = null;
        if (groupingName != null) {
            String setFactorGroupingName = "dataRead[, \"" + groupingName + "\"] = factor(dataRead[, \"" + groupingName + "\"])";
            String getGroupingLevelsCount = "nlevels(dataRead[, \"" + groupingName + "\"])";
            String getGroupingLevels = "levels(dataRead[, \"" + groupingName + "\"])";
            this.rEngine.eval(setFactorGroupingName);
            groupingLevelsCount = this.rEngine.eval(getGroupingLevelsCount).asInt();
            groupingLevels = this.rEngine.eval(getGroupingLevels).asStringArray();
            System.out.println(setFactorGroupingName);
            System.out.println(getGroupingLevelsCount);
            System.out.println(getGroupingLevels);
        }
        String tempData = null;
        int[] count = new int[groupingLevelsCount];
        ArrayList<String> levelCountList = new ArrayList<String>();
        String setFactorCategoryName = "tempData[, \"" + categoryName + "\"] = factor(tempData[, \"" + categoryName + "\"])";
        int m = 0;
        while (m < groupingLevelsCount) {
            tempData = groupingName != null ? "tempData = dataRead[which(dataRead[,\"" + groupingName + "\"] == levels(dataRead[,\"" + groupingName + "\"])[" + (m + 1) + "]),]" : "tempData = dataRead";
            this.rEngine.eval(tempData);
            this.rEngine.eval(setFactorCategoryName);
            System.out.println(tempData);
            System.out.println(setFactorCategoryName);
            String computeNi = "ni<-tapply(tempData$" + responseColumnName + ", tempData$" + categoryName + ", length)";
            String niEqualToOne = "sum(ni==1)";
            this.rEngine.eval(computeNi);
            count[m] = this.rEngine.eval(niEqualToOne).asInt();
            if (count[m] > 0) {
                if (groupingName != null) {
                    levelCountList.add(String.valueOf(groupingLevels[m]) + ": " + count[m]);
                } else {
                    levelCountList.add("FALSE");
                }
            }
            System.out.println(computeNi);
            System.out.println(niEqualToOne);
            ++m;
        }
        if (!levelCountList.isEmpty()) {
            String[] levelCount = new String[levelCountList.size()];
            int i = 0;
            while (i < levelCountList.size()) {
                levelCount[i] = (String)levelCountList.get(i);
                ++i;
            }
            return levelCount;
        }
        String[] withLevelCount = new String[]{"TRUE"};
        return withLevelCount;
    }

    public String[] checkLevelsWithMoreThanOneDatum(String dataFileName, String categoryName, String responseColumnName, String groupingName) {
        this.readData(dataFileName);
        String deleteNA = "dataRead <- dataRead[(is.na(dataRead[,\"" + responseColumnName + "\"]) == FALSE),]";
        this.rEngine.eval(deleteNA);
        System.out.println(deleteNA);
        int groupingLevelsCount = 1;
        String[] groupingLevels = null;
        if (groupingName != null) {
            String setFactorGroupingName = "dataRead[, \"" + groupingName + "\"] = factor(dataRead[, \"" + groupingName + "\"])";
            String getGroupingLevelsCount = "nlevels(dataRead[, \"" + groupingName + "\"])";
            String getGroupingLevels = "levels(dataRead[, \"" + groupingName + "\"])";
            this.rEngine.eval(setFactorGroupingName);
            groupingLevelsCount = this.rEngine.eval(getGroupingLevelsCount).asInt();
            groupingLevels = this.rEngine.eval(getGroupingLevels).asStringArray();
            System.out.println(setFactorGroupingName);
            System.out.println(getGroupingLevelsCount);
            System.out.println(getGroupingLevels);
        }
        String tempData = null;
        int[] count = new int[groupingLevelsCount];
        ArrayList<String> levelCountList = new ArrayList<String>();
        String setFactorCategoryName = "tempData[, \"" + categoryName + "\"] = factor(tempData[, \"" + categoryName + "\"])";
        int m = 0;
        while (m < groupingLevelsCount) {
            tempData = groupingName != null ? "tempData = dataRead[which(dataRead[,\"" + groupingName + "\"] == levels(dataRead[,\"" + groupingName + "\"])[" + (m + 1) + "]),]" : "tempData = dataRead";
            this.rEngine.eval(tempData);
            this.rEngine.eval(setFactorCategoryName);
            System.out.println(tempData);
            System.out.println(setFactorCategoryName);
            String computeNi = "ni<-tapply(tempData$" + responseColumnName + ", tempData$" + categoryName + ", length)";
            String niGreaterThanOne = "sum(ni>1)";
            this.rEngine.eval(computeNi);
            count[m] = this.rEngine.eval(niGreaterThanOne).asInt();
            if (count[m] > 0) {
                if (groupingName != null) {
                    levelCountList.add(String.valueOf(groupingLevels[m]) + ": " + count[m]);
                } else {
                    levelCountList.add("FALSE");
                }
            }
            System.out.println(computeNi);
            System.out.println(niGreaterThanOne);
            ++m;
        }
        if (!levelCountList.isEmpty()) {
            String[] levelCount = new String[levelCountList.size()];
            int i = 0;
            while (i < levelCountList.size()) {
                levelCount[i] = (String)levelCountList.get(i);
                ++i;
            }
            return levelCount;
        }
        String[] withLevelCount = new String[]{"TRUE"};
        return withLevelCount;
    }

    @Override
    public String[] getColumnHeaders(String dataFileName) {
        this.readData(dataFileName);
        String getColumnHeaders = "names(dataRead)";
        Object[] columnHeaders = this.rEngine.eval(getColumnHeaders).asStringArray();
        System.out.println(Arrays.toString(columnHeaders));
        this.rEngineEnd();
        return columnHeaders;
    }

    @Override
    public void importGeneticFile(String dataFileName, String outFileName, String extension, String population, String individual, String ploidy, String sep) {
        try {
            String callLibrary = "library(adegenet)";
            String data = null;
            if (extension.equals("csv")) {
                String sep2 = null;
                sep2 = sep.equals("NULL") ? sep : "\"" + sep + "\"";
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"csv\", population=\"" + population + "\", individual=\"" + individual + "\", ploidyDegree=\"" + ploidy + "\", sep=" + sep2 + ")";
            }
            if (extension.equals("gtx")) {
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"gtx\")";
            }
            if (extension.equals("str")) {
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"str\")";
            }
            if (extension.equals("stru")) {
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"stru\")";
            }
            if (extension.equals("dat")) {
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"dat\")";
            }
            if (extension.equals("gen")) {
                data = "data<-importGenFile(filename=\"" + dataFileName + "\", extension=\"gen\")";
            }
            String save = "save(data, file=\"" + outFileName + "\")";
            this.rEngine.eval(callLibrary);
            this.rEngine.eval(data);
            this.rEngine.eval(save);
            System.out.println(callLibrary);
            System.out.println(data);
            System.out.println(save);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String isObjectGenind(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String isGenind = "is.genind(data)";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        System.out.println(load);
        System.out.println(isGenind);
        String isNumericOut = this.rEngine.eval(isGenind).asBool().toString();
        this.rEngineEnd();
        return isNumericOut;
    }

    @Override
    public String[] getGenindPopNames(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@pop.names";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        Object[] result = this.rEngine.eval("paste(names(b), \": \", b, sep=\"\")").asStringArray();
        System.out.println(Arrays.toString(result));
        this.rEngineEnd();
        return result;
    }

    @Override
    public String[] getGenindIndNames(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@ind.names";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        Object[] result = this.rEngine.eval("paste(names(b), \": \", b, sep=\"\")").asStringArray();
        System.out.println(Arrays.toString(result));
        this.rEngineEnd();
        return result;
    }

    @Override
    public String[] getGenindLocNames(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@loc.names";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        Object[] result = this.rEngine.eval("paste(names(b), \": \", b, sep=\"\")").asStringArray();
        System.out.println(Arrays.toString(result));
        this.rEngineEnd();
        return result;
    }

    @Override
    public String[] getGenindAlleleNames(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@all.names";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        Object[] result = this.rEngine.eval("paste(names(unlist(b)), \": \", unlist(b), sep=\"\")").asStringArray();
        System.out.println(Arrays.toString(result));
        this.rEngineEnd();
        return result;
    }

    @Override
    public String[] getGenindNumberAllelesPerMarker(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@loc.nall";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        Object[] result = this.rEngine.eval("paste(names(b), \": \", b, sep=\"\")").asStringArray();
        System.out.println(Arrays.toString(result));
        this.rEngineEnd();
        return result;
    }

    @Override
    public int getGenindPloidy(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@ploidy";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        int result = this.rEngine.eval("b").asInt();
        System.out.println(result);
        this.rEngineEnd();
        return result;
    }

    @Override
    public String getGenindType(String rDataFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String b = "b<-data@type";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(b);
        System.out.println(load);
        System.out.println(b);
        String resultTemp = this.rEngine.eval("b").asString();
        System.out.println("resultTemp: " + resultTemp);
        String result = null;
        if (resultTemp.equals("codom")) {
            result = "codominant";
        }
        if (resultTemp.equals("PA")) {
            result = "presence/absence";
        }
        System.out.println("result: " + result);
        this.rEngineEnd();
        return result;
    }

    @Override
    public void getGenindTab(String rDataFilename, String outFilename) {
        String rm = "rm(list=ls(all=TRUE))";
        String callLibrary = "library(adegenet)";
        String load = "load(\"" + rDataFilename + "\")";
        String writeToFile = "write.table(data@tab,file = \"" + outFilename + "\",sep=\",\",col.names=NA, row.names=TRUE)";
        this.rEngine.eval(rm);
        this.rEngine.eval(callLibrary);
        System.out.println(rm);
        System.out.println(callLibrary);
        this.rEngine.eval(load);
        this.rEngine.eval(writeToFile);
        System.out.println(load);
        System.out.println(writeToFile);
        this.rEngineEnd();
    }

    public String isPackageInstalled(String packageName) {
        String isPkgInstalled = "isPkgInstalled <- require(" + packageName + ")";
        System.out.println(isPkgInstalled);
        this.rEngine.eval(isPkgInstalled);
        String getResult = this.rEngine.eval("isPkgInstalled").asBool().toString();
        System.out.println(getResult);
        this.rEngine.eval(getResult);
        this.rEngineEnd();
        return getResult;
    }

    private void rEngineEnd() {
        String rm = "rm(list=ls(all=TRUE))";
        this.rEngine.eval(rm);
        this.rEngine.end();
    }
}

