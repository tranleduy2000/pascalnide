package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Created by Duy on 25-Mar-17.
 */

public class ExitToken extends BasicToken {
    public ExitToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "exit";
    }

    public String toCode() {
        return "exit";
    }
}
