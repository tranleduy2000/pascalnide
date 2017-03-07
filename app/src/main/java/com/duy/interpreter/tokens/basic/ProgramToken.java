package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ProgramToken extends BasicToken {

	public ProgramToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "program";
	}
}
