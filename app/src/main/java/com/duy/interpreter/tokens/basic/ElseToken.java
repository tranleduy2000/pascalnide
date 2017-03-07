package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ElseToken extends BasicToken {

	public ElseToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "else";
	}
}
