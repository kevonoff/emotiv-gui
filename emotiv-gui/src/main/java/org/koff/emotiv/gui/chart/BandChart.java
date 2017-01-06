/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.gui.chart;

import javax.swing.JLabel;
import javax.swing.SwingWorker;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/**
 *
 * @author kevin.off
 */
public class BandChart {
    
    String chartName;
    XYChart chart;
    XChartPanel panel;
    SwingWorker worker;
    
    public BandChart(String name){
        double[] xData = {0};
        double[] yData = {0};
        chartName = name;
        chart = QuickChart.getChart(name, "x", "y", name, xData, yData);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setChartTitleVisible(false);
        
        panel = new XChartPanel(chart);
        panel.setAlignmentX(0f);
    }
    
    public JLabel getLabel(){
        JLabel label = new JLabel(chartName, JLabel.LEFT);
        label.setAlignmentX(0f);
        return label;
    }
    
    public XChartPanel getPanel(){
        return panel;
    }
    
    public void updateChart(double[] yData){
        
        chart.updateXYSeries(chartName, null, yData, null);
        panel.revalidate();
        panel.repaint();
        
    }
    
}
