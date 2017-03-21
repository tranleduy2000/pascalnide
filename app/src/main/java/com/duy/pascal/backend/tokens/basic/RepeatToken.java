package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class RepeatToken extends BasicToken {

	public RepeatToken(LineInfo line) {
		super(line);
	}
	@Override
	public String toString() {
		return "repeat";
	}
}
