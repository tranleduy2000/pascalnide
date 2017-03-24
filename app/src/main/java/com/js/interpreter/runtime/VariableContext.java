package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public abstract class VariableContext implements ContainsVariables {
	public abstract Object getGlobalVariable(String name)
			throws RuntimePascalException;

	protected abstract boolean setLocalVar(String name, Object val);

	@Override
	public Object getLocalVariable(String name) throws RuntimePascalException {
		Object result = this.getGlobalVariable(name);
		VariableContext parentcontext = getParentContext();
		if (result == null && parentcontext != null) {
			result = parentcontext.getLocalVariable(name);
		}
		if (result == null) {
			System.err.println("Warning!  Fetched null variable!");
		}
		return result;
	}

	@Override
	public void setLocalVariable(String name, Object val) {
		if (val == null) {
			System.err.println("Warning!  Setting null variable!");
		}
		if (setLocalVar(name, val)) {
			return;
		}
		VariableContext parentContext = getParentContext();
		if (parentContext != null) {
			parentContext.setLocalVariable(name, val);
		}
	}

	public abstract VariableContext getParentContext();

	@Override
	public VariableContext clone() {
		return null;
	}

}
