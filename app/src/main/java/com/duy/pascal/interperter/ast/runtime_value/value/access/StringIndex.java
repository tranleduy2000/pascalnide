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

package com.duy.pascal.interperter.ast.runtime_value.value.access;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.runtime_value.references.StringIndexReference;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.debugable.DebuggableAssignableValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class StringIndex extends DebuggableAssignableValue {
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
        return str.charAt(ind - 1);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return new StringIndexReference(str, ind);
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
        boolean writable = string.getRuntimeType(f).writable;
        return new RuntimeType(BasicType.Character, writable);
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
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        StringBuilder str = (StringBuilder) string.compileTimeValue(context);
        if (str == null) {
            return 0;
        }
        int ind = (int) index.compileTimeValue(context);
        return str.charAt(ind - 1);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RuntimeValue cstr = string.compileTimeExpressionFold(context);
        RuntimeValue cind = index.compileTimeExpressionFold(context);
        return new StringIndex(cstr, cind);
    }
}
