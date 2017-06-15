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

package com.duy.pascal.backend.builtin_libraries.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

/**
 * Visualizer activity
 */
public abstract class PascalActivityTask<T> {
    private final PascalFutureResult<T> mResult = new PascalFutureResult<>();
    private Activity mActivity;

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @CallSuper
    public void onCreate() {
    }

    @CallSuper
    public void onStart() {
    }

    @CallSuper
    public void onResume() {
    }

    @CallSuper
    public void onPause() {
    }

    @CallSuper
    public void onStop() {
    }

    @CallSuper
    public void onDestroy() {
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    protected boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @CallSuper
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public T getResult() throws InterruptedException {
        return mResult.get();
    }

    protected void setResult(T result) {
        mResult.set(result);
    }

    @CallSuper
    public void finish() {
        mActivity.finish();
    }

    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mActivity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}