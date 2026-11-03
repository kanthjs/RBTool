/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.manager.api;

import java.util.ArrayList;

public interface IRJavaDataManipulationManager {
    public void sortCases(String var1, String var2, String[] var3, String[] var4);

    public void subSet(String var1, String var2, ArrayList<String> var3);

    public ArrayList<String> getVariableInfo(String var1, String var2, int var3, String var4);

    public String[] getDataInPackage(String var1);

    public void loadDataInPackage(String var1, String var2, String var3);

    public void dataTransformation(String var1, String var2, String var3, String var4, String var5);

    public void combineFactorLevels(String var1, String var2, String[] var3, String var4);

    public void aggregateData(String var1, String var2, String[] var3, String[] var4, String[] var5, boolean var6);

    public void toWide(String var1, String var2, String[] var3, String[] var4, String[] var5);

    public void toLong(String var1, String var2, ArrayList<String[]> var3, String[] var4, String[] var5, String[] var6, String[] var7);

    public void mergeData(String var1, String var2, String var3, String[] var4, String[] var5, String[] var6, String[] var7, int var8);

    public void appendData(String var1, String var2, String var3, String var4, String var5, String[] var6, String[] var7, String[] var8, String[] var9);

    public void convertRdaToCsv(String var1, String var2);

    public String isColumnNumeric(String var1, String var2);

    public String[] getColumnHeaders(String var1);

    public void importGeneticFile(String var1, String var2, String var3, String var4, String var5, String var6, String var7);

    public String isObjectGenind(String var1);

    public String[] getGenindPopNames(String var1);

    public String[] getGenindIndNames(String var1);

    public String[] getGenindLocNames(String var1);

    public String[] getGenindAlleleNames(String var1);

    public String[] getGenindNumberAllelesPerMarker(String var1);

    public int getGenindPloidy(String var1);

    public String getGenindType(String var1);

    public void getGenindTab(String var1, String var2);
}

