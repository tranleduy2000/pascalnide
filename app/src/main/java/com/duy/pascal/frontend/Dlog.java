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

package com.duy.pascal.frontend;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Duy on 27-Mar-17.
 */

public class Dlog {
    public static final String TAG = Dlog.class.getSimpleName();
    public static final boolean DEBUG = true;
//    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(Object msg) {
        if (DEBUG) Log.d(TAG, msg.toString());
    }

    public static void w(Object msg) {
        if (DEBUG) Log.w(TAG, msg.toString());
    }

    public static void e(Object msg) {
        if (DEBUG) {
            Log.e(TAG, "ERROR: ", (Throwable) msg);
        }
    }

    public static void i(Object msg) {
        if (DEBUG) Log.i(TAG, msg.toString());
    }

    public static void reportException(Throwable e) {
        FirebaseCrash.report(e);
    }

}
