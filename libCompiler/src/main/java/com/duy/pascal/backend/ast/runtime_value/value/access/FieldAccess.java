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

package com.duy.pascal.backend.ast.runtime_value.value.access;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.FieldReference;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;
import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.types.JavaClassBasedType;
import com.duy.pascal.backend.types.ObjectType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.backend.types.RuntimeType;

public class FieldAccess extends DebuggableAssignableValue {
    private static final String TAG = "FieldAccess";
    private RuntimeValue container;
    private String name;
    private LineInfo line;

    public FieldAccess(RuntimeValue container, String name, LineInfo line) {
        this.container = container;
        this.name = name;
        this.line = line;
    }

    public FieldAccess(RuntimeValue container, WordToken name) {
        this(container, name.name, name.getLineNumber());
    }

    @Override
    public String toString() {
        return container.toString() + "." + name;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType r = container.getType(f);
        if (r.declType instanceof ObjectType) {
            return new RuntimeType(((ObjectType) (r.declType)).getMemberType(name), r.writable);
        } else if (r.declType instanceof JavaClassBasedType) {
            return new RuntimeType(r.declType, r.writable);
        } else if (r.declType instanceof PointerType) {
            return new RuntimeType(r.declType, r.writable);
        }
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
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object value = container.compileTimeValue(context);
        if (value != null) {
            try {
                return ((ContainsVariables) value).getVar(name);
            } catch (RuntimePascalException e) {
                throw new ConstantCalculationException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @NonNull
    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value = container.getValue(f, main);
        return ((ContainsVariables) value).getVar(name);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object v = container.getValue(f, main);
        return new FieldReference((ContainsVariables) v, name);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);
        } else {
            return new FieldAccess(container.compileTimeExpressionFold(context), name, line);
        }
    }

    public RuntimeValue getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }
}
