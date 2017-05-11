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

package com.js.interpreter.ast.instructions.with_statement;

import android.util.Log;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.CustomType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FieldAccess;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;

public class WithDeclaration {
    private static final String TAG = "WithDeclaration";

    public ExpressionContextMixin declarations;
    public Executable instructions;
    public LineInfo line;
    public ArrayList<ReturnValue> fields = new ArrayList<>();
    public ArrayList<VariableDeclaration> variableDeclarations = new ArrayList<>();
    public ReturnValue[] arguments;

    public WithDeclaration(ExpressionContext parent, GrouperToken grouperToken) throws ParsingException {
        this.declarations = new WithExpressionContext(this, parent);
        this.line = grouperToken.peek().lineInfo;

        getReferenceVariables(grouperToken, parent);
        for (ReturnValue argument : arguments) {
            if (argument.getType(parent).declType instanceof RecordType) {
                CustomType customType = (CustomType) argument.getType(parent).declType;
                for (VariableDeclaration variableDeclaration : customType.variableDeclarations) {
                    FieldAccess fieldAccess = new FieldAccess(argument, variableDeclaration.name(),
                            variableDeclaration.getLineNumber());
                    fields.add(fieldAccess);
                    variableDeclarations.add(variableDeclaration);
                }

            }
        }
        if (grouperToken.peek() instanceof DoToken) {
            grouperToken.take();
        }
        instructions = grouperToken.getNextCommand(declarations);
    }

    public ReturnValue generate() {
        return new WithCall(this, fields, line);
    }

    public void execute(VariableContext parentcontext,
                        RuntimeExecutable<?> main)
            throws RuntimePascalException {
        if (this.declarations.root() instanceof Library) {
            parentcontext = main.getLibrary((Library) declarations.root());
        }
        new WithOnStack(parentcontext, main, this).execute();
    }

    private void getReferenceVariables(GrouperToken grouperToken, ExpressionContext parent)
            throws ParsingException { // need
        ArrayList<VariableDeclaration> list = new ArrayList<>();
        Token next;
        do {
            next = grouperToken.take();
            if (next instanceof WordToken) {
                String name = ((WordToken) next).name;
                VariableDeclaration variable = parent.getVariableDefinition(name);
                if (variable == null) {
                    throw new NoSuchFunctionOrVariableException(line, name);
                }
                list.add(variable);
                if (!(grouperToken.peek() instanceof DoToken)) {
                    grouperToken.assertNextComma();
                } else {
                    break;
                }
            } else {
                throw new ExpectedTokenException("[Variable identifier]", next);
            }

            next = grouperToken.peek();
        } while (!(next instanceof DoToken));

        arguments = new ReturnValue[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arguments[i] = parent.getIdentifierValue(
                    new WordToken(list.get(i).getLineNumber(), list.get(i).getName()));
        }
    }


    public LineInfo getLineNumber() {
        return line;
    }


    private class WithExpressionContext extends ExpressionContextMixin {
        WithDeclaration withDeclaration;

        public WithExpressionContext(WithDeclaration withDeclaration, ExpressionContext parent) {
            super(parent.root(), parent);
            this.withDeclaration = withDeclaration;
        }

        @Override
        public Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            return null;
        }

        @Override
        public boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
                throws ParsingException {
            return true;
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            instructions = i.getNextCommand(declarations);
            i.assertNextSemicolon(i.next);
        }

        @Override
        public ReturnValue getIdentifierValue(WordToken name) throws ParsingException {
            Log.d(TAG, "getIdentifierValue() called with: name = [" + name + "]");
            for (int i = 0; i < variableDeclarations.size(); i++) {
                if (variableDeclarations.get(i).getName().equalsIgnoreCase(name.getName())) {
                    return fields.get(i);
                }
            }
            return super.getIdentifierValue(name);
        }

        @Override
        public VariableDeclaration getVariableDefinitionLocal(String ident) {
            VariableDeclaration unitVariableDecl = super.getVariableDefinitionLocal(ident);
            if (unitVariableDecl != null) {
                return unitVariableDecl;
            }

            for (VariableDeclaration variableDeclaration : variableDeclarations) {
                if (variableDeclaration.getName().equalsIgnoreCase(ident)) {
                    return variableDeclaration;
                }
            }
            return null;
        }
    }

}
