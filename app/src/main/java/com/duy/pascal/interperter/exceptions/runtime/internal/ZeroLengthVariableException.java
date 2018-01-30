package com.duy.pascal.interperter.exceptions.runtime.internal;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ZeroLengthVariableException extends InternalInterpreterException {
    public ZeroLengthVariableException(LineNumber line) {
        super(line);
    }

    @Override
    public String getInternalError() {
        return "Variable with no name encountered";
    }
}
