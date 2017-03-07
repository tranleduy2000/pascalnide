package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class DotDotToken extends BasicToken {

	public DotDotToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "..";
	}
}
