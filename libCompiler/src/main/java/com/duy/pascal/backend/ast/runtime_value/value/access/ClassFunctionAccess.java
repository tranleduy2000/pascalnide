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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableExecutableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;

/**
 * Created by Duy on 16-Jun-17.
 */
public class ClassFunctionAccess extends DebuggableExecutableReturnValue {
    private String container;
    private FunctionCall function;
    private LineInfo lineInfo;

    public ClassFunctionAccess(String container, FunctionCall function,
                               LineInfo lineInfo) {
        this.container = container;
        this.function = function;
        this.lineInfo = lineInfo;
    }

    @Override
    public String toString() {
        return container + "." + function;
    }

    @Override
    public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        f = main.getRuntimePascalContext(container);
        return function.getValue(f, main);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object valueImpl = getValueImpl(f, main);
        if (valueImpl == ExecutionResult.EXIT) {
            return ExecutionResult.EXIT;
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return function.getType(f);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException {
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
}
