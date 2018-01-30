package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class DowntoToken extends BasicToken {

    public DowntoToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "downto";
    }
}
