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


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.config.DebugMode;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.debugable.DebugListener;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.ScriptTerminatedException;
import com.duy.pascal.interperter.libraries.io.IOLib;
import com.duy.pascal.interperter.source.FileScriptSource;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.ui.BaseActivity;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.file.FileManager;
import com.duy.pascal.ui.runnable.model.InputData;
import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.utils.StringCompare;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.ui.runnable.model.InputData.MAX_INPUT;

public abstract class AbstractExecActivity extends BaseActivity implements ProgramHandler {
    public static final boolean DEBUG = DLog.DEBUG;
    protected static final String TAG = AbstractExecActivity.class.getSimpleName();
    protected static final int COMPLETE = 4;
    protected static final int RUNTIME_ERROR = 5;
    protected static final int SHOW_KEYBOARD = 6;

    protected final AtomicBoolean mIsRunning = new AtomicBoolean(true);
    @SuppressLint("HandlerLeak")
    protected final Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!mIsRunning.get()) return;
            switch (msg.what) {
                case RUNTIME_ERROR:
                    if (!isFinishing()) {
                        onError((Throwable) msg.obj);
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
    protected String mFilePath = "";
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
     * set <code>true</code> if you want to DEBUG program
     */
    protected boolean debugging = false;
    protected RuntimeExecutableCodeUnit mProgram;
    private final Runnable runProgram = new Runnable() {
        @Override
        public void run() {
            try {
                try {
                    ArrayList<ScriptSource> include = new ArrayList<>();
                    FileScriptSource source = new FileScriptSource(new File(mFilePath));
                    include.add(source);
                    PascalProgramDeclaration pascalProgram = PascalCompiler.loadPascal(source, include,
                            AbstractExecActivity.this);

                    mProgram = pascalProgram.generate();

                    //set stack size for the program
                    long maxStackSize = getPreferences().getMaxStackSize();
                    mProgram.setMaxStackSize(maxStackSize);

                    if (isEnableDebug()) {
                        mProgram.enableDebug();
                        mProgram.setDebugMode(DebugMode.STEP_INFO);
                        mProgram.setDebugListener((DebugListener) AbstractExecActivity.this);
                    }

                    mProgram.run();

                    mMessageHandler.sendEmptyMessage(COMPLETE);
                } catch (ScriptTerminatedException e) {
                    mMessageHandler.sendEmptyMessage(COMPLETE);
                } catch (RuntimePascalException | ParsingException e) {
                    mMessageHandler.sendMessage(mMessageHandler.obtainMessage(RUNTIME_ERROR, e));
                }
            } catch (Throwable e) {
                mMessageHandler.sendMessage(mMessageHandler.obtainMessage(RUNTIME_ERROR, e));
            }
        }
    };
    protected String programFile;
    protected FileManager mFileManager;

    @Override
    public String getCurrentDirectory() {
        try {
            return new File(mFilePath).getParent();
        } catch (Exception e) {
            return "";
        }
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

    protected abstract void onError(Throwable obj);

    protected abstract void showDialogComplete();

    protected abstract void showKeyBoard();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileManager = new FileManager(this);
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
        return getConsoleView().readKey();
    }

    @Override
    public boolean keyPressed() {
        return getConsoleView().getKeyBuffer().keyPressed();
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
        } catch (Exception ignored) {

        }
    }

    /**
     * force stop
     */
    private void stopProgram() {
        try {
            mProgram.terminate();
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

        String code = mFileManager.fileToString(path);
        if (code.toLowerCase().startsWith("unit ")) {
            onError(new RuntimeException(getString(R.string.can_not_exec_unit)));
            return;
        }

        //clone file to internal storage
        programFile = mFileManager.setContentFileTemp(code);

        //show prompt
        println(String.format("execute file: %s", path));
        println("---------------------------\n");

        //create new thread and run program
        mMessageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ThreadGroup group = new ThreadGroup("threadGroup");

                //increase stack size in API 25 or above with thread group,
                // if API < 25, it will be throw stack overflow error
                long stackSize = 10000 * 1024; //10 240 000
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

