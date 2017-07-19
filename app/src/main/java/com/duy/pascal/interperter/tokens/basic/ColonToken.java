package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ColonToken extends BasicToken {
    public ColonToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ":";
    }
}
