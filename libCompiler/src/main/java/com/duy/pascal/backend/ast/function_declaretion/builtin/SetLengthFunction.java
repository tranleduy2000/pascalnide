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
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.types.ArgumentType;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.types.VarargsType;
import com.duy.pascal.backend.types.set.ArrayType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SetLengthFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {
            new RuntimeType(BasicType.create(Object.class), true),
            new VarargsType(new RuntimeType(BasicType.Integer, false))};

    @Override
    public String getName() {
        return "setlength";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue array = arguments[0];
        RuntimeType type = array.getType(f);
        RuntimeValue size = arguments[1];
        return new SetLengthCall(array, type, size, line);
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

        private RuntimeValue array;
        private RuntimeType runtimeType;
        private RuntimeValue size;
        private LineInfo line;

        SetLengthCall(RuntimeValue array, RuntimeType type, RuntimeValue size, LineInfo line) {
            this.array = array;
            this.runtimeType = type;
            this.size = size;
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
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new SetLengthCall(array.compileTimeExpressionFold(context),
                    runtimeType, size.compileTimeExpressionFold(context), line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new SetLengthCall(array.compileTimeExpressionFold(c),
                    runtimeType, size.compileTimeExpressionFold(c), line);
        }

        @Override
        protected String getFunctionName() {
            return "setlength";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {

            Integer[] ranges = (Integer[]) size.getValue(f, main);
            DeclaredType type = ((PointerType) runtimeType.getDeclType()).pointedToType;

            PascalReference r = (PascalReference) array.getValue(f, main);
            if (type instanceof ArrayType) {
                Object[] old = (Object[]) r.get();
                System.out.println(Arrays.toString(old));
                Object[] array = (Object[]) Array.newInstance(
                        ((ArrayType) type).getElementType().getStorageClass(), ranges[0]);

                setInitValue(array, ((ArrayType) type).getElementType(), ranges, 0, old);
                r.set(array);
            } else if (type == BasicType.StringBuilder) {
                StringBuilder value = (StringBuilder) r.get();
                if (value == null) {
                    StringBuilder newV = new StringBuilder();
                    newV.setLength(ranges[0]);
                    r.set(newV);
                } else {
                    StringBuilder newV = new StringBuilder(value);
                    newV.setLength(ranges[0]);
                    r.set(newV);
                }
            }
            return null;
        }

        /**
         * range  =   3  4  5
         * array  = a[3][ ][ ]
         *
         * @param array
         * @param elementType - element type
         * @param ranges      | length of {@array} = {@ranges[index]}
         * @param index       |
         * @param old
         */
        private void setInitValue(Object[] array, DeclaredType elementType,
                                  Integer[] ranges, int index, @Nullable Object[] old) {
            if (elementType instanceof ArrayType) {
                if (index == ranges.length - 1) {
                    return;
                }
                ArrayType arrayType = (ArrayType) elementType;
                for (int i = 0; i < ranges[index]; i++) {
                    array[i] = Array.newInstance(arrayType.getElementType().getStorageClass(),
                            ranges[index + 1]);

                    setInitValue((Object[]) array[i], arrayType.getElementType(), ranges, index + 1,
                            (old != null && old.length > i) ? (Object[]) old[i] : null);
                }
            } else {
                if (old != null) {
                    System.arraycopy(old, 0, array, 0, Math.min(array.length, old.length));
                    for (Integer i = old.length ; i < ranges[index]; i++) {
                        Array.set(array, i, elementType.initialize());
                    }
                } else {
                    for (Integer i = 0; i < ranges[index]; i++) {
                        Array.set(array, i, elementType.initialize());
                    }
                }
            }
        }
    }
}
