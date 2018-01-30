package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ForwardToken extends BasicToken {

    public ForwardToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "forward";
    }
}
