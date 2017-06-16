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

package com.duy.pascal.backend.ast.codeunit.classunit;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.IRunnablePascal;

/**
 * Created by Duy on 16-Jun-17.
 */
public class ClassObject extends ExecutableCodeUnit {

    @NonNull
    private ExpressionContext parent;


    public ClassObject(@NonNull ExpressionContext parent, IRunnablePascal handler) throws ParsingException {
        super(handler);
        this.parent = parent;
    }

    @Override
    protected CodeUnitExpressionContext getExpressionContextInstance(IRunnablePascal handler) {
        return null;
    }

    @Override
    public RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate() {
        return null;
    }

    protected class ClassExpressionContext extends ExpressionContextMixin {
        public ClassExpressionContext(@NonNull ExpressionContext parent,
                                      @NonNull IRunnablePascal handler) {
            super(ClassObject.this, parent, handler);
        }

        @NonNull
        @Override
        public LineInfo getStartLine() {
            return new LineInfo(0, programName == null ? getSourceName() : programName);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
                throws ParsingException {
            return false;
        }


        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }


        @Override
        protected void handleBeginEnd(GrouperToken i) throws ParsingException {
            i.take();
        }

    }
}
