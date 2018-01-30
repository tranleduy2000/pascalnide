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

import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
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
 * Change type of constant when wrong type
 * eg.
 * <code>
 * const i: integer = 'string'; begin end.
 * </code>
 * <p>
 * This class will be change to
 * <p><code>
 * <p>const i: string = 'string'; begin end.
 * </code>
 * Created by Duy on 11/2/2017.
 */
public class ChangeTypeConstant implements AutoFixCommand {
    private static final String TAG = "ChangeTypeConstant";
    private final UnConvertibleTypeException exception;
    private final ConstantAccess constant;
    private final Type newType;

    public ChangeTypeConstant(UnConvertibleTypeException exception, ConstantAccess constant, Type newType) {
        this.exception = exception;
        this.constant = constant;
        this.newType = newType;
    }

    @Override
    public void execute(EditorView editable) {
        DLog.d(TAG, "fixUnConvertType: constant " + constant);
        TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineNumber());
        if (constant.getName() == null) { //can not replace because it is not a identifier
            DLog.d(TAG, "changeTypeConst: this is not identifier");
            return;
        }

        Name name = constant.getName();
        Pattern pattern = Pattern.compile("(^const\\s+|\\s+const\\s+)" + //match "const"  //1
                        "(.*?)" + //other const                                  //2
                        "(" + Pattern.quote(name + "") + ")" + //name of const                       //3
                        "(\\s?)" +//one or more white space                         //4
                        "(:)" + //colon                                             //5
                        "(.*?)" + //type????                                        //6
                        "(=)" +
                        "(.*?)" +
                        "(;)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(scope.getText());

        if (matcher.find()) {
            DLog.d(TAG, "fixUnConvertType: match " + matcher);
            final int start = matcher.start(6) + scope.getOffset();
            int end = matcher.end(6) + scope.getOffset();

            final String insertText = " " + newType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start, start + insertText.length());
            editable.showKeyboard();
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String string = context.getString(R.string.change_const_type,
                constant.getName().toString(),
                newType.getName().toString());
        return ExceptionManager.highlight(context, string);
    }
}
