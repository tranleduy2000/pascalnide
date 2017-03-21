package com.js.interpreter.runtime.exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ScriptTerminatedException extends RuntimePascalException {

	public ScriptTerminatedException(LineInfo line) {
		super(line);
	}

	@Override
	public String getMessage() {
		return "Script was manually terminated before it could finish executing";
	}
}
