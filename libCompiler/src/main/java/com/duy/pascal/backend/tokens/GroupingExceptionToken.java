package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
    public GroupingException exception;

    public GroupingExceptionToken(GroupingException g) {
        super(g.line);
        this.exception = g;
    }

    public GroupingExceptionToken(LineInfo line, EnumeratedGroupingException.GroupingExceptionTypes type) {
        super(line);
        this.exception = new EnumeratedGroupingException(line, type);
    }

    @Override
    public String toString() {
        return exception.toString();
    }
}
