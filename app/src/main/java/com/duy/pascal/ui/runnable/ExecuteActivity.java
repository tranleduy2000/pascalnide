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

package com.duy.pascal.ui.runnable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.CompileManager;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.dialog.DialogHelper;
import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleView;

import java.io.File;


public class ExecuteActivity extends AbstractExecActivity {
    public ConsoleView mConsoleView;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mConsoleView = (ConsoleView) findViewById(R.id.console);
        setupToolbar();

        getConsoleView().updateSize();
        getConsoleView().showPrompt();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (savedInstanceState == null) {
                mFilePath = ((File) extras.getSerializable(CompileManager.FILE)).getPath();
                if (mFilePath.isEmpty()) {
                    return;
                }
                File file = new File(mFilePath);
                if (!file.exists()) {
                    finish();
                    return;
                }

                //set title in in toolbar
                setTitle(file.getName());

                //disable debug mode
                setEnableDebug(false);

                //execute file
                createAndRunProgram(mFilePath);
            } else {

            }
        } else {
            finish();
        }
    }

    @Override
    public void debugProgram() {
    }


    protected void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_console, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_show_soft:
                showKeyBoard();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_change_keyboard:
                changeKeyBoard();
                break;
            case R.id.action_step_info:
                mProgram.resume();
                break;
            case R.id.action_rerun:
                CompileManager.execute(this, mFilePath);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * show dialog pick keyboard
     */
    private void changeKeyBoard() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showInputMethodPicker();
        }
    }

    /**
     * show soft keyboard
     */
    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getConsoleView(), 0);
    }

    public void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void showDialogComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.complete)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mMessageHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 100);
                    }
                })
                .setNegativeButton(R.string.view_console, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        try {
            builder.create().show();
        } catch (Exception ignored) {
        }
    }

    /**
     * show error compile or runtime exception
     */
    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExceptionManager exceptionManager = new ExceptionManager(ExecuteActivity.this);
                Dialog dialog = DialogHelper.createFinishDialog(ExecuteActivity.this,
                        getString(R.string.runtime_error),
                        exceptionManager.getMessage(e));
                showDialog(dialog);
                DLog.e(TAG, "onError: ", e);
            }
        });
    }


    @Override
    public ConsoleView getConsoleView() {
        return mConsoleView;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}

