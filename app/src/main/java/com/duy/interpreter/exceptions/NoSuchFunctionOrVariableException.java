package com.duy.interpreter.exceptions;

import com.duy.interpreter.linenumber.LineInfo;

public class NoSuchFunctionOrVariableException extends com.duy.interpreter.exceptions.ParsingException {
    public String name, token;

    public NoSuchFunctionOrVariableException(LineInfo line, String token) {
        super(line, token + " is not a variable or function name");
        this.name = token;
    }

}
