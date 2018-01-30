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

package com.duy.pascal.interperter.ast.runtime.value.access;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.FieldReference;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableAssignableNode;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class VariableAccess extends DebuggableAssignableNode {
    private Name name;
    private LineNumber line;
    @NonNull
    private ExpressionContext declaration;

    public VariableAccess(Name name, LineNumber line, @NonNull ExpressionContext f) {
        this.name = name;
        this.line = line;
        this.declaration = f;
    }

    @NonNull
    public ExpressionContext getContext() {
        return declaration;
    }

    public Name getName() {
        return name;
    }

    @Override
    public boolean canDebug() {
        return false;
    }


    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return line;
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @NonNull
    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return f.getVar(name);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return new FieldReference(f, name);
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return new RuntimeType(declaration.getVariableDefinition(name).type, true);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext declaration)
            throws Exception {
        return null;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext declaration)
            throws Exception {
        return this;
    }
}
