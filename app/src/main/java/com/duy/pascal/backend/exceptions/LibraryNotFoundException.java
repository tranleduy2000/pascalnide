package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.tokens.Token;

public class LibraryNotFoundException extends ParsingException {
    public String name, instead;

    public LibraryNotFoundException(String name, Token instead) {
        super(instead.lineInfo, "Can not find lib: " + instead.toString());
        this.instead = instead.toString();
        this.name = name;
    }
}
