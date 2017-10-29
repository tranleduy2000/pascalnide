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

package com.duy.pascal.interperter.ast.runtime.value.access;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.FieldReference;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.ContainsVariables;
import com.duy.pascal.interperter.debugable.DebuggableAssignableValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.declaration.lang.types.PascalClassType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.ObjectType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class FieldAccess extends DebuggableAssignableValue {
    private static final String TAG = "FieldAccess";
    private RuntimeValue container;
    private Name name;
    private LineInfo line;

    public FieldAccess(RuntimeValue container, Name name, LineInfo line) {
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
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        RuntimeType r = container.getRuntimeType(f);
        if (r.declType instanceof PascalClassType) {
            return new RuntimeType(((PascalClassType) (r.declType)).getMemberType(name), r.writable);
        } else if (r.declType instanceof ObjectType) {
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
            throws Exception {
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
            throws Exception {
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

    public Name getName() {
        return name;
    }
}
