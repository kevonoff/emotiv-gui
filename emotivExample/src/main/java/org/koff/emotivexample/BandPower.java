/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.koff.emotivexample;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import org.koff.emotiv.core.Edk;
import org.koff.emotiv.core.EdkErrorCode;

/**
 *
 * @author kevonoff
 */
public class BandPower {

    public static void main(String[] args) {
        BandPower bp = new BandPower();
        bp.DoIt();
    }
    
    public void DoIt() {
        Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
        Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();

        IntByReference userID = null;
        boolean ready = false;
        int state = 0;

        Edk.IEE_DataChannels_t dataChannel;

        userID = new IntByReference(0);

        if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
                .ToInt()) {
            System.out.println("Emotiv Engine start up failed.");
            return;
        }

        System.out.println("Start receiving Data!");
        System.out.println("Theta, Alpha, Low_beta, High_beta, Gamma");

        while (true) {
            state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

            // New event needs to be handled
            if (state == EdkErrorCode.EDK_OK.ToInt()) {
                int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
                Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);

                // Log the EmoState if it has been updated
                if (eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt()) {
                    if (userID != null) {
                        System.out.println("User added");
                        ready = true;
                    }
                }
            } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                System.out.println("Internal error in Emotiv Engine!");
                break;
            }

            if (ready) {

                DoubleByReference alpha = new DoubleByReference(0);
                DoubleByReference low_beta = new DoubleByReference(0);
                DoubleByReference high_beta = new DoubleByReference(0);
                DoubleByReference gamma = new DoubleByReference(0);
                DoubleByReference theta = new DoubleByReference(0);

                for (int i = 3; i < 4; i++) {
                    int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), i, theta, alpha, low_beta, high_beta, gamma);
                    if (result == EdkErrorCode.EDK_OK.ToInt()) {
                        System.out.print(theta.getValue());
                        System.out.print(", ");
                        System.out.print(alpha.getValue());
                        System.out.print(", ");
                        System.out.print(low_beta.getValue());
                        System.out.print(", ");
                        System.out.print(high_beta.getValue());
                        System.out.print(", ");
                        System.out.print(gamma.getValue());
                        System.out.print(", ");
                    }

                    System.out.println();
                }
            }
        }

        Edk.INSTANCE.IEE_EngineDisconnect();
        Edk.INSTANCE.IEE_EmoStateFree(eState);
        Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
        System.out.println("Disconnected!");
    }

}
