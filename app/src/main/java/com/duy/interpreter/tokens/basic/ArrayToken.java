package com.duy.interpreter.tokens.basic;


import com.duy.interpreter.linenumber.LineInfo;

public class ArrayToken extends BasicToken {

	public ArrayToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "array";
	}
}
