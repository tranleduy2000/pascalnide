package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface Reference<T> {
    void set(T value);

    T get() throws RuntimePascalException;

    Reference<T> clone();
}
