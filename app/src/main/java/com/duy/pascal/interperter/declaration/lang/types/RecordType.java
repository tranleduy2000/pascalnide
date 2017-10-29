/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RecordValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownFieldException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonConstantExpressionException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.grouping.ParenthesizedToken;

import java.util.ArrayList;

public class RecordType extends CustomType implements Cloneable {
    public RecordType() {

    }

    public RecordType(ArrayList<VariableDeclaration> vars) {
        super(vars);
    }

    public static ConstantAccess<RecordValue> getRecordConstant(ExpressionContext context, Token groupConstant,
                                                                RecordType ztype) throws Exception {
        if (groupConstant instanceof ParenthesizedToken) {
            ParenthesizedToken group = (ParenthesizedToken) groupConstant;
            RecordType recordType = ztype.clone();
            while (group.hasNext()) {
                Token name = group.take();
                if (!(name instanceof WordToken)) {
                    throw new ExpectedTokenException("[field identifier]", name);
                }
                VariableDeclaration field = recordType.findField(((WordToken) name).name);
                if (field == null) {
                    throw new UnknownFieldException(name.getLineNumber(), recordType,
                            ((WordToken) name).getName(), context);
                }
                if (group.peek() instanceof ColonToken) {
                    group.take();
                } else {
                    throw new ExpectedTokenException(":", group.peek());
                }
                RuntimeValue value = group.getNextExpression(context);
                RuntimeValue convert = field.getType().convert(value, context);
                if (convert == null) {
                    throw new UnConvertibleTypeException(value, field.getType(),
                            value.getRuntimeType(context).getRawType(), context);
                }
                Object o = value.compileTimeValue(context);
                if (o == null) {
                    throw new NonConstantExpressionException(value);
                }
                recordType.setFieldValue(((WordToken) name).getName(), o);
                if (group.hasNext()) {
                    group.assertNextSemicolon();
                }
            }
            return new ConstantAccess<>(recordType.initialize(), recordType,
                    group.getLineNumber());
        } else {
            throw new ExpectedTokenException(groupConstant, new ParenthesizedToken(null));
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("record ");
        for (VariableDeclaration var : variableDeclarations) {
            out.append(var.getName()).append(":").append(var.getType().toString()).append("; ");
        }
        out.append("end");
        return out.toString();
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "record type";
    }

    /**
     * @param name - name of field
     * @return index of field in list variable of record type
     */
    public VariableDeclaration findField(Name name) {
        for (VariableDeclaration variableDeclaration : variableDeclarations) {
            if (variableDeclaration.getName().equals(name)) return variableDeclaration;
        }
        return null;

    }

    @NonNull
    @Override
    public RecordValue initialize() {
        return super.initialize();
    }

    public boolean setFieldValue(Name name, Object o) {
        for (VariableDeclaration variableDeclaration : variableDeclarations) {
            if (variableDeclaration.getName().equals(name)) {
                variableDeclaration.setInitialValue(o);
                return true;
            }
        }
        return false;
    }

    @Override
    public RecordType clone() {
        RecordType recordType = new RecordType();
        for (VariableDeclaration var : variableDeclarations) {
            recordType.addVariableDeclaration(var.clone());
        }
        recordType.setLineNumber(lineInfo);
        recordType.setName(name);
        return recordType;
    }
}
