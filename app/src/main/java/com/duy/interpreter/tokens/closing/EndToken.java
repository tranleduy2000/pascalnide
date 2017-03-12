package com.duy.interpreter.tokens.closing;

import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.grouping.BeginEndToken;
import com.duy.interpreter.tokens.grouping.CaseToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.RecordToken;

public class EndToken extends ClosingToken {

    public EndToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof BeginEndToken || t instanceof CaseToken
                || t instanceof RecordToken) {
            return null;
        } else {
            return new EnumeratedGroupingException(lineInfo,
                    GroupingExceptionTypes.EXTRA_END);
        }
    }

    @Override
    public String toString() {
        return "end";
    }
}
