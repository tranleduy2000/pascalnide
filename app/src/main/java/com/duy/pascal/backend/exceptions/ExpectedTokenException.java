package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.tokens.Token;

public class ExpectedTokenException extends ParsingException {
    public String token, instead;

    public ExpectedTokenException(String token, Token instead) {
        super(instead.lineInfo, "Expected the following name to appear: " + token + " " +
                "instead.\nThere was the following name: " + instead);
        this.instead = instead.toString();
        this.token = token;
    }
}
