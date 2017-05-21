package com.duy.pascal.backend.tokens.closing;

import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.RecordToken;

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
            return new EnumeratedGroupingException(getLineInfo(),
                    GroupingExceptionTypes.EXTRA_END);
        }
    }

    public String toCode() {
        return toString();
    }

    @Override
    public String toString() {
        return "end";
    }
}
