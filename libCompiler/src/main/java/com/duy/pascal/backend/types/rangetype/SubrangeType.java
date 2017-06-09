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

package com.duy.pascal.backend.types.rangetype;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.InfoType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;

/**
 * Created by Duy on 25-May-17.
 */

public abstract class SubrangeType extends InfoType implements Containable {
    public int lower;

    @IntRange(from = 0)
    public int size = 0;

    protected LineInfo lineInfo;

    protected Object first, last;

    public int getLower() {
        return lower;
    }

    public int getSize() {
        return size;
    }

    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    public void setLineNumber(LineInfo lineInfo) {
        this.lineInfo = lineInfo;
    }

    public abstract String toString();

    @Nullable
    @Override
    public Object initialize() {
        return first;
    }

    @Nullable
    @Override
    public Class getTransferClass() {
        return SubrangeType.class;
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        RuntimeType other_type = runtimeValue.getType(f);
        if (this.equals(other_type.declType)) {
            return cloneValue(runtimeValue);
        }
        return null;
    }

    @Override
    public boolean equals(DeclaredType obj) {
        if (!(obj instanceof SubrangeType)) {
            return false;
        }
        SubrangeType other = (SubrangeType) obj;
        return this.first.equals(other.first) && last.equals(other.last);
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

    @Nullable
    @Override
    public Class<?> getStorageClass() {
        return SubrangeType.class;
    }

    @Override
    public String getEntityType() {
        return "subrange";
    }
}
