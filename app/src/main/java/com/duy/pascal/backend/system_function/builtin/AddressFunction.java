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
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.operators.pointer.DerefEval;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.declaration.lang.types.ArgumentType;
import com.duy.pascal.backend.declaration.lang.types.BasicType;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.PointerType;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

public class AddressFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};
    private PointerType pointerType;

    @Override
    public String getName() {
        return "addr";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        this.pointerType = (PointerType) pointer.getRuntimeType(f).declType;
        return new AddressFunctionCall(pointer, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return pointerType;
    }

    @Override
    public String description() {
        return null;
    }

    private class AddressFunctionCall extends FunctionCall {

        private RuntimeValue pointer;
        private LineInfo line;

        public AddressFunctionCall(RuntimeValue pointer, LineInfo line) {
            this.pointer = pointer;
            this.line = line;
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
            Reference ref = (Reference) pointer.getValue(f, main);
            return ref.get();
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
            RuntimeType pointertype = pointer.getRuntimeType(f);
            return new RuntimeType(((PointerType) pointertype.declType).pointedToType, true);
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
        public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException {
            return null;
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
            Reference<?> ref = (Reference<?>) pointer.compileTimeValue(context);
            if (ref != null) {
                try {
                    return ref.get();
                } catch (RuntimePascalException e) {
                    throw new ConstantCalculationException(e);
                }
            }
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
            Object val = this.compileTimeValue(context);
            if (val != null) {
                return new ConstantAccess<>(val, line);
            } else {
                return new DerefEval(pointer.compileTimeExpressionFold(context), line);
            }
        }

        @Override
        protected String getFunctionName() {
            return "addr";
        }
    }
}
