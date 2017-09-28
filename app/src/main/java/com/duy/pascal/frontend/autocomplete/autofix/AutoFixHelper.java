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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.Log;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.frontend.autocomplete.autofix.model.TextData;
import com.duy.pascal.frontend.autocomplete.completion.KeyWord;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.editor.view.AutoIndentEditText;
import com.duy.pascal.frontend.editor.view.EditorView;
import com.duy.pascal.frontend.editor.view.LineUtils;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.define.MainProgramNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.define.TypeIdentifierExpectException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.VariableIdentifierExpectException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.ChangeValueConstantException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.frontend.autocomplete.autofix.ChangeTypeHelper.changeTypeConst;
import static com.duy.pascal.frontend.autocomplete.autofix.ChangeTypeHelper.changeTypeFunction;
import static com.duy.pascal.frontend.autocomplete.autofix.ChangeTypeHelper.changeTypeVar;
import static com.duy.pascal.frontend.autocomplete.autofix.EditorUtil.getText;
import static com.duy.pascal.frontend.autocomplete.autofix.EditorUtil.getTextInLine;

/**
 * Quick fix some syntax error
 * Created by Duy on 9/24/2017.
 */
public class AutoFixHelper {
    private static final String TAG = "AutoFixHelper";

    private AutoFixHelper() {
    }

    /**
     * This method will be import new type for program
     * the {@link TypeIdentifierExpectException} contains missing type.
     * <p>
     * First, we find the "type" keyword, if not found we will be create new keyword
     * Then, we insert a structure <code>"name" = "type"</code>
     */
    @NonNull
    private static AutoFixCommand declareType(final TypeIdentifierExpectException exception) {
        return new AutoFixCommand() {

            @Override
            public void execute(EditorView editable) {
                //don't work if has selection
                //sub string from 0 to postion error
                TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());

                Name type = exception.getMissingType();
                String textToInsert;
                Matcher matcher = Patterns.TYPE.matcher(scope.getText());
                int insertPosition = 0;

                if (matcher.find()) {
                    insertPosition = matcher.end();
                    textToInsert = editable.getTabCharacter() + type + " = " + AutoIndentEditText.CURSOR + ";\n";
                } else {
                    /*
                    if not found "type" keyword, insert new type keyword
                    type    <== keyword type must be above var
                        ....
                    var
                        ....
                    */
                    if ((matcher = Patterns.PROGRAM.matcher(scope.getText())).find()) {
                        insertPosition = matcher.end();
                    } else if ((matcher = Patterns.VAR.matcher(scope.getText())).find()) {
                        insertPosition = matcher.start();
                    } else if ((matcher = Patterns.CONST.matcher(scope.getText())).find()) {
                        insertPosition = matcher.start();
                    } else if ((matcher = Patterns.USES.matcher(scope.getText())).find()) {
                        insertPosition = matcher.end();
                    }
                    textToInsert = "\ntype\n" + editable.getTabCharacter() + type + " = " + AutoIndentEditText.CURSOR + ";\n";
                }

                insertPosition += scope.getOffset();

                editable.disableTextWatcher();
                editable.getText().insert(insertPosition, textToInsert);
                editable.setSelection(insertPosition + textToInsert.length() - 3);
                editable.setSuggestData(KeyWord.DATA_TYPE);
                editable.enableTextWatcher();
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                String string = context.getString(R.string.declare_type, exception.getMissingType());
                return ExceptionManager.highlight(string);
            }
        };
    }

    @Nullable
    private static AutoFixCommand declareProcedure(UnknownIdentifierException exception) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {

            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                return context.getString(R.string.declare_function);
            }
        };
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
     */
    private static void fixUnConvertType(final ArrayList<AutoFixCommand> commands, UnConvertibleTypeException exception) {

        //identifier
        if (exception.getIdentifier() instanceof VariableAccess) {
            if (exception.getScope() instanceof FunctionDeclaration.FunctionExpressionContext) {
                Name funName = ((FunctionDeclaration.FunctionExpressionContext) exception.getScope()).function.getName();
                //this is function name
                if (funName.equals(((VariableAccess) exception.getIdentifier()).getName())) {
                    commands.add(changeTypeFunction(exception, funName, exception.getValueType()));

                } else {
                    commands.add(changeTypeVar(exception, (VariableAccess) exception.getIdentifier(), exception.getValueType()));
                }
            } else {
                commands.add(changeTypeVar(exception, (VariableAccess) exception.getIdentifier(), exception.getValueType()));
            }
        }

        if (exception.getIdentifier() instanceof ConstantAccess
                && ((ConstantAccess) exception.getIdentifier()).getName() != null) {
            commands.add(changeTypeConst(exception, (ConstantAccess) exception.getIdentifier(), exception.getValueType()));
        }

        //value
        if (exception.getValue() instanceof ConstantAccess
                && ((ConstantAccess) exception.getValue()).getName() != null) {
            commands.add(changeTypeConst(exception, (ConstantAccess) exception.getValue(), exception.getTargetType()));
        }
        if (exception.getValue() instanceof VariableAccess) {
            if (exception.getScope() instanceof FunctionDeclaration.FunctionExpressionContext) {
                Name name = ((FunctionDeclaration.FunctionExpressionContext) exception.getScope()).function.getName();
                //this is function name
                if (name.equals(((VariableAccess) exception.getValue()).getName())) {
                    commands.add(changeTypeFunction(exception, name, exception.getTargetType()));
                } else {
                    commands.add(changeTypeVar(exception, (VariableAccess) exception.getValue(), exception.getTargetType()));
                }
            } else {
                commands.add(changeTypeVar(exception, (VariableAccess) exception.getValue(), exception.getTargetType()));
            }
        }

    }

    @NonNull
    private static AutoFixCommand insertToken(final MissingTokenException e) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                LineInfo line = e.getLineInfo();
                int start = LineUtils.getStartIndexAtLine(editable, line.getLine()) + line.getColumn();
                String insertText = e.getMissingToken();
                editable.getText().insert(start, insertText);
                editable.setSelection(start, insertText.length() + start);
                editable.showKeyboard();
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                String insertText = e.getMissingToken();
                return ExceptionManager.highlight(context.getString(R.string.insert_token, insertText));
            }
        };
    }

    /**
     * Insert "end" into the final position of the editor
     */
    @Nullable
    private static AutoFixCommand fixGroupException(final GroupingException e) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                if (e.getExceptionTypes() == GroupingException.Type.UNFINISHED_BEGIN_END) {
                    String text = "\nend";
                    editable.getText().insert(editable.length(), text); //insert

                    //select the "end" token and show keyboard
                    //don't selected newline character
                    editable.setSelection(editable.length() - text.length() + 1, editable.length());
                    editable.showKeyboard();
                }
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                return context.getString(R.string.add_end_at_the_end_of_program);
            }
        };
    }


    /**
     * This method will be declare const, the constant pascal
     * usually in the top of program, below "program" or "uses" keyword
     */
    @NonNull
    private static AutoFixCommand declareConst(final UnknownIdentifierException e) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                //sub string from 0 to position error
                TextData text = getText(editable, e.getScope().getStartPosition(), e.getLineInfo());

                String textToInsert = "";
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
                return context.getString(R.string.declare_constant_2, e.getName().getOriginName());
            }
        };
    }

    @NonNull
    private static AutoFixCommand declareFunction(UnknownIdentifierException e) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {

            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                return context.getString(R.string.declare_function);
            }
        };
    }

    /**
     * This method will be declare variable, the variable often below the
     * "const", "uses", "program" keyword,
     * First, match position of list keyword
     * Then insert new variable
     */
    @Nullable
    private static AutoFixCommand declareVar(VariableIdentifierExpectException e) {
        LineInfo[] range = {e.getScope().getStartPosition(), e.getLineInfo()};
        String type = e.getExpectedType() != null ? e.getExpectedType().toString() : "";
        return declareVar(range, e.getName(), type, null);
    }

    /**
     * This method will be declare variable, the variable often below the
     * "const", "uses", "program" keyword,
     * First, match position of list keyword
     * Then insert new variable
     */
    @Nullable
    private static AutoFixCommand declareVar(UnknownIdentifierException e) {
        return declareVar(new LineInfo[]{e.getScope().getStartPosition(), e.getLineInfo()},
                e.getName(),
                "",//unknown type
                null); //non init value
    }

    @Nullable
    private static AutoFixCommand declareVar(LineInfo[] lines, Name name, String type, String initValue) {
        if (lines.length != 2) {
            DLog.e(TAG, "The length line array must be 2");
            return null;
        }
        return declareVar(lines[0], lines[1], name, type, initValue);
    }

    @NonNull
    private static AutoFixCommand declareVar(final LineInfo start, final LineInfo end, final Name name,
                                             final String type, final String initValue) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                declareVar(getText(editable, start, end), name, type, initValue).execute(editable);
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                String str = context.getString(R.string.declare_variable_2, name, type);
                return ExceptionManager.highlight(new SpannableString(str));
            }
        };
    }

    /**
     * Declare variable with name and type
     *
     * @param scope     - scope of variable
     * @param name      - the name of variable will be declared
     * @param type      - the type of variable will be declared
     * @param initValue - init value
     */
    private static AutoFixCommand declareVar(final TextData scope, final Name name,
                                             final String type, final String initValue) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                String textToInsert;
                int insertPosition = 0;
                int startSelect;
                int endSelect;

                Matcher matcher = Patterns.VAR.matcher(scope.getText());
                if (matcher.find()) {
                    insertPosition = matcher.end();
                    textToInsert = "\n" + AutoIndentEditText.TAB_CHARACTER + name + ": ";

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
                    textToInsert = "\nvar\n" + AutoIndentEditText.TAB_CHARACTER + name + ": ";

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
                editable.enableTextWatcher();
            }


            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                String str = context.getString(R.string.declare_variable_2, name, type);
                return ExceptionManager.highlight(new SpannableString(str));
            }
        };
    }


    /**
     * Change constant to variable
     * eg const a = 2; -> var a: integer = 2'
     */
    @NonNull
    private static AutoFixCommand changeConstToVar(final ChangeValueConstantException e) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                Log.d(TAG, "changeConstToVar() called with: editable = [" + editable + "]");

                TextData region = getText(editable, e.getScope().getStartPosition(), e.getLineInfo());
                ConstantAccess constant = e.getConst();
                //const a = 2;
                Pattern pattern = Pattern.compile(
                        "(^const\\s+|\\s+const\\s+)" + //1
                                "(" + Pattern.quote(constant.getName() + "") + ")" + //2
                                "(\\s?)" + //3
                                "(=)" +//4
                                "(.*?)" +//5
                                "(;)",//6
                        Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(region.getText());
                if (matcher.find()) {
                    DLog.d(TAG, "changeConstToVar: " + matcher);
                    int start = matcher.start(2) + region.getOffset();
                    start = Math.max(0, start);
                    int end = matcher.end(6) + region.getOffset();

                    editable.disableTextWatcher();
                    editable.getText().delete(start, end);
                    region.getText().delete(matcher.start(2), matcher.end(6));
                    editable.enableTextWatcher();

                    AutoFixCommand declareVar = declareVar(region,
                            constant.getName(), //name
                            constant.getRuntimeType(null).declType.toString(), //type
                            constant.toCode());
                    declareVar.execute(editable);
                } else {
                    //const a: integer = 2;
                    pattern = Pattern.compile(
                            "(^const\\s+|\\s+const\\s+)" + //1
                                    "(" + Pattern.quote(constant.getName() + "") + ")" + //2
                                    "(\\s?)" + //3
                                    "(:)" + //4
                                    "(\\s?)" +//5
                                    "(.*?)" +//6 type
                                    "(=)" + //7
                                    "(.*?)" +//8
                                    "(;)" //9
                            , Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

                    matcher = pattern.matcher(region.getText());
                    if (matcher.find()) {
                        int start = matcher.start(2) + region.getOffset();
                        start = Math.max(0, start);
                        int end = matcher.end(9) + region.getOffset();

                        editable.disableTextWatcher();
                        editable.getText().delete(start, end);
                        region.getText().delete(matcher.start(2), matcher.end(9));
                        editable.enableTextWatcher();

                        AutoFixCommand declareVar = declareVar(region,
                                constant.getName(),  //name
                                constant.getRuntimeType(null).declType.toString(), //type
                                constant.toCode());
                        declareVar.execute(editable);//initialization value
                    }
                }
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                ConstantAccess constant = e.getConst();
                String string = context.getString(R.string.change_const_to_var, constant.getName(),
                        constant.getRuntimeType(null).getRawType().toString(), constant.getValue().toString());
                return ExceptionManager.highlight(string);
            }
        };

    }

    @NonNull
    private static AutoFixCommand fixProgramNotFound() {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {
                Log.d(TAG, "fixProgramNotFound() called with: editable = [" + editable + "]");
                editable.disableTextWatcher();

                String tabCharacter = editable.getTabCharacter();
                editable.getText().insert(editable.length(), "\nbegin\n" + tabCharacter + "\nend.\n");
                editable.setSelection(editable.length() - "\nend.\n".length());

                editable.enableTextWatcher();
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                return ExceptionManager.highlight(context.getString(R.string.add_begin_end));
            }
        };
    }

    @NonNull
    public static ArrayList<AutoFixCommand> buildCommands(Exception e) {
        ArrayList<AutoFixCommand> commands = new ArrayList<>();
        if (e instanceof TypeIdentifierExpectException) {
            commands.add(declareType((TypeIdentifierExpectException) e));
        } else if (e instanceof UnknownIdentifierException) {
            //add missing var
            commands.add(declareVar((UnknownIdentifierException) e));
            //add missing const
            commands.add(declareConst((UnknownIdentifierException) e));
            //add missing function
            commands.add(declareFunction((UnknownIdentifierException) e));
            //add missing procedure
//            commands.add(declareProcedure((UnknownIdentifierException) exception));
        } else if (e instanceof VariableIdentifierExpectException) {
            commands.add(declareVar((VariableIdentifierExpectException) e));

        } else if (e instanceof UnConvertibleTypeException) {
            UnConvertibleTypeException exception = (UnConvertibleTypeException) e;
            fixUnConvertType(commands, exception);
        } else if (e instanceof MissingTokenException) {
            commands.add(insertToken((MissingTokenException) e));

        } else if (e instanceof ChangeValueConstantException) {
            commands.add(changeConstToVar((ChangeValueConstantException) e));

        } else if (e instanceof GroupingException) {
            commands.add(fixGroupException((GroupingException) e));

        } else if (e instanceof MainProgramNotFoundException) {
            commands.add(fixProgramNotFound());
        }
        return commands;
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
    @NonNull
    public static AutoFixCommand fixExpectToken(final String current, final String expect,
                                                final boolean insert, final int line, final int column) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {

                DLog.d(TAG, "fixExpectToken() called with: current = [" + current + "], expect = [" + expect + "], insert = [" + insert + "], line = [" + line + "], column = [" + column + "]");
                //get text in lineInfo
                CharSequence textInLine = getTextInLine(editable, line, column);

                //position from 0 to current token
                int offset = LineUtils.getStartIndexAtLine(editable, line) + column;

                //find token
                Pattern pattern = Pattern.compile("(" + Pattern.quote(current) + ")"); //current token
                Matcher matcher = pattern.matcher(textInLine);
                if (matcher.find()) {

                    int start = matcher.start();
                    int end = matcher.end();

                    editable.disableTextWatcher();
                    //insert or replace other token
                    if (!insert) {
                        editable.getText().replace(offset + start, offset + start + end, expect);
                    } else {
                        String insert = " " + expect + " ";
                        editable.getText().insert(offset + start, insert);
                    }
                    editable.setSelection(offset + start, offset + start + expect.length());
                    editable.showKeyboard();
                    editable.enableTextWatcher();
                }
            }

            @NonNull
            @Override
            public CharSequence getTitle(Context context) {
                return null;
            }
        };
    }


}
