package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ParsingException;

public interface SetValueExecutable extends Executable {
	public void setAssignedValue(ReturnsValue value);

	@Override
	public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
			throws ParsingException;
}
