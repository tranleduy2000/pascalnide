package com.js.interpreter.runtime.exception.internal;

import com.duy.interpreter.linenumber.LineInfo;

public class ZeroLengthVariableException extends InternalInterpreterException {
	public ZeroLengthVariableException(LineInfo line) {
		super(line);
	}

	@Override
	public String getInternalError() {
		return "Variable with no name encountered";
	}
}
