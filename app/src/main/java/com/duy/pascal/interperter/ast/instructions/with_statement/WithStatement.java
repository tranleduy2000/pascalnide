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

package com.duy.pascal.interperter.ast.instructions.with_statement;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.FieldAccess;
import com.duy.pascal.interperter.declaration.lang.types.CustomType;
import com.duy.pascal.interperter.declaration.lang.types.RecordType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.define.TypeIdentifierExpectException;
import com.duy.pascal.interperter.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.DoToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import java.util.ArrayList;
import java.util.List;

public class WithStatement {
    private static final String TAG = "WithDeclaration";
    public Executable instructions;
    public LineInfo line;

    public List<RuntimeValue> references;
    private ExpressionContextMixin withContext;

    private ArrayList<FieldAccess> fields = new ArrayList<>();

    public WithStatement(ExpressionContext parent, GrouperToken grouperToken) throws ParsingException {
        this.line = grouperToken.peek().getLineNumber();
        getReferenceVariables(grouperToken, parent);
        for (RuntimeValue argument : references) {
            Type type = argument.getRuntimeType(parent).declType;
            if (type instanceof RecordType) {
                CustomType recordType = (CustomType) type;
                for (VariableDeclaration var : recordType.getVariableDeclarations()) {
                    fields.add(new FieldAccess(argument, var.getName(),
                            var.getLineNumber()));
                }
            }
        }
        if (grouperToken.peek() instanceof DoToken) {
            grouperToken.take();
        }
        this.withContext = new WithExpressionContext(parent, fields);
        instructions = grouperToken.getNextCommand(withContext);
    }

    public ArrayList<FieldAccess> getFields() {
        return fields;
    }

    public RuntimeValue generate() {
        return new WithCall(this, fields, line);
    }

    private void getReferenceVariables(GrouperToken grouperToken, ExpressionContext parent)
            throws ParsingException {
        references = new ArrayList<>();
        Token next;
        do {
            next = grouperToken.peek();
            if (next instanceof WordToken) {
                RuntimeValue runtimeValue = grouperToken.getNextExpression(parent);
                Type type = runtimeValue.getRuntimeType(parent).declType;
                if (!(type instanceof RecordType)) {
                    throw new TypeIdentifierExpectException(runtimeValue.getLineNumber(),
                            "record", parent);
                }
                references.add(runtimeValue);
            } else {
                throw new ExpectedTokenException("[Variable identifier]", next);
            }
            next = grouperToken.peek();
        } while (!(next instanceof DoToken));

    }


    public LineInfo getLineNumber() {
        return line;
    }


    private class WithExpressionContext extends ExpressionContextMixin {

        private final ExpressionContext parent;
        private final ArrayList<FieldAccess> fields;

        public WithExpressionContext(ExpressionContext parent, ArrayList<FieldAccess> fields) {
            super(parent.root(), parent);
            this.parent = parent;
            this.fields = fields;
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
            instructions = i.getNextCommand(withContext);
            i.assertNextSemicolon();
        }

        @Override
        public RuntimeValue getIdentifierValue(WordToken name) throws ParsingException {
            for (FieldAccess field : fields) {
                if (field.getName().equalsIgnoreCase(name.getName())) {
                    return field;
                }
            }
            return super.getIdentifierValue(name);
        }

        @Override
        public VariableDeclaration getVariableDefinitionLocal(String ident) {
            return super.getVariableDefinitionLocal(ident);
        }
    }

}
