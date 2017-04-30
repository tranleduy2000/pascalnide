package com.duy.pascal.backend.tokens.basic;


import com.duy.pascal.backend.linenumber.LineInfo;

public class PublicToken extends BasicToken {
    public PublicToken(LineInfo lineinfo) {
        super(lineinfo);
    }

    @Override
    public String toString() {
        return "public";
    }
}
