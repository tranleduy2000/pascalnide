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

package com.duy.pascal.interperter.ast.instructions;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

/**
 * Created by Duy on 14-Jun-17.
 */

public class LabelInstruction extends DebuggableNode {
    @Nullable
    private Node command;
    private boolean bodyDeclared = false;

    public LabelInstruction(@Nullable Node command) {
        this.command = command;
    }

    public boolean isBodyDeclared() {
        return bodyDeclared;
    }

    public void setCommand(@Nullable Node command) {
        this.command = command;
    }

    public void parseBody(ExpressionContext context, GrouperToken i) throws Exception {
        Token next = i.peekNoEOF();
        if (command != null) {
            throw new RuntimeException();
        }
        while (!bodyDeclared) {
            command = i.getNextCommand(context);
        }
    }

    @Override
    public LineInfo getLineNumber() {
        return null;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c) throws Exception {
        return null;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main
            )
            throws RuntimePascalException {
        return command.execute(context, main);
    }
}
