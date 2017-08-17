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

package com.duy.pascal.interperter.systemfunction.builtin;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.debug.CallStack;
import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerSubrangeType;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SetLengthFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {
            new RuntimeType(BasicType.create(Object.class), true),
            new VarargsType(new RuntimeType(BasicType.Integer, false))};

    @Override
    public Name getName() {
        return Name.create("SetLength");
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue array = arguments[0];
        RuntimeType type = array.getRuntimeType(f);
        RuntimeValue size = arguments[1];
        return new SetLengthCall(array, type, size, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class SetLengthCall extends BuiltinFunctionCall {

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
        public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
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
                throws Exception {
            return new SetLengthCall(array.compileTimeExpressionFold(context),
                    runtimeType, size.compileTimeExpressionFold(context), line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new SetLengthCall(array.compileTimeExpressionFold(c),
                    runtimeType, size.compileTimeExpressionFold(c), line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "setlength";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {

            Integer[] ranges = (Integer[]) size.getValue(f, main);
            Type type = ((PointerType) runtimeType.getRawType()).pointedToType;

            PascalReference r = (PascalReference) array.getValue(f, main);
            if (type instanceof ArrayType) {
                Object[] old = (Object[]) r.get();
                System.out.println(Arrays.toString(old));

                Object[] array = (Object[]) Array.newInstance(
                        ((ArrayType) type).getElementType().getStorageClass(), ranges[0]);

                //set bound from 0 to range[0]
                ((ArrayType) type).setBound(new IntegerSubrangeType(0, ranges[0]));
                //set default value for all element of array
                setInitValue(array, ((ArrayType) type).getElementType(), ranges, 0, old);
                r.set(array);
            } else if (type.equals(BasicType.StringBuilder)) {
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
            if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
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
        private void setInitValue(Object[] array, Type elementType,
                                  Integer[] ranges, int index, @Nullable Object[] old) {
            if (elementType instanceof ArrayType) {
                if (index == ranges.length - 1) {
                    return;
                }
                ArrayType arrayType = (ArrayType) elementType;
                arrayType.setBound(new IntegerSubrangeType(0, ranges[index + 1]));
                for (int i = 0; i < ranges[index]; i++) {
                    array[i] = Array.newInstance(arrayType.getElementType().getStorageClass(),
                            ranges[index + 1]);

                    setInitValue((Object[]) array[i], arrayType.getElementType(), ranges, index + 1,
                            (old != null && old.length > i) ? (Object[]) old[i] : null);
                }
            } else {
                if (old != null) {
                    System.arraycopy(old, 0, array, 0, Math.min(array.length, old.length));
                    for (Integer i = old.length; i < ranges[index]; i++) {
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
