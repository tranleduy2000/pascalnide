package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ExpectedAnotherTokenException extends ParsingException {

    public ExpectedAnotherTokenException(LineInfo line) {
        super(line, "Another token is expected before the end of this construct.");
    }
}
