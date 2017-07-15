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

import com.duy.pascal.interperter.declaration.LabelDeclaration;
import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

/**
 * Created by Duy on 14-Jun-17.
 */

public class GotoStatement extends DebuggableExecutable {
    @Nullable
    private Executable command;
    private boolean bodyDeclared = false;

    public GotoStatement(@Nullable LabelDeclaration command) {
    }

    public boolean isBodyDeclared() {
        return bodyDeclared;
    }

    public void setCommand(@Nullable Executable command) {
        this.command = command;
    }

    public void parseBody(ExpressionContext context, GrouperToken i) throws ParsingException {
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
    public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException {
        return null;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return command.execute(context, main);
    }
}
