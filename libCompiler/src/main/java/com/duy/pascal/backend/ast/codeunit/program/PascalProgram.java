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

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.FunctionOnStack;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.MultipleDefinitionsMainException;
import com.duy.pascal.backend.parse_exception.missing.MissingDotTokenException;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.IRunnablePascal;

import java.io.Reader;
import java.util.List;

public class PascalProgram extends ExecutableCodeUnit {
    public Executable main;

    private FunctionOnStack mainRunning;
    private IRunnablePascal handler;

    public PascalProgram(Reader program,
                         String sourceName, List<ScriptSource> includeDirectories,
                         IRunnablePascal handler)
            throws ParsingException {
        super(program, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(IRunnablePascal handler) {
        return new PascalProgramExpressionContext(handler);
    }

    @Override
    public RuntimeExecutableCodeUnit<PascalProgram> generate() {
        return new RuntimePascalProgram(this);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        public PascalProgramExpressionContext(@NonNull IRunnablePascal handler) {
            super(handler);
            config.setLibrary(false);
        }

        @NonNull
        @Override
        public LineInfo getStartLine() {
            return new LineInfo(0, programName == null ? getSourceName() : programName);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
                throws ParsingException {
            if (next instanceof ProgramToken) {
                programName = grouperToken.nextWordValue();
                grouperToken.assertNextSemicolon();
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
                throw new MissingDotTokenException(i.peek().getLineNumber());
            }
            i.take();
        }
    }

}
