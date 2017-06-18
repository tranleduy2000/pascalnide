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

import com.duy.pascal.backend.ast.codeunit.RuntimePascalClass;
import com.duy.pascal.backend.ast.runtime_value.value.ClassConstructorCall;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.CompoundStatement;
import com.duy.pascal.backend.ast.variablecontext.FunctionOnStack;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.declaration.function.FunctionDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.declaration.types.DeclaredType;
import com.duy.pascal.backend.declaration.types.PascalClassType;
import com.duy.pascal.frontend.debug.CallStack;

import java.util.List;

/**
 * Created by Duy on 17-Jun-17.
 */

public class ClassConstructor extends FunctionDeclaration {
    private PascalClassType classType;

    public ClassConstructor(PascalClassType classType, String name, ExpressionContext parent,
                            GrouperToken grouperToken, boolean isProcedure)
            throws ParsingException {
        super(name, parent, grouperToken, isProcedure);
        this.classType = classType;
    }

    public ClassConstructor(PascalClassType classType, ExpressionContext parent)
            throws ParsingException {
        super(parent);
        this.classType = classType;
        this.name = "create";
        this.instructions = new CompoundStatement(new LineInfo(0, "system"));
    }

    public ClassConstructor(PascalClassType classType, ExpressionContext parent,
                            GrouperToken grouperToken, boolean isProcedure) throws ParsingException {
        super(parent, grouperToken, isProcedure);
        this.classType = classType;
    }

    public Object call(RuntimeExecutableCodeUnit<?> main, Object[] arguments, String idName) throws RuntimePascalException {
        RuntimePascalClass classVarContext = new RuntimePascalClass(classType.getDeclaration());
        main.addPascalClassContext(idName, classVarContext);
        FunctionOnStack functionOnStack = new FunctionOnStack(classVarContext, main, this, arguments);
        if (main.isDebug()) {
            main.getDebugListener().onVariableChange(new CallStack(functionOnStack));
        }
        return functionOnStack.execute();
    }

    @Override
    public Object call(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object[] arguments) throws RuntimePascalException {
        return new RuntimePascalClass(classType.getDeclaration());
    }

    @Override
    public ClassConstructorCall generateCall(LineInfo line, List<RuntimeValue> values, ExpressionContext f) throws ParsingException {
        RuntimeValue[] args = formatArgs(values, f);
        if (args == null) {
            return null;
        }
        return new ClassConstructorCall(this, args, line);
    }

    @Override
    public ClassConstructorCall generatePerfectFitCall(LineInfo line, List<RuntimeValue> values, ExpressionContext f) throws ParsingException {
        RuntimeValue[] args = perfectMatch(values, f);
        if (args == null) {
            return null;
        }
        return new ClassConstructorCall(this, args, line);
    }

    @Nullable
    @Override
    public DeclaredType returnType() {
        return classType;
    }
}
