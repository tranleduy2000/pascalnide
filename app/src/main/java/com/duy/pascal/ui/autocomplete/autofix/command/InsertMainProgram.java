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

package com.duy.pascal.ui.autocomplete.autofix.command;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;

import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Created by Duy on 11/2/2017.
 */
public class InsertMainProgram implements AutoFixCommand {
    private static final String TAG = "InsertMainProgram";

    @Override
    public void execute(EditorView editable) {
        DLog.d(TAG, "fixProgramNotFound() called with: editable = [" + editable + "]");
        editable.disableTextWatcher();

        String tabCharacter = editable.getTabCharacter();
        editable.getText().insert(editable.length(), "\nbegin\n" + tabCharacter + "\nend.\n");
        editable.setSelection(editable.length() - "\nend.\n".length());

        editable.enableTextWatcher();
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        return highlight(context, context.getString(R.string.add_begin_end));
    }
}
