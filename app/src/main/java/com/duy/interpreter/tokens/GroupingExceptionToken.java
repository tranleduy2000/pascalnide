package com.duy.interpreter.tokens;

import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
	public GroupingException exception;

	public GroupingExceptionToken(GroupingException g) {
		super(g.line);
		this.exception = g;
	}

	public GroupingExceptionToken(LineInfo line, EnumeratedGroupingException.grouping_exception_types type) {
		super(line);
		this.exception = new EnumeratedGroupingException(line, type);
	}

	@Override
	public String toString() {
		return exception.toString();
	}
}
