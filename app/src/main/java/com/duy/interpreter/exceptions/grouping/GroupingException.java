package com.duy.interpreter.exceptions.grouping;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;


public class GroupingException extends ParsingException {

    public GroupingException(LineInfo line, String message) {
        super(line, message);
    }

    public GroupingException(LineInfo line) {
        super(line);
    }
}
