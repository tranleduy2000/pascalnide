package com.js.interpreter.runtime;

import java.util.HashMap;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class FunctionOnStack extends VariableContext {
	public HashMap<String, Object> local_variables = new HashMap<String, Object>();

	public FunctionDeclaration prototype;

	public VariableContext parentContext;

	RuntimeExecutable<?> main;
	@SuppressWarnings("rawtypes")
	HashMap<String, VariableBoxer> reference_variables;

	@SuppressWarnings("rawtypes")
	public FunctionOnStack(VariableContext parentContext,
			RuntimeExecutable<?> main, FunctionDeclaration declaration,
			Object[] arguments) {
		this.prototype = declaration;
		this.parentContext = parentContext;
		this.main = main;
		for (VariableDeclaration v : prototype.declarations.UnitVarDefs) {
			v.initialize(local_variables);
		}
		reference_variables = new HashMap<String, VariableBoxer>();
		for (int i = 0; i < arguments.length; i++) {
			if (prototype.argument_types[i].writable) {
				reference_variables.put(prototype.argument_names[i],
						(VariableBoxer) arguments[i]);
			} else {
				local_variables.put(prototype.argument_names[i], arguments[i]);
			}
		}
		this.parentContext = parentContext;
		this.prototype = declaration;
	}

	public Object execute() throws RuntimePascalException {
		prototype.instructions.execute(this, main);
		return local_variables.get("result");
	}

	@Override
	public Object getLocalVar(String name) throws RuntimePascalException {
		if (local_variables.containsKey(name)) {
			return local_variables.get(name);
		} else if (reference_variables.containsKey(name)) {
			return reference_variables.get(name).get();
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setLocalVar(String name, Object val) {
		if (local_variables.containsKey(name)) {
			local_variables.put(name, val);
		} else if (reference_variables.containsKey(name)) {
			reference_variables.get(name).set(val);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public VariableContext clone() {
		return null;
	}

	@Override
	public VariableContext getParentContext() {
		return parentContext;
	}

}
