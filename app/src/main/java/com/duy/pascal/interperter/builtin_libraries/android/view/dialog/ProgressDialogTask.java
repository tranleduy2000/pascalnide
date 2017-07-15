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

package com.duy.pascal.interperter.builtin_libraries.android.view.dialog;

import android.app.ProgressDialog;

/**
 * Wrapper class for progress dialog running in separate thread
 *
 * @author MeanEYE.rcf (meaneye.rcf@gmail.com)
 */
public class ProgressDialogTask extends DialogTask {

    private final int mStyle;
    private final int mMax;
    private final CharSequence mTitle;
    private final CharSequence mMessage;
    private final Boolean mCancelable;

    public ProgressDialogTask(int style, int max, CharSequence title,
                              CharSequence message, boolean cancelable) {
        mStyle = style;
        mMax = max;
        mTitle = title;
        mMessage = message;
        mCancelable = cancelable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDialog = new ProgressDialog(getActivity());
        ((ProgressDialog) mDialog).setProgressStyle(mStyle);
        ((ProgressDialog) mDialog).setMax(mMax);
        mDialog.setCancelable(mCancelable);
        mDialog.setTitle(mTitle);
        ((ProgressDialog) mDialog).setMessage(mMessage);
        mDialog.show();
        mShowLatch.countDown();
    }
}
