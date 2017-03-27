package com.duy.pascal.frontend;

import android.util.Log;

/**
 * Created by Duy on 27-Mar-17.
 */

public class DLog {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = DLog.class.getSimpleName();

    public static void d(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
