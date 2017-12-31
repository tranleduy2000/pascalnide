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

package com.duy.pascal.interperter.ast.node.forstatement;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.debug.CallStack;

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForNumberNode extends DebuggableNode {
    @NonNull
    private Node mCommand;
    @NonNull
    private AssignableValue mTempVar;
    @NonNull
    private RuntimeValue mFirst;
    @NonNull
    private RuntimeValue mLast;
    @NonNull
    private LineInfo mLine;
    private boolean downto = false;
    @NonNull
    private Type mNumberType = null;

    public ForNumberNode(ExpressionContext f, @NonNull AssignableValue tempVar,
                         @NonNull RuntimeValue first, @NonNull RuntimeValue last, @NonNull Node command,
                         @NonNull LineInfo line, boolean downto) throws Exception {
        this.downto = downto;
        this.mTempVar = tempVar;
        this.mNumberType = tempVar.getRuntimeType(f).getRawType();
        this.mFirst = first;
        this.mLast = last;
        this.mLine = line;
        this.mCommand = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (downto) {
            if (mNumberType == BasicType.Integer) {
                Reference<Integer> reference = mTempVar.getReference(f, main);
                Integer start = (Integer) (mFirst.getValue(f, main));
                Integer end = (Integer) (mLast.getValue(f, main));
                forLoop:
                for (Integer index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Long) {
                Reference<Long> reference = mTempVar.getReference(f, main);
                Long start = (Long) mFirst.getValue(f, main);
                Long end = (Long) mLast.getValue(f, main);
                forLoop:
                for (Long index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Byte) {
                Reference<Byte> reference = mTempVar.getReference(f, main);
                Byte start = (Byte) mFirst.getValue(f, main);
                Byte end = (Byte) mLast.getValue(f, main);
                forLoop:
                for (Byte index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Character) {
                Reference<Character> reference = mTempVar.getReference(f, main);
                Character start = (Character) mFirst.getValue(f, main);
                Character end = (Character) mLast.getValue(f, main);
                forLoop:
                for (Character index = start; index >= end; index--) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                            continue;
                    }
                }
            }
        } else {
            if (mNumberType == BasicType.Integer) {
                Reference<Integer> reference = mTempVar.getReference(f, main);
                Integer start = (Integer) (mFirst.getValue(f, main));
                Integer end = (Integer) (mLast.getValue(f, main));
                forLoop:
                for (Integer index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Long) {
                Reference<Long> reference = mTempVar.getReference(f, main);
                Long start = (Long) mFirst.getValue(f, main);
                Long end = (Long) mLast.getValue(f, main);
                forLoop:
                for (Long index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Byte) {
                Reference<Byte> reference = mTempVar.getReference(f, main);
                Byte start = (Byte) mFirst.getValue(f, main);
                Byte end = (Byte) mLast.getValue(f, main);
                forLoop:
                for (Byte index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
                    switch (result) {
                        case EXIT:
                            return ExecutionResult.EXIT;
                        case BREAK:
                            break forLoop;
                        case CONTINUE:
                    }
                }
            } else if (mNumberType == BasicType.Character) {
                Reference<Character> reference = mTempVar.getReference(f, main);
                Character start = (Character) (mFirst.getValue(f, main));
                Character end = (Character) (mLast.getValue(f, main));
                forLoop:
                for (Character index = start; index <= end; index++) {
                    reference.set(index);
                    if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));
                    ExecutionResult result = mCommand.visit(f, main);
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
        return mLine;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c) throws Exception {
        return null;
    }
}
