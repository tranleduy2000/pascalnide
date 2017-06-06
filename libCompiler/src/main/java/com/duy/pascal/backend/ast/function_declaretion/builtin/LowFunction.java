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


import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.set.ArrayType;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.set.EnumGroupType;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;

public class LowFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public String getName() {
        return "low";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                                      ExpressionContext f) throws ParsingException {
        RuntimeValue object = arguments[0];
        RuntimeType type = object.getType(f);
        return new LowCall(type, line);
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
        return BasicType.create(Object.class);
    }

    @Override
    public String description() {
        return null;
    }

    private class LowCall extends FunctionCall {

        private LineInfo line;
        private RuntimeType type;

        LowCall(RuntimeType type, LineInfo line) {
            this.type = type;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return new RuntimeType(BasicType.create(Object.class), false);
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
            return new LowCall(type, line);
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new LowCall(type, line);
        }

        @Override
        protected String getFunctionName() {
            return "low";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            DeclaredType declType = type.declType;
            if (declType instanceof ArrayType) {
                return ((ArrayType) declType).getBounds().lower;
            } else if (BasicType.Byte.equals(declType)) {
                return Byte.MIN_VALUE;
            } else if (BasicType.Short.equals(declType)) {
                return Short.MIN_VALUE;
            } else if (BasicType.Integer.equals(declType)) {
                return Integer.MIN_VALUE;
            } else if (BasicType.Long.equals(declType)) {
                return Long.MIN_VALUE;
            } else if (BasicType.Double.equals(declType)) {
                return Double.MIN_VALUE;
            } else if (BasicType.Float.equals(declType)) {
                return Float.MIN_VALUE;
            } else if (BasicType.Character.equals(declType)) {
                return Character.MIN_VALUE;
            } else if (declType instanceof EnumGroupType) {
                EnumGroupType enumGroupType = (EnumGroupType) declType;
                return enumGroupType.get(0);
            }
            return null;
        }
    }
}
