package com.js.interpreter.ast.instructions.case_statement;

import com.duy.interpreter.linenumber.LineInfo;

public class SingleValue implements CaseCondition {
	Object value;
	LineInfo line;

	public SingleValue(Object value, LineInfo line) {
		this.value = value;
		this.line = line;
	}

	@Override
	public boolean fits(Object val) {
		return value.equals(val);
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

}
