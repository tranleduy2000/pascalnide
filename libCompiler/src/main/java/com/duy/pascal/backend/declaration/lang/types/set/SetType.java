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

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.SetIndexAccess;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.SetCloner;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

import java.util.LinkedList;

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
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
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
