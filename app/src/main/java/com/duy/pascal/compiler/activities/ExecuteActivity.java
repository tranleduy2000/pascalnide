package com.duy.pascal.compiler.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.startup.PascalCompiler;
import com.duy.pascal.compiler.BuildConfig;
import com.duy.pascal.compiler.CodeManager;
import com.duy.pascal.compiler.CompileManager;
import com.duy.pascal.compiler.data.FileManager;
import com.duy.pascal.compiler.view.ConsoleView;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.compiler.activities.ExecuteActivity.InputData.MAX_INPUT;


public class ExecuteActivity extends AbstractConsoleActivity {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = ExecuteActivity.class.getSimpleName();
    public String input = "";
    String fileName;
    private AtomicBoolean isCanRead = new AtomicBoolean(false);
    Runnable runnableInput = new Runnable() {
        @Override
        public void run() {
            int exitFlag;
            char c;
            InputData inputData = new InputData();
            inputData.first = 0;
            inputData.last = 0;
            exitFlag = 0;
            do {
                c = mConsoleView.getChar();
                Log.d(TAG, "run: " + c);
                switch (c) {
                    case 10: // return
                        exitFlag = 1;
                        break;
                    case 8:
                    case KeyEvent.KEYCODE_DEL: // backspace
                        if (inputData.last > 0) {
                            inputData.last--;
                            mConsoleView.emitChar(c);
                        }
                        break;
                    default:
                        if ((c >= ' ') && (inputData.last < MAX_INPUT)) {
                            inputData.data[inputData.last++] = c;
                            mConsoleView.emitChar(c);
                        }
                        break;
                }
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } while (exitFlag == 0 && isCanRead.get());
            mConsoleView.emitChar((char) 10); // return new line
            input = inputData.toString();
            isCanRead.set(false);
        }
    };
    private RuntimeExecutable program;
    private String programFile;
    private FileManager mFileManager;
    private Handler handler = new Handler();
    Runnable complete = new Runnable() {
        @Override
        public void run() {
            showDialogComplete();
        }
    };
    Runnable runnableRunProgram = new Runnable() {
        @Override
        public void run() {
            try {
                //compile
                PascalCompiler pascalCompiler = new PascalCompiler(ExecuteActivity.this);
                try {
                    //run
                    PascalProgram pascalProgram = pascalCompiler.loadPascal(programFile,
                            new FileReader(programFile),
                            new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>());
                    program = pascalProgram.run();
                    program.run();
                    handler.post(complete);
                } catch (RuntimePascalException | FileNotFoundException | ParsingException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onError(e);
                        }
                    });
                }
            } catch (final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onError(e);
                    }
                });
            }
        }
    };
    private Thread runThread = new Thread(runnableRunProgram);
    private ConsoleView consoleView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileManager = new FileManager(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileName = extras.getString(CompileManager.FILE_PATH);
            doRun(fileName);
        }
    }

    /**
     * exec program, run program in internal memory
     *
     * @param name - file pas
     */
    private void doRun(final String name) {
        String code = mFileManager.readFileAsString(name);
        code = CodeManager.normalCode(code);
        //clone it to internal storage
        this.programFile = mFileManager.setContentFileTemp(code);
        runThread.start();
    }

    private void showDialogComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Complete")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 100);
                    }
                })
                .setNegativeButton("View console", new DialogInterface.OnClickListener() {
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
    public void onResume() {
        super.onResume();
    }

    /**
     * on error compile or runtime
     */
    public void onError(Exception e) {
        mConsoleView.setTextColor(Color.RED);
        if (e instanceof ParsingException) {
            mConsoleView.emitString(e.getMessage() + "\n");
            LineInfo lineInfo = ((ParsingException) e).line;
            String line = String.valueOf(lineInfo.line);
            mConsoleView.emitString("Error in line " + line + "\n");
            mConsoleView.emitString("Code: " + lineInfo.sourcefile + "\n");
        } else {
            mConsoleView.emitString(e.getMessage());
        }
        //debug
        if (DEBUG) e.printStackTrace();
    }

    public void startInput() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showKeyBoard();
            }
        });
        isCanRead.set(true);
        Log.d(TAG, "startInput: ");
        new Thread(runnableInput).start();
    }

    public boolean isInputting() {
        return isCanRead.get();
    }

    /**
     * set text console color
     *
     * @param textColor
     */
    public void setTextColor(final int textColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConsoleView.setTextColor(textColor);
            }
        });
    }

    /**
     * set background console
     *
     * @param color
     */
    public void setTextBackground(final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConsoleView.setConsoleColor(color);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop in put thread
        isCanRead.set(false);
        //stop program
        try {
            program.terminate();
        } catch (Exception ignored) {
        }
    }

    public String getInput() {
        return input;
    }

    public ConsoleView getConsoleView() {
        return mConsoleView;
    }

    public class InputData {
        static final int MAX_INPUT = 1000;
        public char[] data = new char[MAX_INPUT]; // the array of the caracters
        int last;    // number of char in the input buffer
        int first;    // index of the first character

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = first; i < last; i++) stringBuilder.append(data[i]);
            return stringBuilder.toString();
        }
    }

}

