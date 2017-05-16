package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.tokens.Token;

public class UnrecognizedTokenException extends com.duy.pascal.backend.exceptions.ParsingException {
    public Token token;

    public UnrecognizedTokenException(Token t) {
        super(t.lineInfo, "The following name doesn't belong here: " + t);
        this.token = t;
    }
}
