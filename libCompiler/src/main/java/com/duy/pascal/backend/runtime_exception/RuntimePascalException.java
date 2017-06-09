package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class RuntimePascalException extends Exception {
    public LineInfo line;

    public RuntimePascalException() {
    }

    public RuntimePascalException(LineInfo line) {
        this.line = line;
    }

    public RuntimePascalException(String ms) {
        super(ms);
    }

    public RuntimePascalException(LineInfo line, String mes) {
        super(mes);
        this.line = line;
    }

    public RuntimePascalException(Exception e) {
    }
}
