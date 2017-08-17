package com.duy.pascal.interperter.exceptions.runtime.internal;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class MethodReflectionException extends InternalInterpreterException {
    Exception e;

    public MethodReflectionException(LineInfo line, Exception cause) {
        super(line);
        this.e = cause;
    }

    @Override
    public String getInternalError() {
        return "Attempting to use reflection when: "
                + e.getMessage();
    }
}
