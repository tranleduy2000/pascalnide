package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ColonToken extends BasicToken {
	public ColonToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ":";
	}
}
