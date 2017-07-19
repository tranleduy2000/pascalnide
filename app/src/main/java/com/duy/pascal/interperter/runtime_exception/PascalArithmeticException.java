package com.duy.pascal.interperter.runtime_exception;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class PascalArithmeticException extends RuntimePascalException {
    public ArithmeticException error;

    public PascalArithmeticException(LineInfo line, ArithmeticException e) {
        super(line);
        this.error = e;
    }

    @Override
    public String getMessage() {
        return "Arithmetic Exception: " + error.getMessage();
    }
}
