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

package com.duy.pascal.ui.autocomplete.autofix;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;
import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

/**
 * Created by Duy on 9/24/2017.
 */

class ChangeTypeHelper {
    /**
     * Change type of function from <code>valueType</code> to <code>name</code>
     *
     * @param functionName - name of function
     */
    @NonNull
    static AutoFixCommand changeTypeFunction(final UnConvertibleTypeException exception, final Name functionName, final Type newType) {
        return new AutoFixCommand() {
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
                                ";"); //end                                   //6
                Matcher matcher = pattern.matcher(scope.getText());
                if (matcher.find()) {
                    editable.disableTextWatcher();

                    DLog.d(TAG, "changeTypeFunction: match " + matcher);
                    int start = matcher.start(5) + scope.getOffset();
                    int end = matcher.end(5) + scope.getOffset();

                    String insertText = " " + newType.toString();
                    editable.getText().replace(start, end, insertText);
                    editable.setSelection(start, start + insertText.length());
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
        };
    }

    /**
     * Change type of variable
     */
    @NonNull
    static AutoFixCommand changeTypeVar(final UnConvertibleTypeException exception,
                                        final VariableAccess variable,
                                        final Type newType) {
        return new AutoFixCommand() {
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
        };

    }

    /**
     * This method will be Change type constant to type of value
     * if constant is define with type
     * <p>
     * Example
     * const a: integer = 'adsda'; => change to string
     */
    @NonNull
    static AutoFixCommand changeTypeConst(final UnConvertibleTypeException exception, final ConstantAccess constant, final Type newType) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                DLog.d(TAG, "fixUnConvertType: constant " + constant);
                TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());
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
        };
    }
}
