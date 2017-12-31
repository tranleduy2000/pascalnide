package com.duy.pascal.interperter.ast.node;

import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.variablecontext.ContainsVariables;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public class FieldReference implements Reference {
    private ContainsVariables container;
    private Name name;
    private static final String TAG = "FieldReference";

    public FieldReference(ContainsVariables container, Name name) {
        this.container = container;
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
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
