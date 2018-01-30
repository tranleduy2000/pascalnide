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

package com.duy.pascal.ui.debug.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.config.DebugMode;
import com.duy.pascal.interperter.debugable.IDebugListener;
import com.duy.pascal.interperter.declaration.lang.function.AbstractCallableFunction;
import com.duy.pascal.interperter.libraries.io.IOLib;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.CompileManager;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.common.utils.IOUtils;
import com.duy.pascal.ui.debug.CallStack;
import com.duy.pascal.ui.debug.fragments.FragmentFrame;
import com.duy.pascal.ui.dialog.DialogHelper;
import com.duy.pascal.ui.editor.view.HighlightEditor;
import com.duy.pascal.ui.editor.view.LineUtils;
import com.duy.pascal.ui.runnable.AbstractExecActivity;
import com.duy.pascal.ui.runnable.IProgramHandler;
import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.view.LockableScrollView;
import com.duy.pascal.ui.view.console.ConsoleView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class DebugActivity extends AbstractExecActivity implements IDebugListener, IProgramHandler {

    private final Handler mHandler = new Handler();
    private ConsoleView mConsoleView;
    private HighlightEditor mCodeView;
    private Toolbar toolbar;
    private LockableScrollView mScrollView;
    private AlertDialog mAlertDialog;
    private PopupWindow mPopupWindow;
    private AtomicBoolean mEnded = new AtomicBoolean(false);
    private Vibrator mVibrator;
    private Runnable showDialog = new Runnable() {
        @Override
        public void run() {

        }
    };

    private FragmentFrame mFameFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_debug);
        bindView();

        FirebaseAnalytics.getInstance(this).logEvent("open_debug", new Bundle());
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mConsoleView.updateSize();
        mConsoleView.showPrompt();
        mConsoleView.writeString("Enable DEBUG mode\n");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                debugProgram();
            }
        }, 100);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mConsoleView = findViewById(R.id.console_view);
        mCodeView = findViewById(R.id.code_editor);
        mScrollView = findViewById(R.id.vertical_scroll);
        mCodeView.setVerticalScroll(mScrollView);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mFameFragment = (FragmentFrame) getSupportFragmentManager().findFragmentByTag("FragmentFrame");
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onError(Throwable e) {
        if (isFinishing()) return;

        ExceptionManager exceptionManager = new ExceptionManager(this);
        DialogHelper.createFinishDialog(this, R.string.runtime_error, exceptionManager.getMessage(e)).show();
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
        mConsoleView.requestFocus();
        if (imm != null) {
            imm.showSoftInput(mConsoleView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void debugProgram() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(CompileManager.EXTRA_FILE) != null) {
            mFilePath = ((File) extras.getSerializable(CompileManager.EXTRA_FILE)).getPath();
            if (mFilePath.isEmpty()) return;
            File file = new File(mFilePath);
            if (!file.exists()) {
                finish();
                return;
            }
            String code = null;
            try {
                code = IOUtils.toString(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
                finish();
                return;
            }
            mCodeView.setTextHighlighted(code);
            mCodeView.highlightAll();

            setTitle(file.getName());
            mEnded.set(false);
            setEnableDebug(true); //disable DEBUG
            createAndRunProgram(mFilePath); //execute file
        } else {
            finish();
        }
    }

    @Override
    public void onLine(Node node, @Nullable final LineNumber lineNumber) {
        DLog.d(TAG, "onLine() called with: runtimeValue = [" + node + "], line = [" + lineNumber + "]");
        if (lineNumber == null) {
            return;
        }
        scrollTo(lineNumber);
    }

    private void scrollTo(@NonNull final LineNumber lineNumber) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCodeView.pinLine(lineNumber);
                int yCoordinate = LineUtils.getYAtLine(mScrollView,
                        mCodeView.getLineCount(), lineNumber.getLine());
                int heightVisible = mCodeView.getHeightVisible();
                if (!(mScrollView.getScrollY() < yCoordinate
                        && mScrollView.getScrollY() + heightVisible > yCoordinate)) {
                    mScrollView.smoothScrollTo(0, yCoordinate);
                }
            }
        });
    }

    @Override
    public void onLine(RuntimeValue executable, final LineNumber lineNumber) {
        DLog.d(TAG, "onLine() called with: executable = [" + executable.getClass() +
                "], line = [" + lineNumber + "]");
        if (lineNumber == null) return;
        scrollTo(lineNumber);
    }

    @Override
    public void onEvaluatingExpr(LineNumber lineNumber, String expression) {
        DLog.d(TAG, "onEvaluatingExpr() called with: line = [" + lineNumber + "], " +
                "expression = [" + expression + "]");

    }

    /**
     * This method will be show a small popup window for show result of expression
     *
     * @param lineNumber - the line of expression
     * @param expr     - input
     * @param result   - result value of expr
     */
    @Override
    public void onEvaluatedExpr(final LineNumber lineNumber, final String expr, final String result) {
        DLog.d(TAG, "onEvaluatedExpr() called with: line = [" + lineNumber + "], expr = [" +
                expr + "], result = [" + result + "]");
        showPopupAt(lineNumber, expr + " = " + result);
    }

    @WorkerThread
    private void showPopupAt(final LineNumber lineNumber, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return;
                //get relative position of expression at edittext
                Point position = mCodeView.getDebugPosition(lineNumber.getLine(), lineNumber.getColumn(),
                        Gravity.TOP);
                DLog.d(TAG, "generate: " + position);
                dismissPopup();
                //create new popup
                PopupWindow window = new PopupWindow(DebugActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = inflater.inflate(R.layout.popup_expr_result, null);
                container.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                int windowHeight = container.getMeasuredHeight();
                int windowWidth = container.getMeasuredWidth();

                window.setContentView(container);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setTouchable(true);
                window.setSplitTouchEnabled(true);
                window.setOutsideTouchable(true);

                window.showAtLocation(mCodeView, Gravity.NO_GRAVITY, position.x - windowWidth / 3,
                        position.y + toolbar.getHeight() - windowHeight);

                TextView txtResult = container.findViewById(R.id.txt_result);
                txtResult.setText(msg);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setRepeatMode(Animation.REVERSE);
                alphaAnimation.setRepeatCount(Animation.INFINITE);
                txtResult.startAnimation(alphaAnimation);
                DebugActivity.this.mPopupWindow = window;
            }
        });
    }

    private void dismissPopup() {
        if (mPopupWindow != null) {
            if (this.mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    }

    @Override
    public void onAssignValue(LineNumber lineNumber, final AssignableValue left,
                              @NonNull final Object oldValue, final Object newValue,
                              @NonNull VariableContext context) {
        DLog.d(TAG, "onAssignValue() called with: lineNumber = [" + lineNumber + "], left = [" +
                left + "], value = [" + newValue + "]");
    }

    @Override
    public void onPreFunctionCall(AbstractCallableFunction function, RuntimeValue[] arguments) {
        DLog.d(TAG, "onPreFunctionCall() called with: function = [" + function + "], arguments = ["
                + Arrays.toString(arguments) + "]");

    }

    @Override
    public void onFunctionCalled(AbstractCallableFunction function, RuntimeValue[] arguments, Object result) {
        DLog.d(TAG, "onFunctionCalled() called with: function = [" + function + "], arguments = ["
                + Arrays.toString(arguments) + "], result = [" + result + "]");

    }

    @Override
    public void onEvalParameterFunction(LineNumber lineNumber, String name, @Nullable Object value) {
        if (value != null) {
            showPopupAt(lineNumber, name + " = " + value.toString());
        }
    }

    @Override
    public void onFinish() {
        dismissPopup();
        this.mEnded.set(true);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCodeView.pinLine(null);
            }
        });
    }

    @Override
    public void showMessage(LineNumber pos, String msg) {
        showPopupAt(pos, msg);
    }

    @Override
    public void onValueVariableChanged(final CallStack currentFrame) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mFameFragment.update(currentFrame);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_step_info) {
            if (mProgram != null) {
                mProgram.setDebugMode(DebugMode.STEP_INFO);
            }
            resumeProgram();
            return true;
        } else if (i == R.id.action_step_over) {
            if (mProgram != null) {
                mProgram.setDebugMode(DebugMode.STEP_OVER);
            }
            resumeProgram();
            return true;
        } else if (i == R.id.action_add_watch) {
            addWatchVariable();
            return true;

        } else if (i == R.id.action_show_soft) {
            showKeyBoard();
            return true;

        } else if (i == R.id.action_rerun) {
            CompileManager.debug(this, mFilePath);
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void resumeProgram() {
        if (mProgram != null && !mEnded.get()) mProgram.resume();
        else {
            mVibrator.vibrate(100);
            Toast.makeText(this, R.string.program_stopped, Toast.LENGTH_SHORT).show();
        }
    }

    private void addWatchVariable() {
    }


    @Override
    protected void onDestroy() {
        if (mAlertDialog != null) mAlertDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public ConsoleView getConsoleView() {
        return mConsoleView;
    }

    @Override
    public void onNewMessage(String msg) {
        DLog.d(TAG, "onNewMessage() called with: msg = [" + msg + "]");

    }

    @Override
    public synchronized void startInput(final IOLib lock) {
        this.mLock = lock;
        showDialogInput();
    }

    private void showDialogInput() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(DebugActivity.this);
                builder.setView(R.layout.dialog_input);
                mAlertDialog = builder.create();
                mAlertDialog = builder.create();
                mAlertDialog.setCanceledOnTouchOutside(false);
                if (!isFinishing()) {
                    mAlertDialog.show();
                    final EditText editText = mAlertDialog.findViewById(R.id.edit_input);
                    ((TextInputLayout) mAlertDialog.findViewById(R.id.hint)).setHint(getString(R.string.enter_data));
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mLock instanceof IOLib) {
                                ((IOLib) mLock).setInputBuffer(editText.getText().toString());
                            }
                            mConsoleView.writeString(editText.getText().toString());
                            mAlertDialog.cancel();
                        }
                    };
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(onClickListener);
                    mAlertDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
