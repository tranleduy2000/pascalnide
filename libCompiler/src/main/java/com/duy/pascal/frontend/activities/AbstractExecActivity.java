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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.duy.pascal.backend.ast.codeunit.DebugMode;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.program.PascalProgram;
import com.duy.pascal.backend.builtin_libraries.io.IOLib;
import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.ScriptTerminatedException;
import com.duy.pascal.backend.source_include.FileScriptSource;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.alogrithm.InputData;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.utils.StringCompare;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.frontend.alogrithm.InputData.MAX_INPUT;

public abstract class AbstractExecActivity extends RunnableActivity {
    public static final boolean DEBUG = DLog.DEBUG;
    protected static final String TAG = AbstractExecActivity.class.getSimpleName();
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
    /**
     * set <code>true</code> if enable DEBUG mode, program will be pause every line
     */
    protected final AtomicBoolean enableDebug = new AtomicBoolean(false);
    protected String input = "";
    protected String filePath = "";
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
                            getConsoleView().writeString(String.valueOf(str));
                        }
                        break;
                    default:
                        if ((StringCompare.isGreaterEqual(str, " ")) && (inputData.last < MAX_INPUT)) {
                            inputData.data[inputData.last++] = str;
                            getConsoleView().writeString(String.valueOf(str));
                        }
                        break;
                }
            } while (exitFlag == 0 && isCanRead.get());
            getConsoleView().writeString("\n"); //return new line
            input = inputData.toString();
            isCanRead.set(false);
            if (mLock != null) {
                if (mLock instanceof IOLib) {
                    ((IOLib) mLock).setInputBuffer(input);
                }
            }
        }

    };
    /**
     * this object use store buffer key
     */
    protected char keyCodeBuffer;
    /**
     * set <code>true</code> if you want to DEBUG program
     */
    protected boolean debugging = false;
    protected RuntimeExecutableCodeUnit program;
    protected String programFile;
    private final Runnable runProgram = new Runnable() {
        @Override
        public void run() {
            try {
                //compile
                PascalCompiler pascalCompiler = new PascalCompiler(AbstractExecActivity.this);
                try {
                    ArrayList<ScriptSource> searchPath = new ArrayList<>();
                    searchPath.add(new FileScriptSource(new File(filePath).getParent()));
                    PascalProgram pascalProgram = PascalCompiler.loadPascal(
                            new File(programFile).getName(),
                            new FileReader(programFile),
                            searchPath,
                            AbstractExecActivity.this);

                    program = pascalProgram.generate();

                    //set stack size for the program
                    long maxStackSize = getPreferences().getMaxStackSize();
                    program.setMaxStackSize(maxStackSize);

                    if (isEnableDebug()) {
                        program.enableDebug();
                        program.setDebugMode(DebugMode.STEP_INFO);
                        program.setDebugListener((DebugListener) AbstractExecActivity.this);
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

    @Override
    public String getCurrentDirectory() {
        try {
            return new File(filePath).getParent();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public boolean isDebugging() {
        return debugging;
    }

    public boolean isEnableDebug() {
        return enableDebug.get();
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug.set(enableDebug);
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

    @Override
    public void print(final CharSequence charSequence) {
        getConsoleView().writeString(charSequence.toString());
    }

    @Override
    public void println(final CharSequence charSequence) {
        getConsoleView().writeString(charSequence.toString());
        getConsoleView().writeString("\n");
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
    public void clearConsole() {
        getConsoleView().clearScreen();
    }

    @Override
    protected void onDestroy() {
        DLog.d(TAG, "onDestroy() called");

        //stop readkey, keypressed event
        stopInput();
        resumeProgram();
        //stop program
        stopProgram();
        getConsoleView().onDestroy();

        super.onDestroy();
    }

    private void resumeProgram() {
        try {
            if (mLock != null && mLock instanceof IOLib) {
                ((IOLib) mLock).resume();
            }
        } catch (Exception e) {

        }
    }

    /**
     * force stop
     */
    private void stopProgram() {
        try {
            program.terminate();
            Toast.makeText(this, R.string.program_stopped, Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
            if (DLog.DEBUG) {
                DLog.d(TAG, "onDestroy: Program is STOPPED");
            }
        }
    }

    @Override
    public synchronized void startInput(IOLib lock) {
        this.mLock = lock;
        if (DLog.DEBUG) DLog.d(TAG, "startInput: ");
        mMessageHandler.sendEmptyMessage(SHOW_KEYBOARD);
        isCanRead.set(true);
        new Thread(runnableInput).start();
    }

    public void stopInput() {
        //stop in put thread
        isCanRead.set(false);
    }

    /**
     * exec program, run program in internal memory
     *
     * @param path - path of file pas
     */
    protected void createAndRunProgram(final String path) {
        DLog.d(TAG, "createAndRunProgram() called with: path = [" + path + "]");

        StringBuilder code = mFileManager.fileToString(path);
        if (code.toString().toLowerCase().startsWith("unit ")) {
            onError(new RuntimeException(getString(R.string.can_not_exec_unit)));
            return;
        }

        //clone file to internal storage
        programFile = mFileManager.setContentFileTemp(code.toString());

        //show prompt
        this.println("execute file: " + path);
        this.println("---------------------------" + "\n");

        //create new thread and run program
        mMessageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ThreadGroup group = new ThreadGroup("threadGroup");

                //increase stack size in API 25 or above with thread group,
                // if API < 25, it will be throw stack overflow error
                long stackSize = 10000 * 1024; //10.240.000
                new Thread(group, runProgram, path, stackSize).start();
            }
        }, 100);
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

