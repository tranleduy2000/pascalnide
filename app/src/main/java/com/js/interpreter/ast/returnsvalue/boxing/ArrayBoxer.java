package com.js.interpreter.ast.returnsvalue.boxing;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArgumentType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayBoxer extends DebuggableReturnsValue {
	ReturnsValue[] values;
	ArgumentType type;
	LineInfo line;

	public ArrayBoxer(ReturnsValue[] value, ArgumentType elementType,
			LineInfo line) {
		this.values = value;
		this.type = elementType;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		throw new ParsingException(
				line,
				"Attempted to get type of varargs boxer. This should not happen as we are only supposed to pass varargs to plugins");
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
				values.length);
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].getValue(f, main);
		}
		return result;
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
				values.length);
		for (int i = 0; i < values.length; i++) {
			Object val = values[i].compileTimeValue(context);
			if (val == null) {
				return null;
			} else {
				result[i] = val;
			}
		}
		return result;
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		throw new UnassignableTypeException(this);
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
		ReturnsValue[] val = new ReturnsValue[values.length];
		for (int i = 0; i < values.length; i++) {
			val[i] = values[i].compileTimeExpressionFold(context);
		}
		return new ArrayBoxer(val, type, line);
	}
}
