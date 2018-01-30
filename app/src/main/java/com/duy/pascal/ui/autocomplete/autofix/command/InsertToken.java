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

import com.duy.pascal.interperter.exceptions.parsing.missing.MissingTokenException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.editor.view.LineUtils;

import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Created by Duy on 11/2/2017.
 */
public class InsertToken implements AutoFixCommand {
    private MissingTokenException e;

    public InsertToken(MissingTokenException e) {
        this.e = e;
    }

    @Override
    public void execute(EditorView editable) {
        LineNumber line = e.getLineNumber();
        int start = LineUtils.getStartIndexAtLine(editable, line.getLine()) + line.getColumn();
        String insertText = " " + e.getMissingToken() + " ";
        editable.getText().insert(start, insertText);
        editable.setSelection(start, insertText.length() + start);
        editable.showKeyboard();
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String insertText = e.getMissingToken();
        return highlight(context, context.getString(R.string.insert_token, insertText));
    }
}
