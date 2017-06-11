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

package com.duy.pascal.backend.types.set;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ArrayIndexAccess;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.ArrayCloner;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.SetToDynamicArrayCloner;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.types.rangetype.SubrangeType;
import com.duy.pascal.backend.types.util.TypeUtils;

import java.lang.reflect.Array;


public class ArrayType<T extends DeclaredType> extends BaseSetType {
    public final T elementType;
    @Nullable
    private SubrangeType bound;
    private boolean dynamic;

    public ArrayType(T elementType, @Nullable SubrangeType bound) {
        this.elementType = elementType;
        this.bound = bound;
        this.dynamic = bound == null;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public T getElementType() {
        return elementType;
    }

    @Override
    public int getSize() {
        return bound != null ? bound.size : -1;
    }

    @Nullable
    public SubrangeType getBound() {
        return bound;
    }

    public void setBound(@Nullable SubrangeType bound) {
        this.bound = bound;
    }

    /**
     * This basically tells if the types are assignable from each other
     * according to Pascal.
     */
    public boolean superset(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ArrayType) {
            ArrayType<?> o = (ArrayType<?>) obj;
            if (o.elementType.equals(elementType)) {
                try {
                    if (bound == null) return false;
                    if (this.bound.contain(null, null, o.bound)) {
                        return true;
                    }
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ArrayType) {
            ArrayType<?> o = (ArrayType<?>) obj;
            if (o.elementType.equals(elementType)) {
                if (this.bound == null) return true;
                if (this.bound.equals(o.bound)) return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (bound != null) {
            return (elementType.hashCode() * 31 + bound.hashCode());
        } else {
            return (elementType.hashCode() * 31);
        }
    }

    /**
     * TODO: Must make this actually fill in array with default values
     */
    @Override
    public Object initialize() {
        Object result = Array.newInstance(elementType.getTransferClass(),
                bound == null ? 0 : bound.size);
        if (bound != null) {
            for (int i = 0; i < bound.size; i++)
                Array.set(result, i, elementType.initialize());
        }
        return result;
    }

    @Override
    public Class<?> getTransferClass() {
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

    @Override
    public String toString() {
        return "array" + (bound != null ? "[" + bound + "]" : "") + " of " + elementType;
    }

    /**
     * This basically won't do any conversions, as array types have to be exact,
     * except variable length arrays, but they are checked in the
     */
    @Override
    public RuntimeValue convert(RuntimeValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = value.getType(f);
        if (other.declType instanceof ArrayType) {
            return this.superset(other.declType) ? cloneValue(value) : null;
        } else if (other.declType instanceof SetType && this.isDynamic()) {
            if (((SetType) other.declType).getElementType().equals(this.getElementType())) {
                return new SetToDynamicArrayCloner(value);
            }
        }
        return null;

    }

    @Override
    public RuntimeValue cloneValue(final RuntimeValue r) {
        return new ArrayCloner<T>(r);
    }


    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) {
        if (bound != null) {
            return new ArrayIndexAccess(array, index, bound.lower);
        } else {
            return new ArrayIndexAccess(array, index, 0);
        }
    }

    @Override
    public Class<?> getStorageClass() {
        Class c = elementType.getStorageClass();
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
        return "array type";
    }


}
