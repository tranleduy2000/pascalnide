package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class FunctionToken extends BasicToken {
    public FunctionToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "function";
    }
}
