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

package com.duy.pascal.interperter.ast.runtime.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Created by Duy on 13-Jun-17.
 */

public class NullValue implements RuntimeValue {
    private static final NullValue NULL = new NullValue();

    private NullValue() {
    }

    public static NullValue get() {
        return NULL;
    }

    @Override
    public String toString() {
        return "znull";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == null || obj instanceof NullValue;
    }

    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return null;
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
        return null;
    }

    @Override
    public LineInfo getLineNumber() {
        return null;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Nullable
    @Override
    public Object compileTimeValue(CompileTimeContext context) throws Exception {
        return null;
    }

    @Nullable
    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        return null;
    }

    @Nullable
    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
