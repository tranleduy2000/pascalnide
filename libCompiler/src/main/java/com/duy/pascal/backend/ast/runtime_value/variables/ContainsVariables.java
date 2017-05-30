package com.duy.pascal.backend.ast.runtime_value.variables;

import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
	Object getVar(String name) throws RuntimePascalException;

	void setVar(String name, Object val);

	ContainsVariables clone();
}
