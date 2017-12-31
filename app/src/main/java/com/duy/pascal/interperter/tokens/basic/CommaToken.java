package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineInfo;

public class CommaToken extends BasicToken {
    public CommaToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ",";
    }
}
