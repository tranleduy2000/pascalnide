package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineNumber;

public class CommaToken extends BasicToken {
    public CommaToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return ",";
    }
}
