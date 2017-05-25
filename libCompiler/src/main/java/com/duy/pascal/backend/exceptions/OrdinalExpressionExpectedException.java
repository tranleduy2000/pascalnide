package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

/**
 * This exception will be thrown if the variable can not working with ...
 *
 * Created by Duy on 06-Apr-17.
 */
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
