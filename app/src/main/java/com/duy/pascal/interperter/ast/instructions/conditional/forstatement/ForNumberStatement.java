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

package com.duy.pascal.interperter.ast.instructions.conditional.forstatement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.ui.debug.CallStack;

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForNumberStatement<T extends Number> extends DebuggableExecutable {

    private Executable command;
    private AssignableValue tempVar;
    private RuntimeValue first;
    private RuntimeValue last;
    private LineInfo line;
    private boolean downto = false;
    private Type mNumberType = null;

    public ForNumberStatement(ExpressionContext f, AssignableValue tempVar,
                              RuntimeValue first, RuntimeValue last, Executable command,
                              LineInfo line, boolean downto) throws Exception {
        this.downto = downto;
        this.tempVar = tempVar;
        this.mNumberType = tempVar.getRuntimeType(f).getRawType();
        this.first = first;
        this.last = last;
        this.line = line;
        this.command = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (downto) {
            if (mNumberType == BasicType.Integer) {
                Reference<Integer> reference = tempVar.getReference(f, main);
                Integer start = (Integer) (first.getValue(f, main));
                Integer end = (Integer) (last.getValue(f, main));
                forLoop:
                for (Integer index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Long) {
                Reference<Long> reference = tempVar.getReference(f, main);
                Long start = (Long) first.getValue(f, main);
                Long end = (Long) last.getValue(f, main);
                forLoop:
                for (Long index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Byte) {
                Reference<Byte> reference = tempVar.getReference(f, main);
                Byte start = (Byte) first.getValue(f, main);
                Byte end = (Byte) last.getValue(f, main);
                forLoop:
                for (Byte index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Character) {
                Reference<Character> reference = tempVar.getReference(f, main);
                Character start = (Character) first.getValue(f, main);
                Character end = (Character) last.getValue(f, main);
                forLoop:
                for (Character index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            }
        } else {
            if (mNumberType == BasicType.Integer) {
                Reference<Integer> reference = tempVar.getReference(f, main);
                Integer start = (Integer) (first.getValue(f, main));
                Integer end = (Integer) (last.getValue(f, main));
                forLoop:
                for (Integer index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Long) {
                Reference<Long> reference = tempVar.getReference(f, main);
                Long start = (Long) first.getValue(f, main);
                Long end = (Long) last.getValue(f, main);
                forLoop:
                for (Long index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Byte) {
                Reference<Byte> reference = tempVar.getReference(f, main);
                Byte start = (Byte) first.getValue(f, main);
                Byte end = (Byte) last.getValue(f, main);
                forLoop:
                for (Byte index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Character) {
                Reference<Character> reference = tempVar.getReference(f, main);
                Character start =(Character) (first.getValue(f, main));
                Character end =(Character)(last.getValue(f, main));
                forLoop:
                for (Character index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = command.execute(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else {
                throw new RuntimePascalException("Can not execute for statement");
            }
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        return null;
    }
}
