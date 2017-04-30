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

package com.duy.pascal.backend.lib.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.RpcStopEvent;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Exposes Batterymanager API. Note that in order to use any of the batteryGet* functions, you need
 * to batteryStartMonitoring, and then wait for a "battery" event. Sleeping for a second will
 * usually work just as well.
 *
 * @author Alexey Reznichenko (alexey.reznichenko@gmail.com)
 * @author Robbie Matthews (rjmatthews62@gmail.com)
 */
public class AndroidBatteryLib implements PascalLibrary {
    public static final String NAME = "aBattery".toLowerCase();
    /**
     * Power source is an AC charger.
     */

    public static final int BATTERY_PLUGGED_AC = 1;
    /**
     * Power source is a USB port.
     */

    public static final int BATTERY_PLUGGED_USB = 2;
    private final Context mContext;
    private BatteryStateListener mReceiver;
    private volatile Bundle mBatteryData = null;
    private volatile int mBatteryStatus = -1;
    private volatile int mBatteryHealth = -1;
    private volatile int mPlugType = -1;
    private volatile boolean mBatteryPresent = false;
    private volatile int mBatteryLevel = -1;
    private volatile int mBatteryMaxLevel = -1;
    private volatile int mBatteryVoltage = -1;
    private volatile int mBatteryTemperature = -1;
    private volatile String mBatteryTechnology = null;

    public AndroidBatteryLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        mReceiver = null;
        mBatteryData = null;
    }

    private String getBatteryManagerFieldValue(String name) {
        try {
            Field f = BatteryManager.class.getField(name);
            return f.get(null).toString();
        } catch (Exception e) {
            Log.e(e);
        }
        return null;
    }


    @PascalMethod(description = "Returns the most recently recorded battery data.")
    public Bundle readBatteryData() {
        return mBatteryData;
    }

    /**
     * throws "battery" events
     */

    @PascalMethod(description = "Starts tracking battery state.")
    public void batteryStartMonitoring() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            mReceiver = new BatteryStateListener();
            mContext.registerReceiver(mReceiver, filter);
        }
    }

    @PascalMethod(description = "Stops tracking battery state.")
    @RpcStopEvent("battery")
    public void batteryStopMonitoring() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        mBatteryData = null;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {
        batteryStopMonitoring();
    }


    @PascalMethod(description = "Returns  the most recently received battery status data:" + "\n1 - unknown;"
            + "\n2 - charging;" + "\n3 - discharging;" + "\n4 - not charging;" + "\n5 - full;")
    public int batteryGetStatus() {
        return mBatteryStatus;
    }


    @PascalMethod(description = "Returns the most recently received battery health data:" + "\n1 - unknown;"
            + "\n2 - good;" + "\n3 - overheat;" + "\n4 - dead;" + "\n5 - over voltage;"
            + "\n6 - unspecified failure;")
    public int batteryGetHealth() {
        return mBatteryHealth;
    }


    @PascalMethod(description = "Returns the most recently received plug operator data:" + "\n-1 - unknown"
            + "\n0 - unplugged;" + "\n1 - power source is an AC charger"
            + "\n2 - power source is a USB port")
    public int batteryGetPlugType() {
        return mPlugType;
    }


    @PascalMethod(description = "Returns the most recently received battery presence data.")
    public boolean batteryCheckPresent() {
        return mBatteryPresent;
    }


    @PascalMethod(description = "Returns the most recently received battery level (percentage).")

    public int batteryGetLevel() {
        if (mBatteryMaxLevel == -1 || mBatteryMaxLevel == 100 || mBatteryMaxLevel == 0) {
            return mBatteryLevel;
        } else {
            return (int) (mBatteryLevel * 100.0 / mBatteryMaxLevel);
        }
    }


    @PascalMethod(description = "Returns the most recently received battery voltage.")

    public int batteryGetVoltage() {
        return mBatteryVoltage;
    }


    @PascalMethod(description = "Returns the most recently received battery temperature.")

    public int batteryGetTemperature() {
        return mBatteryTemperature;
    }


    @PascalMethod(description = "Returns the most recently received battery technology data.")

    public StringBuilder batteryGetTechnology() {
        return new StringBuilder(mBatteryTechnology);
    }

    private class BatteryStateListener extends BroadcastReceiver {
        private BatteryStateListener() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryStatus = intent.getIntExtra("status", 1);
            mBatteryHealth = intent.getIntExtra("health", 1);
            mPlugType = intent.getIntExtra("plugged", -1);

            mBatteryPresent = intent.getBooleanExtra(getBatteryManagerFieldValue("EXTRA_PRESENT"), false);
            mBatteryLevel = intent.getIntExtra(getBatteryManagerFieldValue("EXTRA_LEVEL"), -1);
            mBatteryMaxLevel = intent.getIntExtra(getBatteryManagerFieldValue("EXTRA_SCALE"), 0);
            mBatteryVoltage = intent.getIntExtra(getBatteryManagerFieldValue("EXTRA_VOLTAGE"), -1);
            mBatteryTemperature = intent.getIntExtra(getBatteryManagerFieldValue("EXTRA_TEMPERATURE"), -1);
            mBatteryTechnology = intent.getStringExtra(getBatteryManagerFieldValue("EXTRA_TECHNOLOGY"));

            Bundle data = new Bundle();
            data.putInt("status", mBatteryStatus);
            data.putInt("health", mBatteryHealth);
            data.putInt("plugged", mPlugType);

            data.putBoolean("battery_present", mBatteryPresent);
            if (mBatteryMaxLevel == -1 || mBatteryMaxLevel == 100 || mBatteryMaxLevel == 0) {
                data.putInt("level", mBatteryLevel);
            } else {
                data.putInt("level", (int) (mBatteryLevel * 100.0 / mBatteryMaxLevel));
            }
            data.putInt("voltage", mBatteryVoltage);
            data.putInt("temperature", mBatteryTemperature);
            data.putString("technology", mBatteryTechnology);

            mBatteryData = data;
        }
    }

}
