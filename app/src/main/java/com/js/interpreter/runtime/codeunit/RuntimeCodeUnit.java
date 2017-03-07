package com.js.interpreter.runtime.codeunit;

import java.util.HashMap;
import java.util.Map;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends
		VariableContext {
	Map<String, Object> UnitVariables = new HashMap<String, Object>();

	parent definition;

	public RuntimeCodeUnit(parent definition) {
		this.definition = definition;
		for (VariableDeclaration v : definition.context.UnitVarDefs) {
			v.initialize(UnitVariables);
		}
	}

	public volatile RunMode mode;

	public parent getDefinition() {
		return definition;
	}

	@Override
	public Object getLocalVar(String name) {
		return UnitVariables.get(name);
	}

	@Override
	protected boolean setLocalVar(String name, Object val) {
		return UnitVariables.put(name, val) != null;
	}

}
