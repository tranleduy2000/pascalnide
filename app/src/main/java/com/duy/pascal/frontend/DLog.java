package com.duy.pascal.frontend;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Duy on 27-Mar-17.
 */

public class DLog {
    public static final String TAG = DLog.class.getSimpleName();
    public static final boolean DEBUG = true;

    /**
     * Set <code>true</code> if enable debug program
     */
    public static boolean DEBUG_PROGRAM = false;

    public static void d(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG) Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG) Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (DEBUG) Log.i(TAG, msg);
    }

    public static void reportException(Throwable e){
        FirebaseCrash.report(e);
    }

}
