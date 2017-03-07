package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ConstToken extends BasicToken {

	public ConstToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "const";
	}
}
