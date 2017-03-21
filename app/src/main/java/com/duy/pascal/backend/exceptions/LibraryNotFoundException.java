package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.tokens.Token;

public class LibraryNotFoundException extends ParsingException {
    public String token, instead;

    public LibraryNotFoundException(String token, Token instead) {
        super(instead.lineInfo, "Can not find lib: " + instead.toString());
        this.instead = instead.toString();
        this.token = token;
    }
}
