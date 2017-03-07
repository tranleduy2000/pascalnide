package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class ThenToken extends BasicToken {
	public ThenToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "then";
	}public String toCode(){
		return toString();
	}
}
