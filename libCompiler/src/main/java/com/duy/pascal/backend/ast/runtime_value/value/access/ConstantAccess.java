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

package com.duy.pascal.backend.ast.runtime_value.value.access;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.RuntimeType;

public class ConstantAccess<T> extends DebuggableReturnValue {
    private T value;
    private DeclaredType type;
    private LineInfo mLineNumber;
    @Nullable
    private String name = null;

    public ConstantAccess(@Nullable T o, @Nullable LineInfo mLineNumber) {
        this.value = o;
        this.mLineNumber = mLineNumber;
    }

    public ConstantAccess(@Nullable T o, @Nullable DeclaredType type, @Nullable LineInfo mLineNumber) {
        this.value = o;
        this.type = type;
        this.mLineNumber = mLineNumber;
    }

    public T getValue() {
        return value;
    }

    public String toCode() {
        if (value instanceof StringBuilder || value instanceof String || value instanceof Character) {
            return "'" + value.toString() + "'";
        }
        return value.toString();
    }

    @Override
    public LineInfo getLineNumber() {
        return mLineNumber;
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) {
        return value;
    }

    @Override
    public String toString() {
        if (name == null) {
            return String.valueOf(value);
        } else {
            return name + (value != null ? " = " + value : "");
        }
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        if (type != null) {
            return new RuntimeType(type, false);
        }
        return new RuntimeType(BasicType.create(value.getClass()), false);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return value;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
