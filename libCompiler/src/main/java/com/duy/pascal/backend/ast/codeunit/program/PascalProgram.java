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

package com.duy.pascal.backend.ast.codeunit.program;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.MultipleDefinitionsMainException;
import com.duy.pascal.backend.parse_exception.missing.MissingDotTokenException;
import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.ast.runtime_value.FunctionOnStack;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.google.common.collect.ListMultimap;
import com.duy.pascal.backend.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.source_include.ScriptSource;

import java.io.Reader;
import java.util.List;

public class PascalProgram extends ExecutableCodeUnit {
    public Executable main;

    private FunctionOnStack mainRunning;
    private IRunnablePascal handler;

    public PascalProgram(Reader program,
                         ListMultimap<String, AbstractFunction> functionTable,
                         String sourceName, List<ScriptSource> includeDirectories,
                         IRunnablePascal handler)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, IRunnablePascal handler) {
        return new PascalProgramExpressionContext(functionTable, handler);
    }

    @Override
    public RuntimeExecutableCodeUnit<PascalProgram> run() {
        return new RuntimePascalProgram(this, handler);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        public PascalProgramExpressionContext(
                ListMultimap<String, AbstractFunction> f, IRunnablePascal handler) {
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
                throw new MultipleDefinitionsMainException(i.peek().getLineNumber());
            }
            main = i.getNextCommand(this);
            if (!(i.peek() instanceof PeriodToken)) {
                throw new MissingDotTokenException(i.peek());
            }
            i.take();
        }
    }

}