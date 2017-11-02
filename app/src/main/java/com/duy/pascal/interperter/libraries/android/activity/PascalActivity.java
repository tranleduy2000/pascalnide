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

package com.duy.pascal.interperter.libraries.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.duy.pascal.PascalApplication;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.BaseActivity;
import com.googlecode.sl4a.Constants;

public class PascalActivity extends BaseActivity {
    private PascalActivityTask<?> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exec);
        int id = getIntent().getIntExtra(Constants.EXTRA_TASK_ID, -1);
        if (id == -1) {
            throw new RuntimeException("FutureActivityTask ID is not specified.");
        }
        PascalActivityTaskExecutor taskQueue = ((PascalApplication) getApplication()).getTaskExecutor();
        mTask = taskQueue.popTask(id);
        if (mTask == null) {
            Toast.makeText(this, "Exception when start activity", Toast.LENGTH_SHORT).show();
        } else {
            mTask.setActivity(this);
            mTask.onCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTask != null) {
            mTask.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTask != null) {
            mTask.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTask != null) {
            mTask.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTask != null) {
            mTask.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.onDestroy();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (mTask != null) {
            mTask.onCreateContextMenu(menu, v, menuInfo);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mTask != null) {
            if (mTask.onPrepareOptionsMenu(menu)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTask != null) {
            mTask.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTask != null) {
            if (mTask.onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return false;
    }
}
