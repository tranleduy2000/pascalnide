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

package com.duy.pascal.interperter.ast.runtime_value.value;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.variablecontext.ContainsVariables;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duy on 17-Apr-17.
 */
public class RecordValue implements ContainsVariables {
    protected HashMap<Name, Object> variableMap = new HashMap<>();
    protected ArrayList<VariableDeclaration> variables = new ArrayList<>();

    public RecordValue(ArrayList<VariableDeclaration> variables) {
        this.variables = variables;
        for (VariableDeclaration declaration : variables) {
            Type returnType = declaration.getType();
            if (declaration.getInitialValue() != null) {
                variableMap.put(declaration.name, declaration.getInitialValue());
            } else {
                variableMap.put(declaration.name, returnType.initialize());
            }
        }
    }

    public RecordValue() {

    }

    public ArrayList<VariableDeclaration> getVariables() {
        return variables;
    }

    public HashMap<Name, Object> getVariableMap() {
        return variableMap;
    }

    @NonNull
    @Override
    public Object getVar(Name name) {
        return variableMap.get(name);
    }

    @Override
    public String toString() {
        Set<Map.Entry<Name, Object>> entries = variableMap.entrySet();
        StringBuilder res = new StringBuilder();
        res.append("(");
        for (Map.Entry<Name, Object> entry : entries) {
            res.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        res.append(")");
        return res.toString();
    }

    @Override
    public void setVar(Name name, Object val) {
        variableMap.put(name, val);
    }

    @NonNull
    @Override
    public ContainsVariables clone() {
        ArrayList<VariableDeclaration> vars = new ArrayList<>();
        for (VariableDeclaration mVariable : variables) {
            vars.add(new VariableDeclaration(mVariable.getName(), mVariable.getType(),
                    variableMap.get(mVariable.getName()), mVariable.getLineNumber()));
        }
        return new RecordValue(vars);
    }
}
