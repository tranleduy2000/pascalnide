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

package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.cloning.ArrayCloner;
import com.ncsa.common.util.TypeUtils;

import java.lang.reflect.Array;

/**
 * set type in pascal
 * <p>
 * Created by Duy on 16-May-17.
 */
public class SetType<T extends DeclaredType> implements DeclaredType {
    private T elementType;
    private int size;
    private LineInfo line;

    public SetType(T elementType, int size, LineInfo lineInfo) {
        this.elementType = elementType;
        this.size = size;
        this.line = lineInfo;
    }

    public T getElementType() {
        return elementType;
    }

    @Override
    public Object initialize() {
        //create new array with element type is elementType via java reflect
        Object result = Array.newInstance(elementType.getTransferClass(), size);

        //init value for element
        for (int i = 0; i < size; i++) {
            Array.set(result, i, elementType.initialize());
        }
        return result;
    }

    @Override
    public Class getTransferClass() {
        String s = elementType.getTransferClass().getName();
        StringBuilder b = new StringBuilder();
        b.append('[');
        b.append('L');
        b.append(s);
        b.append(';');
        try {
            return Class.forName(b.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This basically tells if the types are assignable from each other
     * according to Pascal.
     */
    public boolean superset(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SetType) {
            SetType<?> o = (SetType<?>) obj;
            if (o.elementType.equals(elementType)) {
                if (this.size >= o.size) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        RuntimeType other = runtimeValue.getType(f);
        return this.superset(other.declType) ? cloneValue(runtimeValue) : null;
    }

    @Override
    public String toString() {
        return "set of " + elementType;
    }

    @Override
    public int hashCode() {
        return (elementType.hashCode() * 31 + size);
    }

    @Override
    public boolean equals(DeclaredType other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SetType) {
            SetType other1 = (SetType) other;
            if (other1.elementType.equals(elementType)) {
                if (this.size == other1.size) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new ArrayCloner<>(r);
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index)
            throws NonArrayIndexed {
        throw new NonArrayIndexed(line, this);
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

    public int getSize() {
        return size;
    }
}
