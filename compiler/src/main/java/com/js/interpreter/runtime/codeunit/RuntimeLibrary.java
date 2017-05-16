package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.runtime.VariableContext;

public class RuntimeLibrary extends RuntimeCodeUnit<Library> {

	public RuntimeLibrary(Library l) {
		super(l);
	}


	@Override
	public VariableContext getParentContext() {
		return null;
	}

}
