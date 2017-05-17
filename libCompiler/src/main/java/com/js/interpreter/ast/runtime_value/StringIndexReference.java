package com.js.interpreter.ast.runtime_value;

import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringIndexReference implements Reference {
    private StringBuilder container;
    private int index;

    public StringIndexReference(StringBuilder container, int index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public void set(Object value) {
        container.setCharAt(index - 1, (char) value);
    }

    @Override
    public Object get() throws RuntimePascalException {
        return container.charAt(index - 1);
    }

    @Override
    public Reference clone() {
        return null;
    }
}
