package test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Histogram {

    public static JFreeChart createHistogram(double[][] doubleMatrix, String xTitle, String yTitle){

        // Generate a one dimensional array of the size w*h of the double matrix
        ArrayList<Double> dataArrayList = new ArrayList<Double>();

        for (int i=0; i<doubleMatrix.length; i++) {
            for (int j = 0; j < doubleMatrix[i].length; j++) {
                double value =  doubleMatrix[i][j];
                if( Double.isNaN(value))
                    continue;
                else
                    dataArrayList.add(value);
                System.out.println(value);
            }
        }

        double[] data = new double[dataArrayList.size()];

        for(int p = 0; p < dataArrayList.size();p++)
            data[p] = dataArrayList.get(p);


        // int number = data.length;
        HistogramDataset dataset = new HistogramDataset();
        //dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("Hist",data,200); // Number of bins is 50
        String plotTitle = "";
        String xAxis = xTitle;
        String yAxis = yTitle;
        PlotOrientation orientation = PlotOrientation.VERTICAL;

        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xAxis, yAxis,
                dataset, orientation, show, toolTips, urls);

        chart.setBackgroundPaint(Color.white);

        return chart;
    }

    public static void displayChart(double[][] ex, String x, String y){
        Histogram hist = new Histogram();
        JFrame f = new JFrame();
        var test = hist.createHistogram(ex, x, y);
        ChartPanel chartpanel = new ChartPanel(test);
        f.add(chartpanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
    public static void main(String[] args) {
        //Histogram histogram = new Histogram();
        double[][] ex = {   { 20, 18, 22, 20, 16 },
                { 18, 20, 18, 21, 20 },
                { 16, 18, 16, 20, 24 },
                {  25, 24, 22, 24, 25 }
        };
        Histogram hist = new Histogram();
        hist.displayChart(ex, "x", "y");
    }
}