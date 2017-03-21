package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ForwardToken extends BasicToken {

	public ForwardToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "forward";
	}
}
