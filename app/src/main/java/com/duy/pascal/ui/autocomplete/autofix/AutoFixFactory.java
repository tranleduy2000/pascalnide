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

import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.define.MainProgramNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.define.TypeIdentifierExpectException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.VariableExpectedException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.ChangeValueConstantException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.ui.autocomplete.autofix.command.ChangeTypeConstant;
import com.duy.pascal.ui.autocomplete.autofix.command.ChangeTypeFunction;
import com.duy.pascal.ui.autocomplete.autofix.command.ChangeTypeVariable;
import com.duy.pascal.ui.autocomplete.autofix.command.DeclareConstant;
import com.duy.pascal.ui.autocomplete.autofix.command.DeclareFunction;
import com.duy.pascal.ui.autocomplete.autofix.command.DeclareProcedure;
import com.duy.pascal.ui.autocomplete.autofix.command.DeclareType;
import com.duy.pascal.ui.autocomplete.autofix.command.DeclareVariable;
import com.duy.pascal.ui.autocomplete.autofix.command.FixMissingEndToken;
import com.duy.pascal.ui.autocomplete.autofix.command.InsertMainProgram;
import com.duy.pascal.ui.autocomplete.autofix.command.InsertToken;
import com.duy.pascal.ui.autocomplete.autofix.command.TransformConstantToVariable;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.editor.view.LineUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getTextInLine;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Quick fix some syntax error
 * Created by Duy on 9/24/2017.
 */
public class AutoFixFactory {
    private static final String TAG = "AutoFixHelper";

    private AutoFixFactory() {
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
        return new DeclareType(exception);
    }

    @NonNull
    private static AutoFixCommand declareProcedure(UnknownIdentifierException exception) {
        return new DeclareProcedure(exception);
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
    private static void fixUnConvertType(@NonNull ArrayList<AutoFixCommand> toAdd,
                                         @NonNull UnConvertibleTypeException exception) {

        //identifier
        if (exception.getIdentifier() instanceof VariableAccess) {
            if (exception.getScope() instanceof FunctionDeclaration.FunctionExpressionContext) {
                Name funName = ((FunctionDeclaration.FunctionExpressionContext) exception.getScope()).function.getName();
                //this is function name
                if (funName.equals(((VariableAccess) exception.getIdentifier()).getName())) {
                    toAdd.add(changeTypeFunction(exception, funName, exception.getValueType()));

                } else {
                    toAdd.add(changeTypeVar(exception, (VariableAccess) exception.getIdentifier(), exception.getValueType()));
                }
            } else {
                toAdd.add(changeTypeVar(exception, (VariableAccess) exception.getIdentifier(), exception.getValueType()));
            }
        }

        if (exception.getIdentifier() instanceof ConstantAccess
                && ((ConstantAccess) exception.getIdentifier()).getName() != null) {
            toAdd.add(changeTypeConst(exception, (ConstantAccess) exception.getIdentifier(), exception.getValueType()));
        }

        //value
        if (exception.getValue() instanceof ConstantAccess) {
            ConstantAccess constant = (ConstantAccess) exception.getValue();
            if (constant.getName() != null) { //identifier
                toAdd.add(changeTypeConst(exception, constant, exception.getTargetType()));
            }
        }
        if (exception.getValue() instanceof VariableAccess) {
            if (exception.getScope() instanceof FunctionDeclaration.FunctionExpressionContext) {
                Name name = ((FunctionDeclaration.FunctionExpressionContext) exception.getScope()).function.getName();
                //this is function name
                if (name.equals(((VariableAccess) exception.getValue()).getName())) {
                    toAdd.add(changeTypeFunction(exception, name, exception.getTargetType()));
                } else {
                    toAdd.add(changeTypeVar(exception, (VariableAccess) exception.getValue(), exception.getTargetType()));
                }
            } else {
                toAdd.add(changeTypeVar(exception, (VariableAccess) exception.getValue(), exception.getTargetType()));
            }
        }

    }

    @NonNull
    private static AutoFixCommand insertToken(final MissingTokenException e) {
        return new InsertToken(e);
    }

    /**
     * Insert "end" into the final position of the editor
     */
    @NonNull
    private static AutoFixCommand fixGroupException(final GroupingException e) {
        return new FixMissingEndToken(e);
    }


    /**
     * This method will be declare const, the constant pascal
     * usually in the top of program, below "program" or "uses" keyword
     */
    @NonNull
    private static AutoFixCommand declareConst(final UnknownIdentifierException e) {
        return new DeclareConstant(e);
    }

    @NonNull
    private static AutoFixCommand declareFunction(UnknownIdentifierException e) {
        return new DeclareFunction(e);
    }

    /**
     * This method will be declare variable, the variable often below the
     * "const", "uses", "program" keyword,
     * First, match position of list keyword
     * Then insert new variable
     */
    @NonNull
    private static AutoFixCommand declareVar(@NonNull VariableExpectedException e) {
        String type = e.getExpectedType() != null ? e.getExpectedType().toString() : "";
        return declareVar(e.getScope().getStartPosition(), e.getLineNumber(), e.getName(), type, null);
    }

    /**
     * This method will be declare variable, the variable often below the
     * "const", "uses", "program" keyword,
     * First, match position of list keyword
     * Then insert new variable
     */
    @NonNull
    public static AutoFixCommand declareVar(UnknownIdentifierException e) {
        return declareVar(e.getScope().getStartPosition(), e.getLineNumber(), e.getName(),
                "",//unknown type
                null); //non init value
    }

    @NonNull
    public static AutoFixCommand declareVar(final LineInfo start, final LineInfo end, final Name name,
                                            final String type, final String initValue) {
        return new DeclareVariable(start, end, name, type, initValue);
    }


    /**
     * Change constant to variable
     * eg const a = 2; -> var a: integer = 2'
     */
    @NonNull
    private static AutoFixCommand changeConstToVar(final ChangeValueConstantException e) {
        return new TransformConstantToVariable(e);
    }

    @NonNull
    private static AutoFixCommand fixProgramNotFound() {
        return new InsertMainProgram();
    }

    /**
     * Change type of function from <code>valueType</code> to <code>name</code>
     *
     * @param functionName - name of function
     */
    @NonNull
    private static AutoFixCommand changeTypeFunction(@NonNull UnConvertibleTypeException exception,
                                                     @NonNull Name functionName, @NonNull Type newType) {
        return new ChangeTypeFunction(exception, functionName, newType);
    }

    /**
     * Change type of variable
     */
    @NonNull
    private static AutoFixCommand changeTypeVar(@NonNull UnConvertibleTypeException exception,
                                                @NonNull VariableAccess variable,
                                                @NonNull Type newType) {
        return new ChangeTypeVariable(exception, variable, newType);
    }

    /**
     * This method will be Change type constant to type of value
     * if constant is define with type
     * <p>
     * Example
     * const a: integer = 'adsda'; => change to string
     */
    @NonNull
    private static AutoFixCommand changeTypeConst(@NonNull UnConvertibleTypeException exception,
                                                  @NonNull ConstantAccess constant, @NonNull Type newType) {
        return new ChangeTypeConstant(exception, constant, newType);
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
            commands.add(declareProcedure((UnknownIdentifierException) e));
        } else if (e instanceof VariableExpectedException) {
            commands.add(declareVar((VariableExpectedException) e));

        } else if (e instanceof UnConvertibleTypeException) {
            fixUnConvertType(commands, (UnConvertibleTypeException) e);

        } else if (e instanceof MissingTokenException) {
            commands.add(insertToken((MissingTokenException) e));

        } else if (e instanceof ChangeValueConstantException) {
            commands.add(changeConstToVar((ChangeValueConstantException) e));

        } else if (e instanceof GroupingException) {
            commands.add(fixGroupException((GroupingException) e));

        } else if (e instanceof MainProgramNotFoundException) {
            commands.add(fixProgramNotFound());
        } else if (e instanceof ExpectedTokenException) {
            fixExpectToken(commands, (ExpectedTokenException) e);
        }
        return commands;
    }

    private static void fixExpectToken(ArrayList<AutoFixCommand> commands, ExpectedTokenException e) {

        String[] expected = e.getExpected();
        String current = e.getCurrent();
        for (String s : expected) {
            commands.add(fixExpectToken(current, s, true, e.getLineNumber()));
        }
        for (String s : expected) {
            commands.add(fixExpectToken(current, s, false, e.getLineNumber()));
        }
    }


    /**
     * replace current token by expect token exactly
     *
     * @param current - current token
     * @param expect  - token for replace
     * @param insert  - true if insert, <code>false</code> if replace
     */
    @NonNull
    private static AutoFixCommand fixExpectToken(final String current, final String expect,
                                                 final boolean insert, final LineInfo line) {
        return new AutoFixCommand() {
            @Override
            public void execute(EditorView editable) {

                //get text in lineInfo
                CharSequence textInLine = getTextInLine(editable, line.getLine(), line.getColumn());

                //position from 0 to current token
                int offset = LineUtils.getStartIndexAtLine(editable, line.getLine()) + line.getColumn();

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
                String str = insert ?
                        context.getString(R.string.insert_token_2, expect, current)
                        : context.getString(R.string.replace_token, current, expect);
                return highlight(context, str);
            }
        };
    }


}
