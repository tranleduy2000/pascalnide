package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

public abstract class BasicToken extends Token {

	public BasicToken(LineInfo line) {
		super(line);
	}

}
