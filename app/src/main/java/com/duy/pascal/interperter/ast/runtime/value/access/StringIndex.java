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
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.references.StringIndexReference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableAssignableNode;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.PascalStringIndexOutOfBoundsException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class StringIndex extends DebuggableAssignableNode {
    private RuntimeValue string;
    private RuntimeValue index;

    public StringIndex(RuntimeValue string, RuntimeValue index) {
        this.index = index;
        this.string = string;
    }

    @Override
    public String toString() {
        return string + "[" + index + "]";
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @NonNull
    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        try {
            return str.charAt(ind - 1);
        }  catch (StringIndexOutOfBoundsException e) {
            throw new PascalStringIndexOutOfBoundsException(ind);
        }
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return new StringIndexReference(str, ind);
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        boolean writable = string.getRuntimeType(context).writable;
        return new RuntimeType(BasicType.Character, writable);
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
    public Object compileTimeValue(CompileTimeContext context) throws Exception {
        StringBuilder str = (StringBuilder) string.compileTimeValue(context);
        if (str == null) {
            return 0;
        }
        int ind = (int) index.compileTimeValue(context);
        return str.charAt(ind - 1);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        RuntimeValue cstr = string.compileTimeExpressionFold(context);
        RuntimeValue cind = index.compileTimeExpressionFold(context);
        return new StringIndex(cstr, cind);
    }
}
