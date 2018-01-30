package com.duy.pascal.interperter.exceptions.runtime;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.utils.DLog;

public class UnhandledPascalException extends RuntimePascalException {
    private Exception cause;

    public UnhandledPascalException(LineNumber line, Exception cause) {
        super(line);
        this.cause = cause;
        DLog.reportException(cause);
    }

    public UnhandledPascalException(LineNumber lineNumber, Throwable e) {
        super(lineNumber);
        DLog.reportException(e);
        this.cause = new RuntimeException(e);
    }

    @Override
    public String getMessage() {
        return String.format("%s Runtime pascal exception.\nCaused by: %s", line, cause.getMessage());
    }

    @Override
    public Exception getCause() {
        return cause;
    }
}
