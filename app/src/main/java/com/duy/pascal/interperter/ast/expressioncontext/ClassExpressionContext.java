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

package com.duy.pascal.interperter.ast.expressioncontext;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.MisplacedDeclarationException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public class ClassExpressionContext extends ExpressionContextMixin {

    private FunctionDeclaration destructor;

    public ClassExpressionContext(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    @NonNull
    @Override
    public LineInfo getStartPosition() {
        return new LineInfo(0, mContextName == null ? "class?" : mContextName);
    }

    @Override
    protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
            throws Exception {
        return false;
    }

    @Override
    protected Node handleUnrecognizedStatementImpl(Token next, GrouperToken container)
            throws Exception {
        throw new UnrecognizedTokenException(next);
    }


    @Override
    protected void handleBeginEnd(GrouperToken i) throws Exception {
        throw new MisplacedDeclarationException(i.peek().getLineNumber(),
                "main function", this);
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

    @Override
    public RuntimeValue getIdentifierValue(WordToken name) throws Exception {
        return super.getIdentifierValue(name);
    }

    @Override
    public ExpressionContextMixin clone() throws CloneNotSupportedException {
        return super.clone();
    }
}