package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Created by Duy on 25-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
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
