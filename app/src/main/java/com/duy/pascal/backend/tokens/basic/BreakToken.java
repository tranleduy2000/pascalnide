package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 25-Mar-17.
 */

public class BreakToken extends BasicToken {
    public BreakToken(LineInfo line) {
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
