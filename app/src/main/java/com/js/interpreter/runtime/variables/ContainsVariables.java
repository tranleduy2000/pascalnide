package com.js.interpreter.runtime.variables;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	public Object getLocalVariable(String name) throws RuntimePascalException;

	public void setLocalVariable(String name, Object val);

	public ContainsVariables clone();
}
