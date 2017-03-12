package com.duy.interpreter.exceptions;

import com.duy.interpreter.tokens.Token;

public class UnrecognizedTokenException extends com.duy.interpreter.exceptions.ParsingException {
    public Token token;

    public UnrecognizedTokenException(Token t) {
        super(t.lineInfo, "The following token doesn't belong here: " + t);
        this.token = t;
    }
}
