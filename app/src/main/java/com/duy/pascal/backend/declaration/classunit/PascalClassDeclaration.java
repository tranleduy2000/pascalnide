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

package com.duy.pascal.backend.declaration.classunit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ClassExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.frontend.activities.IRunnablePascal;

/**
 * Created by Duy on 16-Jun-17.
 */
public class PascalClassDeclaration extends CodeUnit implements Cloneable {

    private final CodeUnit root;
    private final IRunnablePascal handler;
    @NonNull
    private ExpressionContext parent;

    public PascalClassDeclaration(CodeUnit root, @NonNull ExpressionContext parent,
                                  IRunnablePascal handler) throws ParsingException {
        this.root = root;
        this.parent = parent;
        this.handler = handler;
        this.context = getExpressionContextInstance(handler);
    }

    @Override
    protected ExpressionContextMixin getExpressionContextInstance(@Nullable IRunnablePascal handler) {
        return new ClassExpressionContext(root, parent);
    }

    @Override
    public RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate() {
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
