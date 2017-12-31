package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ConstToken extends BasicToken {

    public ConstToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "const";
    }

    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
