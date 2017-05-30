package com.duy.pascal.backend.tokens.closing;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType;
import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType.GroupExceptionType;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.RecordToken;
import com.duy.pascal.backend.tokens.grouping.UnitToken;

public class EndToken extends ClosingToken {

    public EndToken(LineInfo line) {
        super(line);
    }

    @Override
    @Nullable
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof BeginEndToken
                || t instanceof CaseToken || t instanceof RecordToken
                || t instanceof UnitToken) {
            return null;
        } else {
            return new GroupingExceptionType(getLineNumber(), GroupExceptionType.EXTRA_END);
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
