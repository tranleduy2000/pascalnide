package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 06-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class OrdinalExpressionExpectedException extends RuntimePascalException {

    public OrdinalExpressionExpectedException(LineInfo line) {
        super(line);
    }

    public OrdinalExpressionExpectedException() {
    }

    public OrdinalExpressionExpectedException(LineInfo line, String mes) {
        super(line, mes);
    }
}
