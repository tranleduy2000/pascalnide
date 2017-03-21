package com.js.interpreter.runtime.exception.internal;

import com.duy.pascal.backend.linenumber.LineInfo;

public class PluginReflectionException extends InternalInterpreterException {
	Exception e;

	public PluginReflectionException(LineInfo line, Exception cause) {
		super(line);
		this.e = cause;
	}

	@Override
	public String getInternalError() {
		return "Attempting to use reflection when: "
				+ e.getMessage();
	}
}
