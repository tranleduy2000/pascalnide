package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class OfToken extends BasicToken {

    public OfToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "of";
    }

    public String toCode() {
        return "of";
    }
}
