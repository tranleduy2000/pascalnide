package com.js.interpreter.runtime;


import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface PascalReference<T>{
    void set(T value);

    T get() throws RuntimePascalException;
}