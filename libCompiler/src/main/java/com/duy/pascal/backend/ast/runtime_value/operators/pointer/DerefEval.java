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

package com.duy.pascal.backend.ast.runtime_value.operators.pointer;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.data_types.PointerType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

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
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType pointertype = pointer.getType(f);
        return new RuntimeType(((PointerType) pointertype.declType).pointedToType, true);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
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
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new DerefEval(pointer.compileTimeExpressionFold(context), line);
        }
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }
}
