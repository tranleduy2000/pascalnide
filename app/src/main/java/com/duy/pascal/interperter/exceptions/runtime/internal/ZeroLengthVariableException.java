package com.duy.pascal.interperter.exceptions.runtime.internal;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ZeroLengthVariableException extends InternalInterpreterException {
    public ZeroLengthVariableException(LineInfo line) {
        super(line);
    }

    @Override
    public String getInternalError() {
        return "Variable with no name encountered";
    }
}
