package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RepeatInstruction extends DebuggableExecutable {
	Executable command;

	ReturnsValue condition;
	LineInfo line;

	public RepeatInstruction(Executable command, ReturnsValue condition,
			LineInfo line) {
		this.command = command;
		this.condition = condition;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		do_loop: do {
			switch (command.execute(f, main)) {
			case BREAK:
				break do_loop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		} while (!((Boolean) condition.getValue(f, main)));
		return ExecutionResult.NONE;
	}

	@Override
	public Executable compileTimeConstantTransform(CompileTimeContext c)
			throws ParsingException {
		Object o = condition.compileTimeValue(c);
		if (o != null) {
			Boolean b = (Boolean) o;
			if (!b) {
				return command.compileTimeConstantTransform(c);
			} else {
				return new RepeatInstruction(
						command.compileTimeConstantTransform(c),
						new ConstantAccess(b, condition.getLineNumber()), line);
			}

		}
		return new RepeatInstruction(command.compileTimeConstantTransform(c),
				condition, line);
	}
}
