/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.gui.chart;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.koff.emotiv.core.wrapper.EmotivWrapper;



/**
 *
 * @author kevin.off
 */
public class ChartSwingWorker {

    SwingWrapper<XYChart> sw;
    double phase = 0;
    
    public void show(JPanel panel) {
        
        
        for(String band : EmotivWrapper.getBandNames()){
            addNewChart(panel, band);
        }
    }
    
    

    private static double[][] getSineData(double phase) {

        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
            double radians = phase + (2 * Math.PI / xData.length * i);
            xData[i] = radians;
            yData[i] = Math.sin(radians);
        }
        return new double[][]{xData, yData};
    }

    private void addNewChart(JPanel parent, String name){
        double[][] initdata = getSineData(phase);
        // Create Chart
        XYChart chart = QuickChart.getChart("title", "x", "y", name, initdata[0], initdata[1]);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setChartTitleVisible(false);
        
        XChartPanel chartPanel = new XChartPanel(chart);
        chartPanel.setAlignmentX(0f);
        
        JLabel label = new JLabel(name, JLabel.LEFT);
        label.setAlignmentX(0f);
        
        parent.add(label);
        parent.add(chartPanel);
        parent.revalidate();
    }
    
}
