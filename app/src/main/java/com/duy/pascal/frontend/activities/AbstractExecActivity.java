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

package com.duy.pascal.frontend.activities;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.alogrithm.InputData;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.utils.StringCompare;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.ScriptTerminatedException;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.frontend.alogrithm.InputData.MAX_INPUT;

public abstract class AbstractExecActivity extends RunnableActivity {
    public static final boolean DEBUG = Dlog.DEBUG;
    protected static final String TAG = ExecuteActivity.class.getSimpleName();
    protected static final int COMPLETE = 4;
    protected static final int RUNTIME_ERROR = 5;
    protected static final int SHOW_KEYBOARD = 6;
    protected final AtomicBoolean mIsRunning = new AtomicBoolean(true);
    protected final Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!mIsRunning.get()) return;
            switch (msg.what) {
                case RUNTIME_ERROR:
                    if (!isFinishing()) {
                        onError((Exception) msg.obj);
                    }
                    break;
                case COMPLETE:
                    if (!isFinishing()) {
                        showDialogComplete();
                    }
                    break;
                case SHOW_KEYBOARD:
                    showKeyBoard();
                    break;
            }
        }
    };
    protected final AtomicBoolean isCanRead = new AtomicBoolean(false);
    protected String input = "";
    protected String filePath;
    protected Object mLock;
    protected final Runnable runnableInput = new Runnable() {
        @Override
        public void run() {
            int exitFlag;
            String str;
            InputData inputData = new InputData();
            inputData.first = 0;
            inputData.last = 0;
            exitFlag = 0;
            do {
                str = getConsoleView().readString();
                switch (str) {
                    case ConsoleView.THE_ENTER_KEY: // return
                    case "\n":
                        exitFlag = 1;
                        break;
                    case ConsoleView.THE_DELETE_COMMAND:
                        if (inputData.last > 0) {
                            inputData.last--;
                            getConsoleView().commitString(String.valueOf(str));
                        }
                        break;
                    default:
                        if ((StringCompare.greaterEqual(str, " ")) && (inputData.last < MAX_INPUT)) {
                            inputData.data[inputData.last++] = str;
                            getConsoleView().commitString(String.valueOf(str));
                        }
                        break;
                }
            } while (exitFlag == 0 && isCanRead.get());
            getConsoleView().commitString("\n"); //return new line
            input = inputData.toString();
            isCanRead.set(false);
            if (mLock != null) {
                if (mLock instanceof IOLib) {
                    ((IOLib) mLock).resume();
                }
            }
        }

    };
    /**
     * this object use store buffer key
     */
    protected char keyCodeBuffer;
    /**
     * set <code>true</code> if you want to debug program
     */
    protected boolean debugging = false;
    /**
     * set <code>true</code> if enable debug mode, program will be pause every line
     */
    protected boolean enableDebug = false;
    protected RuntimeExecutable program;
    protected String programFile;
    final Runnable runProgram = new Runnable() {
        @Override
        public void run() {
            try {
                //compile
                PascalCompiler pascalCompiler = new PascalCompiler(AbstractExecActivity.this);
                try {
                    PascalProgram pascalProgram = pascalCompiler.loadPascal(
                            new File(programFile).getName(),
                            new FileReader(programFile),
                            new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>(),
                            AbstractExecActivity.this);
                    program = pascalProgram.run();
                    if (isEnableDebug()) {
                        program.enableDebug();
                    }
                    program.run();
                    mMessageHandler.sendEmptyMessage(COMPLETE);
                } catch (ScriptTerminatedException e) {
                    mMessageHandler.sendEmptyMessage(COMPLETE);
                } catch (RuntimePascalException | ParsingException e) {
                    mMessageHandler.sendMessage(mMessageHandler.obtainMessage(RUNTIME_ERROR, e));
                }
            } catch (final Exception e) {
                mMessageHandler.sendMessage(mMessageHandler.obtainMessage(RUNTIME_ERROR, e));
            }
        }
    };
    protected ApplicationFileManager mFileManager;

    public boolean isDebugging() {
        return debugging;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    protected abstract void onError(Exception obj);

    protected abstract void showDialogComplete();

    protected abstract void showKeyBoard();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileManager = new ApplicationFileManager(this);
    }


    /**
     * set background console
     *
     * @param color
     */
    public void setTextBackground(final int color) {
        getConsoleView().setConsoleTextBackground(color);
    }


    public String getInput() {
        return input;
    }

    @Override
    public void print(final CharSequence charSequence) {
        getConsoleView().commitString(charSequence.toString());
    }

    @Override
    public void println(final CharSequence charSequence) {
        getConsoleView().commitString(charSequence.toString());
        getConsoleView().commitString("\n");
    }

    @Override
    public char getKeyBuffer() {
        keyCodeBuffer = getConsoleView().readKey();
        return keyCodeBuffer;
    }

    @Override
    public boolean keyPressed() {
        return getConsoleView().isKeyPressed();
    }

    @Override
    public void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration) {
    }

    @Override
    public void onLocalVariableChangeValue(VariableDeclaration variableDeclaration) {
    }

    @Override
    protected void onDestroy() {
        //stop readkey, keypressed event
        getConsoleView().commitChar("s", false);
        stopInput();
        //stop program
        stopProgram();
        getConsoleView().onStop();

        super.onDestroy();
    }

    @Override
    public void onFunctionCall(final FunctionDeclaration functionDeclaration) {
        mMessageHandler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addVariable(new DebugItem(DebugItem.TYPE_MSG, ">_ " + "Call procedure \'"
//                        + functionDeclaration.getName() + "\'"));
            }
        });
    }

    @Override
    public void onProcedureCall(final FunctionDeclaration functionDeclaration) {
        if (Dlog.DEBUG) Log.d(TAG, "onProcedureCall: " + functionDeclaration.getName());
        mMessageHandler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addVariable(new DebugItem(DebugItem.TYPE_MSG, ">_ " + "Call function \'"
//                        + functionDeclaration.getName() + "\'"));
            }
        });
    }

    @Override
    public void onNewMessage(final String msg) {
        mMessageHandler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addVariable(new DebugItem(DebugItem.TYPE_MSG, ">_ " + msg));
            }
        });
    }

    @Override
    public void onClearDebug() {
////        debugView.clear();
    }

    @Override
    public void onVariableChangeValue(final String name, Object old, final Object value) {
    }

    @Override
    public void onFunctionCall(final String name) {
        mMessageHandler.post(new Runnable() {
            @Override
            public void run() {
////                debugView.addVariable(new DebugItem(DebugItem.TYPE_MSG, "> " + "Call procedure \'" + name + "\'"));
            }
        });
    }

    @Override
    public void onLine(LineInfo lineInfo) {
        Log.d(TAG, "onLine: " + lineInfo);
    }

    /**
     * force stop
     */
    private void stopProgram() {
        Log.d(TAG, "stopProgram: ");
        try {
            program.terminate();
            Toast.makeText(this, R.string.program_stopped, Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
            if (Dlog.DEBUG) Log.d(TAG, "onStop: Program is stopped");
        }
    }

    @Override
    public synchronized void startInput(IOLib lock) {
        this.mLock = lock;
        if (Dlog.DEBUG) Log.d(TAG, "startInput: ");
        mMessageHandler.sendEmptyMessage(SHOW_KEYBOARD);
        isCanRead.set(true);
        new Thread(runnableInput).start();
    }


    @Override
    public void stopInput() {
        //stop in put thread
        isCanRead.set(false);
    }

    /**
     * exec program, run program in internal memory
     *
     * @param path - file pas
     */
    protected void createAndRunProgram(final String path) {
        String code = mFileManager.readFileAsString(path);

        //clone it to internal storage
        programFile = mFileManager.setContentFileTemp(code);
        getConsoleView().commitString("execute file: " + path + "\n");
        getConsoleView().commitString("---------------------------" + "\n");
        mMessageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ThreadGroup group = new ThreadGroup("threadGroup");
                new Thread(group, runProgram, path, 2000000).start();
            }
        }, 200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getConsoleView().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConsoleView().onResume();
    }

    public abstract void debugProgram();
}

