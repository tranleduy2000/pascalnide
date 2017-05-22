package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class TypeToken extends BasicToken {
    public TypeToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "type";
    }

    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
