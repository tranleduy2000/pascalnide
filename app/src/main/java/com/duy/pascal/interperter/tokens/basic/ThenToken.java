package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ThenToken extends BasicToken {
    public ThenToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "then";
    }

    public String toCode() {
        return toString();
    }
}
