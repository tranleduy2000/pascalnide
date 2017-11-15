package com.duy.pascal.interperter.exceptions.runtime.internal;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class MethodReflectionException extends InternalInterpreterException {
    private final Exception cause;

    public MethodReflectionException(LineInfo line, Exception cause) {
        super(line);
        this.cause = cause;
    }

    @Override
    public String getInternalError() {
        return "Attempting to use reflection when: "
                + cause.getMessage();
    }
}
