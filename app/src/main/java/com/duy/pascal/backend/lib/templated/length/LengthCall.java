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

package com.duy.pascal.backend.lib.templated.length;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.runtime.exception.TypeMismatchException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

class LengthCall extends FunctionCall {

    private DeclaredType type;
    private LineInfo line;
    private RValue array;

    LengthCall(RValue array, DeclaredType declaredType, LineInfo line) {
        this.array = array;
        type = declaredType;
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
        return new LengthCall(array.compileTimeExpressionFold(context), type, line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new LengthCall(array.compileTimeExpressionFold(c), type, line);
    }

    @Override
    protected String getFunctionName() {
        return "length";
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object value = array.getValue(f, main);
        if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            return Array.getLength(arr);
        } else if (value instanceof StringBuilder) {
            return ((StringBuilder) value).length();
        } else if (value instanceof String) {
            return ((String) value).length();
        } else {
            // TODO: 02-May-17  check exception
            throw new TypeMismatchException(line, getFunctionName(),
                    new DeclaredType[]{BasicType.StringBuilder,
                            new ArrayType<>(BasicType.create(Object.class), new SubrangeType())},
                    type);
        }
    }
}
