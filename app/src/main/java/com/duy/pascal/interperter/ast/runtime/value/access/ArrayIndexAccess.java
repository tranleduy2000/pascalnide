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
import com.duy.pascal.interperter.ast.runtime.references.ArrayIndexReference;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableAssignableNode;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.exceptions.runtime.IndexOutOfBoundsException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;

import java.lang.reflect.Array;

public class ArrayIndexAccess extends DebuggableAssignableNode {
    private final RuntimeValue container;
    private final RuntimeValue index;
    private final int offset;

    public ArrayIndexAccess(RuntimeValue container, RuntimeValue index, int offset) {
        this.container = container;
        this.index = index;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return container + "[" + index + "]";
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        RuntimeType r = (container.getRuntimeType(context));
        return new RuntimeType(((ArrayType<?>) r.declType).elementType, r.writable);
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object cont = container.compileTimeValue(context);
        Object ind = index.compileTimeValue(context);
        if (ind == null || cont == null) {
            return null;
        } else {
            return Array.get(cont, ((int) ind) - offset);
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
        Object cont = container.getValue(f, main);
        Object i = index.getValue(f, main);
        int ind;
        if (i instanceof EnumElementValue) {
            ind = ((EnumElementValue) i).getIndex();
        } else {
            ind = Integer.parseInt(i.toString());
        }
        try {
            return Array.get(cont, ind - offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(this.getLineNumber(),
                    ind, offset, offset + ((Object[]) cont).length - 1);
        }
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Object value = index.getValue(f, main);
        int ind = 0;
        if (value instanceof Integer) {
            ind = (int) value;
        } else if (value instanceof EnumElementValue) {
            ind = ((EnumElementValue) value).getIndex();
        }
        return new ArrayIndexReference(cont, ind, offset);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new ArrayIndexAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context), offset);
    }

}
