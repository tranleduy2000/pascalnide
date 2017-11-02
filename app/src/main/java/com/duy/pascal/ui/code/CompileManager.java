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

package com.duy.pascal.ui.code;

import android.app.Activity;
import android.content.Intent;

import com.duy.pascal.ui.debug.activities.DebugActivity;
import com.duy.pascal.ui.editor.EditorActivity;
import com.duy.pascal.ui.runnable.ExecuteActivity;

import java.io.File;

/**
 * Created by Duy on 11-Feb-17.
 */

public class CompileManager {

    public static final String FILE = "file";
    public static final String IS_NEW = "is_new";
    public static final String INITIAL_POS = "initial_pos";
    public static final int ACTIVITY_EDITOR = 1001;
    private final Activity mActivity;

    public CompileManager(Activity activity) {
        this.mActivity = activity;
    }

    public static void execute(Activity activity, String filePath) {
        Intent intent = new Intent(activity, ExecuteActivity.class);
        intent.putExtra(FILE, new File(filePath));
        activity.finish();
        activity.startActivity(intent);
    }

    public static void debug(Activity mActivity, String filePath) {
        Intent intent = new Intent(mActivity, DebugActivity.class);
        intent.putExtra(FILE, new File(filePath));
        mActivity.startActivity(intent);
    }

    // Execute compiled file
    public void execute(String filePath) {
        Intent intent = new Intent(mActivity, ExecuteActivity.class);
        intent.putExtra(FILE, new File(filePath));
        mActivity.startActivity(intent);
    }

    public void debug(String filePath) {
        Intent intent = new Intent(mActivity, DebugActivity.class);
        intent.putExtra(FILE, new File(filePath));
        mActivity.startActivity(intent);
    }

    public void edit(String filePath, Boolean isNew) {
        Intent intent = new Intent(mActivity, EditorActivity.class);
        intent.putExtra(FILE, new File(filePath));
        intent.putExtra(IS_NEW, isNew);
        intent.putExtra(INITIAL_POS, 0);
        mActivity.startActivityForResult(intent, ACTIVITY_EDITOR);
    }

}
