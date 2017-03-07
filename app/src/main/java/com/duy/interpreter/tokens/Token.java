package com.duy.interpreter.tokens;


import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;

public abstract class Token {
	public LineInfo lineInfo;

	public Token(LineInfo line) {
		this.lineInfo = line;
	}

	public WordToken get_word_value() throws ParsingException {
		throw new ExpectedTokenException("[Identifier]", this);
	}

	/**
	 * Null means not an operator
	 * 
	 * @return
	 */
	public precedence getOperatorPrecedence() {
		return null;
	}

	public enum precedence {
		Dereferencing, Negation, Multiplicative, Additive, Relational, NoPrecedence
	};
}
