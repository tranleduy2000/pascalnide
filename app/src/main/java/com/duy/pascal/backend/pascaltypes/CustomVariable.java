/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Duy on 17-Apr-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CustomVariable implements ContainsVariables {
    private Map<String, Object> variableMap = new HashMap<>();
    private List<VariableDeclaration> variableList;

    public CustomVariable(List<VariableDeclaration> variableList) {
        this.variableList = variableList;
        for (VariableDeclaration declaration : variableList) {
            Class returnType = declaration.getType().getTransferClass();
            if (declaration.initialValue != null) {
                variableMap.put(declaration.name.toLowerCase(), declaration.initialValue);
            } else {
                if (returnType == int.class || returnType == Integer.class) {
                    variableMap.put(declaration.name.toLowerCase(), 0);
                } else if (returnType == double.class || returnType == Double.class) {
                    variableMap.put(declaration.name.toLowerCase(), 0.0d);
                } else if (returnType == char.class || returnType == Character.class) {
                    variableMap.put(declaration.name.toLowerCase(), ' ');
                } else if (returnType == boolean.class || returnType == Boolean.class) {
                    variableMap.put(declaration.name.toLowerCase(), Boolean.FALSE);
                } else if (returnType == long.class || returnType == Long.class) {
                    variableMap.put(declaration.name.toLowerCase(), 0L);
                } else if (returnType == StringBuilder.class) {
                    variableMap.put(declaration.name.toLowerCase(), new StringBuilder(""));
                } else if (returnType == String.class) {
                    variableMap.put(declaration.name.toLowerCase(), "");
                } else if (returnType == Array.class) {
                    variableMap.put(declaration.name, new Object[0]);
                } else if (declaration.type instanceof ArrayType) {
                    variableMap.put(declaration.name, new Object[0]);
                }
            }
        }

    }

    @Override
    public Object get_var(String name) throws RuntimePascalException {
        return variableMap.get(name.toLowerCase());
    }

    @Override
    public void set_var(String name, Object val) {
        variableMap.put(name.toLowerCase(), val);
    }

    @Override
    public ContainsVariables clone() {
        List<VariableDeclaration> listVariable = new ArrayList<>();
        for (VariableDeclaration variable : variableList) {
            VariableDeclaration variableDeclaration = new VariableDeclaration(variable.name(),
                    variable.getType(), variableMap.get(variable.name), variable.getLineNumber());
            listVariable.add(variableDeclaration);
        }
        return new CustomVariable(listVariable);
    }
}
