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

package com.duy.pascal.interperter.ast.codeunit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;


public final class RuntimeUnitPascal extends RuntimeExecutableCodeUnit<PascalUnitDeclaration> {
    public RuntimeUnitPascal(@NonNull PascalUnitDeclaration unitPascal) throws RuntimePascalException {
        super(unitPascal);
    }

    public void runInit() throws RuntimePascalException {
        ExpressionContextMixin var10000 = this.declaration.mContext;
        PascalUnitDeclaration.UnitExpressionContext context = (PascalUnitDeclaration.UnitExpressionContext) var10000;
        Node var2 = context.getInitInstruction();
        if (var2 != null) {
            var2.visit(this, this);
        }
    }

    public void runFinal() throws RuntimePascalException {
        PascalUnitDeclaration.UnitExpressionContext context = (PascalUnitDeclaration.UnitExpressionContext) this.declaration.mContext;
        Node var2 = context.getFinalInstruction();
        if (var2 != null) {
            var2.visit(this, this);
        }
    }

    public void runImpl() throws RuntimePascalException {
    }

    @Nullable
    public VariableContext getParentContext() {
        return null;
    }

    @NonNull
    public String toString() {
        Name programName = this.declaration.getProgramName();
        return programName == null ? "" : programName.toString();
    }
}
