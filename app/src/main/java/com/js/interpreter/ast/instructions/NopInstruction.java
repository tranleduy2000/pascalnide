package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class NopInstruction extends DebuggableExecutable {
	LineInfo line;

	public NopInstruction(LineInfo line) {
		this.line = line;
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		return ExecutionResult.NONE;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Executable compileTimeConstantTransform(CompileTimeContext c)
			throws ParsingException {
		return this;
	}

}
