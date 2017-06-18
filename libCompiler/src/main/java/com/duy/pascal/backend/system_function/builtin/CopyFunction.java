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

package com.duy.pascal.backend.system_function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.declaration.types.ArgumentType;
import com.duy.pascal.backend.declaration.types.BasicType;
import com.duy.pascal.backend.declaration.types.DeclaredType;
import com.duy.pascal.backend.declaration.types.RuntimeType;
import com.duy.pascal.backend.declaration.types.set.ArrayType;

import java.lang.reflect.Array;

/**
 * length of one dimension array
 */
public class CopyFunction implements IMethodDeclaration {

    private static final ArgumentType[] argumentTypes = {
            new RuntimeType(new ArrayType<>(BasicType.create(Object.class), null), false),
            new RuntimeType(BasicType.Integer, false),
            new RuntimeType(BasicType.Integer, false)};

    private ArrayType type;

    @Override
    public String getName() {
        return "copy";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue array = arguments[0];
        RuntimeType type = array.getType(f);
        this.type = (ArrayType) type.declType;
        return new LengthCall(array, type.declType, arguments[1], arguments[2], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return new ArrayType<>(BasicType.create(Object.class), null);
    }

    @Override
    public String description() {
        return null;
    }

    private class LengthCall extends FunctionCall {

        private final RuntimeValue index;
        private final RuntimeValue count;
        private final ArrayType type;
        private final LineInfo line;
        private final RuntimeValue array;

        public LengthCall(RuntimeValue array, DeclaredType type, RuntimeValue index,
                          RuntimeValue count, LineInfo line) {
            this.array = array;
            this.type = (ArrayType) type;
            this.index = index;
            this.count = count;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return new RuntimeType(new ArrayType<>(type.getElementType(), null), false);
        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
//            return new LengthCall(array.compileTimeExpressionFold(context), type, line);
            return null;
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
//            return new LengthCall(array.compileTimeExpressionFold(c), type, line);
            return null;
        }

        @Override
        protected String getFunctionName() {
            return "copy";
        }

        @Override
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            Object[] array = (Object[]) this.array.getValue(f, main);
            int from = (int) index.getValue(f, main);
            int count = (int) this.count.getValue(f, main);
            if (array.length == 0) return array;
            Object[] o = (Object[]) Array.newInstance(array[0].getClass(), count);
            System.arraycopy(array, from, o, 0, count);
            return o;
        }
    }
}
