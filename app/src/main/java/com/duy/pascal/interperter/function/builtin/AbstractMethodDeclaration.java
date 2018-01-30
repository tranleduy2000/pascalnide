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

package com.duy.pascal.interperter.function.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.linenumber.LineNumber;

import java.util.List;

public class AbstractMethodDeclaration extends AbstractFunction {

    private IMethodDeclaration t;

    public AbstractMethodDeclaration(IMethodDeclaration t) {
        this.t = t;
    }

    @Override
    public LineNumber getLineNumber() {
        return new LineNumber(-1, t.getClass().getCanonicalName());
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "Template Plugin";
    }

    @NonNull
    @Override
    public Name getName() {
        return t.getName();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return t.argumentTypes();
    }

    @Override
    public Type returnType() {
        return t.returnType();
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineNumber line,
                                               List<RuntimeValue> values, ExpressionContext f)
            throws Exception {
        RuntimeValue[] args = this.perfectMatch(values, f);
        if (args == null) {
            return null;
        }
        return t.generatePerfectFitCall(line, args, f);
    }

    @Override
    public FunctionCall generateCall(LineNumber line, List<RuntimeValue> values,
                                     ExpressionContext f) throws Exception {
        RuntimeValue[] args = this.formatArgs(values, f);
        if (args == null) {
            return null;
        }
        return t.generateCall(line, args, f);
    }

}
