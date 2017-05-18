package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldReference implements Reference {
    private ContainsVariables container;
    private String name;
    private RuntimeType type = null;

    public FieldReference(ContainsVariables container, String name) {
        this.container = container;
        this.name = name;
    }

    public FieldReference(ContainsVariables container, String name, RuntimeType type) {
        this.container = container;
        this.name = name;
        this.type = type;
    }

    @Override
    public void set(Object value) {
        container.setVar(name, value);
    }

    @Override
    public Object get() throws RuntimePascalException {
        return container.getVar(name);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Reference clone() {
        return this;
    }

    public RuntimeType getType() {
        return type;
    }
}
