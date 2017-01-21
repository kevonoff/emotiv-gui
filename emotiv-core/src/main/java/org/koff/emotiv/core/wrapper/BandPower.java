/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.core.wrapper;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.koff.emotiv.core.Edk;
import org.koff.emotiv.core.EdkErrorCode;

/**
 *
 * @author kevin.off
 */
public class BandPower {

    static boolean ready = false;
    
    /**
     * Returns band powers for all channels and all bands in an array indexed by
     * [channelIndex][bandindex]
     *
     * @param edkInstance
     * @return
     */
    public static List<Map<String, Double>> getAllBandPowers(Pointer eState, Pointer eEvent, IntByReference userId) {

        if (EmotivWrapper.test) {
            return getAllBandPowersFake();
        }
        
        int state;

        while (!BandPower.ready) {
            state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

            // New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {
                int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userId);

                // Log the EmoState if it has been updated
                if (eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt()) {
                    if (userId != null) {
                        System.out.println("User added");
                        ready = true;
                    }
                }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                throw new RuntimeException("Internal error in Emotiv Engine!");
            }
        }

        DoubleByReference alpha = new DoubleByReference(0);
        DoubleByReference low_beta = new DoubleByReference(0);
        DoubleByReference high_beta = new DoubleByReference(0);
        DoubleByReference gamma = new DoubleByReference(0);
        DoubleByReference theta = new DoubleByReference(0);
        //   [channel] [band] = power
        //double[][] powers = new double[5][5];
        List<Map<String, Double>> powers = new LinkedList<>();
        Map<String, Double> channel;
        for (int channelIndex = 0; channelIndex < 5; channelIndex++) {
            int channelType = EmotivWrapper.getChannelType(channelIndex);
            int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userId.getValue(), channelType, theta, alpha, low_beta, high_beta, gamma);
            if(result == EdkErrorCode.EDK_OK.ToInt()){
                channel = new HashMap<>();
                channel.put(EmotivWrapper.getBandName(0), theta.getValue());
                channel.put(EmotivWrapper.getBandName(1), alpha.getValue());
                channel.put(EmotivWrapper.getBandName(2), low_beta.getValue());
                channel.put(EmotivWrapper.getBandName(3), high_beta.getValue());
                channel.put(EmotivWrapper.getBandName(4), gamma.getValue());
                powers.add(channel);
            }else{
                //throw new RuntimeException("Error retrieving average band powers for channel " + channelIndex + ". Error code: " + result);
            }
        }

        return powers;
    }

    public static List<Map<String, Double>> getAllBandPowersFake() {

        //   [channel] [band] = power
        //double[][] powers = new double[5][5];
        List<Map<String, Double>> powers = new LinkedList<>();
        Map<String, Double> channel;
        for (int channelIndex = 0; channelIndex < 5; channelIndex++) {

            channel = new HashMap<>();
            channel.put(EmotivWrapper.getBandName(0), Math.random() * 100);
            channel.put(EmotivWrapper.getBandName(1), Math.random() * 100);
            channel.put(EmotivWrapper.getBandName(2), Math.random() * 100);
            channel.put(EmotivWrapper.getBandName(3), Math.random() * 100);
            channel.put(EmotivWrapper.getBandName(4), Math.random() * 100);
            powers.add(channel);
        }

        return powers;
    }

}
