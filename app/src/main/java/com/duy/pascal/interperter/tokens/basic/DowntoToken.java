package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class DowntoToken extends BasicToken {

    public DowntoToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "downto";
    }
}
