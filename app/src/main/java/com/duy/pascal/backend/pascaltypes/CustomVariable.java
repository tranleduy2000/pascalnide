package com.duy.pascal.backend.pascaltypes;

import android.util.Log;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Duy on 17-Apr-17.
 */
public class CustomVariable implements ContainsVariables {
    private static final String TAG = "CustomVariable";
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
                }
            }
        }
    }

    @Override
    public Object getVariable(String name) throws RuntimePascalException {
        return variableMap.get(name.toLowerCase());
    }

    @Override
    public void setVariable(String name, Object val) {
        variableMap.put(name.toLowerCase(), val);
    }

    @Override
    public ContainsVariables clone() {
        List<VariableDeclaration> listVariable = new ArrayList<>();
        for (VariableDeclaration variable : variableList) {
            VariableDeclaration variableDeclaration = new VariableDeclaration(variable.name(),
                    variable.getType(), variableMap.get(variable.name), variable.getLine());
            listVariable.add(variableDeclaration);
        }
        return new CustomVariable(listVariable);
    }
}
