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

package com.duy.pascal.frontend.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.code_editor.editor_view.HighlightEditor;
import com.duy.pascal.frontend.code_editor.editor_view.LineUtils;
import com.duy.pascal.frontend.debug.VariableItem;
import com.duy.pascal.frontend.debug.VariableWatcherAdapter;
import com.duy.pascal.frontend.debug.VariableWatcherView;
import com.duy.pascal.frontend.dialog.DialogManager;
import com.duy.pascal.frontend.view.LockableScrollView;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;

import java.io.File;

//import butterknife.BindView;


public class DebugActivity extends AbstractExecActivity {

    //    @BindView(R.id.console)
    ConsoleView mConsoleView;
    //    @BindView(R.id.code_editor)
    HighlightEditor mCodeView;
    //    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.vertical_scroll)
    LockableScrollView mScrollView;
    //    @BindView(R.id.watcher)
    VariableWatcherView variableWatcherView;
    //    @BindView(R.id.empty_view)
    View emptyView;
    private Handler handler = new Handler();
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_debug);
//        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mConsoleView = (ConsoleView) findViewById(R.id.console);
        mCodeView = (HighlightEditor) findViewById(R.id.code_editor);
        mScrollView = (LockableScrollView) findViewById(R.id.vertical_scroll);
        variableWatcherView = (VariableWatcherView) findViewById(R.id.watcher);
        emptyView = findViewById(R.id.empty_view);

        setSupportActionBar(toolbar);

        variableWatcherView.setEmptyView(emptyView);
        getConsoleView().updateSize();
        getConsoleView().showPrompt();
        getConsoleView().writeString("enable DEBUG mode");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                debugProgram();
            }
        }, 100);
    }

    @Override
    public void onError(Exception e) {
        ExceptionManager exceptionManager = new ExceptionManager(this);
        DialogManager.Companion.createFinishDialog(this, "Runtime error", exceptionManager.getMessage(e)).show();
        //DEBUG
        if (DEBUG) e.printStackTrace();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getConsoleView(), 0);
    }

    @Override
    public void debugProgram() {
        Log.d(TAG, "debugProgram: ");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filePath = extras.getString(CompileManager.FILE_PATH);
            if (filePath == null || filePath.isEmpty()) return;
            File file = new File(filePath);
            if (!file.exists()) {
                finish();
                return;
            }
            String code = mFileManager.fileToString(file);
            mCodeView.setTextHighlighted(code);

            setTitle(file.getName());
            setEnableDebug(false); //disable DEBUG
            createAndRunProgram(filePath); //execute file
        } else {
            finish();
            return;
        }

        setEnableDebug(true);
        createAndRunProgram(filePath);
    }

    @Override
    public void onLine(final LineInfo lineInfo) {
        super.onLine(lineInfo);
        if (lineInfo == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCodeView.pinLine(lineInfo);
                mScrollView.smoothScrollTo(0, LineUtils.getYAtLine(mScrollView,
                        mCodeView.getLineCount(), lineInfo.getLine()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_next_line) {
            if (program != null) program.resume();
            return true;
        } else if (i == R.id.action_add_watch) {
            addWatchVariable();

        } else if (i == R.id.action_show_soft) {
            showKeyBoard();

        } else if (i == R.id.action_rerun) {
            CompileManager.debug(this, filePath);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void addWatchVariable() {
        final AppCompatEditText edittext = new AppCompatEditText(this);
        edittext.setHint(R.string.var_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_watch)
                .setView(edittext)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = edittext.getText().toString();
                        if (!name.isEmpty()) {
                            variableWatcherView.addVariable(new VariableItem(name));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) alertDialog.dismiss();
    }

    @Override
    public ConsoleView getConsoleView() {
        return mConsoleView;
    }

    @Override
    public void onVariableChangeValue(final String name, final Object old, final Object value) {
        super.onVariableChangeValue(name, old, value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VariableWatcherAdapter adapter = (VariableWatcherAdapter) variableWatcherView.getAdapter();
                adapter.onVariableChangeValue(name, old, value);
            }
        });
    }


}
