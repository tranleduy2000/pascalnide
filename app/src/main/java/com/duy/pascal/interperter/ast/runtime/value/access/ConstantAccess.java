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

package com.duy.pascal.interperter.ast.runtime.value.access;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class ConstantAccess<T> extends DebuggableReturnValue {
    private T mValue;
    private Type type;
    private LineNumber mLineNumber;
    @Nullable
    private Name name = null;

    public ConstantAccess(@Nullable T value, @Nullable LineNumber mLineNumber) {
        this.mValue = value;
        this.mLineNumber = mLineNumber;
    }

    public ConstantAccess(@Nullable T o, @Nullable Type type, @Nullable LineNumber mLineNumber) {
        this.mValue = o;
        this.type = type;
        this.mLineNumber = mLineNumber;
    }

    public T getValue() {
        return mValue;
    }

    public String toCode() {
        if (mValue instanceof StringBuilder || mValue instanceof String || mValue instanceof Character) {
            return "'" + mValue.toString() + "'";
        }
        return mValue.toString();
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return mLineNumber;
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main) {
        return mValue;
    }

    @Override
    public String toString() {
        if (name == null) {
            return String.valueOf(mValue);
        } else {
            return name.toString();
        }
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) {
        if (type != null) {
            return new RuntimeType(type, false);
        }
        return new RuntimeType(BasicType.create(mValue.getClass()), false);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return mValue;
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
