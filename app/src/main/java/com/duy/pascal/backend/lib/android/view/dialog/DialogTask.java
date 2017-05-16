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

package com.duy.pascal.backend.lib.android.view.dialog;

import android.app.Dialog;

import com.duy.pascal.backend.lib.android.activity.PascalActivityTask;
import com.googlecode.sl4a.facade.AndroidEvent;

import java.util.concurrent.CountDownLatch;

public abstract class DialogTask extends PascalActivityTask<Object> {

    protected final CountDownLatch mShowLatch = new CountDownLatch(1);
    protected Dialog mDialog;
    private AndroidEvent mEventFacade;

    public AndroidEvent getEventFacade() {
        return mEventFacade;
    }

    public void setEventFacade(AndroidEvent mEventFacade) {
        this.mEventFacade = mEventFacade;
    }

    @Override
    protected void setResult(Object object) {
        super.setResult(object);
        AndroidEvent eventFacade = getEventFacade();
        if (eventFacade != null) {
            eventFacade.postEvent("dialog", object);
        }
    }

    /**
     * Returns the wrapped {@link Dialog}.
     */
    public Dialog getDialog() {
        return mDialog;
    }


    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            finish();
        }
        mDialog = null;
    }

    /**
     * Returns the {@link CountDownLatch} that is counted down when the dialog is shown.
     */
    public CountDownLatch getShowLatch() {
        return mShowLatch;
    }
}
