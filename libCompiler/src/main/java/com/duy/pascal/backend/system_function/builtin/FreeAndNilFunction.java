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

package com.duy.pascal.backend.system_function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.NullValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.ArgumentType;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.backend.types.RuntimeType;

/**
 * Check if a pointer is valid
 */
public class FreeAndNilFunction implements IMethodDeclaration {
    private final static String NAME = "assigned";
    private ArgumentType[] argumentTypes = {new RuntimeType(
            new PointerType(BasicType.create(Object.class)), true)};

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue value = arguments[0];
        return new AssignedCall(value, value.getType(f), line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values,
                                               ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class AssignedCall extends FunctionCall {

        private RuntimeValue value;
        private RuntimeType type;
        private LineInfo line;

        AssignedCall(RuntimeValue value, RuntimeType type, LineInfo line) {
            this.value = value;
            this.type = type;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return null;
        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new AssignedCall(value, type, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new AssignedCall(value, type, line);
        }

        @Override
        protected String getFunctionName() {
            return NAME;
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f,
                                   @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            PascalReference value = (PascalReference) this.value.getValue(f, main);
            value.set(NullValue.get());
            return null;
        }
    }
}
