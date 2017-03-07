package com.duy.interpreter.tokens;

import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.closing.ClosingToken;
import com.duy.interpreter.tokens.grouping.BeginEndToken;
import com.duy.interpreter.tokens.grouping.BracketedToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.ParenthesizedToken;

public class EOF_Token extends ClosingToken {
	public EOF_Token(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "EOF";
	}

	@Override
	public GroupingException getClosingException(GrouperToken t) {
		if (t instanceof ParenthesizedToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_PARENS);
		} else if (t instanceof BeginEndToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_BEGIN_END);
		} else if (t instanceof BracketedToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_BRACKETS);
		} else {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_CONSTRUCT);
		}
	}

}
