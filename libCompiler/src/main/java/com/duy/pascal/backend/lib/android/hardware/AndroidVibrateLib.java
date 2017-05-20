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

package com.duy.pascal.backend.lib.android.hardware;

import android.content.Context;
import android.os.Vibrator;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.rpc.RpcDefault;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import java.util.Map;

/**
 * Created by Duy on 25-Apr-17.
 */

public class AndroidVibrateLib implements PascalLibrary {
    public static final String NAME = "aVibrate".toLowerCase();
    private Context mContext;
    private Vibrator mVibrator;

    public AndroidVibrateLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        if (mContext != null) {
            mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Vibrates the phone or a specified duration in milliseconds.")
    public void vibrate(
            @PascalParameter(name = "duration", description = "duration in milliseconds")
            @RpcDefault("300") int duration) {
        mVibrator.vibrate(duration);
    }

    @PascalMethod(description = "Turn the vibrator off.")
    @SuppressWarnings("unused")
    public void cancelVibrate() {
        if (mVibrator.hasVibrator()) {
            mVibrator.cancel();
        }
    }

    @PascalMethod(description = " Check whether the hardware has a vibrator.")
    @SuppressWarnings("unused")
    public boolean isVibrating() {
        return mVibrator.hasVibrator();
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {
        cancelVibrate();
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }
}
