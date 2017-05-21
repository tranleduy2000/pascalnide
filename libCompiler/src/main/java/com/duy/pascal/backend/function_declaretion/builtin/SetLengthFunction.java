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
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime_value.FunctionCall;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.references.PascalReference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class SetLengthFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {
            new RuntimeType(new ArrayType<>(BasicType.create(Object.class),
                    new SubrangeType()), true),
            new RuntimeType(BasicType.Integer, false)};

    @Override
    public String name() {
        return "setlength";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue array = arguments[0];
        RuntimeValue size = arguments[1];
        @SuppressWarnings("rawtypes")
        DeclaredType elementYype = ((ArrayType)
                ((PointerType) array.getType(f).declType).pointedToType).elementType;
        return new SetLengthCall(array, size, elementYype, line);
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

    private class SetLengthCall extends FunctionCall {

        RuntimeValue array;
        RuntimeValue size;
        DeclaredType elemtype;

        LineInfo line;

        SetLengthCall(RuntimeValue array, RuntimeValue size, DeclaredType elemType, LineInfo line) {
            this.array = array;
            this.size = size;
            this.elemtype = elemType;
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
            return new SetLengthCall(array.compileTimeExpressionFold(context),
                    size.compileTimeExpressionFold(context), elemtype, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new SetLengthCall(array.compileTimeExpressionFold(c),
                    size.compileTimeExpressionFold(c), elemtype, line);
        }

        @Override
        protected String getFunctionName() {
            return "setlength";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            int length = (int) size.getValue(f, main);
            PascalReference a = (PascalReference) array.getValue(f, main);
            Object arr = a.get();
            int oldlength = Array.getLength(arr);
            Object newarr = Array.newInstance(elemtype.getTransferClass(), length);
            if (oldlength > length) {
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(arr, 0, newarr, 0, length);
            } else {
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(arr, 0, newarr, 0, oldlength);
                for (int i = oldlength; i < length; i++) {
                    Array.set(newarr, i, elemtype.initialize());
                }
            }
            a.set(newarr);
            return null;
        }
    }
}
