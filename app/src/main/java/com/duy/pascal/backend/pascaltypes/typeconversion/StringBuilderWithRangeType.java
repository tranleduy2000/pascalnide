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

package com.duy.pascal.backend.pascaltypes.typeconversion;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderWithRangeType implements RValue {
    protected RValue[] outputFormat;
    private RValue value;
    private RValue length;


    public StringBuilderWithRangeType(RValue value, RValue length) {
        this.value = value;
        this.length = length;
        this.outputFormat = value.getOutputFormat();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public RValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }


    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
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
    public RuntimeType get_type(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.create(StringBuilder.class), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object o = value.compileTimeValue(context);
        if (o != null) {
            return new StringBuilder(o.toString());
        } else {
            return null;
        }
    }


    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderWithRangeType(value.compileTimeExpressionFold(context), length);
    }

    @Override
    public LValue asLValue(ExpressionContext f) {
        return null;
    }
}