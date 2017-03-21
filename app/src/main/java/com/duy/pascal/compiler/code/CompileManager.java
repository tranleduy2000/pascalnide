package com.duy.pascal.compiler.code;

import android.app.Activity;
import android.content.Intent;

import com.duy.interpreter.core.PascalCompiler;
import com.duy.interpreter.exceptions.MainProgramNotFoundException;
import com.duy.pascal.compiler.activities.EditorActivity;
import com.duy.pascal.compiler.activities.ExecuteActivity;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Duy on 11-Feb-17.
 */

public class CompileManager {

    public static final String FILE_PATH = "file_name";     // extras indicators
    public static final String IS_NEW = "is_new";
    public static final String INITIAL_POS = "initial_pos";
    public static final String ERROR_MSG = "error_msg";
    public static final int ACTIVITY_EDITOR = 1001;
    public static final String MODE = "run_mode";
    private final Activity mActivity;

    public CompileManager(Activity activity) {
        this.mActivity = activity;
    }

    // Execute compiled file
    public void execute(String name) {
        Intent intent = new Intent(mActivity, ExecuteActivity.class);
        intent.putExtra(FILE_PATH, name);
        mActivity.startActivity(intent);
    }

    public void edit(String fileName, Boolean isNew) {
        Intent intent = new Intent(mActivity, EditorActivity.class);
        intent.putExtra(FILE_PATH, fileName);
        intent.putExtra(IS_NEW, isNew);
        intent.putExtra(INITIAL_POS, 0);
        mActivity.startActivityForResult(intent, ACTIVITY_EDITOR);
    }

    public Exception doCompile(String mFilePath) {
        try {
            PascalProgram pascalProgram = new PascalCompiler(null).loadPascal(mFilePath,
                    new FileReader(mFilePath),
                    new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>());
            if (pascalProgram.main == null) {
                return new MainProgramNotFoundException();
            }
        } catch (Exception e) {
            return e;
        }
        return null;
    }
}
