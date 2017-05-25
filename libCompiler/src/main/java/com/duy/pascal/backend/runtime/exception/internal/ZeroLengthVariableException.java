package com.duy.pascal.backend.runtime.exception.internal;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ZeroLengthVariableException extends InternalInterpreterException {
	public ZeroLengthVariableException(LineInfo line) {
		super(line);
	}

	@Override
	public String getInternalError() {
		return "Variable with no name encountered";
	}
}
