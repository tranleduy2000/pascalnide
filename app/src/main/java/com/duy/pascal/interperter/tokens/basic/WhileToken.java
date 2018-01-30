package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class WhileToken extends BasicToken {
    public WhileToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "while";
    }
}
