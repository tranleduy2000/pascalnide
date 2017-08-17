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

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class SizeOfArrayFunction implements IMethodDeclaration {

    private static final String TAG = "LengthFunction";
    private ArgumentType[] argumentTypes = {
            new RuntimeType(new ArrayType<>(BasicType.create(Object.class), null), false)};

    @Override
    public Name getName() {
        return Name.create("SizeOf");
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue array = arguments[0];
        return new SizeOfArrayCall(array, line);
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
        return BasicType.Integer;
    }

    @Override
    public String description() {
        return null;
    }

    private class SizeOfArrayCall extends BuiltinFunctionCall {

        private LineInfo line;
        private RuntimeValue array;

        SizeOfArrayCall(RuntimeValue array, LineInfo line) {
            this.array = array;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
            return new RuntimeType(BasicType.Integer, false);
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
            return new SizeOfArrayCall(array.compileTimeExpressionFold(context), line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new SizeOfArrayCall(array.compileTimeExpressionFold(c), line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "sizeof";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            @SuppressWarnings("rawtypes")
            ArrayType arr = (ArrayType) array.getValue(f, main);
            int size = arr.getBound().getSize();
            Class storageClass = arr.elementType.getStorageClass();
            if (storageClass == int.class || storageClass == Integer.class) {
                return size * 4; //32 bit
            } else if (storageClass == long.class || storageClass == Long.class) {
                return size * 8; //64 bit
            } else if (storageClass == double.class || storageClass == Double.class) {
                return size * 8; //64 bit
            } else if (storageClass == char.class || storageClass == Character.class) {
                return size * 2; //16 bit
            }
            return 0;
        }
    }
}
