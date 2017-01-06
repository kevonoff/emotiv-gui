/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotiv.core.wrapper;

import org.koff.emotiv.core.*;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;
import java.util.List;
import java.util.Map;
import org.koff.emotiv.core.Edk;
import org.koff.emotiv.core.EdkErrorCode;

/**
 *
 * @author kevin.off
 */
public class EmotivWrapper {
    
    private final IntByReference userId;
    
    private static final String[] BAND_NAMES = {"Theta", "Alpha", "Low Beta", "High Beta", "Gamma"};
    
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
        int connected = Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5");
        return connected == EdkErrorCode.EDK_OK.ToInt();
    }
    
    public boolean ConnectToEmoComposer(){
        int connected = Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", (short)1726, "Emotiv Systems-5");
        return connected == EdkErrorCode.EDK_OK.ToInt();
    }
    
    public void disconnect(){
        Edk.INSTANCE.IEE_EngineDisconnect();
    }
    
    public List<Map<String, Double>> getBandPowersForAllChannels(){
        return BandPower.getAllBandPowers(Edk.INSTANCE);
    }
    
    public List<Map<String, Double>> getBandPowersForAllChannelsFake(){
        return BandPower.getAllBandPowersFake();
    }
    
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
