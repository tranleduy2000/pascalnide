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

package com.duy.pascal.backend.ast.function_declaretion.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.ObjectBasedPointer;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalPointer;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.types.ArgumentType;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class NewFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};

    @Override
    public String getName() {
        return "new";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        RuntimeType type = pointer.getType(f);
        return new NewCall(pointer, type, line);
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
    public DeclaredType returnType() {
        return null;
    }

    @Override
    public String description() {
        return "Dynamically allocate memory for variable";
    }

    private class NewCall extends FunctionCall {

        private RuntimeValue value;
        private RuntimeType type;
        private LineInfo line;

        NewCall(RuntimeValue value, RuntimeType type, LineInfo line) {
            this.value = value;
            this.type = type;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return null;
        }

        @Override
        public LineInfo getLineNumber() {
            return line;
        }


        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NewCall(value, type, line);
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new NewCall(value, type, line);
        }

        @Override
        protected String getFunctionName() {
            return "new";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            PascalPointer pointer = (PascalPointer) this.value.getValue(f, main);
            PointerType pointerType = (PointerType) ((PointerType) type.declType).pointedToType;
            DeclaredType type = pointerType.pointedToType;
            pointer.set(new ObjectBasedPointer<>(type.initialize()));
           /* if (type instanceof ArrayType) {
                pointer.set(new ObjectBasedPointer<>(new Object[]{}));
            } else if (BasicType.Byte.equals(type)) {
                pointer.set(new ObjectBasedPointer<>((byte) 0));
            } else if (BasicType.Short.equals(type)) {
                pointer.set(new ObjectBasedPointer<>((short) 0));
            } else if (BasicType.Integer.equals(type)) {
                pointer.set(new ObjectBasedPointer<>(0));
            } else if (BasicType.Long.equals(type)) {
                pointer.set(new ObjectBasedPointer<>(0L));
            } else if (BasicType.Double.equals(type)) {
                pointer.set(new ObjectBasedPointer<>(0d));
            } else if (BasicType.Character.equals(type)) {
                pointer.set(new ObjectBasedPointer<>((char) 0));
            } else if (BasicType.StringBuilder.equals(type)) {
                pointer.set(new ObjectBasedPointer<>(""));
            } else if (type instanceof JavaClassBasedType) {
                Object initialize = type.initialize();
                pointer.set(new ObjectBasedPointer<>(initialize));
            }*/
            return null;
        }
    }
}
