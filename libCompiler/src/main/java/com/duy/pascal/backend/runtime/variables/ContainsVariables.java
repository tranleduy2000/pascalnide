package com.duy.pascal.backend.runtime.variables;

import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	Object getVar(String name) throws RuntimePascalException;

	void setVar(String name, Object val);

	ContainsVariables clone();
}
