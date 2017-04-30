package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class SemicolonToken extends BasicToken {
    public SemicolonToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ";";
    }
}
