package com.duy.pascal.backend.exceptions.grouping;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;


public class GroupingException extends ParsingException {

    public GroupingException(LineInfo line, String message) {
        super(line, message);
    }

    public GroupingException(LineInfo line) {
        super(line);
    }
}
