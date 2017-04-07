package com.js.interpreter.runtime.exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class RuntimePascalException extends Exception {
    public LineInfo line;

    public RuntimePascalException(LineInfo line) {
        this.line = line;
    }

    public RuntimePascalException(LineInfo line, String mes) {
        super(mes);
        this.line = line;
    }
}
