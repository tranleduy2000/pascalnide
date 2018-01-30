package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class DotDotToken extends BasicToken {

    public DotDotToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "..";
    }
}
