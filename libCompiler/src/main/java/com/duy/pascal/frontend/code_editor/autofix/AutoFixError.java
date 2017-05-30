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

package com.duy.pascal.frontend.code_editor.autofix;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Layout;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.parse_exception.define.UnrecognizedTypeException;
import com.duy.pascal.backend.parse_exception.missing.MissingTokenException;
import com.duy.pascal.backend.parse_exception.value.ChangeValueConstantException;
import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.ast.runtime_value.value.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.VariableAccess;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.code_editor.completion.KeyWord;
import com.duy.pascal.frontend.code_editor.completion.Patterns;
import com.duy.pascal.frontend.code_editor.editor_view.AutoIndentEditText;
import com.duy.pascal.frontend.code_editor.editor_view.HighlightEditor;
import com.duy.pascal.frontend.code_editor.editor_view.LineUtils;

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
    public void autoFixMissingType(UnrecognizedTypeException e) {
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
            if ((matcher = Patterns.PROGRAM.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.VAR.matcher(text)).find()) {
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

    /**
     * @param e - include lineInfo error
     * @return the part of text start a 0 and end at e.lineInfo
     */
    private CharSequence getText(ParsingException e) {
        return editable.getText().subSequence(0, editable.getLayout().getLineEnd(e.getLineInfo().getLine()));
    }

    private CharSequence getText(int line) {
        return editable.getText().subSequence(0, editable.getLayout().getLineEnd(line));
    }

    /**
     * This method will be add missing define, such as variable,
     * constant, function or procedure
     */
    public void autoFixMissingDefine(NoSuchFunctionOrVariableException e) {
        DLog.d(TAG, "autoFixMissingDefine() called with: e = [" + e + "]" + " " + e.getFitType());
        if (e.getFitType() == DefineType.DECLARE_VAR) {
            //add missing var
            declareVar(e);
        } else if (e.getFitType() == DefineType.DECLARE_CONST) {
            //add missing const
            declareConst(e);
        } else if (e.getFitType() == DefineType.DECLARE_FUNCTION) {
            //add missing function
            declareFunction(e);
        } else if (e.getFitType() == DefineType.DECLARE_PROCEDURE) {
            //add missing procedure
        }
    }

    /**
     * This method will be declare const, the constant pascal
     * usually in the top of program, below "program" or "uses" keyword
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
     * This method will be declare variable, the variable often below the
     * "const", "uses", "program" keyword,
     * First, match position of list keyword
     * Then insert new variable
     */
    private void declareVar(NoSuchFunctionOrVariableException e) {
        declareVar(e.getLineInfo(), e.getName(),
                "",//unknown type
                null); //non init value
    }

    private boolean declareVar(LineInfo endLine, String name, String type, String initValue) {
        //sub string from 0 to position error
        CharSequence text = getText(endLine.getLine());

        String textToInsert = "";
        int insertPosition = 0;
        int startSelect;
        int endSelect;

        Matcher matcher = Patterns.VAR.matcher(text);
        if (matcher.find()) {
            insertPosition = matcher.end();
            textToInsert = AutoIndentEditText.TAB_CHARACTER + name + ": ";

            startSelect = textToInsert.length();
            endSelect = startSelect + type.length();

            textToInsert += type + (initValue != null ? " = " + initValue : "") + ";\n";
        } else {
            if ((matcher = Patterns.TYPE.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.USES.matcher(text)).find()) {
                insertPosition = matcher.end();
            } else if ((matcher = Patterns.PROGRAM.matcher(text)).find()) {
                insertPosition = matcher.end();
            }
            textToInsert = "\nvar\n" + AutoIndentEditText.TAB_CHARACTER + name + ": ";

            startSelect = textToInsert.length();
            endSelect = startSelect + type.length();

            textToInsert += type + (initValue != null ? " = " + initValue : "") + ";\n";
        }

        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + startSelect, insertPosition + endSelect);

        //set suggest data
        editable.restoreAfterClick(KeyWord.DATA_TYPE);

        editable.showKeyboard();


        return true;
    }

    /**
     * Auto wrong type
     * For example
     * <code>
     * var c: integer;
     * begin
     * c := 'hello';            <=== this is wrong type
     * end.
     * </code>
     * <p>
     * This method will be match position of variable or function and change to
     * <code>
     * var c: string;             <== change to String
     * begin
     * c := 'hello';
     * end.
     * </code>
     *
     * @param e
     */
    public void autoFixUnConvertType(UnConvertibleTypeException e) {
        //get a part of text
        CharSequence text = getText(e);
        if (e.getIdentifier() instanceof VariableAccess) {
            if (e.getContext() instanceof FunctionDeclaration.FunctionExpressionContext) {
                String name = ((FunctionDeclaration.FunctionExpressionContext) e.getContext()).function.getName();
                //this is function name
                if (name.equalsIgnoreCase(((VariableAccess) e.getIdentifier()).getName())) {
                    e.setContext(null); //leak
                    changeTypeFunction(name, text, e.getValueType());
                } else {
                    changeTypeVar(text, (VariableAccess) e.getIdentifier(), e.getValueType());
                }
            } else {
                changeTypeVar(text, (VariableAccess) e.getIdentifier(), e.getValueType());
            }
        } else if (e.getIdentifier() instanceof ConstantAccess) {
            changeTypeConst(text, (ConstantAccess) e.getIdentifier(), e.getValueType());

        } else if (e.getValue() instanceof VariableAccess) {
            if (e.getContext() instanceof FunctionDeclaration.FunctionExpressionContext) {
                String name = ((FunctionDeclaration.FunctionExpressionContext) e.getContext()).function.getName();
                //this is function name
                if (name.equalsIgnoreCase(((VariableAccess) e.getValue()).getName())) {
                    e.setContext(null); //leak
                    changeTypeFunction(name, text, e.getTargetType());
                } else {
                    changeTypeVar(text, (VariableAccess) e.getValue(), e.getTargetType());
                }
            } else {
                changeTypeVar(text, (VariableAccess) e.getValue(), e.getTargetType());
            }

        } else if (e.getValue() instanceof ConstantAccess) {
            changeTypeConst(text, (ConstantAccess) e.getValue(), e.getTargetType());

        }
    }

    /**
     * This method will be Change type constant to type of value
     * if constant is define with type
     *
     * Example
     * const a: integer = 'adsda'; => change to string
     */
    private void changeTypeConst(CharSequence text, ConstantAccess identifier, DeclaredType valueType) {
        DLog.d(TAG, "autoFixUnConvertType: constant " + identifier);

        if (identifier.getName() == null) { //can not replace because it is not a identifier
            DLog.d(TAG, "changeTypeConst: this is not identifier");
            return;
        }

        String name = identifier.getName();
        Pattern pattern = Pattern.compile("(^const\\s+|\\s+const\\s+)" + //match "const"  //1
                        "(.*?)" + //other const                                  //2
                        "(" + name + ")" + //name of const                       //3
                        "(\\s?)" +//one or more white space                         //4
                        "(:)" + //colon                                             //5
                        "(.*?)" + //type????                                        //6
                        "(=)" +
                        "(.*?)" +
                        "(;)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            DLog.d(TAG, "autoFixUnConvertType: match " + matcher);
            final int start = matcher.start(6);
            int end = matcher.end(6);

            final String insertText = valueType.toString();
            editable.getEditableText().replace(start, end, insertText);
            editable.post(new Runnable() {
                @Override
                public void run() {
                    editable.setSelection(start, start + insertText.length());
                }
            });
            editable.showKeyboard();
        }
    }

    /**
     * @param name - name of function
     * @param text - a part text of the edit start at 0 and end at lineInfo where then function place
     */
    private void changeTypeFunction(final String name, CharSequence text, DeclaredType valueType) {
        Pattern pattern = Pattern.compile(
                "(^function\\s+|\\s+function\\s+)" + //function token //1
                        "(" + name + ")" + //name of function         //2
                        "(\\s?)" + //white space                      //3
                        "(:)" +                                       //4
                        "(.*?)" + //type of function                  //5
                        ";"); //end                                   //6
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            DLog.d(TAG, "changeTypeFunction: match " + matcher);
            final int start = matcher.start(5);
            final int end = matcher.end(5);

            final String insertText = valueType.toString();
            editable.getEditableText().replace(start, end, insertText);
            editable.post(new Runnable() {
                @Override
                public void run() {
                    editable.setSelection(start, start + insertText.length());
                }
            });
            editable.showKeyboard();
        } else {
            DLog.d(TAG, "changeTypeFunction: can not find " + pattern);
        }
    }

    private void changeTypeVar(CharSequence text, VariableAccess identifier, DeclaredType valueType) {
        DLog.d(TAG, "autoFixUnConvertType: variable");
        final String name = ((VariableAccess) identifier).getName();
        Pattern pattern = Pattern.compile("(^var\\s+|\\s+var\\s+)" + //match "var"  //1
                        "(.*?)" + //other variable                                  //2
                        "(" + name + ")" + //name of variable                       //3
                        "(\\s?)" +//one or more white space                         //4
                        "(:)" + //colon                                             //5
                        "(.*?)" + //any type                                        //6
                        "(;)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(text);
        DLog.d(TAG, "autoFixUnConvertType: " + text);

        if (matcher.find()) {
            DLog.d(TAG, "autoFixUnConvertType: match " + matcher);
            final int start = matcher.start(6);
            int end = matcher.end(6);

            final String insertText = valueType.toString();
            editable.getEditableText().replace(start, end, insertText);
            editable.post(new Runnable() {
                @Override
                public void run() {
                    editable.setSelection(start, start + insertText.length());
                }
            });
            editable.showKeyboard();
        } else {
            DLog.d(TAG, "autoFixUnConvertType: can not find " + pattern);
        }
    }

    /**
     * replace current token by expect token exactly
     *
     * @param current - current token
     * @param expect  - token for replace
     * @param insert  - true if insert, <code>false</code> if replace
     * @param line    - current lineInfo
     * @param column  - start at column of @lineInfo
     */
    public void fixExpectToken(String current, String expect, boolean insert, int line, int column) {
        DLog.d(TAG, "fixExpectToken() called with: current = [" + current + "], expect = [" + expect + "], insert = [" + insert + "], lineInfo = [" + line + "], column = [" + column + "]");
        //get text in lineInfo
        CharSequence textInLine = getTextInLine(line, column);

        //position from 0 to current token
        int offset = LineUtils.getStartIndexAtLine(editable, line) + column;

        //find token
        Pattern pattern = Pattern.compile("(" + current + ")"); //current token
        Matcher matcher = pattern.matcher(textInLine);
        if (matcher.find()) {

            int start = matcher.start();
            int end = matcher.end();

            //insert or replace other token
            Editable text = editable.getText();
            if (!insert) {
                text.replace(offset + start, offset + start + end, expect);
            } else {
                expect = " " + expect + " ";
                text.insert(offset + start, expect);

            }
            editable.setSelection(offset + start, offset + start + expect.length());
            editable.showKeyboard();
        }
    }

    /**
     * get text in lineInfo
     */
    private CharSequence getTextInLine(int line, int column) {
        Editable text = editable.getText();
        Layout layout = editable.getLayout();
        if (layout != null) {
            int lineStart = layout.getLineStart(line);
            int lineEnd = layout.getLineEnd(line);
            lineStart = lineStart + column;
            if (lineStart > text.length()) lineStart = text.length();
            if (lineStart > lineEnd) lineStart = lineEnd;
            return text.subSequence(lineStart, lineEnd);
        }
        return "";
    }

    public void insertToken(MissingTokenException e) {
        final int start = LineUtils.getStartIndexAtLine(editable, e.getLineInfo().getLine()) + e.getLineInfo().getColumn();
        final String insertText = e.getMissingToken();
        editable.getEditableText().insert(start, insertText);
        editable.post(new Runnable() {
            @Override
            public void run() {
                editable.setSelection(start, insertText.length() + start);
            }
        });
        editable.showKeyboard();
    }

    public void changeConstToVar(ChangeValueConstantException e) {
        DLog.d(TAG, "changeConstToVar: " + e);

        CharSequence text = getText(e);
/*
        //const a = 1;
        Pattern pattern = Pattern.compile("(^(const\\+)|(\\s+const\\s+))" +
                "(" + e.getName() + ")" +
                "(\\s?)(=)(\\s?)(.*?)(;)");*/

        Pattern pattern;
        //const a = 2;
        //      b = 3; <== change b to var
        ConstantAccess<Object> constant = e.getConst();
        pattern = Pattern.compile(
                "(^const\\s+|\\s+const\\s+)" + //1
                        "(" + constant.getName() + ")" + //2
                        "(\\s?)" + //3
                        "(=)" +//4
                        "(.*?)" +//5
                        "(;)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            DLog.d(TAG, "changeConstToVar: " + matcher);
            int start = matcher.start(2);
            int end = matcher.end(6);
            editable.getEditableText().delete(start, end);
            declareVar(e.getLineInfo(), constant.getName(), constant.getType(null).declType.toString(),
                    constant.getValue().toString());
        } else {
            pattern = Pattern.compile(
                    "(^const\\s+|\\s+const\\s+)" + //1
                            "(" + constant.getName() + ")" + //2
                            "(\\s?)" + //3
                            "(:)" + //4
                            "(\\s?)" +//5
                            "([a-zA-z0-9]?)" +//5 type
                            "(=)" + //6
                            "(.?*)" +//7
                            "(;)" //8
                    , Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

            matcher = pattern.matcher(text);
            int start = matcher.start(2);
            int end = matcher.end(8);

            editable.getEditableText().delete(start, end);
            declareVar(e.getLineInfo(), constant.getName(), constant.getType(null).declType.toString(),
                    constant.getValue().toString());

        }
    }


}
