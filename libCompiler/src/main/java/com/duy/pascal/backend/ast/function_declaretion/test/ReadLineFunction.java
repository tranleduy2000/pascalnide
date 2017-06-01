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

package com.duy.pascal.backend.ast.function_declaretion.test;


import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.function_declaretion.builtin.IMethodDeclaration;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.builtin_libraries.io.IOLib;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.VarargsType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

/**
 * Casts an object to the class or the interface represented
 */
public class ReadLineFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new VarargsType(new RuntimeType(BasicType.create(Object.class), true))};

    @Override
    public String getName() {
        return "readln";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        return new ReadLineCall(arguments[0], line);
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

    private class ReadLineCall extends FunctionCall {
        private RuntimeValue args;
        private LineInfo line;

        ReadLineCall(RuntimeValue args, LineInfo line) {
            this.args = args;
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
            return new ReadLineCall( args, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws ParsingException {
            return new ReadLineCall( args, line);
        }

        @Override
        protected String getFunctionName() {
            return "readln";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            IOLib ioHandler = main.getDefinition().getContext().getIOHandler();
            PascalReference[] values = (PascalReference[]) args.getValue(f, main);
            ioHandler.readlnz(values);
            return null;
        }

    }
}
