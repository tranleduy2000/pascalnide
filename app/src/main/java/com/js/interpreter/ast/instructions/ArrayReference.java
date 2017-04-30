package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayReference implements Reference {
    Object array;
    int index;
    int offset;

    public ArrayReference(Object array, int index, int offset) {
        this.array = array;
        this.index = index;
        this.offset = offset;
    }

    public void set(Object value) {
            Array.set(array, index - offset, value);
    }

    public Object get() throws RuntimePascalException {
       return Array.get(array, index - offset);
    }

    @Override
    public ArrayReference clone() {
        return null;
    }
}
