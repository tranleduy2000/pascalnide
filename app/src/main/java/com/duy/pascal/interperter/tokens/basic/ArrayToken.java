package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineNumber;

public class ArrayToken extends BasicToken {

    public ArrayToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "array";
    }
}
