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

import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.config.RunMode;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.utils.NullSafety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent declaration;
    private HashMap<Name, Object> unitVariables = new HashMap<>();
    private HashMap<Name, RuntimePascalClass> mRuntimePascalClassMap = new HashMap<>();
    private HashMap<PascalUnitDeclaration, RuntimeUnitPascal> mRuntimeUnitMap = new HashMap<>();

    public RuntimeCodeUnit(parent declaration) throws RuntimePascalException {
        this.declaration = declaration;
        for (VariableDeclaration v : declaration.mContext.variables) v.initialize(unitVariables);
    }

    public HashMap<PascalUnitDeclaration, RuntimeUnitPascal> getRuntimeUnitMap() {
        return mRuntimeUnitMap;
    }

    public Map<Name, RuntimePascalClass> getRuntimePascalClassMap() {
        return mRuntimePascalClassMap;
    }

    public parent getDeclaration() {
        return declaration;
    }

    @NonNull
    @Override
    public Object getLocalVar(Name name) {
        Object o = unitVariables.get(name);
        return NullSafety.zReturn(o);
    }

    @Override
    public boolean setLocalVar(Name name, Object val) {
        return unitVariables.put(name, val) != null;
    }

    @Override
    public ArrayList<Name> getUserDefineVariableNames() {
        ArrayList<VariableDeclaration> variables = declaration.mContext.variables;
        ArrayList<Name> varNames = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            varNames.add(variable.getName());
        }
        return varNames;
    }

    @Override
    public HashMap<Name, Object> getMapVars() {
        return unitVariables;
    }
}
