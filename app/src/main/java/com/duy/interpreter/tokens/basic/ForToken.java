package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ForToken extends BasicToken {
	public ForToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "for";
	}
}
