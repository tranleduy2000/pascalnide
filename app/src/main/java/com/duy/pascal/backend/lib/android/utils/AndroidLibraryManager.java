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

package com.duy.pascal.backend.lib.android.utils;

import android.app.Activity;
import android.content.Context;

import com.duy.pascal.backend.lib.android.BaseAndroidLibrary;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.jsonrpc.RpcReceiverManager;
import com.googlecode.sl4a.rpc.RpcDeprecated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AndroidLibraryManager extends RpcReceiverManager {

    private  Context mContext;
    private int mSdkLevel;

    public AndroidLibraryManager(int sdkLevel, Activity context) {
        mSdkLevel = sdkLevel;
        if (context != null){
            mContext = context.getApplicationContext();
        }
    }

    public int getSdkLevel() {
        return mSdkLevel;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public Object invoke(Class<? extends BaseAndroidLibrary> clazz, Method method, Object[] args)
            throws Exception {
        try {
            if (method.isAnnotationPresent(RpcDeprecated.class)) {
                String replacedBy = method.getAnnotation(RpcDeprecated.class).value();
                String title = method.getName() + " is deprecated";
                Log.notify(mContext, title, title, String.format("Please use %s instead.", replacedBy));
            }
            return super.invoke(clazz, method, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                Log.notify(mContext, "RPC invoke failed...", mContext.getPackageName(), e.getCause()
                        .getMessage());
            }
            throw e;
        }
    }

    public AndroidUtilsLib.Resources getAndroidFacadeResources() {
        return new AndroidUtilsLib.Resources() {
            @Override
            public int getLogo48() {
                // TODO(Alexey): As an alternative, ask application for resource ids.
                String packageName = mContext.getPackageName();
                return mContext.getResources().getIdentifier("script_logo_48", "drawable", packageName);
            }
        };
    }
}
