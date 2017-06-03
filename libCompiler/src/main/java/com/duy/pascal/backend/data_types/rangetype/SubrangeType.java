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

package com.duy.pascal.backend.data_types.rangetype;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.InfoType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

/**
 * Created by Duy on 25-May-17.
 */

public abstract class SubrangeType extends InfoType implements Containable {
    public int lower;
    /**
     * if size = -1, the size of array will be ignore while comparing
     */
    public int size;
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

    @Override
    public boolean contain(@Nullable VariableContext f, @Nullable RuntimeExecutableCodeUnit<?> main, Object value) throws RuntimePascalException {
        if (value.getClass() == first.getClass()) {

        }
        return false;
    }

    @Nullable
    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return null;
    }

    @Nullable
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed {
        return null;
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
