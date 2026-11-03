/*
 * Decompiled with CFR 0.152.
 */
package org.irri.breedingtool.rjava.utilities;

import java.util.ArrayList;

public class InputTransform {
    public String createRVector(String[] stringArrayInput) {
        String stringVectorR = "c(";
        int i = 0;
        while (i < stringArrayInput.length) {
            stringVectorR = i > 0 ? String.valueOf(stringVectorR) + "," + "\"" + stringArrayInput[i] + "\"" : String.valueOf(stringVectorR) + "\"" + stringArrayInput[i] + "\"";
            ++i;
        }
        stringVectorR = String.valueOf(stringVectorR) + ")";
        return stringVectorR;
    }

    public String createRNumVector(int[] intArrayInput) {
        String stringNumVectorR = "c(";
        int i = 0;
        while (i < intArrayInput.length) {
            stringNumVectorR = i > 0 ? String.valueOf(stringNumVectorR) + ", " + intArrayInput[i] : String.valueOf(stringNumVectorR) + intArrayInput[i];
            ++i;
        }
        stringNumVectorR = String.valueOf(stringNumVectorR) + ")";
        return stringNumVectorR;
    }

    public String createRNumVector(Integer[] intArrayInput) {
        String stringNumVectorR = "c(";
        int i = 0;
        while (i < intArrayInput.length) {
            stringNumVectorR = i > 0 ? String.valueOf(stringNumVectorR) + ", " + intArrayInput[i] : String.valueOf(stringNumVectorR) + intArrayInput[i];
            ++i;
        }
        stringNumVectorR = String.valueOf(stringNumVectorR) + ")";
        return stringNumVectorR;
    }

    public String createRNumVector(double[] doubleArrayInput) {
        String stringNumVectorR = "c(";
        int i = 0;
        while (i < doubleArrayInput.length) {
            stringNumVectorR = i > 0 ? String.valueOf(stringNumVectorR) + ", " + doubleArrayInput[i] : String.valueOf(stringNumVectorR) + doubleArrayInput[i];
            ++i;
        }
        stringNumVectorR = String.valueOf(stringNumVectorR) + ")";
        return stringNumVectorR;
    }

    public String createRNumVector(String[] stringArrayInput) {
        String stringNumVectorR = "c(";
        int i = 0;
        while (i < stringArrayInput.length) {
            stringNumVectorR = i > 0 ? String.valueOf(stringNumVectorR) + ", " + stringArrayInput[i] : String.valueOf(stringNumVectorR) + stringArrayInput[i];
            ++i;
        }
        stringNumVectorR = String.valueOf(stringNumVectorR) + ")";
        return stringNumVectorR;
    }

    public String createRList(String[] stringArrayInput, Integer[] integerLevelInput) {
        String tempList = "list(";
        int i = 0;
        while (i < stringArrayInput.length) {
            tempList = i > 0 ? String.valueOf(tempList) + ", " + stringArrayInput[i] + " = " + integerLevelInput[i] : String.valueOf(tempList) + stringArrayInput[i] + " = " + integerLevelInput[i];
            ++i;
        }
        tempList = String.valueOf(tempList) + ")";
        return tempList;
    }

    public String createRList(String[] stringArrayInput, Integer[] startVal, Integer[] integerLevelInput) {
        String tempList = "list(";
        int i = 0;
        while (i < stringArrayInput.length) {
            tempList = i > 0 ? String.valueOf(tempList) + ", " + stringArrayInput[i] + " = c(" + startVal[i] + ":" + integerLevelInput[i] + ")" : String.valueOf(tempList) + stringArrayInput[i] + " = c(" + startVal[i] + ":" + integerLevelInput[i] + ")";
            ++i;
        }
        tempList = String.valueOf(tempList) + ")";
        return tempList;
    }

    public String createRList(String[] stringArrayInput, Integer[] integerLevelInput, String[] stringArrayIDInput) {
        String tempList = "list(";
        int i = 0;
        while (i < stringArrayInput.length) {
            String stringVectorR = "c(";
            int j = 1;
            while (j <= integerLevelInput[i]) {
                stringVectorR = j > 1 ? String.valueOf(stringVectorR) + ", " + "\"" + stringArrayIDInput[i] + j + "\"" : String.valueOf(stringVectorR) + "\"" + stringArrayIDInput[i] + j + "\"";
                ++j;
            }
            stringVectorR = String.valueOf(stringVectorR) + ")";
            tempList = i > 0 ? String.valueOf(tempList) + ", " + stringArrayInput[i] + " = " + stringVectorR : String.valueOf(tempList) + stringArrayInput[i] + " = " + stringVectorR;
            ++i;
        }
        tempList = String.valueOf(tempList) + ")";
        return tempList;
    }

    public String createRVector(ArrayList<String[]> stringArrayListInput) {
        boolean inputOne = false;
        String toreturn = "";
        for (String[] s : stringArrayListInput) {
            if (stringArrayListInput.size() > 1) {
                toreturn = String.valueOf(toreturn) + this.createRVector(s) + ",";
                continue;
            }
            inputOne = true;
            toreturn = this.createRVector(s);
        }
        if (!inputOne) {
            toreturn = toreturn.substring(0, toreturn.length() - 1);
        }
        return toreturn;
    }

    public String createRColorRGBVector(String[] stringArrayInput) {
        String stringVectorR = "c(";
        int i = 0;
        while (i < stringArrayInput.length) {
            stringVectorR = i > 0 ? String.valueOf(stringVectorR) + "," + stringArrayInput[i] : String.valueOf(stringVectorR) + stringArrayInput[i];
            ++i;
        }
        stringVectorR = String.valueOf(stringVectorR) + ")";
        return stringVectorR;
    }

    public String subSetInputTransform(ArrayList<String> subsetConditions) {
        String toreturn = "c(";
        if (subsetConditions.size() > 1) {
            toreturn = "c((";
        }
        for (String s : subsetConditions) {
            String[] con = s.split(":");
            if (con[2].contains(",")) {
                String[] value = con[2].split(",");
                int i = 0;
                while (i < value.length) {
                    toreturn = con[3].equals("Numeric") ? String.valueOf(toreturn) + con[0] + con[1] + value[i] + "|" : String.valueOf(toreturn) + con[0] + con[1] + "\"" + value[i] + "\"|";
                    ++i;
                }
                toreturn = String.valueOf(toreturn.substring(0, toreturn.length() - 1)) + ")";
            } else {
                toreturn = con[3].equals("Numeric") ? String.valueOf(toreturn) + con[0] + con[1] + con[2] + ")" : String.valueOf(toreturn) + con[0] + con[1] + "\"" + con[2] + "\")";
            }
            toreturn = String.valueOf(toreturn) + "&(";
        }
        toreturn = subsetConditions.size() > 1 ? String.valueOf(toreturn.substring(0, toreturn.length() - 2)) + ")" : toreturn.substring(0, toreturn.length() - 2);
        return toreturn;
    }
}

