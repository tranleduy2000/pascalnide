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
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.enumtype.EnumGroupType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;

public class HighFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public String getName() {
        return "high";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue value = arguments[0];
        RuntimeType type = value.getType(f);
        return new HighCall(type, value, line);
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

    static class HighCall extends FunctionCall {

        private RuntimeValue value;
        private LineInfo line;
        private RuntimeType type;

        HighCall(RuntimeType type, RuntimeValue value, LineInfo line) {
            this.type = type;
            this.value = value;
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
            return new HighCall(type, value, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new HighCall(type, value, line);
        }

        @Override
        protected String getFunctionName() {
            return "high";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            DeclaredType declType = type.declType;
            if (declType instanceof ArrayType) {
                SubrangeType bounds = ((ArrayType) declType).getBounds();
                Object[] value = (Object[]) this.value.getValue(f, main);
                int size = value.length - 1;
                return bounds.lower + size - 1;
            } else if (BasicType.Byte.equals(declType)) {
                return Byte.MAX_VALUE;
            } else if (BasicType.Short.equals(declType)) {
                return Short.MAX_VALUE;
            } else if (BasicType.Integer.equals(declType)) {
                return Integer.MAX_VALUE;
            } else if (BasicType.Long.equals(declType)) {
                return Long.MAX_VALUE;
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
