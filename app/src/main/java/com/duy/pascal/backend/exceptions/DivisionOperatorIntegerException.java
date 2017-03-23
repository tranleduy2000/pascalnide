package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

/**
 * Created by Duy on 01-Mar-17.
 */

public class DivisionOperatorIntegerException extends InternalInterpreterException {
    public DivisionOperatorIntegerException(LineInfo line) {
        super(line);
    }

    @Override
    public String getInternalError() {
        return "Can not uses / (division) operator with integer";
    }

    @Override
    public String getMessage() {
        return getInternalError();
    }
}
