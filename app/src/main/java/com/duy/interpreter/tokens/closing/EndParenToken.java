package com.duy.interpreter.tokens.closing;

import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.ParenthesizedToken;

public class EndParenToken extends ClosingToken {

    public EndParenToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof ParenthesizedToken ? null
                : new EnumeratedGroupingException(lineInfo,
                grouping_exception_types.MISMATCHED_PARENS);
    }

    public String toCode() {
        return ")";
    }

    @Override
    public String toString() {
        return ")";
    }
}
