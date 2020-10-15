package test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.KeyedValues2DDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;
 
import java.awt.*;
import java.text.NumberFormat;

import javax.swing.JFrame;
 
public class TwoSidedBarChartDemo extends ApplicationFrame {
 
 private static final long serialVersionUID = 1L;
 
 private ChartPanel chartpanel;
 
 public TwoSidedBarChartDemo() {
      super("Two Sided Bar Chart Demo");
      KeyedValues2DDataset keyedvalues2ddataset = createDataset();
 
      // use stacked bar chart to have 2 columns on each row
      JFreeChart jfreechart = ChartFactory.createStackedBarChart(
    		  "Family #1 Lifespan Distribution By Gender", 
    		  "Gender", "", keyedvalues2ddataset, 
    		  PlotOrientation.HORIZONTAL, true, true, false);
      jfreechart.setBackgroundPaint(new Color(255, 255, 255, 0));
      CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();
      plot.setBackgroundPaint(Color.WHITE);
      //plot.setOutlineVisible(true);
      plot.getDomainAxis().setVisible(false);
      plot.getRangeAxis().setVisible(true);
 
      // add 3 lines to the chart (1 black, 2 white) =&gt; separation effect
      Marker marker = new ValueMarker(0);
      marker.setPaint(Color.black);
      marker.setStroke(new BasicStroke(2));
      plot.addRangeMarker(marker);
 
      marker = new ValueMarker(0.12);
      marker.setPaint(Color.WHITE);
      marker.setStroke(new BasicStroke(1.5f));
      plot.addRangeMarker(marker);
 
      marker = new ValueMarker(-0.12);
      marker.setPaint(Color.WHITE);
      marker.setStroke(new BasicStroke(1.5f));
      plot.addRangeMarker(marker);
 
      BarRenderer renderer = (BarRenderer) plot.getRenderer();
      //renderer.setBarPainter(new StandardBarPainter());
 
     // adjust the bar width and spacing between bars
      renderer.setMaximumBarWidth(.9);
 
      // custom columns color
      renderer.setSeriesPaint(0, Color.RED);
      renderer.setSeriesPaint(1, Color.BLUE);
 
      //renderer.setShadowVisible(false);
      renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{1}", 
    		  NumberFormat.getInstance()));
      renderer.setBaseItemLabelsVisible(true);
      renderer.setBaseItemLabelPaint(Color.BLUE);

 
      // custom item labels position
      ItemLabelPosition p1 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3, 
    		  TextAnchor.BASELINE_LEFT);
      renderer.setBasePositiveItemLabelPosition(p1);
      ItemLabelPosition p2 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, 
    		  TextAnchor.BASELINE_RIGHT);
      renderer.setBaseNegativeItemLabelPosition(p2);
 
      plot.getDomainAxis().setCategoryMargin(0.1);
 
      chartpanel = new ChartPanel(jfreechart);
      //ChartPanel chartpanel = new ChartPanel(jfreechart);
      chartpanel.setPreferredSize(new Dimension(900, 600));
      //setContentPane(chartpanel);
 }
 
 private KeyedValues2DDataset createDataset() {
     DefaultKeyedValues2DDataset defaultkeyedvalues2ddataset = new DefaultKeyedValues2DDataset();
     defaultkeyedvalues2ddataset.addValue(-0D, "Male", "26-30");
     defaultkeyedvalues2ddataset.addValue(-1D, "Male", "21-25");
     defaultkeyedvalues2ddataset.addValue(-3D, "Male", "16-20");
     defaultkeyedvalues2ddataset.addValue(-5D, "Male", "11-15");
     defaultkeyedvalues2ddataset.addValue(-17D, "Male", "6-10");
     defaultkeyedvalues2ddataset.addValue(-38D, "Male", "1-5");
     defaultkeyedvalues2ddataset.addValue(-26D, "Male", "0");
     defaultkeyedvalues2ddataset.addValue(3D, "Female", "26-30");
     defaultkeyedvalues2ddataset.addValue(3D, "Female", "21-25");
     defaultkeyedvalues2ddataset.addValue(3D, "Female", "16-20");
     defaultkeyedvalues2ddataset.addValue(9D, "Female", "11-15");
     defaultkeyedvalues2ddataset.addValue(35D, "Female", "1-5");
     defaultkeyedvalues2ddataset.addValue(12D, "Female", "6-10");
     defaultkeyedvalues2ddataset.addValue(32D, "Female", "0");
     return defaultkeyedvalues2ddataset;
 }
 /*
m	0	26
m	1-5	38
m	11-15	5
m	16-20	3
m	21-25	1
m	6-10	17
f	0	32
f	1-5	35
f	11-15	9
f	16-20	3
f	21-25	3
f	26-30	3
f	6-10	12
  */
 
 public static void main(String args[]) {
     TwoSidedBarChartDemo chartdemo = new TwoSidedBarChartDemo();
     JFrame f = new JFrame("Double-Sided Hitogram Test");
     //chartdemo.pack();
     //RefineryUtilities.centerFrameOnScreen(chartdemo);
     //chartdemo.setVisible(true);
     f.add(chartdemo.chartpanel);
     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     f.pack();
     f.setVisible(true);
 }
}