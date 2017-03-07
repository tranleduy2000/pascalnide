package com.duy.interpreter.exceptions;

import com.duy.interpreter.linenumber.LineInfo;

public class ExpectedAnotherTokenException extends ParsingException {

    public ExpectedAnotherTokenException(LineInfo line) {
        super(line, "Another token is expected before the end of this construct.");
    }
}
