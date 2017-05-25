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

package com.js.interpreter.codeunit;

import com.js.interpreter.VariableDeclaration;
import com.duy.pascal.backend.runtime.VariableContext;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent definition;
    private Map<String, Object> unitVariables = new HashMap<>();

    public RuntimeCodeUnit(parent definition) {
        this.definition = definition;
        for (VariableDeclaration v : definition.mContext.variables) {
            v.initialize(unitVariables);
        }
    }

    public parent getDefinition() {
        return definition;
    }

    @Override
    public Object getLocalVar(String name) {
        return unitVariables.get(name);
    }

    @Override
    public boolean setLocalVar(String name, Object val) {
        return unitVariables.put(name, val) != null;
    }


}
