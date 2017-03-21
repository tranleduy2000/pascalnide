package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ColonToken extends BasicToken {
	public ColonToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ":";
	}
}
