package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

public abstract class DebuggableExecutable implements Executable {

	@Override
	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		try {
			if (main != null) {
				main.scriptControlCheck(getLineNumber());
			}
			return executeImpl(f, main);
		} catch (RuntimePascalException e) {
			throw e;
		} catch (Exception e) {
			throw new UnhandledPascalException(this.getLineNumber(), e);
		}
	}

	public abstract ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException;
}
