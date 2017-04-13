package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.closing.ClosingToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;

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
					GroupingExceptionTypes.UNFINISHED_PARENTHESES);
		} else if (t instanceof BeginEndToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					GroupingExceptionTypes.UNFINISHED_BEGIN_END);
		} else if (t instanceof BracketedToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					GroupingExceptionTypes.UNFINISHED_BRACKETS);
		} else {
			return new EnumeratedGroupingException(t.lineInfo,
					GroupingExceptionTypes.UNFINISHED_CONSTRUCT);
		}
	}

}
