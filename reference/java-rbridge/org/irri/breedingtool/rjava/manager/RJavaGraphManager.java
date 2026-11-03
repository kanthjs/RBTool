/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.rosuda.JRI.Rengine
 */
package org.irri.breedingtool.rjava.manager;

import org.irri.breedingtool.rjava.manager.api.IRJavaGraphManager;
import org.irri.breedingtool.rjava.utilities.InputTransform;
import org.rosuda.JRI.Rengine;

public class RJavaGraphManager
implements IRJavaGraphManager {
    private Rengine rEngine;
    private InputTransform inputTransform;

    public RJavaGraphManager(Rengine rEngine) {
        this.rEngine = rEngine;
        this.inputTransform = new InputTransform();
    }

    private void rEngineEnd() {
        String rm = "rm(list=ls(all=TRUE))";
        this.rEngine.eval(rm);
        this.rEngine.end();
    }

    @Override
    public void createGraphHistogram1(String plotPath, String dataFileName, String respvar) {
        String graphHist = "g <- tryCatch(CreateGraphHistogram(\"" + plotPath + "\", dataName = \"" + dataFileName + "\", respvar = \"" + respvar + "\"), error = function(err) \"notRun\")";
        this.rEngine.eval(graphHist);
        System.out.println(plotPath);
        System.out.println("graphHist: " + graphHist);
        String runSuccessG = this.rEngine.eval("g").asString();
        System.out.println("runSuccessG: " + runSuccessG);
    }

    @Override
    public void createGraphBarplot(String outputPath, String dataFileName, String[] nVar, String cVar, String[] clustVars, String mTitle, String[] yAxisLab, String xAxisLab, String[] minValue, String[] maxValue, String typeData, String descStat, String barsHoriz, String barsClust, String byVar, String errBars, String typeErrBar, int errMult, double confLev, int axisLabelStyle, String[] barColor, String showLeg, String legPos, String legTitle, int legCol, String boxed, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs, String showCatVarLevels, int[] barDensity, int[] barLineAngle) {
        String nVarVector = this.inputTransform.createRVector(nVar);
        String barDensityVector = this.inputTransform.createRNumVector(barDensity);
        String barLineAngleVector = this.inputTransform.createRNumVector(barLineAngle);
        String yAxisLabVector = this.inputTransform.createRVector(yAxisLab);
        String minValueVector = this.inputTransform.createRNumVector(minValue);
        String maxValueVector = this.inputTransform.createRNumVector(maxValue);
        String clustVarsVector = clustVars == null ? "NULL" : this.inputTransform.createRVector(clustVars);
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        xAxisLab = xAxisLab == null ? "NULL" : "\"" + xAxisLab + "\"";
        descStat = descStat == null ? "NULL" : "\"" + descStat + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        typeErrBar = typeErrBar == null ? "NULL" : "\"" + typeErrBar + "\"";
        legTitle = legTitle == null ? "NULL" : "\"" + legTitle + "\"";
        String barColorVector = null;
        barColorVector = !barColor[0].equals("NULL") ? this.inputTransform.createRColorRGBVector(barColor) : barColor[0];
        String graphBar = "g <- try(GraphBar(data, \"" + outputPath + "\", " + nVarVector + ", \"" + cVar + "\", " + clustVarsVector + ", " + mTitle + ", " + yAxisLabVector + ", " + xAxisLab + ", " + minValueVector + ", " + maxValueVector + ", \"" + typeData + "\", " + descStat + ", " + barsHoriz + ", " + barsClust + ", " + byVar + ", " + errBars + ", " + typeErrBar + ", " + errMult + ", " + confLev + ", " + axisLabelStyle + ", " + barColorVector + ", " + showLeg + ", \"" + legPos + "\", " + legTitle + ", " + legCol + ", " + boxed + ", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\" , " + showCatVarLevels + ", " + barDensityVector + ", " + barLineAngleVector + "), silent = TRUE)";
        System.out.println(graphBar);
        this.readData(dataFileName);
        this.rEngine.eval(graphBar);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphBar_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphBar function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void createGraphLineplot(String outputPath, String dataFileName, String[] yVars, String xVar, String[] lineVars, String mTitle, String[] yAxisLab, String xAxisLab, String[] yMinValue, String[] yMaxValue, int axisLabelStyle, String byVar, String errBars, String typeErrBar, int errMult, double confLev, String[] plotCol, String showLineLabels, String showLeg, String legPos, String legTitle, int legCol, String boxed, String[] linePtTypes, int[] lineTypes, double[] lineWidths, String[] pointChars, double[] pointCharSizes, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs) {
        String yVarsVector = this.inputTransform.createRVector(yVars);
        String yAxisLabVector = this.inputTransform.createRVector(yAxisLab);
        String yMinValueVector = this.inputTransform.createRNumVector(yMinValue);
        String yMaxValueVector = this.inputTransform.createRNumVector(yMaxValue);
        String linePtTypesVector = this.inputTransform.createRVector(linePtTypes);
        String lineTypesVector = this.inputTransform.createRNumVector(lineTypes);
        String lineWidthsVector = this.inputTransform.createRNumVector(lineWidths);
        String pointCharsVector = this.inputTransform.createRNumVector(pointChars);
        String pointCharSizesVector = this.inputTransform.createRNumVector(pointCharSizes);
        String lineVarsVector = lineVars == null ? "NULL" : this.inputTransform.createRVector(lineVars);
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        xAxisLab = xAxisLab == null ? "NULL" : "\"" + xAxisLab + "\"";
        xVar = xVar == null ? "NULL" : "\"" + xVar + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        typeErrBar = typeErrBar == null ? "NULL" : "\"" + typeErrBar + "\"";
        legTitle = legTitle == null ? "NULL" : "\"" + legTitle + "\"";
        String plotColVector = null;
        plotColVector = !plotCol[0].equals("NULL") ? this.inputTransform.createRColorRGBVector(plotCol) : plotCol[0];
        String graphLine = "g <- try(GraphLine(data, \"" + outputPath + "\", " + yVarsVector + ", " + xVar + ", " + lineVarsVector + ", " + mTitle + ", " + yAxisLabVector + ", " + xAxisLab + ", " + yMinValueVector + ", " + yMaxValueVector + ", " + axisLabelStyle + ", " + byVar + ", " + errBars + ", " + typeErrBar + ", " + errMult + ", " + confLev + ", " + plotColVector + ", " + showLineLabels + ", " + showLeg + ", \"" + legPos + "\", " + legTitle + ", " + legCol + ", " + boxed + ", " + linePtTypesVector + ", " + lineTypesVector + ", " + lineWidthsVector + ", " + pointCharsVector + ", " + pointCharSizesVector + ", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\"), silent = TRUE)";
        System.out.println(graphLine);
        this.readData(dataFileName);
        this.rEngine.eval(graphLine);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphLine_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphLine function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void createGraphScatterplot(String outputPath, String dataFileName, String[] xVar, String[] yVar, String mTitle, String[] xAxisLab, String[] yAxisLab, String[] xMinValue, String[] xMaxValue, String[] yMinValue, String[] yMaxValue, int axisLabelStyle, String byVar, String pointCol, int pointChar, double pointCharSize, String dispLine, int lineType, String lineCol, double lineWidth, String dispR2P, String r2PPos, String boxed, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs) {
        String xVarVector = this.inputTransform.createRVector(xVar);
        String yVarVector = this.inputTransform.createRVector(yVar);
        String xAxisLabVector = this.inputTransform.createRVector(xAxisLab);
        String yAxisLabVector = this.inputTransform.createRVector(yAxisLab);
        String xMinValueVector = this.inputTransform.createRNumVector(xMinValue);
        String xMaxValueVector = this.inputTransform.createRNumVector(xMaxValue);
        String yMinValueVector = this.inputTransform.createRNumVector(yMinValue);
        String yMaxValueVector = this.inputTransform.createRNumVector(yMaxValue);
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        String graphScatter = "g <- try(GraphScatter(data, \"" + outputPath + "\", " + xVarVector + ", " + yVarVector + ", " + mTitle + ", " + xAxisLabVector + ", " + yAxisLabVector + ", " + xMinValueVector + ", " + xMaxValueVector + ", " + yMinValueVector + ", " + yMaxValueVector + ", " + axisLabelStyle + ", " + byVar + ", " + boxed + ", " + pointCol + ", " + pointChar + ", " + pointCharSize + ", " + dispLine + ", " + lineType + ", " + lineCol + ", " + lineWidth + ", " + dispR2P + ", \"" + r2PPos + "\", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\"), silent = TRUE)";
        System.out.println(graphScatter);
        this.readData(dataFileName);
        this.rEngine.eval(graphScatter);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphScatter_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphScatter function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void createGraphBoxplot(String outputPath, String dataFileName, String[] nVar, String[] cVars, String mTitle, String[] nAxisLab, String cAxisLab, String[] yMinValue, String[] yMaxValue, int axisLabelStyle, String plotHoriz, String byVar, double boxSize, String boxColor, String boxFillColor, int medLineType, double medLineWidth, String medColor, int whiskLineType, double whiskLineWidth, String whiskColor, int outChar, double outCharSize, String outColor, String boxed, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs) {
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        cAxisLab = cAxisLab == null ? "NULL" : "\"" + cAxisLab + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        String cVarsVector = cVars == null ? "NULL" : this.inputTransform.createRVector(cVars);
        String nVarVector = this.inputTransform.createRVector(nVar);
        String nAxisLabVector = this.inputTransform.createRVector(nAxisLab);
        String yMinValueVector = this.inputTransform.createRNumVector(yMinValue);
        String yMaxValueVector = this.inputTransform.createRNumVector(yMaxValue);
        String graphBox = "g <- try(GraphBoxplot(data, \"" + outputPath + "\", " + nVarVector + ", " + cVarsVector + ", " + mTitle + ", " + nAxisLabVector + ", " + cAxisLab + ", " + yMinValueVector + ", " + yMaxValueVector + ", " + axisLabelStyle + ", " + plotHoriz + ", " + byVar + ", " + boxSize + ", " + boxColor + ", " + boxFillColor + ", " + medLineType + ", " + medLineWidth + ", " + medColor + ", " + whiskLineType + ", " + whiskLineWidth + ", " + whiskColor + ", " + outChar + ", " + outCharSize + ", " + outColor + ", " + boxed + ", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\"), silent = TRUE)";
        System.out.println(graphBox);
        this.readData(dataFileName);
        this.rEngine.eval(graphBox);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphBoxplot_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphBoxplot function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void createGraphHist(String outputPath, String dataFileName, String[] nVar, String mTitle, String[] yAxisLabs, String[] xAxisLabs, String[] yMinValues, String[] yMaxValues, String[] xMinValues, String[] xMaxValues, String useFreq, String numBins, int axisLabelStyle, String byVar, String barColor, String dispCurve, int lineType, String lineColor, double lineWidth, String boxed, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs) {
        String nVarVector = this.inputTransform.createRVector(nVar);
        String yAxisLabsVector = this.inputTransform.createRVector(yAxisLabs);
        String xAxisLabsVector = this.inputTransform.createRVector(xAxisLabs);
        String yMinValuesVector = this.inputTransform.createRNumVector(yMinValues);
        String yMaxValuesVector = this.inputTransform.createRNumVector(yMaxValues);
        String xMinValuesVector = this.inputTransform.createRNumVector(xMinValues);
        String xMaxValuesVector = this.inputTransform.createRNumVector(xMaxValues);
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        if (numBins.equals("Auto")) {
            numBins = "\"Sturges\"";
        }
        String graphHist = "g <- try(GraphHist(data, \"" + outputPath + "\", " + nVarVector + ", " + mTitle + ", " + yAxisLabsVector + ", " + xAxisLabsVector + ", " + yMinValuesVector + ", " + yMaxValuesVector + ", " + xMinValuesVector + ", " + xMaxValuesVector + ", " + useFreq + ", " + numBins + ", " + axisLabelStyle + ", " + byVar + ", " + barColor + ", " + dispCurve + ", " + lineType + ", " + lineColor + ", " + lineWidth + ", " + boxed + ", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\"), silent = TRUE)";
        System.out.println(graphHist);
        this.readData(dataFileName);
        this.rEngine.eval(graphHist);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphHist_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphHist function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    @Override
    public void createGraphPie(String outputPath, String dataFileName, String cVar, String mTitle, String wVar, String byVar, String[] sliceColors, String useLabels, String showLeg, String legPos, String legTitle, int legCol, int[] pieDensity, int[] pieLineAngle, String boxed, String multGraphs, int numRowsGraphs, int numColsGraphs, String orientGraphs) {
        mTitle = mTitle == null ? "NULL" : "\"" + mTitle + "\"";
        wVar = wVar == null ? "NULL" : "\"" + wVar + "\"";
        byVar = byVar == null ? "NULL" : "\"" + byVar + "\"";
        legTitle = legTitle == null ? "NULL" : "\"" + legTitle + "\"";
        String pieDensityVector = this.inputTransform.createRNumVector(pieDensity);
        String pieLineAngleVector = this.inputTransform.createRNumVector(pieLineAngle);
        String dispLabels = useLabels.equals("none") ? "FALSE" : "TRUE";
        String sliceColorsVector = null;
        sliceColorsVector = !sliceColors[0].equals("NULL") ? this.inputTransform.createRColorRGBVector(sliceColors) : sliceColors[0];
        String graphPie = "g <- try(GraphPie(data, \"" + outputPath + "\", \"" + cVar + "\", " + mTitle + ", " + wVar + ", " + byVar + ", " + sliceColorsVector + ", " + dispLabels + ", \"" + useLabels + "\", " + showLeg + ", \"" + legPos + "\", " + legTitle + ", " + legCol + ", " + pieDensityVector + ", " + pieLineAngleVector + ", " + boxed + ", " + multGraphs + ", " + numRowsGraphs + ", " + numColsGraphs + ", \"" + orientGraphs + "\"), silent = TRUE)";
        System.out.println(graphPie);
        this.readData(dataFileName);
        this.rEngine.eval(graphPie);
        String runSuccessCommand = this.rEngine.eval("class(g)").asString();
        if (runSuccessCommand.equals("try-error")) {
            String outFilePath = String.valueOf(outputPath) + "GraphPie_error.txt";
            String sinkIn = "sink(\"" + outFilePath + "\")";
            String sinkOut = "sink()";
            String checkError = "if (class(g) == \"try-error\") {\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(strsplit(g, \":\")[[1]])\n";
            checkError = String.valueOf(checkError) + "    msg <- trim.strings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))\n";
            checkError = String.valueOf(checkError) + "    msg <- gsub(\"\\\"\", \"\", msg)\n";
            checkError = String.valueOf(checkError) + "    cat(\"\n*** \nERROR in GraphPie function:\\n  \",msg, \"\n***\", sep = \"\")\n";
            checkError = String.valueOf(checkError) + "}";
            System.out.println(sinkIn);
            System.out.println(checkError);
            System.out.println(sinkOut);
            this.rEngine.eval(sinkIn);
            this.rEngine.eval(checkError);
            this.rEngine.eval(sinkOut);
        }
        this.rEngineEnd();
    }

    private void readData(String dataFileName) {
        String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"\"), blank.lines.skip=TRUE, sep = \",\")";
        System.out.println(readData);
        this.rEngine.eval(readData);
    }
}

