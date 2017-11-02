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

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.define.TypeIdentifierExpectException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.Patterns;
import com.duy.pascal.ui.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.editor.view.AutoIndentEditText;
import com.duy.pascal.ui.editor.view.EditorView;

import java.util.regex.Matcher;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Created by Duy on 11/2/2017.
 */
public class DeclareType implements AutoFixCommand {
    private TypeIdentifierExpectException exception;

    public DeclareType(TypeIdentifierExpectException exception) {
        this.exception = exception;
    }

    @Override
    public void execute(EditorView editable) {
        //don't work if has selection
        //sub string from 0 to postion error
        TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());

        Name type = exception.getMissingType();
        String textToInsert;
        Matcher matcher = Patterns.TYPE.matcher(scope.getText());
        int insertPosition = 0;

        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = editable.getTabCharacter() + type + " = " + AutoIndentEditText.CURSOR + ";\n";
        } else {
                    /*
                    if not found "type" keyword, insert new type keyword
                    type    <== keyword type must be above var
                        ....
                    var
                        ....
                    */
            if ((matcher = Patterns.PROGRAM.matcher(scope.getText())).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.VAR.matcher(scope.getText())).find()) {
                insertPosition = matcher.start();
            } else if ((matcher = Patterns.CONST.matcher(scope.getText())).find()) {
                insertPosition = matcher.start();
            } else if ((matcher = Patterns.USES.matcher(scope.getText())).find()) {
                insertPosition = matcher.end();
            }
            textToInsert = "\ntype\n" + editable.getTabCharacter() + type + " = " + AutoIndentEditText.CURSOR + ";\n";
        }

        insertPosition += scope.getOffset();

        editable.disableTextWatcher();
        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + textToInsert.length() - 3);
        editable.setSuggestData(KeyWord.DATA_TYPE);
        editable.enableTextWatcher();
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String string = context.getString(R.string.declare_type, exception.getMissingType());
        return highlight(context, string);
    }

}
