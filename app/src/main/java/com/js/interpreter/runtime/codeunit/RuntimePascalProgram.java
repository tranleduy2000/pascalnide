package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalProgram extends RuntimeExecutable<PascalProgram> {

	VariableContext main;

	public RuntimePascalProgram(PascalProgram p) {
		super(p);
	}

	@Override
	public void runImpl() throws RuntimePascalException {
		this.mode = RunMode.running;
		definition.main.execute(this, this);
	}

	@Override
	public VariableContext getParentContext() {
		return null;
	}
}
