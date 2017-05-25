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

package com.duy.pascal.backend.function_declaretion.builtin;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class ExitNoneFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes = new ArgumentType[]{};

    @Override
   public String getName() {
        return "exit";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        return new ExitNoneCall(line);
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
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class ExitNoneCall extends FunctionCall {

        private LineInfo line;

        ExitNoneCall(LineInfo line) {
            this.line = line;
        }

        @Override
        public RuntimeType getType(ExpressionContext f) throws ParsingException {
            return null;
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
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new ExitNoneCall(line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new ExitNoneCall(line);
        }

        @Override
        protected String getFunctionName() {
            return "exit";
        }

        @Override
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            return ExecutionResult.EXIT;
        }
    }
}
