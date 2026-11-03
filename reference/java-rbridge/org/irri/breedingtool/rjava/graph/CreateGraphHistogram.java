/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.graph;

import org.irri.breedingtool.rjava.manager.RScriptLibraryInit;
import org.rosuda.JRI.Rengine;

public class CreateGraphHistogram {
    private Rengine rEngine;

    public CreateGraphHistogram(Rengine rEngine, RScriptLibraryInit rScriptLibraryInit) {
        this.rEngine = rEngine;
        rScriptLibraryInit.initLibrary();
    }

    public void createHistogram(String dataFileName, String respvar) {
        String bslash = "\\";
        String fslash = "/";
        String path = String.valueOf(System.getProperty("user.dir")) + System.getProperty("file.separator") + "sample_datasets";
        String plot_path = path.replace(bslash, fslash);
        String graphHist = "g <- tryCatch(CreateGraphHistogram(\"" + plot_path + "\", dataName = \"" + dataFileName + "\", respvar = \"" + respvar + "\"), error = function(err) \"notRun\")";
        this.rEngine.eval(graphHist);
        System.out.println(plot_path);
        System.out.println("graphHist: " + graphHist);
        String runSuccessG = this.rEngine.eval("g").asString();
        System.out.println("runSuccessG: " + runSuccessG);
    }
}

