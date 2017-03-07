package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class DoToken extends BasicToken {
	public DoToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "do";
	}

	public String toCode(){
		return toString();
	}
}
