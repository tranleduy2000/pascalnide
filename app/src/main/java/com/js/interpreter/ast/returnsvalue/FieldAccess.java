package com.js.interpreter.ast.returnsvalue;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetField;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.duy.interpreter.exceptions.ConstantCalculationException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ObjectType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;
import com.duy.interpreter.tokens.WordToken;

public class FieldAccess extends DebuggableReturnsValue {
	ReturnsValue container;
	String name;
	LineInfo line;

	public FieldAccess(ReturnsValue container, String name, LineInfo line) {
		this.container = container;
		this.name = name;
		this.line = line;
	}

	public FieldAccess(ReturnsValue container, WordToken name) {
		this(container, name.name, name.lineInfo);
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		RuntimeType r = container.get_type(f);
		return new RuntimeType(((ObjectType) (r.declType)).getMemberType(name),
				r.writable);
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object value = container.compileTimeValue(context);
		if (value != null) {
			try {
				return ((ContainsVariables) value).get_var(name);
			} catch (RuntimePascalException e) {
				throw new ConstantCalculationException(e);
			}
		} else {
			return null;
		}
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		return new SetField(container, name, line, r);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value = container.getValue(f, main);
		return ((ContainsVariables) value).get_var(name);
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
			throws ParsingException {
		Object val = this.compileTimeValue(context);
		if (val != null) {
			return new ConstantAccess(val, line);
		} else {
			return new FieldAccess(
					container.compileTimeExpressionFold(context), name, line);
		}
	}
}
