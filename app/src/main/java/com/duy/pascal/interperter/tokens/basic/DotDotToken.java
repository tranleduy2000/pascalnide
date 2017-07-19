package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class DotDotToken extends BasicToken {

    public DotDotToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "..";
    }
}
