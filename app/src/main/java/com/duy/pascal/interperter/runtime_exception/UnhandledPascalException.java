package com.duy.pascal.interperter.runtime_exception;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class UnhandledPascalException extends RuntimePascalException {
    private Exception cause;

    public UnhandledPascalException(LineInfo line, Exception cause) {
        super(line);
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return line + "\nUnhandled exception " + cause;
    }
}
