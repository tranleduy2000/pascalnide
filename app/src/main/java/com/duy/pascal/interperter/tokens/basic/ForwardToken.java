package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ForwardToken extends BasicToken {

    public ForwardToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "forward";
    }
}
