package com.duy.pascal.backend.tokens.grouping;


import com.duy.pascal.backend.linenumber.LineInfo;

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
