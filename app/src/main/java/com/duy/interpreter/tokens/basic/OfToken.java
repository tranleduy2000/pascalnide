package com.duy.interpreter.tokens.basic;

import com.duy.interpreter.linenumber.LineInfo;

public class OfToken extends BasicToken {

    public OfToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "of";
    }

    public String toCode() {
        return "of";
    }
}
