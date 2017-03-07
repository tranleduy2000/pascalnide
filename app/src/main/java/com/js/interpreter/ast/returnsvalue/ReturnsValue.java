package com.js.interpreter.ast.returnsvalue;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ReturnsValue {
	public abstract Object getValue(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException;

	public abstract RuntimeType get_type(ExpressionContext f)
			throws ParsingException;

	public abstract LineInfo getLineNumber();

	/*
	 * returns null if not a compile-time constant.
	 */
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException;

	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

	/*
	 * returns null if not a writable value.
	 */
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException;

}
