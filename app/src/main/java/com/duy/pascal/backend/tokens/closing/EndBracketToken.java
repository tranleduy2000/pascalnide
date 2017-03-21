package com.duy.pascal.backend.tokens.closing;

import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

public class EndBracketToken extends ClosingToken {

    public EndBracketToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof BracketedToken ? null
                : new EnumeratedGroupingException(lineInfo,
                        GroupingExceptionTypes.MISMATCHED_BRACKETS);
    }

    @Override
    public String toString() {
        return "]";
    }
}
