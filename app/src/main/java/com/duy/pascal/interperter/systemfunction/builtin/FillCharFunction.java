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
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class FillCharFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), true),
            new RuntimeType(BasicType.Integer, false),
            new RuntimeType(BasicType.Character, false)};

    @Override
    public Name getName() {
        return Name.create("FillChar");
    }

    @Override
    public FillCharCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        return new FillCharCall(arguments, line);
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

    private static class FillCharCall extends BuiltinFunctionCall {

        private final RuntimeValue[] arguments;
        private LineInfo line;

        public FillCharCall(RuntimeValue[] arguments, LineInfo line) {
            this.arguments = arguments;
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
            return new FillCharCall(arguments, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new FillCharCall(arguments, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "fillchar";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            PascalReference array = (PascalReference) arguments[0].getValue(f, main);
            int size = (int) arguments[1].getValue(f, main);
            char value = (char) arguments[2].getValue(f, main);
            if (array.get() instanceof StringBuilder) {
                StringBuilder s = (StringBuilder) array.get();
                if (s == null || s.length() < size) {
                    s = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        s.append(value);
                    }
                    array.set(s);
                    return null;
                }
                for (int i = 0; i < size; i++) {
                    s.setCharAt(i, value);
                }
            } else if (array.get() instanceof String) {
                String s = (String) array.get();
                if (s == null || s.length() < size) {
                    StringBuilder tmp = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        tmp.append(value);
                    }
                    array.set(s.toString());
                    return null;
                }
                StringBuilder stringBuilder = new StringBuilder(s);
                for (int i = 0; i < size; i++) {
                    stringBuilder.setCharAt(i, value);
                }
                array.set(stringBuilder.toString());
            } else if (array.get() instanceof Object[]) {
            }
            return null;
        }
    }
}
