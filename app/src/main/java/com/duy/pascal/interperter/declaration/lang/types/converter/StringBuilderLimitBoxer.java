/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.declaration.lang.types.converter;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class StringBuilderLimitBoxer implements RuntimeValue {
    private RuntimeValue value;
    private RuntimeValue length;

    public StringBuilderLimitBoxer(RuntimeValue value, RuntimeValue length) {
        this.value = value;
        this.length = length;
    }

    public void setLength(RuntimeValue length) {
        this.length = length;
    }


    @Override
    public String toString() {
        return value.toString();
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (length == null)
            return new StringBuilder(value.getValue(f, main).toString());

        String original = value.getValue(f, main).toString();
        int len = (int) length.getValue(f, main);
        if (len > original.length()) {
            return new StringBuilder(original);
        } else {
            return new StringBuilder(original.substring(0, len));
        }
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        StringBuilder string = (StringBuilder) value.compileTimeValue(context);
        if (string != null) {
            int len = (int) length.compileTimeValue(context);
            if (len > string.length()) {
                return new StringBuilder(string);
            } else {
                return new StringBuilder(string.substring(0, len));
            }
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderLimitBoxer(value.compileTimeExpressionFold(context), length);
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}