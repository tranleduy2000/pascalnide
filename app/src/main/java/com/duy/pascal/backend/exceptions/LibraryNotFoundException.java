package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;

public class LibraryNotFoundException extends ParsingException {
    public String name;

    public LibraryNotFoundException(LineInfo lineInfo, String name) {
        super(lineInfo);
        this.name = name;
    }
}
