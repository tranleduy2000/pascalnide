package com.duy.pascal.backend.tokens.closing;

import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;

public class EndParenToken extends ClosingToken {

    public EndParenToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof ParenthesizedToken ? null
                : new EnumeratedGroupingException(lineInfo,
                GroupingExceptionTypes.MISMATCHED_PARENTHESES);
    }

    public String toCode() {
        return ")";
    }

    @Override
    public String toString() {
        return ")";
    }
}
