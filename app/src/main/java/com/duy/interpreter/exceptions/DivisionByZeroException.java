package com.duy.interpreter.exceptions;

import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

/**
 * Created by Duy on 01-Mar-17.
 */

public class DivisionByZeroException extends InternalInterpreterException {
    public DivisionByZeroException(LineInfo line) {
        super(line);
    }

    @Override
    public String getInternalError() {
        return "Division by zero";
    }

    @Override
    public String getMessage() {
        return "Internal Interpreter Error: " + getInternalError();
    }
}
