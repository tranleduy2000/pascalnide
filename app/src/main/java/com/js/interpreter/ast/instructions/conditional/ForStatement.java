package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.duy.interpreter.tokens.OperatorTypes;

public class ForStatement extends DebuggableExecutable {
	SetValueExecutable setfirst;
	ReturnsValue lessthanlast;
	SetValueExecutable increment_temp;
	Executable command;
	LineInfo line;

	public ForStatement(ExpressionContext f, ReturnsValue temp_var,
			ReturnsValue first, ReturnsValue last, Executable command,
			LineInfo line) throws ParsingException {
		this.line = line;
		setfirst = temp_var.createSetValueInstruction(first);
		lessthanlast = BinaryOperatorEvaluation.generateOp(f, temp_var, last,
				OperatorTypes.LESSEQ, this.line);
		increment_temp = temp_var
				.createSetValueInstruction(BinaryOperatorEvaluation.generateOp(
						f, temp_var, new ConstantAccess(1, this.line),
						OperatorTypes.PLUS, this.line));

		this.command = command;

	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		setfirst.execute(f, main);
		while_loop: while (((Boolean) lessthanlast.getValue(f, main))
				.booleanValue()) {
			switch (command.execute(f, main)) {
			case RETURN:
				return ExecutionResult.RETURN;
			case BREAK:
				break while_loop;
			}
			increment_temp.execute(f, main);
		}
		return ExecutionResult.NONE;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Executable compileTimeConstantTransform(CompileTimeContext c)
			throws ParsingException {
		SetValueExecutable first = setfirst.compileTimeConstantTransform(c);
		SetValueExecutable inc = increment_temp.compileTimeConstantTransform(c);
		Executable comm = command.compileTimeConstantTransform(c);
		ReturnsValue comp = lessthanlast;
		Object val = lessthanlast.compileTimeValue(c);
		if (val != null) {
			if (((Boolean) val)) {
				return first;
			} else {
				comp = new ConstantAccess(val, lessthanlast.getLineNumber());
			}
		}
		return new DowntoForStatement(first, comp, inc, comm, line);
	}
}
