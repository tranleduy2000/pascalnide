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

package com.duy.pascal.interperter.function.io;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.function.builtin.IMethodDeclaration;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.libraries.file.FileLib;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.ui.debug.CallStack;

import java.io.File;

/**
 * Casts an object to the class or the interface represented
 */
public class ReadlnFileFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(BasicType.Text, true),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), true))};

    @Override
    public Name getName() {
        return Name.create("ReadLn");
    }

    @Override
    public FunctionCall generateCall(LineNumber line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        return new ReadLineFileCall(arguments[0], arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineNumber line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class ReadLineFileCall extends FunctionCall {
        private RuntimeValue args;
        private LineNumber line;
        private RuntimeValue filePreference;

        ReadLineFileCall(RuntimeValue filePreferences, RuntimeValue args, LineNumber line) {
            this.filePreference = filePreferences;
            this.args = args;
            this.line = line;
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
            return null;
        }

        @NonNull
        @Override
        public LineNumber getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineNumber lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new ReadLineFileCall(filePreference, args, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new ReadLineFileCall(filePreference, args, line);
        }

        @Override
        protected Name getFunctionName() {
            return Name.create("ReadLn");

        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            FileLib fileLib = main.getDeclaration().getContext().getFileHandler();

            PascalReference[] values = (PascalReference[]) args.getValue(f, main);
            PascalReference<File> file = (PascalReference<File>) filePreference.getValue(f, main);
            fileLib.readlnz(file.get(), values);
            if (main.isDebug()) main.getDebugListener().onValueVariableChanged(new CallStack(f));

            return null;
        }

    }
}
