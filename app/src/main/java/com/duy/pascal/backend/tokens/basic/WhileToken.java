package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class WhileToken extends BasicToken {
    public WhileToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "while";
    }
}
