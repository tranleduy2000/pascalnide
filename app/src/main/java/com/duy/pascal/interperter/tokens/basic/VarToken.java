package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class VarToken extends BasicToken {
    public VarToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "var";
    }

    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
