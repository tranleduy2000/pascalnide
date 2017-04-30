package com.duy.pascal.backend.tokens.basic;


import com.duy.pascal.backend.linenumber.LineInfo;

public class PrivateToken extends BasicToken {
    public PrivateToken(LineInfo lineinfo) {
        super(lineinfo);
    }

    @Override
    public String toString() {
        return "private";
    }
}
