/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.utilities;

public class RFunctionName {
    private static String BSLASH = "\\";
    private static String FSLASH = "/";
    private static String PATH1 = String.valueOf(System.getProperty("user.dir")) + System.getProperty("file.separator") + "rscript" + System.getProperty("file.separator");
    public static String RFUNCTIONS_PATH = PATH1.replace(BSLASH, FSLASH);
    public static String GETVARINFO = String.valueOf(RFUNCTIONS_PATH) + "getVarInfo.R";
    public static String CREATEHIST = String.valueOf(RFUNCTIONS_PATH) + "CreateGraphHistogram.R";
    public static String TRIM_STRINGS = String.valueOf(RFUNCTIONS_PATH) + "trim_strings.r";
    public static String CLASS_INFORMATION2 = String.valueOf(RFUNCTIONS_PATH) + "class_information2.r";
    public static String ESTIMATEMISSINGDATA = String.valueOf(RFUNCTIONS_PATH) + "estimateMissingData.R";
    public static String FACGEN = String.valueOf(RFUNCTIONS_PATH) + "facGen.R";
    public static String GENERATEBALANCEDATA = String.valueOf(RFUNCTIONS_PATH) + "GenerateBalanceData.R";
    public static String NC1TEST = String.valueOf(RFUNCTIONS_PATH) + "nc1Test.R";
}

