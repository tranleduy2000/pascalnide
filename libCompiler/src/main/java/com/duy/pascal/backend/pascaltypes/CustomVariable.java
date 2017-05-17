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
import java.util.Map;

/**
 * Created by Duy on 17-Apr-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CustomVariable implements ContainsVariables {
    private Map<String, Object> variableMap = new HashMap<>();
    private ArrayList<VariableDeclaration> variableList;

    public CustomVariable(ArrayList<VariableDeclaration> variableList) {
        this.variableList = variableList;
        for (VariableDeclaration declaration : variableList) {
            Class returnType = declaration.getType().getTransferClass();
            if (declaration.getInitialValue() != null) {
                variableMap.put(declaration.name, declaration.getInitialValue());
            } else {
                if (returnType == int.class || returnType == Integer.class) {
                    variableMap.put(declaration.name, 0);
                } else if (returnType == double.class || returnType == Double.class) {
                    variableMap.put(declaration.name, 0.0d);
                } else if (returnType == char.class || returnType == Character.class) {
                    variableMap.put(declaration.name, ' ');
                } else if (returnType == boolean.class || returnType == Boolean.class) {
                    variableMap.put(declaration.name, Boolean.FALSE);
                } else if (returnType == long.class || returnType == Long.class) {
                    variableMap.put(declaration.name, 0L);
                } else if (returnType == StringBuilder.class) {
                    variableMap.put(declaration.name, new StringBuilder(""));
                } else if (returnType == String.class) {
                    variableMap.put(declaration.name, "");
                } else if (returnType == Array.class) {
                    variableMap.put(declaration.name, new Object[0]);
                } else if (declaration.type instanceof ArrayType) {
                    variableMap.put(declaration.name, new Object[0]);
                } else {
                    variableMap.put(declaration.name, null);
                }
            }
        }

    }

    @Override
    public Object getVar(String name) throws RuntimePascalException {
        return variableMap.get(name);
    }

    @Override
    public void setVar(String name, Object val) {
        variableMap.put(name, val);
    }

    @Override
    @SuppressWarnings({"unchecked", "CloneDoesntCallSuperClone"})
    public ContainsVariables clone() {
        ArrayList<VariableDeclaration> listVariable = (ArrayList<VariableDeclaration>) variableList.clone();
        return new CustomVariable(listVariable);
    }
}
