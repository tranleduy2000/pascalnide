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
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.VarToken;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.EditorUtil;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.code.ExceptionManager;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;


/**
 * Change type of variable when wrong type
 * eg.
 * <code>
 * Var i : string; begin i := 2; end.
 * </code>
 * Change to
 * <code>
 * Var i: Integer; begin i := 2; end.
 * </code>
 * Created by Duy on 11/2/2017.
 */
public class ChangeTypeVariable implements AutoFixCommand {
    private static final String TAG = "ChangeTypeVariable";
    private final UnConvertibleTypeException exception;
    private final VariableAccess mVariable;
    private final Type newType;

    public ChangeTypeVariable(UnConvertibleTypeException exception, VariableAccess variable, Type newType) {
        this.exception = exception;
        this.mVariable = variable;
        this.newType = newType;
    }

    @Override
    public void execute(EditorView editable) {
        ScriptSource source = exception.getScope().getRoot().getSource();
        String hash = source.getContent();
        if (!editable.getText().toString().equals(hash)) {
            DLog.d(TAG, "execute: content has been modified");
            return;
        }
        LineInfo start = exception.getScope().getStartPosition();
        LineInfo end = exception.getLineInfo();

        try {
            LinkedList<Token> list = source.toTokens();
            Token token = list.peekFirst();
            LineInfo line = token.getLineNumber();
            //find scope
            while (line.compareTo(start) < 0) {
                list.removeFirst();
                token = list.peekFirst();
                line = token.getLineNumber();
            }
            //find var
            while (!(list.peekFirst() instanceof VarToken)) {
                list.removeFirst();
            }
            //ensure the first token is VarToken
            list.removeFirst();
            ArrayList<WordToken> variables = new ArrayList<>();
            Token type = null;
            start = null;
            end = null;
            whileloop:
            while (list.peekFirst() instanceof WordToken) {
                WordToken name = (WordToken) list.removeFirst();
                variables.add(name);
                if (start == null) {
                    start = name.getLineNumber();
                }
                if (list.peekFirst() instanceof CommaToken) { //,
                    list.removeFirst();
                } else if (list.peekFirst() instanceof ColonToken) { //:
                    list.removeFirst();
                    type = list.removeFirst(); //type
                    end = list.removeFirst().getLineNumber();  //remove semicolon;
                    for (WordToken wordToken : variables) {
                        if (wordToken.getName().equals(mVariable.getName())) {
                            variables.remove(wordToken);
                            break whileloop;
                        }
                    }
                    variables.clear();
                    type = null;
                    start = null;
                    end = null;
                }
            }
            TextData text = getText(editable, start, end);
            System.out.println("variables = " + variables);
            System.out.println("type = " + type);
            System.out.println("start = " + start);
            System.out.println("end = " + end);
            System.out.println("text = " + text);
            String indent = EditorUtil.getIndentLine(hash, text.getOffset());
            StringBuilder code = new StringBuilder();
            for (WordToken variable : variables) {
                code.append(variable).append(":").append(type).append(";\n").append(indent);
            }
            code.append(mVariable.getName()).append(": ").append(newType).append(";\n");

            editable.disableTextWatcher();
            editable.getText().delete(text.getOffset(), text.getOffset() + text.length() - 1);
            editable.getText().insert(text.getOffset(), code);
            editable.updateTextHighlight();
            editable.enableTextWatcher();
            editable.showKeyboard();
        } catch (IOException ignored) {
        }

        /*//get a part of text
        TextData scope = getText(editable, exception.getScope().getStartPosition(), exception.getLineInfo());
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
        }*/
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String string = context.getString(R.string.change_var_type,
                mVariable.getName().toString(),
                newType.getName().toString());
        return ExceptionManager.highlight(context, string);
    }
}
