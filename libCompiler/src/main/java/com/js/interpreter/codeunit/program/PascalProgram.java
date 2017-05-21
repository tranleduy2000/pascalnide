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

package com.js.interpreter.codeunit.program;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.codeunit.ExecutableCodeUnit;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.source_include.ScriptSource;

import java.io.Reader;
import java.util.List;

public class PascalProgram extends ExecutableCodeUnit {
    public Executable main;

    private FunctionOnStack mainRunning;
    private RunnableActivity handler;

    public PascalProgram(Reader program,
                         ListMultimap<String, AbstractFunction> functionTable,
                         String sourceName, List<ScriptSource> includeDirectories,
                         RunnableActivity handler)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler) {
        return new PascalProgramExpressionContext(functionTable, handler);
    }

    @Override
    public RuntimeExecutableCodeUnit<PascalProgram> run() {
        return new RuntimePascalProgram(this, handler);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        public PascalProgramExpressionContext(
                ListMultimap<String, AbstractFunction> f, RunnableActivity handler) {
            super(f, handler, false);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
                throws ParsingException {
            if (next instanceof ProgramToken) {
                programName = grouperToken.nextWordValue();
                grouperToken.assertNextSemicolon(grouperToken);
                return true;
            }
            return false;
        }


        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            if (main != null) {
                throw new MultipleDefinitionsMainException(i.peek().lineInfo);
            }
            main = i.getNextCommand(this);
            if (!(i.peek() instanceof PeriodToken)) {
                throw new ExpectedTokenException(".", i.peek());
            }
            i.take();
        }
    }

}
