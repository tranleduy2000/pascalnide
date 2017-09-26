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

package com.duy.pascal.interperter.declaration.program;

import android.support.annotation.NonNull;

import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.codeunit.RuntimePascalProgram;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.variablecontext.FunctionOnStack;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.exceptions.parsing.define.MultipleDefinitionsMainException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingDotTokenException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.PeriodToken;
import com.duy.pascal.interperter.tokens.basic.ProgramToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import java.io.Reader;
import java.util.List;

public class PascalProgramDeclaration extends ExecutableCodeUnit {
    public Executable main;

    private FunctionOnStack mainRunning;
    private ProgramHandler handler;

    public PascalProgramDeclaration(Reader program,
                                    String sourceName, List<ScriptSource> includeDirectories,
                                    ProgramHandler handler, DiagnosticCollector collector)
            throws Exception {
        super(program, sourceName, includeDirectories, handler, collector);
        this.handler = handler;
    }

    @Override
    protected PascalProgramExpressionContext createExpressionContext(ProgramHandler handler) {
        return new PascalProgramExpressionContext(handler);
    }

    @Override
    public RuntimeExecutableCodeUnit<PascalProgramDeclaration> generate() {
        return new RuntimePascalProgram(this);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        public PascalProgramExpressionContext(@NonNull ProgramHandler handler) {
            super(handler);
            config.setLibrary(false);
        }

        @NonNull
        @Override
        public LineInfo getStartPosition() {
            return new LineInfo(0, programName == null ? getSourceName() : programName.getOriginName());
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
                throws Exception {
            if (next instanceof ProgramToken) {
                programName = grouperToken.nextWordValue();
                grouperToken.assertNextSemicolon();
                return true;
            }
            return false;
        }


        @Override
        public void handleBeginEnd(GrouperToken i) throws Exception {
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
