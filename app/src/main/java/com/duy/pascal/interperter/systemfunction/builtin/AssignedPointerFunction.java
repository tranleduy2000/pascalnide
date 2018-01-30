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
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.utils.NullSafety;

/**
 * Check if a pointer is valid
 */
public class AssignedPointerFunction implements IMethodDeclaration {
    private final static String NAME = "Assigned";
    private ArgumentType[] argumentTypes = {new RuntimeType(
            new PointerType(BasicType.create(Object.class)), false)};

    @Override
     public Name getName() {
        return Name.create("Assigned");
    }

    @Override
    public FunctionCall generateCall(LineNumber line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue value = arguments[0];
        return new AssignedCall(value, value.getRuntimeType(f), line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineNumber line, RuntimeValue[] values,
                                               ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return BasicType.Boolean;
    }

    @Override
    public String description() {
        return null;
    }

    private class AssignedCall extends BuiltinFunctionCall {

        private RuntimeValue value;
        private RuntimeType type;
        private LineNumber line;

        AssignedCall(RuntimeValue value, RuntimeType type, LineNumber line) {
            this.value = value;
            this.type = type;
            this.line = line;
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
            return new RuntimeType(BasicType.Boolean, false);
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
            return new AssignedCall(value, type, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new AssignedCall(value, type, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return NAME;
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f,
                                   @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            return NullSafety.isNullValue(value.getValue(f, main));
        }
    }
}
