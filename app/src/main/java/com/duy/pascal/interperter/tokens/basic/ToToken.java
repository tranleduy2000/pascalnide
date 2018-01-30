package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ToToken extends BasicToken {

    public ToToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "to";
    }
}
