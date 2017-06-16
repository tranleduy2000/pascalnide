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

import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.parse_exception.syntax.MisplacedDeclarationException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

import java.util.ArrayList;
import java.util.List;

public class ClassExpressionContext extends ExpressionContextMixin {

    private FunctionDeclaration constructor, destructor;
    private List<FunctionDeclaration> privateFunctions = new ArrayList<>();
    private List<FunctionDeclaration> publicFunctions = new ArrayList<>();
    private List<VariableDeclaration> publicVars = new ArrayList<>();
    private List<VariableDeclaration> privateVars = new ArrayList<>();

    public ClassExpressionContext(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    @NonNull
    @Override
    public LineInfo getStartLine() {
        return new LineInfo(0, mContextName == null ? "class?" : mContextName);
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
        throw new MisplacedDeclarationException(i.peek().getLineNumber(),
                "main function", this);
    }


    public FunctionDeclaration getConstructor() {
        return constructor;
    }

    public void setConstructor(FunctionDeclaration constructor) {
        this.constructor = constructor;
    }

    @Override
    public String toString() {
        return "class context; parent = " + parent;
    }

    public FunctionDeclaration getDestructor() {
        return destructor;
    }

    public void setDestructor(FunctionDeclaration destructor) {
        this.destructor = destructor;
    }
}