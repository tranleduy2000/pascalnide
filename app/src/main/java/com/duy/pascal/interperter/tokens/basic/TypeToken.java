package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

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
