package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class WhileToken extends BasicToken {
    public WhileToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "while";
    }
}
