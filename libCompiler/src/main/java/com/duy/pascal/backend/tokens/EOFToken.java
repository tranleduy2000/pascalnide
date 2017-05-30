package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType;
import com.duy.pascal.backend.parse_exception.grouping.GroupingExceptionType.GroupExceptionType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.closing.ClosingToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;

public class EOFToken extends ClosingToken {
    public EOFToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
//        return Character.valueOf((char) 3).toString();
        return "";
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof ParenthesizedToken) {
            return new GroupingExceptionType(t.getLineNumber(),
                    GroupExceptionType.UNFINISHED_PARENTHESES);
        } else if (t instanceof BeginEndToken) {
            return new GroupingExceptionType(t.getLineNumber(),
                    GroupExceptionType.UNFINISHED_BEGIN_END);
        } else if (t instanceof BracketedToken) {
            return new GroupingExceptionType(t.getLineNumber(),
                    GroupExceptionType.UNFINISHED_BRACKETS);
        } else {
            return new GroupingExceptionType(t.getLineNumber(),
                    GroupExceptionType.UNFINISHED_CONSTRUCT);
        }
    }

}
