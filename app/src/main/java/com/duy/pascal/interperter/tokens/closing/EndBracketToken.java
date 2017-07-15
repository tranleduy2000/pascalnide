package com.duy.pascal.interperter.tokens.closing;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.grouping.GroupingException;
import com.duy.pascal.interperter.parse_exception.grouping.GroupingException.Type;
import com.duy.pascal.interperter.tokens.grouping.BracketedToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public class EndBracketToken extends ClosingToken {

    public EndBracketToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof BracketedToken ? null
                : new GroupingException(getLineNumber(),
                Type.MISMATCHED_BRACKETS);
    }

    @Override
    public String toString() {
        return "]";
    }
}
