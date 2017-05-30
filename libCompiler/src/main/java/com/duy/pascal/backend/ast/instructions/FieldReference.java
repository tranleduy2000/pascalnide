package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;

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
