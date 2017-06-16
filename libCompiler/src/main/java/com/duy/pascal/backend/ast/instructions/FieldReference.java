package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class FieldReference implements Reference {
    private ContainsVariables container;
    private String name;
    private static final String TAG = "FieldReference";

    public FieldReference(ContainsVariables container, String name) {
        this.container = container;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
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
        return this;
    }

}
