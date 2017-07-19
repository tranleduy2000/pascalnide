package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ToToken extends BasicToken {

    public ToToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "to";
    }
}
