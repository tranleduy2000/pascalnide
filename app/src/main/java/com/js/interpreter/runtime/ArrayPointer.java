package com.js.interpreter.runtime;

import java.lang.reflect.Array;

public class ArrayPointer<T> implements VariableBoxer<T> {
	private final int index;
	private final Object container;

	boolean isString;

	public ArrayPointer(Object container, int index) {
		this.container = container;
		this.index = index;
		isString = container instanceof StringBuilder;
	}

	@Override
	public T get() {
		if (isString) {
			return (T) (Character) ((StringBuilder) container)
					.charAt(index - 1);
		} else {
			return (T) Array.get(container, index);
		}
	}

	@Override
	public void set(T value) {
		if (isString) {
			((StringBuilder) container).setCharAt(index, (Character) value);
		} else {
			Array.set(container, index, value);
		}
	}

	@Override
	public ArrayPointer<T> clone() {
		return new ArrayPointer<T>(container, index);
	}
}
