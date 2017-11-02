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
import com.duy.pascal.interperter.linenumber.LineInfo;

public class LowFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public Name getName() {
        return Name.create("Low");
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue object = arguments[0];
        RuntimeType type = object.getRuntimeType(f);
        return new LowCall(type, line);
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
        return BasicType.create(Object.class);
    }

    @Override
    public String description() {
        return null;
    }

    private class LowCall extends BuiltinFunctionCall {

        private LineInfo line;
        private RuntimeType type;

        LowCall(RuntimeType type, LineInfo line) {
            this.type = type;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
            return new RuntimeType(BasicType.create(Object.class), false);
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
            return new LowCall(type, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new LowCall(type, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "low";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            Type declType = type.declType;
            if (declType instanceof ArrayType) {
                if (((ArrayType) declType).isDynamic()) {
                    return 0;
                } else {
                    return ((ArrayType) declType).getBound().getFirst();
                }
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
