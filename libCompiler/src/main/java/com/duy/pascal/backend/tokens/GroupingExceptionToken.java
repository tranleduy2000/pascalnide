package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
    public GroupingException exception;

    public GroupingExceptionToken(GroupingException g) {
        super(g.getLineInfo());
        this.exception = g;
    }

    public GroupingExceptionToken(LineInfo line, GroupingException.GroupExceptionType type) {
        super(line);
        this.exception = new GroupingException(line, type);
    }

    @Override
    public String toString() {
        return exception.toString();
    }
}
