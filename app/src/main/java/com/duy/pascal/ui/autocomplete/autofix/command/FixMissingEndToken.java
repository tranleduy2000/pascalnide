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

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.editor.view.EditorView;

/**
 * Created by Duy on 11/2/2017.
 */
public class FixMissingEndToken implements AutoFixCommand {
    private GroupingException e;

    public FixMissingEndToken(GroupingException e) {
        this.e = e;
    }

    @Override
    public void execute(EditorView editable) {
        if (e.getExceptionTypes() == GroupingException.Type.UNFINISHED_BEGIN_END) {
            String text = "\nend";
            editable.getText().insert(editable.length(), text); //insert

            //select the "end" token and show keyboard
            //don't selected newline character
            editable.setSelection(editable.length() - text.length() + 1, editable.length());
            editable.showKeyboard();
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        if (e.getExceptionTypes() == GroupingException.Type.UNFINISHED_BEGIN_END) {
            String str = context.getString(R.string.add_end_at_the_end_of_program);
            return ExceptionManager.highlight(context, str);
        } else {
            return "";
        }
    }
}
