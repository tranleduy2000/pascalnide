package com.js.interpreter.runtime;

public interface PascalPointer<T> extends Reference<T> {
@Override
public PascalPointer<T> clone();
}
