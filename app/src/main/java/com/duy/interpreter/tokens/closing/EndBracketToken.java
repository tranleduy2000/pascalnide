package com.duy.interpreter.tokens.closing;

import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.grouping.BracketedToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;

public class EndBracketToken extends ClosingToken {

    public EndBracketToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof BracketedToken ? null
                : new EnumeratedGroupingException(lineInfo,
                        grouping_exception_types.MISMATCHED_BRACKETS);
    }

    @Override
    public String toString() {
        return "]";
    }
}
