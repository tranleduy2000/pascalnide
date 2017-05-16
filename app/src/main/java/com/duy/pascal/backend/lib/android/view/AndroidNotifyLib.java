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

package com.duy.pascal.backend.lib.android.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.duy.pascal.frontend.R;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;

import java.util.Map;

/**
 * Created by Duy on 25-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class AndroidNotifyLib implements PascalLibrary {
    public static final String NAME = "aNotify".toLowerCase();

    private final Context mContext;

    public AndroidNotifyLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {

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

    @SuppressWarnings("unused")
    @PascalMethod(description = "Displays a notification that will be canceled when the user clicks on it.")
    public void notify(@PascalParameter(name = "title", description = "title") String title,
                       @PascalParameter(name = "message") String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);

        int id = 1;
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification build = mBuilder.build();
        notificationManager.notify(id, build);
    }

}
