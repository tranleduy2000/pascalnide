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
import com.duy.pascal.interperter.ast.runtime.ObjectBasedPointer;
import com.duy.pascal.interperter.ast.runtime.references.PascalPointer;
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

public class NewFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};

    @Override
    public Name getName() {
        return Name.create("New");
    }

    @Override
    public FunctionCall generateCall(LineNumber line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue pointer = arguments[0];
        RuntimeType type = pointer.getRuntimeType(f);
        return new NewCall(pointer, type, line);
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
        return null;
    }

    @Override
    public String description() {
        return "Dynamically allocate memory for variable";
    }

    private class NewCall extends BuiltinFunctionCall {

        private RuntimeValue value;
        private RuntimeType type;
        private LineNumber line;

        NewCall(RuntimeValue value, RuntimeType type, LineNumber line) {
            this.value = value;
            this.type = type;
            this.line = line;
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
            return null;
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
            return new NewCall(value, type, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new NewCall(value, type, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "new";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            PascalPointer pointer = (PascalPointer) this.value.getValue(f, main);
            PointerType pointerType = (PointerType) ((PointerType) type.declType).pointedToType;
            Type type = pointerType.pointedToType;
            pointer.set(new ObjectBasedPointer<>(type.initialize()));
            return null;
        }
    }
}
