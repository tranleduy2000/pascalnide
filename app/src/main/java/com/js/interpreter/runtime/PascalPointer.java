package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface PascalPointer<T> {
    public abstract void set(T value);

    public abstract T get() throws RuntimePascalException;
}
