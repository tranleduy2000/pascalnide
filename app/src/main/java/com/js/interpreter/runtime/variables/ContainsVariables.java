package com.js.interpreter.runtime.variables;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	Object get_var(String name) throws RuntimePascalException;

	void set_var(String name, Object val);

	ContainsVariables clone();
}
