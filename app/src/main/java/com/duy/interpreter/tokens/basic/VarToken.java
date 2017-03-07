package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class VarToken extends BasicToken {
	public VarToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "var";
	}
}
