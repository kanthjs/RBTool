/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.analysis.pbtool;

import org.irri.breedingtool.rjava.manager.RScriptLibraryInit;
import org.rosuda.JRI.Rengine;

public class NCI {
    private Rengine rEngine;

    public NCI(Rengine rEngine, RScriptLibraryInit rScriptLibraryInit) {
        this.rEngine = rEngine;
        rScriptLibraryInit.initLibrary();
    }

    public void doNci(String dataFileName, String outFileName, String design, String respvar, String female, String male, String rep, String block, String inbred, String individual, String environment) {
        String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\"), blank.lines.skip=TRUE, sep = \",\")";
        String sinkIn = "sink(\"" + outFileName + "\")";
        String funcNci = design == "CRD" ? "result<-nc1Test(\"CRD\", \"dataRead\", " + respvar + ", \"" + female + "\", \"" + male + "\", rep = \"" + rep + "\", block = NULL, inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\")" : "result<-nc1Test(\"RCB\", \"dataRead\", " + respvar + ", \"" + female + "\", \"" + male + "\", rep = NULL, " + "block = \"" + block + "\", inbred = " + inbred + ", individual = NULL, environment = \"" + environment + "\")";
        String sinkOut = "sink()";
        System.out.println(readData);
        System.out.println(sinkIn);
        System.out.println(funcNci);
        System.out.println(sinkOut);
        this.rEngine.eval(readData);
        this.rEngine.eval(sinkIn);
        this.rEngine.eval(funcNci);
        this.rEngine.eval(sinkOut);
    }
}

