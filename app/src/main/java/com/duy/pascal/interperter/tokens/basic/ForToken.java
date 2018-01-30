package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ForToken extends BasicToken {
    public ForToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "for";
    }
}
