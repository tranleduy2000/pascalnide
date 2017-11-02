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
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.Patterns;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.editor.view.EditorView;

import java.util.regex.Matcher;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Declare variable when missing variable
 * <p>
 * <code>
 * begin  a := 2; end.
 * </code>
 *
 * After
 * <code>
 *     var a: integer; begin a := 2;end.
 * </code>
 * <p>
 * Created by Duy on 11/2/2017.
 */
public class DeclareVariable implements AutoFixCommand {


    @NonNull
    private final Name name;
    @Nullable
    private final String type;
    @Nullable
    private final String initValue;
    @NonNull
    private final LineInfo start;
    @NonNull
    private final LineInfo end;

    /**
     * @param name      - name of variable
     * @param type      - type of variable
     * @param initValue - init value, it can be null
     */
    public DeclareVariable(@NonNull LineInfo start, @NonNull LineInfo end, @NonNull Name name,
                           @Nullable String type, @Nullable String initValue) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.type = type;
        this.initValue = initValue;
    }

    @Override
    @UiThread
    public void execute(EditorView editable) {
        String textToInsert;
        int insertPosition = 0;
        int startSelect;
        int endSelect;
        TextData scope = getText(editable, start, end);
        String type = this.type != null ? this.type : "";
        Matcher matcher = Patterns.VAR.matcher(scope.getText());
        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = "\n" + editable.getTabCharacter() + name + ": ";

            startSelect = textToInsert.length();
            endSelect = startSelect + type.length();

            textToInsert += type + (initValue != null ? " = " + initValue : "") + ";\n";
        } else {
            if ((matcher = Patterns.TYPE.matcher(scope.getText())).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.USES.matcher(scope.getText())).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.PROGRAM.matcher(scope.getText())).find()) {
                insertPosition = matcher.end();
            }
            textToInsert = "\nvar\n" + editable.getTabCharacter() + name + ": ";

            startSelect = textToInsert.length();
            endSelect = startSelect + type.length();

            textToInsert += type + (initValue != null ? " = " + initValue : "") + ";\n";
        }

        int selectStart = scope.getOffset() + insertPosition + startSelect;
        int selectEnd = scope.getOffset() + insertPosition + endSelect;

        editable.disableTextWatcher();
        editable.getText().insert(scope.getOffset() + insertPosition, textToInsert);
        editable.setSelection(selectStart, selectEnd);
        editable.setSuggestData(KeyWord.DATA_TYPE);
        editable.toast(R.string.select_type);
        editable.enableTextWatcher();
    }


    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String str;
        if (type == null || type.isEmpty()) {
            str = context.getString(R.string.declare_variable_3, name);
        } else {
            str = context.getString(R.string.declare_variable_2, name, type);
        }
        return highlight(context, str);
    }
}
