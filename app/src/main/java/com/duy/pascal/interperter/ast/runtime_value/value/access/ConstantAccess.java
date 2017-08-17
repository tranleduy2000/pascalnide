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

package com.duy.pascal.interperter.ast.runtime_value.value.access;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class ConstantAccess<T> extends DebuggableReturnValue {
    private T value;
    private Type type;
    private LineInfo mLineNumber;
    @Nullable
    private Name name = null;

    public ConstantAccess(@Nullable T o, @Nullable LineInfo mLineNumber) {
        this.value = o;
        this.mLineNumber = mLineNumber;
    }

    public ConstantAccess(@Nullable T o, @Nullable Type type, @Nullable LineInfo mLineNumber) {
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

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return mLineNumber;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

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
            return name.toString();
        }
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) {
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
            throws Exception {
        return this;
    }

    @Nullable
    public Name getName() {
        return name;
    }

    public void setName(@Nullable Name name) {
        this.name = name;
    }

}
