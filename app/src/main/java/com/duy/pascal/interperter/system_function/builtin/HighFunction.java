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

package com.duy.pascal.interperter.system_function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerRange;

public class HighFunction implements IMethodDeclaration {
    private RuntimeType runtimeType;
    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public String getName() {
        return "high";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue value = arguments[0];
        this.runtimeType = value.getRuntimeType(f);
        return new HighCall(value, line);
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
        if (runtimeType != null) {
            if (runtimeType.declType instanceof ArrayType) {
                return BasicType.Integer;
            } else {
                return BasicType.create(Object.class);
            }
        } else {
            return BasicType.create(Object.class);
        }
    }

    @Override
    public String description() {
        return null;
    }

    private class HighCall extends FunctionCall {

        private RuntimeValue value;
        private LineInfo line;

        HighCall(RuntimeValue value, LineInfo line) {
            this.value = value;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
            return new RuntimeType(HighFunction.this.returnType(), false);
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
            return new HighCall(value, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new HighCall(value, line);
        }

        @Override
        protected String getFunctionName() {
            return "high";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {

            Type declType = runtimeType.declType;
            if (declType instanceof ArrayType) {
                IntegerRange bounds = ((ArrayType) declType).getBound();
                Object[] value = (Object[]) this.value.getValue(f, main);
                int size = value.length;
                if (bounds == null) {
                    return size - 1;
                } else {
                    return bounds.getFirst() + size - 1;
                }
            } else if (BasicType.Byte.equals(declType)) {
                return Byte.MAX_VALUE;
            } else if (BasicType.Short.equals(declType)) {
                return Short.MAX_VALUE;
            } else if (BasicType.Integer.equals(declType)) {
                return Integer.MAX_VALUE;
            } else if (BasicType.Long.equals(declType)) {
                return Long.MAX_VALUE;
            } else if (BasicType.Float.equals(declType)) {
                return Float.MAX_VALUE;
            } else if (BasicType.Double.equals(declType)) {
                return Double.MAX_VALUE;
            } else if (BasicType.Character.equals(declType)) {
                return Character.MAX_VALUE;
            } else if (declType instanceof EnumGroupType) {
                EnumGroupType enumGroupType = (EnumGroupType) declType;
                return enumGroupType.get(enumGroupType.getSize() - 1);
            }
            return null;
        }
    }
}
