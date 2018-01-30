package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ColonToken extends BasicToken {
    public ColonToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return ":";
    }
}
