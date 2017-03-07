package com.duy.interpreter.tokens;

import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

public class OperatorToken extends Token {
	public OperatorTypes type;

	public OperatorToken(LineInfo line, OperatorTypes t) {
		super(line);
		this.type = t;
	}

	public boolean can_be_unary() {
		switch (type) {
		case MINUS:
		case NOT:
		case PLUS:
			return true;
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return type.toString();
	}

	@Override
	public precedence getOperatorPrecedence() {
		return type.getPrecedence();
	}
}
