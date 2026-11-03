/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import java.util.Arrays;
import org.irri.breedingtool.rjava.manager.api.IRJavaSTARAnalysisManager;
import org.irri.breedingtool.rjava.utilities.InputTransform;
import org.rosuda.JRI.Rengine;

public class STARAnalysisManager
implements IRJavaSTARAnalysisManager {
    private Rengine rEngine;
    private InputTransform inputTransform;

    public STARAnalysisManager(Rengine rEngine) {
        this.rEngine = rEngine;
        this.inputTransform = new InputTransform();
    }

    private void rEngineEnd() {
        String rm = "rm(list=ls(all=TRUE))";
        this.rEngine.eval(rm);
        this.rEngine.end();
    }

    @Override
    public void doDescriptiveStatistics(String dataFileName, String outFileName, String[] respvar, String[] grp, String[] statistics) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String statVector = this.inputTransform.createRVector(statistics);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcDescStat = "result <- try(";
        String command = "DescriptiveStatistics(data = \"dataRead\", var = " + respvarVector;
        if (grp != null) {
            command = String.valueOf(command) + ", grp = " + this.inputTransform.createRVector(grp);
        }
        command = String.valueOf(command) + ", statistics = " + statVector + ")";
        funcDescStat = String.valueOf(funcDescStat) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcDescStat);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcDescStat);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in DescriptiveStatistics:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            String sinkOut = "sink()";
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            String sinkOut = "sink()";
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            String FName = "newFName <- gsub(\".txt\", \".csv\",\"" + outFileName + "\")";
            String writefile = "write.csv(result, file = newFName,row.names = FALSE)";
            System.out.println(FName);
            System.out.println(writefile);
            this.rEngine.eval(FName);
            this.rEngine.eval(writefile);
        }
        this.rEngineEnd();
    }

    @Override
    public void doCrossTab(String dataFileName, String resultFolderPath, String[] rowvar, String[] colvar, boolean expFreq, boolean stdresid, boolean totPercent, boolean rowPercent, boolean colPercent) {
        String rowvarVector = this.inputTransform.createRVector(rowvar);
        String colvarVector = this.inputTransform.createRVector(colvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "CrossTabOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\n\", date(),\"\n\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "Crosstab(dataRead, rowVar = " + rowvarVector + ", colVar = " + colvarVector + ", obsFreq = TRUE";
        if (expFreq) {
            command = String.valueOf(command) + ", expFreq = TRUE";
        }
        if (stdresid) {
            command = String.valueOf(command) + ", stdresid = TRUE";
        }
        if (totPercent) {
            command = String.valueOf(command) + ", totPercent = TRUE";
        }
        if (rowPercent) {
            command = String.valueOf(command) + ", rowPercent = TRUE";
        }
        if (colPercent) {
            command = String.valueOf(command) + ", colPercent = TRUE";
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in Cross Tabulation:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNormality(String dataFileName, String outFileName, String[] respvar, String[] grp, String[] method, String outputBoxplot, String graphBoxFileName, String outputHist, String graphHistFileName) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String methodVector = this.inputTransform.createRVector(method);
        String grpVector = null;
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcNormality = "result <- try(";
        String command = "NormalityTest(data = \"dataRead\", var = " + respvarVector;
        if (grp != null) {
            grpVector = this.inputTransform.createRVector(grp);
            command = String.valueOf(command) + ", grp = " + grpVector;
        }
        command = String.valueOf(command) + ", method = " + methodVector + ")";
        funcNormality = String.valueOf(funcNormality) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcNormality);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcNormality);
        String sinkOut = "sink()";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in NormalityTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            if (outputBoxplot == "TRUE" || outputHist == "TRUE") {
                String var = "\nrespVar <- " + respvarVector;
                System.out.println(var);
                this.rEngine.eval(var);
                if (grp != null) {
                    String grpvar = "grpVar <- " + grpVector;
                    System.out.println(grpvar);
                    this.rEngine.eval(grpvar);
                }
                if (outputBoxplot == "TRUE") {
                    String boxplot1 = grp != null ? "for (i in (1:length(respVar))) {\n for (j in (1:length(grpVar))) {\n  png(file = paste (\"" + graphBoxFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n  boxplot(dataRead[,respVar[i]] ~ dataRead[,grpVar[j]], main = paste(\"Boxplot of \", respVar[i],\" by \", grpVar[j], sep = \"\"), ylab = respVar[i], xlab = grpVar[j]) \n  dev.off() \n } \n}" : "for (i in (1:length(respVar))) { \n png(file = paste (\"" + graphBoxFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n boxplot(dataRead[,respVar[i]], horizontal = TRUE, main = paste(\"Boxplot of \", respVar[i],sep = \"\"), xlab = respVar[i]) \n dev.off() \n}";
                    System.out.println(boxplot1);
                    this.rEngine.eval(boxplot1);
                }
                if (outputHist == "TRUE") {
                    String libLoad = "library(lattice)";
                    String histogram1 = grp != null ? "for (i in (1:length(respVar))) {\n for (j in (1:length(grpVar))) {\n  png(file = paste (\"" + graphHistFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n  print(histogram(~dataRead[,respVar[i]] | dataRead[,grpVar[j]], data = dataRead, main = paste(\"Histogram of \", respVar[i],\" by \", grpVar[j], sep = \"\"), xlab = respVar[i])) \n  dev.off() \n } \n}" : "for (i in (1:length(respVar))) { \n png(file = paste (\"" + graphHistFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n print(hist(dataRead[,respVar[i]], main = paste(\"Histogram of \", respVar[i],sep = \"\"), xlab = respVar[i])) \n dev.off() \n}";
                    System.out.println(libLoad);
                    System.out.println(histogram1);
                    this.rEngine.eval(libLoad);
                    this.rEngine.eval(histogram1);
                }
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doHeteroskedasticity(String dataFileName, String outFileName, String[] respvar, String[] grp, String[] method, String outputBoxplot, String graphBoxFileName, String outputHist, String graphHistFileName) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String methodVector = this.inputTransform.createRVector(method);
        String grpVector = this.inputTransform.createRVector(grp);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "HeteroskedasticityTest(data = \"dataRead\", var = " + respvarVector + ", grp = " + grpVector + ", method = " + methodVector + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String sinkOut = "sink()";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in HeteroskedasticityTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            if (outputBoxplot == "TRUE" || outputHist == "TRUE") {
                String var = "\nrespVar <- " + respvarVector;
                String grpvar = "grpVar <- " + grpVector;
                System.out.println(var);
                System.out.println(grpvar);
                this.rEngine.eval(var);
                this.rEngine.eval(grpvar);
                if (outputBoxplot == "TRUE") {
                    String boxplot1 = "for (i in (1:length(respVar))) {\n for (j in (1:length(grpVar))) {\n  png(file = paste (\"" + graphBoxFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n  boxplot(dataRead[,respVar[i]] ~ dataRead[,grpVar[j]], main = paste(\"Boxplot of \", respVar[i],\" by \", grpVar[j], sep = \"\"), ylab = respVar[i], xlab = grpVar[j]) \n  dev.off() \n } \n}";
                    System.out.println(boxplot1);
                    this.rEngine.eval(boxplot1);
                }
                if (outputHist == "TRUE") {
                    String libLoad = "library(lattice)";
                    String histogram1 = "for (i in (1:length(respVar))) {\n for (j in (1:length(grpVar))) {\n  png(file = paste (\"" + graphHistFileName + "\",\"_\", respVar[i],\".png\", sep = \"\")) \n  print(histogram(~dataRead[,respVar[i]] | dataRead[,grpVar[j]], data = dataRead, main = paste(\"Histogram of \", respVar[i],\" by \", grpVar[j], sep = \"\"), xlab = respVar[i])) \n  dev.off() \n } \n}";
                    System.out.println(libLoad);
                    System.out.println(histogram1);
                    this.rEngine.eval(libLoad);
                    this.rEngine.eval(histogram1);
                }
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doOneMean(String dataFileName, String outFileName, String[] respvar, Integer testValue, String altHypo, boolean statistics, boolean confInt, float confLevel, String[] method) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMeans(data = \"dataRead\", varX = " + respvarVector + ", testVal = " + testValue + ", procedure = \"one\", alternative = \"" + altHypo + "\"";
        if (statistics) {
            command = String.valueOf(command) + ", statistics = TRUE";
        }
        if (confInt) {
            command = String.valueOf(command) + ", CI = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        if (method != null) {
            command = String.valueOf(command) + ", normality = " + this.inputTransform.createRVector(method);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMeans:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doPairedMean(String dataFileName, String outFileName, String[] varX, String[] varY, String altHypo, boolean statistics, boolean confInt, float confLevel, String[] method) {
        String varXVector = this.inputTransform.createRVector(varX);
        String varYVector = this.inputTransform.createRVector(varY);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMeans(data = \"dataRead\", varX = " + varXVector + ", varY = " + varYVector + ", procedure = \"paired\", alternative = \"" + altHypo + "\"";
        if (statistics) {
            command = String.valueOf(command) + ", statistics = TRUE";
        }
        if (confInt) {
            command = String.valueOf(command) + ", CI = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        if (method != null) {
            command = String.valueOf(command) + ", normality = " + this.inputTransform.createRVector(method);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMeans:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doIndependentMean(String dataFileName, String outFileName, String[] respvar, String grp, String altHypo, double levelSig, boolean statistics, boolean confInt, float confLevel, String[] method) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMeans(data = \"dataRead\", varX = " + respvarVector + ", grp = \"" + grp + "\", procedure = \"independent\", alternative = \"" + altHypo + "\"";
        if (statistics) {
            command = String.valueOf(command) + ", statistics = TRUE";
        }
        if (confInt) {
            command = String.valueOf(command) + ", CI = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        if (method != null) {
            command = String.valueOf(command) + ", normality = " + this.inputTransform.createRVector(method);
        }
        command = String.valueOf(command) + ", alpha = " + levelSig + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMeans:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doProportionTest(String dataFileName, String resultFolderPath, String procedure, String[] varX, String[] varY, String grp, double[] testVal, String alternative, boolean confInterval, int confLevel) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "ProportionOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareProportion(data = \"dataRead\", varX = " + this.inputTransform.createRVector(varX);
        if (procedure.equals("one")) {
            command = String.valueOf(command) + ", procedure = \"one\", testVal = " + this.inputTransform.createRNumVector(testVal);
            command = alternative.equals("not equal") ? String.valueOf(command) + ", alternative = \"two.sided\"" : String.valueOf(command) + ", alternative = \"" + alternative + "\"";
        } else if (procedure.equals("paired")) {
            command = String.valueOf(command) + ", varY = " + this.inputTransform.createRVector(varY) + ", procedure = \"paired\"";
        } else if (procedure.equals("independent")) {
            command = String.valueOf(command) + ", grp = \"" + grp + "\", procedure = \"independent\"";
            command = alternative.equals("not equal") ? String.valueOf(command) + ", alternative = \"two.sided\"" : String.valueOf(command) + ", alternative = \"" + alternative + "\"";
        }
        if (confInterval) {
            command = String.valueOf(command) + ", CI = TRUE, confLevel = " + (confLevel /= 100);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareProportion:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doChiSqGoodnessOfFit(String dataFileName, String resultFolderPath, String inputType, String testvar, String freqVar, boolean categoryEqual, String[] specifiedCategory, int[] specifiedExpFreq) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "GoodnessOfFitOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        if (!categoryEqual) {
            String expCat = "expCat <- " + this.inputTransform.createRVector(specifiedCategory);
            String expFreq = "expFreq <- " + this.inputTransform.createRNumVector(specifiedExpFreq);
            String expFreq1 = "expFreq <- as.table(expFreq)";
            String expFreq2 = "names(expFreq) <- expCat";
            System.out.println(expCat);
            System.out.println(expFreq);
            System.out.println(expFreq1);
            System.out.println(expFreq2);
            this.rEngine.eval(expCat);
            this.rEngine.eval(expFreq);
            this.rEngine.eval(expFreq1);
            this.rEngine.eval(expFreq2);
        }
        String fxnStmt = "result <- try(";
        String command = "GoodnessOfFitTest(data = \"dataRead\", var = \"" + testvar + "\"";
        if (inputType == "summary") {
            command = String.valueOf(command) + ", colFreq = \"" + freqVar + "\"";
        }
        if (!categoryEqual) {
            command = String.valueOf(command) + ", expected = expFreq";
        }
        command = String.valueOf(command) + ")";
        fxnStmt = String.valueOf(fxnStmt) + command + ", silent = TRUE)";
        System.out.println(fxnStmt);
        this.rEngine.eval(fxnStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in Chi-Square Goodness of Fit Test:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doChiSqTestOfIndependence(String dataFileName, String resultFolderPath, String inputType, String[] rowvar, String[] colvar, String freqvar, boolean obsFreq, boolean expFreq, boolean stdresid, boolean totPercent, boolean rowPercent, boolean colPercent, boolean phi, boolean cramersv, boolean contingency) {
        String rowvarVector = this.inputTransform.createRVector(rowvar);
        String colvarVector = this.inputTransform.createRVector(colvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "ChiSqIndependenceOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String fxnStmt = "result <- try(";
        String command = "Crosstab(data = \"dataRead\", rowVar = " + rowvarVector + ", colVar = " + colvarVector;
        command = inputType == "summary" ? (freqvar != null ? String.valueOf(command) + ", freqVar = \"" + freqvar + "\"" : String.valueOf(command) + ", freqVar = NULL") : String.valueOf(command) + ", freqVar = NULL";
        command = String.valueOf(command) + ", obsFreq = " + String.valueOf(obsFreq).toUpperCase();
        if (expFreq) {
            command = String.valueOf(command) + ", expFreq = TRUE";
        }
        if (stdresid) {
            command = String.valueOf(command) + ", stdresid = TRUE";
        }
        if (totPercent) {
            command = String.valueOf(command) + ", totPercent = TRUE";
        }
        if (rowPercent) {
            command = String.valueOf(command) + ", rowPercent = TRUE";
        }
        if (colPercent) {
            command = String.valueOf(command) + ", colPercent = TRUE";
        }
        command = String.valueOf(command) + ", chisq = TRUE";
        if (phi) {
            command = String.valueOf(command) + ", phi = TRUE";
        }
        if (cramersv) {
            command = String.valueOf(command) + ", cramersv = TRUE";
        }
        if (contingency) {
            command = String.valueOf(command) + ", contingency = TRUE";
        }
        command = String.valueOf(command) + ")";
        fxnStmt = String.valueOf(fxnStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(fxnStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(fxnStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in Chi-Square Test of Independence:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public String[] doANOVA(String dataFileName, String outputPath, String design, String[] respvar, String[] factor1, String[] factor2, String[] factor3, String[] factor4, String rep1, String rep2, String set, boolean descriptive, boolean normality, boolean homogeneity, String[] pwtest, String[] pwvar, boolean contrastOption, double sig) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String loadWorkspace = null;
        String contrastStmt = null;
        if (contrastOption) {
            loadWorkspace = "load(file = \"" + outputPath + "tempContrastWorkspace.RData\")";
            contrastStmt = "contrast.option <- resultContrast$contrast.option";
        }
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + design + "Output.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "AOVResult <- AOVTest(data = \"dataRead\", design = \"" + design + "\", respvar = " + this.inputTransform.createRVector(respvar);
        command = String.valueOf(command) + ", factor1 = " + this.inputTransform.createRVector(factor1);
        if (design == "SplitCRD" || design == "SplitRCBD" || design == "SplitLSD" || design == "Strip") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
        }
        if (design == "Split2CRD" || design == "Split2RCBD" || design == "Split2LSD" || design == "Strip-Split") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
            command = String.valueOf(command) + ", factor3 = " + this.inputTransform.createRVector(factor3);
        }
        if (design == "Split3CRD" || design == "Split3RCBD" || design == "Split3LSD" || design == "Strip-Split2") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
            command = String.valueOf(command) + ", factor3 = " + this.inputTransform.createRVector(factor3);
            command = String.valueOf(command) + ", factor4 = " + this.inputTransform.createRVector(factor4);
        }
        if (design != "CRD") {
            command = String.valueOf(command) + ", rep1 = \"" + rep1 + "\"";
        }
        if (design == "LSD" || design == "SplitLSD" || design == "Split2LSD" || design == "Split3LSD") {
            command = String.valueOf(command) + ", rep2 = \"" + rep2 + "\"";
        }
        if (set != null) {
            command = String.valueOf(command) + ", set = \"" + set + "\"";
        }
        if (descriptive) {
            command = String.valueOf(command) + ", descriptive = " + String.valueOf(descriptive).toUpperCase();
        }
        if (normality) {
            command = String.valueOf(command) + ", normality = " + String.valueOf(normality).toUpperCase();
        }
        if (homogeneity) {
            command = String.valueOf(command) + ", homogeneity = " + String.valueOf(homogeneity).toUpperCase();
        }
        if (pwtest != null && pwvar != null) {
            command = String.valueOf(command) + ", pwTest = " + this.inputTransform.createRVector(pwtest);
            command = String.valueOf(command) + ", pwVar = " + this.inputTransform.createRVector(pwvar);
        }
        if (contrastOption) {
            command = String.valueOf(command) + ", contrastOption = contrast.option";
        }
        command = String.valueOf(command) + ", sig = " + sig + ", outputPath = \"" + outputPath + "\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(rmvStmt);
        if (contrastOption) {
            System.out.println(loadWorkspace);
            System.out.println(contrastStmt);
        }
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(rmvStmt);
        if (contrastOption) {
            this.rEngine.eval(loadWorkspace);
            this.rEngine.eval(contrastStmt);
        }
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        Object[] getResult = null;
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in AOVTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            String boxplotCommand = "for (i in (1:length(" + this.inputTransform.createRVector(respvar) + "))) {\n";
            boxplotCommand = String.valueOf(boxplotCommand) + "    png(file = paste(\"" + outputPath + design + "Boxplot_\"," + this.inputTransform.createRVector(respvar) + "[i],\".png\", sep = \"\"))\n";
            boxplotCommand = set == null ? String.valueOf(boxplotCommand) + "    boxplot(AOVResult$data[," + this.inputTransform.createRVector(respvar) + "[i]], horizontal = TRUE, main = paste(\"Boxplot of \", " + this.inputTransform.createRVector(respvar) + "[i], sep = \"\"), xlab = " + this.inputTransform.createRVector(respvar) + "[i]) \n" : String.valueOf(boxplotCommand) + "    boxplot(AOVResult$data[," + this.inputTransform.createRVector(respvar) + "[i]] ~ AOVResult$data[,\"" + set + "\"], main = paste(\"Boxplot of \", " + this.inputTransform.createRVector(respvar) + "[i], sep = \"\"), ylab = " + this.inputTransform.createRVector(respvar) + "[i], xlab = \"" + set + "\") \n";
            boxplotCommand = String.valueOf(boxplotCommand) + "    dev.off() \n";
            boxplotCommand = String.valueOf(boxplotCommand) + "}\n";
            System.out.println(boxplotCommand);
            this.rEngine.eval(boxplotCommand);
            String saveWorkspace = "save(dataRead, AOVResult, file = \"" + outputPath + "tempANOVAWorkspace.RData\")";
            System.out.println(saveWorkspace);
            this.rEngine.eval(saveWorkspace);
            String saveData = "write.csv(AOVResult$data, file = paste(\"" + outputPath + "\", \"residualData.csv\", sep = \"\"),row.names = FALSE)";
            System.out.println(saveData);
            this.rEngine.eval(saveData);
            getResult = this.rEngine.eval("AOVResult$rvWithSigEffect").asStringArray();
            System.out.println(Arrays.toString(getResult));
        }
        this.rEngineEnd();
        return getResult;
    }

    @Override
    public String[] doANOVACombined(String dataFileName, String outputPath, String design, String[] respvar, String[] factor1, String[] factor2, String[] factor3, String[] factor4, String rep1, String rep2, String set, boolean descriptive, boolean normality, boolean homogeneity, String[] pwtest, String[] pwvar, boolean contrastOption, double sig) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String loadWorkspace = null;
        String contrastStmt = null;
        if (contrastOption) {
            loadWorkspace = "load(file = \"" + outputPath + "tempContrastWorkspace.RData\")";
            contrastStmt = "contrast.option <- resultContrast$contrast.option";
        }
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + design + "Output.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "AOVResult <- combAOVTest(data = \"dataRead\", design = \"" + design + "\", respvar = " + this.inputTransform.createRVector(respvar);
        command = String.valueOf(command) + ", factor1 = " + this.inputTransform.createRVector(factor1);
        if (design == "SplitCRD" || design == "SplitRCBD" || design == "SplitLSD" || design == "Strip") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
        }
        if (design == "Split2CRD" || design == "Split2RCBD" || design == "Split2LSD" || design == "Strip-Split") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
            command = String.valueOf(command) + ", factor3 = " + this.inputTransform.createRVector(factor3);
        }
        if (design == "Split3CRD" || design == "Split3RCBD" || design == "Split3LSD" || design == "Strip-Split2") {
            command = String.valueOf(command) + ", factor2 = " + this.inputTransform.createRVector(factor2);
            command = String.valueOf(command) + ", factor3 = " + this.inputTransform.createRVector(factor3);
            command = String.valueOf(command) + ", factor4 = " + this.inputTransform.createRVector(factor4);
        }
        if (design != "CRD") {
            command = String.valueOf(command) + ", rep1 = \"" + rep1 + "\"";
        }
        if (design == "LSD" || design == "SplitLSD" || design == "Split2LSD" || design == "Split3LSD") {
            command = String.valueOf(command) + ", rep2 = \"" + rep2 + "\"";
        }
        command = String.valueOf(command) + ", set = \"" + set + "\"";
        if (descriptive) {
            command = String.valueOf(command) + ", descriptive = " + String.valueOf(descriptive).toUpperCase();
        }
        if (normality) {
            command = String.valueOf(command) + ", normality = " + String.valueOf(normality).toUpperCase();
        }
        command = String.valueOf(command) + ", homogeneity = TRUE";
        if (pwtest != null && pwvar != null) {
            command = String.valueOf(command) + ", pwTest = " + this.inputTransform.createRVector(pwtest);
            command = String.valueOf(command) + ", pwVar = " + this.inputTransform.createRVector(pwvar);
        }
        if (contrastOption) {
            command = String.valueOf(command) + ", contrastOption = contrast.option";
        }
        command = String.valueOf(command) + ", sig = " + sig + ", outputPath = \"" + outputPath + "\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(rmvStmt);
        if (contrastOption) {
            System.out.println(loadWorkspace);
            System.out.println(contrastStmt);
        }
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(rmvStmt);
        if (contrastOption) {
            this.rEngine.eval(loadWorkspace);
            this.rEngine.eval(contrastStmt);
        }
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        Object[] getResult = null;
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in combAOVTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            String saveWorkspace = "save(dataRead, AOVResult, file = \"" + outputPath + "tempANOVAWorkspace.RData\")";
            System.out.println(saveWorkspace);
            this.rEngine.eval(saveWorkspace);
            getResult = this.rEngine.eval("AOVResult$rvWithSigEffect").asStringArray();
            System.out.println(Arrays.toString(getResult));
        }
        this.rEngineEnd();
        return getResult;
    }

    @Override
    public void doPairwiseANOVA(String filePath, String[] respvar, String[] method, String design, double sig, boolean combined) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String loadWorkspace = "load(file = \"" + filePath + "tempANOVAWorkspace.RData\")";
        String sinkIn = "sink(\"" + filePath + "PairwiseOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Pairwise Mean Comparison\\n\", date(),\"\\n\\n\", sep = \"\")";
        String respvarStmt = "respvar <- " + this.inputTransform.createRVector(respvar);
        String posStmt = "pos <- match(respvar, AOVResult$rvWithSigEffect)";
        String commandStmt = null;
        if (combined) {
            commandStmt = "for (i in (1:length(pos))) {\n";
            commandStmt = String.valueOf(commandStmt) + "   if (!is.null(AOVResult$pwOption[[pos[i]]]$sigEffect)) {\n";
            commandStmt = String.valueOf(commandStmt) + "       cat(\"Response Variable:\", AOVResult$pwOption[[pos[i]]]$rv, \"\\n\\n\")\n";
            commandStmt = String.valueOf(commandStmt) + "       for (j in (1:length(AOVResult$pwOption[[pos[i]]]$sigEffect))) {\n";
            commandStmt = String.valueOf(commandStmt) + "            pairwiseComparison(aovTable = AOVResult$aovTable[[pos[i]]], \n";
            commandStmt = String.valueOf(commandStmt) + "                               design = \"" + design + "\",\n";
            commandStmt = String.valueOf(commandStmt) + "                               effect = AOVResult$pwOption[[pos[i]]]$sigEffect[j], \n";
            commandStmt = String.valueOf(commandStmt) + "                               data = AOVResult$data, \n";
            commandStmt = String.valueOf(commandStmt) + "                               respvar = AOVResult$pwOption[[pos[i]]]$rv,\n";
            commandStmt = String.valueOf(commandStmt) + "                               pwTest = " + this.inputTransform.createRVector(method) + ",\n";
            commandStmt = String.valueOf(commandStmt) + "                               siglevel = " + sig + ")";
            commandStmt = String.valueOf(commandStmt) + "\n       }\n   }\n}";
        } else {
            commandStmt = "for (i in (1:length(pos))) {\n";
            commandStmt = String.valueOf(commandStmt) + "     for (j in (1:length(AOVResult$pwOption[[pos[i]]]))) {\n";
            commandStmt = String.valueOf(commandStmt) + "          if (j == 1) cat(\"Response Variable: \", AOVResult$pwOption[[pos[i]]][[j]]$rv, \"\\n\\n\") \n";
            commandStmt = String.valueOf(commandStmt) + "          if (!is.null(AOVResult$pwOption[[pos[i]]][[j]]$sigEffect)) {\n";
            commandStmt = String.valueOf(commandStmt) + "               for (k in (1:length(AOVResult$pwOption[[pos[i]]][[j]]$sigEffect))) {\n";
            commandStmt = String.valueOf(commandStmt) + "                    if (AOVResult$pwOption[[pos[i]]][[j]]$setvar != \"NULL\") { \n";
            commandStmt = String.valueOf(commandStmt) + "                         cat(AOVResult$pwOption[[pos[i]]][[j]]$setvar, \" = \", AOVResult$pwOption[[pos[i]]][[j]]$set, \"\\n\\n\")\n";
            commandStmt = String.valueOf(commandStmt) + "                         pairwiseComparison(aovTable = AOVResult$aovTable[[pos[i]]][[j]], \n";
            commandStmt = String.valueOf(commandStmt) + "                                            design = \"" + design + "\", effect = AOVResult$pwOption[[pos[i]]][[j]]$sigEffect[k], \n";
            commandStmt = String.valueOf(commandStmt) + "                                            data = AOVResult$data[AOVResult$data[,AOVResult$pwOption[[pos[i]]][[j]]$setvar] == AOVResult$pwOption[[pos[i]]][[j]]$set,], \n";
            commandStmt = String.valueOf(commandStmt) + "                                            respvar = AOVResult$pwOption[[pos[i]]][[j]]$rv, \n";
            commandStmt = String.valueOf(commandStmt) + "                                            pwTest = " + this.inputTransform.createRVector(method) + ", \n";
            commandStmt = String.valueOf(commandStmt) + "                                            siglevel = " + sig + ")\n";
            commandStmt = String.valueOf(commandStmt) + "                    } else {\n";
            commandStmt = String.valueOf(commandStmt) + "                         pairwiseComparison(aovTable = AOVResult$aovTable[[pos[i]]][[j]], \n";
            commandStmt = String.valueOf(commandStmt) + "                                            design = \"" + design + "\", effect = AOVResult$pwOption[[pos[i]]][[j]]$sigEffect[k], \n";
            commandStmt = String.valueOf(commandStmt) + "                                            data = AOVResult$data, \n";
            commandStmt = String.valueOf(commandStmt) + "                                            respvar = AOVResult$pwOption[[pos[i]]][[j]]$rv, \n";
            commandStmt = String.valueOf(commandStmt) + "                                            pwTest = " + this.inputTransform.createRVector(method) + ", \n";
            commandStmt = String.valueOf(commandStmt) + "                                            siglevel = " + sig + ")\n";
            commandStmt = String.valueOf(commandStmt) + "                    } \n";
            commandStmt = String.valueOf(commandStmt) + "               }\n";
            commandStmt = String.valueOf(commandStmt) + "          }\n";
            commandStmt = String.valueOf(commandStmt) + "     }\n";
            commandStmt = String.valueOf(commandStmt) + "}\n";
        }
        String sinkOut = "sink()";
        System.out.println(rmvStmt);
        System.out.println(loadWorkspace);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(respvarStmt);
        System.out.println(posStmt);
        System.out.println(commandStmt);
        System.out.println(sinkOut);
        this.rEngine.eval(rmvStmt);
        this.rEngine.eval(loadWorkspace);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(respvarStmt);
        this.rEngine.eval(posStmt);
        this.rEngine.eval(commandStmt);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doContrast(String filePath, String[] respvar, String[] factor, String[] type, String[] level, String[] coef, boolean combined) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String loadWorkspace = "load(file = \"" + filePath + "tempANOVAWorkspace.RData\")";
        String sinkIn = "sink(\"" + filePath + "ContrastAnalysisOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Contrast Analysis\\n\", date(),\"\\n\\n\", sep = \"\")";
        String respvarStmt = "respvar <- " + this.inputTransform.createRVector(respvar);
        String contrastStmt = "contrast.option <- list()";
        String contrastFactorStmt = "contrast.option$factor <- " + this.inputTransform.createRVector(factor);
        String contrastTypeStmt = "contrast.option$type <- " + this.inputTransform.createRVector(type);
        String contrastLevelStmt = "contrast.option$level <- " + this.inputTransform.createRVector(level);
        String contrastCoefStmt = "if (length(which(contrast.option$type == \"user\")) != 0) {\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "   path <- " + this.inputTransform.createRVector(coef) + "\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "   contrast.option$coef<- list()\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "   for (i in (1:length(path))) {\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "        temp <- read.csv(path[i], header = TRUE, na.strings = c(\"NA\",\".\"), blank.lines.skip=TRUE, sep = \",\", , row.names = 1)\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "        names(temp) <- levels(factor(dataRead[,contrast.option$factor[i]]))\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "        contrast.option$coef[[i]] <- as.matrix(temp)\n";
        contrastCoefStmt = String.valueOf(contrastCoefStmt) + "   }\n}\n";
        String commandStmt = null;
        if (combined) {
            commandStmt = "for (i in (1:length(respvar))) {\n";
            commandStmt = String.valueOf(commandStmt) + "   mymodel <- paste(respvar[i], \"~\", AOVResult$model)\n";
            commandStmt = String.valueOf(commandStmt) + "   mymodel2 <- paste(respvar[i], \"~\", AOVResult$model2)\n";
            commandStmt = String.valueOf(commandStmt) + "   resultContrast <- ContrastCompute(data = AOVResult$data, aovTable = AOVResult$aovTable[[i]], mymodel, mymodel2, contrast.option)";
            commandStmt = String.valueOf(commandStmt) + "\n}";
        } else {
            commandStmt = "for (i in (1:length(respvar))) {\n";
            commandStmt = String.valueOf(commandStmt) + "   mymodel <- paste(respvar[i], \"~\", AOVResult$model)\n";
            commandStmt = String.valueOf(commandStmt) + "   mymodel2 <- paste(respvar[i], \"~\", AOVResult$model2)\n";
            commandStmt = String.valueOf(commandStmt) + "   for (j in (1:length(AOVResult$pwOption[[i]]))) {\n";
            commandStmt = String.valueOf(commandStmt) + "        if (AOVResult$pwOption[[i]][[j]]$setvar != \"NULL\") {\n";
            commandStmt = String.valueOf(commandStmt) + "             cat(AOVResult$pwOption[[i]][[j]]$setvar, \" = \", AOVResult$pwOption[[i]][[j]]$set, \"\\n\\n\")\n";
            commandStmt = String.valueOf(commandStmt) + "             ContrastCompute(data = AOVResult$data[AOVResult$data[,AOVResult$pwOption[[i]][[j]]$setvar] == AOVResult$pwOption[[i]][[j]]$set,], \n";
            commandStmt = String.valueOf(commandStmt) + "                             aovTable = AOVResult$aovTable[[i]][[j]], mymodel, mymodel2, contrast.option) \n";
            commandStmt = String.valueOf(commandStmt) + "        } else { \n";
            commandStmt = String.valueOf(commandStmt) + "             ContrastCompute(data = AOVResult$data, aovTable = AOVResult$aovTable[[i]][[j]], mymodel, mymodel2, contrast.option) \n";
            commandStmt = String.valueOf(commandStmt) + "        } \n";
            commandStmt = String.valueOf(commandStmt) + "   } \n";
            commandStmt = String.valueOf(commandStmt) + "}\n";
        }
        String sinkOut = "sink()";
        String saveWorkspace = "save(resultContrast, file = \"" + filePath + "tempContrastWorkspace.RData\")";
        System.out.println(rmvStmt);
        System.out.println(loadWorkspace);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(respvarStmt);
        System.out.println(contrastStmt);
        System.out.println(contrastFactorStmt);
        System.out.println(contrastTypeStmt);
        System.out.println(contrastLevelStmt);
        System.out.println(contrastCoefStmt);
        System.out.println(commandStmt);
        System.out.println(sinkOut);
        System.out.println(saveWorkspace);
        this.rEngine.eval(rmvStmt);
        this.rEngine.eval(loadWorkspace);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(respvarStmt);
        this.rEngine.eval(contrastStmt);
        this.rEngine.eval(contrastFactorStmt);
        this.rEngine.eval(contrastTypeStmt);
        this.rEngine.eval(contrastLevelStmt);
        this.rEngine.eval(contrastCoefStmt);
        this.rEngine.eval(commandStmt);
        this.rEngine.eval(sinkOut);
        this.rEngine.eval(saveWorkspace);
        this.rEngineEnd();
    }

    @Override
    public String[] doBIBD(String dataFileName, String outputPath, String[] respvar, String treatment, String block, String set, String[] method, boolean descriptive, boolean normality, boolean homogeneity, double sig) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "BIBDOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "BIBDResult <- BIBDTest(data = \"dataRead\", respvar = " + this.inputTransform.createRVector(respvar);
        command = String.valueOf(command) + ", trmt = \"" + treatment + "\", block = \"" + block + "\"";
        if (set != null) {
            command = String.valueOf(command) + ", set = \"" + set + "\"";
        }
        if (method != null) {
            command = String.valueOf(command) + ", method = " + this.inputTransform.createRVector(method);
        }
        if (descriptive) {
            command = String.valueOf(command) + ", descriptive = " + String.valueOf(descriptive).toUpperCase();
        }
        if (normality) {
            command = String.valueOf(command) + ", normality = " + String.valueOf(normality).toUpperCase();
        }
        if (homogeneity) {
            command = String.valueOf(command) + ", homogeneity = " + String.valueOf(homogeneity).toUpperCase();
        }
        command = String.valueOf(command) + ", alpha = " + sig + ", outputPath = \"" + outputPath + "\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(rmvStmt);
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(rmvStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        Object[] getResult = null;
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in BIBDTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            String boxplotCommand = "for (i in (1:length(" + this.inputTransform.createRVector(respvar) + "))) {\n";
            boxplotCommand = String.valueOf(boxplotCommand) + "    png(file = paste(\"" + outputPath + "BIBDBoxplot_\"," + this.inputTransform.createRVector(respvar) + "[i],\".png\", sep = \"\"))\n";
            boxplotCommand = set == null ? String.valueOf(boxplotCommand) + "    boxplot(BIBDResult$data[," + this.inputTransform.createRVector(respvar) + "[i]], horizontal = TRUE, main = paste(\"Boxplot of \", " + this.inputTransform.createRVector(respvar) + "[i], sep = \"\"), xlab = " + this.inputTransform.createRVector(respvar) + "[i]) \n" : String.valueOf(boxplotCommand) + "    boxplot(BIBDResult$data[," + this.inputTransform.createRVector(respvar) + "[i]] ~ AOVResult$data[,\"" + set + "\"], main = paste(\"Boxplot of \", " + this.inputTransform.createRVector(respvar) + "[i], sep = \"\"), ylab = " + this.inputTransform.createRVector(respvar) + "[i], xlab = \"" + set + "\") \n";
            boxplotCommand = String.valueOf(boxplotCommand) + "    dev.off() \n";
            boxplotCommand = String.valueOf(boxplotCommand) + "}\n";
            System.out.println(boxplotCommand);
            this.rEngine.eval(boxplotCommand);
            String saveWorkspace = "save(dataRead, BIBDResult, file = \"" + outputPath + "tempANOVAWorkspace.RData\")";
            System.out.println(saveWorkspace);
            this.rEngine.eval(saveWorkspace);
            String saveData = "write.csv(BIBDResult$data, file = paste(\"" + outputPath + "\", \"residualData.csv\", sep = \"\"),row.names = FALSE)";
            System.out.println(saveData);
            this.rEngine.eval(saveData);
            getResult = this.rEngine.eval("BIBDResult$rvWithSigEffect").asStringArray();
            System.out.println(Arrays.toString(getResult));
        }
        this.rEngineEnd();
        return getResult;
    }

    @Override
    public void doPairwiseBIBD(String filePath, String[] respvar, String[] method) {
        String rmvStmt = "rm(list=ls(all=TRUE))";
        String loadWorkspace = "load(file = \"" + filePath + "tempANOVAWorkspace.RData\")";
        String sinkIn = "sink(\"" + filePath + "PairwiseOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Pairwise Mean Comparison\\n\", date(),\"\\n\\n\", sep = \"\")";
        String methodStmt = "method <- " + this.inputTransform.createRVector(method);
        String respvarStmt = "respvar <- " + this.inputTransform.createRVector(respvar);
        String posStmt = "pos <- match(respvar, BIBDResult$rvWithSigEffect)";
        String commandStmt = "if (exists(\"BIBDResult\")) {\n";
        commandStmt = String.valueOf(commandStmt) + "     pos <- na.omit(pos)\n";
        commandStmt = String.valueOf(commandStmt) + "     if (length(pos) >= 1) {\n";
        commandStmt = String.valueOf(commandStmt) + "          for (i in (1:length(pos))) { \n";
        commandStmt = String.valueOf(commandStmt) + "               printRVLabel <- FALSE \n";
        commandStmt = String.valueOf(commandStmt) + "               for (j in (1:length(BIBDResult$result[[pos[i]]]$set))) { \n";
        commandStmt = String.valueOf(commandStmt) + "                    if (length(BIBDResult$result[[pos[i]]]$set[[j]]) > 1 && BIBDResult$result[[pos[i]]]$set[[j]]$sigTrmt) { \n";
        commandStmt = String.valueOf(commandStmt) + "                         trmtLabel <- trimStrings(rownames(BIBDResult$result[[pos[i]]]$set[[j]]$ANOVA.adjblk)[1]) \n";
        commandStmt = String.valueOf(commandStmt) + "                         if (!printRVLabel) { \n";
        commandStmt = String.valueOf(commandStmt) + "                              width1 <- 37 + nchar(BIBDResult$rvWithSigEffect[pos[i]]) \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(paste(rep(\"=\", width1), collapse = \"\"), \"\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(\"Response Variable:\", BIBDResult$rvWithSigEffect[pos[i]], \"\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(paste(rep(\"=\", width1), collapse = \"\"), \"\\n\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                              printRVLabel <- TRUE \n";
        commandStmt = String.valueOf(commandStmt) + "                         } \n";
        commandStmt = String.valueOf(commandStmt) + "                         if (!is.null(BIBDResult$set)) { \n";
        commandStmt = String.valueOf(commandStmt) + "                              width2 <- 5 + nchar(BIBDResult$set) + nchar(BIBDResult$result[[pos[i]]]$set[[j]]$setValue) \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(paste(rep(\"-\", width2), collapse = \"\"), \"\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(BIBDResult$set, \"=\", BIBDResult$result[[pos[i]]]$set[[j]]$setValue, \"\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                              cat(paste(rep(\"-\", width2), collapse = \"\"), \"\\n\\n\") \n";
        commandStmt = String.valueOf(commandStmt) + "                         } \n";
        commandStmt = String.valueOf(commandStmt) + "                         printBIBDPairwise(object = BIBDResult$result[[pos[i]]]$set[[j]]$pw, dfError = BIBDResult$result[[pos[i]]]$set[[j]]$dfError, MSError = BIBDResult$result[[pos[i]]]$set[[j]]$MSError, method = method, trmtLabel = trmtLabel) \n";
        commandStmt = String.valueOf(commandStmt) + "                    } \n";
        commandStmt = String.valueOf(commandStmt) + "               } \n";
        commandStmt = String.valueOf(commandStmt) + "          } \n";
        commandStmt = String.valueOf(commandStmt) + "     } \n";
        commandStmt = String.valueOf(commandStmt) + "} \n";
        String sinkOut = "sink()";
        System.out.println(rmvStmt);
        System.out.println(loadWorkspace);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(methodStmt);
        System.out.println(respvarStmt);
        System.out.println(posStmt);
        System.out.println(commandStmt);
        System.out.println(sinkOut);
        this.rEngine.eval(rmvStmt);
        this.rEngine.eval(loadWorkspace);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(methodStmt);
        this.rEngine.eval(respvarStmt);
        this.rEngine.eval(posStmt);
        this.rEngine.eval(commandStmt);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doBivariateCorr(String dataFileName, String outFileName, String[] respvar, String[] method, String alternative, String statistics, String outputPlot) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String methodVector = this.inputTransform.createRVector(method);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "CorrelationOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Correlation Analysis\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcBivariateCorr = "result <- try(";
        String command = "myCorrObj <- BivariateCorrelationTest(data = \"dataRead\", var = " + respvarVector + ", method = " + methodVector + ", alternative = \"" + alternative + "\", statistics = " + statistics + ")";
        funcBivariateCorr = String.valueOf(funcBivariateCorr) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcBivariateCorr);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcBivariateCorr);
        String sinkOut = "sink()";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in BivariateCorrelationTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
            String saveFile = "for (i in (1:length(myCorrObj))) { \n";
            saveFile = String.valueOf(saveFile) + "     procedureName <-  strsplit(myCorrObj[[i]]$procedure, split = \"'\")[[1]][1] \n";
            saveFile = String.valueOf(saveFile) + "     write.csv(myCorrObj[[i]]$estimate, file = paste(\"" + outFileName + "\", procedureName,\"" + "_Estimates.csv\", sep = \"\"), row.names = TRUE) \n";
            saveFile = String.valueOf(saveFile) + "} \n";
            System.out.println(saveFile);
            this.rEngine.eval(saveFile);
            if (outputPlot == "TRUE") {
                String plot1 = "pairs.var <- t(combn(" + respvarVector + ",2))";
                String plot2 = "for (i in (1:nrow(pairs.var))) { \n";
                plot2 = String.valueOf(plot2) + "   png(file = paste (\"" + outFileName + "Scatterplot_\", pairs.var[i,1],\"_\",pairs.var[i,2],\".png\",sep=\"\")) \n";
                plot2 = String.valueOf(plot2) + "   plot(dataRead[,pairs.var[i,1]], dataRead[,pairs.var[i,2]], main = paste(\"Scatterplot of \", pairs.var[i,1],\" and \", pairs.var[i,2], sep = \"\"), ylab = pairs.var[i,2], xlab = pairs.var[i,1])\n";
                plot2 = String.valueOf(plot2) + "   dev.off() \n";
                plot2 = String.valueOf(plot2) + "}";
                String plot3a = "png(file = paste(\"" + outFileName + "\", \"Scatterplot_ALL.png\", sep = \"\"))";
                String plot3b = "pairs(formula(paste(\"~\", paste(" + respvarVector + ", collapse = \"+\", sep = \"\"), sep = \"\")), data = dataRead, main = \"Simple Scatterplot Matrix\")";
                String plot3c = "dev.off()";
                System.out.println(plot1);
                System.out.println(plot2);
                System.out.println(plot3a);
                System.out.println(plot3b);
                System.out.println(plot3c);
                this.rEngine.eval(plot1);
                this.rEngine.eval(plot2);
                this.rEngine.eval(plot3a);
                this.rEngine.eval(plot3b);
                this.rEngine.eval(plot3c);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doLinearReg(String dataFileName, String outputPath, String[] depvar, String[] indepvar, boolean constant, boolean confInt, double confLevel, boolean covMatrix, boolean VIF, boolean autoCorr, String[] heteroskedasticity, String[] normality, boolean COOKS, boolean leverage) {
        String depvarVector = this.inputTransform.createRVector(depvar);
        String indepvarVector = this.inputTransform.createRVector(indepvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "LinearRegOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "LinearRegressionAnalysis(data = \"dataRead\", depVar = " + depvarVector + ", indepVar = " + indepvarVector;
        command = String.valueOf(command) + ", constant = " + String.valueOf(constant).toUpperCase();
        if (confInt) {
            command = String.valueOf(command) + ", confInt = " + String.valueOf(confInt).toUpperCase() + ", confLevel = " + (confLevel /= 100.0);
        }
        if (normality != null) {
            command = String.valueOf(command) + ", normality = " + this.inputTransform.createRVector(normality);
        }
        if (heteroskedasticity != null) {
            command = String.valueOf(command) + ", heteroskedasticity = " + this.inputTransform.createRVector(heteroskedasticity);
        }
        if (autoCorr) {
            command = String.valueOf(command) + ", autoCorr = " + String.valueOf(autoCorr).toUpperCase();
        }
        if (VIF && indepvar.length > 1) {
            command = String.valueOf(command) + ", VIF = " + String.valueOf(VIF).toUpperCase();
        }
        if (COOKS) {
            command = String.valueOf(command) + ", COOKS = " + String.valueOf(COOKS).toUpperCase();
        }
        if (leverage) {
            command = String.valueOf(command) + ", leverage = " + String.valueOf(leverage).toUpperCase();
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in LinearRegressionAnalysis:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            String saveData = "write.csv(result$data, file = \"" + outputPath + "LinearRegNewData.csv\", row.names = FALSE)";
            String yVar = "depvar <- " + depvarVector;
            String xVar = "indepvar <-" + indepvarVector;
            String diagplotStmt = "for (i in (1:length(result$modelFit))) {\n png(file = paste(\"" + outputPath + "\",depvar[i],\"_DiagPlot.png\", sep = \"\"), width = 960, height = 480) \n par(mfrow = c(1,2)) \n plot(fitted.values(result$modelFit[[i]]), residuals(result$modelFit[[i]]), main = \"Residual vs Fitted\", xlab = \"Fitted Values\", ylab = \"Residuals\") \n qqnorm(residuals(result$modelFit[[i]])) \n qqline(residuals(result$modelFit[[i]])) \n dev.off()\n}";
            String scatterplotStmt = "for (i in (1:length(depvar))) { \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "    for (j in (1:length(indepvar))) { \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "        png(file = paste(\"" + outputPath + "\",depvar[i],\"_\", indepvar[j], \"_Scatterplot.png\", sep = \"\"))\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "        plot(dataRead[,depvar[i]] ~ dataRead[,indepvar[j]],\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "             main = paste(\"Scatterplot of \", indepvar[j], \" vs \", depvar[i], sep = \"\"), \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "             ylab = depvar[i], xlab = indepvar[j], \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "             ylim = c(min(dataRead[,depvar[i]]), max(dataRead[,depvar[i]])), \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "             xlim = c(min(dataRead[,indepvar[j]]), max(dataRead[, indepvar[j]]))) \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "        if (length(indepvar) == 1) { abline(lm(dataRead[,depvar[i]] ~ dataRead[,indepvar[j]])) } \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "        dev.off()\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "    } \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "}";
            System.out.println(saveData);
            System.out.println(yVar);
            System.out.println(xVar);
            System.out.println(sinkOut);
            System.out.println();
            System.out.println(diagplotStmt);
            System.out.println(scatterplotStmt);
            this.rEngine.eval(saveData);
            this.rEngine.eval(yVar);
            this.rEngine.eval(xVar);
            this.rEngine.eval(sinkOut);
            this.rEngine.eval(diagplotStmt);
            this.rEngine.eval(scatterplotStmt);
        }
        this.rEngineEnd();
    }

    @Override
    public void doLinearRegSelection(String dataFileName, String outputPath, String[] depvar, String[] indepvar, boolean constant, boolean correlate, String selection, String selectionStat, boolean confInt, double confLevel, boolean covMatrix, String[] normality, String[] heteroskedasticity, boolean autoCorr, boolean VIF, boolean COOKS, boolean leverage, boolean scatterplot) {
        String depvarVector = this.inputTransform.createRVector(depvar);
        String indepvarVector = this.inputTransform.createRVector(indepvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "LinearRegOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "LinearRegressionSelection(data = \"dataRead\", depVar = " + depvarVector + ", indepVar = " + indepvarVector;
        command = String.valueOf(command) + ", constant = " + String.valueOf(constant).toUpperCase();
        if (correlate) {
            command = String.valueOf(command) + ", correlate = " + String.valueOf(correlate).toUpperCase();
        }
        command = String.valueOf(command) + ", selection = \"" + selection + "\", selectionStat = \"" + selectionStat + "\"";
        if (confInt) {
            command = String.valueOf(command) + ", confInt = " + String.valueOf(confInt).toUpperCase() + ", confLevel = " + (confLevel /= 100.0);
        }
        if (covMatrix) {
            command = String.valueOf(command) + ", covMatrix = " + String.valueOf(covMatrix).toUpperCase();
        }
        if (normality != null) {
            command = String.valueOf(command) + ", normality = " + this.inputTransform.createRVector(normality);
        }
        if (heteroskedasticity != null) {
            command = String.valueOf(command) + ", heteroskedasticity = " + this.inputTransform.createRVector(heteroskedasticity);
        }
        if (autoCorr) {
            command = String.valueOf(command) + ", autoCorr = " + String.valueOf(autoCorr).toUpperCase();
        }
        if (VIF && indepvar.length > 1) {
            command = String.valueOf(command) + ", VIF = " + String.valueOf(VIF).toUpperCase();
        }
        if (COOKS) {
            command = String.valueOf(command) + ", COOKS = " + String.valueOf(COOKS).toUpperCase();
        }
        if (leverage) {
            command = String.valueOf(command) + ", leverage = " + String.valueOf(leverage).toUpperCase();
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in LinearRegressionAnalysis:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else if (!selection.equals("allposs")) {
            String saveData = "write.csv(result$data, file = \"" + outputPath + "LinearRegNewData.csv\", row.names = FALSE)";
            String yVar = "depvar <- " + depvarVector;
            String xVar = "indepvar <-" + indepvarVector;
            String diagplotStmt = "for (i in (1:length(result$modelFit))) {\n png(file = paste(\"" + outputPath + "\",depvar[i],\"_DiagPlot.png\", sep = \"\"), width = 960, height = 480) \n par(mfrow = c(1,2)) \n plot(fitted.values(result$modelFit[[i]]), residuals(result$modelFit[[i]]), main = \"Residual vs Fitted\", xlab = \"Fitted Values\", ylab = \"Residuals\") \n qqnorm(residuals(result$modelFit[[i]])) \n qqline(residuals(result$modelFit[[i]])) \n dev.off()\n}";
            if (indepvar.length == 1) {
                String scatterplotStmt = "for (i in (1:length(depvar))) { \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "    for (j in (1:length(indepvar))) { \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "        png(file = paste(\"" + outputPath + "\",depvar[i],\"_\", indepvar[j], \"_Scatterplot.png\", sep = \"\"))\n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "        plot(dataRead[,depvar[i]] ~ dataRead[,indepvar[j]],\n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "             main = paste(\"Scatterplot of \", indepvar[j], \" vs \", depvar[i], sep = \"\"), \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "             ylab = depvar[i], xlab = indepvar[j], \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "             ylim = c(min(dataRead[,depvar[i]]), max(dataRead[,depvar[i]])), \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "             xlim = c(min(dataRead[,indepvar[j]]), max(dataRead[, indepvar[j]]))) \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "        abline(lm(dataRead[,depvar[i]] ~ dataRead[,indepvar[j]])) \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "        dev.off()\n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "    } \n";
                scatterplotStmt = String.valueOf(scatterplotStmt) + "}";
                System.out.println(saveData);
                System.out.println(yVar);
                System.out.println(xVar);
                System.out.println(sinkOut);
                System.out.println();
                System.out.println(scatterplotStmt);
                this.rEngine.eval(saveData);
                this.rEngine.eval(yVar);
                this.rEngine.eval(xVar);
                this.rEngine.eval(sinkOut);
                this.rEngine.eval(scatterplotStmt);
            }
            System.out.println(saveData);
            System.out.println(yVar);
            System.out.println(xVar);
            System.out.println(sinkOut);
            System.out.println();
            System.out.println(diagplotStmt);
            this.rEngine.eval(saveData);
            this.rEngine.eval(yVar);
            this.rEngine.eval(xVar);
            this.rEngine.eval(sinkOut);
            this.rEngine.eval(diagplotStmt);
            if (scatterplot) {
                String scatterplotStmt1 = "png(file = paste(\"" + outputPath + "\", \"ScatterplotMatrix.png\", sep = \"\"))";
                String scatterplotStmt2 = "pairs(formula(paste(\"~\", paste(" + depvarVector + ", collapse = \"+\", sep = \"\"), \"+\", paste(" + indepvarVector + ", collapse = \"+\", sep = \"\"), sep = \"\")), data = dataRead, main = \"Scatterplot Matrix\")";
                String scatterplotStmt3 = "dev.off()";
                System.out.println(scatterplotStmt1);
                System.out.println(scatterplotStmt2);
                System.out.println(scatterplotStmt3);
                this.rEngine.eval(scatterplotStmt1);
                this.rEngine.eval(scatterplotStmt2);
                this.rEngine.eval(scatterplotStmt3);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doNonlinearRegression(String dataFileName, String outputPath, String[] depvar, String indepvar, boolean statistics, String model, String modelExp, String startValues, boolean covMatrix, boolean normality, boolean confInterval, double confLevel) {
        String depvarVector = this.inputTransform.createRVector(depvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "NonLinearRegOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "NonLinearRegression(data = \"dataRead\", depVar = " + depvarVector + ", indepVar = \"" + indepvar + "\"";
        if (statistics) {
            command = String.valueOf(command) + ", statistics = " + String.valueOf(statistics).toUpperCase();
        }
        command = String.valueOf(command) + ", model = \"" + model + "\"";
        command = model.equals("custom") ? String.valueOf(command) + ", modelExp = \"" + modelExp + "\", startValues = \"" + startValues + "\"" : String.valueOf(command) + ", modelExp = NULL, startValues = NULL";
        if (covMatrix) {
            command = String.valueOf(command) + ", covMatrix = " + String.valueOf(covMatrix).toUpperCase();
        }
        if (normality) {
            command = String.valueOf(command) + ", normality = " + String.valueOf(normality).toUpperCase();
        }
        if (confInterval) {
            command = String.valueOf(command) + ", confInterval = " + String.valueOf(confInterval).toUpperCase() + ", confLevel = " + (confLevel /= 100.0);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in NonLinearRegressionAnalysis:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            String saveData = "write.csv(result$data, file = \"" + outputPath + "NonLinearRegNewData.csv\", row.names = FALSE)";
            String yVar = "depvar <- " + depvarVector;
            String xVar = "indepvar <- \"" + indepvar + "\"";
            String plotData = "plotData <- result$data[order(result$data[,indepvar]),]";
            String scatterplotStmt = "for (i in (1:length(depvar))) { \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "yPredVar = paste(depvar[i], \"_pred\", sep = \"\") \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "png(file = paste(\"" + outputPath + "\",depvar[i],\"_\", indepvar, \"_Scatterplot.png\", sep = \"\"))\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "plot(plotData[,depvar[i]] ~ plotData[,indepvar],\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "   main = paste(\"Scatterplot of \", indepvar, \" vs \", depvar[i], sep = \"\"), \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "   ylab = depvar[i], xlab = indepvar, \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "   ylim = c(min(plotData[,depvar[i]]), max(plotData[,depvar[i]])), \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "   xlim = c(min(plotData[,indepvar]), max(plotData[, indepvar]))) \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "lines(plotData[,indepvar], plotData[,yPredVar], type = \"l\") \n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "dev.off()\n";
            scatterplotStmt = String.valueOf(scatterplotStmt) + "}";
            System.out.println(saveData);
            System.out.println(yVar);
            System.out.println(xVar);
            System.out.println(plotData);
            System.out.println(sinkOut);
            System.out.println();
            System.out.println(scatterplotStmt);
            this.rEngine.eval(saveData);
            this.rEngine.eval(yVar);
            this.rEngine.eval(xVar);
            this.rEngine.eval(plotData);
            this.rEngine.eval(sinkOut);
            this.rEngine.eval(scatterplotStmt);
        }
        this.rEngineEnd();
    }

    @Override
    public void doOneMedian(String dataFileName, String outFileName, String[] respvar, Integer testValue, String altHypo, boolean confInt, float confLevel) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "NonParOneOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMedian(data = \"dataRead\", varX = " + this.inputTransform.createRVector(respvar) + ", hypoValue = " + testValue + ", procedure = \"one\", altHypo = \"" + altHypo + "\"";
        if (confInt) {
            command = String.valueOf(command) + ", confInt = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMedian:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doPairedMedian(String dataFileName, String outFileName, String[] varX, String[] varY, String altHypo, boolean confInt, float confLevel) {
        String varXVector = this.inputTransform.createRVector(varX);
        String varYVector = this.inputTransform.createRVector(varY);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "NonParPairedOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMedian(data = \"dataRead\", varX = " + varXVector + ", varY = " + varYVector + ", procedure = \"paired\", altHypo = \"" + altHypo + "\"";
        if (confInt) {
            command = String.valueOf(command) + ", confInt = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMedian:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doIndependentMedian(String dataFileName, String outFileName, String[] respvar, String grp, String altHypo, boolean confInt, float confLevel) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "NonPar2IndependentOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "CompareMedian(data = \"dataRead\", varX = " + respvarVector + ", grp = \"" + grp + "\", procedure = \"independent\", altHypo = \"" + altHypo + "\"";
        if (confInt) {
            command = String.valueOf(command) + ", confInt = TRUE, confLevel = " + (confLevel /= 100.0f);
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in CompareMedian:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doKruskal(String dataFileName, String outFileName, String[] respvar, String[] grp) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "NonParKIndependentOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "KruskalWallisTest(data = \"dataRead\", var = " + this.inputTransform.createRVector(respvar) + ", grp = " + this.inputTransform.createRVector(grp) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in KruskalWallisTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doFriedman(String dataFileName, String resultFolderPath, String[] respvar, String treatment, String block) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "NonParKRelatedOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "FriedmanTest(data = \"dataRead\", var = " + this.inputTransform.createRVector(respvar) + ", trmt = \"" + treatment + "\", block = \"" + block + "\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in FriedmanTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doUnbalancedAnalysis(String dataFileName, String resultFolderPath, String design, String[] respvars, String environment, String factor, String block, String rep, String row, String column, boolean descriptiveStat, boolean varianceComponents, String performPairwise, double levelSig, String[] control, boolean boxplotRawData, boolean histogramRawData, boolean heatmapResiduals, String heatmapRow, String heatmapColumn, boolean diagnosticPlot) {
        String respvarVector = this.inputTransform.createRVector(respvars);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "IncompleteBlock" + design + "Output.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nIncomplete Block\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcStmt = "result <- try(";
        String command = "ssaTest(exptl.design = \"" + design + "\", data = dataRead, respvar = " + respvarVector + ", geno = \"" + factor + "\"";
        if (design == "AugRCB" || design == "BIBD") {
            command = String.valueOf(command) + ", row = \"" + block + "\", column = NULL, rep = NULL";
        }
        if (design == "AugLS") {
            command = String.valueOf(command) + ", row = \"" + row + "\", column = \"" + column + "\", rep = NULL";
        }
        if (design == "Alpha") {
            command = String.valueOf(command) + ", row = \"" + block + "\", column = NULL, rep = \"" + rep + "\"";
        }
        if (design == "RowCol") {
            command = String.valueOf(command) + ", row = \"" + row + "\", column = \"" + column + "\", rep = \"" + rep + "\"";
        }
        if (environment != null) {
            command = String.valueOf(command) + ", env = \"" + environment + "\"";
        }
        if (descriptiveStat) {
            command = String.valueOf(command) + ", descriptive = TRUE";
        }
        if (varianceComponents) {
            command = String.valueOf(command) + ", varComp = TRUE";
        }
        if (performPairwise != "none") {
            command = String.valueOf(command) + ", alpha = " + levelSig;
        }
        if (performPairwise == "control") {
            command = String.valueOf(command) + ", pairwise = \"Dunnett\"";
            if (control != null) {
                command = String.valueOf(command) + ", exclude = " + this.inputTransform.createRVector(control);
            }
        }
        if (performPairwise == "all") {
            command = String.valueOf(command) + ", pairwise = \"Tukey\", ";
        }
        if (diagnosticPlot) {
            command = String.valueOf(command) + ", diagPlot = TRUE, outputPath = \"" + resultFolderPath + "\"";
        }
        command = String.valueOf(command) + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in ssaTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String saveSummaryStat = "write.csv(result$summary.statistics, file = paste(\"" + resultFolderPath + "SummaryStatistics.csv\", sep = \"\"), row.names = FALSE)";
            System.out.println(saveSummaryStat);
            this.rEngine.eval(saveSummaryStat);
            String saveResiduals = "write.csv(result$residData, file = paste(\"" + resultFolderPath + "Residuals.csv\", sep = \"\"), row.names = FALSE)";
            System.out.println(saveResiduals);
            this.rEngine.eval(saveResiduals);
            String var = "respvar <- " + respvarVector;
            System.out.println(var);
            this.rEngine.eval(var);
            if (boxplotRawData) {
                String boxplotCommand;
                if (environment == null) {
                    boxplotCommand = "for (i in (1:length(respvar))) {\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     png(file = paste (\"" + resultFolderPath + "\",\"boxplotSEA_\", respvar[i],\".png\", sep = \"\"))\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     boxplot(dataRead[,respvar[i]], horizontal = TRUE, main = paste(\"Boxplot of \", respvar[i],sep = \"\"), xylab = respvar[i])\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     dev.off()\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "}";
                } else {
                    boxplotCommand = "for (i in (1:length(respvar))) {\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     png(file = paste (\"" + resultFolderPath + "\",\"boxplotSEA_\", respvar[i],\".png\", sep = \"\"))\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     boxplot(dataRead[,respvar[i]] ~ dataRead[,\"" + environment + "\"], main = paste(\"Boxplot of \", respvar[i],\" by " + environment + "\",sep = \"\"), ylab = respvar[i], xlab = \"" + environment + "\")\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "     dev.off()\n";
                    boxplotCommand = String.valueOf(boxplotCommand) + "}";
                }
                System.out.println(boxplotCommand);
                this.rEngine.eval(boxplotCommand);
            }
            if (histogramRawData) {
                String histplotCommand;
                String loadLibrary = "library(lattice)";
                if (environment == null) {
                    histplotCommand = "for (i in (1:length(respvar))) {\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     png(file = paste (\"" + resultFolderPath + "\",\"histSEA_\", respvar[i],\".png\", sep = \"\"))\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     print(histogram(dataRead[,respvar[i]], main = paste(\"Histogram of \", respvar[i],sep = \"\"), xlab = respvar[i]))\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     dev.off()\n";
                    histplotCommand = String.valueOf(histplotCommand) + "}";
                } else {
                    histplotCommand = "for (i in (1:length(respvar))) {\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     png(file = paste (\"" + resultFolderPath + "\",\"histSEA_\", respvar[i],\".png\", sep = \"\"))\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     print(histogram(~dataRead[,respvar[i]] | dataRead[,\"" + environment + "\"], data = dataRead, main = paste(\"Histogram of \", respvar[i],\" by " + environment + "\",sep = \"\"), ylab = respvar[i], xlab = \"" + environment + "\"))\n";
                    histplotCommand = String.valueOf(histplotCommand) + "     dev.off()\n";
                    histplotCommand = String.valueOf(histplotCommand) + "}";
                }
                System.out.println(loadLibrary);
                System.out.println(histplotCommand);
                this.rEngine.eval(loadLibrary);
                this.rEngine.eval(histplotCommand);
            }
            if (heatmapResiduals) {
                String heatmapCommand;
                String loadLibrary2 = "library(lattice)";
                if (environment != null) {
                    heatmapCommand = "for (i in (1:length(respvar))) {\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     for (j in (1:length(unique(dataRead[,\"" + environment + "\"])))) {\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     \tpng(file = paste (\"" + resultFolderPath + "\",\"heatmapSEA_\",respvar[i], \"_\",unique(dataRead[,\"" + environment + "\"])[j],\".png\", sep = \"\"))\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     \tlp = levelplot(result$residData[result$residData[,\"" + environment + "\"] == unique(dataRead[,\"" + environment + "\"])[j], paste(respvar[i],\"_resid\", sep = \"\")] ~ \n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        dataRead[dataRead[,\"" + environment + "\"] == unique(dataRead[,\"" + environment + "\"])[j], \"" + heatmapRow + "\"] *\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        dataRead[dataRead[,\"" + environment + "\"] == unique(dataRead[,\"" + environment + "\"])[j], \"" + heatmapColumn + "\"], \n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        main = paste(respvar[i], \"_resid\",sep = \"\"),\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        sub = paste(\"" + environment + " = \", unique(dataRead[,\"" + environment + "\"])[j], sep = \"\"),\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        xlab = \"" + heatmapRow + "\", ylab = \"" + heatmapColumn + "\",\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                        col.regions = colorRampPalette(c(\"yellow\",\"red\"))(50))\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     \tprint(lp)\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     \tdev.off()\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "     }\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "}";
                } else {
                    heatmapCommand = "for (i in (1:length(respvar))) {\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "   png(file = paste (\"" + resultFolderPath + "\",\"heatmapSEA_\", respvar[i],\".png\", sep = \"\"))\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "   lp = levelplot(result$residData[, paste(respvar[i],\"_resid\", sep = \"\")] ~ dataRead[, \"" + heatmapRow + "\"] * dataRead[, \"" + heatmapColumn + "\"], \n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                  main = paste(respvar[i], \"_resid\",sep = \"\"),\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                  xlab = \"" + heatmapRow + "\", ylab = \"" + heatmapColumn + "\",\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "                  col.regions = colorRampPalette(c(\"yellow\",\"red\"))(50))\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "   print(lp)\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "   dev.off()\n";
                    heatmapCommand = String.valueOf(heatmapCommand) + "}";
                }
                System.out.println(loadLibrary2);
                System.out.println(heatmapCommand);
                this.rEngine.eval(loadLibrary2);
                this.rEngine.eval(heatmapCommand);
            }
        }
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
    }

    @Override
    public void doPCA(String dataFileName, String outputPath, String[] var, String idVar, boolean descriptiveStat, boolean corMatx, boolean covMatx, String matx, String transform, boolean saveScore, boolean scatterMatx, boolean scree, boolean biplot, boolean pcaplot, boolean useIdVar, int[] pChars, double[] pSizes, String[] pCol, boolean showLeg, String legTitle, String legPos, int legNcol, int axesNum) {
        String varVector = this.inputTransform.createRVector(var);
        String pCharsVector = !useIdVar ? this.inputTransform.createRNumVector(pChars) : "NULL";
        String pSizesVector = this.inputTransform.createRNumVector(pSizes);
        String pColVector = this.inputTransform.createRColorRGBVector(pCol);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "PCAOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcPCA = "result <- try(";
        String command = "PCA(data = \"dataRead\", var = " + varVector;
        if (idVar != null) {
            command = String.valueOf(command) + ", idVar = \"" + idVar + "\"";
        }
        command = descriptiveStat ? String.valueOf(command) + ",descriptiveStat = TRUE" : String.valueOf(command) + ",descriptiveStat = FALSE";
        command = corMatx ? String.valueOf(command) + ",corMatx = TRUE" : String.valueOf(command) + ",corMatx = FALSE";
        command = covMatx ? String.valueOf(command) + ",covMatx = TRUE" : String.valueOf(command) + ",covMatx = FALSE";
        command = String.valueOf(command) + ", matx = \"" + matx + "\"";
        command = String.valueOf(command) + ", transform = \"" + transform + "\"";
        command = saveScore ? String.valueOf(command) + ",saveScore = TRUE, outputPath = \"" + outputPath + "\"" : String.valueOf(command) + ",saveScore = FALSE";
        command = scatterMatx ? String.valueOf(command) + ",scatterMatx = TRUE" : String.valueOf(command) + ",scatterMatx = FALSE";
        command = scree ? String.valueOf(command) + ",scree = TRUE" : String.valueOf(command) + ",scree = FALSE";
        command = biplot ? String.valueOf(command) + ",biplot = TRUE" : String.valueOf(command) + ",biplot = FALSE";
        command = pcaplot ? String.valueOf(command) + ",pcaplot = TRUE" : String.valueOf(command) + ",pcaplot = FALSE";
        command = String.valueOf(command) + ", useIdVar = " + String.valueOf(useIdVar).toUpperCase();
        command = String.valueOf(command) + ", pChars = " + pCharsVector;
        command = String.valueOf(command) + ", pSizes = " + pSizesVector;
        command = String.valueOf(command) + ", pCol = " + pColVector;
        command = String.valueOf(command) + ", showLeg = " + String.valueOf(showLeg).toUpperCase();
        command = String.valueOf(command) + ", legTitle = \"" + legTitle + "\"";
        command = String.valueOf(command) + ", legPos = \"" + legPos + "\"";
        command = String.valueOf(command) + ", legNcol = " + legNcol;
        command = String.valueOf(command) + ", axesNum = " + axesNum;
        command = String.valueOf(command) + ")";
        funcPCA = String.valueOf(funcPCA) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcPCA);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcPCA);
        String sinkOut = "sink()\n";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in PCA:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doClusterAgglo(String dataFileName, String outputPath, String[] var, String idVar, String[] sbinVar, String[] abinVar, String[] ofactorVar, String[] factorVar, boolean stand, String distance, String clusmethod, boolean distMatrix, boolean copDistance, boolean clusterMem, boolean descriptiveStatRaw, boolean corMatx, boolean scatterMatx, boolean descriptiveStat, Integer clusterNum, boolean dendrogram, boolean clusterBox, boolean saveMem) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "ClusterAggloOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String funcClusteragglo = " result <- try( ";
        String command = "ClusterAgglo(\"dataRead\"";
        command = var == null ? String.valueOf(command) + ", var = NULL" : String.valueOf(command) + ", var = " + this.inputTransform.createRVector(var);
        if (idVar != null) {
            command = String.valueOf(command) + ", idVar = \"" + idVar + "\"";
        }
        command = sbinVar == null ? String.valueOf(command) + ", sbinVar = NULL" : String.valueOf(command) + ", sbinVar = " + this.inputTransform.createRVector(sbinVar);
        command = abinVar == null ? String.valueOf(command) + ", abinVar = NULL" : String.valueOf(command) + ", abinVar = " + this.inputTransform.createRVector(abinVar);
        command = ofactorVar == null ? String.valueOf(command) + ", ofactorVar = NULL" : String.valueOf(command) + ", ofactorVar = " + this.inputTransform.createRVector(ofactorVar);
        command = factorVar == null ? String.valueOf(command) + ", factorVar = NULL" : String.valueOf(command) + ", factorVar = " + this.inputTransform.createRVector(factorVar);
        command = stand ? String.valueOf(command) + ",stand = TRUE" : String.valueOf(command) + ",stand = FALSE";
        command = String.valueOf(command) + ", distance = \"" + distance + "\"";
        command = String.valueOf(command) + ", clusmethod = \"" + clusmethod + "\"";
        command = distMatrix ? String.valueOf(command) + ",distMatrix = TRUE" : String.valueOf(command) + ",distMatrix = FALSE";
        command = copDistance ? String.valueOf(command) + ",copDistance = TRUE" : String.valueOf(command) + ",copDistance = FALSE";
        command = clusterMem ? String.valueOf(command) + ",clusterMem = TRUE" : String.valueOf(command) + ",clusterMem = FALSE";
        command = descriptiveStatRaw ? String.valueOf(command) + ",descriptiveStatRaw = TRUE" : String.valueOf(command) + ",descriptiveStatRaw = FALSE";
        command = corMatx ? String.valueOf(command) + ",corMatx = TRUE" : String.valueOf(command) + ",corMatx = FALSE";
        command = scatterMatx ? String.valueOf(command) + ",scatterMatx = TRUE" : String.valueOf(command) + ",scatterMatx = FALSE";
        command = descriptiveStat ? String.valueOf(command) + ",descriptiveStat = TRUE" : String.valueOf(command) + ",descriptiveStat = FALSE";
        command = dendrogram ? String.valueOf(command) + ",dendrogram = TRUE, outputPath = \"" + outputPath + "\"" : String.valueOf(command) + ", dendrogram = FALSE";
        command = clusterBox ? String.valueOf(command) + ", clusterBox = TRUE" : String.valueOf(command) + ", clusterBox = FALSE";
        command = saveMem ? String.valueOf(command) + ", saveMem = TRUE" : String.valueOf(command) + ", saveMem = FALSE";
        command = String.valueOf(command) + ", clusterNum = " + clusterNum + ")";
        funcClusteragglo = String.valueOf(funcClusteragglo) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcClusteragglo);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcClusteragglo);
        String sinkOut = "sink()\n";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in ClusterAgglo:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doClusterDivisive(String dataFileName, String outputPath, String[] var, String idVar, String[] sbinVar, String[] abinVar, String[] ofactorVar, String[] factorVar, boolean stand, String distance, boolean distMatrix, boolean copDistance, boolean clusterMem, boolean descriptiveStatRaw, boolean corMatx, boolean scatterMatx, boolean descriptiveStat, Integer clusterNum, boolean dendrogram, boolean clusterBox, boolean saveMem) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "ClusterDivisiveOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\n\", date(),\"\n\n\", sep = \"\")";
        String funcClusterdiv = "result <- try(";
        String command = "ClusterDivisive(\"dataRead\"";
        command = var == null ? String.valueOf(command) + ", var = NULL" : String.valueOf(command) + ", var = " + this.inputTransform.createRVector(var);
        if (idVar != null) {
            command = String.valueOf(command) + ", idVar = \"" + idVar + "\"";
        }
        command = sbinVar == null ? String.valueOf(command) + ", sbinVar = NULL" : String.valueOf(command) + ", sbinVar = " + this.inputTransform.createRVector(sbinVar);
        command = abinVar == null ? String.valueOf(command) + ", abinVar = NULL" : String.valueOf(command) + ", abinVar = " + this.inputTransform.createRVector(abinVar);
        command = ofactorVar == null ? String.valueOf(command) + ", ofactorVar = NULL" : String.valueOf(command) + ", ofactorVar = " + this.inputTransform.createRVector(ofactorVar);
        command = factorVar == null ? String.valueOf(command) + ", factorVar = NULL" : String.valueOf(command) + ", factorVar = " + this.inputTransform.createRVector(factorVar);
        command = stand ? String.valueOf(command) + ",stand = TRUE" : String.valueOf(command) + ",stand = FALSE";
        command = String.valueOf(command) + ", distance = \"" + distance + "\"";
        command = distMatrix ? String.valueOf(command) + ",distMatrix = TRUE" : String.valueOf(command) + ",distMatrix = FALSE";
        command = copDistance ? String.valueOf(command) + ",copDistance = TRUE" : String.valueOf(command) + ",copDistance = FALSE";
        command = clusterMem ? String.valueOf(command) + ",clusterMem = TRUE" : String.valueOf(command) + ",clusterMem = FALSE";
        command = descriptiveStatRaw ? String.valueOf(command) + ",descriptiveStatRaw = TRUE" : String.valueOf(command) + ",descriptiveStatRaw = FALSE";
        command = corMatx ? String.valueOf(command) + ",corMatx = TRUE" : String.valueOf(command) + ",corMatx = FALSE";
        command = scatterMatx ? String.valueOf(command) + ",scatterMatx = TRUE" : String.valueOf(command) + ",scatterMatx = FALSE";
        command = descriptiveStat ? String.valueOf(command) + ",descriptiveStat = TRUE" : String.valueOf(command) + ",descriptiveStat = FALSE";
        command = dendrogram ? String.valueOf(command) + ",dendrogram = TRUE, outputPath = \"" + outputPath + "\"" : String.valueOf(command) + ", dendrogram = FALSE";
        command = clusterBox ? String.valueOf(command) + ", clusterBox = TRUE" : String.valueOf(command) + ", clusterBox = FALSE";
        command = saveMem ? String.valueOf(command) + ", saveMem = TRUE" : String.valueOf(command) + ", saveMem = FALSE";
        command = String.valueOf(command) + ", clusterNum = " + clusterNum + ")";
        funcClusterdiv = String.valueOf(funcClusterdiv) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcClusterdiv);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcClusterdiv);
        String sinkOut = "sink()\n";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in ClusterDivisive:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doClusterKmeans(String dataFileName, String outputPath, String[] var, String idVar, boolean clusterMem, boolean descriptiveStatRaw, boolean corMatx, boolean descriptiveStat, Integer clusterNum, boolean kgraph, boolean saveMem) {
        String varVector = this.inputTransform.createRVector(var);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "ClusterKmeansOutput.txt\")";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\n\", date(),\"\n\n\", sep = \"\")";
        String funcClusterKmeans = "result <-try(";
        String command = "ClusterKmeans(\"dataRead\", var = " + varVector;
        if (idVar != null) {
            command = String.valueOf(command) + ", idVar = \"" + idVar + "\"";
        }
        command = clusterMem ? String.valueOf(command) + ",clusterMem = TRUE" : String.valueOf(command) + ", clusterMem = FALSE";
        command = descriptiveStatRaw ? String.valueOf(command) + ",descriptiveStatRaw = TRUE" : String.valueOf(command) + ",descriptiveStatRaw = FALSE";
        command = corMatx ? String.valueOf(command) + ",corMatx = TRUE" : String.valueOf(command) + ",corMatx = FALSE";
        command = descriptiveStat ? String.valueOf(command) + ",descriptiveStat = TRUE" : String.valueOf(command) + ", descriptiveStat = FALSE";
        command = kgraph ? String.valueOf(command) + ",kgraph = TRUE, outputPath = \"" + outputPath + "\"" : String.valueOf(command) + ", kgraph = FALSE";
        command = saveMem ? String.valueOf(command) + ", saveMem = TRUE" : String.valueOf(command) + ", saveMem = FALSE";
        command = String.valueOf(command) + ", clusterNum = " + clusterNum + ")";
        funcClusterKmeans = String.valueOf(funcClusterKmeans) + command + ", silent = TRUE)";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcClusterKmeans);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcClusterKmeans);
        String sinkOut = "sink()\n";
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in ClusterKmeans:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doFitDistribution(String dataFileName, String outputPath, String yVar, String byVar, String[] distrib, String fitMethod) {
        String distribVector = this.inputTransform.createRVector(distrib);
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip = TRUE, sep = \",\")";
        String outFilePath = String.valueOf(outputPath) + "FitDistributionOutput.txt";
        String removeObjects = "rm(list=ls())";
        String sinkIn = "sink(\"" + outFilePath + "\")";
        String funcStmt = "result <- try(";
        String command = "FitDistrib(data, \"" + outputPath + "\", \"" + yVar + "\", " + byVar + ", " + distribVector + ", \"" + fitMethod + "\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(removeObjects);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in FitDistrib:\\n\",msg, sep = \"\")";
            String errorMsg5 = "if (regexpr(\"mle failed to estimate the parameters\",msg) > 0) cat(\"\\nTry a different fitting method/distribution.\\n\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            System.out.println(errorMsg5);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            this.rEngine.eval(errorMsg5);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doManova(String dataFileName, String outputPath, String[] yVars, String factorVar, String repVar, String testStat, String descriptive, String correlate, String doNormalTest, String doBoxM, String doSphericity, String saveSSH, String saveSSE) {
        String yVarsVector = this.inputTransform.createRVector(yVars);
        repVar = repVar == null ? "NULL" : "\"" + repVar + "\"";
        String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip = TRUE, sep = \",\")";
        String outFilePath = String.valueOf(outputPath) + "MANOVAOutput.txt";
        String removeObjects = "rm(list=ls())";
        String model = "modelLHS = paste(\"cbind(\", paste(" + yVarsVector + ", collapse=\", \"), \")\", sep = \"\")";
        String model2 = "modelRHS = paste(\"" + factorVar + "\", sep = \"\")";
        String model3 = "if (!is.null(" + repVar + ")) modelRHS = paste(" + repVar + ", \"" + factorVar + "\", sep = \" + \")";
        String model4 = "mymodel = paste(modelLHS, \" ~ \", modelRHS, sep = \"\")";
        String sinkIn = "sink(\"" + outFilePath + "\")";
        String funcStmt = "result <- try(";
        String command = "MAOVTest(data, \"" + outputPath + "\", " + yVarsVector + ", \"" + factorVar + "\", " + repVar + ", \"" + testStat + "\"," + "mymodel, " + descriptive + ", " + correlate + ", " + doNormalTest + ", " + doBoxM + ", " + doSphericity + ", " + saveSSH + ", " + saveSSE + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(model);
        System.out.println(model2);
        System.out.println(model3);
        System.out.println(model4);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(removeObjects);
        this.rEngine.eval(readData);
        this.rEngine.eval(model);
        this.rEngine.eval(model2);
        this.rEngine.eval(model3);
        this.rEngine.eval(model4);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in MAOVTest:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void doMDS(String dataFileName, String outputPath, String inputType, String[] vars, String idVar, String type, String distClass, String distNonmet, int dimnum, boolean useIdVar, int[] pChars, double[] pSizes, String[] pCol, boolean showLeg, String legTitle, String legPos, int legNcol, boolean descriptive, boolean correlate) {
        String varsVector = this.inputTransform.createRVector(vars);
        String pCharsVector = this.inputTransform.createRNumVector(pChars);
        String pSizesVector = this.inputTransform.createRNumVector(pSizes);
        String pColVector = this.inputTransform.createRColorRGBVector(pCol);
        String distOne = null;
        distOne = distClass.equals("NULL") ? distClass : "\"" + distClass + "\"";
        String distTwo = null;
        distTwo = distNonmet.equals("NULL") ? distNonmet : "\"" + distNonmet + "\"";
        String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip = TRUE, sep = \",\")";
        String outFilePath = String.valueOf(outputPath) + "MDSOutput.txt";
        String removeObjects = "rm(list=ls())";
        String sinkIn = "sink(\"" + outFilePath + "\")";
        String funcStmt = "result <- try(";
        String command = "MDS(data, \"" + outputPath + "\", \"" + inputType + "\", " + varsVector + ", \"" + idVar + "\", \"" + type + "\", " + distOne + ", " + distTwo + ", " + dimnum + ", " + String.valueOf(useIdVar).toUpperCase() + ", " + pCharsVector + ", " + pSizesVector + ", " + pColVector + ", " + String.valueOf(showLeg).toUpperCase() + ", \"" + legTitle + "\", \"" + legPos + "\", " + legNcol + ", " + String.valueOf(descriptive).toUpperCase() + ", " + String.valueOf(correlate).toUpperCase() + ")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(removeObjects);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in MDS:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    public void doDiscriminant(String dataFileName, String outputPath, String[] vars, String grpVar, boolean descriptive, boolean doNormalTest, boolean doBoxM, String priorProb, boolean classifyNew, String dataFileName2) {
        String varsVector = this.inputTransform.createRVector(vars);
        String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip = TRUE, sep = \",\")";
        String readData2 = "data2 <- NULL";
        if (classifyNew) {
            readData2 = "data2 <- read.csv(\"" + dataFileName2 + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip = TRUE, sep = \",\")";
        }
        String outFilePath = String.valueOf(outputPath) + "DiscriminantOutput.txt";
        String prior = null;
        prior = priorProb.equals("NULL") ? priorProb : "\"" + priorProb + "\"";
        String removeObjects = "rm(list=ls())";
        String sinkIn = "sink(\"" + outFilePath + "\")";
        String funcStmt = "result <- try(";
        String command = "Discriminant(data, \"" + outputPath + "\", " + varsVector + ", \"" + grpVar + "\", " + String.valueOf(descriptive).toUpperCase() + ", " + String.valueOf(doNormalTest).toUpperCase() + ", " + String.valueOf(doBoxM).toUpperCase() + ", " + prior + ", " + String.valueOf(classifyNew).toUpperCase() + ", data2)";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String pkgName = "cat(\"Statistical Tool for Agricultural Research (STAR)\\n\", date(),\"\\n\\n\", sep = \"\")";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(readData2);
        System.out.println(sinkIn);
        System.out.println(pkgName);
        System.out.println(funcStmt);
        this.rEngine.eval(removeObjects);
        this.rEngine.eval(readData);
        this.rEngine.eval(readData2);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgName);
        this.rEngine.eval(funcStmt);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in Discriminant:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
            this.rEngine.eval(sinkOut);
        } else {
            System.out.println(sinkOut);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }
}

