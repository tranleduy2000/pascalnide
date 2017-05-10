package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldReference implements Reference {
    ContainsVariables container;
    String name;

    public FieldReference(ContainsVariables container, String name) {
        this.container = container;
        this.name = name;
    }

    @Override
    public void set(Object value) {
        container.setVar(name, value);
    }

    @Override
    public Object get() throws RuntimePascalException {
        return container.getVar(name);
    }

    @Override
    public Reference clone() {
        return null;
    }
}
