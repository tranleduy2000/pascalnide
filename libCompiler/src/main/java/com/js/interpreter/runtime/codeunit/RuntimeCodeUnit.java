package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.CodeUnit;
import com.js.interpreter.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends VariableContext {
    public volatile RunMode mode;
    parent definition;
    private Map<String, Object> unitVariables = new HashMap<>();

    public RuntimeCodeUnit(parent definition) {
        this.definition = definition;
        for (VariableDeclaration v : definition.context.variables) {
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
