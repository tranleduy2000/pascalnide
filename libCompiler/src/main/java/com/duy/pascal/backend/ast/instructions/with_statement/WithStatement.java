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

package com.duy.pascal.backend.ast.instructions.with_statement;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.FieldAccess;
import com.duy.pascal.backend.data_types.CustomType;
import com.duy.pascal.backend.data_types.RecordType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.UnknownIdentifierException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

import java.util.ArrayList;

public class WithStatement {
    private static final String TAG = "WithDeclaration";
    public Executable instructions;
    public LineInfo line;
    public RuntimeValue[] arguments;
    private ExpressionContextMixin scopeWithStatement;
    private ArrayList<RuntimeValue> fields = new ArrayList<>();
    private ArrayList<VariableDeclaration> variableDeclarations = new ArrayList<>();
    public WithStatement(ExpressionContext parent, GrouperToken grouperToken) throws ParsingException {
        this.scopeWithStatement = new WithExpressionContext(parent);
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
        instructions = grouperToken.getNextCommand(scopeWithStatement);
    }

    public ArrayList<RuntimeValue> getFields() {
        return fields;
    }

    public ArrayList<VariableDeclaration> getVariableDeclarations() {
        return variableDeclarations;
    }

    public RuntimeValue generate() {
        return new WithCall(this, fields, line);
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
                    throw new UnknownIdentifierException(line, name, parent);
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

        public WithExpressionContext(ExpressionContext parent) {
            super(parent.root(), parent);
        }

        @NonNull
        @Override
        public LineInfo getStartLine() {
            return line;
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
            instructions = i.getNextCommand(scopeWithStatement);
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
