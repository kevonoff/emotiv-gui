/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.gui.chart;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;
import org.koff.emotiv.core.wrapper.EmotivWrapper;



/**
 *
 * @author kevin.off
 */
public class ChartSwingWorker extends SwingWorker<Boolean, Map<String, List<Double>>>{

    private final EmotivWrapper emotiv;
    private final Map<String, BandChart> charts;
    private Map<String, LinkedList<Double>> data;
    
    public ChartSwingWorker(EmotivWrapper wrapper, Map<String, BandChart> charts){
        this.emotiv = wrapper;
        this.charts = charts;
        initializeData();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        
        while(!isCancelled()){
        
            List<Map<String, Double>> powers = emotiv.getBandPowersForAllChannelsFake();

            Map<String, List<Double>> newData = new HashMap<>();
            
            Map<String, Double> frontLeft = powers.get(0);
            Map<String, Double> frontRight = powers.get(1);

            Double newValue;
            for(String bandName : EmotivWrapper.getBandNames()){
                
                newValue = (frontLeft.get(bandName) + frontRight.get(bandName)) / 2;
                data.get(bandName).add(newValue);
                data.get(bandName).removeFirst();
                newData.put(bandName, data.get(bandName));
                
            }

            publish(newData);
            
            Thread.sleep(500);
        }
        return true;
    }
    
    @Override
    protected void process(List<Map<String, List<Double>>> chunks) {
        
        Map<String, List<Double>> newData = chunks.get(chunks.size() - 1);
        BandChart chart;
        for(String bandName : newData.keySet()){
            chart = charts.get(bandName);
            chart.updateChart(newData.get(bandName));
        }
        
    }
    
    private void initializeData(){
        data = new HashMap<>();
        LinkedList<Double> allZeros;
        
        for(String bandName : EmotivWrapper.getBandNames()){
            allZeros = new LinkedList<>();
            for(int i = 0; i < 100; i++){
                allZeros.add(0d);
            }
            data.put(bandName, allZeros);
        }
    }
}
