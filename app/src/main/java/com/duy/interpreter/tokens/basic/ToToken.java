package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ToToken extends BasicToken {

	public ToToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "to";
	}
}
