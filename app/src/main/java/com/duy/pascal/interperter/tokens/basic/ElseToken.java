package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ElseToken extends BasicToken {

    public ElseToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "else";
    }
}
