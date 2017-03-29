package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StackOverflowException extends RuntimePascalException {

    public StackOverflowException(LineInfo lineInfo) {
        super(lineInfo, "Stack overflow error");
    }

}
