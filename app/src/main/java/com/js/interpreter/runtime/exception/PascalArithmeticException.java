package com.js.interpreter.runtime.exception;

import com.duy.interpreter.linenumber.LineInfo;

public class PascalArithmeticException extends RuntimePascalException {
	public ArithmeticException error;

	public PascalArithmeticException(LineInfo line, ArithmeticException e) {
		super(line);
		this.error = e;
	}

	@Override
	public String getMessage() {
		return "Arithmetic Exception: " + error.getMessage(); 
	}
}
