package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ForToken extends BasicToken {
    public ForToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "for";
    }
}
