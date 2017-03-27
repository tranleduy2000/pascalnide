/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package terminal.android.duy.com.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import terminal.android.duy.com.terminal_view.TermSession;

public class TermService implements SharedPreferences.OnSharedPreferenceChangeListener {
    /* Parallels the value of START_STICKY on API Level >= 5 */
    private static final int COMPAT_START_STICKY = 1;

    private static final int RUNNING_NOTIFICATION = 1;
    /*
     * Key logger HACK to get the keycodes..
     */
    private static hardkeymappings mHardKeys = new hardkeymappings();
    private static boolean mLogKeys = false;
    //The Global Key logs File..
    private static File mKeyLogFile = null;
    private static FileOutputStream mKeyLogFileOutputStream = null;
    private static PrintWriter mKeyLogger = null;
    boolean mBacktoESC = false;
    private ArrayList<TermSession> mTermSessions;
    private SharedPreferences mPrefs;
    private TermSettings mSettings;
    private boolean mSessionInit;
    private PowerManager.WakeLock mScreenLock;
    private PowerManager.WakeLock mWakeLock;
    private WifiManager.WifiLock mWifiLock;
    private Context context;

    public TermService(Context context) {
        onCreate(context);
        this.context = context;
    }

    //A link to the key logger
    public static void keyLoggerKey(int zKeyCode) {
        if (mLogKeys && mKeyLogger != null) {
            Log.v("Terminal IDE KEY_LOGGER", "Key Logged : " + zKeyCode);
            mKeyLogger.println("Key Logged : " + zKeyCode);
            mKeyLogger.flush();
        }
    }

    public static boolean isHardKeyEnabled() {
        return mHardKeys.isEnabled();
    }

    public static int isSpecialKeyCode(int zKeyCode) {
        return mHardKeys.checkKeyCode(zKeyCode);
    }

    public static void resetAllKeyCodes() {
        //Set them all to -1
        mHardKeys.resetAllMappings();
    }

    private void closeKeyLog() {
        try {
            if (mKeyLogger != null) {
                mKeyLogger.close();
            }
            if (mKeyLogFileOutputStream != null) {
                mKeyLogFileOutputStream.close();
            }
        } catch (IOException iOException) {
        }

        mKeyLogger = null;
        mKeyLogFileOutputStream = null;

        mLogKeys = false;
    }

    //Check for shared pref change
    public void onSharedPreferenceChanged(SharedPreferences zPrefs, String zKey) {
        if (zKey.contains("lock")) {

        } else if (zKey.contains("hardmap_")) {
            //Check the key logger..
            Log.i("TermService", "Update Key Maps");
            mHardKeys.setKeyMappings(zPrefs);
        }
    }

    public void onCreate(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mPrefs.registerOnSharedPreferenceChangeListener(this);
        mHardKeys.setKeyMappings(mPrefs);

        //Setup the Hard Key Mappings..
        mSettings = new TermSettings(mPrefs);

        //Need to set the HOME Folder and Bash startup..
        //Sometime getfilesdir return NULL ?
        mSessionInit = false;
    }

    private int getStringPref(String zKey, String zDefault) {
        int ival = 0;
        try {
            String value = mPrefs.getString(zKey, zDefault);
            ival = Integer.parseInt(value);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
        return ival;
    }

    //Is BACK ESC
    public boolean isBackESC() {
        return mBacktoESC;
    }

    public void setBackToESC(boolean zBackToEsc) {
        mBacktoESC = zBackToEsc;
    }

    //Toggle Key Logger
    public boolean isKeyLoggerOn() {
        return mLogKeys;
    }

    public class TSBinder extends Binder {
        TermService getService() {
            Log.i("TermService", "Activity binding to service");
            return TermService.this;
        }
    }

}
