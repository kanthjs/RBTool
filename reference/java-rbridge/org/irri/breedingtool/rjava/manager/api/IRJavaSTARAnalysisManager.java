/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.manager.api;

public interface IRJavaSTARAnalysisManager {
    public void doDescriptiveStatistics(String var1, String var2, String[] var3, String[] var4, String[] var5);

    public void doCrossTab(String var1, String var2, String[] var3, String[] var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9);

    public void doNormality(String var1, String var2, String[] var3, String[] var4, String[] var5, String var6, String var7, String var8, String var9);

    public void doHeteroskedasticity(String var1, String var2, String[] var3, String[] var4, String[] var5, String var6, String var7, String var8, String var9);

    public void doOneMean(String var1, String var2, String[] var3, Integer var4, String var5, boolean var6, boolean var7, float var8, String[] var9);

    public void doPairedMean(String var1, String var2, String[] var3, String[] var4, String var5, boolean var6, boolean var7, float var8, String[] var9);

    public void doIndependentMean(String var1, String var2, String[] var3, String var4, String var5, double var6, boolean var8, boolean var9, float var10, String[] var11);

    public void doProportionTest(String var1, String var2, String var3, String[] var4, String[] var5, String var6, double[] var7, String var8, boolean var9, int var10);

    public void doChiSqGoodnessOfFit(String var1, String var2, String var3, String var4, String var5, boolean var6, String[] var7, int[] var8);

    public void doChiSqTestOfIndependence(String var1, String var2, String var3, String[] var4, String[] var5, String var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15);

    public String[] doANOVA(String var1, String var2, String var3, String[] var4, String[] var5, String[] var6, String[] var7, String[] var8, String var9, String var10, String var11, boolean var12, boolean var13, boolean var14, String[] var15, String[] var16, boolean var17, double var18);

    public String[] doANOVACombined(String var1, String var2, String var3, String[] var4, String[] var5, String[] var6, String[] var7, String[] var8, String var9, String var10, String var11, boolean var12, boolean var13, boolean var14, String[] var15, String[] var16, boolean var17, double var18);

    public void doPairwiseANOVA(String var1, String[] var2, String[] var3, String var4, double var5, boolean var7);

    public void doContrast(String var1, String[] var2, String[] var3, String[] var4, String[] var5, String[] var6, boolean var7);

    public String[] doBIBD(String var1, String var2, String[] var3, String var4, String var5, String var6, String[] var7, boolean var8, boolean var9, boolean var10, double var11);

    public void doPairwiseBIBD(String var1, String[] var2, String[] var3);

    public void doBivariateCorr(String var1, String var2, String[] var3, String[] var4, String var5, String var6, String var7);

    public void doLinearReg(String var1, String var2, String[] var3, String[] var4, boolean var5, boolean var6, double var7, boolean var9, boolean var10, boolean var11, String[] var12, String[] var13, boolean var14, boolean var15);

    public void doLinearRegSelection(String var1, String var2, String[] var3, String[] var4, boolean var5, boolean var6, String var7, String var8, boolean var9, double var10, boolean var12, String[] var13, String[] var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19);

    public void doNonlinearRegression(String var1, String var2, String[] var3, String var4, boolean var5, String var6, String var7, String var8, boolean var9, boolean var10, boolean var11, double var12);

    public void doOneMedian(String var1, String var2, String[] var3, Integer var4, String var5, boolean var6, float var7);

    public void doPairedMedian(String var1, String var2, String[] var3, String[] var4, String var5, boolean var6, float var7);

    public void doIndependentMedian(String var1, String var2, String[] var3, String var4, String var5, boolean var6, float var7);

    public void doKruskal(String var1, String var2, String[] var3, String[] var4);

    public void doFriedman(String var1, String var2, String[] var3, String var4, String var5);

    public void doUnbalancedAnalysis(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11, boolean var12, String var13, double var14, String[] var16, boolean var17, boolean var18, boolean var19, String var20, String var21, boolean var22);

    public void doPCA(String var1, String var2, String[] var3, String var4, boolean var5, boolean var6, boolean var7, String var8, String var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, int[] var16, double[] var17, String[] var18, boolean var19, String var20, String var21, int var22, int var23);

    public void doClusterAgglo(String var1, String var2, String[] var3, String var4, String[] var5, String[] var6, String[] var7, String[] var8, boolean var9, String var10, String var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, Integer var19, boolean var20, boolean var21, boolean var22);

    public void doClusterKmeans(String var1, String var2, String[] var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8, Integer var9, boolean var10, boolean var11);

    public void doClusterDivisive(String var1, String var2, String[] var3, String var4, String[] var5, String[] var6, String[] var7, String[] var8, boolean var9, String var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, Integer var18, boolean var19, boolean var20, boolean var21);

    public void doFitDistribution(String var1, String var2, String var3, String var4, String[] var5, String var6);

    public void doManova(String var1, String var2, String[] var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13);

    public void doMDS(String var1, String var2, String var3, String[] var4, String var5, String var6, String var7, String var8, int var9, boolean var10, int[] var11, double[] var12, String[] var13, boolean var14, String var15, String var16, int var17, boolean var18, boolean var19);
}

