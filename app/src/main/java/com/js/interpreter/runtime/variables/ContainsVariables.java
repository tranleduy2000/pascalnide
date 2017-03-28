package com.js.interpreter.runtime.variables;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	public Object getVariable(String name) throws RuntimePascalException;

	public void setVariable(String name, Object val);

	public ContainsVariables clone();
}
