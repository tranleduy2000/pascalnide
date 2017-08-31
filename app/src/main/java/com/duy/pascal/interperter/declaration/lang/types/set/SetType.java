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

package com.duy.pascal.interperter.declaration.lang.types.set;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.conditional.forstatement.ForStatement;
import com.duy.pascal.interperter.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.SetIndexAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.boxing.SetBoxer;
import com.duy.pascal.interperter.ast.runtime_value.value.cloning.SetCloner;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.DuplicateElementException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.DotDotToken;
import com.duy.pascal.interperter.tokens.grouping.BracketedToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * set type in pascal
 * <p>
 * Created by Duy on 16-May-17.
 */
public class SetType<T extends Type> extends BaseSetType {
    private T elementType;
    /**
     * dynamic element
     */
    private LinkedList list = new LinkedList<>();

    public SetType(T elementType, LinkedList linkedList, LineInfo lineInfo) {
        this.elementType = elementType;
        this.list = linkedList;
        this.lineInfo = lineInfo;
    }

    public SetType(T elementType, LineInfo lineInfo) {
        this.elementType = elementType;
        this.lineInfo = lineInfo;
    }

    /**
     * @param typeReference - type of set (example: set of char => type is "char")
     * @return the set constant, I define the enum as {@link LinkedList}
     */
    public static ConstantAccess<LinkedList> getSetConstant(ExpressionContext context, Token token,
                                                            AtomicReference<Type> typeReference)
            throws Exception {
        if (!(token instanceof BracketedToken)) {
            throw new ExpectedTokenException(new BracketedToken(null), token);
        }

        BracketedToken bracketedToken = (BracketedToken) token;
        LinkedList<Object> linkedList = new LinkedList<>();
        Type temp = null;
        while (bracketedToken.hasNext()) {
            ConstantAccess element;
            if (typeReference.get() == null) {
                element = GrouperToken.getConstantElement(context, bracketedToken, null);
                if (temp == null) {
                    if (element.getValue() instanceof EnumElementValue) {
                        temp = ((EnumElementValue) element.getValue()).getRuntimeType(context).declType;
                    } else {
                        temp = (element.getRuntimeType(context).declType);
                    }
                } else if (!(temp.getStorageClass() == Object.class)) {
                    RuntimeValue convert;
                    if (element.getValue() instanceof EnumElementValue) {
                        convert = temp.convert((EnumElementValue) element.getValue(), context);
                    } else {
                        convert = temp.convert(element, context);
                    }
                    if (convert == null) temp = BasicType.create(Object.class);
                }
            } else {
                element = GrouperToken.getConstantElement(context, bracketedToken, typeReference.get());
            }
            for (Object o : linkedList) {
                if (o.equals(element.getValue())) {
                    throw new DuplicateElementException(element.getValue(), linkedList, element.getLineNumber());
                }
            }
            linkedList.add(element.getValue());
        }
        if (typeReference.get() == null) typeReference.set(temp);
        return new ConstantAccess<LinkedList>(linkedList, typeReference.get(), bracketedToken.getLineNumber());
    }

    /**
     * @param typeReference - type of set (example: set of char => type is "char")
     * @return the set constant, I define the enum as {@link LinkedList}
     */
    public static SetBoxer getSetRuntime(@NonNull ExpressionContext context, Token token,
                                         AtomicReference<Type> typeReference)
            throws Exception {
        if (!(token instanceof BracketedToken)) {
            throw new ExpectedTokenException(new BracketedToken(null), token);
        }

        BracketedToken bracket = (BracketedToken) token;
        LinkedList<RuntimeValue> linkedList = new LinkedList<>();
        Type temp = null;
        while (bracket.hasNext()) {
            RuntimeValue element;
            if (typeReference.get() == null) {
                element = bracket.getNextExpression(context);
                if (temp == null) {
                    temp = element.getRuntimeType(context).declType;
                } else if (!(temp.getStorageClass() == Object.class)) {
                    RuntimeValue convert = temp.convert(element, context);
                    if (convert == null) temp = BasicType.create(Object.class);
                }
            } else {
                element = bracket.getNextExpression(context);
                RuntimeValue convert = typeReference.get().convert(element, context);
                if (convert == null) {
                    throw new UnConvertibleTypeException(element,
                            typeReference.get(), element.getRuntimeType(context).declType, context);
                }
                element = convert;
            }

            if (bracket.hasNext()) {
                if (bracket.peek() instanceof DotDotToken) { //range
                    if (linkedList.size() != 0) {
                        bracket.assertNextComma(); //throw exception
                    }
                    bracket.take(); //dot dot

                    //check first type
                    RuntimeValue firstValue = element;
                    Type firstType = firstValue.getRuntimeType(context).declType;
                    if (BasicType.Long.convert(firstValue, context) == null) {
                        throw new UnConvertibleTypeException(firstValue, BasicType.Long, firstType, context);
                    }

                    //check two type
                    RuntimeValue lastValue = bracket.getNextExpression(context);
                    Type lastType = lastValue.getRuntimeType(context).declType;
                    if (firstType.convert(lastValue, context) == null) {
                        throw new UnConvertibleTypeException(lastValue, firstType, lastType, context);
                    }

                    if (bracket.hasNext()){
                        throw new ExpectedTokenException("EOF", bracket.take());
                    }
                } else {
                    bracket.assertNextComma();
                }
            }
            linkedList.add(element);
        }
        if (typeReference.get() == null) typeReference.set(temp);
        return new SetBoxer(linkedList, typeReference.get(), bracket.getLineNumber());
    }

    @SuppressWarnings("unchecked")
    public void add(T element) {
        list.add(element);
    }

    public boolean remove(T element) {
        return list.remove(element);
    }

    public Object peek() {
        return list.peek();
    }

    public Object pop() {
        return list.pop();
    }

    public T getElementType() {
        return elementType;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @NonNull
    @Override
    public Object initialize() {
        return this.list;
    }

    @Override
    public Class getTransferClass() {
        return LinkedList.class;
    }

    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws Exception {
        RuntimeType other = runtimeValue.getRuntimeType(f);
        if (other.declType instanceof SetType) {
            if (((SetType) other.declType).getElementType().equals(this.getElementType())) {
                return cloneValue(runtimeValue);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "set of " + elementType;
    }

    @Override
    public int hashCode() {
        return (elementType.hashCode() * 31 + list.size());
    }

    @Override
    public boolean equals(Type other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SetType) {
            SetType other1 = (SetType) other;
            if (other1.elementType.equals(elementType)
                    && list.equals(((SetType) other).list)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new SetCloner<>(r);
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index)
            throws NonArrayIndexed {
        return new SetIndexAccess(array, index);
    }

    @Override
    public Class<?> getStorageClass() {
        return LinkedList.class;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "set type";
    }
}
