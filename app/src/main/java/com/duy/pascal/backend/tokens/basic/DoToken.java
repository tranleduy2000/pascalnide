package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class DoToken extends BasicToken {
    public DoToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "do";
    }

    public String toCode() {
        return toString();
    }
}
