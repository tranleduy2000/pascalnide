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

package com.duy.pascal.interperter.ast.runtime.operators.pointer;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.debugable.DebuggableAssignableValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class DerefEval extends DebuggableAssignableValue {
    RuntimeValue pointer;
    LineInfo line;

    public DerefEval(RuntimeValue pointer, LineInfo line) {
        this.pointer = pointer;
        this.line = line;
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @NonNull
    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Reference ref = (Reference) pointer.getValue(f, main);
        return ref.get();
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return (Reference) pointer.getValue(f, main);
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        RuntimeType pointertype = pointer.getRuntimeType(f);
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
}
