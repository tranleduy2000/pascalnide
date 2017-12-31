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

import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.editor.view.EditorView;

/**
 * Created by Duy on 11/2/2017.
 */
// TODO: 11/2/2017
public class DeclareFunction implements AutoFixCommand {
    private UnknownIdentifierException exception;

    public DeclareFunction(UnknownIdentifierException exception) {
        this.exception = exception;
    }

    @Override
    public void execute(EditorView editable) {
        int length = 0;
        StringBuilder code = new StringBuilder();
        code.append("function ").append(exception.getName())
                .append("(): ");
        length = code.length();
        code.append(";\n")
                .append("begin\n")
                .append("\n")
                .append("end;\n");
        editable.disableTextWatcher();
        editable.getText().insert(0, code);
        editable.setSelection(length);
        editable.setSuggestData(KeyWord.DATA_TYPE);
        editable.enableTextWatcher();
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.declare_function);
    }

}
