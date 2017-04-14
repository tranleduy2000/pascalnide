package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.tokens.Token;

public class MissingCommaTokenException extends ParsingException {
    private Token token;

    public MissingCommaTokenException(Token instead) {
        super(instead.lineInfo);
        this.token = instead;
    }

    @Override
    public String getMessage() {
        return "Missing semicolon token in line " + line;
    }
}
