package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

public class NoSuchFunctionOrVariableException extends com.duy.pascal.backend.exceptions.ParsingException {
    public String name, token;

    public NoSuchFunctionOrVariableException(LineInfo line, String token) {
        super(line, token + " is not a variable or function name");
        this.name = token;
    }

}
