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
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ExitFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = {new RuntimeType(BasicType.create(Object.class), false)};

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnValue array = arguments[0];
        return new ExitCall(array, line);
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

    private class ExitCall extends FunctionCall {

        private LineInfo line;
        private ReturnValue value;

        ExitCall(ReturnValue value, LineInfo line) {
            this.value = value;
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
            return new ExitCall(value.compileTimeExpressionFold(context), line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new ExitCall(value.compileTimeExpressionFold(c), line);
        }

        @Override
        protected String getFunctionName() {
            return "exit";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            if (f instanceof FunctionOnStack) {
                if (((FunctionOnStack) f).isProcedure()) {
                    throw new RuntimeException();
                } else {
                    String name = ((FunctionOnStack) f).getCurrentFunction().name;
                    Object value = this.value.getValue(f, main);
                    f.setLocalVar(name, value);
                }
            } else {
                // TODO: 30-Apr-17  check exception
            }
            return ExecutionResult.EXIT;
        }
    }
}
