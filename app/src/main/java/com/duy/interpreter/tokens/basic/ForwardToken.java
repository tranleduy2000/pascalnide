package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ForwardToken extends BasicToken {

	public ForwardToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "forward";
	}
}
