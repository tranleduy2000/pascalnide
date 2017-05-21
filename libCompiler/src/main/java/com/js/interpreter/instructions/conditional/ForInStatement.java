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

package com.js.interpreter.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.runtime_value.AssignableValue;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.references.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 14-May-17.
 */

public class ForInStatement extends DebuggableExecutable {
    private Executable command;
    private AssignableValue item;
    private RuntimeValue list;
    private LineInfo line;

    public ForInStatement(ExpressionContext context, GrouperToken grouperToken, LineInfo lineInfo)
            throws ParsingException {
        this.line = lineInfo;
        RuntimeValue nextExpression = grouperToken.getNextExpression(context);
        AssignableValue target = nextExpression.asAssignableValue(context);

    }

    /**
     * foreach loop
     * <p>
     * for s in ['1', '2', '3'] do
     */
    public ForInStatement(AssignableValue item,
                          RuntimeValue list, Executable command,
                          LineInfo line) throws ParsingException {
        this.item = item;
        this.list = list;
        this.line = line;
        this.command = command;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object[] array = (Object[]) list.getValue(context, main);
        for (Object o : array) {
            Reference reference = item.getReference(context, main);
            reference.set(o);
            ExecutionResult result = command.execute(context, main);
            switch (result) {
                case BREAK:
                    break;
                case EXIT:
                    return ExecutionResult.EXIT;
                case CONTINUE:
                    continue;
            }
        }
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new ForInStatement(item, list, command, line);
    }
}
