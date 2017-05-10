package com.js.interpreter.runtime.variables;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	Object getVar(String name) throws RuntimePascalException;

	void setVar(String name, Object val);

	ContainsVariables clone();
}
