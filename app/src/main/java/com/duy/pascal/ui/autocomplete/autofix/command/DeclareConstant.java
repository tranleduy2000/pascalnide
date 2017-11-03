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
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.editor.view.EditorView;

import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Declare constant
 * eg.
 * <code>
 * begin writeln(a); end.
 * </code>
 * <p>
 * After
 * <code>
 * const a = 'string';
 * begin
 * writeln(a);
 * end.
 * </code>
 * <p>
 * Created by Duy on 11/2/2017.
 */
public class DeclareConstant implements AutoFixCommand {
    private static final String TAG = "DeclareConstant";
    private UnknownIdentifierException exception;

    public DeclareConstant(UnknownIdentifierException exception) {
        this.exception = exception;
    }

    @Override
    public void execute(EditorView editable) {
        /*ScriptSource source = exception.getScope().getRoot().getSource();
        String hash = source.getContent();
        if (!editable.getText().toString().equals(hash)) {
            DLog.d(TAG, "execute: content has been modified");
            return;
        }
        LineInfo start = exception.getScope().getStartPosition();
        LineInfo end;

        try {
            LinkedList<Token> list = source.toTokens();
            Token token = list.peekFirst();
            LineInfo line = token.getLineNumber();
            //find scope
            while (!list.isEmpty() && line.compareTo(start) < 0) {
                list.removeFirst();
                token = list.peekFirst();
                line = token.getLineNumber();
            }
            //find var
            while (!list.isEmpty() && !(list.peekFirst() instanceof ConstToken)) {
                list.removeFirst();
            }
            //ensure the first token is VarToken
            if (list.peekFirst() instanceof ConstToken) {
                Token constToken = list.peekFirst();

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
                code.append(mVariable.getName()).append(": ").append(newType).append(";");

                editable.disableTextWatcher();
                editable.getText().delete(text.getOffset(), text.getOffset() + text.length() - 1);
                editable.getText().insert(text.getOffset(), code);
                editable.setLineError(null);
                editable.updateTextHighlight();
                editable.enableTextWatcher();
                editable.showKeyboard();
            }
        } catch (IOException ignored) {
        }*/

        String textToInsert;
        int insertPosition = 0;
        Name name = exception.getName();
        textToInsert = String.format("const %s = ;\n", name);
        editable.getText().insert(insertPosition, textToInsert);
        editable.setSelection(insertPosition + textToInsert.length() - 2);
        editable.toast(R.string.enter_value_of_constant, name);
        editable.showKeyboard();
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        String str = context.getString(R.string.declare_constant_2, exception.getName().getOriginName());
        return highlight(context, str);
    }
}
