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
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;

/**
 * Change type of function when wrong type
 * <code>
 * function func : string;  begin func := 2; end; begin  end.
 * </code>
 * <p>
 * after
 * <code>
 * function func : integer;  begin func := 2; end; begin  end.
 * </code>
 * Created by Duy on 11/2/2017.
 */
public class ChangeTypeFunction implements AutoFixCommand {
    private static final String TAG = "ChangeTypeFunction";
    private final UnConvertibleTypeException exception;
    private final Name functionName;
    private final Type newType;

    public ChangeTypeFunction(UnConvertibleTypeException exception, Name functionName, Type newType) {
        this.exception = exception;
        this.functionName = functionName;
        this.newType = newType;
    }

    @Override
    public void execute(EditorView editable) {
        //get a part of text
        TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());
        Pattern pattern = Pattern.compile(
                "(^function\\s+|\\s+function\\s+)" + //function token //1
                        "(" + Pattern.quote(functionName.getOriginName()) + ")" + //name of function         //2
                        "(\\s?)" + //white space                      //3
                        "(:)" +                                       //4
                        "(.*?)" + //type of function                  //5
                        ";"); //semicolon                             //6
        Matcher matcher = pattern.matcher(scope.getText());
        if (matcher.find()) {
            editable.disableTextWatcher();

            DLog.d(TAG, "changeTypeFunction: match " + matcher);
            int start = matcher.start(5) + scope.getOffset();
            int end = matcher.end(5) + scope.getOffset();

            String insertText = " " + newType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start + 1, start + insertText.length());

            editable.setSuggestData(KeyWord.DATA_TYPE);
            editable.toast(R.string.select_type);
            editable.showKeyboard();

            editable.enableTextWatcher();
        } else {
            DLog.d(TAG, "changeTypeFunction: can not find " + pattern);
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String string = context.getString(R.string.change_function_type,
                functionName.toString(),
                newType.getName().toString());
        return ExceptionManager.highlight(context, string);
    }
}
