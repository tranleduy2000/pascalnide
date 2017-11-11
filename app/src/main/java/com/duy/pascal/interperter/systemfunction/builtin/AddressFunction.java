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
import com.duy.pascal.interperter.ast.runtime.operators.pointer.DerefEval;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class AddressFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};
    private PointerType pointerType;

    @Override
    public Name getName() {
        return Name.create("Addr");
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue pointer = arguments[0];
        this.pointerType = (PointerType) pointer.getRuntimeType(f).declType;
        return new AddressFunctionCall(pointer, line);
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
        return pointerType;
    }

    @Override
    public String description() {
        return null;
    }

    private class AddressFunctionCall extends BuiltinFunctionCall {

        private RuntimeValue pointer;
        private LineInfo line;

        public AddressFunctionCall(RuntimeValue pointer, LineInfo line) {
            this.pointer = pointer;
            this.line = line;
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
            Reference ref = (Reference) pointer.getValue(f, main);
            return ref.get();
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
            RuntimeType pointertype = pointer.getRuntimeType(exprContext);
            return new RuntimeType(((PointerType) pointertype.declType).pointedToType, true);
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
        public Node compileTimeConstantTransform(CompileTimeContext c) throws Exception {
            return null;
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) throws Exception {
            Reference<?> ref = (Reference<?>) pointer.compileTimeValue(context);
            if (ref != null) {
                try {
                    return ref.get();
                } catch (RuntimePascalException e) {
                    throw new ConstantCalculationException(e);
                }
            }
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
            Object val = this.compileTimeValue(context);
            if (val != null) {
                return new ConstantAccess<>(val, line);
            } else {
                return new DerefEval(pointer.compileTimeExpressionFold(context), line);
            }
        }

        @Override
        protected String getFunctionNameImpl() {
            return "addr";
        }
    }
}
