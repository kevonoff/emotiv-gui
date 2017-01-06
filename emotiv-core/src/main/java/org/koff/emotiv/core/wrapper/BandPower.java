/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.core.wrapper;

import com.sun.jna.ptr.DoubleByReference;
import org.koff.emotiv.core.Edk;

/**
 *
 * @author kevin.off
 */
public class BandPower {

    /**
     * Returns band powers for all channels and all bands in an
     * array indexed by [channelIndex][bandindex]
     * @param edkInstance
     * @return 
     */
    public static double[][] getBandPowers(Edk edkInstance){
        
        DoubleByReference alpha     = new DoubleByReference(0);
        DoubleByReference low_beta  = new DoubleByReference(0);
        DoubleByReference high_beta = new DoubleByReference(0);
        DoubleByReference gamma     = new DoubleByReference(0);
        DoubleByReference theta     = new DoubleByReference(0);
        //   [channel] [band] = power
        double[][] powers = new double[5][5];
        
        for(int channelIndex = 0; channelIndex < 5; channelIndex++){
            int channelType = EmotivWrapper.getChannelType(channelIndex);
            edkInstance.IEE_GetAverageBandPowers(0, channelType, theta, alpha, low_beta, high_beta, gamma);
            powers[channelIndex][0] = theta.getValue();
            powers[channelIndex][1] = alpha.getValue();
            powers[channelIndex][2] = low_beta.getValue();
            powers[channelIndex][3] = high_beta.getValue();
            powers[channelIndex][4] = gamma.getValue();
        }
        
        return powers;
    }
    
}
