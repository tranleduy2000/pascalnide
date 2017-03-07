package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ProcedureToken extends BasicToken {
	public ProcedureToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "procedure";
	}
}
