/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.core.wrapper;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.koff.emotiv.core.Edk;
import org.koff.emotiv.core.EdkErrorCode;
import org.koff.emotiv.core.EmoState;

/**
 *
 * @author kevin.off
 */
public class EmotivWrapper {
    
    private final IntByReference userId;
    private static final String[] BAND_NAMES = {"Theta", "Alpha", "Low Beta", "High Beta", "Gamma"};
    public static boolean test = false;
    public static boolean connected = false;
    Pointer eEvent;
    Pointer eState;
    
    private static final Edk.IEE_DataChannels_t[] CHANNEL_LIST = {
        Edk.IEE_DataChannels_t.IED_AF3, //front left
        Edk.IEE_DataChannels_t.IED_AF4, //front right
        Edk.IEE_DataChannels_t.IED_T7,  //left ear
        Edk.IEE_DataChannels_t.IED_T8,  //right ear
        Edk.IEE_DataChannels_t.IED_Pz   //top
    };
    
    public EmotivWrapper(){
        userId = new IntByReference(0);
    }
    
    public boolean connectToHeadset(){
        int attempts = 0;
        while (!connected && attempts < 6){
            attempts++;
            int conn = Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5");
            connected = conn == EdkErrorCode.EDK_OK.ToInt();
            if (!connected){
                Logger.getLogger(EmotivWrapper.class.getName()).log(Level.SEVERE, "Could not connect to the Emotiv. Trying {0} more times", 5 - attempts);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EmotivWrapper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                Logger.getLogger(EmotivWrapper.class.getName()).log(Level.INFO, "Connected to the Emotiv");
            }
        }
        if (!connected){
            Logger.getLogger(EmotivWrapper.class.getName()).log(Level.SEVERE, null, "Failed to connect to the Emotiv after 5 attempts.");
        }else{
            eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
            eState = Edk.INSTANCE.IEE_EmoStateCreate();
        }
        return connected;
    }
    
    public boolean ConnectToEmoComposer(){
        if (!connected){
            int conn = Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", (short)1726, "Emotiv Systems-5");
            connected = conn == EdkErrorCode.EDK_OK.ToInt();
        }
        return connected;
    }
    
    public void disconnect(){
        
        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(eState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
        
    }
    
    public List<Map<String, Double>> getBandPowersForAllChannels(){
        if (test){
            return BandPower.getAllBandPowersFake();
        }else{
            Logger.getLogger(EmotivWrapper.class.getName()).info("Getting Band Powers for all channels.");
            return BandPower.getAllBandPowers(eState, eEvent, userId);
        }
    }
    
//    public List<Double> getContactQualities(){
//        Pointer stuff = new Pointer(0);
//        
//        
//        EmoState.INSTANCE.IS_GetContactQualityFromAllChannels(Pointer.NULL, userId, 0);
//    }
    
    public static String[] getBandNames(){
        return BAND_NAMES;
    }
    
    public static String getBandName(int bandIndex){
        return BAND_NAMES[bandIndex];
    }
    
    public static int getChannelType(int channelIndex){
        return CHANNEL_LIST[channelIndex].getType();
    }
    
}
