package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;

public class FieldReference implements Reference {
    private ContainsVariables container;
    private String name;

    public FieldReference(ContainsVariables container, String name) {
        this.container = container;
        this.name = name;
    }

    public FieldReference(ContainsVariables container, String name, RuntimeType type) {
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

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Reference clone() {
        return this;
    }

}
