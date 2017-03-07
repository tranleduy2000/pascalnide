package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class PeriodToken extends BasicToken {
	public PeriodToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ".";
	}

	@Override
	public precedence getOperatorPrecedence() {
		return precedence.Dereferencing;
	}
}
