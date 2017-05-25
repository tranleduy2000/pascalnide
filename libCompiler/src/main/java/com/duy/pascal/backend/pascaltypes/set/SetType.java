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

package com.duy.pascal.backend.pascaltypes.set;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.InfoType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.SetIndexAccess;
import com.duy.pascal.backend.runtime.value.cloning.SetCloner;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.ncsa.common.util.TypeUtils;

import java.util.LinkedList;

/**
 * set type in pascal
 * <p>
 * Created by Duy on 16-May-17.
 */
public class SetType<T extends DeclaredType> extends InfoType {
    private T elementType;
    private LinkedList<T> list = new LinkedList<>();

    public SetType(T elementType, LinkedList<T> linkedList, LineInfo lineInfo) {
        this.elementType = elementType;
        this.list = linkedList;
        this.lineInfo = lineInfo;
    }

    public SetType(T elementType, LineInfo lineInfo) {
        this.elementType = elementType;
        this.lineInfo = lineInfo;
    }

    public void add(T element) {
        list.add(element);
    }

    public boolean remove(T element) {
        return list.remove(element);
    }

    public T peek() {
        return list.peek();
    }

    public T pop() {
        return list.pop();
    }

    public T getElementType() {
        return elementType;
    }

    @Override
    public Object initialize() {
        //create new array with element type is elementType via java reflect
        return this.list;
    }

    @Override
    public Class getTransferClass() {
        return LinkedList.class;
    }


    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        RuntimeType other = runtimeValue.getType(f);
        if (other.declType instanceof SetType) {
            return cloneValue(runtimeValue);
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
    public boolean equals(DeclaredType other) {
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
        Class c = elementType.getStorageClass();
        if (c == null) return null;
        if (c.isArray()) {
            try {
                return Class.forName("[" + c.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else if (c.isPrimitive()) {
            c = TypeUtils.getClassForType(c);
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        b.append('L');
        b.append(c.getName());
        b.append(';');
        try {
            return Class.forName(b.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEntityType() {
        return "set type";
    }

}
