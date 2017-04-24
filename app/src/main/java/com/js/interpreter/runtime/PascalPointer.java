package com.js.interpreter.runtime;

public interface PascalPointer<T> extends Reference<T> {
@Override
PascalPointer<T> clone();
}
