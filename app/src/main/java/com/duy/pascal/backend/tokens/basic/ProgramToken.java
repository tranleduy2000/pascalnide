package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ProgramToken extends BasicToken {

	public ProgramToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "program";
	}
}
