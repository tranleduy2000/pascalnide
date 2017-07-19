package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ForToken extends BasicToken {
    public ForToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "for";
    }
}
