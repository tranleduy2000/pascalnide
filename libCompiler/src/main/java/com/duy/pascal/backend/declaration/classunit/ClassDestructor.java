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

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.RuntimePascalClass;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.declaration.lang.types.Type;
import com.duy.pascal.backend.declaration.lang.types.PascalClassType;

/**
 * Created by Duy on 17-Jun-17.
 */

public class ClassDestructor extends FunctionDeclaration {
    private PascalClassType classType;

    public ClassDestructor(PascalClassType classType, String name, ExpressionContext parent,
                           GrouperToken grouperToken, boolean isProcedure)
            throws ParsingException {
        super(name, parent, grouperToken, isProcedure);
        this.classType = classType;
    }

    public ClassDestructor(PascalClassType classType, ExpressionContext parent,
                           GrouperToken grouperToken, boolean isProcedure) throws ParsingException {
        super(parent, grouperToken, isProcedure);
        this.classType = classType;
    }

    @Override
    public Object call(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object[] arguments) throws RuntimePascalException {
        return new RuntimePascalClass(classType.getDeclaration());
    }

    @Nullable
    @Override
    public Type returnType() {
        return classType;
    }
}
