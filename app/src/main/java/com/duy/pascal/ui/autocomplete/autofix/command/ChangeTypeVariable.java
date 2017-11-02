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

import com.duy.pascal.interperter.ast.runtime.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;


/**
 * Created by Duy on 11/2/2017.
 */
public class ChangeTypeVariable implements AutoFixCommand {
    private static final String TAG = "ChangeTypeVariable";
    private final UnConvertibleTypeException exception;
    private final VariableAccess variable;
    private final Type newType;

    public ChangeTypeVariable(UnConvertibleTypeException exception, VariableAccess variable, Type newType) {
        this.exception = exception;
        this.variable = variable;
        this.newType = newType;
    }

    @Override
    public void execute(EditorView editable) {
        //get a part of text
        TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());

        DLog.d(TAG, "changeTypeVar() called with: editable = [" + editable + "], text = [" + scope + "], identifier = [" + variable + "], valueType = [" + newType + "]");


        final Name name = variable.getName();
        Pattern pattern = Pattern.compile("(^var\\s+|\\s+var\\s+)" + //match "var"  //1
                "(.*?)" + //other variable                                  //2
                "(" + Pattern.quote(name.getOriginName()) + ")" +  //name of variable                       //3
                "(\\s?)" +//one or more white space                         //4
                "(:)" + //colon                                             //5
                "(.*?)" + //any type                                        //6
                "(;)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(scope.getText());
        DLog.d(TAG, "fixUnConvertType: " + scope);

        if (matcher.find()) {
            editable.disableTextWatcher();
            DLog.d(TAG, "fixUnConvertType: match " + matcher);
            int start = matcher.start(6) + scope.getOffset();
            int end = matcher.end(6) + scope.getOffset();

            String insertText = " " + newType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start + 1, start + insertText.length());
            editable.showKeyboard();
            editable.enableTextWatcher();
        } else {
            DLog.d(TAG, "fixUnConvertType: can not find " + pattern);
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String string = context.getString(R.string.change_var_type,
                variable.getName().toString(),
                newType.getName().toString());
        return ExceptionManager.highlight(context, string);
    }
}
