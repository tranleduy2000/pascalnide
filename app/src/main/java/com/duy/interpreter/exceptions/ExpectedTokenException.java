package com.duy.interpreter.exceptions;


import com.duy.interpreter.tokens.Token;

public class ExpectedTokenException extends ParsingException {
    public ExpectedTokenException(String token, Token instead) {
        super(instead.lineInfo,
                "Expected the following token to appear: " + token + "  Instead, there was the following token: "
                        + instead);
    }
}
