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

package com.duy.pascal.backend.declaration.lang.types.subrange;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.TypeInfo;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

/**
 * Created by Duy on 25-May-17.
 */

public abstract class SubrangeType<T extends Comparable> extends TypeInfo implements Containable<T> {

    protected T first;
    protected T last;

    public SubrangeType(T first, T last) {

        this.first = first;
        this.last = last;
    }

    public abstract boolean contains(SubrangeType other);

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    public void setLineNumber(@NonNull LineInfo lineInfo) {
        this.lineInfo = lineInfo;
    }

    public abstract String toString();

    @NonNull
    @Override
    public Object initialize() {
        return first;
    }

    @Nullable
    @Override
    public Class getTransferClass() {
        return getStorageClass();
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws ParsingException {
        RuntimeType other_type = other.getRuntimeType(f);
        if (this.equals(other_type.declType)) {
            return cloneValue(other);
        }
        return null;
    }

    @Override
    public boolean equals(Type obj) {
        if (obj instanceof SubrangeType) {
            SubrangeType other = (SubrangeType) obj;
            return this.first.equals(other.first) && last.equals(other.last);
        } else if (obj.getStorageClass() == this.getStorageClass()) {
            return true;
        }
        return false;

    }

    @Nullable
    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return r;
    }

    @Nullable
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(index.getLineNumber(), this);
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "subrange";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contain(@Nullable VariableContext f, @Nullable RuntimeExecutableCodeUnit<?> main,
                           T value) throws RuntimePascalException {
        return first.compareTo(value) <= 0 && last.compareTo(value) >= 0;
    }
}
