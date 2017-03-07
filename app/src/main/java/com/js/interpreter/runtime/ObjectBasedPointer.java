package com.js.interpreter.runtime;

public class ObjectBasedPointer<T> implements VariableBoxer<T> {
	public T obj;

	public ObjectBasedPointer(T val) {
		obj = val;
	}

	@Override
	public T get() {
		return obj;
	}

	@Override
	public void set(T value) {
		obj = value;
	}

	@Override
	public ObjectBasedPointer<T> clone() {
		return new ObjectBasedPointer<T>(obj);
	}
}
