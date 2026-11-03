/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import org.irri.breedingtool.rjava.utilities.RFunctionName;
import org.rosuda.JRI.Rengine;

public class RScriptLibraryInit {
    private Rengine rEngine;

    public RScriptLibraryInit(Rengine rEngine) {
        this.rEngine = rEngine;
    }

    public void initLibrary() {
        this.rEngine.eval("library(testPackage)");
        String funcGraphHist = "source(\"" + RFunctionName.CREATEHIST + "\")";
        String funcGetVarInfo = "source(\"" + RFunctionName.GETVARINFO + "\")";
        String source1 = "source(\"" + RFunctionName.CLASS_INFORMATION2 + "\")";
        String source2 = "source(\"" + RFunctionName.TRIM_STRINGS + "\")";
        String source3 = "source(\"" + RFunctionName.ESTIMATEMISSINGDATA + "\")";
        String source4 = "source(\"" + RFunctionName.FACGEN + "\")";
        String source5 = "source(\"" + RFunctionName.GENERATEBALANCEDATA + "\")";
        String source6 = "source(\"" + RFunctionName.NC1TEST + "\")";
        System.out.println(funcGraphHist);
        System.out.println(funcGetVarInfo);
        System.out.println(source1);
        System.out.println(source2);
        System.out.println(source3);
        System.out.println(source4);
        System.out.println(source5);
        System.out.println(source6);
        this.rEngine.eval(funcGraphHist);
        this.rEngine.eval(funcGetVarInfo);
    }
}

