package com.duy.pascal.backend.tokens.basic;


import com.duy.pascal.backend.linenumber.LineInfo;

public class ArrayToken extends BasicToken {

    public ArrayToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "array";
    }
}
