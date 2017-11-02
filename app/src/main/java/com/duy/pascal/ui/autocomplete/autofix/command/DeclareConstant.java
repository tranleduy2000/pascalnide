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
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.Patterns;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.editor.view.EditorView;

import java.util.regex.Matcher;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Declare constant
 * eg.
 * <code>
 * begin writeln(a); end.
 * </code>
 * <p>
 * After
 * <code>
 * const a = 'string';
 * begin
 * writeln(a);
 * end.
 * </code>
 * <p>
 * Created by Duy on 11/2/2017.
 */
public class DeclareConstant implements AutoFixCommand {
    private UnknownIdentifierException e;

    public DeclareConstant(UnknownIdentifierException e) {
        this.e = e;
    }

    @Override
    public void execute(EditorView editable) {
        //sub string from 0 to position error
        TextData text = getText(editable, e.getScope().getStartPosition(), e.getLineInfo());

        String textToInsert;
        int insertPosition = 0;
        Name name = e.getName();

        Matcher matcher = Patterns.CONST.matcher(text.getText());
        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = "\n" + editable.getTabCharacter() + name + " = %v ;";
        } else {
            if ((matcher = Patterns.PROGRAM.matcher(text.getText())).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.USES.matcher(text.getText())).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.TYPE.matcher(text.getText())).find()) {
                insertPosition = matcher.start();
            }
            textToInsert = "\nconst \n" + editable.getTabCharacter() + name + " = %v ;";
        }

        insertPosition += text.getOffset();

        matcher = Patterns.REPLACE_CURSOR.matcher(textToInsert);
        if (matcher.find()) {
            textToInsert = textToInsert.replaceAll("%\\w", "");

            editable.getText().insert(insertPosition, textToInsert);
            editable.setSelection(insertPosition + matcher.start());
            editable.showKeyboard();
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String str = context.getString(R.string.declare_constant_2, e.getName().getOriginName());
        return highlight(context, str);
    }
}
