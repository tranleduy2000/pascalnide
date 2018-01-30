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

package com.duy.pascal.interperter.function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.TypeMismatchException;
import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * length of one dimension array
 */
public class LengthFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {
            new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public Name getName() {
        return Name.create("Length");
    }

    @Override
    public FunctionCall generateCall(LineNumber line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue array = arguments[0];
        RuntimeType type = array.getRuntimeType(f);
        return new LengthCall(array, type.declType, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineNumber line, RuntimeValue[] values, ExpressionContext f) throws Exception {
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

    private class LengthCall extends BuiltinFunctionCall {

        private Type type;
        private LineNumber line;
        private RuntimeValue array;

        LengthCall(RuntimeValue array, Type declaredType, LineNumber line) {
            this.array = array;
            type = declaredType;
            this.line = line;
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
            return new RuntimeType(BasicType.Integer, false);
        }

        @NonNull
        @Override
        public LineNumber getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineNumber lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new LengthCall(array.compileTimeExpressionFold(context), type, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new LengthCall(array.compileTimeExpressionFold(c), type, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "length";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            Object value = array.getValue(f, main);
            if (type instanceof ArrayType) {
                return ((ArrayType) type).getBound().getSize();
            } else if (value instanceof StringBuilder) {
                return ((StringBuilder) value).length();
            } else if (value instanceof String) {
                return ((String) value).length();
            } else if (type instanceof EnumGroupType) {
                return ((EnumGroupType) type).getSize();
            } else {
                // TODO: 02-May-17  check exception
                throw new TypeMismatchException(line, getFunctionName(),
                        new Type[]{BasicType.StringBuilder,
                                new ArrayType<>(BasicType.create(Object.class), null)}, type);
            }
        }
    }
}
