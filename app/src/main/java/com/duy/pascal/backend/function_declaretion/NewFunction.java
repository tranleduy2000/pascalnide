/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.function_declaretion;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.function_declaretion.abstract_class.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.ObjectBasedPointer;
import com.js.interpreter.runtime.PascalPointer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class NewFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};

    @Override
    public String name() {
        return "new";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnValue pointer = arguments[0];
        RuntimeType type = pointer.getType(f);
        return new NewCall(pointer, type, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, ReturnValue[] values, ExpressionContext f) throws ParsingException {
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

        private ReturnValue value;
        private RuntimeType type;
        private LineInfo line;

        NewCall(ReturnValue value, RuntimeType type, LineInfo line) {
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
        public ReturnValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NewCall(value, type, line);
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
        public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            PascalPointer pointer = (PascalPointer) this.value.getValue(f, main);
            PointerType pointerType = (PointerType) ((PointerType) type.declType).pointedToType;
            DeclaredType type = pointerType.pointedToType;
            if (type instanceof ArrayType) {
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
            }
            return null;
        }
    }
}
