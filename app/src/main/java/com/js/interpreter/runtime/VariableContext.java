package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public abstract class VariableContext implements ContainsVariables {
	protected abstract Object getLocalVar(String name)
			throws RuntimePascalException;

	protected abstract boolean setLocalVar(String name, Object val);

	@Override
	public Object get_var(String name) throws RuntimePascalException {
		Object result = this.getLocalVar(name);
		VariableContext parentcontext = getParentContext();
		if (result == null && parentcontext != null) {
			result = parentcontext.get_var(name);
		}
		if (result == null) {
			System.err.println("Warning!  Fetched null variable!");
		}
		return result;
	}

	@Override
	public void set_var(String name, Object val) {
		if (val == null) {
			System.err.println("Warning!  Setting null variable!");
		}
		if (setLocalVar(name, val)) {
			return;
		}
		VariableContext parentcontext = getParentContext();
		if (parentcontext != null) {
			parentcontext.set_var(name, val);
		}
	}

	public abstract VariableContext getParentContext();

	@Override
	public VariableContext clone() {
		return null;
	}

}
