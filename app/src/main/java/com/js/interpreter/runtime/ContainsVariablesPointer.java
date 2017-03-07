package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class ContainsVariablesPointer<T> implements VariableBoxer<T> {
	private final ContainsVariables container;
	private final String index;

	public ContainsVariablesPointer(ContainsVariables container, String index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public T get() throws RuntimePascalException {
		return (T) container.get_var(index);
	}

	@Override
	public void set(T value) {
		container.set_var(index, value);
	}

	@Override
	public ContainsVariablesPointer<T> clone() {
		return new ContainsVariablesPointer<T>(container, index);
	}
}
