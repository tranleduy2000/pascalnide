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

package com.duy.pascal.backend.function_declaretion;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.function_declaretion.abstract_class.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class SizeOfArrayFunction implements IMethodDeclaration {

    private static final String TAG = "LengthFunction";
    private ArgumentType[] argumentTypes = {
            new RuntimeType(new ArrayType<>(BasicType.create(Object.class), new SubrangeType(0, -1)),
                    false)};

    @Override
    public String name() {
        return "sizeof";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnValue array = arguments[0];
        return new SizeOfArrayCall(array, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, ReturnValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return BasicType.Integer;
    }

    @Override
    public String description() {
        return null;
    }

    private class SizeOfArrayCall extends FunctionCall {

        private LineInfo line;
        private ReturnValue array;

        SizeOfArrayCall(ReturnValue array, LineInfo line) {
            this.array = array;
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
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
        public ReturnValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new SizeOfArrayCall(array.compileTimeExpressionFold(context), line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new SizeOfArrayCall(array.compileTimeExpressionFold(c), line);
        }

        @Override
        protected String getFunctionName() {
            return "sizeof";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            @SuppressWarnings("rawtypes")
            ArrayType arr = (ArrayType) array.getValue(f, main);
            int size = arr.getBounds().size;
            Class storageClass = arr.element_type.getStorageClass();
            if (storageClass == int.class || storageClass == Integer.class) {
                return size * 4; //32 bit
            } else if (storageClass == long.class || storageClass == Long.class) {
                return size * 8; //64 bit
            } else if (storageClass == double.class || storageClass == Double.class) {
                return size * 8; //64 bit
            } else if (storageClass == char.class || storageClass == Character.class) {
                return size * 2; //16 bit
            }
            return 0;
        }
    }
}
