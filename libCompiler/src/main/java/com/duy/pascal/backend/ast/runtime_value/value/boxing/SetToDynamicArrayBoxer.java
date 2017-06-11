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

package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;

/**
 * Created by Duy on 11-Jun-17.
 */

public class SetToDynamicArrayBoxer implements RuntimeValue {
    public SetToDynamicArrayBoxer(RuntimeValue array) {
    }

    @Nullable
    @Override
    public RuntimeValue[] getOutputFormat() {
        return new RuntimeValue[0];
    }

    @Override
    public void setOutputFormat(@Nullable RuntimeValue[] formatInfo) {

    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return null;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return null;
    }

    @Nullable
    @Override
    public LineInfo getLineNumber() {
        return null;
    }

    @Nullable
    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        return null;
    }

    @Nullable
    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return null;
    }

    @Nullable
    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }
}
