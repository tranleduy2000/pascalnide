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

package com.duy.pascal.interperter.ast.runtime.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.systemfunction.io.OutputFormatter;
import com.duy.pascal.interperter.utils.NullSafety;

import static com.duy.pascal.interperter.utils.NullSafety.zReturn;

/**
 * Created by Duy on 13-Jun-17.
 */

public class OutputValue implements RuntimeValue {
    private RuntimeValue target;
    @Nullable
    private RuntimeValue[] outputFormat;

    public OutputValue(RuntimeValue target, @Nullable @Size(2) RuntimeValue[] outputFormat) {
        this.target = target;
        this.outputFormat = outputFormat;
    }

    @Nullable
    public RuntimeValue[] getOutputFormat() {
        return outputFormat;
    }


    @NonNull
    @Override
    public StringBuilder getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object value = target.getValue(f, main);
        if (outputFormat != null) {
            StringBuilder out = new StringBuilder(OutputFormatter.getValueOutput(value));
            boolean formatDouble = outputFormat[1] != null;
            if (formatDouble) {
                int sizeOfReal = (int) outputFormat[1].getValue(f, main);
                out = OutputFormatter.formatDecimal(sizeOfReal, out);
            }
            if (outputFormat[0] != null) {
                int column = (int) outputFormat[0].getValue(f, main);
                while (out.length() < column) {
                    out.insert(0, " ");
                }
            }
            return out;
        } else {
            StringBuilder out = new StringBuilder(OutputFormatter.getValueOutput(value));
            return out;
        }
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return target.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Nullable
    @Override
    public Object compileTimeValue(CompileTimeContext context) throws Exception {
        Object value = target.compileTimeValue(context);
        if (NullSafety.isNullValue(value)) return zReturn(value);
        StringBuilder out = new StringBuilder(OutputFormatter.getValueOutput(value));
        if (outputFormat != null) {
            if (outputFormat[1] != null) {
                Object o = outputFormat[1].compileTimeValue(context);
                if (NullSafety.isNullValue(o)) return NullValue.get();
                int sizeOfReal = (int) o;
                out = OutputFormatter.formatDecimal(sizeOfReal, out);
            }

            if (outputFormat[0] != null) {
                Object o = outputFormat[0].compileTimeValue(context);
                if (NullSafety.isNullValue(o)) return NullValue.get();
                int column = (int) o;
                while (out.length() < column) {
                    out.insert(0, " ");
                }
            }
        }
        return out;
    }

    @Nullable
    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        return new OutputValue(target.compileTimeExpressionFold(context), outputFormat);
    }

    @Nullable
    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
