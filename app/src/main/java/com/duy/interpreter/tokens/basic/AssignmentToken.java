package com.duy.interpreter.tokens.basic;


import com.duy.interpreter.linenumber.LineInfo;

public class AssignmentToken extends BasicToken {
	public AssignmentToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ":=";
	}
}
