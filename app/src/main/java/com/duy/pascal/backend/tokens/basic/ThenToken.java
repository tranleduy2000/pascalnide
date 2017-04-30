package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ThenToken extends BasicToken {
    public ThenToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "then";
    }

    public String toCode() {
        return toString();
    }
}
