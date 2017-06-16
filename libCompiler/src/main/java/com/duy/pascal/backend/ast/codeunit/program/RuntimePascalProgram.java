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

package com.duy.pascal.backend.ast.codeunit.program;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.codeunit.library.PascalUnitDeclaration;
import com.duy.pascal.backend.ast.codeunit.library.RuntimeUnitPascal;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.config.RunMode;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.debug.CallStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuntimePascalProgram extends RuntimeExecutableCodeUnit<PascalProgramDeclaration> {

    public RuntimePascalProgram(PascalProgramDeclaration p) {
        super(p);
    }

    @Override
    public void runImpl() throws RuntimePascalException {
        this.mode = RunMode.RUNNING;

        //generate init code of library
        HashMap<PascalUnitDeclaration, RuntimeUnitPascal> librariesMap = getDeclaration().getContext().getRuntimeUnitMap();
        Set<Map.Entry<PascalUnitDeclaration, RuntimeUnitPascal>> entries = librariesMap.entrySet();
        for (Map.Entry<PascalUnitDeclaration, RuntimeUnitPascal> entry : entries) {
            entry.getValue().runInit();
        }

        if (isDebug()) getDebugListener().onVariableChange(new CallStack(this));
        getDeclaration().main.execute(this, this);

        //generate final code library
        for (Map.Entry<PascalUnitDeclaration, RuntimeUnitPascal> entry : entries) {
            entry.getValue().runFinal();
        }

        if (isDebug()) {
            getDebugListener().onVariableChange(new CallStack(this));
            getDebugListener().onEndProgram();
        }
    }

    @Override
    public VariableContext getParentContext() {
        return null;
    }

    @Override
    public String toString() {
        return getDeclaration().getContext().getStartLine().getSourceFile();
    }
}
