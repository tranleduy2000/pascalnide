package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class DotDotToken extends BasicToken {

    public DotDotToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "..";
    }
}
