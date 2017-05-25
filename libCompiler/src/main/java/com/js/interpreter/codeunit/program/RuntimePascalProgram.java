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

package com.js.interpreter.codeunit.program;

import com.duy.pascal.backend.debugable.DebugListener;
import com.js.interpreter.codeunit.RunMode;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.codeunit.library.RuntimeUnitPascal;
import com.js.interpreter.codeunit.library.UnitPascal;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuntimePascalProgram extends RuntimeExecutableCodeUnit<PascalProgram> {

    VariableContext main;

    public RuntimePascalProgram(PascalProgram p) {
        super(p);
    }

    public RuntimePascalProgram(PascalProgram p, DebugListener debugListener) {
        super(p, debugListener);
    }

    @Override
    public void runImpl() throws RuntimePascalException {
        this.mode = RunMode.RUNNING;

        //run init code of library
        HashMap<UnitPascal, RuntimeUnitPascal> librariesMap = getDefinition().getContext().getUnitsMap();
        Set<Map.Entry<UnitPascal, RuntimeUnitPascal>> entries = librariesMap.entrySet();
        for (Map.Entry<UnitPascal, RuntimeUnitPascal> entry : entries) {
            entry.getValue().runInit();
        }

        getDefinition().main.execute(this, this);

        //run final code library
        for (Map.Entry<UnitPascal, RuntimeUnitPascal> entry : entries) {
            entry.getValue().runFinal();
        }
    }

    @Override
    public VariableContext getParentContext() {
        return null;
    }
}
