package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ToToken extends BasicToken {

	public ToToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "to";
	}
}
