package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class DowntoToken extends BasicToken {

	public DowntoToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "downto";
	}
}
