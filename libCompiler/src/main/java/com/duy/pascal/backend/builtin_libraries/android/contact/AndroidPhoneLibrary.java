/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duy.pascal.backend.builtin_libraries.android.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.PhonesColumns;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.builtin_libraries.PascalLibrary;
import com.duy.pascal.backend.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.backend.builtin_libraries.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalParameter;
import com.googlecode.sl4a.MainThread;
import com.googlecode.sl4a.facade.AndroidEvent;
import com.googlecode.sl4a.rpc.RpcStartEvent;
import com.googlecode.sl4a.rpc.RpcStopEvent;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Exposes TelephonyManager functionality.
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Felix Arends (felix.arends@gmail.com)
 */
public class AndroidPhoneLibrary implements PascalLibrary {

    private final AndroidUtilsLib mAndroidFacade;
    private final AndroidEvent mEventFacade;
    private final TelephonyManager mTelephonyManager;
    private final Bundle mPhoneState;
    private final Context mContext;
    private AndroidLibraryManager mManager;
    private PhoneStateListener mPhoneStateListener;

    public AndroidPhoneLibrary(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        mManager = manager;
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mAndroidFacade = manager.getReceiver(AndroidUtilsLib.class);
        mEventFacade = manager.getReceiver(AndroidEvent.class);
        mPhoneState = new Bundle();
        mPhoneStateListener = MainThread.run(mContext, new Callable<PhoneStateListener>() {
            @Override
            public PhoneStateListener call() throws Exception {
                return new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        mPhoneState.putString("incomingNumber", incomingNumber);
                        switch (state) {
                            case TelephonyManager.CALL_STATE_IDLE:
                                mPhoneState.putString("state", "idle");
                                break;
                            case TelephonyManager.CALL_STATE_OFFHOOK:
                                mPhoneState.putString("state", "offhook");
                                break;
                            case TelephonyManager.CALL_STATE_RINGING:
                                mPhoneState.putString("state", "ringing");
                                break;
                        }
                        mEventFacade.postEvent("phone", mPhoneState.clone());
                    }
                };
            }
        });
    }

    @Override
    public void shutdown() {
        stopTrackingPhoneState();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Starts tracking phone state.")
    @RpcStartEvent("phone")
    public void startTrackingPhoneState() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current phone state and incoming number.", returns = "A Map of \"state\" and \"incomingNumber\"")
    public Bundle readPhoneState() {
        return mPhoneState;
    }

    @PascalMethod(description = "Stops tracking phone state.")
    @RpcStopEvent("phone")
    public void stopTrackingPhoneState() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Calls a contact/phone number by URI.")
    public void phoneCall(@PascalParameter(name = "uri") final String uriString) throws Exception {
        Uri uri = Uri.parse(uriString);
        if (uri.getScheme().equals("content")) {
            String phoneNumberColumn = PhonesColumns.NUMBER;
            String selectWhere = null;
            if ((AndroidLibraryManager.class.cast(mManager)).getSdkLevel() >= 5) {
                Class<?> contactsContract_Data_class =
                        Class.forName("android.provider.ContactsContract$Data");
                Field RAW_CONTACT_ID_field = contactsContract_Data_class.getField("RAW_CONTACT_ID");
                selectWhere = RAW_CONTACT_ID_field.get(null).toString() + "=" + uri.getLastPathSegment();
                Field CONTENT_URI_field = contactsContract_Data_class.getField("CONTENT_URI");
                uri = Uri.parse(CONTENT_URI_field.get(null).toString());
                Class<?> ContactsContract_CommonDataKinds_Phone_class =
                        Class.forName("android.provider.ContactsContract$CommonDataKinds$Phone");
                Field NUMBER_field = ContactsContract_CommonDataKinds_Phone_class.getField("NUMBER");
                phoneNumberColumn = NUMBER_field.get(null).toString();
            }
            ContentResolver resolver = mContext.getContentResolver();
            Cursor c = resolver.query(uri, new String[]{phoneNumberColumn}, selectWhere, null, null);
            String number = "";
            if (c.moveToFirst()) {
                number = c.getString(c.getColumnIndexOrThrow(phoneNumberColumn));
            }
            c.close();
            phoneCallNumber(number);
        } else {
            mAndroidFacade.startActivity(Intent.ACTION_CALL, uriString, null, null, null, null, null);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Calls a phone number.")
    public void phoneCallNumber(@PascalParameter(name = "phone number") final String number)
            throws Exception {
        phoneCall("tel:" + URLEncoder.encode(number, "ASCII"));
    }

    @PascalMethod(description = "Dials a contact/phone number by URI.")
    public void phoneDial(@PascalParameter(name = "uri") final String uri) throws Exception {
        mAndroidFacade.startActivity(Intent.ACTION_DIAL, uri, null, null, null, null, null);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Dials a phone number.")
    public void phoneDialNumber(@PascalParameter(name = "phone number") final String number)
            throws Exception {
        phoneDial("tel:" + URLEncoder.encode(number, "ASCII"));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current cell location.")
    public CellLocation getCellLocation() {
        return mTelephonyManager.getCellLocation();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the numeric name (MCC+MNC) of current registered operator.")
    public String getNetworkOperator() {
        return mTelephonyManager.getNetworkOperator();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the alphabetic name of current registered operator.")
    public String getNetworkOperatorName() {
        return mTelephonyManager.getNetworkOperatorName();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a the radio technology (network operator) currently in use on the device.")
    public String getNetworkType() {
        // TODO(damonkohler): API level 5 has many more types.
        switch (mTelephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "edge";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "gprs";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "umts";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "unknown";
            default:
                return null;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the device phone operator.")
    public String getPhoneType() {
        // TODO(damonkohler): API level 4 includes CDMA.
        switch (mTelephonyManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return "gsm";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "none";
            default:
                return null;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the ISO country code equivalent for the SIM provider's country code.")
    public String getSimCountryIso() {
        return mTelephonyManager.getSimCountryIso();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.")
    public String getSimOperator() {
        return mTelephonyManager.getSimOperator();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the Service Provider Name (SPN).")
    public String getSimOperatorName() {
        return mTelephonyManager.getSimOperatorName();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the serial number of the SIM, if applicable. Return null if it is unavailable.")
    public String getSimSerialNumber() {
        return mTelephonyManager.getSimSerialNumber();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the state of the device SIM card.")
    public String getSimState() {
        switch (mTelephonyManager.getSimState()) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return "uknown";
            case TelephonyManager.SIM_STATE_ABSENT:
                return "absent";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return "pin_required";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return "puk_required";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return "network_locked";
            case TelephonyManager.SIM_STATE_READY:
                return "ready";
            default:
                return null;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the unique subscriber ID, for example, the IMSI for a GSM phone. Return null if it is unavailable.")
    public String getSubscriberId() {
        return mTelephonyManager.getSubscriberId();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Retrieves the alphabetic identifier associated with the voice mail number.")
    public String getVoiceMailAlphaTag() {
        return mTelephonyManager.getVoiceMailAlphaTag();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the voice mail number. Return null if it is unavailable.")
    public String getVoiceMailNumber() {
        return mTelephonyManager.getVoiceMailNumber();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns true if the device is considered roaming on the current network, for GSM purposes.")
    public Boolean checkNetworkRoaming() {
        return mTelephonyManager.isNetworkRoaming();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the unique device ID, for example, the IMEI for GSM and the MEID for CDMA phones. Return null if device ID is not available.")
    public String getDeviceId() {
        return mTelephonyManager.getDeviceId();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the software version number for the device, for example, the IMEI/SV for GSM phones. Return null if the software version is not available.")
    public String getDeviceSoftwareVersion() {
        return mTelephonyManager.getDeviceSoftwareVersion();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the phone number string for lineInfo 1, for example, the MSISDN for a GSM phone. Return null if it is unavailable.")
    public String getLine1Number() {
        return mTelephonyManager.getLine1Number();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the neighboring cell information of the device.")
    public List<NeighboringCellInfo> getNeighboringCellInfo() {
        return mTelephonyManager.getNeighboringCellInfo();
    }
}
