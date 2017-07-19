package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineInfo;

public class ArrayToken extends BasicToken {

    public ArrayToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "array";
    }
}
