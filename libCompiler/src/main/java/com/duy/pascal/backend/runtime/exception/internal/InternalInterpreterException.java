package com.duy.pascal.backend.runtime.exception.internal;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class InternalInterpreterException extends RuntimePascalException {
	public InternalInterpreterException(LineInfo line) {
		super(line);
	}

	public String getInternalError() {
		return "Unspecified";
	}

	@Override
	public String getMessage() {
		return "Internal Interpreter Error: " + getInternalError();
	}
}
