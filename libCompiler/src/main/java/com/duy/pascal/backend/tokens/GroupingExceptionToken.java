package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.exceptions.grouping.GroupingExceptionType;
import com.duy.pascal.backend.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
    public GroupingException exception;

    public GroupingExceptionToken(GroupingException g) {
        super(g.lineInfo);
        this.exception = g;
    }

    public GroupingExceptionToken(LineInfo line, GroupingExceptionType.GroupExceptionType type) {
        super(line);
        this.exception = new GroupingExceptionType(line, type);
    }

    @Override
    public String toString() {
        return exception.toString();
    }
}
