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

package com.duy.pascal.frontend.code_editor.editor_view.autofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.define.UnrecognizedTypeException;
import com.duy.pascal.frontend.code_editor.completion.KeyWord;
import com.duy.pascal.frontend.code_editor.completion.Patterns;
import com.duy.pascal.frontend.code_editor.editor_view.AutoIndentEditText;
import com.duy.pascal.frontend.code_editor.editor_view.HighlightEditor;
import com.js.interpreter.runtime_value.FunctionCall;
import com.js.interpreter.runtime_value.VariableAccess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to automatically correct some errors when compiling
 * Such as declare variable , type, function, ..
 * For example:
 * <code>
 * begin
 * a := 2;
 * end.
 * </code>
 * =>
 * <code>
 * var
 * a: integer; {or some other type}
 * begin
 * a := 2;
 * end;
 * </code>
 * <p>
 * Created by Duy on 23-May-17.
 */
public class AutoFixError {
    private static final String TAG = "AutoFix";
    private HighlightEditor editable;

    public AutoFixError(@NonNull HighlightEditor editText) {
        this.editable = editText;
    }

    /**
     * This method will be import new type for program
     * the {@link UnrecognizedTypeException} contains missing type.
     * <p>
     * First, we find the "type" keyword, if not found we will be create new keyword
     * Then, we insert a structure <code>"name" = "type"</code>
     */
    public void autoFixType(UnrecognizedTypeException e) {
        //don't work if has selection
        //sub string from 0 to postion error
        CharSequence text = getText(e);
        String type = e.missingType;
        String textToInsert;
        Matcher matcher = Patterns.TYPE.matcher(text);
        int insertPosition = 0;

        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = "\n" + "    " + type + " = %t ;";
        } else {
            /*
            if not found "type" keyword, insert new type keyword
            type    <== keyword type must be above var
                ....
            var
                ....
            */
            if ((matcher = Patterns.VAR.matcher(text)).find()) {
                insertPosition = matcher.start();
            }
            //if not found var keyword, insert "type" above "uses" keyword
            else if ((matcher = Patterns.USES.matcher(text)).find()) {
                insertPosition = matcher.end();
            }
            textToInsert = "\ntype\n" + "    " + type + " = %t ;\n";
        }

        matcher = Patterns.REPLACE_CURSOR.matcher(textToInsert);
        matcher.find();
        textToInsert = textToInsert.replaceAll("%\\w", "");

        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + matcher.start());


        //set suggest data
        editable.restoreAfterClick(KeyWord.DATA_TYPE);
    }

    private CharSequence getText(ParsingException e) {
        return editable.getText().subSequence(0, editable.getLayout().getLineStart(e.line.line) + e.line.column);
    }

    public void autoFixDefine(NoSuchFunctionOrVariableException e) {
        Log.d(TAG, "autoFixDefine() called with: e = [" + e + "]" + " " + e.getFitType());

        if (e.getFitType() == DefineType.DECLARE_VAR) {
            declareVar(e);
        } else if (e.getFitType() == DefineType.DECLARE_CONST) {
            declareConst(e);
        } else if (e.getFitType() == DefineType.DECLARE_FUNCTION) {
            declareFunction(e);
        }
    }

    /**
     * declare const, the const usually in the top of program, below "program" or "uses" keyword
     */
    private void declareConst(NoSuchFunctionOrVariableException e) {
        //sub string from 0 to postion error
        CharSequence text = getText(e);

        String textToInsert = "";
        int insertPosition = 0;
        String name = e.getName();

        Matcher matcher = Patterns.CONST.matcher(text);
        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = "\n" + "    " + name + " = %v ;";
        } else {
            if ((matcher = Patterns.PROGRAM.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.USES.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.TYPE.matcher(text)).find()) {
                insertPosition = matcher.start();
            }
            textToInsert = "\nconst \n" + AutoIndentEditText.TAB_CHARACTER + name + " = %v ;";
        }


        matcher = Patterns.REPLACE_CURSOR.matcher(textToInsert);
        matcher.find();
        textToInsert = textToInsert.replaceAll("%\\w", "");

        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + matcher.start());
    }

    private void declareFunction(NoSuchFunctionOrVariableException e) {
    }

    /**
     * match position and insert new variable
     */
    private void declareVar(NoSuchFunctionOrVariableException e) {

        //sub string from 0 to postion error
        CharSequence text = getText(e);

        String textToInsert = "";
        int insertPosition = 0;
        String name = e.getName();

        Matcher matcher = Patterns.VAR.matcher(text);
        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = AutoIndentEditText.TAB_CHARACTER + name + ": %t ;\n";
        } else {
            if ((matcher = Patterns.TYPE.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.USES.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.PROGRAM.matcher(text)).find()) {
                insertPosition = matcher.end();
            }
            textToInsert = "var\n" + AutoIndentEditText.TAB_CHARACTER + name + ": %t ;\n";
        }


        matcher = Patterns.REPLACE_CURSOR.matcher(textToInsert);
        matcher.find();
        textToInsert = textToInsert.replaceAll("%\\w", "");

        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + matcher.start());

        //set suggest data
        editable.restoreAfterClick(KeyWord.DATA_TYPE);

    }

    public void autoFixConvertType(UnConvertibleTypeException e) {

        CharSequence text = getText(e);

        if (e.targetValue instanceof VariableAccess) {
            String name = ((VariableAccess) e.targetValue).getName();
            Pattern pattern = Pattern.compile("\\b(var)(.*?)(" + name + ")\\s?:(.*?);",

                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                int start = matcher.start();
                Log.d(TAG, "autoFixConvertType: match at " + matcher);
                text = text.subSequence(start, matcher.end());
                pattern = Pattern.compile(name + "\\s?:(.*?);", Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(text);

                if (matcher.find()) {
                    String insertText = name + ": " + e.valueType.toString() + ";";

                    editable.getText().delete(start + matcher.start(), start + matcher.end());
                    editable.getText().insert(start + matcher.start(), insertText);
                    editable.setSelection(start + name.length() + 2, start + name.length() + 2 + e.valueType.toString().length());
                }
            }
        } else if (e.targetValue instanceof FunctionCall) {

        }
    }
}
