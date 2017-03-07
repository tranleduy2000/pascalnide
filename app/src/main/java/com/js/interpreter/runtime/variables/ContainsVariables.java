package com.js.interpreter.runtime.variables;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	public Object get_var(String name) throws RuntimePascalException;

	public void set_var(String name, Object val);

	public ContainsVariables clone();
}
