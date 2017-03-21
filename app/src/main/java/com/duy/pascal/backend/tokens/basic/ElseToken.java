package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ElseToken extends BasicToken {

	public ElseToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "else";
	}
}
