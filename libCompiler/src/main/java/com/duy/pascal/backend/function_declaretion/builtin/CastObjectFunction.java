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

package com.duy.pascal.backend.function_declaretion.builtin;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime_value.FunctionCall;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.references.PascalReference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Casts an object to the class or the interface represented
 */
public class CastObjectFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new JavaClassBasedType(Object.class), true),
                    new RuntimeType(new JavaClassBasedType(Object.class), false)};

    @Override
    public String name() {
        return "cast";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue pointer = arguments[0];
        RuntimeValue value = arguments[1];
        return new InstanceObjectCall(pointer, value, line);
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
        return null;
    }

    private class InstanceObjectCall extends FunctionCall {
        private RuntimeValue value;
        private LineInfo line;
        private RuntimeValue pointer;

        InstanceObjectCall(RuntimeValue pointer, RuntimeValue value, LineInfo line) {
            this.value = value;
            this.pointer = pointer;
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
            return new InstanceObjectCall(pointer, value, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new InstanceObjectCall(pointer, value, line);
        }

        @Override
        protected String getFunctionName() {
            return "cast";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            //get reference of variable
            PascalReference pointer = (PascalReference) this.pointer.getValue(f, main);

            //get value of arg 2
            Object value = this.value.getValue(f, main);

            //get value of variable
            Object o = pointer.get();
            //get class of variable
            Class<?> aClass = o.getClass();

            //cast object to type of variable
            Object casted = aClass.cast(value);

            //set value
            pointer.set(casted);
            return null;
        }

    }
}
