package com.duy.pascal.backend.tokens.closing;

import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType;
import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType.GroupExceptionType;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
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
                : new GroupingExceptionType(getLineNumber(),
                GroupExceptionType.MISMATCHED_PARENTHESES);
    }

    public String toCode() {
        return ")";
    }

    @Override
    public String toString() {
        return ")";
    }
}
