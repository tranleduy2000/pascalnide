package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent definition;
    private Map<String, Object> unitVariables = new HashMap<>();
    private ArrayList<String> listNameGlobalVariables = new ArrayList<>();

    public RuntimeCodeUnit(parent definition) {
        this.definition = definition;
        for (VariableDeclaration v : definition.context.variables) {
            v.initialize(unitVariables);
            listNameGlobalVariables.add(v.get_name());
        }
    }

    public ArrayList<String> getListNameGlobalVariables() {
        return listNameGlobalVariables;
    }

    public Map<String, Object> getMapUnitVariables() {
        return unitVariables;
    }

    @Override
    public Object getGlobalVariable(String name) {
        return unitVariables.get(name);
    }

    @Override
    protected boolean setLocalVar(String name, Object val) {
        return unitVariables.put(name, val) != null;
    }

}
