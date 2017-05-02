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

package com.duy.pascal.backend.lib.templated.lowhigh;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

class LowCall extends FunctionCall {

    private LineInfo line;
    private RuntimeType type;

    LowCall(RuntimeType type, LineInfo line) {
        this.type = type;
        this.line = line;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Integer, false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return null;
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new LowCall(type, line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new LowCall(type, line);
    }

    @Override
    protected String getFunctionName() {
        return "low";
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        DeclaredType declType = type.declType;
        if (declType instanceof ArrayType) {
            return ((ArrayType) declType).getBounds().lower;
        } else if (BasicType.Byte.equals(declType)) {
            return Byte.MIN_VALUE;
        } else if (BasicType.Short.equals(declType)) {
            return Short.MIN_VALUE;
        } else if (BasicType.Integer.equals(declType)) {
            return Integer.MIN_VALUE;
        } else if (BasicType.Long.equals(declType)) {
            return Long.MIN_VALUE;
        } else if (BasicType.Double.equals(declType)) {
            return Double.MIN_VALUE;
        } else if (BasicType.Character.equals(declType)) {
            return Character.MIN_VALUE;
        }
        return 0;
    }
}
