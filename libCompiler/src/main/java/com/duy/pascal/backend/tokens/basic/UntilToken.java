package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class UntilToken extends BasicToken {

    public UntilToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "until";
    }
}
