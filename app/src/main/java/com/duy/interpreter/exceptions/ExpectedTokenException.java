package com.duy.interpreter.exceptions;


import com.duy.interpreter.tokens.Token;

public class ExpectedTokenException extends ParsingException {
    public String token, instead;

    public ExpectedTokenException(String token, Token instead) {
        super(instead.lineInfo, "Expected the following token to appear: " + token + " " +
                "instead.\nThere was the following token: " + instead);
        this.instead = instead.toString();
        this.token = token;
    }
}
