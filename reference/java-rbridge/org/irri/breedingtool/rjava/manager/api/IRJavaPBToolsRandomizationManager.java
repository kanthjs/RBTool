/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.manager.api;

public interface IRJavaPBToolsRandomizationManager {
    public void doDesignCRD(String var1, String var2, String[] var3, Integer[] var4, Integer var5, Integer var6);

    public void doDesignRCBD(String var1, String var2, String[] var3, Integer[] var4, Integer var5, Integer var6);

    public void doDesignLSD(String var1, String var2, String[] var3, Integer[] var4, Integer var5);

    public void doDesignSplit(String var1, String var2, String var3, String var4, String var5, String var6, Integer[] var7, Integer var8, Integer var9, String var10);

    public void doDesignStrip(String var1, String var2, String var3, String var4, String var5, String var6, Integer[] var7, Integer var8, Integer var9);

    public void doDesignBIBD(String var1, String var2, Integer var3, Integer var4, Integer var5);

    public void doDesignAug(String var1, String var2, Integer var3, Integer var4, Integer var5, Integer var6, String var7);

    public void doDesignLattice(String var1, String var2, Integer var3, Integer var4, Integer var5);

    public void doDesignAlpha(String var1, String var2, Integer var3, Integer var4, Integer var5, Integer var6);
}

