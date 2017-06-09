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

package com.duy.pascal.backend.ast.runtime_value.variables;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duy on 17-Apr-17.
 */
public class CustomVariable implements ContainsVariables {
    private Map<String, Object> variableMap = new HashMap<>();
    private ArrayList<VariableDeclaration> variables;

    public CustomVariable(ArrayList<VariableDeclaration> mVariables) {
        this.variables = mVariables;
        for (VariableDeclaration declaration : mVariables) {
            DeclaredType returnType = declaration.getType();
            if (declaration.getInitialValue() != null) {
                variableMap.put(declaration.name.toLowerCase(), declaration.getInitialValue());
            } else {
                variableMap.put(declaration.name, returnType.initialize());
            }
        }

    }

    public ArrayList<VariableDeclaration> getVariables() {
        return variables;
    }

    @Override
    public Object getVar(String name) throws RuntimePascalException {
        return variableMap.get(name.toLowerCase());
    }

    @Override
    public String toString() {
        Set<Map.Entry<String, Object>> entries = variableMap.entrySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : entries) {
            stringBuilder/*.append(entry.getKey()).append(" = ")*/.append(entry.getValue());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void setVar(String name, Object val) {
        variableMap.put(name.toLowerCase(), val);
    }

    @Override
    @SuppressWarnings({"unchecked", "CloneDoesntCallSuperClone"})
    public ContainsVariables clone() {
        ArrayList<VariableDeclaration> vars = new ArrayList<>();
        for (VariableDeclaration mVariable : variables) {
            vars.add(new VariableDeclaration(mVariable.getName(), mVariable.getType(),
                    variableMap.get(mVariable.getName().toLowerCase()), mVariable.getLineNumber()));
        }
        return new CustomVariable(vars);
    }
}
