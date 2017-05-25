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

package com.js.interpreter.instructions.with_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.CustomType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.codeunit.library.UnitPascal;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime_value.FieldAccess;
import com.js.interpreter.runtime_value.RuntimeValue;

import java.util.ArrayList;

public class WithStatement {
    private static final String TAG = "WithDeclaration";

    public ExpressionContextMixin declarations;
    public Executable instructions;
    public LineInfo line;
    public ArrayList<RuntimeValue> fields = new ArrayList<>();
    public ArrayList<VariableDeclaration> variableDeclarations = new ArrayList<>();
    public RuntimeValue[] arguments;

    public WithStatement(ExpressionContext parent, GrouperToken grouperToken) throws ParsingException {
        this.declarations = new WithExpressionContext(this, parent);
        this.line = grouperToken.peek().getLineNumber();

        getReferenceVariables(grouperToken, parent);
        for (RuntimeValue argument : arguments) {
            if (argument.getType(parent).declType instanceof RecordType) {
                CustomType containsType = (CustomType) argument.getType(parent).declType;
                for (VariableDeclaration variableDeclaration : containsType.variableDeclarations) {
                    FieldAccess fieldAccess = new FieldAccess(argument, variableDeclaration.getName(),
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

    public RuntimeValue generate() {
        return new WithCall(this, fields, line);
    }

    public void execute(VariableContext parentcontext,
                        RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (this.declarations.root() instanceof UnitPascal) {
            parentcontext = main.getLibrary((UnitPascal) declarations.root());
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

        arguments = new RuntimeValue[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arguments[i] = parent.getIdentifierValue(
                    new WordToken(list.get(i).getLineNumber(), list.get(i).getName()));
        }
    }


    public LineInfo getLineNumber() {
        return line;
    }


    private class WithExpressionContext extends ExpressionContextMixin {
        WithStatement withDeclaration;

        public WithExpressionContext(WithStatement withDeclaration, ExpressionContext parent) {
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
        public RuntimeValue getIdentifierValue(WordToken name) throws ParsingException {
            for (int i = 0; i < variableDeclarations.size(); i++) {
                if (variableDeclarations.get(i).getName().equalsIgnoreCase(name.getName())) {
                    return fields.get(i);
                }
            }
            return super.getIdentifierValue(name);
        }
        @Override
        public VariableDeclaration getVariableDefinitionLocal(String ident) {
            for (VariableDeclaration variableDeclaration : variableDeclarations) {
                if (variableDeclaration.getName().equalsIgnoreCase(ident)) {
                    return variableDeclaration;
                }
            }

            return super.getVariableDefinitionLocal(ident);
        }
    }

}
