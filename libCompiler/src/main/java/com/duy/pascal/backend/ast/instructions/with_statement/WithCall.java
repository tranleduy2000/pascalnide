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

package com.duy.pascal.backend.ast.instructions.with_statement;

import com.duy.pascal.backend.debugable.DebuggableExecutableReturnValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.ArrayList;

public class WithCall extends DebuggableExecutableReturnValue {

    public ArrayList<RuntimeValue> arguments;
    private WithStatement withStatement;
    private LineInfo line;

    public WithCall(WithStatement withStatement, ArrayList<RuntimeValue> arguments, LineInfo line) {
        this.withStatement = withStatement;
        this.line = line;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "with";
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object valueImpl = getValueImpl(f, main);
        if (valueImpl == ExecutionResult.EXIT) {
            return ExecutionResult.EXIT;
        }
        return ExecutionResult.NONE;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (main != null) {
            if (main.isDebugMode()) {
                main.getDebugListener().onLine(getLineNumber());
            }
            main.incStack(getLineNumber());
            main.scriptControlCheck(getLineNumber());
        }

        withStatement.execute(f, main);

        if (main != null)
            main.decStack();
        return ExecutionResult.NONE;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) {
        return null;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new WithCall(withStatement, arguments, line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new WithCall(withStatement, arguments, line);
    }
}
