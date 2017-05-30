package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.linenumber.LineInfo;

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
