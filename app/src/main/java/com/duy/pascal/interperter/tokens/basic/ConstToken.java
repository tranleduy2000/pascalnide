package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ConstToken extends BasicToken {

    public ConstToken(LineNumber line) {
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
