package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ElseToken extends BasicToken {

    public ElseToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "else";
    }
}
