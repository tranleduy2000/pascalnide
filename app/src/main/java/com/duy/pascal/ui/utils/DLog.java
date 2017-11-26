/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.ui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.ui.BuildConfig;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Duy on 27-Mar-17.
 */
public class DLog {
    private static final String TAG = "DLog";
    public static boolean DEBUG = BuildConfig.DEBUG;
    public static boolean ANDROID = true;


    public static void d(String TAG, Object msg) {
        if (DEBUG) {
            if (ANDROID) {
                Log.d(TAG, msg.toString());
            } else {
                System.out.println(TAG + ": " + msg.toString());
            }
        }
    }

    public static void w(Object msg) {
        if (DEBUG) {
            if (ANDROID) {
                Log.w(TAG, msg.toString());
            } else {
                System.out.println(TAG + ": " + msg.toString());
            }
        }
    }

    public static void w(String TAG, Object msg) {
        if (DEBUG) {
            if (ANDROID) {
                Log.w(TAG, msg.toString());
            } else {
                System.out.println(TAG + ": " + msg.toString());
            }
        }
    }

    public static void e(Exception e) {
        if (DEBUG) {
            if (ANDROID) {
                e.printStackTrace();
            } else {
                e.printStackTrace(System.err);
            }
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            if (ANDROID) {
                Log.e(TAG, message);
            } else {
                System.err.println(message);
            }
        }
    }

    public static void e(String TAG, Exception exception) {
        if (DEBUG) {
            if (ANDROID) {
                Log.e(TAG, "Error ", exception);
            } else {
                exception.printStackTrace(System.err);
            }
        }
    }

    public static void e(String TAG, String exception) {
        if (DEBUG) {
            if (ANDROID) {
                Log.e(TAG, exception);
            } else {
                System.err.println(TAG + ": " + exception);
            }
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            if (ANDROID) {
                Log.e(TAG, msg, e);
            } else {
                e.printStackTrace(System.err);
            }
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            if (ANDROID) {
                Log.i(TAG, msg.toString());
            } else {
                System.out.println(TAG + ": " + msg.toString());
            }
        }
    }

    public static void reportException(Throwable e) {
        if (ANDROID) {
            FirebaseCrash.report(e);
        }
    }


    private static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void v(String message) {
        Log.v(TAG, message);
    }

    public static void v(String TAG, String message) {
        Log.v(TAG, message);
    }

    public static void v(String message, Throwable e) {
        Log.v(TAG, message, e);
    }

    public static void v(Context context, String message) {
        toast(context, message);
        Log.v(TAG, message);
    }

    public static void v(Context context, String message, Throwable e) {
        toast(context, message);
        Log.v(TAG, message, e);
    }

    public static void e(Throwable e) {
        Log.e(TAG, "Error", e);
    }


    public static void e(String message, Throwable e) {
        Log.e(TAG, message, e);
    }

    public static void e(Context context, String message) {
        toast(context, message);
        Log.e(TAG, message);
    }

    public static void e(Context context, String message, Throwable e) {
        toast(context, message);
        Log.e(TAG, message, e);
    }

    public static void w(Throwable e) {
        Log.w(TAG, "Warning", e);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void w(String message, Throwable e) {
        Log.w(TAG, message, e);
    }

    public static void w(Context context, String message) {
        toast(context, message);
        Log.w(TAG, message);
    }

    public static void w(Context context, String message, Throwable e) {
        toast(context, message);
        Log.w(TAG, message, e);
    }

    public static void d(String msg) {
        if (DEBUG) {
            if (ANDROID) {
                Log.d(TAG, msg);
            } else {
                System.out.println(TAG + ": " + msg);
            }
        }
    }

    public static void d(String message, Throwable e) {
        Log.d(TAG, message, e);
    }

    public static void d(Context context, String message) {
        toast(context, message);
        Log.d(TAG, message);
    }

    public static void d(Context context, String message, Throwable e) {
        toast(context, message);
        Log.d(TAG, message, e);
    }


    public static void i(String message, Throwable e) {
        Log.i(TAG, message, e);
    }

    public static void i(Context context, String message) {
        toast(context, message);
        Log.i(TAG, message);
    }

    public static void i(Context context, String message, Throwable e) {
        toast(context, message);
        Log.i(TAG, message, e);
    }
}
