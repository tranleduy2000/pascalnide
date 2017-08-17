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
import com.duy.pascal.interperter.ast.expressioncontext.ClassExpressionContext;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.debugable.DebuggableExecutableReturnValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

/**
 * Created by Duy on 16-Jun-17.
 */
public class ClassFunctionCall extends DebuggableExecutableReturnValue {
    private String container;
    private FunctionCall function;
    private LineInfo lineInfo;
    private ClassExpressionContext declaration;
    private VariableContext classVarContext;

    public ClassFunctionCall(String container, FunctionCall function,
                             LineInfo lineInfo, ClassExpressionContext declaration) {
        this.container = container;
        this.function = function;
        this.lineInfo = lineInfo;
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return container + "." + function;
    }

    @Override
    public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        if (classVarContext == null) {
            classVarContext = main.getRuntimePascalClassContext(container);
        }
        return function.getValue(classVarContext, main);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (classVarContext == null) {
            classVarContext = main.getRuntimePascalClassContext(container);
        }
        Object valueImpl = getValueImpl(classVarContext, main);
        if (valueImpl == ExecutionResult.EXIT) {
            return ExecutionResult.EXIT;
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
        return function.getRuntimeType(f);
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
