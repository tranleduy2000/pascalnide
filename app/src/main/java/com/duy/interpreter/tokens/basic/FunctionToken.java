package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class FunctionToken extends BasicToken {
	public FunctionToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "function";
	}
}
