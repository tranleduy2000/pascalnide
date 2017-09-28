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

package com.duy.pascal.frontend.autocomplete.autofix;

import android.util.Log;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.autocomplete.autofix.model.TextData;
import com.duy.pascal.frontend.editor.view.EditorView;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

/**
 * Created by Duy on 9/24/2017.
 */

class ChangeTypeHelper {
    /**
     * Change type of function from <code>valueType</code> to <code>name</code>
     *
     * @param editable
     * @param name     - name of function
     * @param text     - a part text of the edit start at 0 and end at lineInfo where then function place
     */
    static void changeTypeFunction(EditorView editable, final Name name, TextData text, Type valueType) {
        Pattern pattern = Pattern.compile(
                "(^function\\s+|\\s+function\\s+)" + //function token //1
                        "(" + name + ")" + //name of function         //2
                        "(\\s?)" + //white space                      //3
                        "(:)" +                                       //4
                        "(.*?)" + //type of function                  //5
                        ";"); //end                                   //6
        Matcher matcher = pattern.matcher(text.getText());
        if (matcher.find()) {
            editable.disableTextWatcher();

            DLog.d(TAG, "changeTypeFunction: match " + matcher);
            int start = matcher.start(5) + text.getOffset();
            int end = matcher.end(5) + text.getOffset();

            String insertText = " " + valueType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start, start + insertText.length());
            editable.showKeyboard();

            editable.enableTextWatcher();
        } else {
            DLog.d(TAG, "changeTypeFunction: can not find " + pattern);
        }
    }

    /**
     * Change type of variable
     *
     * @param editable
     * @param text       - type to change
     * @param identifier - variable
     * @param valueType  - current type of variable
     */
    static void changeTypeVar(EditorView editable, TextData text, VariableAccess identifier, Type valueType) {
        Log.d(TAG, "changeTypeVar() called with: editable = [" + editable + "], text = [" + text + "], identifier = [" + identifier + "], valueType = [" + valueType + "]");

        final Name name = identifier.getName();
        Pattern pattern = Pattern.compile("(^var\\s+|\\s+var\\s+)" + //match "var"  //1
                "(.*?)" + //other variable                                  //2
                "(" + name + ")" + //name of variable                       //3
                "(\\s?)" +//one or more white space                         //4
                "(:)" + //colon                                             //5
                "(.*?)" + //any type                                        //6
                "(;)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(text.getText());
        DLog.d(TAG, "fixUnConvertType: " + text);

        if (matcher.find()) {
            editable.disableTextWatcher();
            DLog.d(TAG, "fixUnConvertType: match " + matcher);
            int start = matcher.start(6) + text.getOffset();
            int end = matcher.end(6) + text.getOffset();

            String insertText = " " + valueType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start + 1, start + insertText.length());
            editable.showKeyboard();

            editable.enableTextWatcher();
        } else {
            DLog.d(TAG, "fixUnConvertType: can not find " + pattern);
        }
    }

    /**
     * This method will be Change type constant to type of value
     * if constant is define with type
     * <p>
     * Example
     * const a: integer = 'adsda'; => change to string
     */
    static void changeTypeConst(EditorView editable, TextData text, ConstantAccess identifier, Type valueType) {
        DLog.d(TAG, "fixUnConvertType: constant " + identifier);

        if (identifier.getName() == null) { //can not replace because it is not a identifier
            DLog.d(TAG, "changeTypeConst: this is not identifier");
            return;
        }

        Name name = identifier.getName();
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
        Matcher matcher = pattern.matcher(text.getText());

        if (matcher.find()) {
            DLog.d(TAG, "fixUnConvertType: match " + matcher);
            final int start = matcher.start(6) + text.getOffset();
            int end = matcher.end(6) + text.getOffset();

            final String insertText = " " + valueType.toString();
            editable.getText().replace(start, end, insertText);
            editable.setSelection(start, start + insertText.length());
            editable.showKeyboard();
        }
    }

}
