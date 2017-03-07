package com.js.interpreter.ast.returnsvalue;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArrayType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetArray;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalIndexOutOfBoundsException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayAccess extends DebuggableReturnsValue {
	ReturnsValue container;
	ReturnsValue index;
	int offset;

	public ArrayAccess(ReturnsValue container, ReturnsValue index, int offset) {
		this.container = container;
		this.index = index;
		this.offset = offset;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		RuntimeType r = (container.get_type(f));
		return new RuntimeType(((ArrayType<?>) r.declType).element_type,
				r.writable);
	}

	@Override
	public LineInfo getLineNumber() {
		return index.getLineNumber();
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object cont = container.compileTimeValue(context);
		Object ind = index.compileTimeValue(context);
		if (ind == null || cont == null) {
			return null;
		} else {
			return Array.get(cont, ((Integer) ind) - offset);
		}
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		return new SetArray(container, index, offset, r);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object cont = container.getValue(f, main);
		Integer ind = (Integer) index.getValue(f, main);
		try {
			return Array.get(cont, ind - offset);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new PascalIndexOutOfBoundsException(this.getLineNumber(),
					ind, offset, offset + ((Object[]) cont).length - 1);
		}
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
			throws ParsingException {
		return new ArrayAccess(container.compileTimeExpressionFold(context),
				index.compileTimeExpressionFold(context), offset);
	}

}
