/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.manager.api;

public interface IRJavaPBToolsAnalysisManager {
    public void doNC1Test(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doNC1METest(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doNC2Test(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doNC2METest(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doNC3Test(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doNC3METest(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12);

    public void doTTCTest(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16);

    public void doTTCMETest(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16);

    public void doDiallel1Test(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel1METest(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel2Test(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel2METest(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel3Test(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel3METest(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel4Test(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doDiallel4METest(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15);

    public void doSingleEnvironmentAnalysis(String var1, String var2, String var3, int var4, String[] var5, String var6, String[] var7, String var8, String var9, String var10, String var11, String var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, String var18, String var19, boolean var20, boolean var21, boolean var22, String var23, String[] var24, String[] var25, boolean var26, boolean var27, boolean var28, boolean var29, boolean var30);

    public void doMultiEnvironmentOneStage(String var1, String var2, String var3, int var4, String[] var5, String var6, String[] var7, String var8, String var9, String var10, String var11, String var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19, String var20, String[] var21, String[] var22, boolean var23, boolean var24, boolean var25);

    public void doMultiEnvironmentSecondStage(String var1, String var2, String var3, String var4, String[] var5, String[] var6, String[] var7, String[] var8, String var9, String[] var10, String var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, String var19, String[] var20, String[] var21, boolean var22, boolean var23, boolean var24);

    public void doMultiEnvironmentSecondStageVersion2(String var1, String var2, String var3, String[] var4, String var5, String[] var6, String var7, String[] var8, String[] var9, boolean var10, boolean var11, boolean var12, boolean var13);

    public void doStabilityModels(String var1, String var2, String[] var3, String var4, String var5, boolean var6, boolean var7);

    public void doMultiplicativeModels(String var1, String var2, String var3, String[] var4, String var5, String var6, String[] var7, String[] var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void doMultiplicativeModelsVersion2(String var1, String var2, String var3, String[] var4, String var5, String var6, String[] var7, String[] var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void doPlsRegression(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9);

    public void doFactorialRegression(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9);

    public void doSelectionIndex(String var1, String var2, String var3, int var4, int var5, boolean var6, String var7, String var8, String var9, int var10);

    public void doQtl(String var1, boolean var2, String var3, String var4, String var5, String var6, int var7, String[] var8, String var9, String var10, String var11, String var12, String var13, String var14, String[] var15, String var16, boolean var17, String var18, String var19, String var20, String var21, String var22, boolean var23, String var24, boolean var25, double var26, double var28, double var30);

    public void doGenerationMeanRawData(String var1, String var2, String[] var3, String[] var4, String var5);

    public void doGenerationMeanSummaryStats(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String[] var8, String[] var9, String var10);

    public void doHWETest(String var1, String var2, String var3);

    public void dogenDistance(String var1, String var2, String var3, int var4, Boolean var5, Boolean var6);

    public void dopopStructure(String var1, String var2, Boolean var3);

    public void doLinearReg(String var1, String var2, String[] var3, String[] var4, boolean var5, boolean var6, double var7, boolean var9, boolean var10, boolean var11, String[] var12, String[] var13, boolean var14, boolean var15);

    public void doLinearRegSelection(String var1, String var2, String[] var3, String[] var4, boolean var5, boolean var6, String var7, String var8, boolean var9, double var10, boolean var12, String[] var13, String[] var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void doNonlinearRegression(String var1, String var2, String[] var3, String var4, boolean var5, String var6, String var7, String var8, boolean var9, boolean var10, boolean var11, double var12);

    public void doSingleEnvironmentAnalysis2(String var1, String var2, String var3, int var4, String[] var5, String var6, String[] var7, String var8, String var9, String var10, String var11, String var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, String var18, String var19, boolean var20, boolean var21, String var22, String[] var23, String[] var24, boolean var25, boolean var26, String var27, boolean var28, boolean var29, boolean var30);

    public void doMultiEnvironmentOneStage(String var1, String var2, String var3, int var4, String[] var5, String var6, String[] var7, String var8, String var9, String var10, String var11, String var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19, String var20, String[] var21, String[] var22, boolean var23, boolean var24, boolean var25, boolean var26, boolean var27, boolean var28, String var29, boolean var30, String var31, boolean var32, boolean var33);

    public void doOutlierDetection(String var1, String var2, String var3, String var4, String var5);

    public void doSingleEnvironmentAnalysisPRep(String var1, String var2, String[] var3, String var4, String var5, String var6, String var7, boolean var8, boolean var9, boolean var10, String[] var11, boolean var12, String[] var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);
}

