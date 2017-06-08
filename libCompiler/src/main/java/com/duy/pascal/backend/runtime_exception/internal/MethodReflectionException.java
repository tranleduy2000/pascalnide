package com.duy.pascal.backend.runtime_exception.internal;

import com.duy.pascal.backend.linenumber.LineInfo;

public class MethodReflectionException extends InternalInterpreterException {
	Exception e;

	public MethodReflectionException(LineInfo line, Exception cause) {
		super(line);
		this.e = cause;
	}

	@Override
	public String getInternalError() {
		return "Attempting to use reflection when: "
				+ e.getMessage();
	}
}
