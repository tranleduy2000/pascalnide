package com.js.interpreter.runtime.exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class UnhandledPascalException extends RuntimePascalException {
    private Exception cause;

	public UnhandledPascalException(LineInfo line, Exception cause) {
		super(line);
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		return "Unhandled exception " + cause;
	}
}
