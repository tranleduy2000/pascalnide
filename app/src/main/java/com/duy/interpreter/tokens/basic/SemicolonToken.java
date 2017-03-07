package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class SemicolonToken extends BasicToken {
	public SemicolonToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ";";
	}
}
