package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent definition;

    public Map<String, Object> getUnitVariables() {
        return UnitVariables;
    }

    private Map<String, Object> UnitVariables = new HashMap<String, Object>();

    public RuntimeCodeUnit(parent definition) {
        this.definition = definition;
        for (VariableDeclaration v : definition.context.variables) {
            v.initialize(UnitVariables);
        }
    }

    public parent getDefinition() {
        return definition;
    }

    @Override
    public Object getLocalVar(String name) {
        return UnitVariables.get(name);
    }

    @Override
    protected boolean setLocalVar(String name, Object val) {
        return UnitVariables.put(name, val) != null;
    }

}
