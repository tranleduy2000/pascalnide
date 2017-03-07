package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface Executable {
	public LineInfo getLineNumber();

	/*
	 * This should not modify the state of the object, unless it is a plugin
	 * call.
	 */
	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException;

	public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException;
}
