/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.android_scripting.facade;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.googlecode.android_scripting.MainThread;
import com.googlecode.android_scripting.jsonrpc.RpcReceiver;
import com.googlecode.android_scripting.rpc.PascalMethod;
import com.googlecode.android_scripting.rpc.RpcStartEvent;
import com.googlecode.android_scripting.rpc.RpcStopEvent;

import java.util.concurrent.Callable;

/**
 * Exposes SignalStrength functionality.
 *
 * @author Joerg Zieren (joerg.zieren@gmail.com)
 */
public class SignalStrengthFacade extends RpcReceiver {
    private final Context mContext;
    private final TelephonyManager mTelephonyManager;
    private final EventFacade mEventFacade;
    private final PhoneStateListener mPhoneStateListener;
    private Bundle mSignalStrengths;

    public SignalStrengthFacade(FacadeManager manager) {
        super(manager);
        mContext = manager.getContext();
        mEventFacade = manager.getReceiver(EventFacade.class);
        mTelephonyManager =
                (TelephonyManager) manager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = MainThread.run(mContext, new Callable<PhoneStateListener>() {
            @Override
            public PhoneStateListener call() throws Exception {
                return new PhoneStateListener() {
                    @Override
                    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                        mSignalStrengths = new Bundle();
                        mSignalStrengths.putInt("gsm_signal_strength", signalStrength.getGsmSignalStrength());
                        mSignalStrengths.putInt("gsm_bit_error_rate", signalStrength.getGsmBitErrorRate());
                        mSignalStrengths.putInt("cdma_dbm", signalStrength.getCdmaDbm());
                        mSignalStrengths.putInt("cdma_ecio", signalStrength.getCdmaEcio());
                        mSignalStrengths.putInt("evdo_dbm", signalStrength.getEvdoDbm());
                        mSignalStrengths.putInt("evdo_ecio", signalStrength.getEvdoEcio());
                        mEventFacade.postEvent("signal_strengths", mSignalStrengths.clone());
                    }
                };
            }
        });
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Starts tracking signal strengths.")
    @RpcStartEvent("signal_strengths")
    public void startTrackingSignalStrengths() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current signal strengths.", returns = "A map of \"gsm_signal_strength\"")
    public Bundle readSignalStrengths() {
        return mSignalStrengths;
    }

    @PascalMethod(description = "Stops tracking signal strength.")
    @RpcStopEvent("signal_strengths")
    public void stopTrackingSignalStrengths() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void shutdown() {
        stopTrackingSignalStrengths();
    }
}
