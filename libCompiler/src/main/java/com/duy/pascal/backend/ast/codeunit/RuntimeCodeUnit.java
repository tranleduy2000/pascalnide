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

package com.duy.pascal.backend.ast.codeunit;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent declaration;
    private HashMap<String, Object> unitVariables = new HashMap<>();

    public RuntimeCodeUnit(parent declaration) {
        this.declaration = declaration;
        for (VariableDeclaration v : declaration.context.variables) {
            v.initialize(unitVariables);
        }
    }

    public parent getDeclaration() {
        return declaration;
    }

    @Override
    public Object getLocalVar(String name) {
        return unitVariables.get(name);
    }

    @Override
    public boolean setLocalVar(String name, Object val) {
        return unitVariables.put(name, val) != null;
    }

    @Override
    public List<String> getUserDefineVariableNames() {
        ArrayList<VariableDeclaration> variables = declaration.context.variables;
        ArrayList<String> varNames = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            varNames.add(variable.getName());
        }
        return varNames;
    }

    @Override
    public List<String> getAllVariableNames() {
        return null;
    }

    @Override
    public HashMap<String, ? extends Object> getMapVars() {
        return unitVariables;
    }
}
