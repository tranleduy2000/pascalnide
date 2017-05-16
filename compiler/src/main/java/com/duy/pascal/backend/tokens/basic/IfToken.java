package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class IfToken extends BasicToken {
    public IfToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "if";
    }
}
