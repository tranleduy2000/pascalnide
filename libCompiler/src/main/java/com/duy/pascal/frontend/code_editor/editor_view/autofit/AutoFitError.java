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
import android.text.Editable;

import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.define.UnrecognizedTypeException;
import com.duy.pascal.frontend.code_editor.completion.KeyWord;
import com.duy.pascal.frontend.code_editor.completion.Patterns;
import com.duy.pascal.frontend.code_editor.editor_view.AutoIndentEditText;
import com.duy.pascal.frontend.code_editor.editor_view.HighlightEditor;

import java.util.regex.Matcher;

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
public class AutoFitError {
    private static final String TAG = "AutoFit";
    private HighlightEditor editable;

    public AutoFitError(@NonNull HighlightEditor editText) {
        this.editable = editText;
    }

    /**
     * This method will be import new type for program
     * the {@link UnrecognizedTypeException} contains missing type.
     * <p>
     * First, we find the "type" keyword, if not found we will be create new keyword
     * Then, we insert a structure <code>"name" = "type"</code>
     */
    public void autoFitType(UnrecognizedTypeException e) {
        //don't work if has selection
        if (editable.hasSelection()) return;

        Editable text = editable.getEditableText();
        String type = e.missingType;
        Matcher matcher = Patterns.TYPE.matcher(text);
        if (matcher.find()) {
            int insertPosition = matcher.end();
            String textToInsert = "\n" + "    " + type + " =  %a ;";
            editable.getText().insert(insertPosition, textToInsert);
            editable.setSelection(insertPosition + textToInsert.length());
        } else {
            /*
            if not found "type" keyword, insert new type keyword
            type    <== keyword type must be above var
                ....
            var
                ....
            */
            int insertPosition = -1;
            if ((matcher = Patterns.VAR.matcher(text)).find()) {
                insertPosition = matcher.start();
            }
            //if not found var keyword, insert "type" above "uses" keyword
            else if ((matcher = Patterns.USES.matcher(text)).find()) {
                insertPosition = matcher.start();
            }
            String textToInsert = "\ntype\n" + "    " + type + "=   ;\n";
            //if not found uses keyword
            if (insertPosition != -1) {
                editable.getText().insert(insertPosition, textToInsert);
                editable.setSelection(insertPosition + textToInsert.length());
            } else {
                insertPosition = 0; //reset cursor at the top of the editor
                editable.getText().insert(insertPosition, textToInsert);
                editable.setSelection(insertPosition + textToInsert.length());
            }
        }
    }

    public void autoFitDefine(NoSuchFunctionOrVariableException e) {
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
        Editable text = editable.getText();
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
                insertPosition = matcher.end();
            }
            textToInsert = "const \n" + AutoIndentEditText.TAB_CHARACTER + name + " = %v ;";
        }


        matcher = Patterns.REPLACE_CURSOR.matcher(textToInsert);
        matcher.find();

        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + matcher.start(), insertPosition + matcher.end());
    }

    private void declareFunction(NoSuchFunctionOrVariableException e) {

    }

    private void declareVar(NoSuchFunctionOrVariableException e) {

        Editable text = editable.getText();
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
        editable.restoreAfterClick(KeyWord.DATA_TYPE);

    }

}
