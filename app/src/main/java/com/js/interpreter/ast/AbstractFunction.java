package com.js.interpreter.ast;

import java.util.Iterator;
import java.util.List;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArgumentType;
import com.duy.interpreter.pascaltypes.DeclaredType;

public abstract class AbstractFunction implements NamedEntity {

	@Override
	public abstract String name();

	public abstract ArgumentType[] argumentTypes();

	public abstract DeclaredType return_type();

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(name());
		result.append('(');
		for (ArgumentType c : argumentTypes()) {
			result.append(c);
			result.append(',');
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * 
	 * @param values
	 * @return converted arguments, or null, if they do not fit.
	 * @throws ParsingException
	 */
	public ReturnsValue[] format_args(List<ReturnsValue> values,
			ExpressionContext f) throws ParsingException {
		ArgumentType[] accepted_types = argumentTypes();
		ReturnsValue[] result = new ReturnsValue[accepted_types.length];
		Iterator<ReturnsValue> iterator = values.iterator();
		for (int i = 0; i < accepted_types.length; i++) {
			result[i] = accepted_types[i].convertArgType(iterator, f);
			if (result[i] == null) {/*
									 * This indicates that it cannot fit.
									 */
				return null;
			}
		}
		if (iterator.hasNext()) {
			return null;
		}
		return result;
	}

	public ReturnsValue[] perfectMatch(List<ReturnsValue> args,
			ExpressionContext context) throws ParsingException {
		ArgumentType[] accepted_types = argumentTypes();
		Iterator<ReturnsValue> iterator = args.iterator();
		ReturnsValue[] result = new ReturnsValue[accepted_types.length];
		for (int i = 0; i < accepted_types.length; i++) {
			result[i] = accepted_types[i].perfectFit(iterator, context);
			if (result[i] == null) {
				return null;
			}
		}
		return result;
	}

	public abstract ReturnsValue generatePerfectFitCall(LineInfo line,
			List<ReturnsValue> values, ExpressionContext f)
			throws ParsingException;

	public abstract ReturnsValue generateCall(LineInfo line,
			List<ReturnsValue> values, ExpressionContext f)
			throws ParsingException;

}
