package com.js.interpreter.ast.returnsvalue.cloning;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderCloner implements ReturnsValue {
	ReturnsValue r;

	@Override
	public RuntimeType get_type(ExpressionContext f)
			throws ParsingException {
		return r.get_type(f);
	}

	@Override
	public Object getValue(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		StringBuilder other = (StringBuilder) r.getValue(f, main);
		return new StringBuilder(other);
	}

	@Override
	public LineInfo getLineNumber() {
		return r.getLineNumber();
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		throw new UnassignableTypeException(this);
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object val = r.compileTimeValue(context);
		if (val != null) {
			return new StringBuilder((StringBuilder) val);
		}
		return null;
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
			throws ParsingException {
		return new StringBuilderCloner(r);
	}

	public StringBuilderCloner(ReturnsValue r) {
		this.r = r;
	}
}