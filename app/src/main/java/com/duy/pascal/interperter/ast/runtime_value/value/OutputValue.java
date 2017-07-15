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

package com.duy.pascal.interperter.ast.runtime_value.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.system_function.io.OutputFormatter;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.utils.NullSafety;

import static com.duy.pascal.interperter.utils.NullSafety.zReturn;

/**
 * Created by Duy on 13-Jun-17.
 */

public class OutputValue implements RuntimeValue {
    private RuntimeValue target;
    @Nullable
    private RuntimeValue[] infoOutput;

    public OutputValue(RuntimeValue target, @Nullable @Size(2) RuntimeValue[] infoOutput) {
        this.target = target;
        this.infoOutput = infoOutput;
    }

    public RuntimeValue[] getOutputFormat() {
        return infoOutput;
    }

    public void setOutputFormat(@Size(2) @Nullable RuntimeValue[] formatInfo) {
        this.infoOutput = formatInfo;
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object value = target.getValue(f, main);
        StringBuilder out = new StringBuilder(OutputFormatter.getValueOutput(value));
        if (infoOutput != null) {
            if (infoOutput[1] != null) {
                int sizeOfReal = (int) infoOutput[1].getValue(f, main);
                out = OutputFormatter.formatDecimal(sizeOfReal, out);
            }

            if (infoOutput[0] != null) {
                int column = (int) infoOutput[0].getValue(f, main);
                while (out.length() < column) {
                    out.insert(0, " ");
                }
            }
        }
        return out;
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
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
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        Object value = target.compileTimeValue(context);
        if (NullSafety.isNullValue(value)) return zReturn(value);
        StringBuilder out = new StringBuilder(OutputFormatter.getValueOutput(value));
        if (infoOutput != null) {
            if (infoOutput[1] != null) {
                Object o = infoOutput[1].compileTimeValue(context);
                if (NullSafety.isNullValue(o)) return NullValue.get();
                int sizeOfReal = (int) o;
                out = OutputFormatter.formatDecimal(sizeOfReal, out);
            }

            if (infoOutput[0] != null) {
                Object o = infoOutput[0].compileTimeValue(context);
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
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return new OutputValue(target.compileTimeExpressionFold(context), infoOutput);
    }

    @Nullable
    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
