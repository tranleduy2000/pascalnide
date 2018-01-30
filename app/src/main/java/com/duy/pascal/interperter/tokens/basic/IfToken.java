package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class IfToken extends BasicToken {
    public IfToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "if";
    }
}
