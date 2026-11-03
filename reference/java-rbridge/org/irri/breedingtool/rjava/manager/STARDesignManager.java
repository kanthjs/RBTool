/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import org.irri.breedingtool.rjava.manager.api.IRJavaSTARDesignManager;
import org.irri.breedingtool.rjava.utilities.InputTransform;
import org.rosuda.JRI.Rengine;

public class STARDesignManager
implements IRJavaSTARDesignManager {
    private Rengine rEngine;
    private InputTransform inputTransform;
    private StringBuilder rscriptCommand;

    public STARDesignManager(Rengine rEngine) {
        this.rEngine = rEngine;
        this.inputTransform = new InputTransform();
    }

    private void rEngineEnd() {
        String rm = "rm(list=ls(all=TRUE))";
        this.rEngine.eval(rm);
        this.rEngine.end();
    }

    @Override
    public void doDesignCRD(String path, String fieldBookName, String[] factorName, String[] factorID, Integer[] factorLevel, Integer rep, Integer trial, Integer numFieldRow, String fieldOrder) {
        String inputList = this.inputTransform.createRList(factorName, factorLevel, factorID);
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designCRD(" + inputList + ", r = " + rep + ", trial = " + trial + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designCRD:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Completely Randomized Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum[[i]], RowLabel = NULL, ColLabel = NULL, title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignRCBD(String path, String fieldBookName, String[] factorName, String[] factorID, Integer[] factorLevel, Integer blk, Integer trial, Integer numFieldRow, Integer rowPerBlk, String fieldOrder) {
        String inputList = this.inputTransform.createRList(factorName, factorLevel, factorID);
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designRCBD(generate = " + inputList + ", r = " + blk + ", trial = " + trial + ", numFieldRow = " + numFieldRow + ", rowPerBlk = " + rowPerBlk;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = String.valueOf(command) + ", display = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designRCBD:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Randomized Complete Block Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignLSD(String path, String fieldBookName, String[] factorName, String[] factorID, Integer[] factorLevel, Integer trial, String fieldOrder) {
        String inputList = this.inputTransform.createRList(factorName, factorLevel, factorID);
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designLSD(" + inputList + ", trial = " + trial;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designLSD:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Latin Square Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum[[i]], RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignSplit(String path, String fieldBookName, String main, String sub, String ssub, String sssub, String[] factorID, Integer[] factorLevel, Integer rep, Integer trial, String design, Integer numFieldRow, Integer rowPerBlk, Integer rowPerMain, Integer rowPerSub, Integer rowPerSubSub, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designSplit(main = list(" + main + " = paste(\"" + factorID[0] + "\", paste(1:" + factorLevel[0] + "), sep = \"\"))";
        command = String.valueOf(command) + ", sub = list(" + sub + " = paste(\"" + factorID[1] + "\", paste(1:" + factorLevel[1] + "), sep = \"\"))";
        if (ssub != null) {
            command = String.valueOf(command) + ", ssub = list(" + ssub + " = paste(\"" + factorID[2] + "\", paste(1:" + factorLevel[2] + "), sep = \"\"))";
        }
        if (sssub != null) {
            command = String.valueOf(command) + ", sssub = list(" + sssub + " = paste(\"" + factorID[3] + "\", paste(1:" + factorLevel[3] + "), sep = \"\"))";
        }
        if (design != "lsd") {
            command = String.valueOf(command) + ", r = " + rep;
        }
        command = String.valueOf(command) + ", trial = " + trial + ", design = \"" + design + "\"";
        if (design != "lsd") {
            command = String.valueOf(command) + ", numFieldRow = " + numFieldRow;
        }
        if (design == "rcbd") {
            command = String.valueOf(command) + ", rowPerBlk = " + rowPerBlk;
        }
        command = String.valueOf(command) + ", rowPerMain = " + rowPerMain;
        if (ssub != null) {
            command = String.valueOf(command) + ", rowPerSub = " + rowPerSub;
        }
        if (sssub != null) {
            command = String.valueOf(command) + ", rowPerSubSub = " + rowPerSubSub;
        }
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designSplit:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for \")\n";
            if (ssub == null && sssub == null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Split Plot in \")\n";
            }
            if (ssub != null && sssub == null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Split-Split Plot in \")\n";
            }
            if (ssub != null && sssub != null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Split-Split-Split Plot in \")\n";
            }
            if (design == "crd") {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Completely Randomized Design: \",\"\\n\\n\")\n";
            }
            if (design == "rcbd") {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Randomized Complete Block Design: \",\"\\n\\n\")\n";
            }
            if (design == "lsd") {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"Latin Square Design: \",\"\\n\\n\")\n";
            }
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignStrip(String path, String fieldBookName, String vertical, String horizontal, String sub, String ssub, String[] factorID, Integer[] factorLevel, Integer rep, Integer trial, Integer numFieldRow, Integer rowPerMain, Integer rowPerSub, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designStrip(vertical = list(" + vertical + " = paste(\"" + factorID[0] + "\", paste(1:" + factorLevel[0] + "), sep = \"\"))";
        command = String.valueOf(command) + ", horizontal = list(" + horizontal + " = paste(\"" + factorID[1] + "\", paste(1:" + factorLevel[1] + "), sep = \"\"))";
        if (sub != null) {
            command = String.valueOf(command) + ", sub = list(" + sub + " = paste(\"" + factorID[2] + "\", paste(1:" + factorLevel[2] + "), sep = \"\"))";
        }
        if (ssub != null) {
            command = String.valueOf(command) + ", ssub = list(" + ssub + " = paste(\"" + factorID[3] + "\", paste(1:" + factorLevel[3] + "), sep = \"\"))";
        }
        command = String.valueOf(command) + ", r = " + rep + ", trial = " + trial + ", numFieldRow = " + numFieldRow;
        if (sub != null) {
            command = String.valueOf(command) + ", rowPerMain = " + rowPerMain;
        }
        if (ssub != null) {
            command = String.valueOf(command) + ", rowPerSub = " + rowPerSub;
        }
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designStrip:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            if (sub == null && ssub == null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Strip Plot:\",\"\\n\\n\")\n";
            }
            if (sub != null && ssub == null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Strip-Split Plot:\",\"\\n\\n\")\n";
            }
            if (sub != null && ssub != null) {
                checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Strip-Split-Split Plot:\",\"\\n\\n\")\n";
            }
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignBIBD(String path, String fieldBookName, Integer numTrmt, Integer blkSize, Integer trial, Integer numFieldRow, Integer rowPerBlk, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designBIBD(generate = list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", blkSize = " + blkSize + ", trial = " + trial + ", numFieldRow = " + numFieldRow + ", rowPerBlk = " + rowPerBlk;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = String.valueOf(command) + ", display = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designBIBD:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Balanced Incomplete Block Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAug(String dataFileName, String outFileName, Integer repTrmt, Integer unrepTrmt, Integer rep, Integer trial, String design) {
        this.rscriptCommand = new StringBuilder();
        String sinkIn = "sink(\"" + outFileName + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAugmented(check = " + repTrmt + ", newTrmt = " + unrepTrmt;
        command = design == "rcbd" ? String.valueOf(command) + ", r = " + rep + ", trial = " + trial + ", design = \"" + design + "\", file = \"" + dataFileName + "\")" : String.valueOf(command) + ", trial = " + trial + ", design = \"" + design + "\", file = \"" + dataFileName + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAugmented:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAugLSD(String path, String fieldBookName, Integer numCheck, Integer numNew, String trmtName, Integer trial, Integer numFieldRow, String fieldOrder, String trmtLabel, String[] checkTrmt, String[] newTrmt) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAugmentedLSD(check = " + numCheck + ", newTrmt = " + numNew;
        command = trmtName == null ? String.valueOf(command) + ", trmtName = NULL" : String.valueOf(command) + ", trmtName = \"" + trmtName + "\"";
        command = String.valueOf(command) + ", trial = " + trial + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = trmtLabel == null ? String.valueOf(command) + ", trmtLabel = NULL" : String.valueOf(command) + ", trmtLabel = \"" + trmtLabel + "\"";
        command = checkTrmt == null ? String.valueOf(command) + ", checkTrmt = NULL" : String.valueOf(command) + ", checkTrmt = " + this.inputTransform.createRVector(checkTrmt);
        command = newTrmt == null ? String.valueOf(command) + ", newTrmt = NULL" : String.valueOf(command) + ", newTrmt = " + this.inputTransform.createRVector(newTrmt);
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAugmentedLSD:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Augmented Latin Square Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum[[i]], RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAugRCB(String path, String fieldBookName, Integer numCheck, Integer numNew, String trmtName, Integer Blk, Integer trial, Integer rowPerBlk, Integer numFieldRow, String fieldOrder, String trmtLabel, String[] checkTrmt, String[] newTrmt) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAugmentedRCB(numCheck = " + numCheck + ", numNew = " + numNew;
        command = trmtName == null ? String.valueOf(command) + ", trmtName = NULL" : String.valueOf(command) + ", trmtName = \"" + trmtName + "\"";
        command = String.valueOf(command) + ", r = " + Blk + ", trial = " + trial + ", rowPerBlk = " + rowPerBlk + " , numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = trmtLabel == null ? String.valueOf(command) + ", trmtLabel = NULL" : String.valueOf(command) + ", trmtLabel = \"" + trmtLabel + "\"";
        command = checkTrmt == null ? String.valueOf(command) + ", checkTrmt = NULL" : String.valueOf(command) + ", checkTrmt = " + this.inputTransform.createRVector(checkTrmt);
        command = newTrmt == null ? String.valueOf(command) + ", newTrmt = NULL" : String.valueOf(command) + ", newTrmt = " + this.inputTransform.createRVector(newTrmt);
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAugmentedRCBD:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignLattice(String path, String fieldBookName, Integer numTrmt, Integer rep, Integer trial, Integer numFieldRow, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designLattice(list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", r = " + rep + ", trial = " + trial + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designLattice:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAugmentedAlpha(String path, String fieldBookName, Integer numCheck, Integer numNew, String trmtName, Integer blkSize, Integer rep, Integer trial, Integer rowPerBlk, Integer rowPerRep, Integer numFieldRow, String fieldOrder, String trmtLabel, String[] checkTrmt, String[] newTrmt) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAugmentedAlpha(numCheck = " + numCheck + ", numNew = " + numNew;
        command = trmtName == null ? String.valueOf(command) + ", trmtName = NULL" : String.valueOf(command) + ", trmtName = \"" + trmtName + "\"";
        command = String.valueOf(command) + ", blksize = " + blkSize + ", r = " + rep + ", trial = " + trial;
        command = String.valueOf(command) + ", rowPerBlk = " + rowPerBlk + ", rowPerRep = " + rowPerRep + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = trmtLabel == null ? String.valueOf(command) + ", trmtLabel = NULL" : String.valueOf(command) + ", trmtLabel = \"" + trmtLabel + "\"";
        command = checkTrmt == null ? String.valueOf(command) + ", checkTrmt = NULL" : String.valueOf(command) + ", checkTrmt = " + this.inputTransform.createRVector(checkTrmt);
        command = newTrmt == null ? String.valueOf(command) + ", newTrmt = NULL" : String.valueOf(command) + ", newtrmt = " + this.inputTransform.createRVector(newTrmt);
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAlphaLattice:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignRowColumn(String path, String fieldBookName, Integer numTrmt, Integer rep, Integer trial, Integer rowPerRep, Integer numFieldRow, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designRowColumn(list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", r = " + rep + ", trial = " + trial;
        command = String.valueOf(command) + ", rowPerRep = " + rowPerRep + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designRowColumn:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Row-Column Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignLatinizedAlpha(String path, String fieldBookName, Integer numTrmt, Integer blkSize, Integer rep, Integer trial, Integer numFieldRow, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designLatinizedAlpha(generate = list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", blksize = " + blkSize + ", r = " + rep + ", trial = " + trial + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAlphaLattice:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Latinized Alpha Lattice Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignLatinizedRowColumn(String path, String fieldBookName, Integer numTrmt, Integer rep, Integer trial, Integer rowPerRep, Integer numFieldRow, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designLatinizedRowCol(list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", r = " + rep + ", trial = " + trial;
        command = String.valueOf(command) + ", rowPerRep = " + rowPerRep + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designLatinizedRowCol:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Latinized Row-Column Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    public StringBuilder getRscriptCommand() {
        return this.rscriptCommand;
    }

    @Override
    public void doDesignPRep(String path, String fieldBookName, String[] trmtGrpName, Integer[] numTrmtPerGrp, Integer[] trmtRepPerGrp, String trmtName, Integer trial, Integer numFieldRow, String fieldOrder, String trmtLabel, String[] trmtListPerGrp) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designPRep(trmtPerGrp = " + this.inputTransform.createRList(trmtGrpName, numTrmtPerGrp);
        command = String.valueOf(command) + ", trmtRepPerGrp = " + this.inputTransform.createRNumVector(trmtRepPerGrp);
        command = String.valueOf(command) + ", trmtName = \"" + trmtName + "\", trial = " + trial;
        command = String.valueOf(command) + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = trmtLabel == null ? String.valueOf(command) + ", trmtLabel = NULL" : String.valueOf(command) + ", trmtLabel = \"" + trmtLabel + "\"";
        command = trmtListPerGrp == null ? String.valueOf(command) + ", trmtListPerGrp = NULL" : String.valueOf(command) + ", trmtListPerGrp = " + this.inputTransform.createRVector(trmtListPerGrp);
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAlphaLattice:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAlpha(String path, String fieldBookName, Integer numTrmt, Integer blkSize, Integer rep, Integer trial, Integer rowPerBlk, Integer rowPerRep, Integer numFieldRow, String fieldOrder) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAlphaLattice(generate = list(Treatment = paste(\"T\", paste(1:" + numTrmt + "), sep = \"\"))";
        command = String.valueOf(command) + ", blksize = " + blkSize + ", r = " + rep + ", trial = " + trial;
        command = String.valueOf(command) + ", rowPerBlk = " + rowPerBlk + ", rowPerRep = " + rowPerRep + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE, file = \"" + CSVOutput + "\")" : String.valueOf(command) + ", serpentine = TRUE, file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designAlphaLattice:\\n\",msg, sep = \"\")";
            System.out.println(errorMsg1);
            System.out.println(errorMsg2);
            System.out.println(errorMsg3);
            System.out.println(errorMsg4);
            this.rEngine.eval(errorMsg1);
            this.rEngine.eval(errorMsg2);
            this.rEngine.eval(errorMsg3);
            this.rEngine.eval(errorMsg4);
        } else {
            String checkOutput = "if (nrow(result$fieldbook) != 0) {\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\nLayout for Alpha Lattice Design:\",\"\\n\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    for (i in (1:length(result$layout))) { \n";
            checkOutput = String.valueOf(checkOutput) + "          printLayout(result$layout[[i]], result$plotNum, RowLabel = rownames(result$layout[[i]]), ColLabel = colnames(result$layout[[i]]), title = paste(\"Trial = \", i, sep = \"\"))\n";
            checkOutput = String.valueOf(checkOutput) + "          cat(\"\\n\")\n";
            checkOutput = String.valueOf(checkOutput) + "    }\n";
            checkOutput = String.valueOf(checkOutput) + "    cat(\"\\n\",\"**Note: Cells contain plot numbers on top, treatments/entries below\")\n";
            checkOutput = String.valueOf(checkOutput) + "}";
            System.out.println(checkOutput);
            this.rEngine.eval(checkOutput);
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }

    @Override
    public void doDesignAugmentedRowColumn(String path, String fieldBookName, Integer numCheck, Integer numNew, String trmtName, Integer rep, Integer trial, Integer rowblkPerRep, Integer rowPerRowblk, Integer numFieldRow, String fieldOrder, String trmtLabel, String[] checkTrmt, String[] newTrmt) {
        this.rscriptCommand = new StringBuilder();
        String CSVOutput = String.valueOf(path) + fieldBookName + ".csv";
        String TxtOuptut = String.valueOf(path) + fieldBookName + ".txt";
        String sinkIn = "sink(\"" + TxtOuptut + "\")";
        String pkgIntro = "cat(\"Statistical Tool for Agricultural Research (STAR)\\nResult of Randomization\\n\",date(),\"\\n\\n\\n\", sep = \"\")";
        String funcRandomize = "result <- try(";
        String command = "designAugmentedRowColumn(numCheck = " + numCheck + ", numNew = " + numNew;
        command = trmtName == null ? String.valueOf(command) + ", trmtName = NULL, r = " + rep + ", trial = " + trial : String.valueOf(command) + ", trmtName = \"" + trmtName + "\", r = " + rep + ", trial = " + trial;
        command = String.valueOf(command) + ", rowblkPerRep = " + rowblkPerRep + ", rowPerRowblk = " + rowPerRowblk + ", numFieldRow = " + numFieldRow;
        command = fieldOrder == "Plot Order" ? String.valueOf(command) + ", serpentine = FALSE" : String.valueOf(command) + ", serpentine = TRUE";
        command = trmtLabel == null ? String.valueOf(command) + ", trmtLabel = NULL" : String.valueOf(command) + ", trmtLabel = \"" + trmtLabel + "\"";
        command = checkTrmt == null ? String.valueOf(command) + ", checkTrmt = NULL" : String.valueOf(command) + ", checkTrmt = " + this.inputTransform.createRVector(checkTrmt);
        command = newTrmt == null ? String.valueOf(command) + ", newTrmt = NULL" : String.valueOf(command) + ", newtrmt = " + this.inputTransform.createRVector(newTrmt);
        command = String.valueOf(command) + ", file = \"" + CSVOutput + "\")";
        funcRandomize = String.valueOf(funcRandomize) + command + ", silent = TRUE)";
        System.out.println(sinkIn);
        System.out.println(pkgIntro);
        System.out.println(funcRandomize);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(pkgIntro);
        this.rEngine.eval(funcRandomize);
        String runSuccessCommand = this.rEngine.eval("class(result)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String errorMsg1 = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
            String errorMsg2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
            String errorMsg3 = "msg <- gsub(\"\\\"\", \"\", msg)";
            String errorMsg4 = "cat(\"Error in designRowColumn:\\n\",msg, sep = \"\")";
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
        this.rscriptCommand.append(String.valueOf(command) + "\n");
        this.rEngineEnd();
    }
}

