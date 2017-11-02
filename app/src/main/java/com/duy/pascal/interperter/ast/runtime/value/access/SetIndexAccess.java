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
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.references.SetPreference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.debugable.DebuggableAssignableValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;

import java.util.LinkedList;

/**
 * Created by Duy on 25-May-17.
 */
public class SetIndexAccess extends DebuggableAssignableValue {
    private RuntimeValue container;
    private RuntimeValue index;

    public SetIndexAccess(RuntimeValue container, RuntimeValue index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
        RuntimeType r = (container.getRuntimeType(exprContext));
        return new RuntimeType(((SetType<?>) r.declType).getElementType(), r.writable);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        LinkedList cont = (LinkedList) container.compileTimeValue(context);
        Integer ind = (Integer) index.compileTimeValue(context);
        if (ind == null || cont == null) {
            return null;
        } else {
            return cont.get(ind);
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
        LinkedList cont = (LinkedList) container.getValue(f, main);
        Integer ind = (Integer) index.getValue(f, main);
        return cont.get(ind); //index out of bound

    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        LinkedList cont = (LinkedList) container.getValue(f, main);
        int ind = Integer.valueOf(index.getValue(f, main).toString());
        return new SetPreference(cont, ind);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new SetIndexAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context));
    }
}
