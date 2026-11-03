/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import org.irri.breedingtool.rjava.manager.api.IRJavaPBToolsAnalysisManager;
import org.irri.breedingtool.rjava.utilities.InputTransform;
import org.rosuda.JRI.Rengine;

public class PBToolAnalysisManager
implements IRJavaPBToolsAnalysisManager {
    private Rengine rEngine;
    private InputTransform inputTransform;

    public PBToolAnalysisManager(Rengine rEngine) {
        this.rEngine = rEngine;
        this.inputTransform = new InputTransform();
    }

    private void rEngineEnd() {
        String rm = "rm(list=ls(all=TRUE))";
        this.rEngine.eval(rm);
        this.rEngine.end();
    }

    @Override
    public void doNC1Test(String dataFileName, String outFileName, String design, String[] respvar, String female, String male, String rep, String block, String row, String column, String inbred, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc1Test = null;
        if (design == "CRD") {
            funcNc1Test = environment == "NULL" ? "result<-try(nc1Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc1Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc1Test = environment == "NULL" ? "result<-try(nc1Test(\"RCB\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc1Test(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", female =\"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc1Test = environment == "NULL" ? "result<-try(nc1Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc1Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc1Test = environment == "NULL" ? "result<-try(nc1Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc1Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc1Test function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc1Test);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc1Test);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNC1METest(String dataFileName, String outFileName, String design, String[] respvar, String female, String male, String rep, String block, String row, String column, String inbred, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc1TestME = null;
        if (design == "CRD") {
            funcNc1TestME = "result<-try(nc1TestME(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc1TestME = "result<-try(nc1TestME(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", female =\"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc1TestME = "result<-try(nc1TestME(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc1TestME = "result<-try(nc1TestME(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc1TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc1TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc1TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNC2Test(String dataFileName, String outFileName, String design, String[] respvar, String female, String male, String rep, String block, String row, String column, String inbred, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc2Test = null;
        if (design == "CRD") {
            funcNc2Test = environment == "NULL" ? "result<-try(nc2Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc2Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc2Test = environment == "NULL" ? "result<-try(nc2Test(\"RCB\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc2Test(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", female =\"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc2Test = environment == "NULL" ? "result<-try(nc2Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc2Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc2Test = environment == "NULL" ? "result<-try(nc2Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc2Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc2Test function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc2Test);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc2Test);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNC2METest(String dataFileName, String outFileName, String design, String[] respvar, String female, String male, String rep, String block, String row, String column, String inbred, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc2TestME = null;
        if (design == "CRD") {
            funcNc2TestME = "result<-try(nc2TestME(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = NULL, block = NULL, row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc2TestME = "result<-try(nc2TestME(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", female =\"" + female + "\", male = \"" + male + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc2TestME = "result<-try(nc2TestME(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc2TestME = "result<-try(nc2TestME(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", female = \"" + female + "\", male = \"" + male + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc2TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc2TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc2TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNC3Test(String dataFileName, String outFileName, String design, String[] respvar, String tester, String f2lines, String rep, String block, String row, String column, String inbred, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc3Test = null;
        if (design == "CRD") {
            funcNc3Test = environment == "NULL" ? "result<-try(nc3Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc3Test(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc3Test = environment == "NULL" ? "result<-try(nc3Test(\"RCB\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc3Test(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", tester =\"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc3Test = environment == "NULL" ? "result<-try(nc3Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc3Test(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc3Test = environment == "NULL" ? "result<-try(nc3Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = " + environment + "), silent = TRUE)" : "result<-try(nc3Test(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc3Test function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc3Test);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc3Test);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doNC3METest(String dataFileName, String outFileName, String design, String[] respvar, String tester, String f2lines, String rep, String block, String row, String column, String individual, String environment) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcNc3TestME = null;
        if (design == "CRD") {
            funcNc3TestME = "result<-try(nc3TestME(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RCB") {
            funcNc3TestME = "result<-try(nc3TestME(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", tester =\"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcNc3TestME = "result<-try(nc3TestME(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcNc3TestME = "result<-try(nc3TestME(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = \"" + environment + "\"), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in nc3TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNc3TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcNc3TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }

    @Override
    public void doTTCTest(String dataFileName, String outFileName, String design, String[] respvar, String tester, String f2lines, String rep, String block, String row, String column, String individual, String environment, String codeP1, String codeP2, String codeF1, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = "capture.output(cat(\"\nDESIGN: TRIPLE TEST CROSS (NO PARENTS) IN " + designName + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcTtcTest = null;
            if (design == "CRD") {
                funcTtcTest = environment == "NULL" ? "result<-try(ttcTest(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = " + environment + ", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)" : "result<-try(ttcTest(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcTtcTest = environment == "NULL" ? "result<-try(ttcTest(\"RCB\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = " + environment + ", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)" : "result<-try(ttcTest(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", tester =\"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcTtcTest = environment == "NULL" ? "result<-try(ttcTest(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = " + environment + ", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)" : "result<-try(ttcTest(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcTtcTest = environment == "NULL" ? "result<-try(ttcTest(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = " + environment + ", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)" : "result<-try(ttcTest(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcTtcTest);
            System.out.println(funcTtcTest);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("ttcTest: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in ttcTest function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String getEnvLevels = "length(result$output[[" + i + "]]$site)";
                    int envLevelsLength = this.rEngine.eval(getEnvLevels).asInt();
                    int m = 0;
                    while (m < envLevelsLength) {
                        String manyNA1;
                        String manyNA2;
                        int j = m + 1;
                        if (environment != "NULL") {
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,result$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        if ((manyNA2 = this.rEngine.eval(manyNA1 = "result$output[[" + i + "]]$site[[" + j + "]]$tooManyNAWarning").asString()).equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialSumTable = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(trialSumHead);
                            this.rEngine.eval(trialSumTable);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(trialObsRead);
                            this.rEngine.eval(trialObsUsed);
                            String exceeded = "result$output[[" + i + "]]$site[[" + j + "]]$exceededWarning";
                            String exceededCheck = this.rEngine.eval(exceeded).asString();
                            this.rEngine.eval(exceeded);
                            this.rEngine.eval(exceededCheck);
                            if (exceededCheck.equals("YES")) {
                                String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(warningExceed);
                            } else {
                                String crossEffect2;
                                double responseRate = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
                                if (design == "CRD" || design == "RCB") {
                                    String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaHead);
                                    if (responseRate < 0.9) {
                                        String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(warningExceed);
                                    } else {
                                        String ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$ttc.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                    }
                                }
                                String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + tester + ":" + f2lines + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(crossEffect1);
                                Double pvalue = 0.0;
                                if (design == "CRD") {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String crossEffect3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$testerF2.test,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    this.rEngine.eval(crossEffect3);
                                    pvalue = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$pValue").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                } else {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$site[[" + j + "]]$formula1), data = result$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model1b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outAnovaTable10);
                                }
                                double alphaNew = Double.parseDouble(alpha);
                                if (pvalue < alphaNew) {
                                    String epistasis1 = "capture.output(cat(\"\n\nANOVA FOR TESTING EPISTASIS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(epistasis1);
                                    if (responseRate < 0.9) {
                                        String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform test for epistasis and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(warningExceed);
                                        this.rEngine.eval(outspace);
                                    } else {
                                        String ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$epistasis.test,file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                        String anovaTable1 = "capture.output(cat(\"\n\nANOVA TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String anovaTable2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$sd.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        String anovaTable3 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String anovaTable4 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(anovaTable1);
                                        this.rEngine.eval(anovaTable2);
                                        this.rEngine.eval(anovaTable3);
                                        this.rEngine.eval(anovaTable4);
                                        String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String genVar2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genVar,file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(genVar1);
                                        this.rEngine.eval(genVar2);
                                        this.rEngine.eval(outspace);
                                        this.rEngine.eval(outspace);
                                    }
                                } else {
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                        ++m;
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doTTCMETest(String dataFileName, String outFileName, String design, String[] respvar, String tester, String f2lines, String rep, String block, String row, String column, String individual, String environment, String codeP1, String codeP2, String codeF1, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = "capture.output(cat(\"\nMULTIPLE ENVIRONMENT ANALYSIS\n\nDESIGN: TRIPLE TEST CROSS (NO PARENTS) IN " + designName + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcTtcTestME = null;
            if (design == "CRD") {
                funcTtcTestME = "result<-try(ttcTestME(\"CRD\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = NULL, row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcTtcTestME = "result<-try(ttcTestME(\"RCB\", \"dataRead\", respvar =" + respvarVector + ", tester =\"" + tester + "\", f2lines = \"" + f2lines + "\", rep = NULL, block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcTtcTestME = "result<-try(ttcTestME(\"Alpha\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcTtcTestME = "result<-try(ttcTestME(\"RowColumn\", \"dataRead\", respvar = " + respvarVector + ", tester = \"" + tester + "\", f2lines = \"" + f2lines + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", individual = NULL, environment = \"" + environment + "\", codeP1 = \"" + codeP1 + "\", codeP2 = \"" + codeP2 + "\", codeF1 = \"" + codeF1 + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcTtcTestME);
            System.out.println(funcTtcTestME);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("ttcTest: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in ttcTest function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String manyNA1 = "result$output[[" + i + "]]$tooManyNAWarning";
                    String manyNA2 = this.rEngine.eval(manyNA1).asString();
                    if (manyNA2.equals("YES")) {
                        String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(warningExceed);
                    } else {
                        String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        String trialSumTable = "capture.output(result$output[[" + i + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                        String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(trialSumHead);
                        this.rEngine.eval(trialSumTable);
                        this.rEngine.eval(outspace);
                        this.rEngine.eval(trialObsRead);
                        this.rEngine.eval(trialObsUsed);
                        String exceeded = "result$output[[" + i + "]]$exceededWarning";
                        String exceededCheck = this.rEngine.eval(exceeded).asString();
                        this.rEngine.eval(exceeded);
                        this.rEngine.eval(exceededCheck);
                        if (exceededCheck.equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            double responseRate = this.rEngine.eval("result$output[[" + i + "]]$responseRate").asDouble();
                            if (design == "CRD" || design == "RCB") {
                                String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(anovaHead);
                                if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                } else {
                                    String ttcAnova1 = "capture.output(result$output[[" + i + "]]$ttc.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(ttcAnova1);
                                    this.rEngine.eval(ttcAnova2);
                                    this.rEngine.eval(ttcAnova3);
                                }
                            }
                            String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + tester + ":" + f2lines + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(crossEffect1);
                            Double pvalue = 0.0;
                            String crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(crossEffect2);
                            String outAnovaTable2 = "library(lmerTest)";
                            String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$formula1), data = result$output[[" + i + "]]$data, REML = T)";
                            String outAnovaTable4 = "a.table <- anova(model1b)";
                            String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                            String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                            String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                            String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable10 = "detach(\"package:lmerTest\")";
                            this.rEngine.eval(outAnovaTable2);
                            this.rEngine.eval(outAnovaTable3);
                            this.rEngine.eval(outAnovaTable4);
                            pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                            System.out.println("pvalue: " + pvalue);
                            this.rEngine.eval(outAnovaTable5);
                            this.rEngine.eval(outAnovaTable6);
                            this.rEngine.eval(outAnovaTable7);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outAnovaTable8);
                            this.rEngine.eval(outAnovaTable9);
                            this.rEngine.eval(outAnovaTable10);
                            double alphaNew = Double.parseDouble(alpha);
                            if (pvalue < alphaNew) {
                                String epistasis1 = "capture.output(cat(\"\n\nANOVA FOR TESTING EPISTASIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(epistasis1);
                                if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform test for epistasis and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                    this.rEngine.eval(outspace);
                                } else {
                                    String ttcAnova1 = "capture.output(result$output[[" + i + "]]$epistasis.test,file=\"" + outFileName + "\",append = TRUE)";
                                    String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(ttcAnova1);
                                    this.rEngine.eval(ttcAnova2);
                                    this.rEngine.eval(ttcAnova3);
                                    String anovaTable1 = "capture.output(cat(\"\n\nANOVA TABLE:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaTable2 = "capture.output(result$output[[" + i + "]]$sd.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaTable3 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaTable4 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$anovaRemark,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaTable1);
                                    this.rEngine.eval(anovaTable2);
                                    this.rEngine.eval(anovaTable3);
                                    this.rEngine.eval(anovaTable4);
                                    String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String genVar2 = "capture.output(result$output[[" + i + "]]$genVar,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(genVar1);
                                    this.rEngine.eval(genVar2);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel1Test(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = null;
            usedDesign = cross == "TRUE" ? "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD I IN " + designName + " (CROSS)\n\n\"),file=\"" + outFileName + "\",append = TRUE)" : "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD I IN " + designName + " (SELF)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcDiallel1Test = null;
            if (design == "CRD") {
                funcDiallel1Test = environment == "NULL" ? "result<-try(diallel1Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel1Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcDiallel1Test = environment == "NULL" ? "result<-try(diallel1Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel1Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcDiallel1Test = environment == "NULL" ? "result<-try(diallel1Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel1Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcDiallel1Test = environment == "NULL" ? "result<-try(diallel1Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel1Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcDiallel1Test);
            System.out.println(funcDiallel1Test);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("diallel1Test: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in diallel1Test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String getEnvLevels = "length(result$output[[" + i + "]]$site)";
                    int envLevelsLength = this.rEngine.eval(getEnvLevels).asInt();
                    int m = 0;
                    while (m < envLevelsLength) {
                        int nLevelsRep;
                        int maxLengthPerCross;
                        int j = m + 1;
                        if (environment != "NULL") {
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,result$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        if ((maxLengthPerCross = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$maxLengthPerCross").asInt()) > (nLevelsRep = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$nlevelsRep").asInt())) {
                            String errorMessage1 = "capture.output(cat(\"\nERROR: \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError),file=\"" + outFileName + "\",append = TRUE)";
                            String errorMessage2 = "capture.output(cat(\"\n       \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError2),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(errorMessage1);
                            this.rEngine.eval(errorMessage2);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            break;
                        }
                        String manyNA1 = "result$output[[" + i + "]]$site[[" + j + "]]$tooManyNAWarning";
                        String manyNA2 = this.rEngine.eval(manyNA1).asString();
                        if (manyNA2.equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialSumTable = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(trialSumHead);
                            this.rEngine.eval(trialSumTable);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(trialObsRead);
                            this.rEngine.eval(trialObsUsed);
                            String exceeded = "result$output[[" + i + "]]$site[[" + j + "]]$exceededWarning";
                            String exceededCheck = this.rEngine.eval(exceeded).asString();
                            this.rEngine.eval(exceeded);
                            this.rEngine.eval(exceededCheck);
                            if (exceededCheck.equals("YES")) {
                                String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(warningExceed);
                            } else {
                                String meansComplete1;
                                String meansComplete2;
                                String crossEffect2;
                                double responseRate = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
                                if (design == "CRD" || design == "RCB") {
                                    String ttcAnova3;
                                    String ttcAnova2;
                                    String ttcAnova1;
                                    String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaHead);
                                    if (design == "CRD") {
                                        ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$diallel1.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                    }
                                    if (design == "RCB") {
                                        if (responseRate < 0.9) {
                                            String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningExceed);
                                        } else {
                                            ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$diallel1.anova,file=\"" + outFileName + "\",append = TRUE)";
                                            ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(ttcAnova1);
                                            this.rEngine.eval(ttcAnova2);
                                            this.rEngine.eval(ttcAnova3);
                                        }
                                    }
                                }
                                String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + p1 + ":" + p2 + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(crossEffect1);
                                Double pvalue = 0.0;
                                if (design == "CRD") {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String crossEffect3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genoEffect.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    this.rEngine.eval(crossEffect3);
                                    pvalue = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$pValue").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                } else if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform the analysis.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                } else {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$site[[" + j + "]]$formula1), data = result$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model1b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outAnovaTable10);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                }
                                if ((meansComplete2 = this.rEngine.eval(meansComplete1 = "result$output[[" + i + "]]$site[[" + j + "]]$meansComplete").asString()).equals("TRUE")) {
                                    double alphaNew = Double.parseDouble(alpha);
                                    if (pvalue < alphaNew) {
                                        String gcaAnovaHead = "capture.output(cat(\"\n\nANALYSIS OF VARIANCE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(gcaAnovaHead);
                                        if (responseRate >= 0.9 || design == "CRD") {
                                            String gcaAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.anova,file=\"" + outFileName + "\",append = TRUE)";
                                            String gcaAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String gcaAnova3 = "capture.output(cat(\"NOTE:\", result$output[[" + i + "]]$site[[" + j + "]]$gcasca.anovaRemark),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(gcaAnova1);
                                            this.rEngine.eval(gcaAnova2);
                                            this.rEngine.eval(gcaAnova3);
                                            String effects1 = "capture.output(cat(\"\n\nGENERAL COMBINING ABILITY EFFECTS (diagonal), SPECIFIC COMBINING\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects2 = "capture.output(cat(\"ABILITY EFFECTS (above diagonal) AND RECIPROCAL EFFECTS (below diagonal):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(effects1);
                                            this.rEngine.eval(effects2);
                                            this.rEngine.eval(effects3);
                                            String effectsFileName2 = null;
                                            effectsFileName2 = environment != "NULL" ? "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                            String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                            this.rEngine.eval(effectsFileName2);
                                            this.rEngine.eval(funcSaveEffectsCsv);
                                            String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                                            if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                                                System.out.println("save matrix of effects: error");
                                                String checkError = "msg <- trimStrings(strsplit(saveDataB2, \":\")[[1]])";
                                                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of effects to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkError);
                                                this.rEngine.eval(checkError2);
                                                this.rEngine.eval(checkError3);
                                                this.rEngine.eval(checkError4);
                                            }
                                            String varComp1 = "capture.output(cat(\"\n\nESTIMATES OF VARIANCE COMPONENTS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String varComp2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$var.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(varComp1);
                                            this.rEngine.eval(varComp2);
                                            String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String genVar2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genvar.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(genVar1);
                                            this.rEngine.eval(genVar2);
                                            String lsd1 = "capture.output(cat(\"\n\nTABLE OF STANDARD ERRORS AND LSDs:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String lsd2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$stderror.table,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(lsd1);
                                            this.rEngine.eval(lsd2);
                                            this.rEngine.eval(outspace);
                                            this.rEngine.eval(outspace);
                                        } else {
                                            String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform test for significance of GCA and SCA effects.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String warningExceed2 = "capture.output(cat(\"        and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningExceed);
                                            this.rEngine.eval(warningExceed2);
                                            this.rEngine.eval(outspace);
                                        }
                                    } else {
                                        this.rEngine.eval(outspace);
                                        this.rEngine.eval(outspace);
                                    }
                                } else if (design == "CRD") {
                                    String meansNotComplete = "capture.output(cat(\"\n\n***\nERROR: At least one required P1xP2 combination is missing. Cannot perform the succeeding analyses.\n***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(meansNotComplete);
                                } else {
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                        ++m;
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel1METest(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcDiallel1TestME = null;
        if (design == "CRD") {
            funcDiallel1TestME = "result<-try(diallel1TestME(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RCB") {
            funcDiallel1TestME = "result<-try(diallel1TestME(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcDiallel1TestME = "result<-try(diallel1TestME(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcDiallel1TestME = "result<-try(diallel1TestME(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in diallel1TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcDiallel1TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcDiallel1TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        String runSuccess = this.rEngine.eval("class(result)").asString();
        if (runSuccess != null && runSuccess.equals("try-error")) {
            System.out.println("results of diallel1TestME: error");
        } else {
            int k = 0;
            while (k < respvar.length) {
                int i = k + 1;
                String estimatedMissing1 = "result$output[[" + i + "]]$canEstimateMissing";
                String estimatedMissing2 = this.rEngine.eval(estimatedMissing1).asString();
                if (estimatedMissing2.equals("TRUE")) {
                    String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(meansFileName2);
                    this.rEngine.eval(funcSavePredMeansCsv);
                    System.out.println(meansFileName2);
                    System.out.println(funcSavePredMeansCsv);
                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                        System.out.println("save matrix of means: error");
                    }
                    String effectsFileName2 = "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(effectsFileName2);
                    this.rEngine.eval(funcSaveEffectsCsv);
                    System.out.println(effectsFileName2);
                    System.out.println(funcSaveEffectsCsv);
                    String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                    if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                        System.out.println("save matrix of effects: error");
                    }
                }
                ++k;
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel2Test(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = null;
            usedDesign = cross == "TRUE" ? "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD II IN " + designName + " (CROSS)\n\n\"),file=\"" + outFileName + "\",append = TRUE)" : "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD II IN " + designName + " (SELF)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcDiallel2Test = null;
            if (design == "CRD") {
                funcDiallel2Test = environment == "NULL" ? "result<-try(diallel2Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel2Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcDiallel2Test = environment == "NULL" ? "result<-try(diallel2Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel2Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcDiallel2Test = environment == "NULL" ? "result<-try(diallel2Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel2Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcDiallel2Test = environment == "NULL" ? "result<-try(diallel2Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel2Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcDiallel2Test);
            System.out.println(funcDiallel2Test);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("diallel2Test: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in diallel2Test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String getEnvLevels = "length(result$output[[" + i + "]]$site)";
                    int envLevelsLength = this.rEngine.eval(getEnvLevels).asInt();
                    int m = 0;
                    while (m < envLevelsLength) {
                        int nLevelsRep;
                        int maxLengthPerCross;
                        int j = m + 1;
                        if (environment != "NULL") {
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,result$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        if ((maxLengthPerCross = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$maxLengthPerCross").asInt()) > (nLevelsRep = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$nlevelsRep").asInt())) {
                            String errorMessage1 = "capture.output(cat(\"\nERROR: \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError),file=\"" + outFileName + "\",append = TRUE)";
                            String errorMessage2 = "capture.output(cat(\"\n       \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError2),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(errorMessage1);
                            this.rEngine.eval(errorMessage2);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            break;
                        }
                        String manyNA1 = "result$output[[" + i + "]]$site[[" + j + "]]$tooManyNAWarning";
                        String manyNA2 = this.rEngine.eval(manyNA1).asString();
                        if (manyNA2.equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialSumTable = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(trialSumHead);
                            this.rEngine.eval(trialSumTable);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(trialObsRead);
                            this.rEngine.eval(trialObsUsed);
                            String exceeded = "result$output[[" + i + "]]$site[[" + j + "]]$exceededWarning";
                            String exceededCheck = this.rEngine.eval(exceeded).asString();
                            this.rEngine.eval(exceeded);
                            this.rEngine.eval(exceededCheck);
                            if (exceededCheck.equals("YES")) {
                                String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(warningExceed);
                            } else {
                                String meansComplete1;
                                String meansComplete2;
                                String crossEffect2;
                                double responseRate = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
                                if (design == "RCB") {
                                    String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaHead);
                                    if (responseRate < 0.9) {
                                        String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(warningExceed);
                                    } else {
                                        String ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$diallel1.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                    }
                                }
                                String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + p1 + ":" + p2 + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(crossEffect1);
                                Double pvalue = 0.0;
                                if (design == "CRD") {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String crossEffect3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genoEffect.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    this.rEngine.eval(crossEffect3);
                                    pvalue = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$pValue").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                } else if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform the analysis.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                } else {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$site[[" + j + "]]$formula1), data = result$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model1b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outAnovaTable10);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                }
                                if ((meansComplete2 = this.rEngine.eval(meansComplete1 = "result$output[[" + i + "]]$site[[" + j + "]]$meansComplete").asString()).equals("TRUE")) {
                                    double alphaNew = Double.parseDouble(alpha);
                                    if (pvalue < alphaNew) {
                                        String gcaAnovaHead = "capture.output(cat(\"\n\nANALYSIS OF VARIANCE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(gcaAnovaHead);
                                        if (responseRate >= 0.9 || design == "CRD") {
                                            String gcaAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.anova,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(gcaAnova1);
                                            String effects1 = "capture.output(cat(\"\n\nGENERAL COMBINING ABILITY EFFECTS (diagonal), SPECIFIC COMBINING\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects2 = "capture.output(cat(\"ABILITY EFFECTS (above diagonal) AND RECIPROCAL EFFECTS (below diagonal):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(effects1);
                                            this.rEngine.eval(effects2);
                                            this.rEngine.eval(effects3);
                                            String effectsFileName2 = null;
                                            effectsFileName2 = environment != "NULL" ? "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                            String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                            this.rEngine.eval(effectsFileName2);
                                            this.rEngine.eval(funcSaveEffectsCsv);
                                            String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                                            if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                                                System.out.println("save matrix of effects: error");
                                                String checkError = "msg <- trimStrings(strsplit(saveDataB2, \":\")[[1]])";
                                                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of effects to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkError);
                                                this.rEngine.eval(checkError2);
                                                this.rEngine.eval(checkError3);
                                                this.rEngine.eval(checkError4);
                                            }
                                            String varComp1 = "capture.output(cat(\"\n\nESTIMATES OF VARIANCE COMPONENTS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String varComp2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$var.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(varComp1);
                                            this.rEngine.eval(varComp2);
                                            String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String genVar2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genvar.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(genVar1);
                                            this.rEngine.eval(genVar2);
                                            String lsd1 = "capture.output(cat(\"\n\nTABLE OF STANDARD ERRORS AND LSDs:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String lsd2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$stderror.table,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(lsd1);
                                            this.rEngine.eval(lsd2);
                                            this.rEngine.eval(outspace);
                                            this.rEngine.eval(outspace);
                                        } else {
                                            String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform test for significance of GCA and SCA effects.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String warningExceed2 = "capture.output(cat(\"        and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningExceed);
                                            this.rEngine.eval(warningExceed2);
                                            this.rEngine.eval(outspace);
                                        }
                                    } else {
                                        this.rEngine.eval(outspace);
                                        this.rEngine.eval(outspace);
                                    }
                                } else if (design == "CRD") {
                                    String meansNotComplete = "capture.output(cat(\"\n\n***\nERROR: At least one required P1xP2 combination is missing. Cannot perform the succeeding analyses.\n***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(meansNotComplete);
                                } else {
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                        ++m;
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel2METest(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcDiallel2TestME = null;
        if (design == "CRD") {
            funcDiallel2TestME = "result<-try(diallel2TestME(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RCB") {
            funcDiallel2TestME = "result<-try(diallel2TestME(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcDiallel2TestME = "result<-try(diallel2TestME(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcDiallel2TestME = "result<-try(diallel2TestME(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in diallel2TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcDiallel2TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcDiallel2TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        String runSuccess = this.rEngine.eval("class(result)").asString();
        if (runSuccess != null && runSuccess.equals("try-error")) {
            System.out.println("results of diallel2TestME: error");
        } else {
            int k = 0;
            while (k < respvar.length) {
                int i = k + 1;
                String estimatedMissing1 = "result$output[[" + i + "]]$canEstimateMissing";
                String estimatedMissing2 = this.rEngine.eval(estimatedMissing1).asString();
                if (estimatedMissing2.equals("TRUE")) {
                    String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(meansFileName2);
                    this.rEngine.eval(funcSavePredMeansCsv);
                    System.out.println(meansFileName2);
                    System.out.println(funcSavePredMeansCsv);
                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                        System.out.println("save matrix of means: error");
                    }
                    String effectsFileName2 = "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(effectsFileName2);
                    this.rEngine.eval(funcSaveEffectsCsv);
                    System.out.println(effectsFileName2);
                    System.out.println(funcSaveEffectsCsv);
                    String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                    if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                        System.out.println("save matrix of effects: error");
                    }
                }
                ++k;
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel3Test(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = null;
            usedDesign = cross == "TRUE" ? "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD III IN " + designName + " (CROSS)\n\n\"),file=\"" + outFileName + "\",append = TRUE)" : "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD III IN " + designName + " (SELF)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcDiallel3Test = null;
            if (design == "CRD") {
                funcDiallel3Test = environment == "NULL" ? "result<-try(diallel3Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel3Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcDiallel3Test = environment == "NULL" ? "result<-try(diallel3Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel3Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcDiallel3Test = environment == "NULL" ? "result<-try(diallel3Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel3Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcDiallel3Test = environment == "NULL" ? "result<-try(diallel3Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel3Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcDiallel3Test);
            System.out.println(funcDiallel3Test);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("diallel3Test: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in diallel3Test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String getEnvLevels = "length(result$output[[" + i + "]]$site)";
                    int envLevelsLength = this.rEngine.eval(getEnvLevels).asInt();
                    int m = 0;
                    while (m < envLevelsLength) {
                        int nLevelsRep;
                        int maxLengthPerCross;
                        int j = m + 1;
                        if (environment != "NULL") {
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,result$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        if ((maxLengthPerCross = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$maxLengthPerCross").asInt()) > (nLevelsRep = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$nlevelsRep").asInt())) {
                            String errorMessage1 = "capture.output(cat(\"\nERROR: \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError),file=\"" + outFileName + "\",append = TRUE)";
                            String errorMessage2 = "capture.output(cat(\"\n       \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError2),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(errorMessage1);
                            this.rEngine.eval(errorMessage2);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            break;
                        }
                        String manyNA1 = "result$output[[" + i + "]]$site[[" + j + "]]$tooManyNAWarning";
                        String manyNA2 = this.rEngine.eval(manyNA1).asString();
                        if (manyNA2.equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialSumTable = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(trialSumHead);
                            this.rEngine.eval(trialSumTable);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(trialObsRead);
                            this.rEngine.eval(trialObsUsed);
                            String exceeded = "result$output[[" + i + "]]$site[[" + j + "]]$exceededWarning";
                            String exceededCheck = this.rEngine.eval(exceeded).asString();
                            this.rEngine.eval(exceeded);
                            this.rEngine.eval(exceededCheck);
                            if (exceededCheck.equals("YES")) {
                                String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(warningExceed);
                            } else {
                                String meansComplete1;
                                String meansComplete2;
                                String crossEffect2;
                                double responseRate = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
                                if (design == "RCB") {
                                    String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaHead);
                                    if (responseRate < 0.9) {
                                        String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(warningExceed);
                                    } else {
                                        String ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$diallel1.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                    }
                                }
                                String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + p1 + ":" + p2 + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(crossEffect1);
                                Double pvalue = 0.0;
                                if (design == "CRD") {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String crossEffect3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genoEffect.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    this.rEngine.eval(crossEffect3);
                                    pvalue = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$pValue").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                } else if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform the analysis.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                } else {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$site[[" + j + "]]$formula1), data = result$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model1b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outAnovaTable10);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                }
                                if ((meansComplete2 = this.rEngine.eval(meansComplete1 = "result$output[[" + i + "]]$site[[" + j + "]]$meansComplete").asString()).equals("TRUE")) {
                                    double alphaNew = Double.parseDouble(alpha);
                                    if (pvalue < alphaNew) {
                                        String gcaAnovaHead = "capture.output(cat(\"\n\nANALYSIS OF VARIANCE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(gcaAnovaHead);
                                        if (responseRate >= 0.9 || design == "CRD") {
                                            String gcaAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.anova,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(gcaAnova1);
                                            String effects1 = "capture.output(cat(\"\n\nGENERAL COMBINING ABILITY EFFECTS (diagonal), SPECIFIC COMBINING\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects2 = "capture.output(cat(\"ABILITY EFFECTS (above diagonal) AND RECIPROCAL EFFECTS (below diagonal):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(effects1);
                                            this.rEngine.eval(effects2);
                                            this.rEngine.eval(effects3);
                                            String effectsFileName2 = null;
                                            effectsFileName2 = environment != "NULL" ? "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                            String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                            this.rEngine.eval(effectsFileName2);
                                            this.rEngine.eval(funcSaveEffectsCsv);
                                            String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                                            if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                                                System.out.println("save matrix of effects: error");
                                                String checkError = "msg <- trimStrings(strsplit(saveDataB2, \":\")[[1]])";
                                                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of effects to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkError);
                                                this.rEngine.eval(checkError2);
                                                this.rEngine.eval(checkError3);
                                                this.rEngine.eval(checkError4);
                                            }
                                            String varComp1 = "capture.output(cat(\"\n\nESTIMATES OF VARIANCE COMPONENTS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String varComp2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$var.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(varComp1);
                                            this.rEngine.eval(varComp2);
                                            String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String genVar2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genvar.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(genVar1);
                                            this.rEngine.eval(genVar2);
                                            String lsd1 = "capture.output(cat(\"\n\nTABLE OF STANDARD ERRORS AND LSDs:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String lsd2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$stderror.table,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(lsd1);
                                            this.rEngine.eval(lsd2);
                                            this.rEngine.eval(outspace);
                                            this.rEngine.eval(outspace);
                                        } else {
                                            String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform test for significance of GCA and SCA effects.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String warningExceed2 = "capture.output(cat(\"        and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningExceed);
                                            this.rEngine.eval(warningExceed2);
                                            this.rEngine.eval(outspace);
                                        }
                                    } else {
                                        this.rEngine.eval(outspace);
                                        this.rEngine.eval(outspace);
                                    }
                                } else if (design == "CRD") {
                                    String meansNotComplete = "capture.output(cat(\"\n\n***\nERROR: At least one required P1xP2 combination is missing. Cannot perform the succeeding analyses.\n***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(meansNotComplete);
                                } else {
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                        ++m;
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel3METest(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcDiallel3TestME = null;
        if (design == "CRD") {
            funcDiallel3TestME = "result<-try(diallel3TestME(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RCB") {
            funcDiallel3TestME = "result<-try(diallel3TestME(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcDiallel3TestME = "result<-try(diallel3TestME(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcDiallel3TestME = "result<-try(diallel3TestME(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in diallel3TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcDiallel3TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcDiallel3TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        String runSuccess = this.rEngine.eval("class(result)").asString();
        if (runSuccess != null && runSuccess.equals("try-error")) {
            System.out.println("results of diallel3TestME: error");
        } else {
            int k = 0;
            while (k < respvar.length) {
                int i = k + 1;
                String estimatedMissing1 = "result$output[[" + i + "]]$canEstimateMissing";
                String estimatedMissing2 = this.rEngine.eval(estimatedMissing1).asString();
                if (estimatedMissing2.equals("TRUE")) {
                    String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(meansFileName2);
                    this.rEngine.eval(funcSavePredMeansCsv);
                    System.out.println(meansFileName2);
                    System.out.println(funcSavePredMeansCsv);
                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                        System.out.println("save matrix of means: error");
                    }
                    String effectsFileName2 = "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(effectsFileName2);
                    this.rEngine.eval(funcSaveEffectsCsv);
                    System.out.println(effectsFileName2);
                    System.out.println(funcSaveEffectsCsv);
                    String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                    if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                        System.out.println("save matrix of effects: error");
                    }
                }
                ++k;
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel4Test(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
        String runSuccessData = this.rEngine.eval("data").asString();
        if (runSuccessData != null && runSuccessData.equals("notRun")) {
            System.out.println("error");
            this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
        } else {
            String designName = null;
            if (design == "CRD") {
                designName = "CRD";
            }
            if (design == "RCB") {
                designName = "RCB";
            }
            if (design == "Alpha") {
                designName = "ALPHA-LATTICE";
            }
            if (design == "RowColumn") {
                designName = "ROW-COLUMN";
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedDesign = null;
            usedDesign = cross == "TRUE" ? "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD IV IN " + designName + " (CROSS)\n\n\"),file=\"" + outFileName + "\",append = TRUE)" : "capture.output(cat(\"\nDIALLEL ANALYSIS: GRIFFING METHOD IV IN " + designName + " (SELF)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedDesign);
            String funcDiallel4Test = null;
            if (design == "CRD") {
                funcDiallel4Test = environment == "NULL" ? "result<-try(diallel4Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel4Test(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RCB") {
                funcDiallel4Test = environment == "NULL" ? "result<-try(diallel4Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel4Test(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "Alpha") {
                funcDiallel4Test = environment == "NULL" ? "result<-try(diallel4Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel4Test(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            if (design == "RowColumn") {
                funcDiallel4Test = environment == "NULL" ? "result<-try(diallel4Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = " + environment + ", alpha = " + alpha + "), silent = TRUE)" : "result<-try(diallel4Test(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
            }
            this.rEngine.eval(funcDiallel4Test);
            System.out.println(funcDiallel4Test);
            String runSuccess = this.rEngine.eval("class(result)").asString();
            if (runSuccess != null && runSuccess.equals("try-error")) {
                System.out.println("diallel3Test: error");
                String checkError = "msg <- trimStrings(strsplit(result, \":\")[[1]])";
                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError4 = "capture.output(cat(\"*** \nERROR in diallel3Test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError3);
                this.rEngine.eval(checkError4);
            } else {
                int k = 0;
                while (k < respvar.length) {
                    int i = k + 1;
                    String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    String getEnvLevels = "length(result$output[[" + i + "]]$site)";
                    int envLevelsLength = this.rEngine.eval(getEnvLevels).asInt();
                    int m = 0;
                    while (m < envLevelsLength) {
                        int nLevelsRep;
                        int maxLengthPerCross;
                        int j = m + 1;
                        if (environment != "NULL") {
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,result$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        if ((maxLengthPerCross = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$maxLengthPerCross").asInt()) > (nLevelsRep = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$nlevelsRep").asInt())) {
                            String errorMessage1 = "capture.output(cat(\"\nERROR: \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError),file=\"" + outFileName + "\",append = TRUE)";
                            String errorMessage2 = "capture.output(cat(\"\n       \",result$output[[" + i + "]]$site[[" + j + "]]$blockLabelError2),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(errorMessage1);
                            this.rEngine.eval(errorMessage2);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                            break;
                        }
                        String manyNA1 = "result$output[[" + i + "]]$site[[" + j + "]]$tooManyNAWarning";
                        String manyNA2 = this.rEngine.eval(manyNA1).asString();
                        if (manyNA2.equals("YES")) {
                            String warningExceed = "capture.output(cat(\"\nToo many missing observations. Cannot proceed with the analysis.\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(warningExceed);
                        } else {
                            String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialSumTable = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsRead = "capture.output(cat(\"Number of observations read: \", result$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String trialObsUsed = "capture.output(cat(\"Number of observations used: \", result$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(trialSumHead);
                            this.rEngine.eval(trialSumTable);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(trialObsRead);
                            this.rEngine.eval(trialObsUsed);
                            String exceeded = "result$output[[" + i + "]]$site[[" + j + "]]$exceededWarning";
                            String exceededCheck = this.rEngine.eval(exceeded).asString();
                            this.rEngine.eval(exceeded);
                            this.rEngine.eval(exceededCheck);
                            if (exceededCheck.equals("YES")) {
                                String warningExceed = "capture.output(cat(\"\n\n***\nERROR: The number of observations read in the data exceeds the size of a balanced data.\n       Please check if the column for block/replicate is properly labeled.\n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(warningExceed);
                            } else {
                                String meansComplete1;
                                String meansComplete2;
                                String crossEffect2;
                                double responseRate = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
                                if (design == "RCB") {
                                    String anovaHead = "capture.output(cat(\"\n\nANOVA TABLE FOR THE EXPERIMENT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaHead);
                                    if (responseRate < 0.9) {
                                        String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform ANOVA for balanced data.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(warningExceed);
                                    } else {
                                        String ttcAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$diallel1.anova,file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova2 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String ttcAnova3 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(ttcAnova1);
                                        this.rEngine.eval(ttcAnova2);
                                        this.rEngine.eval(ttcAnova3);
                                    }
                                }
                                String crossEffect1 = "capture.output(cat(\"\n\nTESTING FOR THE SIGNIFICANCE OF CROSS EFFECT: (Crosses = " + p1 + ":" + p2 + ")\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(crossEffect1);
                                Double pvalue = 0.0;
                                if (design == "CRD") {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String crossEffect3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genoEffect.anova,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    this.rEngine.eval(crossEffect3);
                                    pvalue = this.rEngine.eval("result$output[[" + i + "]]$site[[" + j + "]]$pValue").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                } else if (responseRate < 0.9) {
                                    String warningExceed = "capture.output(cat(\"\n ERROR: Too many missing values. Cannot perform the analysis.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningExceed);
                                } else {
                                    crossEffect2 = "capture.output(cat(\"\nModel:\", result$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(crossEffect2);
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model1b <- lmer(formula(result$output[[" + i + "]]$site[[" + j + "]]$formula1), data = result$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model1b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    pvalue = this.rEngine.eval("a.table[1,6]").asDouble();
                                    System.out.println("pvalue: " + pvalue);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outAnovaTable10);
                                    String matrixMeans1 = "capture.output(cat(\"\n\nMATRIX OF MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String matrixMeans2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(matrixMeans1);
                                    this.rEngine.eval(matrixMeans2);
                                    String anovaRemark1 = "capture.output(cat(\"-------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String anovaRemark2 = "capture.output(cat(\"REMARK:\", result$output[[" + i + "]]$site[[" + j + "]]$anovaRemark2,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(anovaRemark1);
                                    this.rEngine.eval(anovaRemark2);
                                    String meansFileName2 = null;
                                    meansFileName2 = environment != "NULL" ? "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                    System.out.println(meansFileName2);
                                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                    this.rEngine.eval(meansFileName2);
                                    this.rEngine.eval(funcSavePredMeansCsv);
                                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                                        System.out.println("save matrix of means: error");
                                        String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                                        String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of means to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError);
                                        this.rEngine.eval(checkError2);
                                        this.rEngine.eval(checkError3);
                                        this.rEngine.eval(checkError4);
                                    }
                                }
                                if ((meansComplete2 = this.rEngine.eval(meansComplete1 = "result$output[[" + i + "]]$site[[" + j + "]]$meansComplete").asString()).equals("TRUE")) {
                                    double alphaNew = Double.parseDouble(alpha);
                                    if (pvalue < alphaNew) {
                                        String gcaAnovaHead = "capture.output(cat(\"\n\nANALYSIS OF VARIANCE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(gcaAnovaHead);
                                        if (responseRate >= 0.9 || design == "CRD") {
                                            String gcaAnova1 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.anova,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(gcaAnova1);
                                            String effects1 = "capture.output(cat(\"\n\nGENERAL COMBINING ABILITY EFFECTS (diagonal), SPECIFIC COMBINING\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects2 = "capture.output(cat(\"ABILITY EFFECTS (above diagonal) AND RECIPROCAL EFFECTS (below diagonal):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String effects3 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(effects1);
                                            this.rEngine.eval(effects2);
                                            this.rEngine.eval(effects3);
                                            String effectsFileName2 = null;
                                            effectsFileName2 = environment != "NULL" ? "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \"_\", result$output[[" + i + "]]$site[[" + j + "]]$env,\".csv\", sep=\"\")" : "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                                            String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$site[[" + j + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                                            this.rEngine.eval(effectsFileName2);
                                            this.rEngine.eval(funcSaveEffectsCsv);
                                            String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                                            if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                                                System.out.println("save matrix of effects: error");
                                                String checkError = "msg <- trimStrings(strsplit(saveDataB2, \":\")[[1]])";
                                                String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                String checkError4 = "capture.output(cat(\"\n***\nERROR in saving matrix of effects to a file:\\n  \",msg, \"\n***\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkError);
                                                this.rEngine.eval(checkError2);
                                                this.rEngine.eval(checkError3);
                                                this.rEngine.eval(checkError4);
                                            }
                                            String varComp1 = "capture.output(cat(\"\n\nESTIMATES OF VARIANCE COMPONENTS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String varComp2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$var.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(varComp1);
                                            this.rEngine.eval(varComp2);
                                            String genVar1 = "capture.output(cat(\"\n\nESTIMATES OF GENETIC VARIANCE COMPONENTS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String genVar2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$genvar.components,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(genVar1);
                                            this.rEngine.eval(genVar2);
                                            String lsd1 = "capture.output(cat(\"\n\nTABLE OF STANDARD ERRORS AND LSDs:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String lsd2 = "capture.output(result$output[[" + i + "]]$site[[" + j + "]]$stderror.table,file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(lsd1);
                                            this.rEngine.eval(lsd2);
                                            this.rEngine.eval(outspace);
                                            this.rEngine.eval(outspace);
                                        } else {
                                            String warningExceed = "capture.output(cat(\" ERROR: Too many missing values. Cannot perform test for significance of GCA and SCA effects.\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            String warningExceed2 = "capture.output(cat(\"        and estimation of genetic variance components.\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningExceed);
                                            this.rEngine.eval(warningExceed2);
                                            this.rEngine.eval(outspace);
                                        }
                                    } else {
                                        this.rEngine.eval(outspace);
                                        this.rEngine.eval(outspace);
                                    }
                                } else if (design == "CRD") {
                                    String meansNotComplete = "capture.output(cat(\"\n\n***\nERROR: At least one required P1xP2 combination is missing. Cannot perform the succeeding analyses.\n***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(meansNotComplete);
                                } else {
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                }
                            }
                        }
                        ++m;
                    }
                    ++k;
                }
                this.rEngine.eval(outspace);
                this.rEngine.eval(sep2);
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doDiallel4METest(String dataFileName, String outFileName, String resultFolderPath, String design, String[] respvar, String p1, String p2, String rep, String block, String row, String column, String cross, String individual, String environment, String alpha) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String usedData = "cat(\"\nDATA FILE: " + dataFileName + "\n\")";
        String funcDiallel4TestME = null;
        if (design == "CRD") {
            funcDiallel4TestME = "result<-try(diallel4TestME(\"CRD\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, block = NULL, row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RCB") {
            funcDiallel4TestME = "result<-try(diallel4TestME(\"RCB\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = NULL, " + "block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "Alpha") {
            funcDiallel4TestME = "result<-try(diallel4TestME(\"Alpha\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = \"" + block + "\", row = NULL, column = NULL, cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        if (design == "RowColumn") {
            funcDiallel4TestME = "result<-try(diallel4TestME(\"RowColumn\", \"dataRead\", " + respvarVector + ", \"" + p1 + "\", \"" + p2 + "\", rep = \"" + rep + "\", block = NULL, row = \"" + row + "\", column = \"" + column + "\", cross = " + cross + ", individual = NULL, environment = \"" + environment + "\", alpha = " + alpha + "), silent = TRUE)";
        }
        String checkError = "if (class(result) == \"try-error\") {\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
        checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
        checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
        checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in diallel4TestME function:\\n  \",msg, \"\n***\", sep = \"\")\n";
        checkError = String.valueOf(checkError) + "}";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcDiallel4TestME);
        System.out.println(checkError);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(funcDiallel4TestME);
        this.rEngine.eval(checkError);
        this.rEngine.eval(sinkOut);
        String runSuccess = this.rEngine.eval("class(result)").asString();
        if (runSuccess != null && runSuccess.equals("try-error")) {
            System.out.println("results of diallel4TestME: error");
        } else {
            int k = 0;
            while (k < respvar.length) {
                int i = k + 1;
                String estimatedMissing1 = "result$output[[" + i + "]]$canEstimateMissing";
                String estimatedMissing2 = this.rEngine.eval(estimatedMissing1).asString();
                if (estimatedMissing2.equals("TRUE")) {
                    String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixMeans_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(result$output[[" + i + "]]$means.matrix,file = meansFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(meansFileName2);
                    this.rEngine.eval(funcSavePredMeansCsv);
                    System.out.println(meansFileName2);
                    System.out.println(funcSavePredMeansCsv);
                    String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                    if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                        System.out.println("save matrix of means: error");
                    }
                    String effectsFileName2 = "effectsFileName2 <- paste(\"" + resultFolderPath + "\",\"matrixEffects_\", \"" + respvar[k] + "\", \".csv\", sep=\"\")";
                    String funcSaveEffectsCsv = "saveDataB2 <- try(write.table(result$output[[" + i + "]]$gcasca.matrix,file = effectsFileName2 ,sep=\",\",col.names=NA, row.names=TRUE), silent=TRUE)";
                    this.rEngine.eval(effectsFileName2);
                    this.rEngine.eval(funcSaveEffectsCsv);
                    System.out.println(effectsFileName2);
                    System.out.println(funcSaveEffectsCsv);
                    String runSuccessSaveEffects = this.rEngine.eval("class(saveDataB2)").asString();
                    if (runSuccessSaveEffects != null && runSuccessSaveEffects.equals("try-error")) {
                        System.out.println("save matrix of effects: error");
                    }
                }
                ++k;
            }
        }
        this.rEngineEnd();
    }

    @Override
    public void doSingleEnvironmentAnalysis(String dataFileName, String outFileName, String resultFolderPath, int designIndex, String[] respvar, String environment, String[] environmentLevels, String genotype, String block, String rep, String row, String column, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean heatmapResiduals, String heatmapRow, String heatmapColumn, boolean diagnosticPlot, boolean genotypeFixed, boolean performPairwise, String pairwiseAlpha, String[] genotypeLevels, String[] controlLevels, boolean compareControl, boolean performAllPairwise, boolean genotypeRandom, boolean excludeControls, boolean genoPhenoCorrelation) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String controlLevelsVector = this.inputTransform.createRVector(controlLevels);
        boolean runningFixedSuccess = true;
        boolean runningRandomSuccess = true;
        boolean printAllOutputFixed = true;
        boolean printAllOutputRandom = true;
        try {
            String checkError2;
            String checkError;
            String checkError4;
            String checkError42;
            String checkError3;
            String checkError22;
            String printError3;
            String printError2;
            String printError1;
            String lmerError;
            String lmerRun;
            String printError32;
            String printError22;
            String printError12;
            String checkError43;
            String checkError32;
            String checkError23;
            String checkError5;
            String respVarHead;
            String checkError44;
            String checkError33;
            String checkError24;
            String checkError6;
            String groupVars;
            String designUsed = new String();
            String design = new String();
            switch (designIndex) {
                case 0: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                    break;
                }
                case 1: {
                    designUsed = "Augmented RCB";
                    design = "AugRCB";
                    break;
                }
                case 2: {
                    designUsed = "Augmented Latin Square";
                    design = "AugLS";
                    break;
                }
                case 3: {
                    designUsed = "Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 4: {
                    designUsed = "Row-Column";
                    design = "RowCol";
                    break;
                }
                case 5: {
                    designUsed = "Latinized Alpha-Lattice";
                    design = "LatinAlpha";
                    break;
                }
                case 6: {
                    designUsed = "Latinized Row-Column";
                    design = "LatinRowCol";
                    break;
                }
                case 7: {
                    designUsed = "Augmented Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 8: {
                    designUsed = "Augmented Row-Column";
                    design = "RowCol";
                    break;
                }
                default: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                }
            }
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("data").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String outFile = "capture.output(cat(\"\nSINGLE-ENVIRONMENT ANALYSIS\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(outFile);
            this.rEngine.eval(usedDesign);
            if (genotypeFixed) {
                String funcSsaFixed = null;
                groupVars = null;
                if (environment == "NULL") {
                    if (designIndex == 0 || designIndex == 1) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (designIndex == 0 || designIndex == 1) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                } else if (designIndex == 2) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcSsaFixed);
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outspace);
                System.out.println(funcSsaFixed);
                String runSuccess = this.rEngine.eval("class(ssa1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("ssa.test: error");
                    checkError6 = "msg <- trimStrings(strsplit(ssa1, \":\")[[1]])";
                    checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError44 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError24);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError44);
                    runningFixedSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        this.rEngine.eval(outspace);
                        if (descriptiveStat) {
                            String funcDesc = null;
                            funcDesc = environment == "NULL" ? "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", " + environment + "), silent=TRUE)" : "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", \"" + environment + "\"), silent=TRUE)";
                            System.out.println(funcDesc);
                            this.rEngine.eval(funcDesc);
                            String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                            String runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                            if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                System.out.println("desc stat: error");
                                checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError23);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError43);
                            } else {
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                            }
                        }
                        int envLevelsLength = 0;
                        envLevelsLength = environment == "NULL" ? 1 : environmentLevels.length;
                        int m = 0;
                        while (m < envLevelsLength) {
                            double nrowData;
                            printAllOutputFixed = true;
                            int j = m + 1;
                            if (environment != "NULL") {
                                String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,ssa1$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(sep);
                                this.rEngine.eval(envtHead);
                                this.rEngine.eval(sep);
                                this.rEngine.eval(outspace);
                            }
                            if ((nrowData = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble()) < 0.8) {
                                String allNAWarning = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
                                printError12 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError22 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError32 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(printError22);
                                this.rEngine.eval(printError32);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outspace);
                                printAllOutputFixed = false;
                            } else {
                                lmerRun = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
                                if (lmerRun.equals("ERROR")) {
                                    lmerError = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
                                    printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(printError2);
                                    this.rEngine.eval(printError3);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    printAllOutputFixed = false;
                                }
                            }
                            if (printAllOutputFixed) {
                                String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa1$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
                                String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa1$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa1$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(funcTrialSum);
                                String runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                                if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                    System.out.println("class info: error");
                                    String checkError7 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                    String checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError45 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError7);
                                    this.rEngine.eval(checkError25);
                                    this.rEngine.eval(checkError34);
                                    this.rEngine.eval(checkError45);
                                } else {
                                    this.rEngine.eval(trialSumHead);
                                    this.rEngine.eval(trialObsRead);
                                    this.rEngine.eval(trialObsUsed);
                                    this.rEngine.eval(trialSum);
                                    this.rEngine.eval(outspace);
                                }
                                if (varianceComponents) {
                                    String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outVarComp2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outVarComp);
                                    this.rEngine.eval(outVarComp2);
                                    this.rEngine.eval(outspace);
                                }
                                String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable2 = "library(lmerTest)";
                                String outAnovaTable3 = "model1b <- lmer(formula(ssa1$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa1$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                String outAnovaTable4 = "a.table <- anova(model1b)";
                                String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                String outAnovaTable6 = "a.table<-cbind(round(a.table[,c(3,4,1:2,5)], digits=4),pvalue)";
                                String outAnovaTable7 = "colnames(a.table)<-c(\"NumDf\", \"DenDf\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Pr(>F)\")";
                                String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                this.rEngine.eval(outAnovaTable1);
                                this.rEngine.eval(outAnovaTable2);
                                this.rEngine.eval(outAnovaTable3);
                                this.rEngine.eval(outAnovaTable4);
                                this.rEngine.eval(outAnovaTable5);
                                this.rEngine.eval(outAnovaTable6);
                                this.rEngine.eval(outAnovaTable7);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable8);
                                this.rEngine.eval(outAnovaTable9);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable10);
                                String outDescStat = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outDescStat2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                                String outsedTable = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outsedTable2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outsedTable);
                                this.rEngine.eval(outsedTable2);
                                this.rEngine.eval(outspace);
                                if (performPairwise) {
                                    String checkError46;
                                    String checkError35;
                                    String checkError26;
                                    String checkError8;
                                    double pairwiseSig = Double.valueOf(pairwiseAlpha);
                                    if (compareControl) {
                                        String funcPwC = "pwControl <- try(ssa.pairwise(ssa1$output[[" + i + "]]$site[[" + j + "]]$model, type = \"Dunnett\", alpha = " + pairwiseSig + ", control.level = " + controlLevelsVector + "), silent=TRUE)";
                                        String outCompareControl = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \nCompared with control(s)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        String outCompareControl2n = "capture.output(pwControl$result,file=\"" + outFileName + "\",append = TRUE)";
                                        String outCompareControl3n = "capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        System.out.println(funcPwC);
                                        this.rEngine.eval(funcPwC);
                                        this.rEngine.eval(outCompareControl);
                                        String runSuccessPwC = this.rEngine.eval("class(pwControl)").asString();
                                        if (runSuccessPwC != null && runSuccessPwC.equals("try-error")) {
                                            System.out.println("compare with control: error");
                                            checkError8 = "msg <- trimStrings(strsplit(pwControl, \":\")[[1]])";
                                            checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                            checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                            checkError46 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(checkError8);
                                            this.rEngine.eval(checkError26);
                                            this.rEngine.eval(checkError35);
                                            this.rEngine.eval(checkError46);
                                        } else {
                                            this.rEngine.eval(outCompareControl2n);
                                            String warningControlTest = this.rEngine.eval("pwControl$controlTestWarning").asString();
                                            if (!warningControlTest.equals("NONE")) {
                                                String warningCheckTest2 = "capture.output(cat(\"----- \nNOTE:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                                String warningCheckTest3 = "capture.output(cat(\"" + warningControlTest + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(warningCheckTest2);
                                                this.rEngine.eval(warningCheckTest3);
                                            }
                                            this.rEngine.eval(outCompareControl3n);
                                            System.out.println("pairwise control test:" + warningControlTest);
                                        }
                                    } else if (performAllPairwise) {
                                        String outPerformAllPairwise = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(outPerformAllPairwise);
                                        if (genotypeLevels.length > 0 && genotypeLevels.length < 16) {
                                            String funcPwAll = "pwAll <- try(ssa.pairwise(ssa1$output[[" + i + "]]$site[[" + j + "]]$model, type = \"Tukey\", alpha = " + pairwiseSig + ", control.level = NULL), silent=TRUE)";
                                            String outPerformAllPairwise2 = "capture.output(pwAll$result,file=\"" + outFileName + "\",append = TRUE)";
                                            String outPerformAllPairwise3 = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(funcPwAll);
                                            String runSuccessPwAll = this.rEngine.eval("class(pwAll)").asString();
                                            if (runSuccessPwAll != null && runSuccessPwAll.equals("try-error")) {
                                                System.out.println("all pairwise: error");
                                                checkError8 = "msg <- trimStrings(strsplit(pwAll, \":\")[[1]])";
                                                checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                checkError46 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkError8);
                                                this.rEngine.eval(checkError26);
                                                this.rEngine.eval(checkError35);
                                                this.rEngine.eval(checkError46);
                                            } else {
                                                this.rEngine.eval(outPerformAllPairwise2);
                                                this.rEngine.eval(outPerformAllPairwise3);
                                            }
                                        } else {
                                            String nLevelsLarge = "capture.output(cat(\"***\nExceeded maximum number of genotypes that can be compared. \n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(nLevelsLarge);
                                        }
                                    }
                                } else {
                                    this.rEngine.eval(outspace);
                                }
                            }
                            ++m;
                        }
                        ++k;
                    }
                    String checkMeanSSE = this.rEngine.eval("ssa1$meansseWarning").asString();
                    String checkVarRep = this.rEngine.eval("ssa1$varrepWarning").asString();
                    System.out.println("checkMeanSSE: " + checkMeanSSE);
                    System.out.println("checkVarRep: " + checkVarRep);
                    if (checkMeanSSE.equals("empty") | checkVarRep.equals("empty")) {
                        System.out.println("Saving means not done.");
                    } else {
                        String meansFileName = "meansFileName <- paste(\"" + resultFolderPath + "\",\"summaryStats.csv\", sep=\"\")";
                        String funcSaveSesVarRep = null;
                        funcSaveSesVarRep = environment == "NULL" ? "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"EnvLevel\")" : "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"" + environment + "\")";
                        String funcSaveSesVarRepCsv = "saveMeans <- try(write.table(meansVar,file = meansFileName ,sep=\",\",row.names=FALSE), silent=TRUE)";
                        this.rEngine.eval(meansFileName);
                        this.rEngine.eval(funcSaveSesVarRep);
                        this.rEngine.eval(funcSaveSesVarRepCsv);
                        String runSuccessSaveMeansSes = this.rEngine.eval("class(saveMeans)").asString();
                        if (runSuccessSaveMeansSes != null && runSuccessSaveMeansSes.equals("try-error")) {
                            System.out.println("saving means file: error");
                            String checkError9 = "msg <- trimStrings(strsplit(saveMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving means file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError9);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    if (diagnosticPlot) {
                        String diagPlotsFunc = null;
                        diagPlotsFunc = environment == "NULL" ? "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = FALSE, ssa1), silent=TRUE)" : "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = FALSE, ssa1), silent=TRUE)";
                        System.out.println(diagPlotsFunc);
                        this.rEngine.eval(diagPlotsFunc);
                        String runSuccessDiagPlots = this.rEngine.eval("class(diagPlots)").asString();
                        if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                            System.out.println("diagnostic plots(genotype fixed): error");
                            String checkError10 = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
                            String checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError10);
                            this.rEngine.eval(checkError27);
                            this.rEngine.eval(checkError36);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (genotypeRandom) {
                String funcSsaRandom = null;
                groupVars = null;
                if (excludeControls) {
                    if (environment == "NULL") {
                        if (designIndex == 0) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                        } else if (designIndex == 1) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                        } else if (designIndex == 2) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                        } else if (designIndex == 3 || designIndex == 5) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                        } else if (designIndex == 4 || designIndex == 6) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                        } else if (designIndex == 7) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                        } else if (designIndex == 8) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                        }
                    } else if (designIndex == 0) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 1) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 7) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList = NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 8) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList = NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (environment == "NULL") {
                    if (designIndex == 0 || designIndex == 1) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (designIndex == 0 || designIndex == 1) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                } else if (designIndex == 2) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcSsaRandom);
                this.rEngine.eval(sep2);
                this.rEngine.eval(randomHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outspace);
                System.out.println(funcSsaRandom);
                String runSuccess2 = this.rEngine.eval("class(ssa2)").asString();
                if (runSuccess2 != null && runSuccess2.equals("try-error")) {
                    System.out.println("ssa2: error");
                    checkError6 = "msg <- trimStrings(strsplit(ssa2, \":\")[[1]])";
                    checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError44 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError24);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError44);
                    runningRandomSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        this.rEngine.eval(outspace);
                        if (descriptiveStat) {
                            String funcDesc = null;
                            funcDesc = environment == "NULL" ? "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", " + environment + "), silent=TRUE)" : "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", \"" + environment + "\"), silent=TRUE)";
                            this.rEngine.eval(funcDesc);
                            System.out.println(funcDesc);
                            String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                            String runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                            if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                System.out.println("desc stat: error");
                                checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError23);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError43);
                            } else {
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                            }
                        }
                        int envLevelsLength2 = 0;
                        envLevelsLength2 = environment == "NULL" ? 1 : environmentLevels.length;
                        int m = 0;
                        while (m < envLevelsLength2) {
                            double responseRate;
                            printAllOutputRandom = true;
                            int j = m + 1;
                            if (environment != "NULL") {
                                String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,ssa2$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(sep);
                                this.rEngine.eval(envtHead);
                                this.rEngine.eval(sep);
                                this.rEngine.eval(outspace);
                            }
                            if ((responseRate = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble()) < 0.8) {
                                String allNAWarning2 = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
                                printError12 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError22 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError32 = "capture.output(cat(\"" + allNAWarning2 + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(printError22);
                                this.rEngine.eval(printError32);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outspace);
                                printAllOutputRandom = false;
                            } else {
                                lmerRun = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
                                if (lmerRun.equals("ERROR")) {
                                    lmerError = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
                                    printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(printError2);
                                    this.rEngine.eval(printError3);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    printAllOutputRandom = false;
                                }
                            }
                            if (printAllOutputRandom) {
                                int newCheckListLength;
                                String warningCheckTest = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$checkTestWarning").asString();
                                if (!warningCheckTest.equals("NONE")) {
                                    String warningCheckTest2 = "capture.output(cat(\"\n*** \nWARNING:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    String warningCheckTest3 = "capture.output(cat(\"" + warningCheckTest + "\"), file=\"" + outFileName + "\",append = TRUE)";
                                    String warningCheckTest4 = "capture.output(cat(\"\n*** \\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningCheckTest2);
                                    this.rEngine.eval(warningCheckTest3);
                                    this.rEngine.eval(warningCheckTest4);
                                }
                                System.out.println("check test:" + warningCheckTest);
                                String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa2$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
                                String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(funcTrialSum);
                                String runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                                if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                    System.out.println("class info: error");
                                    String checkError11 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                    String checkError28 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError47 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError11);
                                    this.rEngine.eval(checkError28);
                                    this.rEngine.eval(checkError37);
                                    this.rEngine.eval(checkError47);
                                } else {
                                    this.rEngine.eval(trialSumHead);
                                    this.rEngine.eval(trialObsRead);
                                    this.rEngine.eval(trialObsUsed);
                                    this.rEngine.eval(trialSum);
                                    this.rEngine.eval(outspace);
                                }
                                if (varianceComponents) {
                                    String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outVarComp2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outVarComp);
                                    this.rEngine.eval(outVarComp2);
                                    this.rEngine.eval(outspace);
                                }
                                String outTestGen1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen2 = "capture.output(cat(\"\nFormula for Model1: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen3 = "capture.output(cat(\"Formula for Model2: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen4a = "compareModelTable <- data.frame(rownames(ssa2$output[[" + i + "]]$site[[" + j + "]]$models.table), ssa2$output[[" + i + "]]$site[[" + j + "]]$models.table)";
                                String outTestGen4b = "colnames(compareModelTable) <- c(\"\",colnames(ssa2$output[[" + i + "]]$site[[" + j + "]]$models.table))";
                                String outTestGen4 = "capture.output(printDataFrame(compareModelTable, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outTestGen1);
                                this.rEngine.eval(outTestGen2);
                                this.rEngine.eval(outTestGen3);
                                System.out.println(outTestGen4a);
                                System.out.println(outTestGen4b);
                                this.rEngine.eval(outTestGen4a);
                                this.rEngine.eval(outTestGen4b);
                                this.rEngine.eval(outTestGen4);
                                this.rEngine.eval(outspace);
                                String newExcludeCheck = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$newExcludeCheck").asBool().toString();
                                System.out.println("newExcludeCheck: " + newExcludeCheck);
                                if (newExcludeCheck.equals("TRUE")) {
                                    String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF CHECK EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model2b <- lmer(formula(ssa2$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa2$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model2b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,c(3,4,1:2,5)], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"NumDf\", \"DenDf\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable1);
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable10);
                                }
                                String outPredMeans = "capture.output(cat(\"\nPREDICTED MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outPredMeans2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outPredMeans);
                                this.rEngine.eval(outPredMeans2);
                                this.rEngine.eval(outspace);
                                if (excludeControls && (newCheckListLength = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$newCheckListLength").asInt()) > 0) {
                                    String outLSMeansCheck = "capture.output(cat(\"\nCHECK/CONTROL LSMEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outLSMeansCheck2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$lsmeans.checks,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outLSMeansCheck);
                                    this.rEngine.eval(outLSMeansCheck2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstHerit = "capture.output(cat(\"\nHERITABILITY:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outEstHerit2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$heritability,file=\"" + outFileName + "\",append = TRUE)";
                                String outEstHerit3 = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outEstHerit);
                                this.rEngine.eval(outEstHerit2);
                                this.rEngine.eval(outEstHerit3);
                                this.rEngine.eval(outspace);
                            }
                            ++m;
                        }
                        ++k;
                    }
                    if (genoPhenoCorrelation) {
                        this.rEngine.eval(sep2);
                        String funcEstCorr = null;
                        if (excludeControls) {
                            if (environment == "NULL") {
                                if (designIndex == 0) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 1) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 2) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 3) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 4) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 5) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 6) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 7) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 8) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                }
                            } else if (designIndex == 0) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 1) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 2) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 3) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 4) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 5) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 6) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 7) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 8) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            }
                        } else if (environment == "NULL") {
                            if (designIndex == 0 || designIndex == 1) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + "), silent=TRUE)";
                            } else if (designIndex == 2) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + "), silent=TRUE)";
                            } else if (designIndex == 3 || designIndex == 7) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 4 || designIndex == 8) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 5) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 6) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
                            }
                        } else if (designIndex == 0 || designIndex == 1) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 2) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 3 || designIndex == 7) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 4 || designIndex == 8) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 5) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 6) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        }
                        System.out.println(funcEstCorr);
                        this.rEngine.eval(funcEstCorr);
                        String runSuccessGPCorr = this.rEngine.eval("class(gpcorr)").asString();
                        if (runSuccessGPCorr != null && runSuccessGPCorr.equals("try-error")) {
                            System.out.println("geno pheno corr: error");
                            checkError = "msg <- trimStrings(strsplit(gpcorr, \":\")[[1]])";
                            String checkError29 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError38 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            String checkError48 = "capture.output(cat(\"*** \nERROR in genoNpheno.corr function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError29);
                            this.rEngine.eval(checkError38);
                            this.rEngine.eval(checkError48);
                        } else {
                            String outEstGenoCorr = "capture.output(cat(\"\nGENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstGenoCorr);
                            int envLevelsLength = 0;
                            envLevelsLength = environment == "NULL" ? 1 : environmentLevels.length;
                            int m = 0;
                            while (m < envLevelsLength) {
                                int j = m + 1;
                                if (environment != "NULL") {
                                    String outEstGenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outEstGenoCorr2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstGenoCorr2b = "capture.output(gpcorr$GenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outEstGenoCorr2b);
                                this.rEngine.eval(outspace);
                                ++m;
                            }
                            String outEstPhenoCorr = "capture.output(cat(\"\nPHENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstPhenoCorr);
                            int m2 = 0;
                            while (m2 < envLevelsLength) {
                                int j = m2 + 1;
                                if (environment != "NULL") {
                                    String outEstPhenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outEstPhenoCorr2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstPhenoCorr2b = "capture.output(gpcorr$PhenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outEstPhenoCorr2b);
                                this.rEngine.eval(outspace);
                                ++m2;
                            }
                        }
                    }
                    String checkPredMean = this.rEngine.eval("ssa2$meansWarning").asString();
                    System.out.println("checkPredMean: " + checkPredMean);
                    if (checkPredMean.equals("empty")) {
                        System.out.println("Saving predicted means not done.");
                    } else {
                        String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"predictedMeans.csv\", sep=\"\")";
                        String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(ssa2$means,file = meansFileName2 ,sep=\",\",row.names=FALSE), silent=TRUE)";
                        this.rEngine.eval(meansFileName2);
                        this.rEngine.eval(funcSavePredMeansCsv);
                        String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                        if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                            System.out.println("save pred means: error");
                            String checkError12 = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                            String checkError210 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError39 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving predicted means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError12);
                            this.rEngine.eval(checkError210);
                            this.rEngine.eval(checkError39);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        String diagPlotsFunc = null;
                        diagPlotsFunc = environment == "NULL" ? "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = TRUE, ssa2), silent=TRUE)" : "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = TRUE, ssa2), silent=TRUE)";
                        System.out.println(diagPlotsFunc);
                        this.rEngine.eval(diagPlotsFunc);
                        String runSuccessDiagPlots = this.rEngine.eval("class(diagPlots)").asString();
                        if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                            System.out.println("diagnostic plots (genotype random): error");
                            String checkError13 = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
                            String checkError211 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError310 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            String checkError49 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError13);
                            this.rEngine.eval(checkError211);
                            this.rEngine.eval(checkError310);
                            this.rEngine.eval(checkError49);
                        }
                    }
                }
            }
            if (runningFixedSuccess & runningRandomSuccess) {
                String trialObsUsed;
                String warningList;
                String warningListCommand;
                String envLevelsCommand;
                String checkError410;
                String checkError311;
                String runSuccessSaveResid;
                String checkResid1;
                String runSuccessDiagPlots;
                String runSsaResid1;
                String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
                String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
                if (genotypeFixed & !genotypeRandom) {
                    runSsaResid1 = null;
                    runSsaResid1 = environment == "NULL" ? "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)" : "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    this.rEngine.eval(runSsaResid1);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("ssa.resid (genotype fixed): error");
                        checkError6 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        String checkError212 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError411 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError6);
                        this.rEngine.eval(checkError212);
                        this.rEngine.eval(checkError33);
                        this.rEngine.eval(checkError411);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_f$residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError14 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError213 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError312 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError412 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError14);
                                this.rEngine.eval(checkError213);
                                this.rEngine.eval(checkError312);
                                this.rEngine.eval(checkError412);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat1)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (fixed): error");
                                    String checkError15 = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
                                    String checkError214 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError311 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError410 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError15);
                                    this.rEngine.eval(checkError214);
                                    this.rEngine.eval(checkError311);
                                    this.rEngine.eval(checkError410);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        envLevelsCommand = "length(heat1[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            warningListCommand = "heat1[[" + i + "]]$site[[" + j + "]]";
                                            warningList = this.rEngine.eval(warningListCommand).asString();
                                            if (!warningList.equals("empty") && !warningList.equals("unique")) {
                                                trialObsUsed = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                } else if (!genotypeFixed & genotypeRandom) {
                    String runSsaResid2 = null;
                    runSsaResid2 = environment == "NULL" ? "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)" : "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid2);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("ssa.resid (genotype random): error");
                        checkError6 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError215 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError413 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError6);
                        this.rEngine.eval(checkError215);
                        this.rEngine.eval(checkError33);
                        this.rEngine.eval(checkError413);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_r$residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError16 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError216 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError313 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError414 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError16);
                                this.rEngine.eval(checkError216);
                                this.rEngine.eval(checkError313);
                                this.rEngine.eval(checkError414);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat2)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (random): error");
                                    String checkError17 = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
                                    String checkError217 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError311 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError410 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError17);
                                    this.rEngine.eval(checkError217);
                                    this.rEngine.eval(checkError311);
                                    this.rEngine.eval(checkError410);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        envLevelsCommand = "length(heat2[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            warningListCommand = "heat2[[" + i + "]]$site[[" + j + "]]";
                                            warningList = this.rEngine.eval(warningListCommand).asString();
                                            if (!warningList.equals("empty") && !warningList.equals("unique")) {
                                                trialObsUsed = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                } else if (genotypeFixed & genotypeRandom) {
                    runSsaResid1 = null;
                    String runSsaResid2 = null;
                    if (environment == "NULL") {
                        runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)";
                        runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)";
                    } else {
                        runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                        runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    }
                    System.out.println(runSsaResid1);
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid1);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessResidFixed = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {
                        System.out.println("ssa.resid (genotype fixed): error");
                        String checkError18 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError314 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError415 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError18);
                        this.rEngine.eval(checkError2);
                        this.rEngine.eval(checkError314);
                        this.rEngine.eval(checkError415);
                    } else {
                        String checkResid12 = this.rEngine.eval("resid_f$residWarning").asString();
                        System.out.println("checkResid1: " + checkResid12);
                        if (checkResid12.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            String runSuccessSaveResid2 = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid2 != null && runSuccessSaveResid2.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError19 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError218 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError315 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError19);
                                this.rEngine.eval(checkError218);
                                this.rEngine.eval(checkError315);
                                this.rEngine.eval(checkError4);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat1)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (fixed): error");
                                    String checkError20 = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
                                    checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError42 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError20);
                                    this.rEngine.eval(checkError22);
                                    this.rEngine.eval(checkError3);
                                    this.rEngine.eval(checkError42);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        String envLevelsCommand2 = "length(heat1[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand2).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            String warningListCommand2 = "heat1[[" + i + "]]$site[[" + j + "]]";
                                            String warningList2 = this.rEngine.eval(warningListCommand2).asString();
                                            if (!warningList2.equals("empty") && !warningList2.equals("unique")) {
                                                String trialObsUsed2 = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed2);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                    String runSuccessResidRandom = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {
                        System.out.println("ssa.resid (genotype random): error");
                        checkError = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError219 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError316 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError416 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError);
                        this.rEngine.eval(checkError219);
                        this.rEngine.eval(checkError316);
                        this.rEngine.eval(checkError416);
                    } else {
                        String checkResid13 = this.rEngine.eval("resid_r$residWarning").asString();
                        System.out.println("checkResid2: " + checkResid13);
                        if (checkResid13.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            String runSuccessSaveResid3 = this.rEngine.eval("class(saveResid2)").asString();
                            if (runSuccessSaveResid3 != null && runSuccessSaveResid3.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError21 = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
                                String checkError220 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError317 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError410 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError21);
                                this.rEngine.eval(checkError220);
                                this.rEngine.eval(checkError317);
                                this.rEngine.eval(checkError410);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat2)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (random): error");
                                    String checkError25 = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError318 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError417 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError25);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError318);
                                    this.rEngine.eval(checkError417);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        String envLevelsCommand3 = "length(heat2[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand3).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            String warningListCommand3 = "heat2[[" + i + "]]$site[[" + j + "]]";
                                            String warningList3 = this.rEngine.eval(warningListCommand3).asString();
                                            if (!warningList3.equals("empty") && !warningList3.equals("unique")) {
                                                String trialObsUsed3 = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed3);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String withBox = "FALSE";
            if (boxplotRawData) {
                withBox = "TRUE";
            }
            String withHist = "FALSE";
            if (histogramRawData) {
                withHist = "TRUE";
            }
            String ssaOut = "ssa1";
            if (genotypeFixed) {
                ssaOut = "ssa1";
            } else if (genotypeRandom) {
                ssaOut = "ssa2";
            }
            String boxHistFunc = null;
            boxHistFunc = environment == "NULL" ? "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = " + environment + ", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)" : "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = \"" + environment + "\", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)";
            System.out.println(boxHistFunc);
            this.rEngine.eval(boxHistFunc);
            String runSuccessBoxHist = this.rEngine.eval("class(boxHist)").asString();
            if (runSuccessBoxHist != null && runSuccessBoxHist.equals("try-error")) {
                System.out.println("boxplot/histogram: error");
                String checkError26 = "msg <- trimStrings(strsplit(boxHist, \":\")[[1]])";
                checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError319 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError418 = "capture.output(cat(\"*** \nERROR in graph.sea.boxhist function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError26);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError319);
                this.rEngine.eval(checkError418);
            }
            this.rEngine.eval(outspace);
            this.rEngine.eval(sep2);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiEnvironmentOneStage(String dataFileName, String outFileName, String resultFolderPath, int designIndex, String[] respvar, String environment, String[] environmentLevels, String genotype, String block, String rep, String row, String column, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean diagnosticPlot, boolean genotypeFixed, boolean performPairwise, String pairwiseAlpha, String[] genotypeLevels, String[] controlLevels, boolean compareControl, boolean performAllPairwise, boolean genotypeRandom) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String controlLevelsVector = this.inputTransform.createRVector(controlLevels);
        boolean runningFixedSuccess = true;
        boolean runningRandomSuccess = true;
        boolean printAllOutputFixed = true;
        boolean printAllOutputRandom = true;
        try {
            String diagPlotsMea1SFunc;
            String checkError4;
            String checkError3;
            String checkError2;
            String checkError;
            String funcSaveGMeansCsv;
            String checkGenoMean;
            String checkError42;
            String checkError32;
            String checkError22;
            String runSuccessSaveGEMeans;
            String checkGenoEnvMean;
            String outVarComp2;
            String outVarComp;
            String checkError43;
            String checkError33;
            String checkError23;
            String checkError5;
            String runSuccessDescStat;
            String outDescStat2;
            String outDescStat;
            String funcDesc;
            String checkError44;
            String checkError34;
            String checkError24;
            String checkError6;
            String runSuccessTS;
            String trialSum;
            String trialObsUsed;
            String trialObsRead;
            String trialSumHead;
            String funcTrialSum;
            String printError3;
            String printError2;
            String printError1;
            String allNAWarning;
            String respVarHead;
            String checkError45;
            String checkError35;
            String checkError25;
            String checkError7;
            String groupVars;
            String designUsed = new String();
            String design = new String();
            switch (designIndex) {
                case 0: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                    break;
                }
                case 1: {
                    designUsed = "Augmented RCB";
                    design = "AugRCB";
                    break;
                }
                case 2: {
                    designUsed = "Augmented Latin Square";
                    design = "AugLS";
                    break;
                }
                case 3: {
                    designUsed = "Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 4: {
                    designUsed = "Row-Column";
                    design = "RowCol";
                    break;
                }
                case 5: {
                    designUsed = "Latinized Alpha-Lattice";
                    design = "LatinAlpha";
                    break;
                }
                case 6: {
                    designUsed = "Latinized Row-Column";
                    design = "LatinRowCol";
                    break;
                }
                case 7: {
                    designUsed = "Augmented Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 8: {
                    designUsed = "Augmented Row-Column";
                    design = "RowCol";
                    break;
                }
                default: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                }
            }
            String readData = "dataMeaOneStage <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("dataMeaOneStage").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String outFile = "capture.output(cat(\"\nMULTI-ENVIRONMENT ANALYSIS (ONE-STAGE)\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outSpace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(outFile);
            this.rEngine.eval(usedDesign);
            if (genotypeFixed) {
                String funcMeaOneStageFixed = null;
                groupVars = null;
                if (design == "RCB" || design == "AugRCB") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL, rep = NULL,\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\")";
                } else if (design == "AugLS") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\", row = \"" + row + "\", column = \"" + column + "\", rep = NULL,\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (design == "Alpha" || design == "LatinAlpha" || design == "AugAlpha") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL,\"" + rep + "\",\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (design == "RowCol" || design == "LatinRowCol" || design == "AugRowCol") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcMeaOneStageFixed);
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                System.out.println(funcMeaOneStageFixed);
                String runSuccess = this.rEngine.eval("class(meaOne1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("GEOneStage.test: error");
                    checkError7 = "msg <- trimStrings(strsplit(meaOne1, \":\")[[1]])";
                    checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError45 = "capture.output(cat(\"*** \nERROR in GEOneStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError7);
                    this.rEngine.eval(checkError25);
                    this.rEngine.eval(checkError35);
                    this.rEngine.eval(checkError45);
                    runningFixedSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputFixed = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaOne1$output[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaOne1$output[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputFixed = false;
                        }
                        if (printAllOutputFixed) {
                            funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",meaOne1$output[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaOne1$output[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaOne1$output[[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError6 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError44 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError6);
                                this.rEngine.eval(checkError24);
                                this.rEngine.eval(checkError34);
                                this.rEngine.eval(checkError44);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            funcDesc = "outDesc <- try(DescriptiveStatistics(dataMeaOneStage, \"" + respvar[k] + "\", grp = NULL), silent=TRUE)";
                            this.rEngine.eval(funcDesc);
                            if (descriptiveStat) {
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError5);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError33);
                                    this.rEngine.eval(checkError43);
                                } else {
                                    this.rEngine.eval(outDescStat);
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outVarComp2 = "capture.output(meaOne1$output[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable2 = "library(lmerTest)";
                            String outAnovaTable3 = "model1b <- lmer(formula(meaOne1$output[[" + i + "]]$formula1), data = meaOne1$output[[" + i + "]]$data, REML = T)";
                            String outAnovaTable4 = "a.table <- anova(model1b)";
                            String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                            String outAnovaTable6 = "a.table<-cbind(round(a.table[,c(3,4,1,2,5)], digits=4),pvalue)";
                            String outAnovaTable7 = "colnames(a.table)<-c(\"NumDf\", \"DenDF\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Pr(>F)\")";
                            String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable10 = "detach(\"package:lmerTest\")";
                            this.rEngine.eval(outAnovaTable1);
                            this.rEngine.eval(outAnovaTable2);
                            this.rEngine.eval(outAnovaTable3);
                            this.rEngine.eval(outAnovaTable4);
                            this.rEngine.eval(outAnovaTable5);
                            this.rEngine.eval(outAnovaTable6);
                            this.rEngine.eval(outAnovaTable7);
                            this.rEngine.eval(outAnovaTable8);
                            this.rEngine.eval(outAnovaTable9);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outAnovaTable10);
                            String outTestEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne1$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne1$output[[" + i + "]]$formula3,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv4a = "testsigEnv <- data.frame(rownames(meaOne1$output[[" + i + "]]$testsig.Env), meaOne1$output[[" + i + "]]$testsig.Env)";
                            String outTestEnv4b = "colnames(testsigEnv) <- c(\"\", colnames(meaOne1$output[[" + i + "]]$testsig.Env))";
                            String outTestEnv4 = "capture.output(printDataFrame(testsigEnv, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestEnv1);
                            this.rEngine.eval(outTestEnv2);
                            this.rEngine.eval(outTestEnv3);
                            this.rEngine.eval(outTestEnv4a);
                            this.rEngine.eval(outTestEnv4b);
                            this.rEngine.eval(outTestEnv4);
                            this.rEngine.eval(outSpace);
                            String outTestGenoEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPE X ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne1$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne1$output[[" + i + "]]$formula4,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv4a = "testsigGE <- data.frame(rownames(meaOne1$output[[" + i + "]]$testsig.GenoEnv), meaOne1$output[[" + i + "]]$testsig.GenoEnv)";
                            String outTestGenoEnv4b = "colnames(testsigGE) <- c(\"\", colnames(meaOne1$output[[" + i + "]]$testsig.GenoEnv))";
                            String outTestGenoEnv4 = "capture.output(printDataFrame(testsigGE, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGenoEnv1);
                            this.rEngine.eval(outTestGenoEnv2);
                            this.rEngine.eval(outTestGenoEnv3);
                            this.rEngine.eval(outTestGenoEnv4a);
                            this.rEngine.eval(outTestGenoEnv4b);
                            this.rEngine.eval(outTestGenoEnv4);
                            this.rEngine.eval(outSpace);
                            String outGenoEnv = "capture.output(cat(\"\nGENOTYPE X ENVIRONMENT MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outGenoEnv2 = "capture.output(meaOne1$output[[" + i + "]]$wide.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outGenoEnv);
                            this.rEngine.eval(outGenoEnv2);
                            this.rEngine.eval(outSpace);
                            String outDescStat3 = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat22 = "capture.output(meaOne1$output[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat3);
                            this.rEngine.eval(outDescStat22);
                            this.rEngine.eval(outSpace);
                            String outSedStat1 = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outSedStat2 = "capture.output(meaOne1$output[[" + i + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSedStat1);
                            this.rEngine.eval(outSedStat2);
                            this.rEngine.eval(outSpace);
                            if (performPairwise) {
                                String checkError46;
                                String checkError36;
                                String checkError26;
                                String checkError8;
                                double pairwiseSig = Double.valueOf(pairwiseAlpha);
                                if (compareControl) {
                                    String funcPwC = "pwControl <- try(ssa.pairwise(meaOne1$output[[" + i + "]]$model, type = \"Dunnett\", alpha = " + pairwiseSig + ", control.level = " + controlLevelsVector + "), silent=TRUE)";
                                    String outCompareControl = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \nCompared with control(s)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outCompareControl2 = "capture.output(pwControl$result,file=\"" + outFileName + "\",append = TRUE)";
                                    System.out.println(funcPwC);
                                    this.rEngine.eval(funcPwC);
                                    this.rEngine.eval(outCompareControl);
                                    String runSuccessPwC = this.rEngine.eval("class(pwControl)").asString();
                                    if (runSuccessPwC != null && runSuccessPwC.equals("try-error")) {
                                        System.out.println("compare with control: error");
                                        checkError8 = "msg <- trimStrings(strsplit(pwControl, \":\")[[1]])";
                                        checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError46 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError8);
                                        this.rEngine.eval(checkError26);
                                        this.rEngine.eval(checkError36);
                                        this.rEngine.eval(checkError46);
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                    } else {
                                        this.rEngine.eval(outCompareControl2);
                                        String warningControlTest = this.rEngine.eval("pwControl$controlTestWarning").asString();
                                        if (!warningControlTest.equals("NONE")) {
                                            String warningCheckTest2 = "capture.output(cat(\"----- \nNOTE:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            String warningCheckTest3 = "capture.output(cat(\"" + warningControlTest + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningCheckTest2);
                                            this.rEngine.eval(warningCheckTest3);
                                        }
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                        System.out.println("pairwise control test:" + warningControlTest);
                                    }
                                } else if (performAllPairwise) {
                                    String outPerformAllPairwise = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outPerformAllPairwise);
                                    if (genotypeLevels.length > 0 & genotypeLevels.length < 16) {
                                        String funcPwAll = "pwAll <- try(ssa.pairwise(meaOne1$output[[" + i + "]]$model, type = \"Tukey\", alpha = " + pairwiseSig + ", control.level = NULL), silent=TRUE)";
                                        String outPerformAllPairwise2n = "capture.output(pwAll$result,file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(funcPwAll);
                                        String runSuccessPwAll = this.rEngine.eval("class(pwAll)").asString();
                                        if (runSuccessPwAll != null && runSuccessPwAll.equals("try-error")) {
                                            System.out.println("all pairwise: error");
                                            checkError8 = "msg <- trimStrings(strsplit(pwAll, \":\")[[1]])";
                                            checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                            checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                            checkError46 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(checkError8);
                                            this.rEngine.eval(checkError26);
                                            this.rEngine.eval(checkError36);
                                            this.rEngine.eval(checkError46);
                                        } else {
                                            this.rEngine.eval(outPerformAllPairwise2n);
                                            this.rEngine.eval(outSpace);
                                            this.rEngine.eval(outSpace);
                                        }
                                    } else {
                                        String nLevelsLarge = "capture.output(cat(\"***\nExceeded maximum number of genotypes that can be compared. \n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(nLevelsLarge);
                                    }
                                }
                            }
                            String genoEnvMeans = "genoEnvMeans <- meaOne1$output[[" + i + "]]$means.GenoEnvCode";
                            this.rEngine.eval(genoEnvMeans);
                            System.out.println(genoEnvMeans);
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    checkGenoEnvMean = this.rEngine.eval("meaOne1$meansGenoEnvWarning").asString();
                    System.out.println("checkGenoEnvMean: " + checkGenoEnvMean);
                    if (checkGenoEnvMean.equals("empty")) {
                        System.out.println("Saving geno x env means not done.");
                    } else {
                        String funcSaveGEMeansCsv = "saveGEMeans <- try(write.table(meaOne1$means.GenoEnv.all,file =\"" + resultFolderPath + "GenoEnvMeans_fixed.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGEMeansCsv);
                        this.rEngine.eval(funcSaveGEMeansCsv);
                        runSuccessSaveGEMeans = this.rEngine.eval("class(saveGEMeans)").asString();
                        if (runSuccessSaveGEMeans != null && runSuccessSaveGEMeans.equals("try-error")) {
                            System.out.println("save GxE means: error");
                            String checkError9 = "msg <- trimStrings(strsplit(saveGEMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving genotype x environment means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError9);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError32);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    checkGenoMean = this.rEngine.eval("meaOne1$meansGenoWarning").asString();
                    System.out.println("checkGenoMean: " + checkGenoMean);
                    if (checkGenoMean.equals("empty")) {
                        System.out.println("Saving geno means not done.");
                    } else {
                        funcSaveGMeansCsv = "saveGMeans <- try(write.table(meaOne1$means.Geno.all,file =\"" + resultFolderPath + "GenoMeans_fixed.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGMeansCsv);
                        this.rEngine.eval(funcSaveGMeansCsv);
                        String runSuccessSaveGMeans = this.rEngine.eval("class(saveGMeans)").asString();
                        if (runSuccessSaveGMeans != null && runSuccessSaveGMeans.equals("try-error")) {
                            System.out.println("save G means: error");
                            checkError = "msg <- trimStrings(strsplit(saveGMeans, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving genotype means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        diagPlotsMea1SFunc = "diagPlotsMea1S <- try(graph.mea1s.diagplots(dataMeaOneStage, " + respvarVector + ", is.random = FALSE, meaOne1), silent=TRUE)";
                        System.out.println(diagPlotsMea1SFunc);
                        this.rEngine.eval(diagPlotsMea1SFunc);
                        String runSuccessDiag = this.rEngine.eval("class(diagPlotsMea1S)").asString();
                        if (runSuccessDiag != null && runSuccessDiag.equals("try-error")) {
                            System.out.println("diagnostic plot: error");
                            checkError = "msg <- trimStrings(strsplit(diagPlotsMea1S, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in creating diagnostic plot (fixed genotype):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (genotypeRandom) {
                String funcMeaOneStageRandom = null;
                groupVars = null;
                if (design == "RCB" || design == "AugRCB") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL, rep = NULL,\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\")";
                } else if (design == "AugLS") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\", row = \"" + row + "\", column = \"" + column + "\", rep = NULL,\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (design == "Alpha" || design == "LatinAlpha" || design == "AugAlpha") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL,\"" + rep + "\",\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (design == "RowCol" || design == "LatinRowCol" || design == "AugRowCol") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcMeaOneStageRandom);
                this.rEngine.eval(sep2);
                this.rEngine.eval(randomHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                System.out.println(funcMeaOneStageRandom);
                String runSuccess2 = this.rEngine.eval("class(meaOne2)").asString();
                if (runSuccess2 != null && runSuccess2.equals("try-error")) {
                    System.out.println("GEOneStage.test: error");
                    checkError7 = "msg <- trimStrings(strsplit(meaOne2, \":\")[[1]])";
                    checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError45 = "capture.output(cat(\"*** \nERROR in GEOneStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError7);
                    this.rEngine.eval(checkError25);
                    this.rEngine.eval(checkError35);
                    this.rEngine.eval(checkError45);
                    runningRandomSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputRandom = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaOne2$output[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaOne2$output[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputRandom = false;
                        }
                        if (printAllOutputRandom) {
                            funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",meaOne2$output[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaOne2$output[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaOne2$output[[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError6 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError44 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError6);
                                this.rEngine.eval(checkError24);
                                this.rEngine.eval(checkError34);
                                this.rEngine.eval(checkError44);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            funcDesc = "outDesc <- DescriptiveStatistics(dataMeaOneStage, \"" + respvar[k] + "\", grp = NULL)";
                            this.rEngine.eval(funcDesc);
                            if (descriptiveStat) {
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError5);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError33);
                                    this.rEngine.eval(checkError43);
                                } else {
                                    this.rEngine.eval(outDescStat);
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outVarComp2 = "capture.output(meaOne2$output[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outTestGeno1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno4a = "testsigGeno <- data.frame(rownames(meaOne2$output[[" + i + "]]$testsig.Geno), meaOne2$output[[" + i + "]]$testsig.Geno)";
                            String outTestGeno4b = "colnames(testsigGeno) <- c(\"\", colnames(meaOne2$output[[" + i + "]]$testsig.Geno))";
                            String outTestGeno4 = "capture.output(printDataFrame(testsigGeno, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGeno1);
                            this.rEngine.eval(outTestGeno2);
                            this.rEngine.eval(outTestGeno3);
                            this.rEngine.eval(outTestGeno4a);
                            this.rEngine.eval(outTestGeno4b);
                            this.rEngine.eval(outTestGeno4);
                            this.rEngine.eval(outSpace);
                            String outTestEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula3,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv4a = "testsigEnv <- data.frame(rownames(meaOne2$output[[" + i + "]]$testsig.Env), meaOne2$output[[" + i + "]]$testsig.Env)";
                            String outTestEnv4b = "colnames(testsigEnv) <- c(\"\", colnames(meaOne2$output[[" + i + "]]$testsig.Env))";
                            String outTestEnv4 = "capture.output(printDataFrame(testsigEnv, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestEnv1);
                            this.rEngine.eval(outTestEnv2);
                            this.rEngine.eval(outTestEnv3);
                            this.rEngine.eval(outTestEnv4a);
                            this.rEngine.eval(outTestEnv4b);
                            this.rEngine.eval(outTestEnv4);
                            this.rEngine.eval(outSpace);
                            String outTestGenoEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPE X ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula4,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv4a = "testsigGenoEnv <- data.frame(rownames(meaOne2$output[[" + i + "]]$testsig.GenoEnv), meaOne2$output[[" + i + "]]$testsig.GenoEnv)";
                            String outTestGenoEnv4b = "colnames(testsigGenoEnv) <- c(\"\", colnames(meaOne2$output[[" + i + "]]$testsig.Env))";
                            String outTestGenoEnv4 = "capture.output(printDataFrame(testsigGenoEnv, border = FALSE), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGenoEnv1);
                            this.rEngine.eval(outTestGenoEnv2);
                            this.rEngine.eval(outTestGenoEnv3);
                            this.rEngine.eval(outTestGenoEnv4a);
                            this.rEngine.eval(outTestGenoEnv4b);
                            this.rEngine.eval(outTestGenoEnv4);
                            this.rEngine.eval(outSpace);
                            String outGenoEnv = "capture.output(cat(\"\nGENOTYPE X ENVIRONMENT MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outGenoEnv2 = "capture.output(meaOne2$output[[" + i + "]]$wide.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outGenoEnv);
                            this.rEngine.eval(outGenoEnv2);
                            this.rEngine.eval(outSpace);
                            String outDescStat4 = "capture.output(cat(\"\nPREDICTED GENOTYPE MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat23 = "capture.output(meaOne2$output[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat4);
                            this.rEngine.eval(outDescStat23);
                            this.rEngine.eval(outSpace);
                            String outEstHerit = "capture.output(cat(\"\nHERITABILITY:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outEstHerit2 = "capture.output(meaOne2$output[[" + i + "]]$heritability,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstHerit);
                            this.rEngine.eval(outEstHerit2);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    checkGenoEnvMean = this.rEngine.eval("meaOne2$meansGenoEnvWarning").asString();
                    System.out.println("checkGenoEnvMean: " + checkGenoEnvMean);
                    if (checkGenoEnvMean.equals("empty")) {
                        System.out.println("Saving geno x env means not done.");
                    } else {
                        String funcSaveGEMeansCsv = "saveGEMeans <- try(write.table(meaOne2$means.GenoEnv.all,file =\"" + resultFolderPath + "GenoEnvMeans_random.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGEMeansCsv);
                        this.rEngine.eval(funcSaveGEMeansCsv);
                        runSuccessSaveGEMeans = this.rEngine.eval("class(saveGEMeans)").asString();
                        if (runSuccessSaveGEMeans != null && runSuccessSaveGEMeans.equals("try-error")) {
                            System.out.println("save GxE means: error");
                            String checkError10 = "msg <- trimStrings(strsplit(saveGEMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving genotype x environment means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError10);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError32);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    checkGenoMean = this.rEngine.eval("meaOne2$meansGenoWarning").asString();
                    System.out.println("checkGenoMean: " + checkGenoMean);
                    if (checkGenoMean.equals("empty")) {
                        System.out.println("Saving geno means not done.");
                    } else {
                        funcSaveGMeansCsv = "saveGMeans <- try(write.table(meaOne2$means.Geno.all,file =\"" + resultFolderPath + "GenoMeans_random.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGMeansCsv);
                        this.rEngine.eval(funcSaveGMeansCsv);
                        String runSuccessSaveGMeans = this.rEngine.eval("class(saveGMeans)").asString();
                        if (runSuccessSaveGMeans != null && runSuccessSaveGMeans.equals("try-error")) {
                            System.out.println("save G means: error");
                            checkError = "msg <- trimStrings(strsplit(saveGMeans, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving genotype means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        diagPlotsMea1SFunc = "diagPlotsMea1S <- tryCatch(graph.mea1s.diagplots(dataMeaOneStage, " + respvarVector + ", is.random = TRUE, meaOne2), error=function(err) \"notRun\")";
                        System.out.println(diagPlotsMea1SFunc);
                        this.rEngine.eval(diagPlotsMea1SFunc);
                        String runSuccessDiag = this.rEngine.eval("class(diagPlotsMea1S)").asString();
                        if (runSuccessDiag != null && runSuccessDiag.equals("try-error")) {
                            System.out.println("diagnostic plot: error");
                            checkError = "msg <- trimStrings(strsplit(diagPlotsMea1S, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in creating diagnostic plot (fixed genotype):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (runningFixedSuccess & runningRandomSuccess) {
                String runSuccessSaveResid;
                String func1SaveResidualsCsv;
                String checkResid1;
                String runSuccessDiagPlots;
                String runSsaResid1;
                String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
                String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
                if (genotypeFixed & !genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GEOneStage_resid(meaOne1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    this.rEngine.eval(runSsaResid1);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype fixed): error");
                        checkError7 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError47 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError7);
                        this.rEngine.eval(checkError25);
                        this.rEngine.eval(checkError35);
                        this.rEngine.eval(checkError47);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_f$ge1residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError11 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError11);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            }
                        }
                    }
                } else if (!genotypeFixed & genotypeRandom) {
                    String runSsaResid2 = "resid_r <- try(GEOneStage_resid(meaOne2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid2);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype random): error");
                        checkError7 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError48 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError7);
                        this.rEngine.eval(checkError25);
                        this.rEngine.eval(checkError35);
                        this.rEngine.eval(checkError48);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_r$ge1residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError12 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError12);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            }
                        }
                    }
                } else if (genotypeFixed & genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GEOneStage_resid(meaOne1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    String runSsaResid2 = "resid_r <- try(GEOneStage_resid(meaOne2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid1);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessResidFixed = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype fixed): error");
                        String checkError13 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        String checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError49 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError13);
                        this.rEngine.eval(checkError27);
                        this.rEngine.eval(checkError37);
                        this.rEngine.eval(checkError49);
                    } else {
                        String checkResid12 = this.rEngine.eval("resid_f$ge1residWarning").asString();
                        System.out.println("checkResid1: " + checkResid12);
                        if (checkResid12.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv2 = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv2);
                            String runSuccessSaveResid2 = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid2 != null && runSuccessSaveResid2.equals("try-error")) {
                                System.out.println("save residuals: error");
                                checkError = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError);
                                this.rEngine.eval(checkError2);
                                this.rEngine.eval(checkError3);
                                this.rEngine.eval(checkError4);
                            }
                        }
                    }
                    String runSuccessResidRandom = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype random): error");
                        String checkError14 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError28 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError38 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError410 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError14);
                        this.rEngine.eval(checkError28);
                        this.rEngine.eval(checkError38);
                        this.rEngine.eval(checkError410);
                    } else {
                        String checkResid13 = this.rEngine.eval("resid_r$ge1residWarning").asString();
                        System.out.println("checkResid2: " + checkResid13);
                        if (checkResid13.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv3 = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv3);
                            String runSuccessSaveResid3 = this.rEngine.eval("class(saveResid2)").asString();
                            if (runSuccessSaveResid3 != null && runSuccessSaveResid3.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError15 = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
                                String checkError29 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError39 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError411 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError15);
                                this.rEngine.eval(checkError29);
                                this.rEngine.eval(checkError39);
                                this.rEngine.eval(checkError411);
                            }
                        }
                    }
                }
            }
            String withBox = "FALSE";
            if (boxplotRawData) {
                withBox = "TRUE";
            }
            String withHist = "FALSE";
            if (histogramRawData) {
                withHist = "TRUE";
            }
            String meaOut = "meaOne1";
            if (genotypeFixed) {
                meaOut = "meaOne1";
            } else if (genotypeRandom) {
                meaOut = "meaOne2";
            }
            String boxHistMeaFunc = "boxHistMea <- tryCatch(graph.mea1s.boxhist(dataMeaOneStage, " + respvarVector + ", " + meaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), error=function(err) \"notRun\")";
            System.out.println(boxHistMeaFunc);
            this.rEngine.eval(boxHistMeaFunc);
            String runSuccessBoxHistMea = this.rEngine.eval("boxHistMea").asString();
            if (runSuccessBoxHistMea != null && runSuccessBoxHistMea.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Boxplot(s) and histogram(s) not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
            }
            this.rEngine.eval(outSpace);
            this.rEngine.eval(sep2);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiEnvironmentSecondStage(String dataFileName, String outFileName, String resultFolderPath, String weightOption, String[] respvar, String[] standardErrors, String[] residualVariances, String[] numberOfReps, String environment, String[] environmentLevels, String genotype, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean diagnosticPlot, boolean genotypeFixed, boolean performPairwise, String pairwiseAlpha, String[] genotypeLevels, String[] controlLevels, boolean compareControl, boolean performAllPairwise, boolean genotypeRandom) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String standardErrorsVector = this.inputTransform.createRVector(standardErrors);
        String residualVariancesVector = this.inputTransform.createRVector(residualVariances);
        String numberOfRepsVector = this.inputTransform.createRVector(numberOfReps);
        boolean runningFixedSuccess = true;
        boolean runningRandomSuccess = true;
        boolean printAllOutputFixed = true;
        boolean printAllOutputRandom = true;
        try {
            String checkError4;
            String checkError3;
            String checkError2;
            String checkError;
            String runSuccessDescStat;
            String outDescStat2;
            String outDescStat;
            String funcDesc;
            String checkError42;
            String checkError32;
            String checkError22;
            String checkError5;
            String runSuccessTS;
            String trialSum;
            String trialObsUsed;
            String trialObsRead;
            String trialSumHead;
            String funcTrialSum;
            String printError3;
            String printError2;
            String printError1;
            String allNAWarning;
            String respVarHead;
            String checkError43;
            String checkError33;
            String checkError23;
            String checkError6;
            String runSuccess;
            String outWeights;
            String readData = "dataMeaTwoStage <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("dataMeaTwoStage").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String outFile = "capture.output(cat(\"\nMULTI-ENVIRONMENT ANALYSIS (TWO-STAGE)\n\"),file=\"" + outFileName + "\", append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outSpace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(outFile);
            if (weightOption == "none") {
                outWeights = "capture.output(cat(\"\nWEIGHT OPTION: NONE\n\"),file=\"" + outFileName + "\", append = TRUE)";
                this.rEngine.eval(outWeights);
                this.rEngine.eval(outSpace);
            } else {
                outWeights = "capture.output(cat(\"\nWEIGHT OPTION: 1/(sem^2)\n\"),file=\"" + outFileName + "\", append = TRUE)";
                this.rEngine.eval(outWeights);
                this.rEngine.eval(outSpace);
            }
            if (genotypeFixed) {
                String funcMeaFixed = null;
                funcMeaFixed = weightOption == "none" ? "meaTwo1 <- try(GETwoStage.test(dataMeaTwoStage, " + respvarVector + ",stderr = NULL," + residualVariancesVector + "," + numberOfRepsVector + ",\"" + genotype + "\",\"" + environment + "\", weight = \"" + weightOption + "\", is.genoRandom = FALSE), silent=TRUE)" : "meaTwo1 <- try(GETwoStage.test(dataMeaTwoStage, " + respvarVector + "," + standardErrorsVector + "," + residualVariancesVector + "," + numberOfRepsVector + ",\"" + genotype + "\",\"" + environment + "\", weight = \"" + weightOption + "\", is.genoRandom = FALSE), silent=TRUE)";
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                System.out.println(funcMeaFixed);
                this.rEngine.eval(funcMeaFixed);
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                runSuccess = this.rEngine.eval("class(meaTwo1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("MEATwoStage.test: error");
                    checkError6 = "msg <- trimStrings(strsplit(meaTwo1, \":\")[[1]])";
                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError43 = "capture.output(cat(\"*** \nERROR in GETwoStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError23);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError43);
                    runningFixedSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputFixed = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaTwo1[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaTwo1[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputFixed = false;
                        }
                        if (printAllOutputFixed) {
                            funcTrialSum = "funcTrialSum <- try(class.information(c(\"" + genotype + "\",\"" + environment + "\"),meaTwo1[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaTwo1[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaTwo1 [[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError5 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            funcDesc = "outDesc <- try(DescriptiveStatistics(dataMeaTwoStage, \"" + respvar[k] + "\", grp = NULL), silent=TRUE)";
                            this.rEngine.eval(funcDesc);
                            if (descriptiveStat) {
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError4 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError);
                                    this.rEngine.eval(checkError2);
                                    this.rEngine.eval(checkError3);
                                    this.rEngine.eval(checkError4);
                                } else {
                                    this.rEngine.eval(outDescStat);
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outVarComp2 = "capture.output(meaTwo1[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable2 = "library(lmerTest)";
                            String outAnovaTable3 = "model1b <- lmer(formula(meaTwo1[[" + i + "]]$formula1), data = meaTwo1[[" + i + "]]$data, REML = T)";
                            String outAnovaTable4 = "a.table <- anova(model1b)";
                            String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                            String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                            String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                            String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable10 = "detach(\"package:lmerTest\")";
                            this.rEngine.eval(outAnovaTable1);
                            this.rEngine.eval(outAnovaTable2);
                            this.rEngine.eval(outAnovaTable3);
                            this.rEngine.eval(outAnovaTable4);
                            this.rEngine.eval(outAnovaTable5);
                            this.rEngine.eval(outAnovaTable6);
                            this.rEngine.eval(outAnovaTable7);
                            this.rEngine.eval(outAnovaTable8);
                            this.rEngine.eval(outAnovaTable9);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outAnovaTable10);
                            String outDescStat3 = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat22 = "capture.output(meaTwo1[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat3);
                            this.rEngine.eval(outDescStat22);
                            this.rEngine.eval(outSpace);
                            String outSedStat1 = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outSedStat2 = "capture.output(meaTwo1[[" + i + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSedStat1);
                            this.rEngine.eval(outSedStat2);
                            this.rEngine.eval(outSpace);
                            if (performPairwise) {
                                String checkError44;
                                String checkError34;
                                String checkError24;
                                String checkError7;
                                double pairwiseSig = Double.valueOf(pairwiseAlpha);
                                this.rEngine.rniAssign("trmt.levels", this.rEngine.rniPutStringArray(genotypeLevels), 0L);
                                if (compareControl) {
                                    this.rEngine.rniAssign("controlLevels", this.rEngine.rniPutStringArray(controlLevels), 0L);
                                    String funcPairwise = "pwControl <- try(ssa.pairwise(meaTwo1[[" + i + "]]$model, type = \"Dunnett\", alpha = " + pairwiseSig + ", control.level = controlLevels), silent=TRUE)";
                                    String outCompareControl = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \nCompared with control(s)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outCompareControl2n = "capture.output(pwControl$result,file=\"" + outFileName + "\",append = TRUE)";
                                    System.out.println(funcPairwise);
                                    this.rEngine.eval(funcPairwise);
                                    this.rEngine.eval(outCompareControl);
                                    String runSuccessPwC = this.rEngine.eval("class(pwControl)").asString();
                                    if (runSuccessPwC != null && runSuccessPwC.equals("try-error")) {
                                        System.out.println("compare with control: error");
                                        checkError7 = "msg <- trimStrings(strsplit(pwControl, \":\")[[1]])";
                                        checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError44 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError7);
                                        this.rEngine.eval(checkError24);
                                        this.rEngine.eval(checkError34);
                                        this.rEngine.eval(checkError44);
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                    } else {
                                        this.rEngine.eval(outCompareControl2n);
                                        String warningControlTest = this.rEngine.eval("pwControl$controlTestWarning").asString();
                                        if (!warningControlTest.equals("NONE")) {
                                            String warningCheckTest2 = "capture.output(cat(\"----- \nNOTE:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            String warningCheckTest3 = "capture.output(cat(\"" + warningControlTest + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningCheckTest2);
                                            this.rEngine.eval(warningCheckTest3);
                                        }
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                        System.out.println("pairwise control test:" + warningControlTest);
                                    }
                                } else if (performAllPairwise) {
                                    String outPerformAllPairwise = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outPerformAllPairwise);
                                    if (genotypeLevels.length > 0 & genotypeLevels.length < 16) {
                                        String funcPairwise = "pwAll <- try(ssa.pairwise(meaTwo1[[" + i + "]]$model, type = \"Tukey\", alpha = " + pairwiseSig + ", control.level = NULL), silent=TRUE)";
                                        String outPerformAllPairwise2n = "capture.output(pwAll$result,file=\"" + outFileName + "\",append = TRUE)";
                                        System.out.println(funcPairwise);
                                        this.rEngine.eval(funcPairwise);
                                        String runSuccessPwAll = this.rEngine.eval("class(pwAll)").asString();
                                        if (runSuccessPwAll != null && runSuccessPwAll.equals("try-error")) {
                                            System.out.println("all pairwise: error");
                                            checkError7 = "msg <- trimStrings(strsplit(pwAll, \":\")[[1]])";
                                            checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                            checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                            checkError44 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(checkError7);
                                            this.rEngine.eval(checkError24);
                                            this.rEngine.eval(checkError34);
                                            this.rEngine.eval(checkError44);
                                        } else {
                                            this.rEngine.eval(outPerformAllPairwise2n);
                                            this.rEngine.eval(outSpace);
                                            this.rEngine.eval(outSpace);
                                        }
                                    } else {
                                        String nLevelsLarge = "capture.output(cat(\"***\nExceeded maximum number of genotypes that can be compared. \n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(nLevelsLarge);
                                    }
                                }
                            }
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    if (diagnosticPlot) {
                        String diagPlotsMea2SFunc = "diagPlotsMea2S <- try(graph.mea2s.diagplots(dataMeaTwoStage, " + respvarVector + ", is.random = FALSE, meaTwo1), silent=TRUE)";
                        System.out.println(diagPlotsMea2SFunc);
                        this.rEngine.eval(diagPlotsMea2SFunc);
                        String runSuccessDiagPlotsMea2S = this.rEngine.eval("diagPlotsMea2S").asString();
                        if (runSuccessDiagPlotsMea2S != null && runSuccessDiagPlotsMea2S.equals("notRun")) {
                            System.out.println("error");
                            this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Diagnostic plots for fixed genotype not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
                        }
                    }
                }
            }
            if (genotypeRandom) {
                String funcMeaRandom = null;
                funcMeaRandom = weightOption == "none" ? "meaTwo2 <- try(GETwoStage.test(dataMeaTwoStage, " + respvarVector + ",stderr = NULL," + residualVariancesVector + "," + numberOfRepsVector + ",\"" + genotype + "\",\"" + environment + "\", weight = \"" + weightOption + "\", is.genoRandom = TRUE), silent=TRUE)" : "meaTwo2 <- try(GETwoStage.test(dataMeaTwoStage, " + respvarVector + "," + standardErrorsVector + "," + residualVariancesVector + "," + numberOfRepsVector + ",\"" + genotype + "\",\"" + environment + "\", weight = \"" + weightOption + "\", is.genoRandom = TRUE), silent=TRUE)";
                String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
                System.out.println(funcMeaRandom);
                this.rEngine.eval(funcMeaRandom);
                this.rEngine.eval(outSpace);
                this.rEngine.eval(sep2);
                this.rEngine.eval(randomHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                runSuccess = this.rEngine.eval("class(meaTwo2)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("MEATwoStage.test: error");
                    checkError6 = "msg <- trimStrings(strsplit(meaTwo2, \":\")[[1]])";
                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError43 = "capture.output(cat(\"*** \nERROR in GETwoStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError23);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError43);
                    runningRandomSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputRandom = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaTwo2[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaTwo2[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputRandom = false;
                        }
                        if (printAllOutputRandom) {
                            funcTrialSum = "funcTrialSum <- try(class.information(c(\"" + genotype + "\",\"" + environment + "\"),meaTwo2[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaTwo2[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaTwo2[[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError5 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            if (descriptiveStat) {
                                funcDesc = "outDesc <- try(DescriptiveStatistics(dataMeaTwoStage, \"" + respvar[k] + "\", grp = NULL), silent=TRUE)";
                                this.rEngine.eval(funcDesc);
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outDescStat);
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError4 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError);
                                    this.rEngine.eval(checkError2);
                                    this.rEngine.eval(checkError3);
                                    this.rEngine.eval(checkError4);
                                } else {
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outVarComp2 = "capture.output(meaTwo2[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outTestGeno1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno2 = "capture.output(cat(\"\nFormula for Model1: \", meaTwo2[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno3 = "capture.output(cat(\"Formula for Model2: \", meaTwo2[[" + i + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno4 = "capture.output(meaTwo2[[" + i + "]]$testsig.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGeno1);
                            this.rEngine.eval(outTestGeno2);
                            this.rEngine.eval(outTestGeno3);
                            this.rEngine.eval(outTestGeno4);
                            this.rEngine.eval(outSpace);
                            String outTestEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaTwo2[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv3 = "capture.output(cat(\"Formula for Model2: \", meaTwo2[[" + i + "]]$formula3,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv4 = "capture.output(meaTwo2[[" + i + "]]$testsig.Env,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestEnv1);
                            this.rEngine.eval(outTestEnv2);
                            this.rEngine.eval(outTestEnv3);
                            this.rEngine.eval(outTestEnv4);
                            this.rEngine.eval(outSpace);
                            String outDescStat4 = "capture.output(cat(\"\nPREDICTED GENOTYPE MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat23 = "capture.output(meaTwo2[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat4);
                            this.rEngine.eval(outDescStat23);
                            this.rEngine.eval(outSpace);
                            if (weightOption == "none") {
                                String outEstHerit = "capture.output(cat(\"\nHERITABILITY = \", meaTwo2[[" + i + "]]$heritability,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outEstHerit);
                                this.rEngine.eval(outSpace);
                            }
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    if (diagnosticPlot) {
                        String diagPlotsMea2SFunc = "diagPlotsMea2S <- tryCatch(graph.mea2s.diagplots(dataMeaTwoStage, " + respvarVector + ", is.random = TRUE, meaTwo2), error=function(err) \"notRun\")";
                        System.out.println(diagPlotsMea2SFunc);
                        this.rEngine.eval(diagPlotsMea2SFunc);
                        String runSuccessDiagPlotsMea2S = this.rEngine.eval("diagPlotsMea2S").asString();
                        if (runSuccessDiagPlotsMea2S != null && runSuccessDiagPlotsMea2S.equals("notRun")) {
                            System.out.println("error");
                            this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Diagnostic plots for random genotype not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
                        }
                    }
                }
            }
            if (runningFixedSuccess & runningRandomSuccess) {
                String checkError45;
                String checkError35;
                String checkError25;
                String runSuccessSaveResid;
                String func1SaveResidualsCsv;
                String checkError36;
                String checkError26;
                String runSsaResid1;
                String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
                String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
                if (genotypeFixed & !genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GETwoStage_resid(meaTwo1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    this.rEngine.eval(runSsaResid1);
                    String runSuccessDiagPlots = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GETwoStage_resid (genotype fixed): error");
                        String checkError8 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError46 = "capture.output(cat(\"*** \nERROR in GETwoStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError8);
                        this.rEngine.eval(checkError26);
                        this.rEngine.eval(checkError36);
                        this.rEngine.eval(checkError46);
                    } else {
                        String checkResid1 = this.rEngine.eval("resid_f$ge2residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError9 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError45 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError9);
                                this.rEngine.eval(checkError25);
                                this.rEngine.eval(checkError35);
                                this.rEngine.eval(checkError45);
                            }
                        }
                    }
                } else if (!genotypeFixed & genotypeRandom) {
                    String runSsaResid2 = "resid_r <- try(GETwoStage_resid(meaTwo2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessDiagPlots = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GETwoStage_resid (genotype random): error");
                        String checkError10 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError47 = "capture.output(cat(\"*** \nERROR in GETwoStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError10);
                        this.rEngine.eval(checkError26);
                        this.rEngine.eval(checkError36);
                        this.rEngine.eval(checkError47);
                    } else {
                        String checkResid1 = this.rEngine.eval("resid_r$ge2residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError11 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError45 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError11);
                                this.rEngine.eval(checkError25);
                                this.rEngine.eval(checkError35);
                                this.rEngine.eval(checkError45);
                            }
                        }
                    }
                } else if (genotypeFixed & genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GETwoStage_resid(meaTwo1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    String runSsaResid2 = "resid_r <- try(GETwoStage_resid(meaTwo2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid1);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessResidFixed = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {
                        System.out.println("GETwoStage_resid (genotype fixed): error");
                        String checkError12 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        String checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError48 = "capture.output(cat(\"*** \nERROR in GETwoStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError12);
                        this.rEngine.eval(checkError27);
                        this.rEngine.eval(checkError37);
                        this.rEngine.eval(checkError48);
                    } else {
                        String checkResid1 = this.rEngine.eval("resid_f$ge2residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv2 = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv2);
                            String runSuccessSaveResid2 = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid2 != null && runSuccessSaveResid2.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError13 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError28 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError38 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError49 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError13);
                                this.rEngine.eval(checkError28);
                                this.rEngine.eval(checkError38);
                                this.rEngine.eval(checkError49);
                            }
                        }
                    }
                    String runSuccessResidRandom = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {
                        System.out.println("GETwoStage_resid (genotype random): error");
                        String checkError14 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError29 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError39 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError410 = "capture.output(cat(\"*** \nERROR in GETwoStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError14);
                        this.rEngine.eval(checkError29);
                        this.rEngine.eval(checkError39);
                        this.rEngine.eval(checkError410);
                    } else {
                        String checkResid1 = this.rEngine.eval("resid_r$ge2residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv3 = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv3);
                            String runSuccessSaveResid3 = this.rEngine.eval("class(saveResid2)").asString();
                            if (runSuccessSaveResid3 != null && runSuccessSaveResid3.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError15 = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
                                String checkError210 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError310 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError411 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError15);
                                this.rEngine.eval(checkError210);
                                this.rEngine.eval(checkError310);
                                this.rEngine.eval(checkError411);
                            }
                        }
                    }
                }
            }
            String withBox = "FALSE";
            if (boxplotRawData) {
                withBox = "TRUE";
            }
            String withHist = "FALSE";
            if (histogramRawData) {
                withHist = "TRUE";
            }
            String meaOut = "meaTwo1";
            if (genotypeFixed) {
                meaOut = "meaTwo1";
            } else if (genotypeRandom) {
                meaOut = "meaTwo2";
            }
            String boxHistMea2SFunc = "boxHistMea2S <- tryCatch(graph.mea2s.boxhist(dataMeaTwoStage, " + respvarVector + ", " + meaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), error=function(err) \"notRun\")";
            System.out.println(boxHistMea2SFunc);
            this.rEngine.eval(boxHistMea2SFunc);
            String runSuccessBoxHistMea2S = this.rEngine.eval("boxHistMea2S").asString();
            if (runSuccessBoxHistMea2S != null && runSuccessBoxHistMea2S.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Boxplot(s) and histogram(s) not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
            }
            this.rEngine.eval(outSpace);
            this.rEngine.eval(sep2);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiEnvironmentSecondStageVersion2(String dataFileName, String outFileName, String resultFolderPath, String[] respvar, String environment, String[] environmentLevels, String genotype, String[] mseValue, String[] repValue, boolean stabilityFinlay, boolean stabilityShukla, boolean ammi, boolean gge) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        try {
            String readData = "dataMeaTwoStage <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("dataMeaTwoStage").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String recodeData = "dataRecoded <- GETwoStage.dataRecode(dataMeaTwoStage, " + respvarVector + ", \"" + genotype + "\", \"" + environment + "\")";
                System.out.println(recodeData);
                this.rEngine.eval(recodeData);
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
                String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
                String outFile = "capture.output(cat(\"\nMULTI-ENVIRONMENT ANALYSIS (TWO-STAGE)\n\"),file=\"" + outFileName + "\", append = TRUE)";
                String sep = "capture.output(cat(\"------------------------------\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String outSpace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(usedData);
                this.rEngine.eval(outFile);
                this.rEngine.eval(outSpace);
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                int k = 0;
                while (k < respvar.length) {
                    String runSuccessPlot2;
                    String checkError4;
                    String checkError3;
                    String checkError2;
                    String checkError;
                    String outAmmi3;
                    String runSuccessAmmi;
                    String outAmmi2;
                    String outAmmi1;
                    String outRemark2;
                    String outRemark;
                    String checkError42;
                    String checkError32;
                    String checkError22;
                    String checkError5;
                    String runSuccessStab;
                    String respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(respVarHead);
                    this.rEngine.eval(sep);
                    if (stabilityFinlay) {
                        if (environmentLevels.length > 4) {
                            String funcStability1 = "funcStability1 <- try(stability.analysis(dataRecoded[[" + (k + 1) + "]]$data, \"" + respvar[k] + "\", \"" + genotype + "\", \"" + environment + "\", method = \"regression\"), silent=TRUE)";
                            String outTestStability1 = "capture.output(cat(\"\nSTABILITY ANALYSIS USING FINLAY-WILKINSON MODEL:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestStability1b = "capture.output(funcStability1[[1]][[1]]$stability,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcStability1);
                            this.rEngine.eval(outTestStability1);
                            System.out.println(funcStability1);
                            runSuccessStab = this.rEngine.eval("class(funcStability1)").asString();
                            if (runSuccessStab != null && runSuccessStab.equals("try-error")) {
                                System.out.println("stability reg: error");
                                checkError5 = "msg <- trimStrings(strsplit(funcStability1, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in stability.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                this.rEngine.eval(outTestStability1b);
                                this.rEngine.eval(outSpace);
                            }
                        } else {
                            outRemark = "capture.output(cat(\"\nSTABILITY ANALYSIS USING FINLAY-WILKINSON MODEL:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least five levels.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outRemark);
                            this.rEngine.eval(outRemark2);
                        }
                    }
                    if (stabilityShukla) {
                        if (environmentLevels.length > 4) {
                            String funcStability2 = "funcStability2 <- try(stability.analysis(dataRecoded[[" + (k + 1) + "]]$data, \"" + respvar[k] + "\", \"" + genotype + "\", \"" + environment + "\", method = \"shukla\"), silent=TRUE)";
                            String outTestStability2 = "capture.output(cat(\"\nSTABILITY ANALYSIS USING SHUKLA'S MODEL:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestStability2b = "capture.output(funcStability2[[1]][[1]]$stability,file=\"" + outFileName + "\",append = TRUE)";
                            System.out.println(funcStability2);
                            this.rEngine.eval(funcStability2);
                            this.rEngine.eval(outTestStability2);
                            runSuccessStab = this.rEngine.eval("class(funcStability2)").asString();
                            if (runSuccessStab != null && runSuccessStab.equals("try-error")) {
                                System.out.println("stability shukla: error");
                                checkError5 = "msg <- trimStrings(strsplit(funcStability2, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in stability.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                this.rEngine.eval(outTestStability2b);
                                this.rEngine.eval(outSpace);
                            }
                        } else {
                            outRemark = "capture.output(cat(\"\nSTABILITY ANALYSIS USING SHUKLA'S MODEL:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least five levels.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outRemark);
                            this.rEngine.eval(outRemark2);
                        }
                    }
                    if (ammi) {
                        if (environmentLevels.length > 2) {
                            String ammiOut = "ammiOut <- try(ammi.analysis(dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedEnv\", names(dataRecoded[[" + (k + 1) + "]]$data))], dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedGeno\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + repValue[k] + ", dataRecoded[[" + (k + 1) + "]]$data[,match(\"" + respvar[k] + "\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + mseValue[k] + ", number = FALSE, graph = \"biplot\", yVar = \"" + respvar[k] + "\"), silent=TRUE)";
                            outAmmi1 = "capture.output(cat(\"\nAMMI ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outAmmi2 = "capture.output(cat(\"Percentage of Total Variation Accounted for by the Principal Components: \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(ammiOut);
                            this.rEngine.eval(outAmmi1);
                            System.out.println(ammiOut);
                            runSuccessAmmi = this.rEngine.eval("class(ammiOut)").asString();
                            if (runSuccessAmmi != null && runSuccessAmmi.equals("try-error")) {
                                System.out.println("ammi: error");
                                checkError5 = "msg <- trimStrings(strsplit(ammiOut, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in ammi.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                outAmmi3 = "capture.output(ammiOut$analysis,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outAmmi2);
                                this.rEngine.eval(outAmmi3);
                                this.rEngine.eval(outSpace);
                            }
                        } else {
                            outRemark = "capture.output(cat(\"\nAMMI ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least three levels.***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outRemark);
                            this.rEngine.eval(outRemark2);
                        }
                    }
                    if (gge) {
                        if (environmentLevels.length > 2) {
                            String ggeOut = "ggeOut <- try(gge.analysis(dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedEnv\", names(dataRecoded[[" + (k + 1) + "]]$data))], dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedGeno\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + repValue[k] + ", dataRecoded[[" + (k + 1) + "]]$data[,match(\"" + respvar[k] + "\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + mseValue[k] + ", number = FALSE, graph = \"biplot\", yVar = \"" + respvar[k] + "\", f=0.5), silent=TRUE)";
                            outAmmi1 = "capture.output(cat(\"\nGGE ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outAmmi2 = "capture.output(cat(\"Percentage of Total Variation Accounted for by the Principal Components: \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(ggeOut);
                            this.rEngine.eval(outAmmi1);
                            System.out.println(ggeOut);
                            runSuccessAmmi = this.rEngine.eval("class(ggeOut)").asString();
                            if (runSuccessAmmi != null && runSuccessAmmi.equals("try-error")) {
                                System.out.println("gge1: error");
                                checkError5 = "msg <- trimStrings(strsplit(ggeOut, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in gge.analysis2 function (f=0.5):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            } else {
                                outAmmi3 = "capture.output(ggeOut$analysis,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outAmmi2);
                                this.rEngine.eval(outAmmi3);
                                this.rEngine.eval(outSpace);
                            }
                            String ggeOut2 = "ggeOut2 <- try(gge.analysis(dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedEnv\", names(dataRecoded[[" + (k + 1) + "]]$data))], dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedGeno\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + repValue[k] + ", dataRecoded[[" + (k + 1) + "]]$data[,match(\"" + respvar[k] + "\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + mseValue[k] + ", number = FALSE, graph = \"biplot\", yVar = \"" + respvar[k] + "\", f=0), silent=TRUE)";
                            this.rEngine.eval(ggeOut2);
                            System.out.println(ggeOut2);
                            String runSuccessAmmi2 = this.rEngine.eval("class(ggeOut2)").asString();
                            if (runSuccessAmmi2 != null && runSuccessAmmi2.equals("try-error")) {
                                System.out.println("gge2: error");
                                String checkError6 = "msg <- trimStrings(strsplit(ggeOut2, \":\")[[1]])";
                                String checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError43 = "capture.output(cat(\"*** \nERROR in gge.analysis2 function (f=0):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError6);
                                this.rEngine.eval(checkError23);
                                this.rEngine.eval(checkError33);
                                this.rEngine.eval(checkError43);
                            }
                            String ggeOut3 = "ggeOut3 <- try(gge.analysis(dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedEnv\", names(dataRecoded[[" + (k + 1) + "]]$data))], dataRecoded[[" + (k + 1) + "]]$data[,match(\"CodedGeno\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + repValue[k] + ", dataRecoded[[" + (k + 1) + "]]$data[,match(\"" + respvar[k] + "\", names(dataRecoded[[" + (k + 1) + "]]$data))], " + mseValue[k] + ", number = FALSE, graph = \"biplot\", yVar = \"" + respvar[k] + "\", f=1), silent=TRUE)";
                            this.rEngine.eval(ggeOut3);
                            System.out.println(ggeOut3);
                            String runSuccessAmmi3 = this.rEngine.eval("class(ggeOut3)").asString();
                            if (runSuccessAmmi3 != null && runSuccessAmmi3.equals("try-error")) {
                                System.out.println("gge2: error");
                                checkError = "msg <- trimStrings(strsplit(ggeOut2, \":\")[[1]])";
                                checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError4 = "capture.output(cat(\"*** \nERROR in gge.analysis2 function (f=1):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError);
                                this.rEngine.eval(checkError2);
                                this.rEngine.eval(checkError3);
                                this.rEngine.eval(checkError4);
                            }
                        } else {
                            outRemark = "capture.output(cat(\"\nGGE ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least three levels.***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outRemark);
                            this.rEngine.eval(outRemark2);
                        }
                    }
                    String recodedLevels = this.rEngine.eval("dataRecoded[[" + (k + 1) + "]]$recodedLevels").asBool().toString();
                    System.out.println("recodedLevels: " + recodedLevels);
                    if (recodedLevels.equals("TRUE")) {
                        String outLegends = "capture.output(cat(\"\nCODES USED IN GRAPHS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        String outLegends2 = "capture.output(dataRecoded[[" + (k + 1) + "]]$newCodingGeno,file=\"" + outFileName + "\",append = TRUE)";
                        String outLegends3 = "capture.output(dataRecoded[[" + (k + 1) + "]]$newCodingEnv,file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(outLegends);
                        this.rEngine.eval(outLegends2);
                        this.rEngine.eval(outSpace);
                        this.rEngine.eval(outLegends3);
                        this.rEngine.eval(outSpace);
                        this.rEngine.eval(outSpace);
                    } else {
                        this.rEngine.eval(outSpace);
                    }
                    String responsePlot1 = "dataCoded <- dataRecoded[[" + (k + 1) + "]]$data";
                    String responsePlot2 = "nlevelsEnv <- dataRecoded[[" + (k + 1) + "]]$nlevelsEnv";
                    String responsePlot3 = "nlevelsGeno <- dataRecoded[[" + (k + 1) + "]]$nlevelsGeno";
                    String responsePlot4 = "resPlot1 <- try(GraphLine(data=dataCoded, outputPath=\"" + resultFolderPath + "\", yVars =c(\"" + respvar[k] + "\"), xVar =c(\"CodedGeno\"), lineVars =c(\"CodedEnv\"), mTitle =\"Response Plot of " + respvar[k] + "\", yAxisLab =c(\"" + respvar[k] + "\"), xAxisLab =\"" + genotype + "\", yMinValue = c(NA), yMaxValue = c(NA), axisLabelStyle = 2, byVar = NULL, plotCol = c(1:nlevelsEnv), showLineLabels =TRUE, showLeg = FALSE, boxed = TRUE, linePtTypes=rep(\"b\", nlevelsEnv), lineTypes=rep(1, nlevelsEnv), lineWidths=rep(1, nlevelsEnv), pointChars=rep(\"  \", nlevelsEnv), pointCharSizes=rep(1, nlevelsEnv), multGraphs =FALSE), silent = TRUE)";
                    String responsePlot5 = "resPlot2 <- try(GraphLine(data=dataCoded, outputPath=\"" + resultFolderPath + "\", yVars =c(\"" + respvar[k] + "\"), xVar =c(\"CodedEnv\"), lineVars =c(\"CodedGeno\"), mTitle =\"Response Plot of " + respvar[k] + "\", yAxisLab =c(\"" + respvar[k] + "\"), xAxisLab =\"" + environment + "\", yMinValue = c(NA), yMaxValue = c(NA), axisLabelStyle = 2, byVar = NULL, plotCol = c(1:nlevelsGeno), showLineLabels =TRUE, showLeg = FALSE, boxed = TRUE, linePtTypes=rep(\"b\", nlevelsGeno), lineTypes=rep(1, nlevelsGeno), lineWidths=rep(1, nlevelsGeno), pointChars=rep(\"  \", nlevelsGeno), pointCharSizes=rep(1, nlevelsGeno), multGraphs =FALSE), silent = TRUE)";
                    System.out.println(responsePlot1);
                    System.out.println(responsePlot2);
                    System.out.println(responsePlot3);
                    System.out.println(responsePlot4);
                    System.out.println(responsePlot5);
                    this.rEngine.eval(responsePlot1);
                    this.rEngine.eval(responsePlot2);
                    this.rEngine.eval(responsePlot3);
                    this.rEngine.eval(responsePlot4);
                    this.rEngine.eval(responsePlot5);
                    String runSuccessPlot1 = this.rEngine.eval("class(resPlot1)").asString();
                    if (runSuccessPlot1 != null && runSuccessPlot1.equals("try-error")) {
                        System.out.println("response plot geno: error");
                        String checkError7 = "msg <- trimStrings(strsplit(resPlot1, \":\")[[1]])";
                        String checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError44 = "capture.output(cat(\"*** \nERROR in GraphLine function (geno):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError7);
                        this.rEngine.eval(checkError24);
                        this.rEngine.eval(checkError34);
                        this.rEngine.eval(checkError44);
                    }
                    if ((runSuccessPlot2 = this.rEngine.eval("class(resPlot2)").asString()) != null && runSuccessPlot2.equals("try-error")) {
                        System.out.println("response plot env: error");
                        checkError = "msg <- trimStrings(strsplit(resPlot2, \":\")[[1]])";
                        checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        checkError4 = "capture.output(cat(\"*** \nERROR in GraphLine function (env):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError);
                        this.rEngine.eval(checkError2);
                        this.rEngine.eval(checkError3);
                        this.rEngine.eval(checkError4);
                    }
                    ++k;
                }
                this.rEngine.eval(outSpace);
                this.rEngine.eval(sep2);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doStabilityModels(String dataFileName, String outFileName, String[] respvar, String environment, String genotype, boolean stabilityFinlay, boolean stabilityShukla) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String doFinlay = new Boolean(stabilityFinlay).toString().toUpperCase();
        String doShukla = new Boolean(stabilityShukla).toString().toUpperCase();
        try {
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("data").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String callStabilityTest = "result <- try(stabilityTest(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", environment=\"" + environment + "\", genotype=\"" + genotype + "\", useFinlay = " + doFinlay + ", useShukla = " + doShukla + "), silent=TRUE)";
                System.out.println(callStabilityTest);
                this.rEngine.eval(callStabilityTest);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiplicativeModels(String dataFileName, String outFileName, String resultFolderPath, String[] respvar, String environment, String genotype, String[] numberOfReps, String[] residualVariances, boolean responsePlot, boolean doAMMI, boolean biplotPC12, boolean biplotPC13, boolean biplotPC23, boolean ammi1Biplot, boolean adaptationMap, boolean doGGE, boolean graphSymmetricView, boolean graphEnvironmentView, boolean graphGenotypicView) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String numberOfRepsVector = this.inputTransform.createRVector(numberOfReps);
        String residualVariancesVector = this.inputTransform.createRVector(residualVariances);
        String doResponsePlot = new Boolean(responsePlot).toString().toUpperCase();
        String doAMMIAnalysis = new Boolean(doAMMI).toString().toUpperCase();
        String dobiplotPC12 = new Boolean(biplotPC12).toString().toUpperCase();
        String dobiplotPC13 = new Boolean(biplotPC13).toString().toUpperCase();
        String dobiplotPC23 = new Boolean(biplotPC23).toString().toUpperCase();
        String doAmmi1Biplot = new Boolean(ammi1Biplot).toString().toUpperCase();
        String doAdaptationMap = new Boolean(adaptationMap).toString().toUpperCase();
        String doGGEAnalysis = new Boolean(doGGE).toString().toUpperCase();
        String doGraphSymmetricView = new Boolean(graphSymmetricView).toString().toUpperCase();
        String doGraphEnvironmentView = new Boolean(graphEnvironmentView).toString().toUpperCase();
        String doGraphGenotypicView = new Boolean(graphGenotypicView).toString().toUpperCase();
        try {
            String setWd = "setwd(\"" + resultFolderPath + "\")";
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(setWd);
            System.out.println(readData);
            this.rEngine.eval(setWd);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("data").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String callMultiplicativeTest = "result <- try(multiplicativeModels(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", environment=\"" + environment + "\", genotype=\"" + genotype + "\", numberOfReps = " + numberOfRepsVector + ", residualVariances = " + residualVariancesVector + ", responsePlot = " + doResponsePlot + ",  doAMMI = " + doAMMIAnalysis + ", biplotPC12 = " + dobiplotPC12 + ", biplotPC13 = " + dobiplotPC13 + ", biplotPC23 = " + dobiplotPC23 + ", ammi1 = " + doAmmi1Biplot + ", adaptMap = " + doAdaptationMap + ", doGGE = " + doGGEAnalysis + ", graphSym = " + doGraphSymmetricView + ", graphEnv = " + doGraphEnvironmentView + ", graphGeno = " + doGraphGenotypicView + "), silent=TRUE)";
                System.out.println(callMultiplicativeTest);
                this.rEngine.eval(callMultiplicativeTest);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiplicativeModelsVersion2(String dataFileName, String outFileName, String resultFolderPath, String[] respvar, String environment, String genotype, String[] numberOfReps, String[] residualVariances, boolean responsePlot, boolean doAMMI, boolean biplotPC12, boolean biplotPC13, boolean biplotPC23, boolean ammi1Biplot, boolean adaptationMap, boolean doGGE, boolean graphSymmetricView, boolean graphEnvironmentView, boolean graphGenotypicView) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String numberOfRepsVector = this.inputTransform.createRNumVector(numberOfReps);
        String residualVariancesVector = this.inputTransform.createRNumVector(residualVariances);
        String doResponsePlot = new Boolean(responsePlot).toString().toUpperCase();
        String doAMMIAnalysis = new Boolean(doAMMI).toString().toUpperCase();
        String dobiplotPC12 = new Boolean(biplotPC12).toString().toUpperCase();
        String dobiplotPC13 = new Boolean(biplotPC13).toString().toUpperCase();
        String dobiplotPC23 = new Boolean(biplotPC23).toString().toUpperCase();
        String doAmmi1Biplot = new Boolean(ammi1Biplot).toString().toUpperCase();
        String doAdaptationMap = new Boolean(adaptationMap).toString().toUpperCase();
        String doGGEAnalysis = new Boolean(doGGE).toString().toUpperCase();
        String doGraphSymmetricView = new Boolean(graphSymmetricView).toString().toUpperCase();
        String doGraphEnvironmentView = new Boolean(graphEnvironmentView).toString().toUpperCase();
        String doGraphGenotypicView = new Boolean(graphGenotypicView).toString().toUpperCase();
        try {
            String setWd = "setwd(\"" + resultFolderPath + "\")";
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(setWd);
            System.out.println(readData);
            this.rEngine.eval(setWd);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("data").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String callStabilityTest = "result <- try(multiplicativeModels(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", environment=\"" + environment + "\", genotype=\"" + genotype + "\", numberOfReps = " + numberOfRepsVector + ", residualVariances = " + residualVariancesVector + ", responsePlot = " + doResponsePlot + ",  doAMMI = " + doAMMIAnalysis + ", biplotPC12 = " + dobiplotPC12 + ", biplotPC13 = " + dobiplotPC13 + ", biplotPC23 = " + dobiplotPC23 + ", ammi1 = " + doAmmi1Biplot + ", adaptMap = " + doAdaptationMap + ", doGGE = " + doGGEAnalysis + ", graphSym = " + doGraphSymmetricView + ", graphEnv = " + doGraphEnvironmentView + ", graphGeno = " + doGraphGenotypicView + "), silent=TRUE)";
                System.out.println(callStabilityTest);
                this.rEngine.eval(callStabilityTest);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPlsRegression(String dataFileName, String outFileName, String resultFolderPath, String[] respvar, String environment, String genotype, String covariateData, String covariateEnvironment, String covariateGenotype) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        try {
            String setWd = "setwd(\"" + resultFolderPath + "\")";
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(setWd);
            System.out.println(readData);
            this.rEngine.eval(setWd);
            this.rEngine.eval(readData);
            String readData2 = "dataCov <- read.csv(\"" + covariateData + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData2);
            this.rEngine.eval(readData2);
            String runSuccessData = this.rEngine.eval("data").asString();
            String runSuccessData2 = this.rEngine.eval("dataCov").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else if (runSuccessData2 != null && runSuccessData2.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading covariate data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String callPlsRegression = null;
                if (covariateEnvironment.equals("NULL")) {
                    callPlsRegression = "result <- try(plsRegression(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", genotype=\"" + genotype + "\", environment=\"" + environment + "\", covariateData = dataCov, covariateEnvironment = " + covariateEnvironment + ", covariateGenotype = \"" + covariateGenotype + "\"), silent=TRUE)";
                }
                if (covariateGenotype.equals("NULL")) {
                    callPlsRegression = "result <- try(plsRegression(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", genotype=\"" + genotype + "\", environment=\"" + environment + "\", covariateData = dataCov, covariateEnvironment = \"" + covariateEnvironment + "\", covariateGenotype = " + covariateGenotype + "), silent=TRUE)";
                }
                System.out.println(callPlsRegression);
                this.rEngine.eval(callPlsRegression);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFactorialRegression(String dataFileName, String outFileName, String resultFolderPath, String[] respvar, String environment, String genotype, String covariateData, String covariateEnvironment, String covariateGenotype) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        try {
            String setWd = "setwd(\"" + resultFolderPath + "\")";
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(setWd);
            System.out.println(readData);
            this.rEngine.eval(setWd);
            this.rEngine.eval(readData);
            String readData2 = "dataCov <- read.csv(\"" + covariateData + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData2);
            this.rEngine.eval(readData2);
            String runSuccessData = this.rEngine.eval("data").asString();
            String runSuccessData2 = this.rEngine.eval("dataCov").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else if (runSuccessData2 != null && runSuccessData2.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading covariate data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String callFactorialRegression = null;
                if (covariateEnvironment.equals("NULL")) {
                    callFactorialRegression = "result <- try(factorialRegression(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", genotype=\"" + genotype + "\", environment=\"" + environment + "\", covariateData = dataCov, covariateEnvironment = " + covariateEnvironment + ", covariateGenotype = \"" + covariateGenotype + "\"), silent=TRUE)";
                }
                if (covariateGenotype.equals("NULL")) {
                    callFactorialRegression = "result <- try(factorialRegression(data, outFileName=\"" + outFileName + "\", respvar=" + respvarVector + ", genotype=\"" + genotype + "\", environment=\"" + environment + "\", covariateData = dataCov, covariateEnvironment = \"" + covariateEnvironment + "\", covariateGenotype = " + covariateGenotype + "), silent=TRUE)";
                }
                System.out.println(callFactorialRegression);
                this.rEngine.eval(callFactorialRegression);
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doSelectionIndex(String dataFileName, String outFileName, String resultFolderPath, int selectionIndex, int designIndex, boolean basisCorrelation, String weightsFileName, String markersFileName, String qtlFileName, int percentSelected) {
        try {
            String selectionIndexUsed = new String();
            switch (selectionIndex) {
                case 0: {
                    selectionIndexUsed = "Smith Selection Index";
                    break;
                }
                case 1: {
                    selectionIndexUsed = "Eigen Selection Index Method (ESIM)";
                    break;
                }
                case 2: {
                    selectionIndexUsed = "Restrictive Kempthorne and Nordskog Selection Index";
                    break;
                }
                case 3: {
                    selectionIndexUsed = "Restrictive Eigen Selection Index Method";
                    break;
                }
                case 4: {
                    selectionIndexUsed = "Lande and Thompson Selection Index";
                    break;
                }
                case 5: {
                    selectionIndexUsed = "Molecular Eigen Selection Index Method";
                    break;
                }
                default: {
                    selectionIndexUsed = "Smith Selection Index";
                }
            }
            String designUsed = new String();
            String design = new String();
            switch (designIndex) {
                case 0: {
                    design = "lattice";
                    designUsed = "Lattice";
                    break;
                }
                case 1: {
                    design = "rcb";
                    designUsed = "Randomized Complete Block (RCB)";
                }
            }
            String basisSI = null;
            basisSI = basisCorrelation ? "TRUE" : "FALSE";
            String setWd = "setwd(\"" + resultFolderPath + "\")";
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String usedSelInd = "capture.output(cat(\"\n" + selectionIndexUsed + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(setWd);
            this.rEngine.eval(usedData);
            this.rEngine.eval(usedSelInd);
            this.rEngine.eval(usedDesign);
            System.out.println(setWd);
            String funcSelInd = null;
            switch (selectionIndex) {
                case 0: {
                    funcSelInd = "index <- tryCatch(SmithIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE), error=function(err) \"notRun\")";
                    break;
                }
                case 1: {
                    funcSelInd = "index <- tryCatch(ESIMIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE), error=function(err) \"notRun\")";
                    break;
                }
                case 2: {
                    funcSelInd = "index <- tryCatch(KNIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE), error=function(err) \"notRun\")";
                    break;
                }
                case 3: {
                    funcSelInd = "index <- tryCatch(RESIMIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE), error=function(err) \"notRun\")";
                    break;
                }
                case 4: {
                    funcSelInd = "index <- tryCatch(LTIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE, \"" + markersFileName + "\", \"" + qtlFileName + "\"), error=function(err) \"notRun\")";
                    break;
                }
                case 5: {
                    funcSelInd = "index <- tryCatch(MESIMIndex_2(\"" + dataFileName + "\", \"" + weightsFileName + "\", " + percentSelected + ", \"" + design + "\", corr = " + basisSI + ", rawdata = TRUE, \"" + markersFileName + "\", \"" + qtlFileName + "\"), error=function(err) \"notRun\")";
                }
            }
            this.rEngine.eval(funcSelInd);
            System.out.println(funcSelInd);
            String runSuccessSelInd = this.rEngine.eval("index").asString();
            if (runSuccessSelInd != null && runSuccessSelInd.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Selection index not computed.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
            } else {
                String matrixName = "COVARIANCE";
                if (basisCorrelation) {
                    matrixName = "CORRELATION";
                }
                String genoMatrix = "capture.output(cat(\"\nGENETIC " + matrixName + " MATRIX\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String genoMatrix2 = "capture.output(index$MVGeno,file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(genoMatrix);
                this.rEngine.eval(genoMatrix2);
                String phenoMatrix = "capture.output(cat(\"\n\nPHENOTYPIC " + matrixName + " MATRIX\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String phenoMatrix2 = "capture.output(index$MVPheno,file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(phenoMatrix);
                this.rEngine.eval(phenoMatrix2);
                if (selectionIndex == 4 | selectionIndex == 5) {
                    String moleCovMatrix = "capture.output(cat(\"\n\nMOLECULAR COVARIANCE MATRIX\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    String moleCovMatrix2 = "capture.output(index$MVMole,file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(moleCovMatrix);
                    this.rEngine.eval(moleCovMatrix2);
                }
                String covIndBv = "capture.output(cat(\"\n\nCOVARIANCE BETWEEN SELECTION INDEX AND BREEDING VALUE: \",index$CovIndBV[[1]]),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(covIndBv);
                String varInd = "capture.output(cat(\"\n\nVARIANCE OF THE SELECTION INDEX: \", index$VarInd[[1]]),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(varInd);
                String varBv = "capture.output(cat(\"\n\nVARIANCE OF THE BREEDING VALUE: \", index$VarBV[[1]]),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(varBv);
                String corrIndBv = "capture.output(cat(\"\n\nCORRELATION BETWEEN SELECTION INDEX AND BREEDING VALUE: \", index$CorrIndBV[[1]]),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(corrIndBv);
                String selGenMatrix = "capture.output(cat(\"\n\nVALUES OF THE TRAITS, SELECTION INDEX, MEANS, GAINS FOR THE " + percentSelected + "% SELECTED INDIVIDUALS\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String selGenMatrix2 = "capture.output(index$SelGen,file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(selGenMatrix);
                this.rEngine.eval(selGenMatrix2);
                String allGenMatrix = "capture.output(cat(\"\n\nVALUES OF THE TRAITS AND THE SELECTION INDEX FOR ALL INDIVIDUALS\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String allGenMatrix2 = "capture.output(index$AllGen,file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(allGenMatrix);
                this.rEngine.eval(allGenMatrix2);
                this.rEngine.eval(sep);
                String funcSaveToCsv = "savePctSel <- tryCatch(write.csv(index$SelGen,file =\"" + resultFolderPath + "selectedGenotypes.csv\",row.names=T), error=function(err) \"notRun\")";
                this.rEngine.eval(funcSaveToCsv);
                String runSuccessSavePctSel = this.rEngine.eval("savePctSel").asString();
                if (runSuccessSavePctSel != null && runSuccessSavePctSel.equals("notRun")) {
                    System.out.println("error");
                    this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Percentage selected not saved.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
                }
            }
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doQtl(String dataFileName, boolean isInputRawData, String outFileName, String resultFolderPath, String genotypicDataFileName, String mapFileName, int designIndex, String[] respvar, String block, String rep, String row, String column, String genotype, String environment, String[] environmentLevels, String selectedEnvironmentLevel, boolean heterozygousPresent, String crossType, String step, String windowSize, String minDistance, String qtlMethod, boolean thresholdLiJi, String thresholdNumericValue, boolean estimatePlotMarkerMap, double allelicDiffThreshold, double cutOffMissingData, double significanceLevelChiSquare) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String[] respVarMean = new String[respvar.length];
        try {
            String dire3 = null;
            String environment_In = null;
            environment_In = !environment.equals("NULL") ? "\"" + environment + "\"" : "NULL";
            String[] envts = null;
            if (environmentLevels != null) {
                envts = environmentLevels.length > 0 ? environmentLevels : new String[]{"1"};
            }
            String specEnv = "NULL";
            int numEnvts = 1;
            if (!environment.equals("NULL")) {
                if (selectedEnvironmentLevel.equals("[ALL]")) {
                    numEnvts = envts.length;
                }
            } else {
                numEnvts = 1;
            }
            String designUsed = new String();
            String design = new String();
            boolean readingDataMeansSuccess = true;
            if (isInputRawData) {
                boolean printAllOutputFixed = true;
                String readData = "data <- tryCatch(read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\"), error=function(err) \"notRun\")";
                System.out.println(readData);
                this.rEngine.eval(readData);
                if (!selectedEnvironmentLevel.equals("[ALL]")) {
                    String dire2 = "data<-tryCatch(data[which(data[match(\"" + environment + "\", names(data))] == \"" + selectedEnvironmentLevel + "\"),], error=function(err) \"notRun\")";
                    System.out.println(dire2);
                    this.rEngine.eval(dire2);
                }
                switch (designIndex) {
                    case 0: {
                        designUsed = "Randomized Complete Block (RCB)";
                        design = "RCB";
                        break;
                    }
                    case 1: {
                        designUsed = "Augmented RCB";
                        design = "AugRCB";
                        break;
                    }
                    case 2: {
                        designUsed = "Augmented Latin Square";
                        design = "AugLS";
                        break;
                    }
                    case 3: {
                        designUsed = "Alpha-Lattice";
                        design = "Alpha";
                        break;
                    }
                    case 4: {
                        designUsed = "Row-Column";
                        design = "RowCol";
                        break;
                    }
                    default: {
                        designUsed = "Randomized Complete Block (RCB)";
                        design = "RCB";
                    }
                }
                String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
                String outFile = "capture.output(cat(\"\nSINGLE-ENVIRONMENT ANALYSIS\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(usedData);
                this.rEngine.eval(outFile);
                this.rEngine.eval(usedDesign);
                String runSuccessData = this.rEngine.eval("data").asString();
                if (runSuccessData != null && runSuccessData.equals("notRun")) {
                    System.out.println("error");
                    this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
                }
                String funcSeaFixed = null;
                String groupVars = null;
                if (design == "RCB" || design == "AugRCB") {
                    funcSeaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL, env = " + environment_In + ", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                } else if (design == "AugLS") {
                    funcSeaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL, env = " + environment_In + ", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (design == "Alpha") {
                    funcSeaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\", env = " + environment_In + ", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (design == "RowCol") {
                    funcSeaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\", env = " + environment_In + ", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                System.out.println(funcSeaFixed);
                this.rEngine.eval(funcSeaFixed);
                String runSuccess = this.rEngine.eval("class(ssa1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("ssa.test: error");
                    String checkError = "msg <- trimStrings(strsplit(ssa1, \":\")[[1]])";
                    String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError);
                    this.rEngine.eval(checkError2);
                    this.rEngine.eval(checkError3);
                    this.rEngine.eval(checkError4);
                } else {
                    dire3 = "dataMeans<-tryCatch(ssa1$meansse, error=function(err) \"notRun\")";
                    this.rEngine.eval(dire3);
                    System.out.println(dire3);
                    String runSuccessDire3 = this.rEngine.eval("dataMeans").asString();
                    if (runSuccessDire3 != null && runSuccessDire3.equals("notRun")) {
                        System.out.println("error with capturing dataMeans");
                        readingDataMeansSuccess = false;
                    }
                    int i = 0;
                    while (i < respvar.length) {
                        respVarMean[i] = String.valueOf(respvar[i]) + "_Mean";
                        ++i;
                    }
                    String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
                    String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    int k = 0;
                    while (k < respvar.length) {
                        int i2 = k + 1;
                        String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        this.rEngine.eval(outspace);
                        String funcDesc = "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", \"" + environment + "\"), silent=TRUE)";
                        System.out.println(funcDesc);
                        this.rEngine.eval(funcDesc);
                        String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                        String runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                        if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                            System.out.println("desc stat: error");
                            String checkError = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                            String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            String checkError4 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        } else {
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outDescStat);
                            this.rEngine.eval(outDescStat2);
                            this.rEngine.eval(outspace);
                        }
                        int m = 0;
                        while (m < numEnvts) {
                            printAllOutputFixed = true;
                            int j = m + 1;
                            String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,ssa1$output[[" + i2 + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(envtHead);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                            double nrowData = this.rEngine.eval("ssa1$output[[" + i2 + "]]$site[[" + j + "]]$responseRate").asDouble();
                            if (nrowData < 0.8) {
                                String allNAWarning = this.rEngine.eval("ssa1$output[[" + i2 + "]]$site[[" + j + "]]$manyNAWarning").asString();
                                String printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                String printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                String printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(printError1);
                                this.rEngine.eval(printError2);
                                this.rEngine.eval(printError3);
                                this.rEngine.eval(printError1);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outspace);
                                printAllOutputFixed = false;
                            }
                            if (printAllOutputFixed) {
                                String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa1$output[[" + i2 + "]]$site[[" + j + "]]$data), silent=TRUE)";
                                String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa1$output[[" + i2 + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa1$output[[" + i2 + "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(funcTrialSum);
                                String runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                                if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                    System.out.println("class info: error");
                                    String checkError = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                    String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError4 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError);
                                    this.rEngine.eval(checkError2);
                                    this.rEngine.eval(checkError3);
                                    this.rEngine.eval(checkError4);
                                } else {
                                    this.rEngine.eval(trialSumHead);
                                    this.rEngine.eval(trialObsRead);
                                    this.rEngine.eval(trialObsUsed);
                                    this.rEngine.eval(trialSum);
                                    this.rEngine.eval(outspace);
                                }
                                String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outVarComp2 = "capture.output(ssa1$output[[" + i2 + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outspace);
                                String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable2 = "library(lmerTest)";
                                String outAnovaTable3 = "model1b <- lmer(formula(ssa1$output[[" + i2 + "]]$site[[" + j + "]]$formula1), data = ssa1$output[[" + i2 + "]]$site[[" + j + "]]$data, REML = T)";
                                String outAnovaTable4 = "a.table <- anova(model1b)";
                                String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                this.rEngine.eval(outAnovaTable1);
                                this.rEngine.eval(outAnovaTable2);
                                this.rEngine.eval(outAnovaTable3);
                                this.rEngine.eval(outAnovaTable4);
                                this.rEngine.eval(outAnovaTable5);
                                this.rEngine.eval(outAnovaTable6);
                                this.rEngine.eval(outAnovaTable7);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable8);
                                this.rEngine.eval(outAnovaTable9);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable10);
                                String outLSMeans1 = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outLSMeans2 = "capture.output(ssa1$output[[" + i2 + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outLSMeans1);
                                this.rEngine.eval(outLSMeans2);
                                this.rEngine.eval(outspace);
                                String outsedTable = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outsedTable2 = "capture.output(ssa1$output[[" + i2 + "]]$site[[" + j + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outsedTable);
                                this.rEngine.eval(outsedTable2);
                                this.rEngine.eval(outspace);
                            }
                            ++m;
                        }
                        ++k;
                    }
                    this.rEngine.eval(outspace);
                    this.rEngine.eval(sep2);
                }
            }
            if (!isInputRawData) {
                dire3 = "dataMeans<- tryCatch(read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\"), error=function(err) \"notRun\")";
                this.rEngine.eval(dire3);
                System.out.println(dire3);
                String runSuccessDire3 = this.rEngine.eval("dataMeans").asString();
                if (runSuccessDire3 != null && runSuccessDire3.equals("notRun")) {
                    System.out.println("error with capturing dataMeans");
                    readingDataMeansSuccess = false;
                }
                int i = 0;
                while (i < respvar.length) {
                    respVarMean[i] = respvar[i];
                    ++i;
                }
            }
            if (readingDataMeansSuccess) {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                String dataPrepFunction = "dataPrepResult<-QTL.dataprep(dataMeans, \"" + genotypicDataFileName + "\", \"" + mapFileName + "\", \"" + genotype + "\")";
                this.rEngine.eval(setWd);
                this.rEngine.eval(dataPrepFunction);
                System.out.println(setWd);
                System.out.println(dataPrepFunction);
                String isNewPhenoCreated = this.rEngine.eval("dataPrepResult$isNewPhenoCreated").asBool().toString();
                String isNewMapCreated = this.rEngine.eval("dataPrepResult$isNewMapCreated").asBool().toString();
                String isNewGenoCreated = this.rEngine.eval("dataPrepResult$isNewGenoCreated").asBool().toString();
                System.out.println("is new phenotypic file created:" + isNewPhenoCreated);
                System.out.println("is new map file created:" + isNewMapCreated);
                System.out.println("is new genotypic file created:" + isNewGenoCreated);
                if (isNewPhenoCreated == "TRUE") {
                    String newPheno = "dataMeans<- tryCatch(read.csv(\"" + resultFolderPath + "newPhenoData.csv\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\"), error=function(err) \"notRun\")";
                    this.rEngine.eval(newPheno);
                    System.out.println(newPheno);
                }
                String newMapFilePath = null;
                newMapFilePath = isNewMapCreated == "TRUE" ? String.valueOf(resultFolderPath) + "newMapData.txt" : mapFileName;
                String newGenoFilePath = null;
                newGenoFilePath = isNewGenoCreated == "TRUE" ? String.valueOf(resultFolderPath) + "newGenoData.txt" : genotypicDataFileName;
                if (!isInputRawData) {
                    String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
                    this.rEngine.eval(usedData);
                }
                String heading2 = "capture.output(cat(\"\nQTL ANALYSIS\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String heading3 = "capture.output(cat(\"\nMETHOD: " + qtlMethod + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
                String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
                String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(heading2);
                this.rEngine.eval(heading3);
                String bHet = "FALSE";
                bHet = heterozygousPresent ? "TRUE" : "FALSE";
                String bMarker = "FALSE";
                bMarker = estimatePlotMarkerMap ? "TRUE" : "FALSE";
                String sThresh = thresholdLiJi ? "Li&Ji" : thresholdNumericValue;
                double dThreshMQ = allelicDiffThreshold;
                double dChi2Sig = significanceLevelChiSquare;
                double dCutoff = cutOffMissingData;
                int i = 0;
                while (i < respvar.length) {
                    String qtlResultAll1 = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[i] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(sep);
                    this.rEngine.eval(qtlResultAll1);
                    this.rEngine.eval(sep);
                    this.rEngine.eval(outspace);
                    int j = 0;
                    while (j < numEnvts) {
                        specEnv = !selectedEnvironmentLevel.equals("[ALL]") ? selectedEnvironmentLevel : envts[j];
                        if (environment != "NULL") {
                            String qtlResultAll1b = "capture.output(cat(\"\nANALYSIS FOR: " + environment + " = " + specEnv + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(sep);
                            this.rEngine.eval(qtlResultAll1b);
                            this.rEngine.eval(sep);
                            this.rEngine.eval(outspace);
                        }
                        String funcDmMq1 = "dmMq1 <- tryCatch(cross.data<-load.cross.data(dataMeans, \"" + newGenoFilePath + "\", \"" + newMapFilePath + "\", cross = \"" + crossType + "\", heterozygotes = " + bHet + ", genotype = \"" + genotype + "\", env.label = " + environment_In + ", env = \"" + specEnv + "\", ST = NULL), error=function(err) \"notRun\")";
                        String funcDmMq2 = "dmMq2 <- tryCatch(crossobj <- cross.data, error=function(err) \"notRun\")";
                        this.rEngine.eval(funcDmMq1);
                        System.out.println(funcDmMq1);
                        String runSuccessDmMq1 = this.rEngine.eval("dmMq1").asString();
                        if (runSuccessDmMq1 != null && runSuccessDmMq1.equals("notRun")) {
                            System.out.println("error");
                            this.rEngine.eval("capture.output(cat(\"\n***Problem encountered during data reading/compilation.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)");
                        }
                        this.rEngine.eval(funcDmMq2);
                        System.out.println(funcDmMq2);
                        String runSuccessDmMq2 = this.rEngine.eval("dmMq2").asString();
                        if (runSuccessDmMq2 != null && runSuccessDmMq2.equals("notRun")) {
                            System.out.println("error");
                            this.rEngine.eval("capture.output(cat(\"\n***Problem encountered during data reading/compilation.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)");
                        }
                        if (i == 0 && j == 0) {
                            String funcDmMq3 = "dmMq3 <- tryCatch(MQ.marker.diag(crossobj, \"" + resultFolderPath + "\", estmarker = " + bMarker + ", thresholdMQ = " + dThreshMQ + ", p.val = " + dChi2Sig + ", na.cutoff = " + dCutoff + "), error=function(err) \"notRun\")";
                            this.rEngine.eval(funcDmMq3);
                            System.out.println(funcDmMq3);
                            String runSuccessDmMq3 = this.rEngine.eval("dmMq3").asString();
                            if (runSuccessDmMq3 != null && runSuccessDmMq3.equals("notRun")) {
                                System.out.println("error");
                                this.rEngine.eval("capture.output(cat(\"\n***Problem encountered during marker quality check.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)");
                            }
                        }
                        String funcQtlSIM = null;
                        funcQtlSIM = thresholdLiJi ? "qtlSIM <- tryCatch(QTL.result<-QTL.analysis(crossobj, \"" + resultFolderPath + "\", env.label = " + environment_In + ", env = \"" + specEnv + "\", trait = \"" + respVarMean[i] + "\", step = " + step + ", method = \"SIM\", threshold = \"" + sThresh + "\", distance = " + minDistance + ", window.size = " + windowSize + "), error=function(err) \"notRun\")" : "qtlSIM <- tryCatch(QTL.result<-QTL.analysis(crossobj, \"" + resultFolderPath + "\", env.label = " + environment_In + ", env = \"" + specEnv + "\", trait = \"" + respVarMean[i] + "\", step = " + step + ", method = \"SIM\", threshold = " + sThresh + ", distance = " + minDistance + ", window.size = " + windowSize + "), error=function(err) \"notRun\")";
                        System.out.println(funcQtlSIM);
                        this.rEngine.eval(funcQtlSIM);
                        String runSuccessQtl = null;
                        if (qtlMethod == "SIM") {
                            runSuccessQtl = this.rEngine.eval("qtlSIM").asString();
                        }
                        if (qtlMethod == "CIM") {
                            String funcQtlCIM1 = "qtlCIM1 <- tryCatch(cofactors<-as.vector(QTL.result$selected$marker), error=function(err) \"notRun\")";
                            String funcQtlCIM2 = null;
                            funcQtlCIM2 = thresholdLiJi ? "qtlCIM2 <- tryCatch(QTL.result<-QTL.analysis(crossobj, \"" + resultFolderPath + "\", env.label = " + environment_In + ", env = \"" + specEnv + "\", trait = \"" + respVarMean[i] + "\", step = " + step + ", method = \"CIM\", threshold = \"" + sThresh + "\", distance = " + minDistance + ", cofactors, window.size = " + windowSize + "), error=function(err) \"notRun\")" : "qtlCIM2 <- tryCatch(QTL.result<-QTL.analysis(crossobj, \"" + resultFolderPath + "\", env.label = " + environment_In + ", env = \"" + specEnv + "\", trait = \"" + respVarMean[i] + "\", step = " + step + ", method = \"CIM\", threshold = " + sThresh + ", distance = " + minDistance + ", cofactors, window.size = " + windowSize + "), error=function(err) \"notRun\")";
                            System.out.println(funcQtlCIM1);
                            this.rEngine.eval(funcQtlCIM1);
                            String runSuccessQtlCIM1 = this.rEngine.eval("qtlCIM1").asString();
                            if (runSuccessQtlCIM1 != null && runSuccessQtlCIM1.equals("notRun")) {
                                System.out.println("error");
                                this.rEngine.eval("capture.output(cat(\"\n***Cannot proceed with CIM (error on the cofactors).***\n***(Shown output for SIM).***\n\"),file=\"" + outFileName + "\",append = TRUE)");
                            } else {
                                System.out.println(funcQtlCIM2);
                                this.rEngine.eval(funcQtlCIM2);
                                runSuccessQtl = this.rEngine.eval("qtlCIM2").asString();
                            }
                        }
                        if (runSuccessQtl != null && runSuccessQtl.equals("notRun")) {
                            System.out.println("error");
                            this.rEngine.eval("capture.output(cat(\"\n***QTL analysis not done.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
                        } else {
                            String qtlResultAll1a = "capture.output(cat(\"\nQTL RESULT (ALL):\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(qtlResultAll1a);
                            String qtlResultAll2 = "capture.output(QTL.result$all,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(qtlResultAll2);
                            String qtlResultSel1 = "capture.output(cat(\"\nQTL RESULT (SELECTED):\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String qtlResultSel2 = "capture.output(QTL.result$selected,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(qtlResultSel1);
                            this.rEngine.eval(qtlResultSel2);
                            this.rEngine.eval(outspace);
                            this.rEngine.eval(outspace);
                        }
                        ++j;
                    }
                    ++i;
                }
                this.rEngine.eval(sep2);
            }
            this.rEngineEnd();
            System.out.println("reached end.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGenerationMeanRawData(String dataFileName, String outFileName, String[] usersNotation, String[] generalNotation, String alpha) {
        String usersNotationVector = this.inputTransform.createRVector(usersNotation);
        String generalNotationVector = this.inputTransform.createRVector(generalNotation);
        try {
            String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            String sinkIn = "sink(\"" + outFileName + "\")";
            String func = "result <- try(generationMean.test(dataRead, " + usersNotationVector + "," + generalNotationVector + ",\"" + alpha + "\"), silent=TRUE)";
            String checkError = "if (class(result) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in generationMean.test function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            String sinkOut = "sink()";
            this.rEngine.eval(readData);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(func);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
            System.out.println(readData);
            System.out.println(sinkIn);
            System.out.println(func);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGenerationMeanSummaryStats(String dataFileName, String outFileName, String generationMean, String weights, String generationStandardDeviation, String numberObservations, String generation, String[] usersNotation, String[] generalNotation, String alpha) {
        String usersNotationVector = this.inputTransform.createRVector(usersNotation);
        String generalNotationVector = this.inputTransform.createRVector(generalNotation);
        try {
            String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            String sinkIn = "sink(\"" + outFileName + "\")";
            String func = null;
            func = weights != "NULL" ? "result <- try(generationMeanSummaryStats.test(dataRead, \"" + generationMean + "\", \"" + weights + "\", stdDevVar=NULL, numObsVar=NULL,\"" + generation + "\"," + usersNotationVector + "," + generalNotationVector + ",\"" + alpha + "\"), silent=TRUE)" : (numberObservations != "NULL" ? "result <- try(generationMeanSummaryStats.test(dataRead, \"" + generationMean + "\", weightsVar=NULL,\"" + generationStandardDeviation + "\",\"" + numberObservations + "\",\"" + generation + "\"," + usersNotationVector + "," + generalNotationVector + ",\"" + alpha + "\"), silent=TRUE)" : "result <- try(generationMeanSummaryStats.test(dataRead, \"" + generationMean + "\", weightsVar=NULL,\"" + generationStandardDeviation + "\", numObsVar=NULL,\"" + generation + "\"," + usersNotationVector + "," + generalNotationVector + ",\"" + alpha + "\"), silent=TRUE)");
            String checkError = "if (class(result) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trimStrings(strsplit(result, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in generationMeanSummaryStats.test function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            String sinkOut = "sink()";
            this.rEngine.eval(readData);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(func);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
            System.out.println(readData);
            System.out.println(sinkIn);
            System.out.println(func);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doHWETest(String rDataFilename, String outFilename, String display) {
        try {
            String rm = "rm(list=ls(all=TRUE))";
            String callLibrary = "library(adegenet)";
            String load = "load(\"" + rDataFilename + "\")";
            String hweTest1 = "capture.output(cat(\"\nDATA FILE: " + rDataFilename + "\n\n\",file=\"" + outFilename + "\"))";
            String hweTest2 = "capture.output(cat(\"HARDY-WEINBERG EQUILIBRIUM TEST\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String hweTest3 = null;
            String hweTest4 = null;
            if (display.equals("full")) {
                hweTest3 = "capture.output(cat(\"SUMMARY OF TESTS:\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
                hweTest4 = "result<-HWETest(data, display=\"full\")";
            }
            if (display.equals("matrix")) {
                hweTest3 = "capture.output(cat(\"MATRIX OF P-VALUES:\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
                hweTest4 = "result<-HWETest(data, display=\"matrix\")";
            }
            String hweTest5 = "capture.output(result,file=\"" + outFilename + "\",append = TRUE)";
            String detachLibrary = "detach(\"package:adegenet\")";
            this.rEngine.eval(rm);
            this.rEngine.eval(callLibrary);
            this.rEngine.eval(load);
            System.out.println(rm);
            System.out.println(callLibrary);
            System.out.println(load);
            this.rEngine.eval(hweTest1);
            this.rEngine.eval(hweTest2);
            this.rEngine.eval(hweTest3);
            this.rEngine.eval(hweTest4);
            System.out.println(hweTest4);
            this.rEngine.eval(hweTest5);
            this.rEngine.eval(detachLibrary);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dogenDistance(String rDataFilename, String outFilename, String resultFolderPath, int method, Boolean displayDiag, Boolean displayUpper) {
        try {
            String methodUsed = new String();
            switch (method) {
                case 1: {
                    methodUsed = "Nei\u2019s Distance";
                    break;
                }
                case 2: {
                    methodUsed = "Angular Distance or Edwards\u2019 Distance";
                    break;
                }
                case 3: {
                    methodUsed = "Coancestrality Coefficient or Reynolds\u2019 Distance";
                    break;
                }
                case 4: {
                    methodUsed = "Classical Euclidean Distance or Rogers\u2019 Distance";
                    break;
                }
                case 5: {
                    methodUsed = "Absolute Genetics Distance or Provesti\u2019s Distance";
                    break;
                }
                default: {
                    methodUsed = "Nei\u2019s Distance";
                }
            }
            String diagString = displayDiag.toString().toUpperCase();
            String upperString = displayUpper.toString().toUpperCase();
            String rm = "rm(list=ls(all=TRUE))";
            String callLibrary = "library(adegenet)";
            String load = "load(\"" + rDataFilename + "\")";
            String hweTest1 = "capture.output(cat(\"\nDATA FILE: " + rDataFilename + "\n\n\",file=\"" + outFilename + "\"))";
            String hweTest2 = "capture.output(cat(\"GENETIC DISTANCES BETWEEN POPULATIONS\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String hweTest3 = "capture.output(cat(\"METHOD: " + methodUsed + "\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String hweTest4 = "capture.output(cat(\"DISTANCE MATRIX:\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String hweTest5 = "result<-genDistance(data, method=" + method + ", displayDiag=" + diagString + ", displayUpper=" + upperString + ")";
            String hweTest6 = "capture.output(result,file=\"" + outFilename + "\",append = TRUE)";
            String writeToFile = "write.table(as.matrix(result),file = \"" + resultFolderPath + "genDistanceMatrix.csv\",sep=\",\",col.names=NA, row.names=TRUE)";
            String detachLibrary = "detach(\"package:adegenet\")";
            this.rEngine.eval(rm);
            this.rEngine.eval(callLibrary);
            this.rEngine.eval(load);
            System.out.println(rm);
            System.out.println(callLibrary);
            System.out.println(load);
            this.rEngine.eval(hweTest1);
            this.rEngine.eval(hweTest2);
            this.rEngine.eval(hweTest3);
            this.rEngine.eval(hweTest4);
            this.rEngine.eval(hweTest5);
            System.out.println(hweTest5);
            this.rEngine.eval(hweTest6);
            this.rEngine.eval(writeToFile);
            System.out.println(writeToFile);
            this.rEngine.eval(detachLibrary);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dopopStructure(String rDataFilename, String outFilename, Boolean pairwiseFst) {
        try {
            String pairwiseString = pairwiseFst.toString().toUpperCase();
            String rm = "rm(list=ls(all=TRUE))";
            String callLibrary = "library(adegenet)";
            String load = "load(\"" + rDataFilename + "\")";
            String Test1 = "capture.output(cat(\"\nDATA FILE: " + rDataFilename + "\n\n\",file=\"" + outFilename + "\"))";
            String Test2 = "capture.output(cat(\"TESTING POPULATION STRUCTURE\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String Test3 = "result<-popStructure(data, pairwiseFst=" + pairwiseString + ")";
            String Test4 = "capture.output(cat(\"Fst = \", result$fst,\"\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String Test5 = "capture.output(cat(\"Fit = \", result$fit,\"\n\"),file=\"" + outFilename + "\",append = TRUE)";
            String Test6 = "capture.output(cat(\"Fis = \", result$fis,\"\n\"),file=\"" + outFilename + "\",append = TRUE)";
            this.rEngine.eval(rm);
            this.rEngine.eval(callLibrary);
            this.rEngine.eval(load);
            System.out.println(rm);
            System.out.println(callLibrary);
            System.out.println(load);
            this.rEngine.eval(Test1);
            this.rEngine.eval(Test2);
            this.rEngine.eval(Test3);
            System.out.println(Test3);
            this.rEngine.eval(Test4);
            this.rEngine.eval(Test5);
            this.rEngine.eval(Test6);
            if (pairwiseFst.booleanValue()) {
                String Test7 = "capture.output(cat(\"\n\nPAIRWISE Fst:\n\n\"),file=\"" + outFilename + "\",append = TRUE)";
                String Test8 = "capture.output(result$fstMatrix,file=\"" + outFilename + "\",append = TRUE)";
                this.rEngine.eval(Test7);
                this.rEngine.eval(Test8);
            }
            String detachLibrary = "detach(\"package:adegenet\")";
            this.rEngine.eval(detachLibrary);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doLinearReg(String dataFileName, String outputPath, String[] depvar, String[] indepvar, boolean constant, boolean confInt, double confLevel, boolean covMatrix, boolean VIF, boolean autoCorr, String[] heteroskedasticity, String[] normality, boolean COOKS, boolean leverage) {
        String depvarVector = this.inputTransform.createRVector(depvar);
        String indepvarVector = this.inputTransform.createRVector(indepvar);
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outputPath + "LinearRegOutput.txt\")";
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
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
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
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
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
        System.out.println(funcStmt);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
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
    public void doSingleEnvironmentAnalysis2(String dataFileName, String outFileName, String resultFolderPath, int designIndex, String[] respvar, String environment, String[] environmentLevels, String genotype, String block, String rep, String row, String column, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean heatmapResiduals, String heatmapRow, String heatmapColumn, boolean diagnosticPlot, boolean genotypeFixed, String pairwiseAlpha, String[] genotypeLevels, String[] controlLevels, boolean compareControl, boolean specifiedContrast, String contrastFileName, boolean genotypeRandom, boolean excludeControls, boolean genoPhenoCorrelation) {
        long startTime = System.nanoTime();
        String respvarVector = this.inputTransform.createRVector(respvar);
        String controlLevelsVector = this.inputTransform.createRVector(controlLevels);
        boolean runningFixedSuccess = true;
        boolean runningRandomSuccess = true;
        boolean printAllOutputFixed = true;
        boolean printAllOutputRandom = true;
        try {
            String checkError2;
            String checkError;
            String checkError4;
            String checkError42;
            String checkError3;
            String checkError22;
            String printError3;
            String printError2;
            String printError1;
            String lmerError;
            String lmerRun;
            String printError32;
            String printError22;
            String printError12;
            String checkError43;
            String checkError32;
            String checkError23;
            String checkError5;
            String respVarHead;
            String checkError44;
            String checkError33;
            String checkError24;
            String checkError6;
            String groupVars;
            String designUsed = new String();
            String design = new String();
            switch (designIndex) {
                case 0: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                    break;
                }
                case 1: {
                    designUsed = "Augmented RCB";
                    design = "AugRCB";
                    break;
                }
                case 2: {
                    designUsed = "Augmented Latin Square";
                    design = "AugLS";
                    break;
                }
                case 3: {
                    designUsed = "Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 4: {
                    designUsed = "Row-Column";
                    design = "RowCol";
                    break;
                }
                case 5: {
                    designUsed = "Latinized Alpha-Lattice";
                    design = "LatinAlpha";
                    break;
                }
                case 6: {
                    designUsed = "Latinized Row-Column";
                    design = "LatinRowCol";
                    break;
                }
                case 7: {
                    designUsed = "Augmented Alpha-Lattice";
                    design = "AugAlpha";
                    break;
                }
                case 8: {
                    designUsed = "Augmented Row-Column";
                    design = "AugRowCol";
                    break;
                }
                default: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                }
            }
            String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("data").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String outFile = "capture.output(cat(\"\nSINGLE-ENVIRONMENT ANALYSIS\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(outFile);
            this.rEngine.eval(usedDesign);
            if (genotypeFixed) {
                String funcSsaFixed = null;
                groupVars = null;
                if (environment == "NULL") {
                    if (designIndex == 0 || designIndex == 1) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                        funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = FALSE), silent = TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (designIndex == 0 || designIndex == 1) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                } else if (designIndex == 2) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                    funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = FALSE), silent = TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcSsaFixed);
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outspace);
                System.out.println(funcSsaFixed);
                String runSuccess = this.rEngine.eval("class(ssa1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("ssa.test: error");
                    checkError6 = "msg <- trimStrings(strsplit(ssa1, \":\")[[1]])";
                    checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError44 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError24);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError44);
                    runningFixedSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        this.rEngine.eval(outspace);
                        if (descriptiveStat) {
                            String funcDesc = null;
                            funcDesc = environment == "NULL" ? "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", " + environment + "), silent=TRUE)" : "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", \"" + environment + "\"), silent=TRUE)";
                            System.out.println(funcDesc);
                            this.rEngine.eval(funcDesc);
                            String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                            String runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                            if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                System.out.println("desc stat: error");
                                checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError23);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError43);
                            } else {
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                            }
                        }
                        int envLevelsLength = 0;
                        envLevelsLength = environment == "NULL" ? 1 : environmentLevels.length;
                        int m = 0;
                        while (m < envLevelsLength) {
                            double nrowData;
                            printAllOutputFixed = true;
                            int j = m + 1;
                            if (environment != "NULL") {
                                String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,ssa1$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(sep);
                                this.rEngine.eval(envtHead);
                                this.rEngine.eval(sep);
                                this.rEngine.eval(outspace);
                            }
                            if ((nrowData = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble()) < 0.8) {
                                String allNAWarning = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
                                printError12 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError22 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError32 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(printError22);
                                this.rEngine.eval(printError32);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outspace);
                                printAllOutputFixed = false;
                            } else {
                                lmerRun = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
                                if (lmerRun.equals("ERROR")) {
                                    lmerError = this.rEngine.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
                                    printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(printError2);
                                    this.rEngine.eval(printError3);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    printAllOutputFixed = false;
                                }
                            }
                            if (printAllOutputFixed) {
                                String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa1$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
                                String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa1$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa1$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(funcTrialSum);
                                String runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                                if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                    System.out.println("class info: error");
                                    String checkError7 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                    String checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError45 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError7);
                                    this.rEngine.eval(checkError25);
                                    this.rEngine.eval(checkError34);
                                    this.rEngine.eval(checkError45);
                                } else {
                                    this.rEngine.eval(trialSumHead);
                                    this.rEngine.eval(trialObsRead);
                                    this.rEngine.eval(trialObsUsed);
                                    this.rEngine.eval(trialSum);
                                    this.rEngine.eval(outspace);
                                }
                                if (varianceComponents) {
                                    String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outVarComp2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outVarComp);
                                    this.rEngine.eval(outVarComp2);
                                    this.rEngine.eval(outspace);
                                }
                                String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable2 = "library(lmerTest)";
                                String outAnovaTable3 = "model1b <- lmer(formula(ssa1$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa1$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                String outAnovaTable4 = "a.table <- anova(model1b)";
                                String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                String outAnovaTable6 = "a.table<-cbind(round(a.table[,c(\"NumDF\", \"Sum Sq\", \"Mean Sq\", \"F.value\", \"DenDF\")], digits=4),pvalue)";
                                String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                System.out.println(outAnovaTable1);
                                System.out.println(outAnovaTable2);
                                System.out.println(outAnovaTable3);
                                System.out.println(outAnovaTable4);
                                System.out.println(outAnovaTable5);
                                System.out.println(outAnovaTable6);
                                System.out.println(outAnovaTable7);
                                System.out.println(outAnovaTable8);
                                System.out.println(outAnovaTable9);
                                System.out.println(outAnovaTable10);
                                this.rEngine.eval(outAnovaTable1);
                                this.rEngine.eval(outAnovaTable2);
                                this.rEngine.eval(outAnovaTable3);
                                this.rEngine.eval(outAnovaTable4);
                                this.rEngine.eval(outAnovaTable5);
                                this.rEngine.eval(outAnovaTable6);
                                this.rEngine.eval(outAnovaTable7);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable8);
                                this.rEngine.eval(outAnovaTable9);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outAnovaTable10);
                                String outDescStat = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outDescStat2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)";
                                System.out.println(outDescStat);
                                System.out.println(outDescStat2);
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                                String outsedTable = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outsedTable2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                                System.out.println(outsedTable);
                                System.out.println(outsedTable2);
                                this.rEngine.eval(outsedTable);
                                this.rEngine.eval(outsedTable2);
                                this.rEngine.eval(outspace);
                                if (compareControl || specifiedContrast) {
                                    double pairwiseSig = Double.valueOf(pairwiseAlpha);
                                    if (compareControl) {
                                        String compareCtrl = "cntrl <- try(contrastAnalysis(model = ssa1$output[[" + i + "]]$site[[" + j + "]]$model, contrastOpt = \"control\", controlLevel = " + controlLevelsVector + ", alpha = " + pairwiseSig + "), silent = TRUE)";
                                        System.out.println(compareCtrl);
                                        this.rEngine.eval(compareCtrl);
                                        String runSuccessCompareCtrl = this.rEngine.eval("class(cntrl)").asString();
                                        if (runSuccessCompareCtrl != null && runSuccessCompareCtrl.equals("try-error")) {
                                            System.out.println("compare with control: error");
                                            String checkError8 = "msg <- trimStrings(strsplit(cntrl, \":\")[[1]])";
                                            String checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                            String checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                            String checkError46 = "capture.output(cat(\"*** \nERROR in contrastAnalysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(checkError8);
                                            this.rEngine.eval(checkError26);
                                            this.rEngine.eval(checkError35);
                                            this.rEngine.eval(checkError46);
                                        } else {
                                            String outCompareControl = "if (nrow(cntrl) != 0) {\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\nSIGNIFICANT LINEAR CONTRAST AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cntrl,file=\"" + outFileName + "\",append = TRUE)\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "} else {";
                                            outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n NO SIGNIFICANT LINEAR CONTRAST AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                            outCompareControl = String.valueOf(outCompareControl) + "}";
                                            System.out.println(outCompareControl);
                                            this.rEngine.eval(outCompareControl);
                                        }
                                    }
                                    if (specifiedContrast) {
                                        String readContrastData = "contrastData <- read.csv(\"" + contrastFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
                                        System.out.println(readContrastData);
                                        this.rEngine.eval(readContrastData);
                                        String runSuccessContrastData = this.rEngine.eval("contrastData").asString();
                                        if (runSuccessContrastData != null && runSuccessContrastData.equals("notRun")) {
                                            System.out.println("error");
                                            this.rEngine.eval("capture.output(cat(\"\n***Error reading contrast data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
                                        } else {
                                            String userContrast = "userContrast <- try(contrastAnalysis(model = ssa1$output[[" + i + "]]$site[[" + j + "]]$model, contrastOpt = \"user\", contrast = contrastData, alpha = " + pairwiseSig + "), silent = TRUE)";
                                            System.out.println(userContrast);
                                            this.rEngine.eval(userContrast);
                                            String runSuccessUserCtrl = this.rEngine.eval("class(userContrast)").asString();
                                            if (runSuccessUserCtrl != null && runSuccessUserCtrl.equals("try-error")) {
                                                System.out.println("compare with control: error");
                                                String checkUError = "msg <- trimStrings(strsplit(userContrast, \":\")[[1]])";
                                                String checkUError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                                String checkUError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                                String checkUError4 = "capture.output(cat(\"*** \nERROR in contrastAnalysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(checkUError);
                                                this.rEngine.eval(checkUError2);
                                                this.rEngine.eval(checkUError3);
                                                this.rEngine.eval(checkUError4);
                                            } else {
                                                String outCompareControl = "if (nrow(userContrast) != 0) {\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\nSIGNIFICANT LINEAR CONTRAST AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "     capture.output(userContrast,file=\"" + outFileName + "\",append = TRUE)\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "} else {";
                                                outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n NO SIGNIFICANT LINEAR CONTRAST AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                                outCompareControl = String.valueOf(outCompareControl) + "}";
                                                System.out.println(outCompareControl);
                                                this.rEngine.eval(outCompareControl);
                                            }
                                        }
                                    }
                                }
                            }
                            ++m;
                        }
                        ++k;
                    }
                    String checkMeanSSE = this.rEngine.eval("ssa1$meansseWarning").asString();
                    String checkVarRep = this.rEngine.eval("ssa1$varrepWarning").asString();
                    System.out.println("checkMeanSSE: " + checkMeanSSE);
                    System.out.println("checkVarRep: " + checkVarRep);
                    if (checkMeanSSE.equals("empty") | checkVarRep.equals("empty")) {
                        System.out.println("Saving means not done.");
                    } else {
                        String meansFileName = "meansFileName <- paste(\"" + resultFolderPath + "\",\"summaryStats.csv\", sep=\"\")";
                        String funcSaveSesVarRep = null;
                        funcSaveSesVarRep = environment == "NULL" ? "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"EnvLevel\")" : "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"" + environment + "\")";
                        String funcSaveSesVarRepCsv = "saveMeans <- try(write.table(meansVar,file = meansFileName ,sep=\",\",row.names=FALSE), silent=TRUE)";
                        this.rEngine.eval(meansFileName);
                        this.rEngine.eval(funcSaveSesVarRep);
                        this.rEngine.eval(funcSaveSesVarRepCsv);
                        String runSuccessSaveMeansSes = this.rEngine.eval("class(saveMeans)").asString();
                        if (runSuccessSaveMeansSes != null && runSuccessSaveMeansSes.equals("try-error")) {
                            System.out.println("saving means file: error");
                            String checkError9 = "msg <- trimStrings(strsplit(saveMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving means file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError9);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    if (diagnosticPlot) {
                        String diagPlotsFunc = null;
                        diagPlotsFunc = environment == "NULL" ? "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = FALSE, ssa1), silent=TRUE)" : "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = FALSE, ssa1), silent=TRUE)";
                        System.out.println(diagPlotsFunc);
                        this.rEngine.eval(diagPlotsFunc);
                        String runSuccessDiagPlots = this.rEngine.eval("class(diagPlots)").asString();
                        if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                            System.out.println("diagnostic plots(genotype fixed): error");
                            String checkError10 = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
                            String checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError10);
                            this.rEngine.eval(checkError27);
                            this.rEngine.eval(checkError36);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (genotypeRandom) {
                String funcSsaRandom = null;
                groupVars = null;
                if (excludeControls) {
                    if (environment == "NULL") {
                        if (designIndex == 0) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                        } else if (designIndex == 1) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                        } else if (designIndex == 2) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                        } else if (designIndex == 3 || designIndex == 5) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                        } else if (designIndex == 4 || designIndex == 6) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                        } else if (designIndex == 7) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                        } else if (designIndex == 8) {
                            funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                            groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                        }
                    } else if (designIndex == 0) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 1) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 7) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 8) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (environment == "NULL") {
                    if (designIndex == 0 || designIndex == 1) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                    } else if (designIndex == 2) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                    } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                    } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                        funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
                        groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                    }
                } else if (designIndex == 0 || designIndex == 1) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
                } else if (designIndex == 2) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (designIndex == 3 || designIndex == 5 || designIndex == 7) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (designIndex == 4 || designIndex == 6 || designIndex == 8) {
                    funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcSsaRandom);
                this.rEngine.eval(sep2);
                this.rEngine.eval(randomHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outspace);
                System.out.println(funcSsaRandom);
                String runSuccess2 = this.rEngine.eval("class(ssa2)").asString();
                if (runSuccess2 != null && runSuccess2.equals("try-error")) {
                    System.out.println("ssa2: error");
                    checkError6 = "msg <- trimStrings(strsplit(ssa2, \":\")[[1]])";
                    checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError44 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError6);
                    this.rEngine.eval(checkError24);
                    this.rEngine.eval(checkError33);
                    this.rEngine.eval(checkError44);
                    runningRandomSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        this.rEngine.eval(outspace);
                        if (descriptiveStat) {
                            String funcDesc = null;
                            funcDesc = environment == "NULL" ? "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", " + environment + "), silent=TRUE)" : "outDesc <- try(DescriptiveStatistics(data, \"" + respvar[k] + "\", \"" + environment + "\"), silent=TRUE)";
                            this.rEngine.eval(funcDesc);
                            System.out.println(funcDesc);
                            String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                            String runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                            if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                System.out.println("desc stat: error");
                                checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError5);
                                this.rEngine.eval(checkError23);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError43);
                            } else {
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outDescStat);
                                this.rEngine.eval(outDescStat2);
                                this.rEngine.eval(outspace);
                            }
                        }
                        int envLevelsLength2 = 0;
                        envLevelsLength2 = environment == "NULL" ? 1 : environmentLevels.length;
                        int m = 0;
                        while (m < envLevelsLength2) {
                            double responseRate;
                            printAllOutputRandom = true;
                            int j = m + 1;
                            if (environment != "NULL") {
                                String envtHead = "capture.output(cat(\"\nANALYSIS FOR: " + environment + "\", \" = \" ,ssa2$output[[" + i + "]]$site[[" + j + "]]$env,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(sep);
                                this.rEngine.eval(envtHead);
                                this.rEngine.eval(sep);
                                this.rEngine.eval(outspace);
                            }
                            if ((responseRate = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble()) < 0.8) {
                                String allNAWarning2 = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
                                printError12 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError22 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                printError32 = "capture.output(cat(\"" + allNAWarning2 + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(printError22);
                                this.rEngine.eval(printError32);
                                this.rEngine.eval(printError12);
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outspace);
                                printAllOutputRandom = false;
                            } else {
                                lmerRun = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
                                if (lmerRun.equals("ERROR")) {
                                    lmerError = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
                                    printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(printError2);
                                    this.rEngine.eval(printError3);
                                    this.rEngine.eval(printError1);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outspace);
                                    printAllOutputRandom = false;
                                }
                            }
                            if (printAllOutputRandom) {
                                int newCheckListLength;
                                String warningCheckTest = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$checkTestWarning").asString();
                                if (!warningCheckTest.equals("NONE")) {
                                    String warningCheckTest2 = "capture.output(cat(\"\n*** \nWARNING:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    String warningCheckTest3 = "capture.output(cat(\"" + warningCheckTest + "\"), file=\"" + outFileName + "\",append = TRUE)";
                                    String warningCheckTest4 = "capture.output(cat(\"\n*** \\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(warningCheckTest2);
                                    this.rEngine.eval(warningCheckTest3);
                                    this.rEngine.eval(warningCheckTest4);
                                }
                                System.out.println("check test:" + warningCheckTest);
                                String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa2$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
                                String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(funcTrialSum);
                                String runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                                if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                    System.out.println("class info: error");
                                    String checkError11 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                    String checkError28 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError47 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError11);
                                    this.rEngine.eval(checkError28);
                                    this.rEngine.eval(checkError37);
                                    this.rEngine.eval(checkError47);
                                } else {
                                    this.rEngine.eval(trialSumHead);
                                    this.rEngine.eval(trialObsRead);
                                    this.rEngine.eval(trialObsUsed);
                                    this.rEngine.eval(trialSum);
                                    this.rEngine.eval(outspace);
                                }
                                if (varianceComponents) {
                                    String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outVarComp2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outVarComp);
                                    this.rEngine.eval(outVarComp2);
                                    this.rEngine.eval(outspace);
                                }
                                String outTestGen1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen2 = "capture.output(cat(\"\nFormula for Model1: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen3 = "capture.output(cat(\"Formula for Model2: \", ssa2$output[[" + i + "]]$site[[" + j + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outTestGen4 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$models.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outTestGen1);
                                this.rEngine.eval(outTestGen2);
                                this.rEngine.eval(outTestGen3);
                                this.rEngine.eval(outTestGen4);
                                this.rEngine.eval(outspace);
                                String newExcludeCheck = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$newExcludeCheck").asBool().toString();
                                System.out.println("newExcludeCheck: " + newExcludeCheck);
                                if (newExcludeCheck.equals("TRUE")) {
                                    String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF CHECK EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable2 = "library(lmerTest)";
                                    String outAnovaTable3 = "model2b <- lmer(formula(ssa2$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa2$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
                                    String outAnovaTable4 = "a.table <- anova(model2b)";
                                    String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                                    String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                                    String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                                    String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                                    String outAnovaTable10 = "detach(\"package:lmerTest\")";
                                    this.rEngine.eval(outAnovaTable1);
                                    this.rEngine.eval(outAnovaTable2);
                                    this.rEngine.eval(outAnovaTable3);
                                    this.rEngine.eval(outAnovaTable4);
                                    this.rEngine.eval(outAnovaTable5);
                                    this.rEngine.eval(outAnovaTable6);
                                    this.rEngine.eval(outAnovaTable7);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable8);
                                    this.rEngine.eval(outAnovaTable9);
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outAnovaTable10);
                                }
                                String outPredMeans = "capture.output(cat(\"\nPREDICTED MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outPredMeans2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outPredMeans);
                                this.rEngine.eval(outPredMeans2);
                                this.rEngine.eval(outspace);
                                if (excludeControls && (newCheckListLength = this.rEngine.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$newCheckListLength").asInt()) > 0) {
                                    String outLSMeansCheck = "capture.output(cat(\"\nCHECK/CONTROL LSMEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outLSMeansCheck2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$lsmeans.checks,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outLSMeansCheck);
                                    this.rEngine.eval(outLSMeansCheck2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstHerit = "capture.output(cat(\"\nHERITABILITY:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                String outEstHerit2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$heritability,file=\"" + outFileName + "\",append = TRUE)";
                                String outEstHerit3 = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outEstHerit);
                                this.rEngine.eval(outEstHerit2);
                                this.rEngine.eval(outEstHerit3);
                                this.rEngine.eval(outspace);
                            }
                            ++m;
                        }
                        ++k;
                    }
                    if (genoPhenoCorrelation) {
                        this.rEngine.eval(sep2);
                        String funcEstCorr = null;
                        if (excludeControls) {
                            if (environment == "NULL") {
                                if (designIndex == 0) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 1) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 2) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 3) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 4) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 5) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 6) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                                } else if (designIndex == 7) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                } else if (designIndex == 8) {
                                    funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                                }
                            } else if (designIndex == 0) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 1) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 2) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 3) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 4) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 5) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 6) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
                            } else if (designIndex == 7) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            } else if (designIndex == 8) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
                            }
                        } else if (environment == "NULL") {
                            if (designIndex == 0 || designIndex == 1) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + "), silent=TRUE)";
                            } else if (designIndex == 2) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + "), silent=TRUE)";
                            } else if (designIndex == 3 || designIndex == 7) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 4 || designIndex == 8) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 5) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
                            } else if (designIndex == 6) {
                                funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
                            }
                        } else if (designIndex == 0 || designIndex == 1) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 2) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 3 || designIndex == 7) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 4 || designIndex == 8) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 5) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        } else if (designIndex == 6) {
                            funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
                        }
                        System.out.println(funcEstCorr);
                        this.rEngine.eval(funcEstCorr);
                        String runSuccessGPCorr = this.rEngine.eval("class(gpcorr)").asString();
                        if (runSuccessGPCorr != null && runSuccessGPCorr.equals("try-error")) {
                            System.out.println("geno pheno corr: error");
                            checkError = "msg <- trimStrings(strsplit(gpcorr, \":\")[[1]])";
                            String checkError29 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError38 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            String checkError48 = "capture.output(cat(\"*** \nERROR in genoNpheno.corr function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError29);
                            this.rEngine.eval(checkError38);
                            this.rEngine.eval(checkError48);
                        } else {
                            String outEstGenoCorr = "capture.output(cat(\"\nGENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstGenoCorr);
                            int envLevelsLength = 0;
                            envLevelsLength = environment == "NULL" ? 1 : environmentLevels.length;
                            int m = 0;
                            while (m < envLevelsLength) {
                                int j = m + 1;
                                if (environment != "NULL") {
                                    String outEstGenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outEstGenoCorr2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstGenoCorr2b = "capture.output(gpcorr$GenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outEstGenoCorr2b);
                                this.rEngine.eval(outspace);
                                ++m;
                            }
                            String outEstPhenoCorr = "capture.output(cat(\"\nPHENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstPhenoCorr);
                            int m2 = 0;
                            while (m2 < envLevelsLength) {
                                int j = m2 + 1;
                                if (environment != "NULL") {
                                    String outEstPhenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outspace);
                                    this.rEngine.eval(outEstPhenoCorr2);
                                    this.rEngine.eval(outspace);
                                }
                                String outEstPhenoCorr2b = "capture.output(gpcorr$PhenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outspace);
                                this.rEngine.eval(outEstPhenoCorr2b);
                                this.rEngine.eval(outspace);
                                ++m2;
                            }
                        }
                    }
                    String checkPredMean = this.rEngine.eval("ssa2$meansWarning").asString();
                    System.out.println("checkPredMean: " + checkPredMean);
                    if (checkPredMean.equals("empty")) {
                        System.out.println("Saving predicted means not done.");
                    } else {
                        String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"predictedMeans.csv\", sep=\"\")";
                        String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(ssa2$means,file = meansFileName2 ,sep=\",\",row.names=FALSE), silent=TRUE)";
                        this.rEngine.eval(meansFileName2);
                        this.rEngine.eval(funcSavePredMeansCsv);
                        String runSuccessSavePredMeans = this.rEngine.eval("class(saveDataB1)").asString();
                        if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {
                            System.out.println("save pred means: error");
                            String checkError12 = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
                            String checkError210 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError39 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving predicted means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError12);
                            this.rEngine.eval(checkError210);
                            this.rEngine.eval(checkError39);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        String diagPlotsFunc = null;
                        diagPlotsFunc = environment == "NULL" ? "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = TRUE, ssa2), silent=TRUE)" : "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = TRUE, ssa2), silent=TRUE)";
                        System.out.println(diagPlotsFunc);
                        this.rEngine.eval(diagPlotsFunc);
                        String runSuccessDiagPlots = this.rEngine.eval("class(diagPlots)").asString();
                        if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                            System.out.println("diagnostic plots (genotype random): error");
                            String checkError13 = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
                            String checkError211 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            String checkError310 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            String checkError49 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError13);
                            this.rEngine.eval(checkError211);
                            this.rEngine.eval(checkError310);
                            this.rEngine.eval(checkError49);
                        }
                    }
                }
            }
            if (runningFixedSuccess & runningRandomSuccess) {
                String trialObsUsed;
                String warningList;
                String warningListCommand;
                String envLevelsCommand;
                String checkError410;
                String checkError311;
                String runSuccessSaveResid;
                String checkResid1;
                String runSuccessDiagPlots;
                String runSsaResid1;
                String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
                String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
                if (genotypeFixed & !genotypeRandom) {
                    runSsaResid1 = null;
                    runSsaResid1 = environment == "NULL" ? "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)" : "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    this.rEngine.eval(runSsaResid1);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("ssa.resid (genotype fixed): error");
                        checkError6 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        String checkError212 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError411 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError6);
                        this.rEngine.eval(checkError212);
                        this.rEngine.eval(checkError33);
                        this.rEngine.eval(checkError411);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_f$residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            System.out.println(residFileNameFixed);
                            System.out.println(func1SaveResidualsCsv);
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError14 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError213 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError312 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError412 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError14);
                                this.rEngine.eval(checkError213);
                                this.rEngine.eval(checkError312);
                                this.rEngine.eval(checkError412);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat1)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (fixed): error");
                                    String checkError15 = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
                                    String checkError214 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError311 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError410 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError15);
                                    this.rEngine.eval(checkError214);
                                    this.rEngine.eval(checkError311);
                                    this.rEngine.eval(checkError410);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        envLevelsCommand = "length(heat1[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            warningListCommand = "heat1[[" + i + "]]$site[[" + j + "]]";
                                            warningList = this.rEngine.eval(warningListCommand).asString();
                                            if (!warningList.equals("empty") && !warningList.equals("unique")) {
                                                trialObsUsed = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                } else if (!genotypeFixed & genotypeRandom) {
                    String runSsaResid2 = null;
                    runSsaResid2 = environment == "NULL" ? "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)" : "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid2);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("ssa.resid (genotype random): error");
                        checkError6 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError215 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError413 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError6);
                        this.rEngine.eval(checkError215);
                        this.rEngine.eval(checkError33);
                        this.rEngine.eval(checkError413);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_r$residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError16 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError216 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError313 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError414 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError16);
                                this.rEngine.eval(checkError216);
                                this.rEngine.eval(checkError313);
                                this.rEngine.eval(checkError414);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat2)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (random): error");
                                    String checkError17 = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
                                    String checkError217 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError311 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError410 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError17);
                                    this.rEngine.eval(checkError217);
                                    this.rEngine.eval(checkError311);
                                    this.rEngine.eval(checkError410);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        envLevelsCommand = "length(heat2[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            warningListCommand = "heat2[[" + i + "]]$site[[" + j + "]]";
                                            warningList = this.rEngine.eval(warningListCommand).asString();
                                            if (!warningList.equals("empty") && !warningList.equals("unique")) {
                                                trialObsUsed = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                } else if (genotypeFixed & genotypeRandom) {
                    runSsaResid1 = null;
                    String runSsaResid2 = null;
                    if (environment == "NULL") {
                        runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)";
                        runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)";
                    } else {
                        runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                        runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    }
                    System.out.println(runSsaResid1);
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid1);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessResidFixed = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {
                        System.out.println("ssa.resid (genotype fixed): error");
                        String checkError18 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError314 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError415 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError18);
                        this.rEngine.eval(checkError2);
                        this.rEngine.eval(checkError314);
                        this.rEngine.eval(checkError415);
                    } else {
                        String checkResid12 = this.rEngine.eval("resid_f$residWarning").asString();
                        System.out.println("checkResid1: " + checkResid12);
                        if (checkResid12.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            String runSuccessSaveResid2 = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid2 != null && runSuccessSaveResid2.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError19 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                String checkError218 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError315 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError19);
                                this.rEngine.eval(checkError218);
                                this.rEngine.eval(checkError315);
                                this.rEngine.eval(checkError4);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat1)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (fixed): error");
                                    String checkError20 = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
                                    checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError42 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError20);
                                    this.rEngine.eval(checkError22);
                                    this.rEngine.eval(checkError3);
                                    this.rEngine.eval(checkError42);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        String envLevelsCommand2 = "length(heat1[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand2).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            String warningListCommand2 = "heat1[[" + i + "]]$site[[" + j + "]]";
                                            String warningList2 = this.rEngine.eval(warningListCommand2).asString();
                                            if (!warningList2.equals("empty") && !warningList2.equals("unique")) {
                                                String trialObsUsed2 = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed2);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                    String runSuccessResidRandom = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {
                        System.out.println("ssa.resid (genotype random): error");
                        checkError = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError219 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError316 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError416 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError);
                        this.rEngine.eval(checkError219);
                        this.rEngine.eval(checkError316);
                        this.rEngine.eval(checkError416);
                    } else {
                        String checkResid13 = this.rEngine.eval("resid_r$residWarning").asString();
                        System.out.println("checkResid2: " + checkResid13);
                        if (checkResid13.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            String runSuccessSaveResid3 = this.rEngine.eval("class(saveResid2)").asString();
                            if (runSuccessSaveResid3 != null && runSuccessSaveResid3.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError21 = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
                                String checkError220 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError317 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError410 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError21);
                                this.rEngine.eval(checkError220);
                                this.rEngine.eval(checkError317);
                                this.rEngine.eval(checkError410);
                            }
                            if (heatmapResiduals) {
                                String funcHeat = null;
                                funcHeat = environment == "NULL" ? "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)" : "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
                                System.out.println(funcHeat);
                                this.rEngine.eval(funcHeat);
                                String runSuccessHeat = this.rEngine.eval("class(heat2)").asString();
                                if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {
                                    System.out.println("heatmap (random): error");
                                    String checkError25 = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    String checkError318 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    String checkError417 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError25);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError318);
                                    this.rEngine.eval(checkError417);
                                } else {
                                    int k = 0;
                                    while (k < respvar.length) {
                                        int i = k + 1;
                                        String envLevelsCommand3 = "length(heat2[[" + i + "]]$site)";
                                        int envLevels = this.rEngine.eval(envLevelsCommand3).asInt();
                                        int m = 0;
                                        while (m < envLevels) {
                                            int j = m + 1;
                                            String warningListCommand3 = "heat2[[" + i + "]]$site[[" + j + "]]";
                                            String warningList3 = this.rEngine.eval(warningListCommand3).asString();
                                            if (!warningList3.equals("empty") && !warningList3.equals("unique")) {
                                                String trialObsUsed3 = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[[" + j + "]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                                this.rEngine.eval(trialObsUsed3);
                                            }
                                            ++m;
                                        }
                                        ++k;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String withBox = "FALSE";
            if (boxplotRawData) {
                withBox = "TRUE";
            }
            String withHist = "FALSE";
            if (histogramRawData) {
                withHist = "TRUE";
            }
            String ssaOut = "ssa1";
            if (genotypeFixed) {
                ssaOut = "ssa1";
            } else if (genotypeRandom) {
                ssaOut = "ssa2";
            }
            String boxHistFunc = null;
            boxHistFunc = environment == "NULL" ? "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = " + environment + ", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)" : "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = \"" + environment + "\", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)";
            System.out.println(boxHistFunc);
            this.rEngine.eval(boxHistFunc);
            String runSuccessBoxHist = this.rEngine.eval("class(boxHist)").asString();
            if (runSuccessBoxHist != null && runSuccessBoxHist.equals("try-error")) {
                System.out.println("boxplot/histogram: error");
                String checkError26 = "msg <- trimStrings(strsplit(boxHist, \":\")[[1]])";
                checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                String checkError319 = "msg <- gsub(\"\\\"\", \"\", msg)";
                String checkError418 = "capture.output(cat(\"*** \nERROR in graph.sea.boxhist function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(checkError26);
                this.rEngine.eval(checkError2);
                this.rEngine.eval(checkError319);
                this.rEngine.eval(checkError418);
            }
            this.rEngine.eval(outspace);
            this.rEngine.eval(sep2);
            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("#####: Elapsed Time = " + elapsedTime + " ns = " + (double)elapsedTime / 1.0E9 + " sec");
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doMultiEnvironmentOneStage(String dataFileName, String outFileName, String resultFolderPath, int designIndex, String[] respvar, String environment, String[] environmentLevels, String genotype, String block, String rep, String row, String column, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean diagnosticPlot, boolean genotypeFixed, boolean performPairwise, String pairwiseAlpha, String[] genotypeLevels, String[] controlLevels, boolean compareControl, boolean performAllPairwise, boolean genotypeRandom, boolean stabilityFinlay, boolean stabilityShukla, boolean specifiedContrastGeno, String contrastGenoFilename, boolean specifiedContrastEnv, String contrastEnvFilename, boolean ammi, boolean gge) {
        String respvarVector = this.inputTransform.createRVector(respvar);
        String controlLevelsVector = this.inputTransform.createRVector(controlLevels);
        boolean runningFixedSuccess = true;
        boolean runningRandomSuccess = true;
        boolean printAllOutputFixed = true;
        boolean printAllOutputRandom = true;
        try {
            String diagPlotsMea1SFunc;
            String checkError4;
            String checkError3;
            String checkError2;
            String checkError;
            String funcSaveGMeansCsv;
            String checkGenoMean;
            String checkError42;
            String checkError32;
            String checkError22;
            String runSuccessSaveGEMeans;
            String checkGenoEnvMean;
            String outVarComp2;
            String outVarComp;
            String checkError43;
            String checkError33;
            String checkError23;
            String checkError5;
            String runSuccessDescStat;
            String outDescStat2;
            String outDescStat;
            String funcDesc;
            String checkError44;
            String checkError34;
            String checkError24;
            String checkError6;
            String runSuccessTS;
            String trialSum;
            String trialObsUsed;
            String trialObsRead;
            String trialSumHead;
            String funcTrialSum;
            String printError3;
            String printError2;
            String printError1;
            String allNAWarning;
            String respVarHead;
            String checkError45;
            String checkError35;
            String checkError25;
            String checkError7;
            String groupVars;
            String designUsed = new String();
            String design = new String();
            switch (designIndex) {
                case 0: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                    break;
                }
                case 1: {
                    designUsed = "Augmented RCB";
                    design = "AugRCB";
                    break;
                }
                case 2: {
                    designUsed = "Augmented Latin Square";
                    design = "AugLS";
                    break;
                }
                case 3: {
                    designUsed = "Alpha-Lattice";
                    design = "Alpha";
                    break;
                }
                case 4: {
                    designUsed = "Row-Column";
                    design = "RowCol";
                    break;
                }
                case 5: {
                    designUsed = "Latinized Alpha-Lattice";
                    design = "LatinAlpha";
                    break;
                }
                case 6: {
                    designUsed = "Latinized Row-Column";
                    design = "LatinRowCol";
                    break;
                }
                default: {
                    designUsed = "Randomized Complete Block (RCB)";
                    design = "RCB";
                }
            }
            String readData = "dataMeaOneStage <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
            System.out.println(readData);
            this.rEngine.eval(readData);
            String runSuccessData = this.rEngine.eval("dataMeaOneStage").asString();
            if (runSuccessData != null && runSuccessData.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
            } else {
                String setWd = "setwd(\"" + resultFolderPath + "\")";
                System.out.println(setWd);
                this.rEngine.eval(setWd);
            }
            String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileName + "\n\",file=\"" + outFileName + "\"))";
            String outFile = "capture.output(cat(\"\nMULTI-ENVIRONMENT ANALYSIS (ONE-STAGE)\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep = "capture.output(cat(\"------------------------------\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
            String outSpace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
            this.rEngine.eval(usedData);
            this.rEngine.eval(outFile);
            this.rEngine.eval(usedDesign);
            if (genotypeFixed) {
                String funcMeaOneStageFixed = null;
                groupVars = null;
                if (design == "RCB" || design == "AugRCB") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL, rep = NULL,\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\")";
                } else if (design == "AugLS") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\", row = \"" + row + "\", column = \"" + column + "\", rep = NULL,\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (design == "Alpha" || design == "LatinAlpha") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL,\"" + rep + "\",\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (design == "RowCol" || design == "LatinRowCol") {
                    funcMeaOneStageFixed = "meaOne1 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcMeaOneStageFixed);
                this.rEngine.eval(sep2);
                this.rEngine.eval(fixedHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                System.out.println(funcMeaOneStageFixed);
                String runSuccess = this.rEngine.eval("class(meaOne1)").asString();
                if (runSuccess != null && runSuccess.equals("try-error")) {
                    System.out.println("GEOneStage.test: error");
                    checkError7 = "msg <- trimStrings(strsplit(meaOne1, \":\")[[1]])";
                    checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError45 = "capture.output(cat(\"*** \nERROR in GEOneStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError7);
                    this.rEngine.eval(checkError25);
                    this.rEngine.eval(checkError35);
                    this.rEngine.eval(checkError45);
                    runningFixedSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputFixed = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaOne1$output[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaOne1$output[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputFixed = false;
                        }
                        if (printAllOutputFixed) {
                            String outAmmi3;
                            String checkError46;
                            String checkError36;
                            String checkError26;
                            String checkError8;
                            String runSuccessAmmi;
                            String outAmmi2;
                            String outAmmi1;
                            String setWd;
                            String outRemark2;
                            String outRemark;
                            String runSuccessStab;
                            String checkError47;
                            String checkError37;
                            String checkError27;
                            String checkError9;
                            double pairwiseSig;
                            funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",meaOne1$output[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaOne1$output[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaOne1$output[[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError6 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError44 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError6);
                                this.rEngine.eval(checkError24);
                                this.rEngine.eval(checkError34);
                                this.rEngine.eval(checkError44);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            funcDesc = "outDesc <- try(DescriptiveStatistics(dataMeaOneStage, \"" + respvar[k] + "\", grp = NULL), silent=TRUE)";
                            this.rEngine.eval(funcDesc);
                            if (descriptiveStat) {
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError5);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError33);
                                    this.rEngine.eval(checkError43);
                                } else {
                                    this.rEngine.eval(outDescStat);
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outVarComp2 = "capture.output(meaOne1$output[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable2 = "library(lmerTest)";
                            String outAnovaTable3 = "model1b <- lmer(formula(meaOne1$output[[" + i + "]]$formula1), data = meaOne1$output[[" + i + "]]$data, REML = T)";
                            String outAnovaTable4 = "a.table <- anova(model1b)";
                            String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
                            String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
                            String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
                            String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
                            String outAnovaTable10 = "detach(\"package:lmerTest\")";
                            this.rEngine.eval(outAnovaTable1);
                            this.rEngine.eval(outAnovaTable2);
                            this.rEngine.eval(outAnovaTable3);
                            this.rEngine.eval(outAnovaTable4);
                            this.rEngine.eval(outAnovaTable5);
                            this.rEngine.eval(outAnovaTable6);
                            this.rEngine.eval(outAnovaTable7);
                            this.rEngine.eval(outAnovaTable8);
                            this.rEngine.eval(outAnovaTable9);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outAnovaTable10);
                            String outTestEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne1$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne1$output[[" + i + "]]$formula3,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv4 = "capture.output(meaOne1$output[[" + i + "]]$testsig.Env,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestEnv1);
                            this.rEngine.eval(outTestEnv2);
                            this.rEngine.eval(outTestEnv3);
                            this.rEngine.eval(outTestEnv4);
                            this.rEngine.eval(outSpace);
                            String outTestGenoEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPE X ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne1$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne1$output[[" + i + "]]$formula4,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv4 = "capture.output(meaOne1$output[[" + i + "]]$testsig.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGenoEnv1);
                            this.rEngine.eval(outTestGenoEnv2);
                            this.rEngine.eval(outTestGenoEnv3);
                            this.rEngine.eval(outTestGenoEnv4);
                            this.rEngine.eval(outSpace);
                            String outGenoEnv = "capture.output(cat(\"\nGENOTYPE X ENVIRONMENT MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outGenoEnv2 = "capture.output(meaOne1$output[[" + i + "]]$wide.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outGenoEnv);
                            this.rEngine.eval(outGenoEnv2);
                            this.rEngine.eval(outSpace);
                            String outDescStat3 = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat22 = "capture.output(meaOne1$output[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat3);
                            this.rEngine.eval(outDescStat22);
                            this.rEngine.eval(outSpace);
                            String outSedStat1 = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outSedStat2 = "capture.output(meaOne1$output[[" + i + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSedStat1);
                            this.rEngine.eval(outSedStat2);
                            this.rEngine.eval(outSpace);
                            if (performPairwise) {
                                pairwiseSig = Double.valueOf(pairwiseAlpha);
                                if (compareControl) {
                                    String funcPwC = "pwControl <- try(ssa.pairwise(meaOne1$output[[" + i + "]]$model, type = \"Dunnett\", alpha = " + pairwiseSig + ", control.level = " + controlLevelsVector + "), silent=TRUE)";
                                    String outCompareControl = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \nCompared with control(s)\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outCompareControl2 = "capture.output(pwControl$result,file=\"" + outFileName + "\",append = TRUE)";
                                    System.out.println(funcPwC);
                                    this.rEngine.eval(funcPwC);
                                    this.rEngine.eval(outCompareControl);
                                    String runSuccessPwC = this.rEngine.eval("class(pwControl)").asString();
                                    if (runSuccessPwC != null && runSuccessPwC.equals("try-error")) {
                                        System.out.println("compare with control: error");
                                        checkError9 = "msg <- trimStrings(strsplit(pwControl, \":\")[[1]])";
                                        checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError47 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError9);
                                        this.rEngine.eval(checkError27);
                                        this.rEngine.eval(checkError37);
                                        this.rEngine.eval(checkError47);
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                    } else {
                                        this.rEngine.eval(outCompareControl2);
                                        String warningControlTest = this.rEngine.eval("pwControl$controlTestWarning").asString();
                                        if (!warningControlTest.equals("NONE")) {
                                            String warningCheckTest2 = "capture.output(cat(\"----- \nNOTE:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            String warningCheckTest3 = "capture.output(cat(\"" + warningControlTest + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(warningCheckTest2);
                                            this.rEngine.eval(warningCheckTest3);
                                        }
                                        this.rEngine.eval(outSpace);
                                        this.rEngine.eval(outSpace);
                                        System.out.println("pairwise control test:" + warningControlTest);
                                    }
                                } else if (performAllPairwise) {
                                    String outPerformAllPairwise = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outPerformAllPairwise);
                                    if (genotypeLevels.length > 0 & genotypeLevels.length < 16) {
                                        String funcPwAll = "pwAll <- try(ssa.pairwise(meaOne1$output[[" + i + "]]$model, type = \"Tukey\", alpha = " + pairwiseSig + ", control.level = NULL), silent=TRUE)";
                                        String outPerformAllPairwise2n = "capture.output(pwAll$result,file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(funcPwAll);
                                        String runSuccessPwAll = this.rEngine.eval("class(pwAll)").asString();
                                        if (runSuccessPwAll != null && runSuccessPwAll.equals("try-error")) {
                                            System.out.println("all pairwise: error");
                                            checkError9 = "msg <- trimStrings(strsplit(pwAll, \":\")[[1]])";
                                            checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                            checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                            checkError47 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                            this.rEngine.eval(checkError9);
                                            this.rEngine.eval(checkError27);
                                            this.rEngine.eval(checkError37);
                                            this.rEngine.eval(checkError47);
                                        } else {
                                            this.rEngine.eval(outPerformAllPairwise2n);
                                            this.rEngine.eval(outSpace);
                                            this.rEngine.eval(outSpace);
                                        }
                                    } else {
                                        String nLevelsLarge = "capture.output(cat(\"***\nExceeded maximum number of genotypes that can be compared. \n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(nLevelsLarge);
                                    }
                                }
                            }
                            if (specifiedContrastGeno) {
                                pairwiseSig = Double.valueOf(pairwiseAlpha);
                                String readContrastData = "contrastGenoData <- read.csv(\"" + contrastGenoFilename + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
                                System.out.println(readContrastData);
                                this.rEngine.eval(readContrastData);
                                String runSuccessContrastData = this.rEngine.eval("contrastGenoData").asString();
                                if (runSuccessContrastData != null && runSuccessContrastData.equals("notRun")) {
                                    System.out.println("error");
                                    this.rEngine.eval("capture.output(cat(\"\n***Error reading contrast data.***\n\"),file=\"" + outFileName + "\",append = FALSE)");
                                } else {
                                    String userContrast = "userContrast <- try(contrastAnalysis(model = meaOne1$output[[" + i + "]]$model, contrastOpt = \"user\", contrast = contrastGenoData, alpha = " + pairwiseSig + "), silent = TRUE)";
                                    System.out.println(userContrast);
                                    this.rEngine.eval(userContrast);
                                    String runSuccessUserCtrl = this.rEngine.eval("class(userContrast)").asString();
                                    if (runSuccessUserCtrl != null && runSuccessUserCtrl.equals("try-error")) {
                                        System.out.println("compare with control: error");
                                        String checkUError = "msg <- trimStrings(strsplit(userContrast, \":\")[[1]])";
                                        String checkUError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkUError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkUError4 = "capture.output(cat(\"*** \nERROR in contrastAnalysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkUError);
                                        this.rEngine.eval(checkUError2);
                                        this.rEngine.eval(checkUError3);
                                        this.rEngine.eval(checkUError4);
                                    } else {
                                        String outCompareControl = "if (nrow(userContrast) != 0) {\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\nSIGNIFICANT LINEAR CONTRAST FOR GENOTYPIC EFFECT AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "     capture.output(userContrast,file=\"" + outFileName + "\",append = TRUE)\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "} else {";
                                        outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n NO SIGNIFICANT LINEAR CONTRAST FOR GENOTYPIC EFFECT AT ALPHA = " + pairwiseSig + ":\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)\n";
                                        outCompareControl = String.valueOf(outCompareControl) + "}";
                                        this.rEngine.eval(outCompareControl);
                                    }
                                }
                            }
                            String genoEnvMeans = "genoEnvMeans <- meaOne1$output[[" + i + "]]$means.GenoEnvCode";
                            this.rEngine.eval(genoEnvMeans);
                            System.out.println(genoEnvMeans);
                            String ybarName = String.valueOf(respvar[k]) + "_means";
                            if (stabilityFinlay) {
                                if (environmentLevels.length > 4) {
                                    String funcStability1 = "funcStability1 <- try(stability.analysis(genoEnvMeans, \"" + ybarName + "\", \"" + genotype + "\", \"" + environment + "\", method = \"regression\"), silent=TRUE)";
                                    String outTestStability1 = "capture.output(cat(\"\nSTABILITY ANALYSIS USING FINLAY-WILKINSON MODEL:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outTestStability1b = "capture.output(funcStability1[[1]][[1]]$stability,file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(funcStability1);
                                    this.rEngine.eval(outTestStability1);
                                    System.out.println(funcStability1);
                                    runSuccessStab = this.rEngine.eval("class(funcStability1)").asString();
                                    if (runSuccessStab != null && runSuccessStab.equals("try-error")) {
                                        System.out.println("stability reg: error");
                                        checkError9 = "msg <- trimStrings(strsplit(funcStability1, \":\")[[1]])";
                                        checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError47 = "capture.output(cat(\"*** \nERROR in stability.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError9);
                                        this.rEngine.eval(checkError27);
                                        this.rEngine.eval(checkError37);
                                        this.rEngine.eval(checkError47);
                                    } else {
                                        this.rEngine.eval(outTestStability1b);
                                        this.rEngine.eval(outSpace);
                                    }
                                } else {
                                    outRemark = "capture.output(cat(\"\nSTABILITY ANALYSIS USING FINLAY-WILKINSON MODEL:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least five levels.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outRemark);
                                    this.rEngine.eval(outRemark2);
                                }
                            }
                            if (stabilityShukla) {
                                if (environmentLevels.length > 4) {
                                    String funcStability2 = "funcStability2 <- try(stability.analysis(genoEnvMeans, \"" + ybarName + "\", \"" + genotype + "\", \"" + environment + "\", method = \"shukla\"), silent=TRUE)";
                                    String outTestStability2 = "capture.output(cat(\"\nSTABILITY ANALYSIS USING SHUKLA'S MODEL:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    String outTestStability2b = "capture.output(funcStability2[[1]][[1]]$stability,file=\"" + outFileName + "\",append = TRUE)";
                                    System.out.println(funcStability2);
                                    this.rEngine.eval(funcStability2);
                                    this.rEngine.eval(outTestStability2);
                                    runSuccessStab = this.rEngine.eval("class(funcStability2)").asString();
                                    if (runSuccessStab != null && runSuccessStab.equals("try-error")) {
                                        System.out.println("stability shukla: error");
                                        checkError9 = "msg <- trimStrings(strsplit(funcStability2, \":\")[[1]])";
                                        checkError27 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError37 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError47 = "capture.output(cat(\"*** \nERROR in stability.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError9);
                                        this.rEngine.eval(checkError27);
                                        this.rEngine.eval(checkError37);
                                        this.rEngine.eval(checkError47);
                                    } else {
                                        this.rEngine.eval(outTestStability2b);
                                        this.rEngine.eval(outSpace);
                                    }
                                } else {
                                    outRemark = "capture.output(cat(\"\nSTABILITY ANALYSIS USING SHUKLA'S MODEL:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least five levels.***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outRemark);
                                    this.rEngine.eval(outRemark2);
                                }
                            }
                            if (ammi) {
                                if (environmentLevels.length > 2) {
                                    setWd = "setwd(\"" + resultFolderPath + "\")";
                                    String ammiOut = "ammiOut <- try(ammi.analysis(ENV = genoEnvMeans[,match(\"CodedEnv\", names(genoEnvMeans))], GEN = genoEnvMeans[,match(\"CodedGeno\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$harmonicMean, genoEnvMeans[,match(\"" + ybarName + "\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$MSE, number = FALSE, biplotPC12 = TRUE, biplotPC13 = TRUE, biplotPC23 = TRUE, ammi1 = TRUE, adaptMap = TRUE, yVar = \"" + ybarName + "\"), silent=TRUE)";
                                    outAmmi1 = "capture.output(cat(\"\nAMMI ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outAmmi2 = "capture.output(cat(\"Percentage of Total Variation Accounted for by the Principal Components: \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(setWd);
                                    this.rEngine.eval(ammiOut);
                                    this.rEngine.eval(outAmmi1);
                                    System.out.println(setWd);
                                    System.out.println(ammiOut);
                                    runSuccessAmmi = this.rEngine.eval("class(ammiOut)").asString();
                                    if (runSuccessAmmi != null && runSuccessAmmi.equals("try-error")) {
                                        System.out.println("ammi: error");
                                        checkError8 = "msg <- trimStrings(strsplit(ammiOut, \":\")[[1]])";
                                        checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError46 = "capture.output(cat(\"*** \nERROR in ammi.analysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError8);
                                        this.rEngine.eval(checkError26);
                                        this.rEngine.eval(checkError36);
                                        this.rEngine.eval(checkError46);
                                    } else {
                                        outAmmi3 = "capture.output(ammiOut$analysis,file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(outAmmi2);
                                        this.rEngine.eval(outAmmi3);
                                        this.rEngine.eval(outSpace);
                                    }
                                } else {
                                    outRemark = "capture.output(cat(\"\nAMMI ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least three levels.***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outRemark);
                                    this.rEngine.eval(outRemark2);
                                }
                            }
                            if (gge) {
                                if (environmentLevels.length > 2) {
                                    setWd = "setwd(\"" + resultFolderPath + "\")";
                                    String ggeOut = "ggeOut <- try(gge.analysis(genoEnvMeans[,match(\"CodedEnv\", names(genoEnvMeans))], genoEnvMeans[,match(\"CodedGeno\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$harmonicMean, genoEnvMeans[,match(\"" + ybarName + "\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$MSE, number = FALSE, yVar = \"" + ybarName + "\", f=0.5, graphSym = TRUE, graphEnv = FALSE, graphGeno = FALSE), silent=TRUE)";
                                    outAmmi1 = "capture.output(cat(\"\nGGE ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outAmmi2 = "capture.output(cat(\"Percentage of Total Variation Accounted for by the Principal Components: \n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(setWd);
                                    this.rEngine.eval(ggeOut);
                                    this.rEngine.eval(outAmmi1);
                                    System.out.println(setWd);
                                    System.out.println(ggeOut);
                                    runSuccessAmmi = this.rEngine.eval("class(ggeOut)").asString();
                                    if (runSuccessAmmi != null && runSuccessAmmi.equals("try-error")) {
                                        System.out.println("gge1: error");
                                        checkError8 = "msg <- trimStrings(strsplit(ggeOut, \":\")[[1]])";
                                        checkError26 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        checkError36 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        checkError46 = "capture.output(cat(\"*** \nERROR in gge.analysis function (f=0.5):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError8);
                                        this.rEngine.eval(checkError26);
                                        this.rEngine.eval(checkError36);
                                        this.rEngine.eval(checkError46);
                                    } else {
                                        outAmmi3 = "capture.output(ggeOut$analysis,file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(outAmmi2);
                                        this.rEngine.eval(outAmmi3);
                                        this.rEngine.eval(outSpace);
                                    }
                                    String ggeOut2 = "ggeOut2 <- try(gge.analysis(genoEnvMeans[,match(\"CodedEnv\", names(genoEnvMeans))], genoEnvMeans[,match(\"CodedGeno\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$harmonicMean, genoEnvMeans[,match(\"" + ybarName + "\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$MSE, number = FALSE, yVar = \"" + ybarName + "\", f=0, graphSym = FALSE, graphEnv = TRUE, graphGeno = FALSE), silent=TRUE)";
                                    this.rEngine.eval(ggeOut2);
                                    System.out.println(ggeOut2);
                                    String runSuccessAmmi2 = this.rEngine.eval("class(ggeOut2)").asString();
                                    if (runSuccessAmmi2 != null && runSuccessAmmi2.equals("try-error")) {
                                        System.out.println("gge2: error");
                                        String checkError10 = "msg <- trimStrings(strsplit(ggeOut2, \":\")[[1]])";
                                        String checkError28 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError38 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError48 = "capture.output(cat(\"*** \nERROR in gge.analysis function (f=0):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError10);
                                        this.rEngine.eval(checkError28);
                                        this.rEngine.eval(checkError38);
                                        this.rEngine.eval(checkError48);
                                    }
                                    String ggeOut3 = "ggeOut3 <- try(gge.analysis(genoEnvMeans[,match(\"CodedEnv\", names(genoEnvMeans))], genoEnvMeans[,match(\"CodedGeno\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$harmonicMean, genoEnvMeans[,match(\"" + ybarName + "\", names(genoEnvMeans))], meaOne1$output[[" + i + "]]$MSE, number = FALSE, yVar = \"" + ybarName + "\", f=1, graphSym = FALSE, graphEnv = FALSE, graphGeno = TRUE), silent=TRUE)";
                                    this.rEngine.eval(ggeOut3);
                                    System.out.println(ggeOut3);
                                    String runSuccessAmmi3 = this.rEngine.eval("class(ggeOut3)").asString();
                                    if (runSuccessAmmi3 != null && runSuccessAmmi3.equals("try-error")) {
                                        System.out.println("gge2: error");
                                        String checkError11 = "msg <- trimStrings(strsplit(ggeOut2, \":\")[[1]])";
                                        String checkError29 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                        String checkError39 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                        String checkError49 = "capture.output(cat(\"*** \nERROR in gge.analysis function (f=1):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                        this.rEngine.eval(checkError11);
                                        this.rEngine.eval(checkError29);
                                        this.rEngine.eval(checkError39);
                                        this.rEngine.eval(checkError49);
                                    }
                                } else {
                                    outRemark = "capture.output(cat(\"\nGGE ANALYSIS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    outRemark2 = "capture.output(cat(\"***This is not done. The environment factor should have at least three levels.***\n\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(outRemark);
                                    this.rEngine.eval(outRemark2);
                                }
                            }
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    checkGenoEnvMean = this.rEngine.eval("meaOne1$meansGenoEnvWarning").asString();
                    System.out.println("checkGenoEnvMean: " + checkGenoEnvMean);
                    if (checkGenoEnvMean.equals("empty")) {
                        System.out.println("Saving geno x env means not done.");
                    } else {
                        String funcSaveGEMeansCsv = "saveGEMeans <- try(write.table(meaOne1$means.GenoEnv.all,file =\"" + resultFolderPath + "GenoEnvMeans_fixed.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGEMeansCsv);
                        this.rEngine.eval(funcSaveGEMeansCsv);
                        runSuccessSaveGEMeans = this.rEngine.eval("class(saveGEMeans)").asString();
                        if (runSuccessSaveGEMeans != null && runSuccessSaveGEMeans.equals("try-error")) {
                            System.out.println("save GxE means: error");
                            String checkError12 = "msg <- trimStrings(strsplit(saveGEMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving genotype x environment means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError12);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError32);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    checkGenoMean = this.rEngine.eval("meaOne1$meansGenoWarning").asString();
                    System.out.println("checkGenoMean: " + checkGenoMean);
                    if (checkGenoMean.equals("empty")) {
                        System.out.println("Saving geno means not done.");
                    } else {
                        funcSaveGMeansCsv = "saveGMeans <- try(write.table(meaOne1$means.Geno.all,file =\"" + resultFolderPath + "GenoMeans_fixed.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGMeansCsv);
                        this.rEngine.eval(funcSaveGMeansCsv);
                        String runSuccessSaveGMeans = this.rEngine.eval("class(saveGMeans)").asString();
                        if (runSuccessSaveGMeans != null && runSuccessSaveGMeans.equals("try-error")) {
                            System.out.println("save G means: error");
                            checkError = "msg <- trimStrings(strsplit(saveGMeans, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving genotype means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        diagPlotsMea1SFunc = "diagPlotsMea1S <- try(graph.mea1s.diagplots(dataMeaOneStage, " + respvarVector + ", is.random = FALSE, meaOne1), silent=TRUE)";
                        System.out.println(diagPlotsMea1SFunc);
                        this.rEngine.eval(diagPlotsMea1SFunc);
                        String runSuccessDiag = this.rEngine.eval("class(diagPlotsMea1S)").asString();
                        if (runSuccessDiag != null && runSuccessDiag.equals("try-error")) {
                            System.out.println("diagnostic plot: error");
                            checkError = "msg <- trimStrings(strsplit(diagPlotsMea1S, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in creating diagnostic plot (fixed genotype):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (genotypeRandom) {
                String funcMeaOneStageRandom = null;
                groupVars = null;
                if (design == "RCB" || design == "AugRCB") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL, rep = NULL,\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\")";
                } else if (design == "AugLS") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\", row = \"" + row + "\", column = \"" + column + "\", rep = NULL,\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
                } else if (design == "Alpha" || design == "LatinAlpha") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column = NULL,\"" + rep + "\",\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
                } else if (design == "RowCol" || design == "LatinRowCol") {
                    funcMeaOneStageRandom = "meaOne2 <- try(GEOneStage.test(\"" + design + "\",dataMeaOneStage," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
                    groupVars = "c(\"" + environment + "\", \"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
                }
                String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
                this.rEngine.eval(funcMeaOneStageRandom);
                this.rEngine.eval(sep2);
                this.rEngine.eval(randomHead);
                this.rEngine.eval(sep2);
                this.rEngine.eval(outSpace);
                System.out.println(funcMeaOneStageRandom);
                String runSuccess2 = this.rEngine.eval("class(meaOne2)").asString();
                if (runSuccess2 != null && runSuccess2.equals("try-error")) {
                    System.out.println("GEOneStage.test: error");
                    checkError7 = "msg <- trimStrings(strsplit(meaOne2, \":\")[[1]])";
                    checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                    checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                    checkError45 = "capture.output(cat(\"*** \nERROR in GEOneStage.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                    this.rEngine.eval(checkError7);
                    this.rEngine.eval(checkError25);
                    this.rEngine.eval(checkError35);
                    this.rEngine.eval(checkError45);
                    runningRandomSuccess = false;
                } else {
                    int k = 0;
                    while (k < respvar.length) {
                        printAllOutputRandom = true;
                        int i = k + 1;
                        respVarHead = "capture.output(cat(\"RESPONSE VARIABLE: " + respvar[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(sep);
                        this.rEngine.eval(respVarHead);
                        this.rEngine.eval(sep);
                        double responseRate = this.rEngine.eval("meaOne2$output[[" + i + "]]$responseRate").asDouble();
                        if (responseRate < 0.8) {
                            allNAWarning = this.rEngine.eval("meaOne2$output[[" + i + "]]$manyNAWarning").asString();
                            printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(printError2);
                            this.rEngine.eval(printError3);
                            this.rEngine.eval(printError1);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                            printAllOutputRandom = false;
                        }
                        if (printAllOutputRandom) {
                            funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",meaOne2$output[[" + i + "]]$data), silent=TRUE)";
                            trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsRead = "capture.output(cat(\"Number of observations read: \", meaOne2$output[[" + i + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialObsUsed = "capture.output(cat(\"Number of observations used: \", meaOne2$output[[" + i + "]]$obsused[[1]],\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(funcTrialSum);
                            runSuccessTS = this.rEngine.eval("class(funcTrialSum)").asString();
                            if (runSuccessTS != null && runSuccessTS.equals("try-error")) {
                                System.out.println("class info: error");
                                checkError6 = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
                                checkError24 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError34 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError44 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError6);
                                this.rEngine.eval(checkError24);
                                this.rEngine.eval(checkError34);
                                this.rEngine.eval(checkError44);
                            } else {
                                this.rEngine.eval(trialSumHead);
                                this.rEngine.eval(trialObsRead);
                                this.rEngine.eval(trialObsUsed);
                                this.rEngine.eval(trialSum);
                                this.rEngine.eval(outSpace);
                            }
                            funcDesc = "outDesc <- DescriptiveStatistics(dataMeaOneStage, \"" + respvar[k] + "\", grp = NULL)";
                            this.rEngine.eval(funcDesc);
                            if (descriptiveStat) {
                                outDescStat = "capture.output(cat(\"\nDESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";
                                runSuccessDescStat = this.rEngine.eval("class(outDesc)").asString();
                                if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {
                                    System.out.println("desc stat: error");
                                    checkError5 = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
                                    checkError23 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                    checkError33 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                    checkError43 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                    this.rEngine.eval(checkError5);
                                    this.rEngine.eval(checkError23);
                                    this.rEngine.eval(checkError33);
                                    this.rEngine.eval(checkError43);
                                } else {
                                    this.rEngine.eval(outDescStat);
                                    this.rEngine.eval(outDescStat2);
                                    this.rEngine.eval(outSpace);
                                }
                            }
                            if (varianceComponents) {
                                outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                                outVarComp2 = "capture.output(meaOne2$output[[" + i + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(outVarComp);
                                this.rEngine.eval(outVarComp2);
                                this.rEngine.eval(outSpace);
                            }
                            String outTestGeno1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGeno4 = "capture.output(meaOne2$output[[" + i + "]]$testsig.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGeno1);
                            this.rEngine.eval(outTestGeno2);
                            this.rEngine.eval(outTestGeno3);
                            this.rEngine.eval(outTestGeno4);
                            this.rEngine.eval(outSpace);
                            String outTestEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula3,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestEnv4 = "capture.output(meaOne2$output[[" + i + "]]$testsig.Env,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestEnv1);
                            this.rEngine.eval(outTestEnv2);
                            this.rEngine.eval(outTestEnv3);
                            this.rEngine.eval(outTestEnv4);
                            this.rEngine.eval(outSpace);
                            String outTestGenoEnv1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPE X ENVIRONMENT EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv2 = "capture.output(cat(\"\nFormula for Model1: \", meaOne2$output[[" + i + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv3 = "capture.output(cat(\"Formula for Model2: \", meaOne2$output[[" + i + "]]$formula4,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outTestGenoEnv4 = "capture.output(meaOne2$output[[" + i + "]]$testsig.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outTestGenoEnv1);
                            this.rEngine.eval(outTestGenoEnv2);
                            this.rEngine.eval(outTestGenoEnv3);
                            this.rEngine.eval(outTestGenoEnv4);
                            this.rEngine.eval(outSpace);
                            String outGenoEnv = "capture.output(cat(\"\nGENOTYPE X ENVIRONMENT MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outGenoEnv2 = "capture.output(meaOne2$output[[" + i + "]]$wide.GenoEnv,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outGenoEnv);
                            this.rEngine.eval(outGenoEnv2);
                            this.rEngine.eval(outSpace);
                            String outDescStat4 = "capture.output(cat(\"\nPREDICTED GENOTYPE MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outDescStat23 = "capture.output(meaOne2$output[[" + i + "]]$means.Geno,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outDescStat4);
                            this.rEngine.eval(outDescStat23);
                            this.rEngine.eval(outSpace);
                            String outEstHerit = "capture.output(cat(\"\nHERITABILITY:\n\"),file=\"" + outFileName + "\",append = TRUE)";
                            String outEstHerit2 = "capture.output(meaOne2$output[[" + i + "]]$heritability,file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(outEstHerit);
                            this.rEngine.eval(outEstHerit2);
                            this.rEngine.eval(outSpace);
                            this.rEngine.eval(outSpace);
                        }
                        ++k;
                    }
                    checkGenoEnvMean = this.rEngine.eval("meaOne2$meansGenoEnvWarning").asString();
                    System.out.println("checkGenoEnvMean: " + checkGenoEnvMean);
                    if (checkGenoEnvMean.equals("empty")) {
                        System.out.println("Saving geno x env means not done.");
                    } else {
                        String funcSaveGEMeansCsv = "saveGEMeans <- try(write.table(meaOne2$means.GenoEnv.all,file =\"" + resultFolderPath + "GenoEnvMeans_random.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGEMeansCsv);
                        this.rEngine.eval(funcSaveGEMeansCsv);
                        runSuccessSaveGEMeans = this.rEngine.eval("class(saveGEMeans)").asString();
                        if (runSuccessSaveGEMeans != null && runSuccessSaveGEMeans.equals("try-error")) {
                            System.out.println("save GxE means: error");
                            String checkError13 = "msg <- trimStrings(strsplit(saveGEMeans, \":\")[[1]])";
                            checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError42 = "capture.output(cat(\"*** \nERROR in saving genotype x environment means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError13);
                            this.rEngine.eval(checkError22);
                            this.rEngine.eval(checkError32);
                            this.rEngine.eval(checkError42);
                        }
                    }
                    checkGenoMean = this.rEngine.eval("meaOne2$meansGenoWarning").asString();
                    System.out.println("checkGenoMean: " + checkGenoMean);
                    if (checkGenoMean.equals("empty")) {
                        System.out.println("Saving geno means not done.");
                    } else {
                        funcSaveGMeansCsv = "saveGMeans <- try(write.table(meaOne2$means.Geno.all,file =\"" + resultFolderPath + "GenoMeans_random.csv\",sep=\",\",row.names=FALSE), silent=TRUE)";
                        System.out.println(funcSaveGMeansCsv);
                        this.rEngine.eval(funcSaveGMeansCsv);
                        String runSuccessSaveGMeans = this.rEngine.eval("class(saveGMeans)").asString();
                        if (runSuccessSaveGMeans != null && runSuccessSaveGMeans.equals("try-error")) {
                            System.out.println("save G means: error");
                            checkError = "msg <- trimStrings(strsplit(saveGMeans, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in saving genotype means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                    if (diagnosticPlot) {
                        diagPlotsMea1SFunc = "diagPlotsMea1S <- tryCatch(graph.mea1s.diagplots(dataMeaOneStage, " + respvarVector + ", is.random = TRUE, meaOne2), error=function(err) \"notRun\")";
                        System.out.println(diagPlotsMea1SFunc);
                        this.rEngine.eval(diagPlotsMea1SFunc);
                        String runSuccessDiag = this.rEngine.eval("class(diagPlotsMea1S)").asString();
                        if (runSuccessDiag != null && runSuccessDiag.equals("try-error")) {
                            System.out.println("diagnostic plot: error");
                            checkError = "msg <- trimStrings(strsplit(diagPlotsMea1S, \":\")[[1]])";
                            checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                            checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                            checkError4 = "capture.output(cat(\"*** \nERROR in creating diagnostic plot (fixed genotype):\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                            this.rEngine.eval(checkError);
                            this.rEngine.eval(checkError2);
                            this.rEngine.eval(checkError3);
                            this.rEngine.eval(checkError4);
                        }
                    }
                }
            }
            if (runningFixedSuccess & runningRandomSuccess) {
                String runSuccessSaveResid;
                String func1SaveResidualsCsv;
                String checkResid1;
                String runSuccessDiagPlots;
                String runSsaResid1;
                String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
                String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
                if (genotypeFixed & !genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GEOneStage_resid(meaOne1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    this.rEngine.eval(runSsaResid1);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype fixed): error");
                        checkError7 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError410 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError7);
                        this.rEngine.eval(checkError25);
                        this.rEngine.eval(checkError35);
                        this.rEngine.eval(checkError410);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_f$ge1residWarning").asString();
                        System.out.println("checkResid1: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError14 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError14);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            }
                        }
                    }
                } else if (!genotypeFixed & genotypeRandom) {
                    String runSsaResid2 = "resid_r <- try(GEOneStage_resid(meaOne2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid2);
                    runSuccessDiagPlots = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype random): error");
                        checkError7 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        checkError25 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        checkError35 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError411 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError7);
                        this.rEngine.eval(checkError25);
                        this.rEngine.eval(checkError35);
                        this.rEngine.eval(checkError411);
                    } else {
                        checkResid1 = this.rEngine.eval("resid_r$ge1residWarning").asString();
                        System.out.println("checkResid2: " + checkResid1);
                        if (checkResid1.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv);
                            runSuccessSaveResid = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError15 = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError22 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError32 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError42 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError15);
                                this.rEngine.eval(checkError22);
                                this.rEngine.eval(checkError32);
                                this.rEngine.eval(checkError42);
                            }
                        }
                    }
                } else if (genotypeFixed & genotypeRandom) {
                    runSsaResid1 = "resid_f <- try(GEOneStage_resid(meaOne1, " + respvarVector + ", is.genoRandom = FALSE), silent=TRUE)";
                    String runSsaResid2 = "resid_r <- try(GEOneStage_resid(meaOne2, " + respvarVector + ", is.genoRandom = TRUE), silent=TRUE)";
                    System.out.println(runSsaResid1);
                    System.out.println(runSsaResid2);
                    this.rEngine.eval(runSsaResid1);
                    this.rEngine.eval(runSsaResid2);
                    String runSuccessResidFixed = this.rEngine.eval("class(resid_f)").asString();
                    if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype fixed): error");
                        String checkError16 = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
                        String checkError210 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError310 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError412 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError16);
                        this.rEngine.eval(checkError210);
                        this.rEngine.eval(checkError310);
                        this.rEngine.eval(checkError412);
                    } else {
                        String checkResid12 = this.rEngine.eval("resid_f$ge1residWarning").asString();
                        System.out.println("checkResid1: " + checkResid12);
                        if (checkResid12.equals("empty")) {
                            System.out.println("Saving resid (fixed) not done.");
                        } else {
                            String func1SaveResidualsCsv2 = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameFixed);
                            this.rEngine.eval(func1SaveResidualsCsv2);
                            String runSuccessSaveResid2 = this.rEngine.eval("class(saveResid)").asString();
                            if (runSuccessSaveResid2 != null && runSuccessSaveResid2.equals("try-error")) {
                                System.out.println("save residuals: error");
                                checkError = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
                                checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                checkError3 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError);
                                this.rEngine.eval(checkError2);
                                this.rEngine.eval(checkError3);
                                this.rEngine.eval(checkError4);
                            }
                        }
                    }
                    String runSuccessResidRandom = this.rEngine.eval("class(resid_r)").asString();
                    if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {
                        System.out.println("GEOneStage_resid (genotype random): error");
                        String checkError17 = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
                        String checkError211 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                        String checkError311 = "msg <- gsub(\"\\\"\", \"\", msg)";
                        String checkError413 = "capture.output(cat(\"*** \nERROR in GEOneStage_resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                        this.rEngine.eval(checkError17);
                        this.rEngine.eval(checkError211);
                        this.rEngine.eval(checkError311);
                        this.rEngine.eval(checkError413);
                    } else {
                        String checkResid13 = this.rEngine.eval("resid_r$ge1residWarning").asString();
                        System.out.println("checkResid2: " + checkResid13);
                        if (checkResid13.equals("empty")) {
                            System.out.println("Saving resid (random) not done.");
                        } else {
                            String func1SaveResidualsCsv3 = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
                            this.rEngine.eval(residFileNameRandom);
                            this.rEngine.eval(func1SaveResidualsCsv3);
                            String runSuccessSaveResid3 = this.rEngine.eval("class(saveResid2)").asString();
                            if (runSuccessSaveResid3 != null && runSuccessSaveResid3.equals("try-error")) {
                                System.out.println("save residuals: error");
                                String checkError18 = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
                                String checkError212 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
                                String checkError312 = "msg <- gsub(\"\\\"\", \"\", msg)";
                                String checkError414 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
                                this.rEngine.eval(checkError18);
                                this.rEngine.eval(checkError212);
                                this.rEngine.eval(checkError312);
                                this.rEngine.eval(checkError414);
                            }
                        }
                    }
                }
            }
            String withBox = "FALSE";
            if (boxplotRawData) {
                withBox = "TRUE";
            }
            String withHist = "FALSE";
            if (histogramRawData) {
                withHist = "TRUE";
            }
            String meaOut = "meaOne1";
            if (genotypeFixed) {
                meaOut = "meaOne1";
            } else if (genotypeRandom) {
                meaOut = "meaOne2";
            }
            String boxHistMeaFunc = "boxHistMea <- tryCatch(graph.mea1s.boxhist(dataMeaOneStage, " + respvarVector + ", " + meaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), error=function(err) \"notRun\")";
            System.out.println(boxHistMeaFunc);
            this.rEngine.eval(boxHistMeaFunc);
            String runSuccessBoxHistMea = this.rEngine.eval("boxHistMea").asString();
            if (runSuccessBoxHistMea != null && runSuccessBoxHistMea.equals("notRun")) {
                System.out.println("error");
                this.rEngine.eval("capture.output(cat(\"\n***An error has occurred.***\n***Boxplot(s) and histogram(s) not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)");
            }
            this.rEngine.eval(outSpace);
            this.rEngine.eval(sep2);
            this.rEngineEnd();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doOutlierDetection(String dataFileName, String outputPath, String respvar, String trmt, String replicate) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String funcStmt = "result <- try(";
        String command = "OutlierDetection(data = \"dataRead\", var = \"" + respvar + "\"";
        if (trmt != null) {
            command = String.valueOf(command) + ", grp = \"" + trmt + "\"";
        }
        if (replicate != null) {
            command = String.valueOf(command) + ", rep = \"" + replicate + "\"";
        }
        command = String.valueOf(command) + ", path = \"" + outputPath + "\", method = \"method2\")";
        funcStmt = String.valueOf(funcStmt) + command + ", silent = TRUE)";
        String saveData = "if (!is.null(result[[1]]$outlier)) {\n";
        saveData = String.valueOf(saveData) + "write.csv(result[[1]]$outlier, file = \"" + outputPath + "Outlier.csv\", row.names = FALSE)\n";
        saveData = String.valueOf(saveData) + "}";
        System.out.println(readData);
        System.out.println(funcStmt);
        System.out.println(saveData);
        this.rEngine.eval(readData);
        this.rEngine.eval(funcStmt);
        this.rEngine.eval(saveData);
        this.rEngineEnd();
    }

    @Override
    public void doSingleEnvironmentAnalysisPRep(String dataFileName, String resultFolderPath, String[] respvar, String genotype, String row, String column, String environment, boolean genotypeFixed, boolean genotypeRandom, boolean excludeControls, String[] controlLevel, boolean moransTest, String[] spatialStruc, boolean descriptiveStat, boolean varianceComponents, boolean boxplotRawData, boolean histogramRawData, boolean heatmapResiduals, boolean diagnosticPlot) {
        String errorMsg4;
        String errorMsg3;
        String errorMsg2;
        String errorMsg1;
        String runSuccessCommand;
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\", \"\", \" \"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + resultFolderPath + "SSAOutput.txt\")";
        String usedData = "cat(\"\\nDATA FILE: " + dataFileName + "\\n\")";
        String analysisDone = "cat(\"\\nSINGLE-ENVIRONMENT ANALYSIS\\n\")";
        String usedDesign = "cat(\"\\nDESIGN: p-rep Design\\n\\n\")";
        String command1 = "ssaTestPrep(data = dataRead, respvar = " + this.inputTransform.createRVector(respvar) + ", geno = \"" + genotype + "\"";
        command1 = String.valueOf(command1) + ", row = \"" + row + "\", column = \"" + column + "\"";
        command1 = environment != null ? String.valueOf(command1) + ", env = \"" + environment + "\"" : String.valueOf(command1) + ", env = NULL";
        String command2 = controlLevel != null ? ", checkList = " + this.inputTransform.createRVector(controlLevel) : ", checkList = " + String.valueOf(controlLevel).toUpperCase();
        command2 = String.valueOf(command2) + ", moransTest = " + String.valueOf(moransTest).toUpperCase();
        command2 = String.valueOf(command2) + ", spatialStruc = " + this.inputTransform.createRVector(spatialStruc);
        command2 = String.valueOf(command2) + ", descriptive = " + String.valueOf(descriptiveStat).toUpperCase();
        command2 = String.valueOf(command2) + ", varCorr = " + String.valueOf(varianceComponents).toUpperCase();
        command2 = String.valueOf(command2) + ", heatmap = " + String.valueOf(heatmapResiduals).toUpperCase();
        command2 = String.valueOf(command2) + ", diagplot = " + String.valueOf(diagnosticPlot).toUpperCase();
        command2 = String.valueOf(command2) + ", histogram = " + String.valueOf(histogramRawData).toUpperCase();
        command2 = String.valueOf(command2) + ", boxplot = " + String.valueOf(boxplotRawData).toUpperCase();
        command2 = String.valueOf(command2) + ", outputPath = \"" + resultFolderPath + "\")";
        String funcSSAPRepFixed = "resultFixed <- try(" + command1;
        if (genotypeFixed) {
            funcSSAPRepFixed = String.valueOf(funcSSAPRepFixed) + ", is.random = FALSE";
            funcSSAPRepFixed = String.valueOf(funcSSAPRepFixed) + command2 + ", silent = TRUE)";
        }
        String funcSSAPRepRandom = "resultRandom <- try(" + command1;
        if (genotypeRandom) {
            funcSSAPRepRandom = String.valueOf(funcSSAPRepRandom) + ", is.random = " + String.valueOf(genotypeRandom).toUpperCase();
            funcSSAPRepRandom = String.valueOf(funcSSAPRepRandom) + ", excludeCheck = " + String.valueOf(excludeControls).toUpperCase();
            funcSSAPRepRandom = String.valueOf(funcSSAPRepRandom) + command2 + ", silent = TRUE)";
        }
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(usedData);
        System.out.println(analysisDone);
        System.out.println(usedDesign);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(usedData);
        this.rEngine.eval(analysisDone);
        this.rEngine.eval(usedDesign);
        if (genotypeFixed) {
            System.out.println(funcSSAPRepFixed);
            this.rEngine.eval(funcSSAPRepFixed);
            runSuccessCommand = this.rEngine.eval("class(resultFixed)").asString();
            if (runSuccessCommand.equals("try-error")) {
                errorMsg1 = "msg1 <- trimStrings(strsplit(resultFixed, \":\")[[1]])";
                errorMsg2 = "msg1 <- trimStrings(paste(strsplit(msg1, \"\\n\")[[length(msg1)]], collapse = \" \"))";
                errorMsg3 = "msg1 <- gsub(\"\\\"\", \"\", msg1)";
                errorMsg4 = "cat(\"Error in SSATestPrep:\\n\",msg1, sep = \"\")";
                System.out.println(errorMsg1);
                System.out.println(errorMsg2);
                System.out.println(errorMsg3);
                System.out.println(errorMsg4);
                this.rEngine.eval(errorMsg1);
                this.rEngine.eval(errorMsg2);
                this.rEngine.eval(errorMsg3);
                this.rEngine.eval(errorMsg4);
            } else {
                String funcResidFixed = "residFixed <- ssaTestPrepResid(resultFixed)";
                String funcResidFixedWrite = "if (nrow(residFixed) > 0) { \n";
                funcResidFixedWrite = String.valueOf(funcResidFixedWrite) + "  write.csv(residFixed, file = \"" + resultFolderPath + "residuals_fixed.csv\", row.names = FALSE) \n";
                funcResidFixedWrite = String.valueOf(funcResidFixedWrite) + "} \n";
                String funcSummaryFixed = "summaryFixed <- ssaTestPrepSummary(resultFixed)";
                String funcSummaryFixedWrite = "if (nrow(summaryFixed) > 0) { \n";
                funcSummaryFixedWrite = String.valueOf(funcSummaryFixedWrite) + "  write.csv(summaryFixed, file = \"" + resultFolderPath + "summaryStats.csv\", row.names = FALSE) \n";
                funcSummaryFixedWrite = String.valueOf(funcSummaryFixedWrite) + "} \n";
                System.out.println(funcResidFixed);
                this.rEngine.eval(funcResidFixed);
                System.out.println(funcResidFixedWrite);
                this.rEngine.eval(funcResidFixedWrite);
                System.out.println(funcSummaryFixed);
                this.rEngine.eval(funcSummaryFixed);
                System.out.println(funcSummaryFixedWrite);
                this.rEngine.eval(funcSummaryFixedWrite);
            }
        }
        if (genotypeRandom) {
            System.out.println(funcSSAPRepRandom);
            this.rEngine.eval(funcSSAPRepRandom);
            runSuccessCommand = this.rEngine.eval("class(resultRandom)").asString();
            if (runSuccessCommand.equals("try-error")) {
                errorMsg1 = "msg2 <- trimStrings(strsplit(resultRandom, \":\")[[1]])";
                errorMsg2 = "msg2 <- trimStrings(paste(strsplit(msg2, \"\\n\")[[length(msg2)]], collapse = \" \"))";
                errorMsg3 = "msg2 <- gsub(\"\\\"\", \"\", msg2)";
                errorMsg4 = "cat(\"Error in SSATestPrep:\\n\",msg2, sep = \"\")";
                System.out.println(errorMsg1);
                System.out.println(errorMsg2);
                System.out.println(errorMsg3);
                System.out.println(errorMsg4);
                this.rEngine.eval(errorMsg1);
                this.rEngine.eval(errorMsg2);
                this.rEngine.eval(errorMsg3);
                this.rEngine.eval(errorMsg4);
            } else {
                String funcResidRandom = "residRandom <- ssaTestPrepResid(resultRandom)";
                String funcResidRandomWrite = "if (nrow(residRandom) > 0) { \n";
                funcResidRandomWrite = String.valueOf(funcResidRandomWrite) + "  write.csv(residRandom, file = \"" + resultFolderPath + "residuals_random.csv\", row.names = FALSE) \n";
                funcResidRandomWrite = String.valueOf(funcResidRandomWrite) + "} \n";
                String funcSummaryRandom = "summaryRandom <- ssaTestPrepSummary(resultRandom)";
                String funcSummaryRandomWrite = "if (nrow(summaryRandom) > 0) { \n";
                funcSummaryRandomWrite = String.valueOf(funcSummaryRandomWrite) + "  write.csv(summaryRandom, file = \"" + resultFolderPath + "predictedMeans.csv\", row.names = FALSE) \n";
                funcSummaryRandomWrite = String.valueOf(funcSummaryRandomWrite) + "} \n";
                System.out.println(funcResidRandom);
                this.rEngine.eval(funcResidRandom);
                System.out.println(funcResidRandomWrite);
                this.rEngine.eval(funcResidRandomWrite);
                System.out.println(funcSummaryRandom);
                this.rEngine.eval(funcSummaryRandom);
                System.out.println(funcSummaryRandomWrite);
                this.rEngine.eval(funcSummaryRandomWrite);
            }
        }
        String sinkOut = "sink()";
        System.out.println(sinkOut);
        this.rEngine.eval(sinkOut);
        this.rEngineEnd();
    }
}

