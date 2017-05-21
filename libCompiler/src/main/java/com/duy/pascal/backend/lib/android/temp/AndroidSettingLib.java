/*
 * Copyright (C) 2009 Google Inc.
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

package com.duy.pascal.backend.lib.android.temp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

import com.duy.pascal.BasePascalApplication;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.android.activity.PascalActivityTask;
import com.duy.pascal.backend.lib.android.activity.PascalActivityTaskExecutor;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.RpcOptional;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Exposes phone settings functionality.
 *
 * @author Frank Spychalski (frank.spychalski@gmail.com)
 */
public class AndroidSettingLib implements PascalLibrary {
    public static final String NAME = "aSetting".toLowerCase();
    private static final int AIRPLANE_MODE_OFF = 0;
    private static final int AIRPLANE_MODE_ON = 1;

    private Context mContext;
    private AudioManager mAudio;
    private PowerManager mPower;

    public AndroidSettingLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        if (mContext != null) {
            mAudio = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            mPower = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the screen timeout to this number of seconds.", returns = "The original screen timeout.")
    public Integer setScreenTimeout(@PascalParameter(name = "value") Integer value) {
        Integer oldValue = getScreenTimeout();
        android.provider.Settings.System.putInt(mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT, value * 1000);
        return oldValue;
    }

    @PascalMethod(description = "Returns the current screen timeout in seconds.", returns = "the current screen timeout in seconds.")
    public Integer getScreenTimeout() {
        try {
            return android.provider.Settings.System.getInt(mContext.getContentResolver(),
                    android.provider.Settings.System.SCREEN_OFF_TIMEOUT) / 1000;
        } catch (SettingNotFoundException e) {
            return 0;
        }
    }

    @PascalMethod(description = "Checks the airplane mode setting.", returns = "True if airplane mode is enabled.")
    public Boolean checkAirplaneMode() {
        try {
            return android.provider.Settings.System.getInt(mContext.getContentResolver(),
                    android.provider.Settings.System.AIRPLANE_MODE_ON) == AIRPLANE_MODE_ON;
        } catch (SettingNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Toggles airplane mode on and off.", returns = "True if airplane mode is enabled.")
    public Boolean toggleAirplaneMode(@PascalParameter(name = "enabled") @RpcOptional Boolean enabled) {
        if (enabled == null) {
            enabled = !checkAirplaneMode();
        }
        android.provider.Settings.System.putInt(mContext.getContentResolver(),
                android.provider.Settings.System.AIRPLANE_MODE_ON, enabled ? AIRPLANE_MODE_ON
                        : AIRPLANE_MODE_OFF);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabled);
        mContext.sendBroadcast(intent);
        return enabled;
    }

    @PascalMethod(description = "Checks the ringer silent mode setting.", returns = "True if ringer silent mode is enabled.")
    public Boolean checkRingerSilentMode() {
        return mAudio.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Toggles ringer silent mode on and off.", returns = "True if ringer silent mode is enabled.")
    public Boolean toggleRingerSilentMode(@PascalParameter(name = "enabled") @RpcOptional Boolean enabled) {
        if (enabled == null) {
            enabled = !checkRingerSilentMode();
        }
        mAudio.setRingerMode(enabled ? AudioManager.RINGER_MODE_SILENT
                : AudioManager.RINGER_MODE_NORMAL);
        return enabled;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Toggles vibrate mode on and off. If ringer=true then set Ringer setting, else set Notification setting", returns = "True if vibrate mode is enabled.")
    public Boolean toggleVibrateMode(@PascalParameter(name = "enabled") @RpcOptional Boolean enabled,
                                     @PascalParameter(name = "ringer") @RpcOptional Boolean ringer) {
        int atype = ringer ? AudioManager.VIBRATE_TYPE_RINGER : AudioManager.VIBRATE_TYPE_NOTIFICATION;
        int asetting = enabled ? AudioManager.VIBRATE_SETTING_ON : AudioManager.VIBRATE_SETTING_OFF;
        mAudio.setVibrateSetting(atype, asetting);
        return enabled;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Checks Vibration setting. If ringer=true then query Ringer setting, else query Notification setting", returns = "True if vibrate mode is enabled.")
    public Boolean getVibrateMode(@PascalParameter(name = "ringer") @RpcOptional Boolean ringer) {
        int atype = ringer ? AudioManager.VIBRATE_TYPE_RINGER : AudioManager.VIBRATE_TYPE_NOTIFICATION;
        return mAudio.shouldVibrate(atype);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the maximum ringer volume.")
    public int getMaxRingerVolume() {
        return mAudio.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current ringer volume.")
    public int getRingerVolume() {
        return mAudio.getStreamVolume(AudioManager.STREAM_RING);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the ringer volume.")
    public void setRingerVolume(@PascalParameter(name = "volume") Integer volume) {
        mAudio.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the maximum media volume.")
    public int getMaxMediaVolume() {
        return mAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the current media volume.")
    public int getMediaVolume() {
        return mAudio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the media volume.")
    public void setMediaVolume(@PascalParameter(name = "volume") Integer volume) {
        mAudio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    @PascalMethod(description = "Returns the screen backlight brightness.", returns = "the current screen brightness between 0 and 255")
    public Integer getScreenBrightness() {
        try {
            return android.provider.Settings.System.getInt(mContext.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the the screen backlight brightness.", returns = "the original screen brightness.")
    public Integer setScreenBrightness(
            @PascalParameter(name = "value", description = "brightness value between 0 and 255") Integer value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        final int brightness = value;
        Integer oldValue = getScreenBrightness();
        android.provider.Settings.System.putInt(mContext.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);

        PascalActivityTask<Object> task = new PascalActivityTask<Object>() {
            @Override
            public void onCreate() {
                super.onCreate();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.screenBrightness = brightness * 1.0f / 255;
                getActivity().getWindow().setAttributes(lp);
                setResult(null);
                finish();
            }
        };

        PascalActivityTaskExecutor taskExecutor =
                ((BasePascalApplication) mContext).getTaskExecutor();
        taskExecutor.execute(task);

        return oldValue;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Checks if the screen is on or off (requires API level 7).", returns = "True if the screen is currently on.")
    public Boolean checkScreenOn() throws Exception {
        Class<?> powerManagerClass = mPower.getClass();
        Boolean result;
        try {
            Method isScreenOn = powerManagerClass.getMethod("isScreenOn");
            result = (Boolean) isScreenOn.invoke(mPower);
        } catch (Exception e) {
            Log.e(e);
            throw new UnsupportedOperationException("This feature is only available after Eclair.");
        }
        return result;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {
        // Nothing to do yet.
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
}
