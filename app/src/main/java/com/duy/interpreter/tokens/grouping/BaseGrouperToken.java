package com.duy.interpreter.tokens.grouping;


import com.duy.interpreter.linenumber.LineInfo;

public class BaseGrouperToken extends GrouperToken {

	public BaseGrouperToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toCode() {
		return getClosingText();
	}

	@Override
	protected String getClosingText() {
		return "[End of file]";
	}

}
