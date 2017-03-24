package com.duy.pascal.frontend.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.BuildConfig;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.alogrithm.InputData;
import com.duy.pascal.frontend.code.CodeManager;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.view.ConsoleView;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.frontend.alogrithm.InputData.MAX_INPUT;


public class ExecuteActivity extends AbstractExecActivity implements DebugListener {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = ExecuteActivity.class.getSimpleName();
    public String input = "";
    String filePath;
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
            } while (exitFlag == 0 && isCanRead.get());
            mConsoleView.emitChar((char) 10); // return new line
            input = inputData.toString();
            isCanRead.set(false);
        }
    };
    private RuntimeExecutable program;
    private String programFile;
    private ApplicationFileManager mFileManager;
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
                            new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>(),
                            ExecuteActivity.this);
                    program = pascalProgram.run();
//                    program.enableDebug();
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileManager = new ApplicationFileManager(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filePath = extras.getString(CompileManager.FILE_PATH);
            if (filePath == null || filePath.isEmpty()) return;
            File file = new File(filePath);
            if (!file.exists()) {
                finish();
                return;
            }
            setTitle(file.getName());
            doRun(filePath);
        }
    }

    /**
     * exec program, run program in internal memory
     *
     * @param path - file pas
     */
    private void doRun(final String path) {
        String code = mFileManager.readFileAsString(path);
        code = CodeManager.normalCode(code);
        //clone it to internal storage
        programFile = mFileManager.setContentFileTemp(code);
        mConsoleView.emitString("execute file: " + filePath + "\n");
        mConsoleView.emitString("---------------------------" + "\n");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runThread.start();
            }
        }, 200);
    }

    private void showDialogComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.complete)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
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
            mConsoleView.emitString("File: " + lineInfo.sourcefile + "\n");
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
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Program is stopped", Toast.LENGTH_SHORT).show();
        //stop in put thread
        isCanRead.set(false);
        //stop program
        try {
            program.terminate();
        } catch (Exception ignored) {
            Log.d(TAG, "onStop: Program is stopped");
        }
    }

    public String getInput() {
        return input;
    }

    public ConsoleView getConsoleView() {
        return mConsoleView;
    }

    @Override
    public void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration) {
        Log.d(TAG, "onGlobalVariableChangeValue: " + variableDeclaration.getName() + " = " + variableDeclaration.getInitialValue());
    }

    @Override
    public void onLocalVariableChangeValue(VariableDeclaration variableDeclaration) {
        Log.d(TAG, "onLocalVariableChangeValue: " + variableDeclaration.getName() + " = " + variableDeclaration.getInitialValue());

    }

    @Override
    public void onFunctionCall(final FunctionDeclaration functionDeclaration) {
        Log.d(TAG, "onFunctionCall: " + functionDeclaration.getName());
        handler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addLine(new DebugItem(DebugItem.TYPE_MSG, ">_ " + "Call procedure \'"
//                        + functionDeclaration.getName() + "\'"));
            }
        });
    }

    @Override
    public void onProcedureCall(final FunctionDeclaration functionDeclaration) {
        Log.d(TAG, "onProcedureCall: " + functionDeclaration.getName());
        handler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addLine(new DebugItem(DebugItem.TYPE_MSG, ">_ " + "Call function \'"
//                        + functionDeclaration.getName() + "\'"));
            }
        });
    }

    @Override
    public void onNewMessage(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
//                debugView.addLine(new DebugItem(DebugItem.TYPE_MSG, ">_ " + msg));
            }
        });
    }

    @Override
    public void onClearDebug() {
////        debugView.clear();
    }

    @Override
    public void onVariableChangeValue(final String name, final Object value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
////                debugView.addLine(new DebugItem(DebugItem.TYPE_VAR, name, String.valueOf(value)));
            }
        });
    }

    @Override
    public void onFunctionCall(final String name) {
        handler.post(new Runnable() {
            @Override
            public void run() {
////                debugView.addLine(new DebugItem(DebugItem.TYPE_MSG, "> " + "Call procedure \'" + name + "\'"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next_line) {
            try {
//                program.resume();
            } catch (Exception e) {
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

