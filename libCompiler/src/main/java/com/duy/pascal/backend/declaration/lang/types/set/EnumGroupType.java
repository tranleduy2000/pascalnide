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

package com.duy.pascal.backend.declaration.lang.types.set;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.declaration.lang.types.BasicType;
import com.duy.pascal.backend.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.backend.declaration.lang.types.OperatorTypes;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.TypeInfo;
import com.duy.pascal.backend.declaration.lang.types.subrange.Containable;
import com.duy.pascal.backend.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 25-May-17.
 */

public class EnumGroupType extends TypeInfo implements Containable<EnumElementValue> {
    private LinkedList<EnumElementValue> list;

    public EnumGroupType(@NonNull LinkedList<EnumElementValue> list) {
        this.list = list;
    }


    /**
     * @param targetType - type of enum
     * @return the enum constant, I define the enum as {@link LinkedList}
     */
    public static ConstantAccess<EnumElementValue> getEnumConstant(GrouperToken i, ExpressionContext context, Token token,
                                                                   Type targetType) throws ParsingException {
        RuntimeValue expression = i.getNextExpression(context, token);
        Object constant = expression.compileTimeValue(context);
        if (constant == null) {
            throw new NonConstantExpressionException(expression);
        }
        RuntimeValue convert = targetType.convert(expression, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(expression, targetType,
                    expression.getRuntimeType(context).declType, context);
        }
        Object o = convert.compileTimeValue(context);
        return new ConstantAccess<>((EnumElementValue) o, targetType, token.getLineNumber());
    }


    public static Type getEnumType(ExpressionContext c, ParenthesizedToken group)
            throws ParsingException {
        LinkedList<EnumElementValue> elements = new LinkedList<>();
        EnumGroupType enumGroupType = new EnumGroupType(elements);
        AtomicInteger index = new AtomicInteger(0);
        while (group.hasNext()) {
            Token token = group.take();
            if (!(token instanceof WordToken)) {
                throw new ExpectedTokenException("identifier", token);
            }
            WordToken wordToken = (WordToken) token;
            if (group.peek() instanceof OperatorToken) {
                OperatorToken operator = (OperatorToken) group.take();
                if (operator.type == OperatorTypes.EQUALS) {
                    RuntimeValue value = group.getNextExpression(c);
                    value = value.compileTimeExpressionFold(c);
                    RuntimeValue convert = BasicType.Integer.convert(value, c);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(value, BasicType.Integer,
                                value.getRuntimeType(c).declType, c);
                    }
                    Object oddValue = convert.compileTimeValue(c);
                    if (oddValue == null) {
                        throw new NonConstantExpressionException(convert);
                    }
                    EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType,
                            index.get(), token.getLineNumber());   //create new enum
                    e.setValue((Integer) oddValue);
                    elements.add(e);                    //add to parent
                    ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType,
                            e, e.getLineNumber());
                    c.verifyNonConflictingSymbol(constant); //check duplicate value
                    c.declareConst(constant);                    //add as constant
                } else {
                    throw new ExpectedTokenException(operator, ",", "=");
                }
            } else {

                EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType, index.get(),
                        token.getLineNumber()); //create new enum
                e.setValue(index.get());
                elements.add(e);        //add to container
                ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType, e,
                        e.getLineNumber());
                c.declareConst(constant);  //add as constant
            }
            index.getAndIncrement();
            //if has next, check comma token
            if (group.hasNext()) {
                group.assertNextComma();
            }
        }
        return enumGroupType;
    }


    public void add(EnumElementValue element) {
        list.add(element);
    }

    /**
     * @param position - index of value in list object
     */
    @Nullable
    public EnumElementValue get(int position) {
        if (position > list.size() - 1) {
            return null;
        }
        return list.get(position);
    }

    public EnumElementValue getFirst() {
        return list.getFirst();
    }

    public EnumElementValue getLast() {
        return list.getLast();
    }


    @Nullable
    public EnumElementValue get(String element) {
        for (EnumElementValue pair : list) {
            if (pair.getName().equalsIgnoreCase(element)) {
                return pair;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public Object initialize() {
        return new LinkedList<>();
    }

    @Override
    public Class getTransferClass() {
        return LinkedList.class;
    }


    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        RuntimeType other = runtimeValue.getRuntimeType(f);
        if (this.equals(other.declType)) {
            return cloneValue(runtimeValue);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Type other) {
        if (this == other) {
            return true;
        }
        if (other instanceof EnumGroupType) {
            EnumGroupType otherEnum = (EnumGroupType) other;
            if (this.list.size() != otherEnum.list.size()) {
                return false;
            }
            if (this.list.equals(otherEnum.list)) {
                return true;
            }
        } else if (other instanceof JavaClassBasedType && other.getStorageClass() ==
                EnumElementValue.class) {
            return true;
        }
        return false;
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return r;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index)
            throws NonArrayIndexed {
        throw new NonArrayIndexed(lineInfo, this);
    }

    @Override
    public Class<?> getStorageClass() {
        return LinkedList.class;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "enum type";
    }


    public int getSize() {
        return list.size();
    }

    public LinkedList<EnumElementValue> getList() {
        return list;
    }

    public int indexOf(EnumElementValue key) {
        return this.list.indexOf(key);
    }

    @Override
    public boolean contain(@Nullable VariableContext f, @Nullable RuntimeExecutableCodeUnit<?> main,
                           EnumElementValue value) throws RuntimePascalException {
        return list.contains(value);
    }
}
