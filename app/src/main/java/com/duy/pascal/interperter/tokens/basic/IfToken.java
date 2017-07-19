package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class IfToken extends BasicToken {
    public IfToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "if";
    }
}
