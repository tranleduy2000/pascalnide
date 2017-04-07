package com.js.interpreter.runtime.exception;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 07-Apr-17.
 */

public class InvalidNumericFormatException extends RuntimePascalException {
    public InvalidNumericFormatException(LineInfo line) {
        super(line);
    }

    public InvalidNumericFormatException() {
        super(new LineInfo(-1, "unknown"));
    }

    public InvalidNumericFormatException(LineInfo line, String mes) {
        super(line, mes);
    }
}
