package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 26-Feb-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OperationNotSupportedException extends RuntimePascalException {
    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(LineInfo line) {
        super(line);
    }

    public OperationNotSupportedException(LineInfo line, String mes) {
        super(line, mes);
    }

    public OperationNotSupportedException(String mes) {
        super(null, mes);
    }
}
