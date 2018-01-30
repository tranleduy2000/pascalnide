package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 25-Mar-17.
 */


public class BreakToken extends BasicToken {
    public BreakToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "break";
    }

    public String toCode() {
        return "break";
    }
}
