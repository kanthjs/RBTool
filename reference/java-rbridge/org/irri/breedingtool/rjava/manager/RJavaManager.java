/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import org.irri.breedingtool.rjava.manager.PBToolAnalysisManager;
import org.irri.breedingtool.rjava.manager.PBToolsRandomizationManager;
import org.irri.breedingtool.rjava.manager.RJavaDataManipulationManager;
import org.irri.breedingtool.rjava.manager.RJavaGraphManager;
import org.irri.breedingtool.rjava.manager.STARAnalysisManager;
import org.irri.breedingtool.rjava.manager.STARDesignManager;
import org.rosuda.JRI.Rengine;

public class RJavaManager {
    private Rengine rEngine;
    private RJavaDataManipulationManager RJavaDataManipulationManager;
    private RJavaGraphManager RJavaGraphManager;
    private PBToolAnalysisManager pbToolAnalysisManager;
    private PBToolsRandomizationManager pbToolRandomizationManager;
    private STARAnalysisManager starAnalysisManager;
    private STARDesignManager starDesignManager;

    public RJavaDataManipulationManager getRJavaDataManipulationManager() {
        return this.RJavaDataManipulationManager;
    }

    public RJavaGraphManager getRJavaGraphManager() {
        return this.RJavaGraphManager;
    }

    public Rengine getrEngine() {
        return this.rEngine;
    }

    public PBToolAnalysisManager getPbToolAnalysisManager() {
        return this.pbToolAnalysisManager;
    }

    public PBToolsRandomizationManager getPbToolRandomizationManager() {
        return this.pbToolRandomizationManager;
    }

    public STARAnalysisManager getSTARAnalysisManager() {
        return this.starAnalysisManager;
    }

    public STARDesignManager getSTARDesignManager() {
        return this.starDesignManager;
    }

    public void initPBtool() {
        this.rEngine = new Rengine(new String[]{" "}, false, null);
        this.rEngine.eval("library(PBTools)");
        this.RJavaDataManipulationManager = new RJavaDataManipulationManager(this.rEngine);
        this.RJavaGraphManager = new RJavaGraphManager(this.rEngine);
        this.pbToolAnalysisManager = new PBToolAnalysisManager(this.rEngine);
        this.pbToolRandomizationManager = new PBToolsRandomizationManager(this.rEngine);
    }

    public void initStar() {
        this.rEngine = new Rengine(new String[]{" "}, false, null);
        this.rEngine.eval("library(STAR)");
        this.RJavaDataManipulationManager = new RJavaDataManipulationManager(this.rEngine);
        this.RJavaGraphManager = new RJavaGraphManager(this.rEngine);
        this.starAnalysisManager = new STARAnalysisManager(this.rEngine);
        this.starDesignManager = new STARDesignManager(this.rEngine);
    }
}

